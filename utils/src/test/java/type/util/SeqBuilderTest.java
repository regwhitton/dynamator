package type.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import type.Seq;
import type.util.EmptySeq;
import type.util.SeqBuilder;

public class SeqBuilderTest {

    @Test
    public void shouldBuildSeqWithAdd() {
        SeqBuilder<Integer> blr = SeqBuilder.create();
        Seq<Integer> seq = blr.add(0).add(1).add(2).add(3).build();

        testCountingSeq(seq, 4);
    }

    @Test
    public void shouldCreateSeqFromArgs() {
        Seq<Integer> seq = SeqBuilder.seq(0, 1, 2, 3);

        testCountingSeq(seq, 4);
    }

    @Test
    public void shouldAddASequence() {
        Seq<Integer> seqToAdd = SeqBuilder.seq(1, 2);
        SeqBuilder<Integer> blr = SeqBuilder.create();
        Seq<Integer> seq = blr.add(0).addAll(seqToAdd).add(3).build();

        testCountingSeq(seq, 4);
    }

    @Test
    public void shouldReuseSameEmptySet() {
        Seq<Integer> seq1 = SeqBuilder.seq();
        SeqBuilder<Integer> blr = SeqBuilder.create();
        Seq<Integer> seq2 = blr.build();

        assertThat(seq1).isInstanceOf(EmptySeq.class);
        assertThat(seq1).isSameAs(seq2);
    }

    private void testCountingSeq(Seq<Integer> seq, int size) {
        assertThat(seq.size()).isEqualTo(size);
        for (int i = 0; i < size; i++) {
            assertThat(seq.get(i)).isEqualTo(i);
        }
    }
}
