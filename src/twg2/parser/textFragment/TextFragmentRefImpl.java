package twg2.parser.textFragment;

import java.util.List;

public class TextFragmentRefImpl implements TextFragmentRef {
	private final int offsetStart;
	private final int offsetEnd;
	private final int lineStart;
	private final int columnStart;
	private final int lineEnd;
	private final int columnEnd;


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
	public CharSequence getText(char[] chars, int offset, int length) {
		if(offsetStart < offset) { throw new IndexOutOfBoundsException("offset " + offsetStart + ", expected " + offset + " or greater"); }
		if(offsetEnd > offset + length) { throw new IndexOutOfBoundsException("end offset " + offsetEnd + ", expected " + (offset + length) + " or less"); }
		return new String(chars, offsetStart, offsetEnd - offsetStart);
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