package twg2.parser.textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.UncheckedIOException;

import twg2.parser.textStream.LineCounter;
import twg2.parser.textStream.StringLineSupplier;
import twg2.streams.PeekableIterator;

/** A buffered text reader with conditional read methods that reads characters from a char[]
 * and allows any number of characters back to the beginning of the input to be unread.
 * @see PushbackReader
 * @see BufferedReader
 * @author TeamworkGuy2
 * @since 2013-12-10
 */
public final class TextCharsParser implements TextParserConditionalsDefault, TextParser, Closeable {
	private char[] input;
	private int inputOff;
	private int inputMaxExclusive;
	private int maxReadPos;
	private int curPos;
	private int curLineNum;
	private int nextLineOffsetIfRewinded = -1;
	private LineCounter lines;


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the buffered reader to read the lines of text from
	 */
	protected TextCharsParser(char[] input) {
		this(input, 0, input.length);
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the buffered reader to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	protected TextCharsParser(char[] input, int offset, int length) {
		this.input = input;
		this.inputOff = offset;
		this.inputMaxExclusive = offset + length;
		this.maxReadPos = offset - 1;
		this.curPos = offset - 1;
		this.curLineNum = 0;
		this.lines = new LineCounter(offset);
	}


	public LineCounter getLineNumbers() {
		return this.lines;
	}


	@Override
	public int getPosition() {
		return this.curPos;
	}


	@Override
	public int getLineNumber() {
		return this.curLineNum + 1;
	}


	@Override
	public int getColumnNumber() {
		return this.curPos - this.lines.getLineOffset(this.curLineNum) + 1;
	}


	@Override
	public boolean hasNext() {
		return this.curPos + 1 < this.inputMaxExclusive;
	}


	@Override
	public boolean hasPrevChar() {
		return this.curPos > this.inputOff;
	}


	@Override
	public char prevChar() {
		if(this.curPos <= this.inputOff) { throw new IndexOutOfBoundsException(createUnreadErrorMsg(2)); }
		// TODO somewhat messy hack to look back at the previous character and ensure that it's not one of certain chars that never precede numbers
		// (e.g. if an A-Z character preceeds a digit, it's not a number, it's part of an identifier)
		this.unread(2);
		this.curPos++;
		char prevCh = this.input[this.curPos];
		this.curPos++;
		this.curLineNum = this.lines.getLineNumber(this.curPos);
		return prevCh;
	}


	@Override
	public char nextChar() {
		if(hasNext()) {
			char nextChar = advanceToNextChar();
			return nextChar;
		}
		throw new IndexOutOfBoundsException("end of buffer reached");
	}


	@Override
	public int readCount(int count, Appendable dst) {
		int readI = 0;
		if(dst != null) {
			try {
				while(readI < count && hasNext()) {
					char ch = advanceToNextChar();
					dst.append(ch);
					readI++;
				}
			} catch(IOException ioe) {
				throw new UncheckedIOException(ioe);
			}
		}
		else {
			while(readI < count && hasNext()) {
				advanceToNextChar();
				readI++;
			}
		}
		return count;
	}


	@Override
	public void skip(int count) {
		if(this.curPos + count > this.inputMaxExclusive) { throw new IndexOutOfBoundsException(createEndOfInputErrorMsg()); }
		this.curPos += count;
	}


	@Override
	public void unread(int count) {
		if(this.curPos - count < this.inputOff - 1) {
			throw new IndexOutOfBoundsException(createUnreadErrorMsg(count));
		}
		this.curPos -= count;
		this.curLineNum = this.lines.getLineNumber(this.curPos);
		this.nextLineOffsetIfRewinded = getNextLineOffsetIfRewinded();
	}


	@Override
	public void close() {
	}


	private final char advanceToNextChar() {
		this.curPos++;
		if(this.curPos >= inputMaxExclusive) {
			throw new IndexOutOfBoundsException(createEndOfInputErrorMsg());
		}
		char ch = this.input[this.curPos];
		// only adjust max position and line number when we're reading into new chars (i.e. unread() leftovers being re-read)
		if(this.curPos > this.maxReadPos) {
			this.maxReadPos = this.curPos;
			this.curLineNum = this.lines.apply(ch);
		}
		else {
			// if we're re-reading after an unread()
			if(this.nextLineOffsetIfRewinded > -1 && this.curPos >= this.nextLineOffsetIfRewinded) {
				this.curLineNum = this.lines.getLineNumber(this.curPos);
				this.nextLineOffsetIfRewinded = getNextLineOffsetIfRewinded();
			}
		}

		return ch;
	}


	/** Create a {@link TextParser} from a string.<br>
	 * Implementation note: if the string ends with a newline, the empty string between
	 * the last character and the end of the string is converted to an empty line.<br>
	 * For example, the string {@code "a\nb\nc\n"} is only 3 lines, whereas {@code "a\nb\nc\nd"} is 4 lines
	 * even though both strings have the same number of newlines.<br>
	 * @param src the string
	 * @return a line buffer that reads the contents of the string, line
	 * by line if the string contains line separators
	 * @see StringLineSupplier
	 */
	public static TextCharsParser of(String src) {
		return TextCharsParser.of(src, 0, src.length(), true, true, true);
	}


	public static TextCharsParser of(String src, int off, int len, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		char[] chs = new char[len];
		src.getChars(off, off + len, chs, 0);
		TextCharsParser lineBuffer = new TextCharsParser(chs, 0, len);
		return lineBuffer;
	}


	public static TextCharsParser of(char[] src) {
		return TextCharsParser.of(src, 0, src.length, true, true, true);
	}


	public static TextCharsParser of(char[] src, int off, int len, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		TextCharsParser lineBuffer = new TextCharsParser(src, off, len);
		return lineBuffer;
	}


	private final int getNextLineOffsetIfRewinded() {
		int offset = this.lines.size() > this.curLineNum + 1 ? this.lines.getLineOffset(this.curLineNum + 1) : -1;
		return offset;
	}


	private final String createUnreadErrorMsg(int count) {
		return "cannot unread " + count + " from " + this.curPos +
				", offset must remain greater than or equal to -1";
	}


	private final String createEndOfInputErrorMsg() {
		return "cannot read past end of input";
	}

}
