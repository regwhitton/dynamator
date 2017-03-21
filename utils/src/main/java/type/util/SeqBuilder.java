package type.util;

import static util.Verify.argNotNull;
import static util.Verify.containsNoNullArgs;

import java.util.ArrayList;
import java.util.List;

import type.Seq;

/**
 * A builder that creates a parallelise-able implementation of {@link Seq}. The
 * implementation uses {@link Object} arrays and is unchecked at runtime.
 * Instead it relies on compile time checking.
 * <p>
 * Returns the same empty sequence when there are no items or
 * {@link #emptySeq()} is used. The empty sequence also always returns the same
 * exhausted {@link java.util.Iterator}. This is to prevent the creation of
 * multiple identical objects.
 *
 * @param <T>
 *            type of members to be contained within the sequence.
 */
public class SeqBuilder<T> {

	private static final Seq<Object> EMPTY_SEQ = new EmptySeq<>();

	private final List<T> seq = new ArrayList<>();

	/**
	 * @param element
	 *            to add to the sequence.
	 * @return this @{link SeqBuilder} to allow the fluent builder pattern.
	 * @throws NullPointerException
	 *             if a null argument is passed.
	 */
	public SeqBuilder<T> add(T element) {
		seq.add(argNotNull(element));
		return this;
	}

	/**
	 * @param otherSeq
	 *            sequence of elements to be added to the end of the new
	 *            sequence.
	 * @return this @{link SeqBuilder} to allow the fluent builder pattern.
	 */
	public SeqBuilder<T> addAll(Seq<T> otherSeq) {
		otherSeq.forEach(x -> seq.add(x));
		return this;
	}

	@SuppressWarnings("unchecked")
	public Seq<T> build() {
		if (seq.isEmpty()) {
			return emptySeq();
		}
		return new ArraySeq<>((T[]) seq.toArray());
	}

	public static <T> SeqBuilder<T> create() {
		return new SeqBuilder<>();
	}

	/**
	 * Create a @{link Seq} from the arguments. Assumes ownership of the
	 * argument list, that the caller will not modify it, and that the compiler
	 * will not allow the wrong type to be passed.
	 *
	 * @param <T>
	 *            the type of items in the sequence.
	 * @param elements
	 *            the items to put into the sequence.
	 * @throws NullPointerException
	 *             if a null argument is passed.
	 * @return the created {@link Seq} containing the items.
	 */
	@SafeVarargs
	public static <T> Seq<T> seq(T... elements) {
		containsNoNullArgs(elements);
		if (elements.length == 0) {
			return emptySeq();
		}
		return new ArraySeq<>(elements);
	}

	@SuppressWarnings("unchecked")
	public static <T> Seq<T> emptySeq() {
		return (Seq<T>) EMPTY_SEQ;
	}
}
