package twg2.parser.textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.UncheckedIOException;

import twg2.parser.textStream.LineCounter;
import twg2.parser.textStream.StringLineSupplier;

/** A buffered text reader with conditional read methods that reads characters from a char[]
 * and allows any number of characters to be unread back to the beginning of the input.
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
	private int nextLineOffsetAfterRewind = -1;
	private LineCounter lineCtr;


	/** Create a line buffer with a {@code char[]} source.
	 * @param reader the buffered reader to read the lines of text from
	 */
	protected TextCharsParser(char[] input) {
		this(input, 0, input.length);
	}


	/** Create a line buffer with a {@link char[]} source and offset and length.
	 * @param input the raw text data to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	protected TextCharsParser(char[] input, int offset, int length) {
		this.input = input;
		this.inputOff = offset;
		this.inputMaxExclusive = offset + length;
		this.maxReadPos = offset - 1;
		this.curPos = offset - 1;
		this.curLineNum = 0;
		this.lineCtr = new LineCounter(offset);
	}


	@Override
	public LineCounter getLineNumbers() {
		return this.lineCtr;
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
		return this.curPos - this.lineCtr.getLineOffset(this.curLineNum) + 1;
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
		if(this.curPos <= this.inputOff) {
			throw new IndexOutOfBoundsException(createUnreadErrorMsg(2));
		}
		// TODO somewhat messy hack to look back at the previous character and ensure that it's not one of certain chars that never precede numbers
		// (e.g. if an A-Z character precedes a digit, it's not a number, it's part of an identifier)
		this.unread(2);
		this.curPos++;
		char prevCh = this.input[this.curPos];
		this.curPos++;
		this.curLineNum = this.lineCtr.getLineNumber(this.curPos);
		return prevCh;
	}


	@Override
	public char nextChar() {
		int pos = this.curPos + 1;
		if(pos >= this.inputMaxExclusive) {
			throw new IndexOutOfBoundsException("end of buffer reached");
		}
		this.curPos = pos;

		char ch = this.input[pos];
		// only adjust max position and line number when we're reading into new chars (i.e. unread() leftovers being re-read)
		if(pos > this.maxReadPos) {
			this.maxReadPos = pos;
			this.curLineNum = this.lineCtr.read(ch);
		}
		else {
			// if we're re-reading after an unread()
			if(this.nextLineOffsetAfterRewind > -1 && pos >= this.nextLineOffsetAfterRewind) {
				this.curLineNum = this.lineCtr.getLineNumber(pos);
				this.nextLineOffsetAfterRewind = getNextLineOffsetIfRewinded();
			}
		}

		return ch;
	}


	@Override
	public int readCount(int count, Appendable dst) {
		int readI = 0;
		if(dst != null) {
			try {
				while(readI < count && hasNext()) {
					char ch = nextChar();
					dst.append(ch);
					readI++;
				}
			} catch(IOException ioe) {
				throw new UncheckedIOException(ioe);
			}
		}
		else {
			while(readI < count && hasNext()) {
				nextChar();
				readI++;
			}
		}
		return count;
	}


	@Override
	public void skip(int count) {
		if(this.curPos + count > this.inputMaxExclusive) {
			throw new IndexOutOfBoundsException("cannot read past end of input");
		}
		this.curPos += count;
	}


	@Override
	public void unread(int count) {
		if(this.curPos - count < this.inputOff - 1) {
			throw new IndexOutOfBoundsException(createUnreadErrorMsg(count));
		}
		this.curPos -= count;
		this.curLineNum = this.lineCtr.getLineNumber(this.curPos);
		this.nextLineOffsetAfterRewind = getNextLineOffsetIfRewinded();
	}


	@Override
	public void close() {
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
		return TextCharsParser.of(src, 0, src.length());
	}


	public static TextCharsParser of(String src, int off, int len) {
		char[] chs = new char[len];
		src.getChars(off, off + len, chs, 0);
		TextCharsParser lineBuffer = new TextCharsParser(chs, 0, len);
		return lineBuffer;
	}


	public static TextCharsParser of(char[] src) {
		return TextCharsParser.of(src, 0, src.length);
	}


	public static TextCharsParser of(char[] src, int off, int len) {
		TextCharsParser lineBuffer = new TextCharsParser(src, off, len);
		return lineBuffer;
	}


	private final int getNextLineOffsetIfRewinded() {
		int offset = this.lineCtr.lineCount() > this.curLineNum + 1 ? this.lineCtr.getLineOffset(this.curLineNum + 1) : -1;
		return offset;
	}


	private final String createUnreadErrorMsg(int count) {
		return "cannot unread " + count + " from " + this.curPos +
				", offset must remain greater than or equal to -1";
	}

}
