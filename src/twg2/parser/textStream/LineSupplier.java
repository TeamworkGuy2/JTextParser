package twg2.parser.textStream;

import java.io.BufferedReader;

/** Read and split a source string into lines of {@code String} or {@code char[]}.<br>
 * Lines are separated by {@code '\n'}, {@code '\r'}, or {@code "\r\n"},
 * similar to the behavior of {@link BufferedReader}.<br>
 * This class is basically a shortcut for (except it returns char[] instead of String):
 * <pre>new BufferedReader(new StringReader(str)).lines().</pre>
 * Note: if the last character of the input string is a newline, the empty string between
 * the last character and the end of the string can be ignored or read as an empty
 * line depending on constructor parameters.<br>
 * For example, the string {@code "a\nb\nc\n"} can be read as either 3 or 4 lines depending on parser parameters,
 * although {@code "a\nb\nc\nd"} will always be read unambiguously as 4 lines.<br>
 * @threadSafety not thread safe
 * @author TeamworkGuy2
 * @since 2015-1-31
 */
public class LineSupplier {
	protected static final char[] EMPTY_CHAR_ARRAY = new char[0];
	protected String src;
	protected StringBuilder tmpBuf = new StringBuilder(120);
	protected int off;
	protected int max;
	protected boolean includeEmptyLines;
	protected boolean treatEolNewlineAsTwoLines;
	protected boolean includeNewlinesAtEndOfReturnedLines;
	protected boolean collapseNewlinesIntoOneChar;


	/** Create a LineSupplier based on a sub-sequence of a string.
	 * @param str the source string
	 * @param off offset into the source string at which to start
	 * @param len the number of characters to read from the source string
	 * @param includeEmptyLines true if empty lines, such as {@code ""} should be considered 1 line,
	 * false if no lines should be returned
	 * @param treatEolNewlineAsTwoLines true if lines ending with a newline, such as {@code "...\n"} should be considered 2 lines,
	 * false if it should only be treated as 1 line
	 */
	public LineSupplier(String str, int off, int len, boolean includeEmptyLines, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		this.src = str;
		this.off = off;
		this.max = off + len;
		this.includeEmptyLines = includeEmptyLines;
		this.treatEolNewlineAsTwoLines = treatEolNewlineAsTwoLines;
		this.includeNewlinesAtEndOfReturnedLines = includeNewlinesAtEndOfReturnedLines;
		this.collapseNewlinesIntoOneChar = collapseNewlinesIntoOneChar;
	}


	public final char[] readChars() {
		int read = readLineIntoBuffer(tmpBuf);

		if(read < 0) {
			return null;
		}
		else if(read == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		else {
			char[] chs = new char[read];
			tmpBuf.getChars(0, read, chs, 0);
			return chs;
		}
	}


	public final String readString() {
		int read = readLineIntoBuffer(tmpBuf);

		if(read < 0) {
			return null;
		}
		else if(read == 0) {
			return "";
		}
		else {
			return tmpBuf.toString();
		}
	}


	/** Read chars into a destination buffer.
	 * @param buf the destination buffer to insert read chars into, the buffer is cleared first
	 * @return -1 if no data could be read, 0 for an empty line (no data insert into {@code dstBuf}), a positive number equal to the length of {@code dstBuf} if data was successfully read into {@code dstBuf} 
	 */
	protected final int readLineIntoBuffer(StringBuilder buf) {
		String str = src;
		int i = off;
		int maxI = max;
		buf.setLength(0);

		if(i >= maxI) {
			// handle empty lines
			if(this.includeEmptyLines && i == 0 && maxI == 0) {
				this.off = maxI + 1;
				return 0;
			}
			// handle lines ending with "...\n", such a string would be considered 2 lines
			if(this.treatEolNewlineAsTwoLines && maxI > 0 && i == maxI) {
				this.off = maxI + 1;
				char ch = str.charAt(i - 1);
				if(ch == '\n' || ch == '\r') {
					return 0;
				}
			}
			return -1;
		}

		for(; i < maxI; i++) {
			char ch = str.charAt(i);
			if(ch == '\n' || ch == '\r') {
				if(ch == '\r' && i + 1 < maxI && str.charAt(i + 1) == '\n') {
					i++;
					// additionally, include newline chars at end of each line if requested
					if(this.includeNewlinesAtEndOfReturnedLines) {
						if(this.collapseNewlinesIntoOneChar) {
							buf.append('\n');
						}
						else {
							buf.append('\r');
							buf.append('\n');
						}
					}
				}
				else if(ch == '\n') {
					// additionally, include newline chars at end of each line if requested
					if(this.includeNewlinesAtEndOfReturnedLines) {
						buf.append('\n');
					}
				}
				i++;
				break;
			}
			buf.append(ch);
		}

		this.off = i;

		return buf.length();
	}

}
