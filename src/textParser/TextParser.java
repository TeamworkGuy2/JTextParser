package textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.PushbackReader;

import twg2.ranges.helpers.CharCategory;
import functionUtils.CharPredicate;

/** A buffered reader like class that reads lines and also allows the current line
 * to be fully or partially unread.
 * @see PushbackReader
 * @see BufferedReader
 * @author TeamworkGuy2
 * @since 2013-12-10
 */
public interface TextParser extends LineReader, Closeable {

	/**
	 * @return true if there is another line, false otherwise
	 */
	public boolean hasNextLine();


	/** Read the next character from the line
	 * @param ch the character to match
	 * @return true if the next char was read and matched the specified char, false if not (and the line buffer is left unmodified)
	 */
	public boolean nextIf(char ch);


	/** Read a matching string from the line
	 * @param str the string to match
	 * @return true if the entire string was matched, false if not (and the line buffer is left unmodified)
	 */
	public boolean nextIf(String str);


	/** Read the next character if the current character at the current line offset equals the specified character
	 * @param ch the character to compare to the current line character
	 * @param dst the destination to store the read character in
	 * @return 1 if a matching character is read, 0 if not
	 */
	public int nextIf(char ch, Appendable dst);


	/** Read the next character if the current character at the current line offset equals the specified character
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param dst the destination to store the read character in
	 * @return 1 if a matching character is read, 0 if not
	 */
	public int nextIf(char ch, char ch2, Appendable dst);


	/** Read the next character if the current character at the current line offset equals the specified character
	 * @param ch the character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the read character in
	 * @return 1 if a matching character is read, 0 if not
	 */
	public int nextIf(char ch, int count, Appendable dst);


	/** Read the next character if the current character at the current line offset equals the specified character
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the read character in
	 * @return 1 if a matching character is read, 0 if not
	 */
	public int nextIf(char ch, char ch2, int count, Appendable dst);


	/** Read as many characters as match from the current line
	 * @param chars the list of valid characters that can be read
	 * @param dst the destination to store the parsed text in
	 * @return the number of characters read
	 */
	public int nextIf(char[] chars, Appendable dst);


	public int nextIf(char[] chars, int off, int len, Appendable dst);


	/** Read as many characters as match, up to {@code count} number of characters, from the current line
	 * @param chars the list of valid characters that can be read
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the parsed text in
	 * @return the number of characters read
	 */
	public int nextIf(char[] chars, int count, Appendable dst);


	public int nextIf(char[] chars, int off, int len, int count, Appendable dst);


	/** Read the next character if the current character at the current line offset equals the specified character type
	 * @param type the {@link CharCategory} of the characters to ready
	 * @param dst the destination to store the read characters in
	 * @return the number of character read
	 */
	public int nextIf(CharCategory type, Appendable dst);


	/** Read the next character if the current character at the current line offset equals the specified character type
	 * @param type the {@link CharCategory} of the characters to ready
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the read characters in
	 * @return the number of character read
	 */
	public int nextIf(CharCategory type, int count, Appendable dst);


	/** Read as many characters as match, up to {@code count}, from the current line
	 * @param condition the condition to check each character against
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the parsed text in
	 * @return the number of characters read
	 */
	public int nextIf(CharPredicate condition, int count, Appendable dst);


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param ch the character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the read character in
	 * @return the number of matching characters read
	 */
	public int nextIfNot(char ch, int count, Appendable dst);


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the read character in
	 * @return the number of matching characters read
	 */
	public int nextIfNot(char ch, char ch2, int count, Appendable dst);


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param ch the first character to compare to the current line character
	 * @param ch2 the second character to compare to the current line character
	 * @param ch3 the third character to compare to the current line character
	 * @param count the maximum number of matching characters to read, or 0 to
	 * read as many matching characters as possible
	 * @param dst the destination to store the read character in
	 * @return the number of matching characters read
	 */
	public int nextIfNot(char ch, char ch2, char ch3, int count, Appendable dst);


	/** Read as many characters as do not match, up to {@code count}, from the current line
	 * @param chars the list of valid invalid characters to stop reading at
	 * @param count the maximum number of characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to store the parsed text in
	 * @return the number of characters read
	 */
	public int nextIfNot(char[] chars, int count, Appendable dst);


	public int nextIfNot(char[] chars, int off, int len, int count, Appendable dst);


	/** Read as many characters as match, up to {@code count}, from the current line
	 * until a {@code endCh} is encountered, only if it is not proceeded by {@code escCh}
	 * @param dropEscChars true to not append escape characters to {@code dst}, false add all matching characters to dst
	 */
	public int nextIfNotPrecededBy(char endCh, char escCh, boolean dropEscChars, int count, Appendable dst);


	/** Read as many characters as match, up to {@code count}, from the current line
	 * until a {@code endCh} is encountered, only if it is not proceeded by {@code escCh}
	 * or until {@code stopCh} is encountered regardless of preceding character
	 * @param dropEscChars true to not append escape characters to {@code dst}, false add all matching characters to dst
	 */
	public int nextIfNotPrecededBy(char endCh, char escCh, char stopCh, boolean dropEscChars, int count, Appendable dst);


	/** {@link #nextIfNotPrecededBy(char, char, char, boolean, int, Appendable)}
	 */
	public int nextIfNotPrecededBy(char endCh, char escCh, char stopCh1, char stopCh2, boolean dropEscChars, int count, Appendable dst);


	/** {@link #nextIfNotPrecededBy(char, char, char, boolean, int, Appendable)}
	 */
	public int nextIfNotPrecededBy(char endCh, char escCh, char stopCh1, char stopCh2, char stopCh3, boolean dropEscChars, int count, Appendable dst);


	public int nextBetween(char lower, char upper, int count, Appendable dst);


	public default String readLine() {
		StringBuilder strB = new StringBuilder();
		readLine(strB);
		return strB.toString();
	}


	public default void readLine(Appendable dst) {
		nextIfNot('\n', 0, dst);
		nextIf('\n');
	}


	public default String getPositionDisplayText() {
		return this.getPlainLineNumber() + ":" + this.getLineOffset();
	}

}
