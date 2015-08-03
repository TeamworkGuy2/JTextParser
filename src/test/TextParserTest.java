package test;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import parserUtils.ReadMatching;
import parserUtils.ReadPeek;
import textParser.TextParserImpl;
import checks.CheckTask;

public class TextParserTest {


	@Test
	public void unescapeTest() {
		String[] inputs = new String[] {
				"a \\\"block\\\" char '\\\"'",
				" \\\\abc",
				" \\abc",
				"\\\"",
		};
		String[] expect = new String[] {
				"a \"block\" char '\"'",
				" \\abc",
				" abc",
				"\"",
		};
		Assert.assertEquals("array length", inputs.length, expect.length);
		StringBuilder dst = new StringBuilder();

		for(int i = 0, size = inputs.length; i < size; i++) {
			ReadMatching.readUnescape(TextParserImpl.of(inputs[i]), '\\', '"', false, '\n', dst);
			Assert.assertEquals(expect[i], dst.toString());
			dst.setLength(0);
		}
	}


	@Test
	public void readMatchingString() {
		String[] inputs = new String[] {
				"Object a",
				"things and stuff",
				"ABC",
				"ABC",
				"X",
				"",
				""
		};
		String[] match = new String[] {
				"Object",
				"things and stuff",
				"A",
				"&",
				"X",
				"Y",
				""
		};
		char[] expectNext = new char[] {
				' ',
				0,
				'B',
				'A',
				0,
				0,
				0
		};
		String[] expect = new String[] {
				"Object",
				"things and stuff",
				"A",
				"",
				"X",
				"",
				""
		};
		Assert.assertEquals("array length 1", inputs.length, expect.length);
		Assert.assertEquals("array length 2", match.length, expectNext.length);
		Assert.assertEquals("array length 3", inputs.length, match.length);

		for(int i = 0, size = inputs.length; i < size; i++) {
			TextParserImpl in = TextParserImpl.of(inputs[i]);
			Assert.assertEquals("read matching string (index: " + i + ")", (expect[i].length() > 0), in.nextIf(match[i]));
			Assert.assertEquals("next char does not match (index: " + i + ")", expectNext[i], (in.hasNext() ? in.nextChar() : 0));
		}
	}


	@Test
	public void partialEscapedTest() {
		String[] inputs = new String[] {
				"stuff \"with quotes\" other",
				"first, next",
				"\"a b c\" last"
		};
		String[] expect = new String[] {
				"stuff \"with quotes\"",
				"first",
				"\"a b c\""
		};
		Assert.assertEquals("array length", inputs.length, expect.length);

		for(int i = 0, size = inputs.length; i < size; i++) {
			StringBuilder dst = new StringBuilder();
			TextParserImpl in = TextParserImpl.of(inputs[i]);
			in.nextIfNot(',', '"', 0, dst);
			if(in.nextIf('"')) {
				dst.append('"');
				ReadMatching.readUnescape(in, '\\', '"', false, '\n', dst);
				dst.append('"');
			}
			Assert.assertEquals(expect[i], dst.toString());
		}
	}


	@Test
	public void testReadLines() {
		{
			TextParserImpl buf = TextParserImpl.of("\tstring a\n\tstring b\n3");
			String[] expect = {
					"\tstring a\n",
					"\tstring b\n",
					"3"
			};
			StringBuilder strB = new StringBuilder();
			int i = 0;
			while(buf.hasNext()) {
				strB.append(buf.nextChar());
				if(buf.getLineRemaining() == 0) {
					Assert.assertEquals("lines not equal", expect[i], strB.toString());
					strB.setLength(0);
					i++;
				}
			}
		}

		{
			TextParserImpl buf = TextParserImpl.of("\n\n\n");
			String expect = "\n\n\n";
			StringBuilder strB = new StringBuilder();
			while(buf.hasNext()) {
				char ch = buf.nextChar();
				strB.append(ch);
			}
			Assert.assertEquals("lines not equal", expect, strB.toString());
		}
	}


	@Test
	public void testReadLine() {
		TextParserImpl buf = TextParserImpl.of("\tstring a\n\tstring b\n3");
		String[] expect = {
				"\tstring a",
				"\tstring b",
				"3"
		};

		StringBuilder strB = new StringBuilder();
		int i = 0;
		while(buf.hasNext()) {
			buf.readLine(strB);

			Assert.assertEquals("lines not equal", expect[i], strB.toString());
			strB.setLength(0);
			i++;
		}
	}


	@Test
	public void testLineReminaing() {
		TextParserImpl buf = TextParserImpl.of("\tstring a\nabc\n3");
		String[] expect = {
				"\tstring a\n",
				"abc\n",
				"3"
		};

		StringBuilder strB = new StringBuilder();
		int i = 0;
		while(buf.hasNext()) {
			int charI = 0;
			int curLineLen = expect[i].length();
			@SuppressWarnings("unused")
			int remaining = -1;
			// peek ahead to initialize the next line, because getLineRemaining() == 0 until some call advances the line buffer to the next line
			ReadPeek.peekNext(buf);
			while((remaining = buf.getLineRemaining()) > 0) {
				Assert.assertEquals(curLineLen - charI, buf.getLineRemaining());
				charI++;
			}
			i++;
		}

	}


	@Test
	public void readNextIfNotPrecededByTest() {
		char endCh = '\'';
		char escCh = '\\';
		char stopCh = ',';

		String[] inputs = {
			"a '1 2', ",
			"1 2', ",
			"a b\\'c\\'' d",
			"a, b'"
		};

		String[] expectNotEscaped = {
			"a ",
			"1 2",
			"a b\\'c\\'",
			"a"
		};

		CheckTask.assertTests(inputs, expectNotEscaped, createReadNextIfNotPrecededByTask1(endCh, escCh, stopCh, false));

		String[] expectEscaped = {
			"a ",
			"1 2",
			"a b'c'",
			"a"
		};

		CheckTask.assertTests(inputs, expectEscaped, createReadNextIfNotPrecededByTask1(endCh, escCh, stopCh, true));
	}


	private Function<String, String> createReadNextIfNotPrecededByTask1(char endChar, char escChar, char stopChar, boolean dropEscChar) {
		return (str) -> {
			TextParserImpl buf = TextParserImpl.of(str);
			StringBuilder strB = new StringBuilder();
			buf.nextIfNotPrecededBy(endChar, escChar, stopChar, dropEscChar, 0, strB);
			return strB.toString();
		};
	}

}
