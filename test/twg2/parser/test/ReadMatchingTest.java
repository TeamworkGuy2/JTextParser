package twg2.parser.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.textParser.TextParserImpl;
import twg2.parser.textParserUtils.ReadMatching;
import twg2.parser.textParserUtils.ReadUnescape;
import twg2.parser.textParserUtils.SearchRange;
import twg2.text.test.StringEscapePartialTest;
import checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2015-6-16
 */
public class ReadMatchingTest {

	@Test
	public void testReadUnescapePartialQuoted() {
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
			StringBuilder strB = new StringBuilder();
			TextParserImpl in = TextParserImpl.of(str);

			ReadUnescape.readUnescapePartialQuoted(in, '"', '\\', ',', ')', true, '\n', strB);
			String res = strB.toString();
			strB.setLength(0);

			return res;
		});
	}



	@Test
	public void testReadUnescapePartialQuoted2() {
		int offset = 3;
		List<String> inputs = StringEscapePartialTest.UnescapePartialQuoted.inputs;
		List<String> expect = StringEscapePartialTest.UnescapePartialQuoted.expected;
		List<Integer> expectIndex = StringEscapePartialTest.UnescapePartialQuoted.expectedIndexes;
		StringBuilder dst = new StringBuilder();

		for(int i = 0, size = inputs.size(); i < size; i++) {
			StringBuilder strB = new StringBuilder();
			TextParserImpl in = TextParserImpl.of(inputs.get(i), offset, inputs.get(i).length() - offset);

			ReadUnescape.Default.readUnescapePartialQuoted(in, '"', '\\', ',', ']', strB);

			int index = in.getPosition();
			Assert.assertEquals(expect.get(i), strB.toString());
			Assert.assertEquals(i + ". expect (" + expectIndex.get(i) + "): " + expect.get(i) + ", result (" + index + "): " + strB.toString(), (int)expectIndex.get(i), index + offset + 1);
			dst.setLength(0);
		}
	}



	@Test
	public void readCharMatchingTest() {
		String lin = "1_2zthisat 1 with bcdw";

		List<List<String>> strs = Arrays.asList(
				Arrays.asList("this", "abc"),
				Arrays.asList("this", "bcd", "with")
		);
		char[][] chars = new char[][] {
				{'a', 'b', '1', '2', '3', '_', 't', 'z'},
				{' ', '1', 'w'}
		};

		String[] expect = {
				"1_2zthisat",
				" 1 with bcdw"
		};

		StringBuilder strB = new StringBuilder();
		int i = 0;
		int k = 0;
		while(k < lin.length()) {
			//boolean found = TokenParser.searchStartsWith(strs, strB, k);
			ReadMatching.FromString.readCharsMatching(lin, k, strs.get(i), chars[i], strB);
			Assert.assertEquals(expect[i], strB.toString());

			k += (strB.length() > 0 ? strB.length() : 1);
			strB.setLength(0);
			i++;
		}
	}


	@Test
	public void testParseArrayLike() {
		String[] inputs = {
				"\"abc\",\"1.2\",alpha omega, a \"b c\"",
				"stuff,1.2.3 , 010\"11\"]"
		};

		@SuppressWarnings("unchecked")
		List<String>[] expected = new List[] {
				Arrays.asList("\"abc\"", "\"1.2\"", "alpha omega", " a \"b c\""),
				Arrays.asList("stuff", "1.2.3 ", " 010\"11\"")
		};

		CheckTask.assertTests(inputs, expected, (String str) -> {
			StringBuilder sb = new StringBuilder();
			TextParserImpl in = TextParserImpl.of(str);
			List<String> parsedElems = new ArrayList<>();

			while(in.hasNext()) {
				ReadUnescape.Default.readUnescapePartialQuoted(in, '"', '\\', ',', ']', sb);
				parsedElems.add(sb.toString());

				in.nextIf(',', ']', sb);
				sb.setLength(0);
			}
			return parsedElems;
		});
	}


	@Test
	public void binaryStartsWithTest() {
		List<String> strs = Arrays.asList(
				"Barney",
				"Billy",
				"Cali",
				"Straight",
				"Top Hat"
		);
		SearchRange range = new SearchRange();

		range.reset(5);
		StringBuilder searchSb = new StringBuilder("Bill");
		Assert.assertEquals(1, ReadMatching.binaryStartsWith(range, strs, searchSb));

		range.reset(5);
		searchSb = new StringBuilder("D");
		Assert.assertEquals(-4, ReadMatching.binaryStartsWith(range, strs, searchSb));

		range.reset(5);
		searchSb = new StringBuilder("Xyz");
		Assert.assertEquals(-6, ReadMatching.binaryStartsWith(range, strs, searchSb));
	}


	@Test
	public void testReadUnescapePartialOrFullQuotedThrows() {
		final boolean appendEndChar = false;
		final boolean dropEscChars = true;
		final boolean throwIfQuotedNoStartQuote = true;
		final boolean throwIfNoEndQuote = true;
		final boolean throwIfNoEndChar = false;

		// partial quoted, no end quote
		testReadQuotedThrows("A\nB\nC", false, "A\nB\nC", true, false, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);
		testReadQuotedThrows("\"A\nB", true, "",          true, false, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);

		// partial quoted, no end quote
		testReadQuotedThrows("1 \"ABC", false, "1 \"ABC", false, false, dropEscChars, appendEndChar, true, false, throwIfNoEndChar);
		testReadQuotedThrows("1 \"ABC", true, "",         false, false, dropEscChars, appendEndChar, true, true, throwIfNoEndChar);

		// partial quoted, read end char
		testReadQuotedThrows("ABC,", false, "ABC,", false, false, dropEscChars, true, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);
		testReadQuotedThrows("ABC,", false, "ABC",  false, false, dropEscChars, false, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);

		// quoted, read end char
		testReadQuotedThrows("\"ABC\",", false, "\"ABC\",", false, true, dropEscChars, true, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);
		testReadQuotedThrows("\"ABC\",", false, "\"ABC\"",  false, true, dropEscChars, false, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);

		// quoted, allow newline
		testReadQuotedThrows("\"A\nB\"", false, "\"A\nB\"", true, true, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);
		testReadQuotedThrows("\"A\nB\"", true, "",          false, true, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);

		// quoted, no end quote
		testReadQuotedThrows("\"ABC", false, "\"ABC", false, true, dropEscChars, appendEndChar, true, false, throwIfNoEndChar);
		testReadQuotedThrows("\"ABC", true, "",       false, true, dropEscChars, appendEndChar, true, true, throwIfNoEndChar);

		// quoted, no start quote, throw
		testReadQuotedThrows("\"ABC\"", false, "\"ABC\"", false, true, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);
		testReadQuotedThrows("ABC\"", true, "",           false, true, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar);

		// quoted, no start quote, read
		testReadQuotedThrows("A\n\"", false, "", true, true, dropEscChars, appendEndChar, false, throwIfNoEndQuote, throwIfNoEndChar);
		testReadQuotedThrows("A\n\"", true, "",  true, true, dropEscChars, appendEndChar, true, throwIfNoEndQuote, throwIfNoEndChar);

		// partial quoted, throw if no end char
		testReadQuotedThrows("A \"B\", 1", false, "A \"B\"", true, false, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, true);
		testReadQuotedThrows("A \"B\" 1", true, "",          true, false, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, true);

		// quoted, throw if no end char
		testReadQuotedThrows("\"A\", 1", false, "\"A\"", true, true, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, true);
		testReadQuotedThrows("\"A\" 1", true, "",        true, true, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, true);

		//testReadQuotedThrows("ABC", expectThrows, "abc", allowNewline, quotedOnly, throwIfQuotedNoStartChar, throwIfNoEndChar);
	}


	private static void testReadQuotedThrows(String str, boolean expectThrows, String expected,
			boolean allowNewline, boolean quotedOnly, boolean dropEscChars, boolean appendEndChar, boolean throwIfQuotedNoStartQuote, boolean throwIfNoEndQuote, boolean throwIfNoEndChar) {
		StringBuilder strB = new StringBuilder();

		Runnable task = () -> {
			TextParserImpl in = TextParserImpl.of(str);
			ReadUnescape.readUnescapePartialOrFullQuotedThrows(in, '"', '\\', ',', ')', allowNewline, '\n', quotedOnly, dropEscChars, appendEndChar, throwIfQuotedNoStartQuote, throwIfNoEndQuote, throwIfNoEndChar, strB);
		};

		if(expectThrows) {
			CheckTask.assertException(task);
		}
		else {
			task.run();
			Assert.assertEquals(expected, strB.toString());
		}
	}

}
