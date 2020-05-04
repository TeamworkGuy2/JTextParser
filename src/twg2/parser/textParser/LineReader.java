package twg2.parser.textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.PushbackReader;

/** A buffered text reader that tracks position, line number, and column offset.
 * Also allows the current line to be fully or partially unread.
 * @see PushbackReader
 * @see BufferedReader
 * @author TeamworkGuy2
 * @since 2015-1-15
 */
public interface LineReader extends Closeable, ParserPos {

	/**
	 * @return the parser's current, 0 based, position within the underlying data stream.
	 * NOTE: returns -1 if the parser is not initialized
	 */
	@Override
	public int getPosition();


	/** Get the plain line number of the current line.
	 * The first line read is 1.
	 * @return the current line number
	 */
	@Override
	public int getLineNumber();


	/**
	 * @return the current line character offset, 1 based
	 */
	@Override
	public int getColumnNumber();


	/**
	 * @param count the number of characters to read
	 * @param dst the destination to store the characters, nullable
	 * @return the number of characters successfully ready, will equal {@code count}
	 * unless end-of-input has been reached
	 */
	public int readCount(int count, Appendable dst);


	/** Advance the current line offset by the specified number of characters
	 * @param count the value to add to the current line offset
	 */
	public void skip(int count);


	/** Unread {@code count} number from the current line offset.
	 * For example if the last call to {@link #getColumnNumber()} returned {@code 5}
	 * and this function is called with the parameter {@code 3}, the next call
	 * to {@link #getColumnNumber()} returns {@code 2}.
	 * @param count the number to subtract from the current line offset
	 */
	public void unread(int count);


	/**
	 * @return true if {@link #hasNext()} return true, false otherwise
	 */
	public boolean hasNext();


	/** Read the next character
	 * @return the next character
	 * @throws IndexOutOfBoundsException if the end of the buffer has been reached, check for this condition with {@link #hasNext()}
	 */
	public char nextChar();


	/**
	 * @return true if the current line has a previous character, false if not
	 */
	public boolean hasPrevChar();


	/**
	 * @return the previous character from this line if available, throws an error if no previous char is available
	 */
	public char prevChar();
}
