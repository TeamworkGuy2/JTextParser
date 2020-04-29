package twg2.parser.textStream;

import java.util.Collection;

import twg2.collections.primitiveCollections.IntListSorted;

/** A line counter which accepts characters and keeps a running list of line numbers.<br>
 * Lines start at the index of the first character after a '\n'.
 * Offsets and line numbers are all 0-based indices.
 * This class is stateful and new line numbers are added as additional characters are feed to {@link #read(char)}.
 * @author TeamworkGuy2
 * @since 2016-09-10
 */
public class LineCounter {
	private IntListSorted lineOffsets;
	private int curOffset;
	private int maxReadPos;
	private int curLineNum;


	/** Create a line counter starting at the first character of the first line
	 * @param startLineOffset absolute start character offset, this is the first line offset
	 */
	public LineCounter(int startLineOffset) {
		this.lineOffsets = new IntListSorted();
		this.curOffset = startLineOffset;
		this.maxReadPos = startLineOffset;
		this.curLineNum = 0;
		this.lineOffsets.add(startLineOffset);
	}


	/** Create a 0-based line counter given a list of line numbers
	 */
	public LineCounter(Collection<? extends Integer> prevLineStartOffsets) {
		this.lineOffsets = IntListSorted.of(prevLineStartOffsets);
		this.curOffset = this.lineOffsets.size() > 0 ? this.lineOffsets.getLast() : 0;
		this.maxReadPos = this.curOffset;
		this.curLineNum = this.lineOffsets.size() > 0 ? this.lineOffsets.size() - 1 : 0;
	}


	/** Checks if (based on previous chars) this is a newline
	 * @return the current line number (0-based)
	 */
	public int read(char ch) {
		// reading new characters
		if(curOffset >= maxReadPos) {
			curOffset++;
			maxReadPos++;
			if(ch == '\n') {
				lineOffsets.add(curOffset);
				curLineNum++;
			}
		}
		// re-reading characters after unread()
		else {
			curOffset++;
			int nextLineOffset = lineOffsets.size() > curLineNum + 1 ? lineOffsets.get(curLineNum + 1) : -1;
			if(nextLineOffset > -1 && curOffset >= nextLineOffset) {
				curLineNum++;
			}
		}
		return curLineNum;
	}


	/** Unread/rewind the specified number of characters.
	 * Adjust line number and offset so that calls to {@link #getLineNumber()} reflect the new position.
	 * @param count the positive number of characters to unread
	 */
	public void unread(int count) {
		if(this.curOffset - count < 0) { throw new IndexOutOfBoundsException("cannot unread " + count + " from " + this.curOffset + ", offset must remain greater than or equal to 0"); }
		this.curOffset -= count;
		this.curLineNum = getLineNumber(this.curOffset);
	}


	/** Get the current 0-based line number
	 * @return 0-based line number
	 */
	public int getLineNumber() {
		return this.curLineNum;
	}


	/**
	 * @param offset the absolute character offset (0-based)
	 * @return the 0-based line number of the line containing the character at {@code offset}
	 */
	public int getLineNumber(int offset) {
		int idx = this.lineOffsets.binarySearch(offset);
		idx = idx < 0 ? -idx - 2 : idx;
		return idx;
	}


	/**
	 * @param lineIndex 0-based line number
	 * @return the absolute character offset (0-based) of the first character of line number {@code lineIndex}
	 */
	public int getLineOffset(int lineIndex) {
		return this.lineOffsets.get(lineIndex);
	}


	/**
	 * @return the number of lines this line counter has encountered
	 */
	public int lineCount() {
		return this.lineOffsets.size();
	}


	/** Indicates that input is complete and if the last char was a line ending, count it, and return the line numbers
	 * @return this line counter's internal list of line number offsets based on the chars passed to {@link #read(char)} and the initial constructor offset.
	 * NOTE: you should not modify the returned list, if you'd like a copy, call {@link IntListSorted#copy()} on the return value
	 */
	public IntListSorted getRawCompletedLineOffsets() {
		return this.lineOffsets;
	}


	@Override
	public String toString() {
		return "LineCounter: { currentLineNum: " + this.curLineNum + ", lines: " + this.lineOffsets + " }";
	}

}
