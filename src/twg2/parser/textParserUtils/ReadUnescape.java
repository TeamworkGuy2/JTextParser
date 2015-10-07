package twg2.parser.textParserUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

import twg2.parser.textParser.TextParser;

/**
 * @author TeamworkGuy2
 * @since 2015-8-1
 */
public class ReadUnescape {

	/**
	 * @author TeamworkGuy2
	 * @since 2015-6-17
	 */
	public static class Default {

		/** Read and unescape a partial quoted string that does not allow newline characters
		 * @see ReadUnescape#readUnescapePartialQuoted(TextParser, char, char, char, char, boolean, char, Appendable)
		 */
		public static final int readUnescapePartialQuoted(TextParser in, char quoteChar, char escChar, char endChar1, char endChar2, Appendable dst) {
			return ReadUnescape.readUnescapePartialQuoted(in, quoteChar, escChar, endChar1, endChar2, false, '\n', dst);
		}


		/** Read and unescape a quoted string that does not allow newline characters
		 * @see ReadUnescape#readUnescapeQuoted(TextParser, char, char, char, char, boolean, char, Appendable)
		 */
		public static final int readUnescapeQuoted(TextParser in, char quoteChar, char escChar, char endChar1, char endChar2, Appendable dst) {
			return ReadUnescape.readUnescapeQuoted(in, quoteChar, escChar, endChar1, endChar2, false, '\n', dst);
		}
	}




	public static final int readUnescapePartialQuoted(TextParser in, char quoteChar, char escChar, char endChar1, char endChar2, boolean allowNewline, char newlineChar, Appendable dst) {
		return readUnescapePartialOrFullQuotedThrows(in, quoteChar, escChar, endChar1, endChar2, allowNewline, newlineChar, false, true, false, false, false, false, dst);
	}


	public static final int readUnescapeQuoted(TextParser in, char quoteChar, char escChar, char endChar1, char endChar2, boolean allowNewline, char newlineChar, Appendable dst) {
		return readUnescapePartialOrFullQuotedThrows(in, quoteChar, escChar, endChar1, endChar2, allowNewline, newlineChar, true, true, false, false, false, false, dst);
	}


	public static final int readUnescapeQuotedThrows(TextParser in, char quoteChar, char escChar, char endChar1, char endChar2, boolean allowNewline, char newlineChar, Appendable dst) {
		return readUnescapePartialOrFullQuotedThrows(in, quoteChar, escChar, endChar1, endChar2, allowNewline, newlineChar, true, true, false, true, true, false, dst);
	}


	/** reads 'quoted' strings which can optionally be proceeded by non-quoted characters and ended
	 * by one of a few end characters or an ending quote if a beginning quote has been encountered.<br>
	 * Example: {@code key: "value, (stuff)", mk}<br>
	 * with: quote=" esc=\ end1=, end2=)<br>
	 * returns: {@code key: "value, (stuff)"}<br>
	 * Useful for parsing CSV or JSON like quoted strings or combinations of none quoted strings followed by a quoted string.
	 * @param in the input source
	 * @param quoteChar the character marking the beginning and end of a quoted sub-section of a string
	 * @param escChar a character that if it precedes {@code quoteChar} at some point after an opening {@code quoteChar},
	 * the 'escaped' {@code quoteChar} is treated as a normal character and not the end of the quoted string
	 * (only applies to {@code quoteChar}, not {@code endChar1} or {@code endChar2})
	 * @param endChar1 character that marks the end of a non-quoted character string, is treated as a literal in a quoted string
	 * @param endChar2 character that marks the end of a non-quoted character string, is treated as a literal in a quoted string
	 * @param allowNewline true to allow newlines in the parsed string, false if not
	 * @param newlineChar the character that represents newlines
	 * @param readQuotedOnly true to only read a string if the first character is a quote
	 * @param dropEscChars true to drop the escape character before a quote in quoted sub-string.<br>
	 * For example, if this param is true, the quoted sub-string portion {@code ...\"things\"...} would be
	 * append to {@code dst} as {@code ..."things"...}.<br>
	 * Else if this param is false the string would be appended to {@code dst} in its original form {@code ...\"things\"...} 
	 * @param appendEndChar true to read and append the {@code endChar1/endChar2} char to {@code dst} that ends the string read, false to read up to that character
	 * @param throwIfQuotedNoStartQuote if {@code readQuotedOnly == true}, else this param is ignored:<br>
	 * <ul> 
	 * <li>if this param is true, throw an error if the first character is not a quote,</li>
	 * <li>else if this param is false and the first character is not a quote, read nothing and return</li>
	 * </ul>
	 * @param throwIfNoEndQuote if an opening quote is encountered and a newline (if {@code !allowNewline}) is read or
	 * the end of the input is read without encountering a closing quote, throw an error
	 * @param throwIfNoEndChar if the string does not end with a {@code endChar1/endChar2} (after any required end quotes, see {@code throwIfNoEndQuote}), throw an error.
	 * @param dst the destination to append successfully matched and read characters to,
	 * will include characters between the next character from {@code in} when this
	 * function was called through (and including) the ending quote or {@code endChar1/endChar2}
	 * @return the number of characters read
	 */
	public static final int readUnescapePartialOrFullQuotedThrows(TextParser in, char quoteChar, char escChar, char endChar1, char endChar2, boolean allowNewline, char newlineChar,
			boolean readQuotedOnly, boolean dropEscChars, boolean appendEndChar, boolean throwIfQuotedNoStartQuote, boolean throwIfNoEndQuote, boolean throwIfNoEndChar, Appendable dst) {
		// all-in-one mess of a function to read a partially or fully quoted string
		int count = 0;
		boolean endingNewline = false;

		try {
			if(!readQuotedOnly) {
				// read up the the first opening quote or ending char (keep escaped chars)
				if(allowNewline) {
					count += in.nextIfNotPrecededBy(quoteChar, escChar, endChar1, endChar2, false, 0, dst);
				}
				else {
					count += in.nextIfNotPrecededBy(quoteChar, escChar, endChar1, endChar2, newlineChar, false, 0, dst);
				}
			}

			// quoted section, closing quote = end of string
			if(in.nextIf(quoteChar)) {
				if(dst != null) { dst.append(quoteChar); }

				count++;
				if(allowNewline) {
					count += in.nextIfNotPrecededBy(quoteChar, escChar, dropEscChars, 0, dst);
				}
				else {
					count += in.nextIfNotPrecededBy(quoteChar, escChar, newlineChar, dropEscChars, 0, dst);
				}

				// quote found, normal end of string
				if(in.nextIf(quoteChar)) {
					if(dst != null) { dst.append(quoteChar); }

					// require end character or end of input after closing quote, append the ending char if requested
					if(in.nextIf(endChar1, endChar2, appendEndChar ? dst : null) == 0 && throwIfNoEndChar) {
						throw new IllegalStateException("quoted strings must end after last quote");
					}
				}
				// if buffer empty or ending character is newline and newlines not allowed, throw error if requested
				else if((throwIfNoEndQuote || throwIfNoEndChar) && (!in.hasNext() || (!allowNewline && (endingNewline = in.nextIf(newlineChar))))) {
					if(endingNewline) {
						in.unread(1);
						throw new IllegalStateException("quoted string ended with newline where newline was not allowed");
					}
					throw new IllegalStateException("end of quoted string content reached without finding ending quote");
				}

				count++;
			}
			// if reading quoted string 
			else {
				if(readQuotedOnly && throwIfQuotedNoStartQuote) {
					throw new IllegalStateException("no quote string start character '" + quoteChar + "'");
				}

				// if read and append end character to {@code dst} or check and throw if no end char
				if(throwIfNoEndChar || appendEndChar) {
					boolean foundEndChar = in.nextIf(endChar1, endChar2, appendEndChar ? dst : null) == 1;
					// if end char read and not requested to append to {@code dst}, then unread
					if(!appendEndChar && foundEndChar) {
						in.unread(1);
					}

					// throw error if end char required and not found
					if(throwIfNoEndChar && !foundEndChar) {
						throw new IllegalStateException("string does not with ending character '" + endChar1 + "' or '" + endChar2 + "'");
					}
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		return count;
	}

}
