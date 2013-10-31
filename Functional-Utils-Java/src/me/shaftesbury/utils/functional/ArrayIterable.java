package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 09:14
 * To change this template use File | Settings | File Templates.
 */

import java.util.Iterator;

public final class ArrayIterable<T> implements  Iterable<T>
{
    private final T[] _a;
    private ArrayIterable(final T[] array) { _a = array; }

    public final Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            private final T[] _array = _a;
            private int _posn = 0;

            public final boolean hasNext() { return _posn < _array.length; }
            public final T next() { return _array[_posn++]; }
            public void remove() { throw new UnsupportedOperationException("remove is not permitted in ArrayIterable"); }
        };
    }

    public final static <T>ArrayIterable<T> create(final T[] array) { return new ArrayIterable<T>(array); }
}
