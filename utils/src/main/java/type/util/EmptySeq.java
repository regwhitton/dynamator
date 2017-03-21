package type.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import type.Seq;

/**
 * Implements {@link Seq} but this version never contains any items.
 */
class EmptySeq<T> implements Seq<T> {

    private final EmptyIterator<T> emptyIterator = new EmptyIterator<>();

    @Override
    public T get(int index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return emptyIterator;
    }

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    private static class EmptyIterator<T> implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
