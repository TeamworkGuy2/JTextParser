package test;

import org.junit.Assert;
import org.junit.Test;

import parserUtils.SlidingStringView;
import checks.CheckTask;

/**
 * @author TeamworkGuy2
 * @since 2015-6-12
 */
public class SlidingStringViewTest {

	@Test
	public void testSlidingStringView() {
		SlidingStringView view = new SlidingStringView(5);
		view.append("important");

		Assert.assertEquals("rtant", view.getContent());

		view.append(new char[] { 'i', 'n' });

		Assert.assertEquals(view.substring(8, 11), "tin");

		CheckTask.assertException(() -> view.substring(5, 11));

		view.append("outside", 3, 3);

		Assert.assertEquals(view.substring(9, 14), "insid");
	}

}
