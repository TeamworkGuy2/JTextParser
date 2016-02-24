package twg2.parser.test;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.textParser.TextParser;
import twg2.parser.textParser.TextParserImpl;
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

		for(int i = 0, size= inputs.length; i < size; i++) {
			int res = ReadWhitespace.readWhitespace(inputs[i], off);
			Assert.assertEquals("input: " + inputs[i] + ", expect: " + expect[i] + ", result: " + res, expect[i], res);
		}
	}


	@Test
	public void readWhitespaceLineBufferTest() {
		TextParser[] inputs = {
				TextParserImpl.of("\t\t\t\t,"),
				TextParserImpl.of(" \n   ="),
				TextParserImpl.of("\n"),
				TextParserImpl.of("A")
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
			Assert.assertEquals("i=" + i + ", expect: " + expect[i] + ", result: " + res, expect[i], res);
		}
	}

}
