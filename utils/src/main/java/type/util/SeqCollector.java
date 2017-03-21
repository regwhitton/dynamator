package type.util;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import type.Seq;

/**
 * Used to collect the output from a (possibly parallelised) stream into a
 * correctly ordered Seq.
 * 
 * <pre>
 * Seq&lt;Integer&gt; evens = integerSeq.stream()
 *        .filter(i -&gt; (i % 2) == 0).collect(SeqCollector.toSeq());
 * </pre>
 *
 * @param <T>
 *            type of members to be collected within the sequence.
 */
// @see http://www.nurkiewicz.com/2014/07/introduction-to-writing-custom.html
public class SeqCollector<T> implements Collector<T, SeqBuilder<T>, Seq<T>> {

	@Override
	public Supplier<SeqBuilder<T>> supplier() {
		return SeqBuilder::create;
	}

	@Override
	public BiConsumer<SeqBuilder<T>, T> accumulator() {
		return (builder, t) -> builder.add(t);
	}

	@Override
	public BinaryOperator<SeqBuilder<T>> combiner() {
		return (left, right) -> {
			left.addAll(right.build());
			return left;
		};
	}

	@Override
	public Function<SeqBuilder<T>, Seq<T>> finisher() {
		return SeqBuilder::build;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}

	public static <T> Collector<T, ?, Seq<T>> toSeq() {
		return new SeqCollector<>();
	}
}
