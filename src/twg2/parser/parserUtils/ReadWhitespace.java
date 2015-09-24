package twg2.parser.parserUtils;

import stringUtils.StringCheck;
import twg2.parser.textParser.TextParser;

/** A tool for parsing specified characters from a line buffer
 * @author TeamworkGuy2
 * @since 2014-8-23
 */
public final class ReadWhitespace {

	public ReadWhitespace() { throw new AssertionError("cannot instantiate static class ReadWhitespace"); }


	/** Count the number of contiguous characters matching {@link StringCheck#SIMPLE_WHITESPACE}
	 * @param in the {@link TextParser} to read characters from
	 * @return the number of whitespace characters read
	 */
	public static int readWhitespace(TextParser in) {
		return readWhitespaceCustom(in, StringCheck.SIMPLE_WHITESPACE, 0);
	}


	/**
	 * @see #readWhitespace(char[], int, int)
	 */
	public static int readWhitespace(char[] str, int strOff) {
		return readWhitespaceCustom(str, strOff, str.length - strOff, StringCheck.SIMPLE_WHITESPACE);
	}


	/** Count the number of contiguous characters matching {@link StringCheck#SIMPLE_WHITESPACE}
	 * @param str the char[] to read whitespace from
	 * @param strOff the offset into {@code str} at which to start reading whitespace
	 * @param strLen the maximum number of whitespace characters to attempt to read from {@code str}
	 * @return the number of whitespace characters read
	 */
	public static int readWhitespace(char[] str, int strOff, int strLen) {
		return readWhitespaceCustom(str, strOff, strLen, StringCheck.SIMPLE_WHITESPACE);
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


	/** Count the number of contiguous whitespace characters in a string starting
	 * from a offset
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
	 * starting from a offset.
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
