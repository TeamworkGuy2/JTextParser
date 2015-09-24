package twg2.parser.parserUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;

import twg2.collections.util.arrayUtils.ArrayUtil;

/**
 * This class is not thread safe
 * @author TeamworkGuy2
 * @since 2013-12-10
 */
public final class ReadRepeats {

	private ReadRepeats() { throw new AssertionError("cannot instantiate static class ReadRepeats"); }


	/** Read characters from an input stream until they do not match the specified character.
	 * The last non-matching character read, is unread using {@link BufferedReader#reset()}. 
	 * @param in the input stream reader to read from
	 * @param ch the character to compare each character read from the input stream to
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @return the number of characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final BufferedReader in, final char ch, final int max) {
		int read = 0;
		try {
			while(true) {
					in.mark(1);
				if((char)in.read() == ch) {
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				else {
					in.reset();
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/**
	 * @see #readRepeat(BufferedReader, char[], int, int, int, StringBuilder)
	 */
	public static final StringBuilder readRepeat(final BufferedReader in, final char[] ch, final int max, StringBuilder dst) {
		return readRepeat(in, ch, 0, ch.length, max, dst);
	}


	/** Read characters from an input stream until a character does not match one
	 * of the specified characters.
	 * The last non-matching character read, is unread using {@link BufferedReader#reset()}.
	 * @param in the input stream reader to read from
	 * @param ch the array of possible characters to compare to each character
	 * read from the input stream to
	 * @param chOff the offset into the array at which matching characters to compare begin
	 * @param chLen the offset into the array at which matching characters to compare end
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the {@link StringBuilder} to store the read characters in
	 * @return the {@code dst} string builder
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final StringBuilder readRepeat(final BufferedReader in, final char[] ch, final int chOff, final int chLen,
			final int max, StringBuilder dst) {
		int read = 0;
		try {
			while(true) {
				in.mark(1);
				char c = (char)in.read();
				// Add a character if it matches
				if(ArrayUtil.indexOf(ch, chOff, chLen, c) > -1) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				// Reset the stream and return if the character does not match
				else {
					in.reset();
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return dst;
	}


	public static final int readRepeat(String str, final char ch, final int max) {
		return readRepeat(str, 0, str.length(), ch, max);
	}


	public static final int readRepeat(String str, final int strOff, final char ch, final int max) {
		return readRepeat(str, strOff, str.length() - strOff, ch, max);
	}


	/** Read characters from a string until they do not match the specified character.
	 * The last non-matching character read, is unread using {@link BufferedReader#reset()}. 
	 * @param str the string read characters from
	 * @param strOff the offset into the string at which to start reading repeat characters
	 * @param strLen the maximum number of characters to read from {@code str} starting at {@code strOff}
	 * @param ch the character to compare each character read from the input stream to
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @return the number of characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final String str, final int strOff, final int strLen, final char ch, final int max) {
		if(strOff + strLen > str.length()) { return 0; }
		int read = strOff;
		for(int size = strOff + strLen; read < size; ) {
			if(str.charAt(read) == ch) {
				read++;
				if(max != 0 && read - strOff >= max) {
					break;
				}
			}
			else {
				break;
			}
		}
		return read - strOff;
	}


	/**
	 * @see #readRepeat(String, int, char[], int, int, int, Appendable)
	 */
	public static final String readRepeat(final String str, final int strOffset, final char[] ch, final int max) {
		return readRepeat(str, strOffset, ch, 0, ch.length, max);
	}


	/**
	 * @see #readRepeat(String, int, char[], int, int, int, Appendable)
	 */
	public static final String readRepeat(final String str, final int strOffset, final char[] ch,
			final int offset, final int length, final int max) {
		StringBuilder tmpStrB = new StringBuilder();
		readRepeat(str, strOffset, ch, offset, length, max, tmpStrB);
		String resultString = tmpStrB.toString();
		return resultString;
	}


	/**
	 * @see #readRepeat(String, int, char[], int, int, int, Appendable)
	 */
	public static final int readRepeat(final String str, final int strOffset, final char[] ch,
			final int max, final Appendable dst) {
		return readRepeat(str, strOffset, ch, 0, ch.length, max, dst);
	}


	/** Read characters from a string until a character does not match one of the specified character.
	 * @param str the string read characters from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param ch the array of possible characters to compare to each character
	 * read from the input stream to
	 * @param chOff the offset into the array at which matching characters to compare begin
	 * @param chLen the offset into the array at which matching characters to compare end
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final String str, final int strOffset, final char[] ch,
			final int chOff, final int chLen, final int max, final Appendable dst) {
		if(strOffset >= str.length()) { return 0; }
		int read = 0;
		try {
			for(int size = str.length() - strOffset; read < size; ) {
				char c = str.charAt(strOffset + read);
				// Add a character if it matches
				if(ArrayUtil.indexOf(ch, chOff, chLen, c) > -1) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				// Break and return if the character does not match
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/**
	 * @see #readRepeat(char[], int, char[], int, int, int, Appendable)
	 */
	public static final String readRepeat(final char[] str, final int strOffset, final char[] ch,
			final int max) {
		return readRepeat(str, strOffset, ch, 0, ch.length, max);
	}


	/**
	 * @see #readRepeat(char[], int, char[], int, int, int, Appendable)
	 */
	public static final String readRepeat(final char[] str, final int strOffset, final char[] ch,
			final int offset, final int length, final int max) {
		StringBuilder tmpStrB = new StringBuilder();
		readRepeat(str, strOffset, ch, offset, length, max, tmpStrB);
		String resultString = tmpStrB.toString();
		return resultString;
	}


	/**
	 * @see #readRepeat(char[], int, char[], int, int, int, Appendable)
	 */
	public static final int readRepeat(final char[] str, final int strOffset, final char[] ch,
			final int max, final Appendable dst) {
		return readRepeat(str, strOffset, ch, 0, ch.length, max, dst);
	}


	/** Read characters from a char array until a character does not match one of the specified character.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param ch the array of possible characters to compare to each character
	 * read from the input stream to
	 * @param chOff the offset into the array at which matching characters to compare begin
	 * @param chLen the offset into the array at which matching characters to compare end
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final char[] str, final int strOffset, final char[] ch,
			final int chOff, final int chLen, final int max, final Appendable dst) {
		if(strOffset >= str.length) { return 0; }
		int read = 0;
		try {
			while(true) {
				char c = str[strOffset + read];
				// Add a character if it matches
				if(ArrayUtil.indexOf(ch, chOff, chLen, c) > -1) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				// Break and return if the character does not match
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/** Read characters from a char array until a character does not match one of the specified character.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param lowerBound the lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound the upper bound (inclusive) of characters to read from the input stream
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final char[] str, final int strOffset,
			char lowerBound, char upperBound, final int max, final Appendable dst) {
		if(strOffset >= str.length) { return 0; }
		int read = 0;
		try {
			while(true) {
				char c = str[strOffset + read];
				// Add the character if it matches, else break from the loop and return
				if(c >= lowerBound && c <= upperBound) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/** Read characters from a char array until a character does not match one of the specified character.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param lowerBound1 the lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound1 the upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound2 the second lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound2 the second upper bound (inclusive) of characters to read from the input stream
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final char[] str, final int strOffset,
			char lowerBound1, char upperBound1,
			char lowerBound2, char upperBound2,
			final int max, final Appendable dst) {
		if(strOffset >= str.length) { return 0; }
		int read = 0;
		try {
			while(true) {
				char c = str[strOffset + read];
				// Add the character if it matches, else break from the loop and return
				if((c >= lowerBound1 && c <= upperBound1) || (c >= lowerBound2 && c <= upperBound2)) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/** Read characters from a char array until a character does not match one of the specified character.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param lowerBound1 the lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound1 the upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound2 the second lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound2 the second upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound3 the third lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound3 the third upper bound (inclusive) of characters to read from the input stream
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final char[] str, final int strOffset,
			char lowerBound1, char upperBound1,
			char lowerBound2, char upperBound2,
			char lowerBound3, char upperBound3,
			final int max, final Appendable dst) {
		if(strOffset >= str.length) { return 0; }
		int read = 0;
		try {
			while(true) {
				char c = str[strOffset + read];
				// Add the character if it matches, else break from the loop and return
				if((c >= lowerBound1 && c <= upperBound1) ||
						(c >= lowerBound2 && c <= upperBound2) ||
						(c >= lowerBound3 && c <= upperBound3)) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/** Read characters from a char array until a character does not match one of the specified character.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param lowerBound1 the lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound1 the upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound2 the second lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound2 the second upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound3 the third lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound3 the third upper bound (inclusive) of characters to read from the input stream
	 * @param c1 another optional character to read
	 * @param c2 another optional character to read
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final char[] str, final int strOffset,
			char lowerBound1, char upperBound1,
			char lowerBound2, char upperBound2,
			char lowerBound3, char upperBound3,
			char c1, char c2, final int max, final Appendable dst) {
		if(strOffset >= str.length) { return 0; }
		int read = 0;
		try {
			while(true) {
				char c = str[strOffset + read];
				// Add the character if it matches, else break from the loop and return
				if(c == c1 || c == c2 || (c >= lowerBound1 && c <= upperBound1) ||
						(c >= lowerBound2 && c <= upperBound2) ||
						(c >= lowerBound3 && c <= upperBound3)) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	/** Read characters from a char array until a character does not match one of the specified character.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param ch the array of possible characters to compare to each character
	 * read from the input stream to
	 * @param offset the offset into the array at which matching characters to compare begin
	 * @param length the offset into the array at which matching characters to compare end
	 * @param lowerBound1 the lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound1 the upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound2 the second lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound2 the second upper bound (inclusive) of characters to read from the input stream
	 * @param lowerBound3 the third lower bound (inclusive) of characters to read from the input stream
	 * @param upperBound3 the third upper bound (inclusive) of characters to read from the input stream
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int readRepeat(final char[] str, final int strOffset, final char[] ch,
			final int offset, final int length,
			char lowerBound1, char upperBound1,
			char lowerBound2, char upperBound2,
			char lowerBound3, char upperBound3,
			final int max, final Appendable dst) {
		if(strOffset >= str.length) { return 0; }
		final int totalLength = offset + length;
		int read = 0;
		boolean match = false;
		try {
			while(true) {
				char c = str[strOffset+read];
				// Check for a matching character
				match = false;
				if((c >= lowerBound1 && c <= upperBound1) ||
						(c >= lowerBound2 && c <= upperBound2) ||
						(c >= lowerBound3 && c <= upperBound3)) {
					match = true;
				}
				if(match == false) {
					for(int i = offset; i < totalLength; i++) {
						if(c == ch[i]) {
							match = true;
						}
					}
				}
				// Add the matching character
				if(match) {
					dst.append(c);
					read++;
					if(max != 0 && read >= max) {
						break;
					}
				}
				// Break and return if the character does not match
				else {
					break;
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return read;
	}


	public static final int skipRepeat(final char[] str, final int strOffset, final char[] ch, final int max) {
		return skipRepeat(str, strOffset, str.length - strOffset, ch, 0, ch.length, max);
	}


	public static final int skipRepeat(final char[] str, final int strOffset, final int strLength,
			final char[] ch, final int chOff, final int chLen, final int max) {
		if(strOffset + strLength > str.length || strOffset >= str.length || strLength < 0) { return 0; }
		int read = 0;
		for(int size = strLength; read < size; ) {
			char c = str[strOffset + read];
			// Add a character if it matches
			if(ArrayUtil.indexOf(ch, chOff, chLen, c) > -1) {
				read++;
				if(max != 0 && read >= max) {
					break;
				}
			}
			// Break and return if the character does not match
			else {
				break;
			}
		}
		return read;
	}

}
