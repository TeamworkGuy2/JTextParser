package twg2.parser.textParserUtils;

import java.util.function.Function;

/** A {@link Function} that takes a string and escapes unicode characters in it
 * @author TeamworkGuy2
 * @since 2014-9-1
 */
public class UnicodeEscapeDecoder implements Function<String, String> {

	@Override
	public String apply(String line) {
		StringBuilder buf = new StringBuilder(line.length());
		escape(line, 0, buf);
		return buf.toString();
	}


	public static void escape(String line, int off, StringBuilder dst) {
		char[] tmpEscCodePoint = new char[2];
		final int size = line.length();
		int read = off;
		for( ; read < size; ) {
			char ch = line.charAt(read);
			// If an escape characters is found, process an escape code or any unicode points
			if(ch == EscapeSequences.escapeStart) {
				// Read all escape slashes '\'
				int escapeCount = 1;
				read++;
				escapeCount += ReadRepeats.readRepeat(line, read, EscapeSequences.escapeStart, 0);

				// Pairs of escape slashes '\' form a single '\' escape character
				while(escapeCount >= 2) {
					dst.append(EscapeSequences.escapeStart);
					escapeCount -= 2;
				}
				// If odd number of escape slashes '\', then check for a valid unicode char point
				if((escapeCount & 0x01) == 1) {
					// Try reading the unicode character point 'u' after the last odd numbered escape slash '\'
					int count = ReadRepeats.readRepeat(line, read, EscapeSequences.unicodeStart, 0);
					read += count;
					// If the first character after the slash '\' is not 'u', try to read an escape character
					if(count == 0) {
						int escLen = ReadRepeats.readRepeat(line, read, EscapeSequences.escapeIdentifiers, 1, dst);
						String escapeStr = dst.substring(dst.length() - escLen);
						read += escLen;
						// An escape character (for example: \ r, \ t, \ ", etc)
						if(escapeStr.length() != 1) {
							throw new IllegalArgumentException("invalid character escape sequence");
						}
					}

					// Else check for four valid hexadecimal values, convert to a unicode character
					else {
						int unicodeLen = ReadRepeats.readRepeat(line, read, EscapeSequences.unicodeLiterals, 4, dst);
						String unicodeStr = dst.substring(dst.length() - unicodeLen);
						dst.setLength(dst.length() - unicodeLen);
						read += unicodeStr.length();

						// If there are an invalid number of hexidecimal values, throw an error
						if(unicodeStr.length() < 4) {
							throw new IllegalArgumentException("invalid unicode character");
						}

						int escNum = Integer.parseInt(unicodeStr, 16);
						int chCount = Character.toChars(escNum, tmpEscCodePoint, 0);

						dst.append(tmpEscCodePoint[0]);
						if(chCount > 1) {
							dst.append(tmpEscCodePoint[1]);
						}
					}
				}
			}
			// Else append a standard character to the string
			else {
				dst.append(ch);
				read++;
			}
			//System.out.println("Building: " + str.toString() + ",\t remaining: " + line.substring(read));
		}
	}

}