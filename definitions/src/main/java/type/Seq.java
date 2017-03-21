package type;

import java.util.stream.Stream;

/**
 * A read only sequence of items. The sequence may be empty, but will never
 * contain a null value.
 *
 * @param <T>
 *            type of member within the sequence.
 */
public interface Seq<T> extends Iterable<T> {

	/**
	 * @throws IndexOutOfBoundsException
	 *             if index is out of bounds.
	 */
	T get(int index);

	int size();

	boolean isEmpty();

	/**
	 * @return a parallel stream. If processing order is significant then
	 *         {@link Stream#sequential()} should be used to convert to a
	 *         sequential stream, or
	 *         {@link Stream#forEachOrdered(java.util.function.Consumer)} used
	 *         to process items in order. Note that
	 *         {@link java.util.stream.Collectors#toList()} will collect items
	 *         in the correct order even if parallel.
	 */
	Stream<T> stream();
}
