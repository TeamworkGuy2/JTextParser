package twg2.parser.textStream;

import java.util.function.Supplier;

/** A {@link Supplier} that iterates through the lines in a string.<br>
 * @see LineSupplier
 * @threadSafety not thread safe
 * @author TeamworkGuy2
 * @since 2015-1-31
 */
public class StringLineSupplier extends LineSupplier implements Supplier<String> {

	/**
	 * @see LineSupplier#LineSupplier(String, int, int, boolean, boolean, boolean, boolean)
	 */
	public StringLineSupplier(String str, int off, int len, boolean includeEmptyLines, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		super(str, off, len, includeEmptyLines, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, collapseNewlinesIntoOneChar);
	}


	@Override
	public String get() {
		return super.readString();
	}

}
