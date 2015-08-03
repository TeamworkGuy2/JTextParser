package parserUtils;

import java.util.function.Function;

/** Static decoder functions for character data such as escape sequences (i.e. {@code \t, \r, \n}, etc.)
 * @author TeamworkGuy2
 * @since 2014-9-1
 */
public final class EscapeSequences {
	public static final char newline = '\n';
	public static final char escapeStart = '\\';
	public static final char unicodeStart = 'u';
	/** the valid escape characters that can come after a '\', other than unicode escape sequences */
	public static final char[] escapeIdentifiers = new char[] { 't', 'b', 'n', 'r', 'f', '\'', '\"', '\\' };
	/** the escape characters created by the escape sequences in {@link #escapeIdentifiers} */
	public static final char[] escapeChars = new char[] { '\t', '\b', '\n', '\r', '\f', '\'', '\"', '\\' };
	public static final char[] unicodeLiterals = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final UnicodeEscapeDecoder defaultDecoderInst = new UnicodeEscapeDecoder();


	private EscapeSequences() { throw new AssertionError("cannot instantiate static class EscapeSequences"); }


	/** Returns a function that decodes unicode escape sequences and escape identifiers in a string.<br>
	 * The escape sequences (e.g. {@code \ b, \ t}) must be in the Java 8 language specification (§3.10.6) format.
	 * Note: octal escape sequences are not supported.
	 * The unicode escape sequences must be in the format ({@code \ u x x x}) specified by the
	 * Java 8 language specification (§3.3) - Unicode Escapes
	 * @return the input string with all unicode escape sequences replace with corresponding characters
	 */
	public static final Function<String, String> unicodeEscapeDecoder() {
		return defaultDecoderInst;
	}


	/** A {@link Function} that takes a string and escapes unicode characters in it
	 * @author TeamworkGuy2
	 * @since 2014-9-1
	 */
	public static class UnicodeEscapeDecoder implements Function<String, String> {

		@Override
		public String apply(String line) {
			return escape(line, 0);
		}


		public static String escape(String line, int off) {
			final int size = line.length();
			StringBuilder strB = new StringBuilder(size);
			int read = off;
			for( ; read < size; ) {
				char curChar = line.charAt(read);
				// If an escape characters is found, process an escape code or any unicode points
				if(curChar == escapeStart) {
					// Read all escape slashes '\'
					int escapeCount = 1;
					read++;
					escapeCount += ReadRepeats.readRepeat(line, read, escapeStart, 0);

					// Pairs of escape slashes '\' form a single '\' escape character
					while(escapeCount >= 2) {
						strB.append(escapeStart);
						escapeCount -= 2;
					}
					// If odd number of escape slashes '\', then check for a valid unicode char point
					if((escapeCount & 0x01) == 1) {
						// Try reading the unicode character point 'u' after the last odd numbered escape slash '\'
						int count = ReadRepeats.readRepeat(line, read, unicodeStart, 0);
						read += count;
						// If the first character after the slash '\' is not 'u', try to read an escape character
						if(count == 0) {
							int escLen = ReadRepeats.readRepeat(line, read, escapeIdentifiers, 1, strB);
							String escapeStr = strB.substring(strB.length() - escLen);
							read += escLen;
							//System.out.println("Found escape: [" + escapeStr + "],\t remaining: " + line.substring(read));
							// An escape character (for example: \ r, \ t, \ ", etc)
							if(escapeStr.length() != 1) {
								throw new IllegalArgumentException("invalid character escape sequence");
							}
						}

						// Else check for four valid hexidecimal values, convert to a unicode character
						else {
							int unicodeLen = ReadRepeats.readRepeat(line, read, unicodeLiterals, 4, strB);
							String unicodeStr = strB.substring(strB.length() - unicodeLen);
							//System.out.println("Found unicode: " + line.substring(read));
							read += unicodeStr.length();
	
							// If there are an invalid number of hexidecimal values, throw an error
							if(unicodeStr.length() < 4) {
								throw new IllegalArgumentException("invalid unicode character");
							}
							strB.append((char)Integer.parseInt(unicodeStr, 16));
						}
					}
				}
				// Else append a standard character to the string
				else {
					strB.append(curChar);
					read++;
				}
				//System.out.println("Building: " + str.toString() + ",\t remaining: " + line.substring(read));
			}
			return strB.toString();
		}

	}

}
