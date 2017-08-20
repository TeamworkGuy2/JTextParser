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
	private IntListSorted lineStartOffsets;
	private int off;
	private int curLineNum;
	private char prevChar;


	/** Create a line counter starting at the first character of the first line
	 * @param startLineOffset absolute start character offset, this is the first line offset
	 */
	public LineCounter(int startLineOffset) {
		this.lineStartOffsets = new IntListSorted();
		this.off = startLineOffset;
		this.curLineNum = 0;
		this.lineStartOffsets.add(startLineOffset);
	}


	/** Create a 0-based line counter given a list of line numbers
	 */
	public LineCounter(Collection<? extends Integer> prevLineStartOffsets) {
		this.lineStartOffsets = IntListSorted.of(prevLineStartOffsets);
		this.off = this.lineStartOffsets.getLast();
		this.curLineNum = this.lineStartOffsets.size();
	}


	/** Checks if (based on previous chars) this is a newline
	 * @return the current line number (0-based)
	 */
	public int read(char ch) {
		if(prevChar == '\n') {
			lineStartOffsets.add(off);
			curLineNum++;
		}
		prevChar = ch;
		off++;
		return curLineNum;
	}


	/**
	 * @param offset the absolute character offset (0-based)
	 * @return the 0-based line number of the line containing the character at {@code offset}
	 */
	public int getLineNumber(int offset) {
		int idx = this.lineStartOffsets.binarySearch(offset);
		idx = idx < 0 ? -idx - 2 : idx;
		return idx;
	}


	/**
	 * @param lineIndex 0-based line number
	 * @return the absolute character offset (0-based) of the first character of line number {@code lineIndex}
	 */
	public int getLineOffset(int lineIndex) {
		return this.lineStartOffsets.get(lineIndex);
	}


	/**
	 * @return the number of lines this line counter has encountered
	 */
	public int size() {
		return this.lineStartOffsets.size();
	}


	/** Indicates that input is complete and if the last char was a line ending, count it, and return the line numbers
	 * @return this line counter's internal list of line number offsets based on the chars passed to {@link #read(char)} and the initial constructor offset.
	 * NOTE: should not modify this list, if you'd like a copy, call {@link IntListSorted#copy()} on the return value
	 */
	public IntListSorted getRawCompletedLineOffsets() {
		if(this.prevChar == '\n') {
			this.lineStartOffsets.add(this.off);
		}
		return this.lineStartOffsets;
	}


	@Override
	public String toString() {
		return "LineCounter: { currentLineNum: " + this.curLineNum + ", lines: " + this.lineStartOffsets + " }";
	}

}
