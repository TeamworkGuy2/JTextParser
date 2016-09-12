package twg2.parser.textStream.test;

import org.junit.Test;

import twg2.parser.textStream.CharsLineSupplier;
import twg2.streams.EnhancedIterator;

/**
 * @author TeamworkGuy2
 * @since 2016-2-24
 */
public class CharsLineSupplierTest {

	@Test
	public void testCharsLineSupplier() throws Exception {
		LineSupplierTest.runStringLineSupplierTests(CharsLineSupplierTest::createLineIter, (t) -> new String(t));
	}


	private static final EnhancedIterator<char[]> createLineIter(String str, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines,
			boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar) {
		CharsLineSupplier strs = new CharsLineSupplier(str, 0, str.length(), treatEmptyLineAsLine, treatEolNewlineAsTwoLines, includeNewlinesAtEndOfReturnedLines, collapseNewlinesIntoOneChar);
		return new EnhancedIterator<>(strs);
	}

}
