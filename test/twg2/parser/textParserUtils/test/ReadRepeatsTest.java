package twg2.parser.textParserUtils.test;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.textParserUtils.ReadRepeats;

/**
 * @author TeamworkGuy2
 * @since 2015-1-3
 */
public class ReadRepeatsTest {

	@Test
	public void readRepeatCharTest() {
		int offset = 3;
		String[] inputs = {
				"0.array",
				"fluff",
				"00 === 0",
				"3. a",
				"4. ",
				""
		};
		char[] search = {
				'r',
				'f',
				'=',
				'a',
				'b',
				'c'
		};
		int[] max = {
				1,
				0,
				0,
				0,
				0,
				0
		};
		int[] expect = {
				1,
				2,
				3,
				1,
				0,
				0
		};

		for(int i = 0, size = inputs.length; i < size; i++) {
			int res = ReadRepeats.readRepeat(buf(inputs[i], offset), search[i], max[i]);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expect[i], res);

			res = ReadRepeats.readRepeat(sub(inputs[i], offset), search[i], max[i]);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expect[i], res);

			res = ReadRepeats.readRepeat(inputs[i], offset, search[i], max[i]);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expect[i], res);
		}
	}


	@Test
	public void readRepeatCharArrayTest() {
		int offset = 3;
		String[] inputs = {
				"0. wubwub",
				"fluffy",
				"2. }{}{}",
				"3. a",
				"4. ",
				""
		};
		String[] search = {
				"wub",
				"f",
				"{}",
				"a",
				"b",
				"c"
		};
		int[] max = {
				3,
				0,
				0,
				0,
				0,
				0
		};
		int[] expectI = {
				3,
				2,
				5,
				1,
				0,
				0
		};
		String[] expectStr = {
				"wub",
				"ff",
				"}{}{}",
				"a",
				"",
				""
		};

		for(int i = 0, size = inputs.length; i < size; i++) {
			char[] searchChars = search[i].toCharArray();
			StringBuilder dst = new StringBuilder();
			dst = ReadRepeats.readRepeat(buf(inputs[i], offset), searchChars, max[i], dst);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectStr[i], dst.toString());
			dst.setLength(0);

			dst = ReadRepeats.readRepeat(buf(inputs[i], offset), searchChars, 0, searchChars.length, max[i], dst);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectStr[i], dst.toString());
			dst.setLength(0);

			int res = ReadRepeats.readRepeat(inputs[i], offset, searchChars, max[i], dst);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectI[i], res);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectStr[i], dst.toString());
			dst.setLength(0);

			String str = ReadRepeats.readRepeat(inputs[i], offset, searchChars, max[i]);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectStr[i], str);

			res = ReadRepeats.readRepeat(inputs[i].toCharArray(), offset, searchChars, max[i], dst);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectI[i], res);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectStr[i], dst.toString());

			str = ReadRepeats.readRepeat(inputs[i].toCharArray(), offset, searchChars, max[i]);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + search[i], expectStr[i], str);
		}
	}


	@Test
	public void readRepeatTest() {
		StringBuilder dst = new StringBuilder();
		assertBufferRead(3, "www", dst, ReadRepeats.readRepeat(chars("www"), 0, 'a', 'z', 0, dst));
		assertBufferRead(2, "ww", dst,  ReadRepeats.readRepeat(chars("www"), 0, 'a', 'z', 2, dst));
		assertBufferRead(4, "1123", dst, ReadRepeats.readRepeat(chars("112358"), 0, '0', '3', 4, dst));
		assertBufferRead(6, "112358", dst, ReadRepeats.readRepeat(chars("11235800"), 0, '1', '3', '5', '8', 0, dst));
		assertBufferRead(4, "1123",   dst, ReadRepeats.readRepeat(chars("11235800"), 0, '5', '8', '1', '3', 4, dst));
		assertBufferRead(6, "112358", dst, ReadRepeats.readRepeat(chars("11235800"), 0, 'a', 'f', '1', '3', '5', '8', 0, dst));
		assertBufferRead(4, "1123",   dst, ReadRepeats.readRepeat(chars("11235800"), 0, '1', '3', '5', '8', 'a', 'f', 4, dst));
		assertBufferRead(8, "Base64+/", dst, ReadRepeats.readRepeat(chars("Base64+/"), 0, 'A', 'Z', 'a', 'z', '0', '9', '+', '/', 0, dst));
		assertBufferRead(6, "Base64",   dst, ReadRepeats.readRepeat(chars("Base64+/"), 0, '0', '9', 'a', 'z', 'A', 'Z', '+', '/', 6, dst));
		assertBufferRead(8, "Sp3c1AL!", dst, ReadRepeats.readRepeat(chars("Sp3c1AL!"), 0, new char[] { ' ', '!' }, 0, 2, 'A', 'Z', 'a', 'z', '0', '9', 0, dst));
		assertBufferRead(6, "Sp3c1A",   dst, ReadRepeats.readRepeat(chars("Sp3c1AL!"), 0, new char[] { ' ', '!' }, 0, 2, 'A', 'Z', 'a', 'z', '0', '9', 6, dst));
		assertBufferRead(3, "Sp3", dst, ReadRepeats.readRepeat(chars("Sp3? c1AL!"), 0, new char[] { ' ', '!' }, 0, 2, 'A', 'Z', 'a', 'z', '0', '9', 0, dst));

		assertBufferRead(0, "", dst, ReadRepeats.readRepeat(chars("www"), 0, 'A', 'Z', 3, dst));
		assertBufferRead(0, "", dst, ReadRepeats.readRepeat(chars("112358"), 0, 'A', 'Z', '5', '8', 0, dst));
		assertBufferRead(0, "", dst, ReadRepeats.readRepeat(chars("112358"), 0, 'A', 'Z', '5', '8', 0, dst));
		assertBufferRead(0, "", dst, ReadRepeats.readRepeat(chars("www"), 0, '0', '9', 'a', 'f', 'A', 'F', 3, dst));
		assertBufferRead(0, "", dst, ReadRepeats.readRepeat(chars("=="), 0, '0', '9', 'A', 'Z', 'a', 'z', '+', '/', 2, dst));
		assertBufferRead(0, "", dst, ReadRepeats.readRepeat(chars("?Sp3c1AL!"), 0, new char[] { ' ', '!' }, 0, 2, 'A', 'Z', 'a', 'z', '0', '9', 0, dst));
	}


	private static void assertBufferRead(int expectedCount, String expectedStr, StringBuilder resultBuf, int actualCount) {
		Assert.assertEquals(expectedCount, actualCount);
		Assert.assertEquals(expectedStr, resultBuf.toString());
		resultBuf.setLength(0);
	}


	private static BufferedReader buf(String src, int offset) {
		return new BufferedReader(new StringReader(src.length() >= offset ? src.substring(offset) : ""));
	}


	private static char[] chars(String src) {
		return src.toCharArray();
	}


	private static String sub(String str, int offset) {
		return str.length() >= offset ? str.substring(offset) : "";
	}

}
