package twg2.parser.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.ObjectEquality;
import twg2.parser.textFragment.TextFragmentRefImpl;
import twg2.parser.textFragment.TextFragmentRefImplMut;

/**
 * @author TeamworkGuy2
 * @since 2019-03-24
 */
public class TextFragmentTest {
	public List<String> textBlob = Arrays.asList(
		"//",
		"class A {",
		"}"
	);

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
		Assert.assertEquals("/class A {}", new TextFragmentRefImpl(1, 13, 0, 1, 2, 0).getText(textBlob));

		Assert.assertEquals("class A {}", new TextFragmentRefImpl(2, 13, 1, 0, 2, 0).getText(textBlob));

		Assert.assertEquals("lass", new TextFragmentRefImplMut(3, 7, 1, 1, 1, 4).getText(textBlob));
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

}
