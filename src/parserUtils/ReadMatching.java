package parserUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import stringUtils.StringCompare;
import stringUtils.StringIndex;
import textParser.TextParser;
import arrayUtils.ArrayUtil;
import functionUtils.CharPredicate;

/**
 * @author TeamworkGuy2
 * @since 2014-12-21
 */
public class ReadMatching {


	public static class FromString {

		public static final void readCharsMatching(String line, int offset, char[] searchChars, StringBuilder dst) {
			for(int charI = offset, len = line.length(); charI < len; charI++) {
				char ch = line.charAt(charI);
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
		 * @param dst the string builder to add the matching characters from
		 * @param strs a list of strings that are valid characters to read from {@code line}
		 * @param searchChars a list of characters that are valid characters to read from {@code line}
		 */
		public static final void readCharsMatching(String line, int offset, List<String> strs,
				char[] searchChars, StringBuilder dst) {
			// for each character in the line, see if it matches a character in the character array
			// or is part of a string of characters that matches of the strings in the string array
			for(int charI = offset, len = line.length(); charI < len; charI++) {
				char ch = line.charAt(charI);
				// preemptively append the first character, this must be undone if a character match cannot be found
				dst.append(ch);
				int index = StringIndex.startsWithIndex(strs, dst, charI - offset);

				// if the start of a matching string is found, read until the entire string is read
				// or until a mismatching character is found
				if(index != -1) {
					boolean readFullString = false;
					int initialLength = dst.length();
					int cI = charI + 1;
					int strI = index;
					while((strI = StringIndex.startsWithIndex(strs, dst, charI - offset)) != -1 &&
							cI < len && strs.get(strI).length() != cI - charI) {
						dst.append(line.charAt(cI));
						cI++;
					}
					// if an entire matching string was read, move the counter forward and don't reset the string builder
					if(strI != -1 && strs.get(strI).length() == cI-charI) {
						readFullString = true;
						// TODO remove print
						//System.out.println("found match: " + strs.get(strI));
						charI = cI - 1;
						continue;
					}
					// if a matching string was not found, reset the string builder
					if(!readFullString) {
						// TODO remove print
						//System.out.println("no match: " + strBuilder + ", now: " + strBuilder.substring(0, initialLength));
						index = -1;
						dst.setLength(initialLength);
					}
				}
				if(index == -1 && ArrayUtil.indexOf(searchChars, ch) != -1) {
					// TODO remove print
					//System.out.println("match char: " + ch);
					continue;
				}
				// no string or char match, reset the string builder and end the loop
				dst.setLength(dst.length() - 1);
				// TODO remove print
				//System.out.println("no match for: " + ch + " (" + line.substring(offset, charI+1) + ")");
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
				while(true) {
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


		/** Check if characters from a char array match based on the comparison function.
		 * @param str the array of character to read from
		 * @param strOffset the offset into the string at which to start reading repeat characters
		 * @param check the condition to call for each character read, if it returns true the character is read,
		 * if it returns false the method returns
		 * @param max the maximum number of repeating characters to read, or 0 to read as
		 * many matching characters as possible
		 * @return the number of matching characters read
		 * @throws UncheckedIOException if there is an error reading from the input stream
		 */
		public static final int checkIf(final char[] str, final int strOffset, CharPredicate check, final int max) {
			if(strOffset >= str.length) {
				return 0;
			}
			char c = 0;
			int read = 0;
			while(true) {
				c = str[strOffset + read];
				// Check if the character is valid and add it
				if(check.test(c)) {
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


	/** Binary search a list of strings for a specified string builder matching one of the strings
	 */
	public static final int binaryStartsWith(SearchRange range, List<String> strs, int midIndex, StringBuilder key) {
		while(range.low <= range.high) {
			int mid = range.getMid();
			// TODO remove testing
			System.out.println("compareStr: " + strs.get(mid) + " vs. " + key + " = " + StringCompare.compareStartsWith(strs.get(mid), key, 0));
			int cmp = StringCompare.compareStartsWith(strs.get(mid), key, 0);
			if(cmp < 0) {
				range.low = mid + 1;
			}
			else if(cmp > 0) {
				range.high = mid - 1;
			}
			else {
				return mid;
			}
		}
		return -(range.low + 1);
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

}
