package type;

/**
 * Size of an area. Implements {@link Comparable} so that sizes can be compared
 * and sorted.
 */
public interface Size extends Comparable<Size> {

	int getWidth();

	int getHeight();

	/**
	 * @return true when both width and height match in both sizes.
	 */
	@Override
	boolean equals(Object other);

	/**
	 * A string representing the width and height. e.g. "10x5" when width is 10
	 * and height is 5.
	 */
	@Override
	String toString();
}
