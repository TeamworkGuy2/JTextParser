package twg2.parser.textParserUtils.test;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.textParser.TextParser;
import twg2.parser.textParser.TextIteratorParser;
import twg2.parser.textParserUtils.ReadWhitespace;

/**
 * @author TeamworkGuy2
 * @since 2015-1-4
 */
public class ReadWhitespaceTest {

	@Test
	public void readWhitespaceStringTest() {
		int off = 2;
		String[] inputs = {
				"0[				]",
				"1. \n   ;",
				"2 \n",
				""
		};
		int[] expect = {
				4,
				5,
				1,
				0
		};

		Assert.assertEquals("array lengths", inputs.length, expect.length);

		for(int i = 0, size = inputs.length; i < size; i++) {
			int res = ReadWhitespace.readWhitespace(inputs[i], off);
			Assert.assertEquals("i=" + i + " input: " + inputs[i], expect[i], res);

			int res2 = ReadWhitespace.readWhitespace(inputs[i].toCharArray(), off);
			Assert.assertEquals("i=" + i + " input: " + inputs[i], expect[i], res2);
		}
	}


	@Test
	public void readWhitespaceLineBufferTest() {
		TextParser[] inputs = {
				TextIteratorParser.of("\t\t\t\t,"),
				TextIteratorParser.of(" \n   ="),
				TextIteratorParser.of("\n"),
				TextIteratorParser.of("A")
		};
		int[] expect = {
				4,
				5,
				1,
				0
		};

		Assert.assertEquals("array lengths", inputs.length, expect.length);

		for(int i = 0, size= inputs.length; i < size; i++) {
			int res = ReadWhitespace.readWhitespaceCustom(inputs[i], new char[] { ' ', '\t', '\n' }, 0);
			Assert.assertEquals("i=" + i, expect[i], res);
		}
	}

}
