package twg2.parser.textParserUtils.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.parser.textParser.TextIteratorParser;
import twg2.parser.textParserUtils.ReadUnescape;

/**
 * @author TeamworkGuy2
 * @since 2015-6-16
 */
public class ReadUnescapeTest {
	// test data copied from JTextUtil twg2.text.test.DataUnescapePartialQuoted.java
	public static int inputsOffset = 3;

	public static List<String> inputs = Arrays.asList(
		"0. with \"quoted block\" end",
		"1.  \\abc, xyz",
		"2.  abc, xyz",
		"3.  \"abc, xyz\"], ",
		"4. \\\"\"",
		"5. \"a, b\", c",
		"6. , "
	);

	public static List<String> expected = Arrays.asList(
		"with \"quoted block\"",
		" \\abc",
		" abc",
		" \"abc, xyz\"",
		"\\\"\"",
		"\"a, b\"",
		""
	);

	/** The index of the first ending character (i.e. if the string is a quoted string followed by an ending char, return the index of the closing char, not the ending char */
	public static List<Integer> expectedIndexesIncludingQuote = Arrays.asList(
		inputs.get(0).lastIndexOf("\"") + 1,
		inputs.get(1).lastIndexOf(','),
		inputs.get(2).lastIndexOf(','),
		inputs.get(3).lastIndexOf("]"),
		inputs.get(4).lastIndexOf("\"") + 1,
		inputs.get(5).lastIndexOf("\"") + 1,
		inputs.get(6).lastIndexOf(',')
	);

	/** The index of the last ending character (i.e. if the string is a quoted string followed by an ending char, return the index of the ending char, not the closing quote */
	public static List<Integer> expectedIndexesIncludingTrueEndingChar = Arrays.asList(
		inputs.get(0).lastIndexOf("\"") + 1,
		inputs.get(1).lastIndexOf(','),
		inputs.get(2).lastIndexOf(','),
		inputs.get(3).lastIndexOf("]") + 1,
		inputs.get(4).lastIndexOf("\"") + 1,
		inputs.get(5).lastIndexOf("\"") + 2,
		inputs.get(6).lastIndexOf(',')
	);


	@Test
	public void readUnescapePartialQuotedTest2() {
		int offset = ReadUnescapeTest.inputsOffset;
		List<String> inputs = ReadUnescapeTest.inputs;
		List<String> expect = ReadUnescapeTest.expected;
		List<Integer> expectIdxs = ReadUnescapeTest.expectedIndexesIncludingTrueEndingChar;
		StringBuilder dst = new StringBuilder();

		for(int i = 0, size = inputs.size(); i < size; i++) {
			var sb = new StringBuilder();
			var in = TextIteratorParser.of(inputs.get(i), offset, inputs.get(i).length() - offset);

			// TODO also check the return count from readUnescapePartialQuoted()
			ReadUnescape.readUnescapePartialQuoted(in, '"', '\\', ',', ']', sb);

			int index = in.getPosition();
			Assert.assertEquals("#" + i + ".", expect.get(i), sb.toString());
			Assert.assertEquals(i + ". expect (" + expectIdxs.get(i) + "): " + expect.get(i) + ", result (" + (index + offset + 1) + "): " + sb.toString(), (int)expectIdxs.get(i), index + offset + 1);
			dst.setLength(0);
		}
	}


	@Test
	public void readUnescapePartialQuotedTest() {
		String[] inputs = {
				"key: \"value, (stuff)",
				"key: \"value, (stuff)\", mk",
				"\"value, a\"",
				"\"value, a\") A",
				"\"a, b)",
				"\"a, b) A",
				"a, b)",
				"a\\, b)"
		};
		String[] expected = {
				"key: \"value, (stuff)",
				"key: \"value, (stuff)\"",
				"\"value, a\"",
				"\"value, a\"",
				"\"a, b)",
				"\"a, b) A",
				"a",
				"a\\"
		};

		CheckTask.assertTests(inputs, expected, (String str) -> {
			StringBuilder dst = new StringBuilder();
			TextIteratorParser in = TextIteratorParser.of(str);

			ReadUnescape.readUnescapePartialQuoted(in, '"', '\\', ',', ')', true, '\n', null);
			in.unread(in.getPosition() + 1);
			ReadUnescape.readUnescapePartialQuoted(in, '"', '\\', ',', ')', true, '\n', dst);
			return dst.toString();
		});
	}


	@Test
	public void readUnescapeQuotedTest() {
		String[] inputs = {
				"\"value, (stuff)",
				"\"value, (stuff)\", mk",
				"\"value, a\"",
				"\"value, a\") A",
				"\"a, b)",
				"\"a, b) A"
		};
		String[] expected = {
				"\"value, (stuff)",
				"\"value, (stuff)\"",
				"\"value, a\"",
				"\"value, a\"",
				"\"a, b)",
				"\"a, b) A"
		};

		CheckTask.assertTests(inputs, expected, (String str) -> {
			StringBuilder dst = new StringBuilder();
			TextIteratorParser in = TextIteratorParser.of(str);

			ReadUnescape.readUnescapeQuoted(in, '"', '\\', ',', ')', true, '\n', null);
			in.unread(in.getPosition() + 1);
			ReadUnescape.readUnescapeQuoted(in, '"', '\\', ',', ')', true, '\n', dst);
			return dst.toString();
		});
	}


	@Test
	public void readUnescapePartialFailTests() {
		// SAME AS JTextUtil: StringEscapePartialTest.java
		var inputs = Arrays.asList(
			"un\"closed",
			"\"abc,",
			"\""
		);

		int i = 0;
		for(String errInput : inputs) {
			CheckTask.assertException("(" + i + ") " + errInput, () -> {
				var sb = new StringBuilder();
				var in = TextIteratorParser.of(errInput, 0, errInput.length());

				ReadUnescape.readUnescapePartialOrFullQuotedThrows(in, '"', '\\', ',', ']', false, (char)0, false, true, true, false, true, false, sb);
			});
			i++;
		}
	}

}
