package twg2.parser.textFragment;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author TeamworkGuy2
 * @since 2015-3-7
 */
public interface TextFragmentRef {

	public TextFragmentRef copy();

	/**
	 * @return absolute start position offset, inclusive, 0 based
	 */
	public int getOffsetStart();

	/**
	 * @return absolute end position offset, exclusive, 0 based
	 */
	public int getOffsetEnd();

	/**
	 * @return starting row inclusive, 0 based
	 */
	public int getLineStart();

	/**
	 * @return ending row inclusive, 0 based
	 */
	public int getLineEnd();

	/**
	 * @return starting row, starting column, inclusive, 0 based
	 */
	public int getColumnStart();

	/**
	 * @return ending row, ending column, inclusive, 0 based
	 */
	public int getColumnEnd();


	public default boolean contains(TextFragmentRef frag) {
		return this.getOffsetStart() <= frag.getOffsetStart() &&
			this.getOffsetEnd() >= frag.getOffsetEnd();
	}


	/** Behaves as if calling:<br>
	 * {@code allText.}{@link CharSequence#subSequence(int, int) subSequence}{@code (this.}{@link #getOffsetStart() getOffsetStart()}{@code , this.}{@link #getOffsetEnd() getOffsetEnd()}{@code )}
	 * @param allText a character sequence containing all of the text from index 0.
	 * @return the text referenced by this text fragment
	 */
	public CharSequence getText(char[] allText, int offset, int length);

	/** Behaves as if calling:<br>
	 * {@code allText.}{@link CharSequence#subSequence(int, int) subSequence}{@code (this.}{@link #getOffsetStart() getOffsetStart()}{@code , this.}{@link #getOffsetEnd() getOffsetEnd()}{@code )}
	 * @param allText a character sequence containing all of the text from index 0.
	 * @return the text referenced by this text fragment
	 */
	public CharSequence getText(CharSequence allText);

	public CharSequence getText(List<? extends CharSequence> textLines);

	/**
	 * @return combined textual representation of {@link #toString()} and {@link #getText(CharSequence)}
	 */
	public String toString(CharSequence allText);


	@Override
	public boolean equals(Object obj);


	@Override
	public String toString();


	static CharSequence getText(int offsetStart, int offsetEnd, CharSequence chseq) {
		return chseq.subSequence(offsetStart, offsetEnd);
	}


	/**
	 * @param offsetStart - inclusive
	 * @param offsetEnd - exclusive
	 * @param lineStart - inclusive
	 * @param lineEnd - inclusive
	 * @param columnStart - inclusive
	 * @param columnEnd - inclusive
	 * @param lines
	 * @return the text sub-string represented by the offsets, line, and column numbers provided
	 */
	public static CharSequence getText(int offsetStart, int offsetEnd, int lineStart, int lineEnd, int columnStart, int columnEnd, List<? extends CharSequence> lines) {
		StringBuilder sb = new StringBuilder();

		CharSequence ln = lines.get(lineStart);

		sb.append(ln.subSequence(columnStart, lineStart == lineEnd ? Math.min(columnEnd + 1, ln.length()) : ln.length()));

		if(lineEnd > lineStart) {
			for(int i = lineStart + 1; i < lineEnd; i++) {
				ln = lines.get(i);
				sb.append(ln.subSequence(0, ln.length()));
			}

			ln = lines.get(lineEnd);
			sb.append(ln.subSequence(0, Math.min(columnEnd + 1, ln.length())));
		}

		return sb.toString();
	}


	static String _toString(int offsetStart, int offsetEnd, int lineStart, int lineEnd, int columnStart, int columnEnd, CharSequence chseq) {
		return "TextFragmentRef: { off: " + offsetStart + ", len: " + (offsetEnd - offsetStart) +
				", start: [" + lineStart + ":" + columnStart + "], end: [" + lineEnd + ":" + columnEnd + "], " +
				"text: \"" + getText(offsetStart, offsetEnd, chseq) + "\" }";
	}


	static String _toString(int offsetStart, int offsetEnd, int lineStart, int lineEnd, int columnStart, int columnEnd) {
		return "TextFragmentRef: { off: " + offsetStart + ", len: " + (offsetEnd - offsetStart) +
				", start: [" + lineStart + ":" + columnStart + "], end: [" + lineEnd + ":" + columnEnd + "]" +
				" }";
	}


	static TextFragmentRefImpl copy(TextFragmentRef src) {
		TextFragmentRefImpl copy = new TextFragmentRefImpl(src.getOffsetStart(), src.getOffsetEnd(), src.getLineStart(), src.getColumnStart(), src.getLineEnd(), src.getColumnEnd());
		return copy;
	}


	static TextFragmentRefImplMut copyMutable(TextFragmentRef src) {
		TextFragmentRefImplMut copy = new TextFragmentRefImplMut(src.getOffsetStart(), src.getOffsetEnd(), src.getLineStart(), src.getColumnStart(), src.getLineEnd(), src.getColumnEnd());
		return copy;
	}


	static TextFragmentRefImplMut merge(TextFragmentRefImplMut dst, TextFragmentRef... fragments) {
		// sort the fragments by start offset
		Arrays.sort(fragments, (a, b) -> a.getOffsetStart() - b.getOffsetStart());

		return _merge(fragments, dst);
	}


	static TextFragmentRef merge(TextFragmentRef... fragments) {
		// sort the fragments by start offset
		Arrays.sort(fragments, (a, b) -> a.getOffsetStart() - b.getOffsetStart());

		return _merge(fragments, null);
	}


	static TextFragmentRef merge(Collection<? extends TextFragmentRef> fragments) {
		// sort the fragments by start offset
		TextFragmentRef[] fragmentsAry = fragments.toArray(new TextFragmentRef[fragments.size()]);
		Arrays.sort(fragmentsAry, (a, b) -> a.getOffsetStart() - b.getOffsetStart());

		return _merge(fragmentsAry, null);
	}


	static TextFragmentRefImplMut _merge(TextFragmentRef[] mutableSortedFragments, TextFragmentRefImplMut dstOpt) {
		TextFragmentRefImplMut res = dstOpt != null ? dstOpt : copyMutable(mutableSortedFragments[0]);
		int resOffsetEnd = res.offsetEnd;
		int resLineEnd = 0;
		int resColumnEnd = 0;

		// loop over each fragment (note the fragment list is sorted) and set the ending offset/line/column equal to the fragment's
		for(TextFragmentRef frag : mutableSortedFragments) {
			// stop if a fragment is not adjacent to the previous fragment, + 1 because indices are inclusive
			if(frag.getOffsetStart() > resOffsetEnd + 1) {
				break;
			}
			resOffsetEnd = frag.getOffsetEnd();
			resLineEnd = frag.getLineEnd();
			resColumnEnd = frag.getColumnEnd();
		}

		res.offsetEnd = resOffsetEnd;
		res.lineEnd = resLineEnd;
		res.columnEnd = resColumnEnd;
		return res;
	}

}
