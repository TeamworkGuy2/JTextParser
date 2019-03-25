package twg2.parser.textParserUtils.test;

import java.util.function.Supplier;

import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.parser.textParser.TextIteratorParser;
import twg2.parser.textParserUtils.ReadNumber;

/**
 * @author TeamworkGuy2
 * @since 2019-03-24
 */
public class ReadNumberTest {

	@Test
	public void readNumberTest() {
		String[] inputs = {
				"",
				"0",
				"9876543210987654321098765432109876543210987654321098765432109876543210987654321098765432109876543210",
				"0.0",
				"1.234567890",
				"12",
				"090",
				"42 - 99",
				"0.0##",
				"12.5 *"
		};
		String[] expected = {
				"",
				"0",
				"9876543210987654321098765432109876543210987654321098765432109876543210987654321098765432109876543210",
				"0.0",
				"1.234567890",
				"12",
				"090",
				"42",
				"0.0",
				"12.5"
		};

		CheckTask.assertTests(inputs, expected, (String str) -> {
			StringBuilder strB = new StringBuilder();
			TextIteratorParser in = TextIteratorParser.of(str);

			ReadNumber.readNumberInto(in, strB);
			String res = strB.toString();
			strB.setLength(0);

			return res;
		});

		// throws exception if number ends at decimal point without at-least one more digit
		CheckTask.assertException(() -> createReadNumberIntoFunc("0.").get());
		CheckTask.assertException(() -> createReadNumberIntoFunc("15.#").get());
	}


	private static Supplier<String> createReadNumberIntoFunc(String input) {
		return () -> {
			StringBuilder sb = new StringBuilder();
			TextIteratorParser in = TextIteratorParser.of(input);

			ReadNumber.readNumberInto(in, sb);
			return sb.toString();
		};
	}

}
