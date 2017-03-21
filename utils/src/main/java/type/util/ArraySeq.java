package type.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import type.Seq;

/**
 * Implements {@link Seq} backed by the array passed. Promises not to modify the array, but assumes ownership of the
 * passed {@link Object} array, and that the caller will not modify it either. This implementation allows streams to be
 * parallelised.
 */
class ArraySeq<T> implements Seq<T> {

    private final T[] list;

    ArraySeq(T[] list) {
        this.list = list;
    }

    @Override
    public T get(int index) {
        return list[index];
    }

    @Override
    public int size() {
        return list.length;
    }

    @Override
    public boolean isEmpty() {
        return list.length == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                if (i == size()) {
                    throw new NoSuchElementException();
                }
                return get(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(orderedImmutableNonNullSpliterator(), true);
    }

    private Spliterator<T> orderedImmutableNonNullSpliterator() {
        return Spliterators.spliterator(list, 0, list.length, Spliterator.ORDERED | Spliterator.IMMUTABLE
                | Spliterator.NONNULL);
    }
}
