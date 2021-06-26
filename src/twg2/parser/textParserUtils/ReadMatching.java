package twg2.parser.textParserUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import twg2.arrays.ArrayUtil;
import twg2.functions.predicates.CharPredicate;
import twg2.parser.textParser.TextParser;

/**
 * @author TeamworkGuy2
 * @since 2014-12-21
 */
public class ReadMatching {

	public static final void readCharsMatching(String line, int offset, char[] searchChars, StringBuilder dst) {
		for(int i = offset, len = line.length(); i < len; i++) {
			char ch = line.charAt(i);
			if(ArrayUtil.indexOf(searchChars, ch) == -1) {
				break;
			}
			dst.append(ch);
		}
	}


	/** Starting at {@code offset} in {@code line}, read as many characters as match
	 * one of {@code chars} or {@code strs}
	 * @param line the line to read characters from, null throws {@link NullPointerException}
	 * @param offset the offset into {@code line} at which to start reading, must be equal or
	 * greater than 0 and less than {@code line.length()}
	 * @param strs a list of strings that are valid characters to read from {@code line}
	 * @param searchChars a list of characters that are valid characters to read from {@code line}
	 * @param dst the string builder to add the matching characters from
	 */
	public static final void readCharsMatching(String line, int offset, List<String> strs, char[] searchChars, StringBuilder dst) {
		// for each character in the line, see if it matches a character in the character array
		// or is part of a string of characters that matches of the strings in the string array
		for(int charI = offset, len = line.length(); charI < len; charI++) {
			char ch = line.charAt(charI);
			// preemptively append the first character, this must be undone if a character match cannot be found
			dst.append(ch);
			int index = startsWithIndex(strs, dst, charI - offset);

			// if the start of a matching string is found, read until the entire string is read
			// or until a mismatching character is found
			if(index != -1) {
				boolean readFullString = false;
				int initialLength = dst.length();
				int cI = charI + 1;
				int strI = index;
				while((strI = startsWithIndex(strs, dst, charI - offset)) != -1 &&
						cI < len && strs.get(strI).length() != cI - charI) {
					dst.append(line.charAt(cI));
					cI++;
				}
				// if an entire matching string was read, move the counter forward and don't reset the string builder
				if(strI != -1 && strs.get(strI).length() == cI-charI) {
					readFullString = true;
					charI = cI - 1;
					continue;
				}
				// if a matching string was not found, reset the string builder
				if(!readFullString) {
					index = -1;
					dst.setLength(initialLength);
				}
			}
			if(index == -1 && ArrayUtil.indexOf(searchChars, ch) != -1) {
				continue;
			}
			// no string or char match, reset the string builder and end the loop
			dst.setLength(dst.length() - 1);
			break;
		}
	}


	/** Read characters from a char array until a character does not match based on the comparison function.
	 * @param str the array of character to read from
	 * @param strOffset the offset into the string at which to start reading repeat characters
	 * @param check the condition to call for each character read, if it returns true the character is read,
	 * if it returns false the method returns
	 * @param max the maximum number of repeating characters to read, or 0 to read as
	 * many matching characters as possible
	 * @param dst the destination to write the read characters to
	 * @return the number of matching characters read
	 * @throws UncheckedIOException if there is an error reading from the input stream
	 */
	public static final int read(char[] str, int strOffset, CharPredicate check, int max, final Appendable dst) {
		if(strOffset >= str.length) {
			return 0;
		}
		char c = 0;
		int read = 0;
		try {
			for(int size = str.length - strOffset; read < size; ) {
				c = str[strOffset + read];
				// Check if the character is valid and add it
				if(check.test(c)) {
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


	/** Read a sequence of escaped characters.<br>
	 * For example, a call:<br>
	 * {@code unescape("a \\\"block\\\" char '\\\"'", 0, '\\', '"', new StringBuilder())}<br>
	 * would return {@code 21} (the index of the end character or end of the string)<br>
	 * and the last, appendable, parameter would contain:<br>
	 * {@code a "block" char '"'}
	 * @param in the source to read characters from
	 * @param chReplace the char to replace
	 * @param chEnd stop reading when this char is reached
	 * @param dst the destination to store the read characters in
	 * @return the number of characters read from {@code in} before parsing stopped
	 */
	public static final int readUnescape(TextParser in, char chReplace, char chEnd, boolean allowNewline, char newlineChar, Appendable dst) {
		int i = 0;
		try {
			while(in.hasNext()) {
				char ch = in.nextChar();
				i++;
				if(!allowNewline && ch == newlineChar) {
					in.unread(1);
					return i - 1;
				}
				if(ch == chEnd) {
					return i;
				}
				if(ch == chReplace) {
					if(!in.hasNext()) {
						return i;
					}
					ch = in.nextChar();
					i++;
				}
				dst.append(ch);
			}
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
		return i;
	}


	// NOTE: copied from JTextUtil@0.13.4 project (2021-06-26): StringIndex.java and StringCompare.java

	/** Returns the list index of the string that starts with {@code startStr}
	 * @param strs the list of strings, the start of each is compared to {@code startStr}
	 * @param startStr the character sequence to compare to the start of each string in {@code strs}
	 * @param startStrOffset the offset into the start string's contents at which to start
	 * comparing characters to strings from {@code strs}
	 * @return the {@code str} index of the string that starts with the contents of {@code startStr},
	 * -1 if none of the strings start with {@code startStr}
	 */
	public static int startsWithIndex(List<String> strs, CharSequence startStr, int startStrOffset) {
		for(int i = 0, len = strs.size(); i < len; i++) {
			if(compareStartsWith(strs.get(i), startStr, startStrOffset) == 0) {
				return i;
			}
		}
		return -1;
	}


	/** Compare a string to the {@link CharSequence}. Returns 0 if {@code str} starts
	 * with the contents of {@code startStr}.
	 * <pre>
	 * str="b" > startStr="ab"
	 * str="ab" < startStr="b"
	 * str="ab" < startStr="abc"
	 * str="abc" == startStr="ab"
	 * str="abc" == startStr="abc"
	 * </pre>
	 * @param str the string to compare to
	 * @param startStr the char sequence to compare to the beginning of {@code str}
	 * @param startStrOffset the offset into the CharSequence at which to start comparing characters to {@code str}
	 * @return 0 if {@code str} starts with {@code startStr}, greater than 0 if {@code str}
	 * is greater than {@code startStr}, less than 0 if {@code str} is less than {@code startStr}
	 */
	public static int compareStartsWith(String str, CharSequence startStr, int startStrOffset) {
		int strLen = str.length();
		int ssRemLen = startStr.length() - startStrOffset;
		int len = strLen > ssRemLen ? ssRemLen : strLen;
		int k = 0;
		for( ; k < len; k++) {
			char c1 = str.charAt(k);
			char c2 = startStr.charAt(startStrOffset + k);
			if(c1 != c2) {
				return c1 - c2;
			}
		}
		// startStr empty or 0 characters matched and str is not empty, then str is greater
		if((ssRemLen == 0 || k == 0) && strLen != 0) {
			return 1;
		}
		return (k == len && strLen >= ssRemLen ? 0 : strLen - ssRemLen);
	}

}
