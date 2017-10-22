package twg2.parser.textParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.PushbackReader;

import twg2.parser.textStream.LineCounter;

/** A buffered reader like class that reads lines and also allows the current line
 * to be fully or partially unread.
 * @see PushbackReader
 * @see BufferedReader
 * @author TeamworkGuy2
 * @since 2013-12-10
 */
public interface TextParser extends LineReader, TextParserConditionals, Closeable {

	/**
	 * @return get line number counter which tracks the current line and column number position of this text parser
	 */
	public LineCounter getLineNumbers();


	/**
	 * @return the remainder of the buffer's current line of text, excluding the ending '\n'
	 */
	public default String readLine() {
		StringBuilder strB = new StringBuilder();
		readLine(strB);
		return strB.toString();
	}


	/** Read the remainder of the buffer's current line of text, excluding the ending '\n' and write the characters to {@code dst}
	 * @param dst write the characters to this destination
	 */
	public default void readLine(Appendable dst) {
		nextIfNot('\n', 0, dst);
		nextIf('\n');
	}


	public default String getPositionDisplayText() {
		return this.getLineNumber() + ":" + this.getColumnNumber();
	}

}
