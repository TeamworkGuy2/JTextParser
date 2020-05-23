package twg2.parser.test;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.parser.textParser.TextCharsParser;
import twg2.parser.textParser.TextIteratorParser;
import twg2.parser.textParser.TextParser;
import twg2.parser.textParser.TextParserConditionalsDefault;
import twg2.parser.textParserUtils.ReadMatching;

public class TextParserTest {

	@Test
	public void readMatchingString() {
		runTests(this::readMatchingString);
	}


	public void readMatchingString(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
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
			TextParser in = parserFactory.apply(inputs[i]);
			Assert.assertEquals("i=" + i + ". read matching string", (expect[i].length() > 0), in.nextIf(match[i]));
			Assert.assertEquals("i=" + i + ". next char does not match", expectNext[i], (in.hasNext() ? in.nextChar() : 0));
		}
	}


	@Test
	public void readMatchingChar() {
		runTests(this::readMatchingChar);
	}


	public void readMatchingChar(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
		int readLimit = 4;
		String[] inputs = new String[] {
				"Object a",
				"ABAAA",
				"ABBB",
				"ABCACC",
				"--.-..",
				""
		};
		char[][] match = new char[][] {
				{ 'O', 'b', 'j' },
				{ 'A', 'B' },
				{ 'A', 'B' },
				{ 'A', 'B', 'C' },
				{ '-' },
				{ 'A' },
		};
		char[] expectLimitedNext = new char[] {
				'e',
				'A',
				0,
				'C',
				'.',
				0
		};
		String[] expectLimited = new String[] {
				"Obj",
				"ABAA",
				"ABBB",
				"ABCA",
				"--",
				""
		};
		String[] expect = new String[] {
				"Obj",
				"ABAAA",
				"ABBB",
				"ABCACC",
				"--",
				""
		};
		Assert.assertEquals(inputs.length, match.length);
		Assert.assertEquals(inputs.length, expectLimitedNext.length);
		Assert.assertEquals(inputs.length, expectLimited.length);
		Assert.assertEquals(inputs.length, expect.length);

		for(int i = 0, size = inputs.length; i < size; i++) {
			// nextIf() limit
			TextParser in = parserFactory.apply(inputs[i]);

			int readRes = match[i].length == 1 ? in.nextIf(match[i][0], readLimit, null)
					: match[i].length == 2 ? in.nextIf(match[i][0], match[i][1], readLimit, null)
					: i % 2 == 0 ? in.nextIf(match[i], readLimit, null)
					: in.nextIf(match[i], 0, match[i].length, readLimit, null);

			Assert.assertEquals("i=" + i + ". read matching string", expectLimited[i].length(), readRes);
			Assert.assertEquals("i=" + i + ". next char does not match", expectLimitedNext[i], (in.hasNext() ? in.nextChar() : 0));

			// nextIf() unlimited
			in = parserFactory.apply(inputs[i]);

			readRes = match[i].length == 1 ? in.nextIf(match[i][0], 0, null)
					: match[i].length == 2 ? in.nextIf(match[i][0], match[i][1], 0, null)
					: i % 2 == 0 ? in.nextIf(match[i], 0, null)
					: in.nextIf(match[i], 0, match[i].length, 0, null);

			Assert.assertEquals("i=" + i + ". read matching string", expect[i].length(), readRes);
		}
	}


	@Test
	public void unescapeTest() {
		runTests(this::unescapeTest);
	}


	public void unescapeTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
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
			ReadMatching.readUnescape(parserFactory.apply(inputs[i]), '\\', '"', false, '\n', dst);
			Assert.assertEquals(expect[i], dst.toString());
			dst.setLength(0);
		}
	}


	@Test
	public void partialEscapedTest() {
		runTests(this::partialEscapedTest);
	}


	public void partialEscapedTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
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
			TextParser in = parserFactory.apply(inputs[i]);
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
	public void positionTest() {
		runTests(this::positionTest);
	}


	public void positionTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
		String str =
			"\tstring a\n" +
			"\tstring b\n" +
			"3\n";
		TextParser buf = parserFactory.apply(str);

		Assert.assertFalse(buf.hasPrevChar());
		Assert.assertEquals(offset - 1, buf.getPosition());
		buf.hasNext();
		Assert.assertEquals('\t', buf.nextChar());
		Assert.assertFalse(buf.hasPrevChar());
		Assert.assertEquals(offset + 0, buf.getPosition());
		Assert.assertEquals(1, buf.getLineNumber());
		Assert.assertEquals(1, buf.getColumnNumber());

		String line = buf.readLine();
		Assert.assertEquals("string a", line);
		Assert.assertEquals('\t', buf.nextChar());
		Assert.assertEquals(2, buf.getLineNumber());
		buf.skip(7);
		Assert.assertTrue(buf.hasPrevChar());
		Assert.assertEquals('g', buf.prevChar());
		Assert.assertEquals('b', buf.nextChar());
		buf.unread(2);
		Assert.assertEquals(' ', buf.nextChar());
		buf.skip(1);
		Assert.assertEquals(offset + 18, buf.getPosition());
		Assert.assertEquals(2, buf.getLineNumber());
		Assert.assertEquals(9, buf.getColumnNumber());

		Assert.assertEquals('\n', buf.nextChar());
		Assert.assertEquals('3', buf.nextChar());
		Assert.assertEquals(3, buf.getLineNumber());
		Assert.assertEquals(1, buf.getColumnNumber());

		var success = false;
		try {
			buf.unread(2);
			success = true;
		} catch(Exception e) {
			// not supported by TextIteratorParser
		}
		if(success) {
			Assert.assertEquals('\n', buf.nextChar());
			Assert.assertEquals('3', buf.nextChar());
			Assert.assertEquals(3, buf.getLineNumber());
			Assert.assertEquals(1, buf.getColumnNumber());
		}
	}


	@Test
	public void readLineTest() {
		runTests(this::readLineTest);
	}


	public void readLineTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
		String[] expect = {
				"\tstring a",
				"\tstring b",
				"",
				"3"
		};
		TextParser buf = parserFactory.apply(String.join("\n", expect));

		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(buf.hasNext()) {
			buf.readLine(sb);

			Assert.assertEquals("lines not equal", expect[i], sb.toString());
			sb.setLength(0);
			i++;
		}
	}


	@Test
	public void readLinesTest() {
		runTests(this::readLinesTest);
	}


	public void readLinesTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
		TextParser buf = parserFactory.apply("\n\n\n");
		StringBuilder sb = new StringBuilder();
		while(buf.hasNext()) {
			sb.append(buf.nextChar());
		}
		Assert.assertEquals("lines not equal", "\n\n\n", sb.toString());
	}


	@Test
	public void lineReminaingTest() {
		String[] expect = {
				"\tstring a\n",
				"abc\n",
				"3"
		};
		TextIteratorParser buf = TextIteratorParser.of(String.join("", expect));

		int i = 0;
		while(buf.hasNext()) {
			int charI = 0;
			int curLineLen = expect[i].length();
			// peek ahead to initialize the next line, because getLineRemaining() == 0 until some call advances the line buffer to the next line
			buf.nextChar();
			buf.unread(1);
			while(buf.getLineRemaining() > 0) {
				Assert.assertEquals(curLineLen - charI, buf.getLineRemaining());
				buf.nextChar();
				charI++;
			}
			i++;
		}
	}


	@Test
	public void readCountTest() {
		runTests(this::readCountTest);
	}


	public void readCountTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
		String[] expect = {
				"boolean a\n",
				"int b\n",
				"3"
		};
		TextParserConditionalsDefault buf = parserFactory.apply(String.join("", expect));

		StringBuilder sb = new StringBuilder();
		buf.nextChar();
		Assert.assertEquals(9, buf.readCount(9, sb)); // TODO TextIteratorParse can't read past new lines
		Assert.assertEquals("oolean a\n", sb.toString());
		buf.nextChar();
		Assert.assertEquals(5, buf.readCount(5, sb));
		Assert.assertEquals("oolean a\nnt b\n", sb.toString());
		buf.nextChar();
		Assert.assertEquals(0, buf.readCount(99, sb));
		Assert.assertEquals("oolean a\nnt b\n", sb.toString());
	}


	@Test
	public void readNextBetweenTest() {
		runTests(this::readNextBetweenTest);
	}


	public void readNextBetweenTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
		TextParserConditionalsDefault buf;
		StringBuilder sb = new StringBuilder();

		buf = parserFactory.apply("123 123");
		buf.nextBetween('0', '9', 5, sb);
		Assert.assertEquals("123", sb.toString());
		buf.unread(buf.getPosition() - offset + 1);
		Assert.assertEquals(3, buf.nextBetween('0', '9', 5, null));
		sb.setLength(0);

		buf = parserFactory.apply("a1b2cc3 4d");
		buf.nextBetween('0', '9', 'a', 'z', 5, sb);
		Assert.assertEquals("a1b2c", sb.toString());
		buf.unread(buf.getPosition() - offset + 1);
		Assert.assertEquals(5, buf.nextBetween('0', '9', 'a', 'z', 5, null));
		sb.setLength(0);

		buf = parserFactory.apply("a1b2cc3 4d");
		buf.nextBetween('0', '9', 'a', 'z', 10, sb);
		Assert.assertEquals("a1b2cc3", sb.toString());
		buf.unread(buf.getPosition() - offset + 1);
		Assert.assertEquals(7, buf.nextBetween('0', '9', 'a', 'z', 10, null));
		sb.setLength(0);
	}


	@Test
	public void readNextIfNotPrecededByTest() {
		runTests(this::readNextIfNotPrecededByTest);
	}


	public void readNextIfNotPrecededByTest(Function<String, TextParserConditionalsDefault> parserFactory, int offset) {
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

		CheckTask.assertTests(inputs, expectNotEscaped, (str) -> {
			StringBuilder sb = new StringBuilder();
			TextParser buf = parserFactory.apply(str);
			buf.nextIfNotPrecededBy(endCh, escCh, stopCh, false, 0, sb);
			return sb.toString();
		});

		String[] expectEscaped = {
			"a ",
			"1 2",
			"a b'c'",
			"a"
		};

		CheckTask.assertTests(inputs, expectEscaped, (str) -> {
			TextParser buf = parserFactory.apply(str);
			StringBuilder sb = new StringBuilder();
			buf.nextIfNotPrecededBy(endCh, escCh, stopCh, true, 0, sb);
			return sb.toString();
		});
	}


	// package-private
	void runTests(BiConsumer<Function<String, TextParserConditionalsDefault>, Integer> testFunc) {
		testFunc.accept((str) -> TextIteratorParser.of(str), 0);
		testFunc.accept((str) -> TextCharsParser.of(str), 0);
		testFunc.accept((str) -> TextIteratorParser.of("~~~" + str, 3, str.length()), 0);
		testFunc.accept((str) -> TextCharsParser.of(("~~~" + str).toCharArray(), 3, str.length()), 3);
	}

}
