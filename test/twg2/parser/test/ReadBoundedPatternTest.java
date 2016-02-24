package twg2.parser.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import twg2.parser.textParserUtils.ReadBoundedPattern;

/**
 * @author TeamworkGuy2
 * @since 2015-7-18
 */
public class ReadBoundedPatternTest {

	public static enum ReqCount {
		NONE(0),
		ATLEAST_1(1),
		ATLEAST_2(2);

		final int required;

		ReqCount(int count) {
			this.required = count;
		}

		boolean isRequire1() {
			return required == 1;
		}

		boolean isRequire2() {
			return required == 2;
		}

		boolean _isAtleast1() {
			return required >= 1;
		}

		boolean _isAtleast2() {
			return required >= 2;
		}


		public static ReqCount ofOneBasedZeroNone(int count) {
			switch(count) {
			case 0:
				return NONE;
			case 1:
				return ATLEAST_1;
			case 2:
				return ATLEAST_2;
			default:
				throw new IllegalStateException("unsupported required count of '" + count + "', min=0, max=2");
			}
		}
	}


	public static class ThreeTask {
		static String fail1 = "fail1";
		static String fail2 = "fail2";
		static String fail3 = "fail3";
		static String atleast1 = "atleast1";
		static String atleast2 = "atleast2";
		static String success = "success";

		boolean input1;
		boolean input2;
		boolean input3;
		boolean require1;
		boolean require2;
		boolean require3;
		ReqCount requireCount;
		String expectedResult;
		int taskInputCount;


		public ThreeTask(boolean input1, boolean input2, boolean require1, boolean require2, ReqCount reqCount, String expectedResult) {
			this.input1 = input1;
			this.input2 = input2;
			this.require1 = require1;
			this.require2 = require2;
			this.requireCount = reqCount;
			this.expectedResult = expectedResult;
			this.taskInputCount = 2;
		}


		public ThreeTask(boolean input1, boolean input2, boolean input3, boolean require1, boolean require2, boolean require3, ReqCount reqCount, String expectedResult) {
			this.input1 = input1;
			this.input2 = input2;
			this.input3 = input3;
			this.require1 = require1;
			this.require2 = require2;
			this.require3 = require3;
			this.requireCount = reqCount;
			this.expectedResult = expectedResult;
			this.taskInputCount = 3;
		}


		public void runTest() {
			switch(this.taskInputCount) {
			case 2:
				run2Test();
				break;
			case 3:
				run3Test();
				break;
			default:
				throw new IllegalStateException("invalid 'taskInputCount' of '" + this.taskInputCount + "', valid values are 2 or 3");
			}
		}


		public void run2Test() {
			Assert.assertEquals(expectedResult,
					ReadBoundedPattern.testTwoTasks(() -> input1, require1, fail1, () -> input2, require2, fail2, success, requireCount.isRequire1(), atleast1));
		}


		public void run3Test() {
			Assert.assertEquals(expectedResult,
					ReadBoundedPattern.testThreeTasks(() -> input1, require1, fail1, () -> input2, require2, fail2, () -> input3, require3, fail3, success, requireCount.isRequire1(), atleast1, requireCount.isRequire2(), atleast2));
		}


		@Override
		public String toString() {
			return "ThreeTask: {input1: " + input1 + " (" + require1 + "), input2: " + input2 + " (" + require2 + "), input3: " + input3 + " (" + require3 + ")" +
					", require: " + requireCount + ", expect: " + expectedResult + ", taskType: " + taskInputCount +
					"}";
		}

	}




	private String expectResult(boolean i1, boolean i2, boolean i3, boolean req1, boolean req2, boolean req3, boolean reqAtleast1, boolean reqAtleast2) {
		String fail1 = ThreeTask.fail1;
		String fail2 = ThreeTask.fail2;
		String fail3 = ThreeTask.fail3;
		String atleast1 = ThreeTask.atleast1;
		String atleast2 = ThreeTask.atleast2;
		String success = ThreeTask.success;

		if(reqAtleast1) {
			return i1 || i2 || i3 ? success : atleast1;
		}

		if(reqAtleast2) {
			return (i1 && i2) || (i1 && i3) || (i2 && i3) ? success : atleast2;
		}

		if(req1) {
			if(req2) {
				if(req3) {
					return !i1 ? fail1 : (!i2 ? fail2 : (!i3 ? fail3 : success));
				}
				else {
					return !i1 ? fail1 : (!i2 ? fail2 : success);
				}
			}
			else {
				if(req3) {
					return !i1 ? fail1 : (!i3 ? fail3 : success);
				}
				else {
					return !i1 ? fail1 : success;
				}
			}
		}
		else {
			if(req2) {
				if(req3) {
					return !i2 ? fail2 : (!i3 ? fail3 : success);
				}
				else {
					return !i2 ? fail2 : success;
				}
			}
			else {
				if(req3) {
					return !i3 ? fail3 : success;
				}
				else {
					return success;
				}
			}
		}
	}


	@Test
	public void testReadTwoTasks() {
		List<ThreeTask> input = generateExpectedInputs();

		for(ThreeTask task : input) {
			task.runTest();
		}
	}


	private List<ThreeTask> generateExpectedInputs() {
		List<ThreeTask> res = new ArrayList<>();
		int max = 2;
		int reqMax = 3;
		for(int i1 = 0; i1 < max; i1++) {
			for(int i2 = 0; i2 < max; i2++) {
				for(int i3 = 0; i3 < max; i3++) {
					for(int r1 = 0; r1 < max; r1++) {
						for(int r2 = 0; r2 < max; r2++) {
							for(int r3 = 0; r3 < max; r3++) {
								for(int reqCount = 0; reqCount < reqMax; reqCount++) {
									ReqCount reqVal = ReqCount.ofOneBasedZeroNone(reqCount);
									// three-condition checks
									res.add(new ThreeTask(i1 == 1, i2 == 1, i3 == 1, r1 == 1, r2 == 1, r3 == 1, reqVal, expectResult(i1 == 1, i2 == 1, i3 == 1, r1 == 1, r2 == 1, r3 == 1, reqVal.isRequire1(), reqVal.isRequire2())));

									// two-condition checks
									if(i3 == 0 && r3 == 0 && reqCount == 0) {
										res.add(new ThreeTask(i1 == 1, i2 == 1, r1 == 1, r2 == 1, reqVal, expectResult(i1 == 1, i2 == 1, false, r1 == 1, r2 == 1, false, reqVal.isRequire1(), false)));
									}
								}
							}
						}
					}
				}
			}
		}

		return res;
	}

}
