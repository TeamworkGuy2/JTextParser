package twg2.parser.textStream.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import twg2.collections.primitiveCollections.IntListSorted;
import twg2.parser.textStream.LineCounter;

/**
 * @author TeamworkGuy2
 * @since 2016-09-10
 */
public class LineCounterTest {

	@Test
	public void givenLines() {
		LineCounter lc = new LineCounter(Arrays.asList(0, 12, 22, 62, 87, 88));
		Assert.assertEquals(6, lc.size());
		assertAll(0, lc.getLineNumber(0), lc.getLineNumber(1), lc.getLineNumber(9), lc.getLineNumber(10), lc.getLineNumber(11));
		assertAll(1, lc.getLineNumber(12), lc.getLineNumber(13), lc.getLineNumber(20), lc.getLineNumber(21));
		assertAll(4, lc.getLineNumber(87));
		assertAll(5, lc.getLineNumber(88), lc.getLineNumber(89));
	}


	@Test
	public void addText() {
		String lns = "start" + "\n" +
				"1. len 11" + "\r\n" +
				"2. len 9" + "\n";
		int off = 6;
		// correct offsets
		IntListSorted lnStarts = IntListSorted.of(0, off, 17, 26);

		LineCounter lc = new LineCounter(Arrays.asList(0, off));
		char[] chs = lns.toCharArray();
		int prevLine = -1;
		// pass each char to the line counter and check that the line number of that character matches the expected ones from the known correct list
		for(int i = off; i < chs.length; i++) {
			int line = lc.read(chs[i]);

			if(line != prevLine) {
				int lnNum = lnStarts.binarySearch(i) + 1;
				Assert.assertEquals("char " + i + " '" + chs[i] + "'", lnNum, line);
			}

			prevLine = line;
		}

		// check the completed list of offsets
		IntListSorted lcLns = lc.getRawCompletedLineOffsets();

		Assert.assertEquals(lnStarts.size(), lcLns.size());
		for(int i = 0, size = lnStarts.size(); i < size; i++) {
			Assert.assertEquals("line offset " + i, lnStarts.get(i), lcLns.get(i));
		}
	}


	public void assertAll(int... ints) {
		if(ints == null || ints.length < 1) {
			return;
		}
		int first = ints[0];
		int i = 0;
		int size = ints.length;
		for(int it : ints) {
			if(first != it) {
				Assert.assertEquals("item " + i + " of " + (size - 1) + " does not equal", first, it);
			}
			i++;
		}
	}

}
