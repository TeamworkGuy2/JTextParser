package twg2.parser.textFragment;

import java.util.List;

import twg2.parser.textParser.ParserPos;

public class TextFragmentRefImplMut implements TextFragmentRef {
	int offsetStart;
	int offsetEnd;
	int lineStart;
	int columnStart;
	int lineEnd;
	int columnEnd;


	public TextFragmentRefImplMut() {
	}


	public TextFragmentRefImplMut(int offsetStart, int offsetEnd, int lineStart, int columnStart, int lineEnd, int columnEnd) {
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


	public void setOffsetStart(int offsetStart) {
		this.offsetStart = offsetStart;
	}


	@Override
	public int getOffsetEnd() {
		return offsetEnd;
	}


	public void setOffsetEnd(int offsetEnd) {
		this.offsetEnd = offsetEnd;
	}


	@Override
	public int getLineStart() {
		return lineStart;
	}


	public void setLineStart(int lineStart) {
		this.lineStart = lineStart;
	}


	@Override
	public int getColumnStart() {
		return columnStart;
	}


	public void setColumnStart(int columnStart) {
		this.columnStart = columnStart;
	}


	@Override
	public int getLineEnd() {
		return lineEnd;
	}


	public void setLineEnd(int lineEnd) {
		this.lineEnd = lineEnd;
	}


	@Override
	public int getColumnEnd() {
		return columnEnd;
	}


	public void setColumnEnd(int columnEnd) {
		this.columnEnd = columnEnd;
	}


	public void setStart(ParserPos pos) {
		this.offsetStart = pos.getPosition();
		this.lineStart = pos.getLineNumber() - 1;
		this.columnStart = pos.getColumnNumber() - 1; 
	}


	public void setEnd(ParserPos pos) {
		this.offsetEnd = pos.getPosition() + 1;
		this.lineEnd = pos.getLineNumber() - 1;
		this.columnEnd = pos.getColumnNumber() - 1;
	}


	@Override
	public TextFragmentRefImplMut copy() {
		return new TextFragmentRefImplMut(offsetStart, offsetEnd, lineStart, columnStart, lineEnd, columnEnd);
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