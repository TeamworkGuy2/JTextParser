package twg2.parser.textParserUtils.test;

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
			int res = ReadRepeats.readRepeat(inputs[i], offset, search[i], max[i]);
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
		char[][] search = {
				"wub".toCharArray(),
				"f".toCharArray(),
				"{}".toCharArray(),
				"a".toCharArray(),
				"b".toCharArray(),
				"c".toCharArray()
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
			StringBuilder dst = new StringBuilder();
			int res = ReadRepeats.readRepeat(inputs[i], offset, search[i], max[i], dst);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + new String(search[i]), expectI[i], res);
			Assert.assertEquals("input: " + inputs[i] + ", search: " + new String(search[i]) + ", result: " + dst.toString(), expectStr[i], dst.toString());
		}
	}

}
