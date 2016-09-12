package twg2.parser.textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import twg2.parser.textStream.StringLineSupplier;
import twg2.streams.EnhancedIterator;
import twg2.streams.PeekableIterator;

/** A buffered text reader with conditional read methods that reads lines of text and allows the current line
 * to be fully or partially unread.
 * @see PushbackReader
 * @see BufferedReader
 * @author TeamworkGuy2
 * @since 2013-12-10
 */
public final class TextIteratorParser implements TextParserConditionalsDefault, TextParser, Closeable {
	private PeekableIterator<Object> in;
	private boolean inReturnsCharArray;
	private char[] curLineChars;
	private char[] nextLineChars;
	private int previousLinesOffset;
	private int lineNum;
	private int offset = -1;
	private boolean started = false;
	private boolean returned;
	//private SlidingStringView textBuf;


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the buffered reader to read the lines of text from
	 */
	protected TextIteratorParser(PeekableIterator<? extends Object> reader, boolean inReturnsCharArray) {
		this(reader, inReturnsCharArray, 1);
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the buffered reader to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	@SuppressWarnings("unchecked")
	protected TextIteratorParser(PeekableIterator<? extends Object> reader, boolean inReturnsCharArray, int lastLineNum) {
		this.in = (PeekableIterator<Object>)reader;
		this.inReturnsCharArray = inReturnsCharArray;
		this.lineNum = lastLineNum;
		//this.textBuf = new SlidingStringView(4096);
	}


	@Override
	public int getPosition() {
		return previousLinesOffset + offset;
	}


	@Override
	public int getLineNumber() {
		return lineNum;
	}


	@Override
	public int getColumnNumber() {
		return offset + 1;
	}


	// TODO deprecating @Override
	public int getLineRemaining() {
		// - 1 because nextChar() returns the next offset, so only (remaining - 1) characters can be read
		int remaining = this.curLineChars.length - this.offset - 1;
		return remaining;
	}


	@Override
	public boolean hasNext() {
		char[] chs;
		return hasNextChar() || (chs = peekNextLine()) != null && chs.length > 0;
	}


	public boolean hasNextChar() {
		return this.curLineChars != null ? this.curLineChars.length - this.offset > 1 : false;
	}


	@Override
	public boolean hasPrevChar() {
		// TODO this.offset should be using getPosition(), but buffer doesn't allow unread() into previous lines
		return this.offset > 0;
	}


	@Override
	public char prevChar() {
		// TODO this.offset should be using getPosition(), but buffer doesn't allow unread() into previous lines
		if(this.offset < 1) { throw new IndexOutOfBoundsException(createUnreadErrorMsg(2)); }
		// TODO somewhat messy hack to look back at the previous character and ensure that it's not one of certain chars that never precede numbers
		// (e.g. if an A-Z character preceds a digit, it's not a number, it's part of an identifier)
		this.unread(2);
		this.offset++;
		char prevCh = this.curLineChars[this.offset];
		this.offset++;
		return prevCh;
	}


	/** Do not edit the returned char[]
	 */
	protected char[] peekNextLine() {
		boolean firstCheck = !started;
		if(firstCheck) {
			nextLine();
			unread(0);
		}
		return (firstCheck ? curLineChars : nextLineChars);
	}


	@Override
	public char nextChar() {
		if(hasNextChar()) {
			char nextChar = advanceToNextChar();
			return nextChar;
		}
		else {
			while(peekNextLine() != null) {
				nextLine();
				if(hasNextChar()) {
					char nextChar = advanceToNextChar();
					return nextChar;
				}
			}
			throw new IndexOutOfBoundsException("end of buffer reached");
		}
	}


	@Override
	public int readCount(int count, Appendable dst) {
		int readI = 0;
		if(dst != null) {
			try {
				while(readI < count && hasNextChar()) {
					char ch = advanceToNextChar();
					dst.append(ch);
					readI++;
				}
			} catch(IOException ioe) {
				throw new UncheckedIOException(ioe);
			}
		}
		else {
			while(readI < count && hasNextChar()) {
				advanceToNextChar();
				readI++;
			}
		}
		return count;
	}


	@Override
	public void skip(int count) {
		if(offset + count > curLineChars.length) { throw new IndexOutOfBoundsException(createEndOfLineErrorMsg()); }
		offset += count;
	}


	@Override
	public void unread(int count) {
		if(offset - count < -1) {
			throw new IndexOutOfBoundsException(createUnreadErrorMsg(count));
		}
		offset -= count;
		returned = true;
	}


	@Override
	public void close() {
		if(in instanceof AutoCloseable) {
			try {
				((AutoCloseable)in).close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}


	/** Read the next available line. Note that the beginning of the line may not be the first character,
	 * call {@link #getLineOffset()} to get the index into the returned character array at which the line starts
	 * @return the line read from the input stream (this may be a new line, a returned line, or a partial line),
	 * or null if the end of the stream has been reached.
	 * @throws IOException if there is an error reading the line from the input stream
	 * @see #getLineOffset()
	 */
	private char[] nextLine() {
		char[] wasCurrentLine = this.curLineChars;

		if(this.returned) {
			this.returned = false;
			return this.curLineChars;
		}

		if(!this.started) {
			Object line = this.in.hasNext() ? this.in.next() : null;
			if(line != null) {
				//this.textBuf.append(line);
			}
			this.curLineChars = line != null ? (this.inReturnsCharArray ? (char[])line : ((String)line).toCharArray()) : null;
		}
		else {
			this.curLineChars = this.nextLineChars;
			if(this.curLineChars != null) {
				this.lineNum++;
			}
		}

		Object nextLine = this.in.hasNext() ? this.in.next() : null;
		this.nextLineChars = nextLine != null ? (this.inReturnsCharArray ? (char[])nextLine : ((String)nextLine).toCharArray()) : null;
		if(this.nextLineChars != null) {
			//this.textBuf.append(this.nextLineChars);
		}

		this.started = true;
		this.offset = -1; // adjust offset so nextChar() returns the first char the first time it is called
		this.previousLinesOffset += wasCurrentLine != null ? wasCurrentLine.length : 0;
		return this.curLineChars;
	}


	private final char advanceToNextChar() {
		offset++;
		if(offset >= curLineChars.length) {
			throw new IndexOutOfBoundsException(createEndOfLineErrorMsg());
		}
		return curLineChars[offset];
	}


	// package-private
	static char requireBmpChar(int codePoint) {
		if(!Character.isBmpCodePoint(codePoint)) {
			throw new IllegalArgumentException("character required, " + codePoint + " is not a valid 16-bit character, unicode 32-bit is not supported");
		}
		return (char)codePoint;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 */
	public static TextIteratorParser fromStrings(PeekableIterator<String> reader) {
		TextIteratorParser impl = new TextIteratorParser(reader, false);
		return impl;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	public static TextIteratorParser fromStrings(PeekableIterator<String> reader, int lastLineNum) {
		TextIteratorParser impl = new TextIteratorParser(reader, false, lastLineNum);
		return impl;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 */
	public static TextIteratorParser fromCharArrays(PeekableIterator<char[]> reader) {
		TextIteratorParser impl = new TextIteratorParser(reader, true);
		return impl;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	public static TextIteratorParser fromCharArrays(PeekableIterator<char[]> reader, int lastLineNum) {
		TextIteratorParser impl = new TextIteratorParser(reader, true, lastLineNum);
		return impl;
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
	public static TextIteratorParser of(String src) {
		return TextIteratorParser.of(src, 0, src.length(), true, true, true, true);
	}


	public static TextIteratorParser of(String src, int off, int len) {
		return of(src, off, len, true, true, true, true);
	}


	public static TextIteratorParser of(String src, int off, int len, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		//BufferedReader reader = new BufferedReader(new StringReader(src));
		Supplier<String> lines = new StringLineSupplier(src, off, len, treatEmptyLineAsLine, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, collapseNewlinesIntoOneChar);
		EnhancedIterator<String> lineReader = new EnhancedIterator<>(lines, null);
		TextIteratorParser lineBuffer = new TextIteratorParser(lineReader, false);

		return lineBuffer;
	}


	private final String createUnreadErrorMsg(int count) {
		return "cannot unread " + count + " from " + this.offset +
				", offset must remain greater than or equal to -1";
	}


	private final String createEndOfLineErrorMsg() {
		return "end of line, must read next line";
	}

}