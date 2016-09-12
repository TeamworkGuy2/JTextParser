package twg2.parser.textStream.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;

import twg2.streams.EnhancedIterator;

/**
 * @author TeamworkGuy2
 * @since 2015-5-26
 */
public class LineSupplierTest {

	@FunctionalInterface
	public static interface LineSupplierFactory<T> {

		public EnhancedIterator<T> apply(String str, boolean treatEmptyLineAsLine, boolean treatEolNewlineAsTwoLines, boolean includeNewlinesAtEndOfReturnedLines, boolean collapseNewlinesIntoOneChar);

	}


	static final String[] inputStrs = new String[] {
			"-a\n-b\n-c",
			"\r\nnew\r\nvar\r\n",
			"\n123",
			"",
			"\n\n"
	};

	static final String[][] expected = new String[][] {
			{
				"-a", "-b", "-c"
			}, {
				"", "new", "var"
			}, {
				"", "123"
			}, {
				""
			}, {
				"", ""
			}
	};

	static final String[][] expectedEolNewLine = new String[][] {
			{
				"-a", "-b", "-c"
			}, {
				"", "new", "var", ""
			}, {
				"", "123"
			}, {
				""
			}, {
				"", "", ""
			}
	};

	static final String[][] expectedEolNewLineWithNewLine = new String[][] {
			{
				"-a\n", "-b\n", "-c"
			}, {
				"\n", "new\n", "var\n", ""
			}, {
				"\n", "123"
			}, {
				""
			}, {
				"\n", "\n", ""
			}
	};


	public static <T> void runStringLineSupplierTests(LineSupplierFactory<T> supplierFactory, Function<T, String> toString) throws Exception {

		int i = 0;

		EnhancedIterator<T> supplier = null;
		for(String strA : inputStrs) {
			supplier = supplierFactory.apply(strA, true, false, false, true);
			String[] linesAry = supplierLines(supplier, toString);

			Assert.assertArrayEquals(i + "", expected[i], linesAry);

			supplier = supplierFactory.apply(strA, true, true, false, true);
			String[] linesAry2 = supplierLines(supplier, toString);

			Assert.assertArrayEquals(i + "", expectedEolNewLine[i], linesAry2);

			supplier = supplierFactory.apply(strA, true, true, true, true);
			String[] linesAry3 = supplierLines(supplier, toString);

			Assert.assertArrayEquals(i + "", expectedEolNewLineWithNewLine[i], linesAry3);

			i++;
		}

		if(supplier != null) {
			supplier.close();
		}
	}


	private static final <T> String[] supplierLines(EnhancedIterator<T> supplier, Function<T, String> toString) {
		List<String> lines = new ArrayList<>();
		supplier.forEachRemaining((ln) -> lines.add(toString.apply(ln)));
		return lines.toArray(new String[lines.size()]);
	}

}
