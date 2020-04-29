package twg2.parser.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.junitassist.checks.ObjectEquality;
import twg2.parser.textFragment.TextFragmentRef;
import twg2.parser.textFragment.TextFragmentRefImpl;
import twg2.parser.textFragment.TextFragmentRefImplMut;

/**
 * @author TeamworkGuy2
 * @since 2019-03-24
 */
public class TextFragmentTest {
	private List<String> textBlob = Arrays.asList(
		"//",
		"class A {",
		"}"
	);
	private String textString = String.join("", textBlob);


	@Test
	public void containsFragmentTest() {
		TextFragmentRefImpl frag = new TextFragmentRefImpl(2, 13, 1, 0, 2, 0);
		TextFragmentRefImplMut fragPart = new TextFragmentRefImplMut(3, 7, 1, 1, 1, 4);

		// identity
		Assert.assertTrue(frag.contains(frag));
		// larger contains smaller
		Assert.assertTrue(frag.contains(fragPart));
		// smaller does not contain larger
		Assert.assertFalse(fragPart.contains(frag));

		// smaller starts same
		fragPart.setOffsetStart(2);
		Assert.assertTrue(frag.contains(fragPart));

		// smaller starts earlier
		fragPart.setOffsetStart(1);
		Assert.assertFalse(frag.contains(fragPart));
	}


	@Test
	public void getTextTest() {
		Assert.assertEquals("/class A {}", new TextFragmentRefImpl(1, 12, 0, 1, 2, 0).getText(textBlob));
		Assert.assertEquals("class A {}", new TextFragmentRefImpl(2, 12, 1, 0, 2, 0).getText(textBlob));
		Assert.assertEquals("lass", new TextFragmentRefImplMut(3, 7, 1, 1, 1, 4).getText(textBlob));

		Assert.assertEquals("/class A {}", new TextFragmentRefImpl(1, 12, 0, 1, 2, 0).getText(textString));
		Assert.assertEquals("class A {}", new TextFragmentRefImpl(2, 12, 1, 0, 2, 0).getText(textString));
		Assert.assertEquals("lass", new TextFragmentRefImplMut(3, 7, 1, 1, 1, 5).getText(textString));
		Assert.assertEquals("lass A {}", new TextFragmentRefImpl(3, 12, 1, 1, 2, 0).getText(3, chars(textString.substring(3)), 0, textString.length() - 3));
		Assert.assertEquals("s A ", new TextFragmentRefImplMut(6, 10, 1, 4, 1, 7).getText(1, chars(textString.substring(1)), 1, textString.length() - 4));

		CheckTask.assertException(() -> new TextFragmentRefImplMut(2, 7, 1, 0, 1, 5).getText(2, chars(textString.substring(1)), 1, textString.length() - 1));
		CheckTask.assertException(() -> new TextFragmentRefImplMut(6, 10, 1, 4, 1, 7).getText(1, chars(textString.substring(1)), 1, textString.length() - 5));
	}


	@Test
	public void mergeTest() {
		var start =  new TextFragmentRefImpl(2, 10, 0, 2, 1, 2);
		var middle = new TextFragmentRefImpl(11, 20, 1, 1, 2, 5);
		var end =    new TextFragmentRefImpl(22, 23, 3, 0, 3, 1);
		// 2/2 overlapping
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.merge((TextFragmentRefImplMut)null, new TextFragmentRefImpl(2, 10, 0, 2, 1, 2), new TextFragmentRefImpl(9, 20, 1, 1, 2, 5)));
		// 2/3 overlapping, last 1 not
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.merge(TextFragmentRef.copyMutable(start), start, middle, end)); // new dst
		// 2/3 overlapping, last 1 not
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.merge(start, middle, end)); // null dst
		// first and last overlapping, second not
		var start2 = TextFragmentRef.copyMutable(start);
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.merge(start2, start2, end, middle)); // reuse dst
	}


	@Test
	public void spanTest() {
		// overlapping
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.span(new TextFragmentRefImplMut(2, 10, 0, 2, 1, 2), new TextFragmentRefImpl(9, 20, 1, 1, 2, 5), null)); // null dst
		// adjacent
		var start = new TextFragmentRefImplMut(2, 10, 0, 2, 1, 2);
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.span(start, new TextFragmentRefImpl(11, 20, 1, 1, 2, 5), new TextFragmentRefImplMut())); // new empty dst
		// non-adjacent
		start = new TextFragmentRefImplMut(2, 10, 0, 2, 1, 2);
		Assert.assertEquals(new TextFragmentRefImplMut(2, 20, 0, 2, 2, 5), TextFragmentRef.span(start, new TextFragmentRefImpl(11, 20, 1, 1, 2, 5), start)); // reuse dst
	}


	// boring model tests

	@Test
	public void equalityTest() {
		TextFragmentRefImpl frag = new TextFragmentRefImpl(3, 13, 1, 0, 2, 0);
		TextFragmentRefImpl fragSame = new TextFragmentRefImpl(3, 13, 1, 0, 2, 0);
		TextFragmentRefImplMut fragPart = new TextFragmentRefImplMut(3, 7, 1, 1, 1, 4);
		TextFragmentRefImplMut fragEmpty = new TextFragmentRefImplMut();

		ObjectEquality.testEquality(frag, frag.copy(), fragSame, fragPart, fragPart.copy(), fragEmpty);
	}


	@Test
	public void textFragmentRefImplTest() {
		TextFragmentRefImpl frag = new TextFragmentRefImpl(1, 3, 1, 0, 2, 1);
		Assert.assertEquals(1, frag.getOffsetStart());
		Assert.assertEquals(3, frag.getOffsetEnd());
		Assert.assertEquals(1, frag.getLineStart());
		Assert.assertEquals(0, frag.getColumnStart());
		Assert.assertEquals(2, frag.getLineEnd());
		Assert.assertEquals(1, frag.getColumnEnd());
	}


	@Test
	public void textFragmentRefImplMutTest() {
		TextFragmentRefImplMut frag = new TextFragmentRefImplMut(1, 3, 1, 0, 2, 1);
		Assert.assertEquals(1, frag.getOffsetStart());
		Assert.assertEquals(3, frag.getOffsetEnd());
		Assert.assertEquals(1, frag.getLineStart());
		Assert.assertEquals(0, frag.getColumnStart());
		Assert.assertEquals(2, frag.getLineEnd());
		Assert.assertEquals(1, frag.getColumnEnd());

		frag.setOffsetStart(15);
		frag.setOffsetEnd(20);
		frag.setLineStart(3);
		frag.setColumnStart(10);
		frag.setLineEnd(5);
		frag.setColumnEnd(2);
		Assert.assertEquals(15, frag.getOffsetStart());
		Assert.assertEquals(20, frag.getOffsetEnd());
		Assert.assertEquals(3, frag.getLineStart());
		Assert.assertEquals(10, frag.getColumnStart());
		Assert.assertEquals(5, frag.getLineEnd());
		Assert.assertEquals(2, frag.getColumnEnd());

		frag = new TextFragmentRefImplMut();
		Assert.assertEquals(0, frag.getOffsetStart());
		Assert.assertEquals(0, frag.getOffsetEnd());
		Assert.assertEquals(0, frag.getLineStart());
		Assert.assertEquals(0, frag.getColumnStart());
		Assert.assertEquals(0, frag.getLineEnd());
		Assert.assertEquals(0, frag.getColumnEnd());
	}


	@Test
	public void toStringTest() {
		String str = "// A" +
				"" +
				"BB { 1 + 2 = 3 " +
				"" +
				"" +
				"} // end of comment";

		var frag = new TextFragmentRefImpl(7, 33, 2, 3, 5, 14);
		var fragMut = new TextFragmentRefImplMut(7, 33, 2, 3, 5, 14);

		Assert.assertEquals("TextFragmentRef: { off: 7, len: 26, start: [2:3], end: [5:14] }", frag.toString());
		Assert.assertEquals("TextFragmentRef: { off: 7, len: 26, start: [2:3], end: [5:14] }", fragMut.toString());

		Assert.assertEquals("TextFragmentRef: { off: 7, len: 26, start: [2:3], end: [5:14], text: \"{ 1 + 2 = 3 } // end of co\" }", frag.toString(str));
		Assert.assertEquals("TextFragmentRef: { off: 7, len: 26, start: [2:3], end: [5:14], text: \"{ 1 + 2 = 3 } // end of co\" }", fragMut.toString(str));
	}


	private static char[] chars(String str) {
		return str.toCharArray();
	}

}
