package twg2.parser.textStream;

import java.util.function.Supplier;

/** A {@link Supplier} that iterates through the lines in a string and returns each line as a new char[].<br>
 * @see LineSupplier
 * @threadSafety not thread safe
 * @author TeamworkGuy2
 * @since 2016-2-24
 */
public class CharsLineSupplier extends LineSupplier implements Supplier<char[]> {

	/**
	 * @see LineSupplier#LineSupplier(String, int, int, boolean, boolean, boolean, boolean)
	 */
	public CharsLineSupplier(String str, int off, int len, boolean includeEmptyLines, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		super(str, off, len, includeEmptyLines, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, collapseNewlinesIntoOneChar);
	}


	@Override
	public char[] get() {
		return super.readChars();
	}

}
