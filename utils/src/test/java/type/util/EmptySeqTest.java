package type.util;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import type.Seq;
import type.util.EmptySeq;

public class EmptySeqTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldFailIfIndexOutOfBounds() {
        Seq<Integer> seq = new EmptySeq<>();

        seq.get(0);
    }

    @Test
    public void shouldGetTheSize0() {
        Seq<Integer> seq = new EmptySeq<>();

        assertThat(seq.size()).isEqualTo(0);
    }

    @Test
    public void shouldSignalIsEmpty() {
        Seq<Integer> seq = new EmptySeq<>();
        assertThat(seq.isEmpty()).isEqualTo(false);
    }

    @Test
    public void shouldHaveNoIterations() {
        Iterator<Integer> it = new EmptySeq<Integer>().iterator();

        assertThat(it.hasNext()).isEqualTo(false);
    }

    @Test
    public void shouldReuseTheSameIterator() {
        Seq<Integer> seq = new EmptySeq<>();
        Iterator<Integer> it1 = seq.iterator();
        Iterator<Integer> it2 = seq.iterator();

        assertThat(it1).isSameAs(it2);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailIfIteratePastEnd() {
        Iterator<Integer> it = new EmptySeq<Integer>().iterator();

        it.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldFailIfRemoveFromIterator() {
        Iterator<Integer> it = new EmptySeq<Integer>().iterator();

        it.remove();
    }

    @Test
    public void shouldStreamNoItems() {
        Seq<Integer> seq = new EmptySeq<>();

        List<Integer> result = seq.stream().collect(toList());

        assertThat(result.isEmpty()).isTrue();
    }
}
