package twg2.parser.textParserUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

import twg2.parser.textParser.LineReader;

/**
 * @author TeamworkGuy2
 * @since 2015-7-15
 */
public class ReadPeek {

	private ReadPeek() { throw new AssertionError("cannot instantiate static class ReadPeek"); }


	public static char peekNext(LineReader in) {
		char ch = in.nextChar();
		in.unread(1);
		return ch;
	}


	public static int peekNext(LineReader in, int count, Appendable dst) {
		int i = 0;
		try {
			for( ; i < count && in.hasNext(); i++) {
				char ch = in.nextChar();
				dst.append(ch);
			}
		} catch(IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
		return i;
	}

}
