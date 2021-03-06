package twg2.parser.textParserUtils;

import twg2.functions.predicates.CharPredicate;
import twg2.parser.textParser.TextParser;

/**
 * @author TeamworkGuy2
 * @since 2015-6-25
 */
public class ReadIsMatching {

	public static boolean isNext(TextParser in, char ch) {
		boolean res = in.nextIf(ch);
		if(res) {
			in.unread(1);
		}
		return res;
	}


	/** Read a matching string from the line
	 * @param str the string to match
	 * @return true if the entire string was matched, false if not (and the line buffer is left unmodified)
	 */
	public static boolean isNext(TextParser in, String str) {
		boolean res = in.nextIf(str);
		if(res) {
			in.unread(str.length());
		}
		return res;
	}


	/** Read the next character if the current character at the current line offset equals the specified character
	 * @param ch the character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return 1 if a matching character is read, 0 if not
	 */
	public static boolean isNext(TextParser in, char ch, int count) {
		int foundCount = in.nextIf(ch, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read the next character if the current character at the current line offset equals the specified character
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return 1 if a matching character is read, 0 if not
	 */
	public static boolean isNext(TextParser in, char ch, char ch2, int count) {
		int foundCount = in.nextIf(ch, ch2, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as match, up to {@code count} number of characters, from the current line
	 * @param chars the array of valid characters that can be read
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return the number of characters read
	 */
	public static boolean isNext(TextParser in ,char[] chars, int count) {
		int foundCount = in.nextIf(chars, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as match, up to {@code count} number of characters, from the current line
	 * @param chars the array of valid characters that can be read
	 * @param off the offset into the {@code chars} array at which valid chars start
	 * @param len the count of valid characters to use starting from {@code off} in the {@code chars} array
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return the number of characters read
	 */
	public static boolean isNext(TextParser in, char[] chars, int off, int len, int count) {
		int foundCount = in.nextIf(chars, off, len, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as match, up to {@code count}, from the current line
	 * @param condition the condition to check each character against
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return the number of characters read
	 */
	public static boolean isNext(TextParser in, CharPredicate condition, int count) {
		int foundCount = in.nextIf(condition, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param ch the character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return the number of matching characters read
	 */
	public static boolean isNextNot(TextParser in, char ch, int count) {
		int foundCount = in.nextIfNot(ch, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return the number of matching characters read
	 */
	public static boolean isNextNot(TextParser in, char ch, char ch2, int count) {
		int foundCount = in.nextIfNot(ch, ch2, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param ch3 the third character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @return the number of matching characters read
	 */
	public static boolean isNextNot(TextParser in, char ch, char ch2, char ch3, int count) {
		int foundCount = in.nextIfNot(ch, ch2, ch3, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param chars the list of valid invalid characters to stop reading at
	 * @param count the maximum number of characters to read, or 0 to read as
	 * many matching characters as possible
	 * @return the number of characters read
	 */
	public static boolean isNextNot(TextParser in, char[] chars, int count) {
		int foundCount = in.nextIfNot(chars, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	public static boolean isNextNot(TextParser in, char[] chars, int off, int len, int count) {
		int foundCount = in.nextIfNot(chars, off, len, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as match, up to {@code count}, from the current line
	 * until a {@code endCh} is encountered, only if it is not proceeded by {@code escCh}
	 */
	public static boolean isNextNotPrecededBy(TextParser in, char endCh, char escCh, int count) {
		int foundCount = in.nextIfNotPrecededBy(endCh, escCh, false, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** Read as many characters as match, up to {@code count}, from the current line
	 * until a {@code endCh} is encountered, only if it is not proceeded by {@code escCh}
	 * or until {@code stopCh} is encountered regardless of preceding character
	 */
	public static boolean isNextNotPrecededBy(TextParser in, char endCh, char escCh, char stopCh, int count) {
		int foundCount = in.nextIfNotPrecededBy(endCh, escCh, stopCh, false, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** {@link ReadIsMatching#isNextNotPrecededBy(TextParser, char, char, char, boolean, int)}
	 */
	public static boolean isNextNotPrecededBy(TextParser in, char endCh, char escCh, char stopCh1, char stopCh2, int count) {
		int foundCount = in.nextIfNotPrecededBy(endCh, escCh, stopCh1, stopCh2, false, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	/** {@link ReadIsMatching#isNextNotPrecededBy(TextParser, char, char, char, boolean, int)}
	 */
	public static boolean isNextNotPrecededBy(TextParser in, char endCh, char escCh, char stopCh1, char stopCh2, char stopCh3, int count) {
		int foundCount = in.nextIfNotPrecededBy(endCh, escCh, stopCh1, stopCh2, stopCh3, false, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}


	public static boolean isNextBetween(TextParser in, char lower, char upper, int count) {
		int foundCount = in.nextBetween(lower, upper, count, null);
		if(foundCount > 0) in.unread(foundCount);
		return foundCount == count;
	}

}
