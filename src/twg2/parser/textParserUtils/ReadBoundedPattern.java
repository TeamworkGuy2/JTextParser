package twg2.parser.textParserUtils;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

import twg2.parser.textParser.TextParser;

/** Static functions to twg2.parser.test conditional tasks
 * @author TeamworkGuy2
 * @since 2015-7-16
 */
public class ReadBoundedPattern {

	private ReadBoundedPattern() { throw new AssertionError("cannot instantiate static class ReadBoundedPattern"); }


	public static <T> T testRequiredThenOptional(BooleanSupplier required, T requiredFailVal, BooleanSupplier optional, T successVal) {
		return testTwoTasks(required, true, requiredFailVal, optional, false, null, successVal, false, null);
	}


	public static <T> T testRequiredThenOptional(int requireExpected, IntSupplier required, T requiredFailVal,
			int optionalExpected, IntSupplier optional, T successVal) {
		return testTwoTasks(requireExpected, required, true, requiredFailVal, optionalExpected, optional, false, null, successVal, false, null);
	}


	public static <T> T testOptionalThenRequired(BooleanSupplier optional, BooleanSupplier required, T requiredFailVal, T successVal) {
		return testTwoTasks(optional, false, null, required, true, requiredFailVal, successVal, false, null);
	}


	public static <T> T testOptionalThenRequired(int optionalExpected, IntSupplier optional,
			int requireExpected, IntSupplier required, T requiredFailVal, T successVal) {
		return testTwoTasks(optionalExpected, optional, false, null, requireExpected, required, true, requiredFailVal, successVal, false, null);
	}
	

	/** Run two tasks, with requirements constraining the success of one or both.
	 * Return error values based on the results of running one or both tasks.<br>
	 * {@code 'succeed' = return true}<br>
	 * {@code 'fails' = return false}<br>
	 * @param task1 the first task
	 * @param required1 true if task 1 is required to succeed
	 * @param task1FailVal the value to return if task 1 fails
	 * @param task2 the second task
	 * @param required2 true if task 2 is required to succeed
	 * @param task2FailVal the value to return if task 2 fails
	 * @param successVal the value to return if one or both tasks succeed and match the required constraints
	 * @param atleastOneRequired this flags causes the function to ignore {@code required1} and {@code required2} and behave as {@code (task1 || task2)},
	 * which is not possible when explicitly setting {@code required1} and {@code required2} 
	 * @param atleastOneFailVal the value to return if both tasks fail and {@code atleastOneRequired} was true
	 * @return one of the failure values or the success value
	 */
	public static <T> T testTwoTasks(BooleanSupplier task1, boolean required1, T task1FailVal,
			BooleanSupplier task2, boolean required2, T task2FailVal, T successVal, boolean atleastOneRequired, T atleastOneFailVal) {
		boolean res1 = task1.getAsBoolean();
		if(required1 && !res1) {
			return task1FailVal;
		}

		if(!required2 && res1) {
			return successVal;
		}

		boolean res2 = task2.getAsBoolean();
		if(required2 && !res2) {
			return task2FailVal;
		}

		if(atleastOneRequired && !(res1 || res2)) {
			return atleastOneFailVal;
		}

		return successVal;
	}


	/** Run two tasks which return integers, with requirements constraining the success values of one or both.
	 * Return error values based on the results of running one or both tasks.<br>
	 * (NOTE: This function is a designed to compliment methods like {@link TextParser#nextIf(char, Appendable)} which return an integer})
	 * {@code 'succeed' = return true}<br>
	 * {@code 'fails' = return false}<br>
	 * @param task1Expected the value representing success for task 1
	 * @param task1 the first task
	 * @param required1 true if task 1 is required to succeed
	 * @param task1FailVal the value to return if task 1 fails
	 * @param task2Expected the value representing success for task 2
	 * @param task2 the second task
	 * @param required2 true if task 2 is required to succeed
	 * @param task2FailVal the value to return if task 2 fails
	 * @param successVal the value to return if one or both tasks succeed and match the required constraints
	 * @param atleastOneRequired this flags causes the function to ignore {@code required1} and {@code required2} and behave as {@code (task1 || task2)},
	 * which is not possible when explicitly setting {@code required1} and {@code required2} 
	 * @param atleastOneFailVal the value to return if both tasks fail and {@code atleastOneRequired} was true
	 * @return one of the failure values or the success value
	 */
	public static <T> T testTwoTasks(int task1Expected, IntSupplier task1, boolean required1, T task1FailVal,
			int task2Expected, IntSupplier task2, boolean required2, T task2FailVal, T successVal, boolean atleastOneRequired, T atleastOneFailVal) {
		boolean res1 = task1.getAsInt() == task1Expected;
		if(required1 && !res1) {
			return task1FailVal;
		}

		if(!required2 && res1) {
			return successVal;
		}

		boolean res2 = task2.getAsInt() == task2Expected;
		if(required2 && !res2) {
			return task2FailVal;
		}

		if(atleastOneRequired && !(res1 || res2)) {
			return atleastOneFailVal;
		}

		return successVal;

	}



	/** Run three tasks, with requirements constraining the success of one or both.
	 * Return error values based on the results of running one or both tasks.<br>
	 * {@code 'succeed' = return true}<br>
	 * {@code 'fails' = return false}<br>
	 * @param task1 the first task
	 * @param required1 true if task 1 is required to succeed
	 * @param task1FailVal the value to return if task 1 fails
	 * @param task2 the second task
	 * @param required2 true if task 2 is required to succeed
	 * @param task2FailVal the value to return if task 2 fails
	 * @param successVal the value to return if one or both tasks succeed and match the required constraints
	 * @param atleastOneRequired this flags causes the function to ignore {@code required1} and {@code required2} and behave as {@code (task1 || task2)},
	 * which is not possible when explicitly setting {@code required1} and {@code required2} 
	 * @param atleastOneFailVal the value to return if both tasks fail and {@code atleastOneRequired} was true
	 * @return one of the failure values or the success value
	 */
	public static <T> T testThreeTasks(BooleanSupplier task1, boolean required1, T task1FailVal,
			BooleanSupplier task2, boolean required2, T task2FailVal,
			BooleanSupplier task3, boolean required3, T task3FailVal,
			T successVal, boolean atleastOneRequired, T atleastOneFailVal, boolean atleastTwoRequired, T atleastTwoFailVal) {
		if(atleastOneRequired) {
			boolean res1 = task1.getAsBoolean();
			if(res1) {
				return successVal;
			}
			boolean res2 = task2.getAsBoolean();
			if(res2) {
				return successVal;
			}
			boolean res3 = task3.getAsBoolean();
			if(res3) {
				return successVal;
			}
			return atleastOneFailVal;
		}

		if(atleastTwoRequired) {
			boolean res1 = task1.getAsBoolean();
			boolean res2 = task2.getAsBoolean();
			if(res1 && res2) {
				return successVal;
			}
			boolean res3 = task3.getAsBoolean();
			if((res1 && res3) || (res2 && res3)) {
				return successVal;
			}
			return atleastTwoFailVal;
		}

		boolean res1 = task1.getAsBoolean();
		if(required1 && !res1) {
			return task1FailVal;
		}

		if(!required2 && !required3 && res1) {
			return successVal;
		}

		boolean res2 = task2.getAsBoolean();
		if(required2 && !res2) {
			return task2FailVal;
		}

		if(!required1 && !required3 && res2) {
			return successVal;
		}

		boolean res3 = task3.getAsBoolean();
		if(required3 && !res3) {
			return task3FailVal;
		}

		if(!required1 && !required2 && res3) {
			return successVal;
		}

		return successVal;
	}


	/** Run three tasks which return integers, with requirements constraining the success values of one or both.
	 * Return error values based on the results of running one or both tasks.<br>
	 * (NOTE: This function is a designed to compliment methods like {@link TextParser#nextIf(char, Appendable)} which return an integer})
	 * {@code 'succeed' = return true}<br>
	 * {@code 'fails' = return false}<br>
	 * @param task1Expected the value representing success for task 1
	 * @param task1 the first task
	 * @param required1 true if task 1 is required to succeed
	 * @param task1FailVal the value to return if task 1 fails
	 * @param task2Expected the value representing success for task 2
	 * @param task2 the second task
	 * @param required2 true if task 2 is required to succeed
	 * @param task2FailVal the value to return if task 2 fails
	 * @param successVal the value to return if one or both tasks succeed and match the required constraints
	 * @param atleastOneRequired this flags causes the function to ignore {@code required1} and {@code required2} and behave as {@code (task1 || task2)},
	 * which is not possible when explicitly setting {@code required1} and {@code required2} 
	 * @param atleastOneFailVal the value to return if both tasks fail and {@code atleastOneRequired} was true
	 * @return one of the failure values or the success value
	 */
	public static <T> T testThreeTasks(int task1Expected, IntSupplier task1, boolean required1, T task1FailVal,
			int task2Expected, IntSupplier task2, boolean required2, T task2FailVal,
			int task3Expected, IntSupplier task3, boolean required3, T task3FailVal,
			T successVal, boolean atleastOneRequired, T atleastOneFailVal, boolean atleastTwoRequired, T atleastTwoFailVal) {
		if(atleastOneRequired) {
			boolean res1 = task1.getAsInt() == task1Expected;
			if(res1) {
				return successVal;
			}
			boolean res2 = task2.getAsInt() == task2Expected;
			if(res2) {
				return successVal;
			}
			boolean res3 = task3.getAsInt() == task3Expected;
			if(res3) {
				return successVal;
			}
			return atleastOneFailVal;
		}

		if(atleastTwoRequired) {
			boolean res1 = task1.getAsInt() == task1Expected;
			boolean res2 = task2.getAsInt() == task2Expected;
			if(res1 && res2) {
				return successVal;
			}
			boolean res3 = task3.getAsInt() == task3Expected;
			if((res1 && res3) || (res2 && res3)) {
				return successVal;
			}
			return atleastTwoFailVal;
		}

		boolean res1 = task1.getAsInt() == task1Expected;
		if(required1 && !res1) {
			return task1FailVal;
		}

		if(!required2 && !required3 && res1) {
			return successVal;
		}

		boolean res2 = task2.getAsInt() == task2Expected;
		if(required2 && !res2) {
			return task2FailVal;
		}

		if(!required1 && !required3 && res2) {
			return successVal;
		}

		boolean res3 = task3.getAsInt() == task3Expected;
		if(required3 && !res3) {
			return task3FailVal;
		}

		if(!required1 && !required2 && res3) {
			return successVal;
		}

		return successVal;
	}

}
