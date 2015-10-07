package twg2.parser.textParserUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

import twg2.parser.textParser.TextParser;

/**
 * @author TeamworkGuy2
 * @since 2015-6-21
 */
public class ReadNumber {


	public static final int readIntInto(TextParser in, Appendable dst) {
		return readNumberInto(in, dst);
	}


	public static final int readDecimalInto(TextParser in, Appendable dst) {
		return readNumberInto(in, dst);
	}


	public static final int readNumberInto(TextParser in, Appendable dst) {
		@SuppressWarnings("unused")
		boolean containsDecimal = false;
		int read = 0;

		try {
			int tmp = 0;
			if((tmp = in.nextBetween('0', '9', 0, dst)) > 0) {
				read += tmp;
				// read integer value
				if(in.nextIf('.')) {
					read++;
					containsDecimal = true;
					dst.append('.');
					// read decimal value
					if((tmp = in.nextBetween('0', '9', 0, dst)) > 0) {
						read += tmp;
					}
					else {
						throw new IllegalArgumentException("incorrect number format, cannot end with decimal point '.' and no further digits");
					}
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		return read;
	}

}
