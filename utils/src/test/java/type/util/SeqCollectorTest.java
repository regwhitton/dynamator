package type.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import type.Seq;
import type.util.SeqBuilder;
import type.util.SeqCollector;

public class SeqCollectorTest {

    @Test
    public void shouldCollectValuesIntoSeq() {
        Seq<Integer> seq = Arrays.asList(1, 2, 3, 4).stream().collect(SeqCollector.toSeq());
        assertThat(seq.size()).isEqualTo(4);
        assertThat(seq.get(0)).isEqualTo(1);
        assertThat(seq.get(1)).isEqualTo(2);
        assertThat(seq.get(2)).isEqualTo(3);
        assertThat(seq.get(3)).isEqualTo(4);
    }

    @Test
    public void shouldCollectValuesFromMultipleThreadsIntoSeqInCorrectOrder() {
        final int SIZE = 200;
        Seq<Integer> source = createCountingSeq(SIZE);

        Seq<Integer> result = source.stream().collect(SeqCollector.toSeq());

        testCountingSeq(result, SIZE);
    }

    private Seq<Integer> createCountingSeq(int size) {
        SeqBuilder<Integer> srcBldr = SeqBuilder.create();
        for (int i = 0; i < size; i++) {
            srcBldr.add(i);
        }
        return srcBldr.build();
    }

    private void testCountingSeq(Seq<Integer> seq, int size) {
        assertThat(seq.size()).isEqualTo(size);
        for (int i = 0; i < size; i++) {
            assertThat(seq.get(i)).isEqualTo(i);
        }
    }
}
