package twg2.parser.textFragment;

import java.util.List;

public class TextFragmentRefImpl implements TextFragmentRef {
	final int offsetStart;
	final int offsetEnd;
	final int lineStart;
	final int columnStart;
	final int lineEnd;
	final int columnEnd;


	/**
	 * @param offsetStart absolute start position offset, inclusive, 0 based
	 * @param offsetEnd absolute end position offset, exclusive, 0 based
	 * @param lineStart starting row inclusive, 0 based
	 * @param columnStart starting row, starting column, inclusive, 0 based
	 * @param lineEnd ending row inclusive, 0 based
	 * @param columnEnd ending row, ending column, inclusive, 0 based
	 */
	public TextFragmentRefImpl(int offsetStart, int offsetEnd, int lineStart, int columnStart, int lineEnd, int columnEnd) {
		this.offsetStart = offsetStart;
		this.offsetEnd = offsetEnd;
		this.lineStart = lineStart;
		this.columnStart = columnStart;
		this.lineEnd = lineEnd;
		this.columnEnd = columnEnd;
	}


	@Override
	public int getOffsetStart() {
		return offsetStart;
	}


	@Override
	public int getOffsetEnd() {
		return offsetEnd;
	}


	@Override
	public int getLineStart() {
		return lineStart;
	}


	@Override
	public int getColumnStart() {
		return columnStart;
	}


	@Override
	public int getLineEnd() {
		return lineEnd;
	}


	@Override
	public int getColumnEnd() {
		return columnEnd;
	}


	@Override
	public TextFragmentRefImpl copy() {
		TextFragmentRefImpl copy = new TextFragmentRefImpl(offsetStart, offsetEnd, lineStart, columnStart, lineEnd, columnEnd);
		return copy;
	}


	@Override
	public CharSequence getText(int baseOffset, char[] chars, int offset, int length) {
		return TextFragmentRef.getText(baseOffset, chars, offset, length, offsetStart, offsetEnd);
	}


	@Override
	public CharSequence getText(CharSequence chseq) {
		return chseq.subSequence(offsetStart, offsetEnd);
	}


	@Override
	public CharSequence getText(List<? extends CharSequence> lines) {
		return TextFragmentRef.getText(offsetStart, offsetEnd, lineStart, lineEnd, columnStart, columnEnd, lines);
	}


	@Override
	public String toString(CharSequence chseq) {
		return TextFragmentRef._toString(offsetStart, offsetEnd, lineStart, lineEnd, columnStart, columnEnd, chseq);
	}


	@Override
	public String toString() {
		return TextFragmentRef._toString(offsetStart, offsetEnd, lineStart, lineEnd, columnStart, columnEnd);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnEnd;
		result = prime * result + columnStart;
		result = prime * result + lineEnd;
		result = prime * result + lineStart;
		result = prime * result + offsetEnd;
		result = prime * result + offsetStart;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TextFragmentRef)) { return false; }
		TextFragmentRef frag = (TextFragmentRef)obj;
		return this.offsetStart == frag.getOffsetStart() &&
			this.offsetEnd == frag.getOffsetEnd() &&
			this.columnStart == frag.getColumnStart() &&
			this.columnEnd == frag.getColumnEnd() &&
			this.lineStart == frag.getLineStart() &&
			this.lineEnd == frag.getLineEnd();
	}

}