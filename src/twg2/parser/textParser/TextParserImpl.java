package twg2.parser.textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import twg2.arrays.ArrayUtil;
import twg2.functions.Predicates;
import twg2.parser.textStream.StringLineSupplier;
import twg2.ranges.CharSearcher;
import twg2.ranges.helpers.CharCategory;
import twg2.streams.EnhancedIterator;
import twg2.streams.PeekableIterator;

/** A buffered text reader with conditional read methods that reads lines of text and allows the current line
 * to be fully or partially unread.
 * @see PushbackReader
 * @see BufferedReader
 * @author TeamworkGuy2
 * @since 2013-12-10
 */
public final class TextParserImpl implements TextParser, Closeable {
	private PeekableIterator<Object> in;
	private boolean inReturnsCharArray;
	private char[] curLineChars;
	//private String currentLine;
	private char[] nextLineChars;
	//private String nextLine;
	private int previousLinesOffset;
	private int lineNum;
	private int offset = -1;
	private int lineMod;
	private boolean started = false;
	private boolean returned;
	//private SlidingStringView textBuf;


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the buffered reader to read the lines of text from
	 */
	protected TextParserImpl(PeekableIterator<? extends Object> reader, boolean inReturnsCharArray) {
		this(reader, inReturnsCharArray, 1);
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the buffered reader to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	@SuppressWarnings("unchecked")
	protected TextParserImpl(PeekableIterator<? extends Object> reader, boolean inReturnsCharArray, int lastLineNum) {
		this.in = (PeekableIterator<Object>)reader;
		this.inReturnsCharArray = inReturnsCharArray;
		this.lineNum = lastLineNum;
		//this.textBuf = new SlidingStringView(4096);
	}


	@Override
	public boolean isUnmodifiedLine() {
		return lineMod == 0;
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


	/* commented out 2016-2-23, due to performance overhead of keeping a SlidingStringView reference in every text buffer
	@Override
	public String substring(int startIndex, int endIndex) {
		return textBuf.substring(startIndex, endIndex);
	}
	*/


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
	public boolean hasNextLine() {
		return peekNextLine() != null;
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
			while(hasNextLine()) {
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
		if(offset + count > curLineChars.length) { throw new IndexOutOfBoundsException("end of line, must read next line"); }
		offset += count;
	}


	@Override
	public void unread(int count) {
		if(offset - count < -1) {
			throw new IndexOutOfBoundsException("cannot unread " + count + " from " + offset +
				", offset must remain greater than or equal to -1");
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


	@Override
	public boolean nextIf(char ch) {
		return nextIf(ch, 1, null) == 1;
	}


	@Override
	public int nextIf(char ch, Appendable dst) {
		return nextIf(ch, 1, dst);
	}


	@Override
	public int nextIf(char ch, char ch2, Appendable dst) {
		return nextIf(ch, ch2, 1, dst);
	}


	@Override
	public int nextIf(char[] chars, Appendable dst) {
		return nextIf(chars, 0, chars.length, dst);
	}


	@Override
	public int nextIf(char[] chars, int off, int len, Appendable dst) {
		return nextIf(chars, off, len, 0, dst);
	}


	@Override
	public int nextIf(char ch, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;

			while(i < count && hasNext()) {
				if(ch == nextChar()) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIf(char ch, char ch2, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char c = 0;

			while(i < count && hasNext()) {
				if(((c = nextChar()) == ch || c == ch2)) {
					if(dst != null) {
						dst.append(c);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public boolean nextIf(String str) {
		int count = str.length(); //(count == 0 ? Integer.MAX_VALUE : count);
		int i = 0;

		if(!hasNext()) {
			return false;
		}

		for(i = 0; i < count && hasNext() && (nextChar() == str.charAt(i)); i++) {
		}

		if(i != count) {
			unread(i + 1);
			return false;
		}
		return true;
	}


	@Override
	public int nextIf(char[] chars, int count, Appendable dst) {
		return nextIf(chars, 0, chars.length, count, dst);
	}


	@Override
	public int nextIf(char[] chars, int off, int len, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if(ArrayUtil.indexOf(chars, off, len, (ch = nextChar())) > -1) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIf(Predicates.Char condition, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if(condition.test((ch = nextChar()))) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIfNot(char c, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != c) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIfNot(char c, char c2, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != c && ch != c2) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIfNot(char c, char c2, char c3, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != c && ch != c2 && ch != c3) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIfNot(char[] chars, int count, Appendable dst) {
		return nextIfNot(chars, 0, chars.length, count, dst);
	}


	@Override
	public int nextIfNot(char[] chars, int off, int len, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if(ArrayUtil.indexOf(chars, off, len, (ch = nextChar())) == -1) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIfNotPrecededBy(char endCh, char escCh, boolean dropEscChars, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char prevCh = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != endCh || prevCh == escCh) {
					if(dst != null) {
						if(i > 0 && (!dropEscChars || (prevCh != escCh || ch != endCh))) {
							dst.append(prevCh);
						}
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
				prevCh = ch;
			}

			if(i > 0) {
				dst.append(prevCh);
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, boolean dropEscChars, int count, Appendable dst) {
		return nextIfNotPrecededBy(endCh, escCh, stopCh, stopCh, dropEscChars, count, dst);
	}


	@Override
	public int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, char stopCh2, boolean dropEscChars, int count, Appendable dst) {
		return nextIfNotPrecededBy(endCh, escCh, stopCh, stopCh2, stopCh2, dropEscChars, count, dst);
	}


	@Override
	public int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, char stopCh2, char stopCh3, boolean dropEscChars, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char prevCh = 0;
			char ch = 0;

			while(i < count && hasNext()) {
				if((ch = nextChar()) != stopCh && ch != stopCh2 && ch != stopCh3 && (ch != endCh || prevCh == escCh)) {
					if(dst != null) {
						if(i > 0 && (!dropEscChars || (prevCh != escCh || ch != endCh))) {
							dst.append(prevCh);
						}
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
				prevCh = ch;
			}

			if(i > 0) {
				dst.append(prevCh);
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	@Override
	public int nextIf(CharCategory type, Appendable dst) {
		return nextIf(type, 0, dst);
	}


	@Override
	public int nextIf(CharCategory type, int count, Appendable dst) {
		CharSearcher charSearchers = type.getCharCondition();
		return nextIf(charSearchers::contains, count, dst);
	}


	/** Read characters matching characters between two inclusive values
	 * @param lower the lower inclusive character
	 * @param upper the upper inclusive character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store read characters in
	 * @return the number of characters read
	 */
	@Override
	public int nextBetween(char lower, char upper, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;
	
			while(i < count && hasNext()) {
				if((ch = nextChar()) >= lower && ch <= upper) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	/** Read characters matching characters between two inclusive values
	 * @param lower the lower inclusive character
	 * @param upper the upper inclusive character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store read characters in
	 * @return the number of characters read
	 */
	public int nextBetween(char lower, char upper, char lower2, char upper2, int count, Appendable dst) {
		try {
			count = (count == 0 ? Integer.MAX_VALUE : count);
			int i = 0;
			char ch = 0;
	
			while(i < count && hasNext()) {
				if((((ch = nextChar()) >= lower && ch <= upper) || (ch >= lower2 && ch <= upper))) {
					if(dst != null) {
						dst.append(ch);
					}
					i++;
				}
				else {
					unread(1);
					break;
				}
			}
			return i;
		} catch(IOException e) {
			throw new UncheckedIOException(e);
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
			throw new IndexOutOfBoundsException("end of line, must read next line");
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
	public static TextParserImpl fromStrings(PeekableIterator<String> reader) {
		TextParserImpl impl = new TextParserImpl(reader, false);
		return impl;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	public static TextParserImpl fromStrings(PeekableIterator<String> reader, int lastLineNum) {
		TextParserImpl impl = new TextParserImpl(reader, false, lastLineNum);
		return impl;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 */
	public static TextParserImpl fromCharArrays(PeekableIterator<char[]> reader) {
		TextParserImpl impl = new TextParserImpl(reader, true);
		return impl;
	}


	/** Create a line buffer with a {@link PeekableIterator} source
	 * @param reader the peekable iterator to read the lines of text from
	 * @param lastLineNum the starting line number of this line buffer
	 */
	public static TextParserImpl fromCharArrays(PeekableIterator<char[]> reader, int lastLineNum) {
		TextParserImpl impl = new TextParserImpl(reader, true, lastLineNum);
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
	public static TextParserImpl of(String src) {
		return TextParserImpl.of(src, 0, src.length(), true, true, true, true);
	}


	public static TextParserImpl of(String src, int off, int len) {
		return of(src, off, len, true, true, true, true);
	}


	public static TextParserImpl of(String src, int off, int len, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		//BufferedReader reader = new BufferedReader(new StringReader(src));
		Supplier<String> lines = new StringLineSupplier(src, off, len, treatEmptyLineAsLine, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, collapseNewlinesIntoOneChar);
		EnhancedIterator<String> lineReader = new EnhancedIterator<>(lines, null);
		TextParserImpl lineBuffer = new TextParserImpl(lineReader, false);

		return lineBuffer;
	}

}
