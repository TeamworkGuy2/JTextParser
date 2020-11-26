package twg2.parser.textParserUtils.test;

import org.junit.Assert;
import org.junit.Test;

import twg2.functions.predicates.CharPredicate;
import twg2.parser.textParser.TextCharsParser;
import twg2.parser.textParserUtils.ReadIsMatching;

/**
 * @author TeamworkGuy2
 * @since 2020-04-26
 */
public class ReadIsMatchingTest {

	@Test
	public void isNextTest() {
		CharPredicate isWhitespace = (ch) -> ch == (char)32/*space (' ')*/ || ch == (char)9/*horizontal tab*/ || ch == (char)12/*form feed*/ || ch == (char)10/*line feed*/ || ch == (char)13/*carriage return*/;

		Assert.assertTrue(ReadIsMatching.isNext(parser("abc"), 'a'));
		Assert.assertTrue(ReadIsMatching.isNext(parser("abc"), 'a', 1));
		Assert.assertTrue(ReadIsMatching.isNext(parser("www"), 'w'));
		Assert.assertTrue(ReadIsMatching.isNext(parser("www"), 'w', 3));
		Assert.assertTrue(ReadIsMatching.isNext(parser("a"), 'a'));
		Assert.assertFalse(ReadIsMatching.isNext(parser("a"), 'b'));
		Assert.assertFalse(ReadIsMatching.isNext(parser("abc"), 'a', 2));

		Assert.assertTrue(ReadIsMatching.isNext(parser("aaab"), "aa"));
		Assert.assertTrue(ReadIsMatching.isNext(parser("int;"), "int"));
		Assert.assertFalse(ReadIsMatching.isNext(parser("inT"), "int"));

		Assert.assertTrue(ReadIsMatching.isNext(parser("https"), 'h', 't', 3));
		Assert.assertTrue(ReadIsMatching.isNext(parser("abc"), 'a', 'b', 1));
		Assert.assertTrue(ReadIsMatching.isNext(parser("a"), 'a', '0', 1));
		Assert.assertFalse(ReadIsMatching.isNext(parser("https"), 'h', 't', 4));
		Assert.assertFalse(ReadIsMatching.isNext(parser("abc"), 'a', 'b', 3));
		Assert.assertFalse(ReadIsMatching.isNext(parser("a"), 'a', '0', 2));

		Assert.assertTrue(ReadIsMatching.isNext(parser("www"), of('w'), 3));
		Assert.assertTrue(ReadIsMatching.isNext(parser("www"), of('w'), 2));
		Assert.assertTrue(ReadIsMatching.isNext(parser("abc"), of('d', 'a', 'b', 'c'), 3));
		Assert.assertTrue(ReadIsMatching.isNext(parser("abc"), of('d', 'a', 'b', 'c'), 2));
		Assert.assertTrue(ReadIsMatching.isNext(parser("abc"), of('d', 'a', 'b', 'c'), 1, 2, 2));
		Assert.assertTrue(ReadIsMatching.isNext(parser("a"), of('a'), 1));
		Assert.assertTrue(ReadIsMatching.isNext(parser("this"), of('t', 'h', 'e'), 2));
		Assert.assertFalse(ReadIsMatching.isNext(parser("abc"), of('a', 'b'), 3));
		Assert.assertFalse(ReadIsMatching.isNext(parser("abc"), of('a', 'b', 'c'), 4));
		Assert.assertFalse(ReadIsMatching.isNext(parser("abc"), of('a', 'b', 'c'), 0, 2, 3));

		Assert.assertTrue(ReadIsMatching.isNext(parser("1.2"), (ch) -> ch >= '0' && ch <= '9', 1));
		Assert.assertTrue(ReadIsMatching.isNext(parser("\t A"), isWhitespace, 2));
		Assert.assertFalse(ReadIsMatching.isNext(parser("\t A"), isWhitespace, 3));

		Assert.assertTrue(ReadIsMatching.isNext(parser("BDE"), (ch) -> (int)ch % 2 == 0, 2));
		Assert.assertFalse(ReadIsMatching.isNext(parser("ABC"), (ch) -> false, 1));
	}


	@Test
	public void isNextBetweenTest() {
		Assert.assertTrue(ReadIsMatching.isNextBetween(parser("https"), 'a', 'i', 1));
		Assert.assertTrue(ReadIsMatching.isNextBetween(parser("https"), 'a', 't', 5));
		Assert.assertTrue(ReadIsMatching.isNextBetween(parser("abcdef"), 'a', 'a', 1));
		Assert.assertTrue(ReadIsMatching.isNextBetween(parser("112358"), '0', '5', 5));
		Assert.assertFalse(ReadIsMatching.isNextBetween(parser("abcdef"), 'a', 'c', 4));
		Assert.assertFalse(ReadIsMatching.isNextBetween(parser("123"), 'a', 'c', 1));
	}


	@Test
	public void isNextNotTest() {
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("https"), 's', 4));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("https"), 's', 3));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("112358"), '9', 6));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("abcdef"), 'c', 'd', 2));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("abcdef"), 'd', 'e', 'f', 3));
		Assert.assertFalse(ReadIsMatching.isNextNot(parser("https"), 's', 5));
		Assert.assertFalse(ReadIsMatching.isNextNot(parser("abcdef"), 'f', 'f', 0));
		Assert.assertFalse(ReadIsMatching.isNextNot(parser("abcdef"), 'd', 'e', 'f', 4));

		Assert.assertTrue(ReadIsMatching.isNextNot(parser("https"), of('s'), 4));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("https"), of('s'), 3));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("abcdef"), of('d', 'e'), 3));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("abcdef"), of('d', 'e', 'f'), 0, 1, 3));
		Assert.assertTrue(ReadIsMatching.isNextNot(parser("abcdef"), of('d', 'e', 'f'), 1, 2, 4));
		Assert.assertFalse(ReadIsMatching.isNextNot(parser("https"), of('s'), 5));
		Assert.assertFalse(ReadIsMatching.isNextNot(parser("abcdef"), of('c', 'd', 'e'), 1, 2, 4));
	}


	@Test
	public void isNextNotPreceededByTest() {
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("it is well"), ' ', ':', 2));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("it is well"), ' ', ':', 1));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("it's: well"), ' ', ':', 10));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("it:s: well"), ' ', ':', 10));
		Assert.assertFalse(ReadIsMatching.isNextNotPrecededBy(parser("it's: well"), ' ', ':', 11));
		Assert.assertFalse(ReadIsMatching.isNextNotPrecededBy(parser("it's: well"), ' ', '_', 10));

		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("1? 22; 333"), ' ', '?', ';', 5));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("1? 22; 333"), ' ', '?', ';', 2));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("1? 2?2 ; 33"), ' ', '?', ';', 6));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("1? 2?2 ; 33"), ' ', '_', ';', 2));
		Assert.assertFalse(ReadIsMatching.isNextNotPrecededBy(parser("1? 2?2 ; 33"), ' ', '?', ';', 7));
		Assert.assertFalse(ReadIsMatching.isNextNotPrecededBy(parser("1? 2?2 ; 33"), ' ', '_', ';', 3));

		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("first- 1\n2 3"), ' ', '-', ';', '\n', 8));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("first- 1\n2 3"), ' ', '-', ';', '\n', 7));
		Assert.assertFalse(ReadIsMatching.isNextNotPrecededBy(parser("first- 1\n2 3"), ' ', '-', ';', '\n', 12));

		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("first- 1\n2 3"), ' ', '-', ';', '\n', '\n', 8));
		Assert.assertTrue(ReadIsMatching.isNextNotPrecededBy(parser("first- 1\n2 3"), ' ', '-', ';', '\n', '\n', 7));
		Assert.assertFalse(ReadIsMatching.isNextNotPrecededBy(parser("first- 1\n2 3"), ' ', '-', ';', '\n', '\n', 12));
	}


	private static TextCharsParser parser(String str) {
		return TextCharsParser.of(str);
	}


	private static char[] of(char...cs) {
		return cs;
	}

}
