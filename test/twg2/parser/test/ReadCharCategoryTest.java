package twg2.parser.test;

import org.junit.Test;

import twg2.parser.textParser.TextParser;
import twg2.parser.textParser.TextParserImpl;
import twg2.ranges.helpers.CharCategory;
import checks.Check;

/**
 * @author TeamworkGuy2
 * @since 2016-1-13
 */
public class ReadCharCategoryTest {

	@Test
	public void readCharTypeTest() {
		CharCategory[] types = new CharCategory[] {
				CharCategory.ALPHA_LOWER,
				CharCategory.ALPHA_UPPER,
				CharCategory.ALPHA_LOWER,
				CharCategory.DIGIT,
				CharCategory.DIGIT,
				CharCategory.ALPHA_UPPER_OR_LOWER
		};
		String[] strs = new String[] {
				"characterswithoutspaces",
				"ALPHAUPPERCHARACTHERS",
				"with spaces",
				"932421",
				"7252_312",
				"AlphaWITHlowerCase"
		};
		String[] expect = new String[] {
				"characterswithoutspaces",
				"ALPHAUPPERCHARACTHERS",
				"with",
				"932421",
				"7252",
				"AlphaWITHlowerCase"
		};
		Check.assertLengths("types, strs, expect arrays must be the same length", types.length, strs.length, expect.length);

		StringBuilder dst = new StringBuilder();
		for(int i = 0, size = strs.length; i < size; i++) {
			TextParser tool = TextParserImpl.of(strs[i]);
			tool.nextIf(types[i], dst);

			Check.assertEqual(dst.toString(), expect[i], "");
			dst.setLength(0);
		}
	}

}
