package util;

import java.util.Comparator;

public class Order {

	private Order() {
	}

	public static final ReverseComparator<?> REVERSE_COMPARATOR = new ReverseComparator<>();

	/**
	 * Returns a {@link Comparator} for sorting items into reverse order, given
	 * that they implement {@link Comparable}. e.g.
	 * 
	 * <pre>
	 * seq.stream().sorted(Order.reversed()).collect(SeqCollector.toSeq());
	 * </pre>
	 * 
	 * @param <T>
	 *            The type of item being sorted.
	 * @return the {@link Comparator}.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> Comparator<T> reversed() {
		return (ReverseComparator<T>) REVERSE_COMPARATOR;
	}

	private static class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {
		@Override
		public int compare(T o1, T o2) {
			return o2.compareTo(o1);
		}
	}
}
