package util;

public class Verify {

	/**
	 * Verify that an argument is not null.
	 * 
	 * @return the passed reference, unless it is null.
	 * @throws IllegalArgumentException
	 *             if value is null.
	 */
	public static <T> T argNotNull(T value) {
		if (value == null) {
			throw new IllegalArgumentException("unexpected null value provided");
		}
		return value;
	}

	/**
	 * Verify that an argument is not null.
	 * 
	 * @return the passed reference, unless it is null.
	 * @throws IllegalArgumentException
	 *             if value is null.
	 */
	public static <T> T argNotNull(String argName, T value) {
		if (value == null) {
			throw new IllegalArgumentException("unexpected null " + argName + " value provided");
		}
		return value;
	}

	/**
	 * Verify that an argument is not null or blank (only whitespace).
	 * 
	 * @return the passed reference, unless it is null or blank
	 * @throws IllegalArgumentException
	 *             if value is null, empty or only contains whitespace
	 */
	public static <T extends CharSequence> T argNotNullOrBlank(T value) {
		if (value == null) {
			throw new IllegalArgumentException("unexpected null value provided");
		}
		if (value.length() == 0 || value.chars().allMatch(Character::isWhitespace)) {
			throw new IllegalStateException("unexpected blank value provided");
		}
		return value;
	}

	/**
	 * Verify that an argument is not negative.
	 * 
	 * @return the passed integer, unless it is negative.
	 * @throws IllegalArgumentException
	 *             if value is negative.
	 */
	public static int argNotNeg(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("unexpected negative value: " + value);
		}
		return value;
	}

	/**
	 * Verify that an argument is not negative.
	 * 
	 * @return the passed integer, unless it is negative.
	 * @throws IllegalArgumentException
	 *             if value is negative.
	 */
	public static int argNotNeg(String argName, int value) {
		if (value < 0) {
			throw new IllegalArgumentException("unexpected negative " + argName + " value: " + value);
		}
		return value;
	}

	/**
	 * Verify that an argument is in range.
	 * 
	 * @return the passed integer, unless it is less than range start, or
	 *         equal/greater that range end.
	 * @throws IllegalArgumentException
	 *             if value is negative.
	 */
	public static int argInRange(String argName, int value, int start, int end) {
		if (value < start || value >= end) {
			throw new IllegalArgumentException("" + argName + " is " + value + " which is outside range value: " + start
					+ " to " + end + " (exclusive)");
		}
		return value;
	}

	/**
	 * Verify that a dependency has been provided.
	 * 
	 * @throws IllegalStateException
	 *             if value is null.
	 */
	public static void hasBeenProvided(Object value) {
		if (value == null) {
			throw new IllegalStateException("dependency has not been provided");
		}
	}

	/**
	 * Verify that a reference is not null.
	 * 
	 * @return the passed reference, unless it is null.
	 * @throws NullPointerException
	 *             if value is null.
	 */
	public static <T> T isNotNull(T value) {
		if (value == null) {
			throw new NullPointerException("unexpected null value");
		}
		return value;
	}

	/**
	 * Verify that a reference is not null.
	 * 
	 * @return the passed reference, unless it is null.
	 * @throws NullPointerException
	 *             if value is null.
	 */
	public static <T> T isNotNull(String valueDescription, T value) {
		if (value == null) {
			throw new NullPointerException("unexpected null value for " + valueDescription);
		}
		return value;
	}

	/**
	 * Verify that a varargs array contains no null values.
	 * 
	 * @return the passed reference, unless it contains any null values.
	 * @throws IllegalArgumentException
	 *             if array contains a null value.
	 */
	public static <T> T[] containsNoNullArgs(T[] args) {
		for (T arg : args) {
			if (arg == null) {
				throw new IllegalArgumentException("unexpected null varargs argument");
			}
		}
		return args;
	}

	/**
	 * Verify that a value is not negative.
	 * 
	 * @return the passed integer, unless it is negative.
	 * @throws IllegalArgumentException
	 *             if value is negative.
	 */
	public static int isNotNeg(String argName, int value) {
		if (value < 0) {
			throw new IllegalStateException("unexpected negative " + argName + " value: " + value);
		}
		return value;
	}

	/**
	 * Verify that a value is greater than.
	 * 
	 * @return the passed integer, unless it is less than.
	 * @throws IllegalArgumentException
	 *             if value is less.
	 */
	public static int isAtLeast(String argName, int value, int minimum) {
		if (value < minimum) {
			throw new IllegalStateException("" + argName + " was " + value + " it must be at aleast " + minimum);
		}
		return value;
	}
}
