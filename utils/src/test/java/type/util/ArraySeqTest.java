package type.util;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import type.Seq;
import type.util.ArraySeq;

public class ArraySeqTest {

    @Test
    public void shouldGetTheCorrectItem() {
        Seq<Integer> seq = new ArraySeq<>(new Integer[]{2, 3, 4});

        assertThat(seq.get(0)).isEqualTo(2);
        assertThat(seq.get(1)).isEqualTo(3);
        assertThat(seq.get(2)).isEqualTo(4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldFailIfIndexOutOfBounds() {
        Seq<Integer> seq = new ArraySeq<>(new Integer[]{2, 3, 4});

        seq.get(3);
    }

    @Test
    public void shouldGetTheSize() {
        Seq<Integer> seq = new ArraySeq<>(new Integer[]{2, 3, 4});

        assertThat(seq.size()).isEqualTo(3);
    }

    @Test
    public void shouldGetIfEmpty() {
        Seq<Integer> seq = new ArraySeq<>(new Integer[]{2, 3, 4});
        assertThat(seq.isEmpty()).isEqualTo(false);

        Seq<Integer> seq2 = new ArraySeq<>(new Integer[0]);
        assertThat(seq2.isEmpty()).isEqualTo(true);
    }

    @Test
    public void shouldIterate() {
        Iterator<Integer> it = new ArraySeq<>(new Integer[]{5, 6}).iterator();

        assertThat(it.hasNext()).isEqualTo(true);
        assertThat(it.next()).isEqualTo(5);
        assertThat(it.hasNext()).isEqualTo(true);
        assertThat(it.next()).isEqualTo(6);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailIfIteratePastEnd() {
        Iterator<Integer> it = new ArraySeq<>(new Integer[]{6}).iterator();

        it.next();
        it.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldFailIfRemoveFromIterator() {
        Iterator<Integer> it = new ArraySeq<>(new Integer[]{6}).iterator();

        it.remove();
    }

    @Test
    public void shouldStream() {
        Seq<Integer> seq = new ArraySeq<>(new Integer[]{2, 3, 4});

        List<Integer> result = seq.stream().collect(toList());

        assertThat(result).contains(2, 3, 4);
    }

    @Test
    public void shouldParalleliseStream() {
        final int SIZE = 200;
        Seq<Integer> seq = createCountingArraySeq(SIZE);
        Map<Thread, Object> threadSet = new ConcurrentHashMap<>();

        List<Integer> result = seq.stream()
                .filter(i -> {
                    threadSet.put(Thread.currentThread(), "");
                    return true;
                }).collect(toList());

        testCountingList(result, SIZE);
        // Cannot guarantee that multiple threads will have been used.
        //assertThat(threadSet.size()).isGreaterThan(1);

        assertThat(seq.stream().isParallel()).isTrue();
    }

    @Test
    public void shouldAllowSequentialStream() {
        final int SIZE = 200;
        Seq<Integer> seq = createCountingArraySeq(SIZE);
        Map<Thread, Object> threadSet = new ConcurrentHashMap<>();

        List<Integer> result = seq.stream().sequential()
                .filter(i -> {
                    threadSet.put(Thread.currentThread(), "");
                    return true;
                }).collect(toList());

        testCountingList(result, SIZE);
        assertThat(threadSet.size()).isEqualTo(1);

        assertThat(seq.stream().sequential().isParallel()).isFalse();
    }

    private Seq<Integer> createCountingArraySeq(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return new ArraySeq<>(list.toArray(new Integer[size]));
    }

    private void testCountingList(List<Integer> list, int size) {
        assertThat(list.size()).isEqualTo(size);
        for (int i = 0; i < size; i++) {
            assertThat(list.get(i)).isEqualTo(i);
        }
    }
}
