package twg2.parser.textFragment;

import java.util.Arrays;
import java.util.List;

/** Represents the location of a fragment of text with absolute and line/column offsets and end indexes
 * @author TeamworkGuy2
 * @since 2015-3-7
 */
public interface TextFragmentRef {

	/**
	 * @return a copy of this text fragment reference
	 */
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


	/** Check if this text fragment contains/encompasses (inclusive) the given text fragment
	 * @param frag the text fragment to compare to
	 * @return true if this text fragment contains/encompasses the given text fragment
	 */
	public default boolean contains(TextFragmentRef frag) {
		return this.getOffsetStart() <= frag.getOffsetStart() &&
			this.getOffsetEnd() >= frag.getOffsetEnd();
	}


	/** Behaves as if calling:<br>
	 * {@code allText.}{@link CharSequence#subSequence(int, int) subSequence}{@code (this.}{@link #getOffsetStart() getOffsetStart()}{@code , this.}{@link #getOffsetEnd() getOffsetEnd()}{@code )}
	 * @param baseOffset represents the offset of {@code allText[0]} within the source document this TextFragmentRef's offsets/indexes are based on.
	 * This offset is subtracted from {@link #getOffsetStart()} to determine where to begin retrieving text from {@code allText}.
	 * @param allText a character sequence containing all of the text from index 0.
	 * @param offset {@code allText} offset (inclusive) at which valid text begins, throw an exception if the fragment starts before this index.
	 * @param length {@code allText} length from offset (includes) of valid text, throw an exception of the fragment ends after this index.
	 * @return the text referenced by this text fragment
	 */
	public CharSequence getText(int baseOffset, char[] allText, int offset, int length);

	/** Behaves as if calling:<br>
	 * {@code allText.}{@link CharSequence#subSequence(int, int) subSequence}{@code (this.}{@link #getOffsetStart() getOffsetStart()}{@code , this.}{@link #getOffsetEnd() getOffsetEnd()}{@code )}
	 * @param allText a character sequence containing all of the text from index 0.
	 * @return the text referenced by this text fragment
	 */
	public CharSequence getText(CharSequence allText);

	/** Extract this text fragment based from a list of strings by treating {@link #getLineStart()} and {@link #getLineEnd()} as
	 * indexes into the string list and {@link #getColumnStart()} and {@link #getColumnEnd()} as offsets within each string.
	 * Extracts and concatenates the resulting sequence of lines.
	 * @param textLines the lines of text to extract from
	 * @return the extracted sub sequence of lines
	 */
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


	static String getText(int baseOffset, char[] chars, int offset, int length, int offsetStart, int offsetEnd) {
		// example for calculating source document vs chars with baseOffset=5 and a fragment
		// doc  0 [ . . . . . . ] 100
		// chars  5 [ ... ] 20
		// frag    6 [..] 12  (len 6, offset in 'chars' = 6 - 5)
		int thisOffset = offsetStart - baseOffset;
		int thisEnd = offsetEnd - baseOffset;
		if(thisOffset < offset) { throw new IndexOutOfBoundsException("text offset " + thisOffset + ", must be " + offset + " or greater"); }
		if(thisEnd > offset + length) { throw new IndexOutOfBoundsException("text end offset " + thisEnd + ", must be " + (offset + length) + " or less"); }
		return new String(chars, thisOffset, offsetEnd - offsetStart);
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


	public static String toStartPositionDisplayText(TextFragmentRef textFrag) {
		return (textFrag.getLineStart() + 1) + ":" + (textFrag.getColumnStart() + 1);
	}


	public static String toEndPositionDisplayText(TextFragmentRef textFrag) {
		return (textFrag.getLineEnd() + 1) + ":" + (textFrag.getColumnEnd() + 1);
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
		return new TextFragmentRefImpl(src.getOffsetStart(), src.getOffsetEnd(), src.getLineStart(), src.getColumnStart(), src.getLineEnd(), src.getColumnEnd());
	}


	static TextFragmentRefImplMut copyMutable(TextFragmentRef src) {
		return new TextFragmentRefImplMut(src.getOffsetStart(), src.getOffsetEnd(), src.getLineStart(), src.getColumnStart(), src.getLineEnd(), src.getColumnEnd());
	}


	/** Create/modify the 'dst' fragment to span from the start to the end fragment.
	 * Differs from {@link #merge(TextFragmentRefImplMut, TextFragmentRef...) merge(...)} by creating a span regardless of whether {@code start} and {@code end} are adjacent/overlapping or not.
	 * NOTE: instead of passing a new empty 'dst' text fragment, null will properly create a new instance.
	 */
	public static TextFragmentRefImplMut span(TextFragmentRefImplMut start, TextFragmentRef end, TextFragmentRefImplMut dst) {
		TextFragmentRefImplMut res = dst != null ? dst : new TextFragmentRefImplMut();
		res.columnStart = start.columnStart;
		res.lineStart = start.lineStart;
		res.offsetStart = start.offsetStart;
		res.columnEnd = end.getColumnEnd();
		res.lineEnd = end.getLineEnd();
		res.offsetEnd = end.getOffsetEnd();
		return res;
	}


	/** Loop through the fragments in iteration order and create a fragment which starts at the first fragment's start offset/line/column and
	 * ends at the offset/line/column of the last fragment which is adjacent-to/overlapping-with the fragment before it.<br>
	 * i.e. once a fragment is reached which is not adjacent-to/overlapping-with the previous fragment, the merge ends.
	 * NOTE: instead of passing a new empty 'dst' text fragment, null will properly create a new instance.
	 */
	static TextFragmentRefImplMut merge(TextFragmentRefImplMut dst, TextFragmentRef... fragments) {
		// sort the fragments by start offset
		Arrays.sort(fragments, (a, b) -> a.getOffsetStart() - b.getOffsetStart());

		return _merge(fragments, dst);
	}


	/** Loop through the fragments and create a fragment which starts at the first fragment's start offset/line/column and
	 * ends at the offset/line/column of the last fragment which is adjacent-to/overlapping-with the fragment before it.<br>
	 * i.e. once a fragment is reached which is not adjacent-to/overlapping-with the previous fragment, the merge ends.
	 */
	static TextFragmentRef merge(TextFragmentRef... fragments) {
		// sort the fragments by start offset
		Arrays.sort(fragments, (a, b) -> a.getOffsetStart() - b.getOffsetStart());

		return _merge(fragments, null);
	}


	/** Loop through the fragments and create a fragment which starts at the first fragment's start offset/line/column and
	 * ends at the offset/line/column of the last fragment which is adjacent-to/overlapping-with the fragment before it.<br>
	 * i.e. once a fragment is reached which is not adjacent-to/overlapping-with the previous fragment, the merge ends.
	 */
	static TextFragmentRefImplMut _merge(TextFragmentRef[] mutableSortedFragments, TextFragmentRefImplMut dstOpt) {
		TextFragmentRefImplMut res = dstOpt != null ? dstOpt : new TextFragmentRefImplMut();
		var firstFragment = mutableSortedFragments[0];
		res.offsetStart = firstFragment.getOffsetStart();
		res.columnStart = firstFragment.getColumnStart();
		res.lineStart = firstFragment.getLineStart();

		int resOffsetEnd = firstFragment.getOffsetEnd();
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
