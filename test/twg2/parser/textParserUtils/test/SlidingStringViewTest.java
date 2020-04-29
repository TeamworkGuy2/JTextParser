package twg2.parser.textParserUtils.test;

import org.junit.Assert;
import org.junit.Test;

import twg2.junitassist.checks.CheckTask;
import twg2.parser.textParserUtils.SlidingStringView;

/**
 * @author TeamworkGuy2
 * @since 2015-6-12
 */
public class SlidingStringViewTest {

	@Test
	public void appendTest() {
		SlidingStringView view = new SlidingStringView(5);

		view.append("important"); // 9

		Assert.assertEquals("rtant", view.getContent());

		view.append(chars("in")); // 2

		Assert.assertEquals(view.substring(8, 11), "tin");

		CheckTask.assertException(() -> view.substring(5, 11));
		CheckTask.assertException(() -> view.substring(6, 12));

		view.append("outside", 3, 3); // 3

		Assert.assertEquals(view.substring(9, 14), "insid");

		view.append(" outside"); // 8

		Assert.assertEquals(view.substring(17, 22), "tside");

		view.append(chars("in out")); // 6
		view.append('i');
		view.append('n');
		view.append(' ');
		view.append('o');
		view.append('u');
		view.append('t'); // 6

		Assert.assertEquals(view.substring(29, 34), "n out");
	}


	@Test
	public void constructorTest() {
		SlidingStringView view = new SlidingStringView(5);
		view.append("abc");
		Assert.assertEquals("abc", view.getContent());
		Assert.assertEquals(3, view.getPosition());

		view = new SlidingStringView(42, 5);
		view.append("abcdef");
		Assert.assertEquals("bcdef", view.getContent());
		Assert.assertEquals(48, view.getPosition());

		view = new SlidingStringView(42, 5, new StringBuilder(10));
		view.append("abcdef");
		Assert.assertEquals("bcdef", view.getContent());
		Assert.assertEquals(48, view.getPosition());

		CheckTask.assertException(() -> new SlidingStringView(-2));
		CheckTask.assertException(() -> new SlidingStringView(0, -2, null));
	}


	private static char[] chars(String src) {
		return src.toCharArray();
	}

}
