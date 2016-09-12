package twg2.parser.textStream;

import java.io.BufferedReader;

/** Logic for reading a source string and spliting it into lines.<br>
 * Lines are separated by {@code '\n'}, {@code '\r'}, or {@code "\r\n"},
 * similar to the behavior of {@link BufferedReader}.<br>
 * This class is basically a shortcut for (except it returns char[] instead of String):
 * <pre>new BufferedReader(new StringReader(str)).lines().</pre><br>
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
	private static final char[] EMPTY_CHAR_ARRAY = new char[0];
	private String src;
	private StringBuilder tmpBuf = new StringBuilder(64);
	private int off;
	private int max;
	private boolean treatEmptyLineAsLine;
	private boolean treatEolNewlineAsTwoLines;
	private boolean includeNewlinesAtEndOfReturnedLines;
	private boolean collapseNewlinesIntoOneChar;


	/**
	 * @param str
	 * @param off
	 * @param len
	 * @param treatEmptyLineAsLine true if empty lines, such as {@code ""} should be considered 1 line,
	 * false if no lines should be returned
	 * @param treatEolNewlineAsTwoLines true if lines ending with a newline, such as {@code "...\n"} should be considered 2 lines,
	 * false if it should only be treated as 1 line
	 */
	public LineSupplier(String str, int off, int len, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		this.src = str;
		this.off = off;
		this.max = off + len;
		this.treatEmptyLineAsLine = treatEmptyLineAsLine;
		this.treatEolNewlineAsTwoLines = treatEolNewlineAsTwoLines;
		this.includeNewlinesAtEndOfReturnedLines = includeNewlinesAtEndOfReturnedLines;
		this.collapseNewlinesIntoOneChar = collapseNewlinesIntoOneChar;
	}


	public final char[] readChars() {
		return (char[])readCharsOrString(true);
	}


	public final String readString() {
		return (String)readCharsOrString(false);
	}


	protected final Object readCharsOrString(boolean returnCharArray) {
		String str = src;
		int i = off;
		int maxI = max;
		StringBuilder buf = tmpBuf;
		buf.setLength(0);

		if(i >= maxI) {
			// handle empty lines
			if(this.treatEmptyLineAsLine && i == 0 && maxI == 0) {
				this.off = maxI + 1;
				return returnCharArray ? EMPTY_CHAR_ARRAY : "";
			}
			// handle lines ending with "...\n", such a string would be considered 2 lines
			if(this.treatEolNewlineAsTwoLines && maxI > 0 && i == maxI) {
				this.off = maxI + 1;
				char ch = str.charAt(i - 1);
				if(ch == '\n' || ch == '\r') {
					return returnCharArray ? EMPTY_CHAR_ARRAY : "";
				}
			}
			this.tmpBuf = null;
			return null;
		}

		for(; i < maxI; i++) {
			char ch = str.charAt(i);
			if(ch == '\n' || ch == '\r') {
				if(ch == '\r' && i + 1 < maxI && str.charAt(i + 1) == '\n') {
					i++;
					// additionally, include newline chars at end of each line if requested
					if(includeNewlinesAtEndOfReturnedLines) {
						if(collapseNewlinesIntoOneChar) {
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
					if(includeNewlinesAtEndOfReturnedLines) {
						buf.append('\n');
					}
				}
				i++;
				break;
			}
			buf.append(ch);
		}

		this.off = i;

		if(returnCharArray) {
			int bufLen = buf.length();
			char[] chs = new char[bufLen];
			buf.getChars(0, bufLen, chs, 0);
			return chs;
		}
		else {
			String res = buf.toString();
			return res;
		}
	}


}
