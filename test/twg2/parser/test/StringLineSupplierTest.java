package twg2.parser.test;

import org.junit.Test;

import twg2.parser.textStream.StringLineSupplier;
import twg2.streams.EnhancedIterator;

/**
 * @author TeamworkGuy2
 * @since 2015-5-26
 */
public class StringLineSupplierTest {

	@Test
	public void stringLineSupplierTest() throws Exception {
		LineSupplierTest.runStringLineSupplierTests(StringLineSupplierTest::createLineIter, (t) -> t);
	}


	private static final EnhancedIterator<String> createLineIter(String str, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines,
			boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		StringLineSupplier strs = new StringLineSupplier(str, 0, str.length(), treatEmptyLineAsLine, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, collapseNewlinesIntoOneChar);
		return new EnhancedIterator<>(strs);
	}

}
