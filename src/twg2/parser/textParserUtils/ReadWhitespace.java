package twg2.parser.textParserUtils;

import twg2.parser.textParser.TextParser;

/** Static methods for reading whitespace characters from a string or {@link TextParser}
 * @author TeamworkGuy2
 * @since 2014-8-23
 */
public final class ReadWhitespace {
	// NOTE: copied from JTextUtil@0.13.4 StringCheck.java (2021-06-26)
	public static final char[] SIMPLE_WHITESPACE = new char[] {
		' ' /* space: 32 */,
		'	' /* tab: 9 */,
		12 /* vertical tab: 12 */,
		'\n' /* line terminators */
	};

	private ReadWhitespace() { throw new AssertionError("cannot instantiate static class ReadWhitespace"); }


	/** Count the number of contiguous characters matching {@link StringCheck#SIMPLE_WHITESPACE}
	 * @param in the {@link TextParser} to read characters from
	 * @return the number of whitespace characters read
	 */
	public static int readWhitespace(TextParser in) {
		return readWhitespaceCustom(in, SIMPLE_WHITESPACE, 0);
	}


	/**
	 * @see #readWhitespace(char[], int, int)
	 */
	public static int readWhitespace(char[] str, int strOff) {
		return readWhitespaceCustom(str, strOff, str.length - strOff, SIMPLE_WHITESPACE);
	}


	/** Count the number of contiguous characters matching {@link StringCheck#SIMPLE_WHITESPACE}
	 * @param str the char[] to read whitespace from
	 * @param strOff the offset into {@code str} at which to start reading whitespace
	 * @param strLen the maximum number of whitespace characters to attempt to read from {@code str}
	 * @return the number of whitespace characters read
	 */
	public static int readWhitespace(char[] str, int strOff, int strLen) {
		return readWhitespaceCustom(str, strOff, strLen, SIMPLE_WHITESPACE);
	}


	/**
	 * @see #readWhitespaceCustom(char[], int, int, char[])
	 */
	public static int readWhitespaceCustom(char[] str, int strOff, char[] whitespaces) {
		return readWhitespaceCustom(str, strOff, str.length - strOff, whitespaces);
	}


	/** Count the number of contiguous characters matching any of the specified characters in the {@code whitespaces} char[]
	 * @param str the char[] to read whitespace from
	 * @param strOff the offset into {@code str} at which to start reading whitespace
	 * @param strLen the maximum number of whitespace characters to attempt to read from {@code str}
	 * @param whitespaces the array of valid whitespace characters to compare characters from {@code str} to
	 * @return the number of whitespace characters read
	 */
	public static int readWhitespaceCustom(char[] str, int strOff, int strLen, char[] whitespaces) {
		return ReadRepeats.skipRepeat(str, strOff, strLen, whitespaces, 0, whitespaces.length, 0);
	}


	/**
	 * @see #readWhitespace(String, int, int)
	 */
	public static int readWhitespace(String str, int offset) {
		return readWhitespace(str, offset, str.length() - offset);
	}


	/** Count the number of contiguous whitespace characters (based on {@link Character#isWhitespace(char)}
	 * in a string starting from an offset
	 * @param str the string to read whitespace characters from
	 * @param offset the offset into the string at which to start
	 * checking for whitespace characters
	 * @param length the maximum number of whitespace characters to check from {@code str}
	 * @return the number of contiguous whitespace characters found
	 * in the string.
	 */
	public static int readWhitespace(String str, int offset, int length) {
		int count = offset;
		int offLen = offset + length;
		while(count < offLen && Character.isWhitespace(str.charAt(count))) {
			count++;
		}
		return count - offset;
	}


	/**
	 * @see #readWhitespaceCustom(TextParser, char[], int)
	 */
	public static int readWhitespaceCustom(TextParser in, char[] whitespace) {
		return readWhitespaceCustom(in, whitespace, 0);
	}


	/** Count the number of contiguous whitespace characters in a {@link TextParser}
	 * starting from an offset.
	 * Read new lines if the whitespace sequence extends to the end of the line and {@code max}
	 * characters have not been read
	 * @param in the {@link TextParser} to read whitespace characters from
	 * @param whitespace the char[] of valid whitespace characters
	 * @param max the maximum number of whitespace characters to check from {@code in}
	 * @return the number of contiguous {@code whitespace} characters found
	 * in the string.
	 */
	public static int readWhitespaceCustom(TextParser in, char[] whitespace, int max) {
		int whitespaceReadCount = in.nextIf(whitespace, max, null);
		return whitespaceReadCount;
	}

}
