package twg2.parser.test;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.condition.text.CharParser;
import twg2.parser.textFragment.TextFragmentRef;
import twg2.parser.textParser.TextCharsParser;
import twg2.parser.textParser.TextParser;

public class CharParserTest {

	@Test
	public void readConditional() {
		// 0 chars required - always matches
		assertReadConditional(new CharParserMock(0, '\0'), "", "");
		assertReadConditional(new CharParserMock(0, '\0'), "abcdef", "");
		// 3 chars required
		assertReadConditional(new CharParserMock(3, '\0'), "abcdef", "abc");
		// empty input will not match 3 chars required
		assertReadConditionalFail(new CharParserMock(3, '\0'), "", "");
		// 10 chars required (more than input)
		assertReadConditionalFail(new CharParserMock(10, '\0'), "abcdef", "abcdef");
		// invalid char input
		assertReadConditionalFail(new CharParserMock(10, '#'), "abcd#ef", "");
	}


	/** Test if a readConditional() call is successful.
	 * also test a second time with the readConditional() StringBuilder override.
	 */
	private void assertReadConditional(CharParser parser, String src, String expected) {
		var textParser = TextCharsParser.of(src);
		Assert.assertTrue(parser.readConditional(textParser));

		var dst = new StringBuilder();
		textParser = TextCharsParser.of(src);
		parser.copyOrReuse();
		Assert.assertTrue(parser.readConditional(textParser, dst));
		Assert.assertEquals(expected, dst.toString());
	}


	/** Test if a readConditional() call is successful.
	 * Also test a second time with the readConditional() StringBuilder override.
	 */
	private void assertReadConditionalFail(CharParser parser, String src, String expected) {
		var textParser = TextCharsParser.of(src);
		Assert.assertFalse(parser.readConditional(textParser));

		var dst = new StringBuilder();
		textParser = TextCharsParser.of(src);
		parser.copyOrReuse();
		Assert.assertFalse(parser.readConditional(textParser, dst));
		Assert.assertEquals(expected, dst.toString());
	}


	public static class CharParserMock implements CharParser {
		protected char invalidChar;
		protected boolean failed;
		protected int count;
		protected int maxCount;

		public CharParserMock(int maxCount, char failAtChar) {
			this.invalidChar = failAtChar;
			this.count = 0;
			this.maxCount = maxCount;
		}

		@Override
		public String name() {
			return this.getClass().getTypeName();
		}

		@Override
		public boolean isComplete() {
			return !failed && count >= maxCount;
		}

		@Override
		public boolean isFailed() {
			return failed;
		}

		@Override
		public boolean canRecycle() {
			return true;
		}

		@Override
		public CharParser recycle() {
			this.count = 0;
			this.failed = false;
			return this;
		}

		@Override
		public boolean acceptNext(char ch, TextParser pos) {
			if(ch == invalidChar) {
				failed = true;
			}
			return !failed && count++ < maxCount;
		}

		@Override
		public TextFragmentRef getMatchedTextCoords() {
			return null;
		}

		@Override
		public CharParser copy() {
			return new CharParserMock(this.count, this.invalidChar);
		}
		
	}
}
