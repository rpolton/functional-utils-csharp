package me.shaftesbury.utils.functional;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 23/10/13
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public final class Iterators
{
    private Iterators(){}

    public static final <T>Iterable<T> ReverseIterator(final List<T> list)
    {
        if (list == null) throw new IllegalArgumentException("list");

        if (list.isEmpty())
            throw new IllegalArgumentException("Collection is empty");

        return new Iterable<T>() {
            private final List<T> _list=list;
            private int _posn=list.size()-1;
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return _posn>=0;
                    }

                    @Override
                    public T next() {
                        return _list.get(_posn--);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static final <T>Iterable<T> SteppedIterator(final int step, final Iterable<T> enumerable)
    {
        if (enumerable == null) throw new IllegalArgumentException("enumerable");

        if (step < 1)
            throw new IllegalArgumentException("Invalid step value, must be greater than zero.");

        return new Iterable<T>(){
            final private Iterable<T> cache = enumerable;

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>(){
                    final private Iterator<T> posn = cache.iterator();

                    @Override
                    public boolean hasNext() {
                        for(int i=0;i<step-1;++i) if(posn.hasNext()) posn.next();
                        return posn.hasNext();
                    }

                    @Override
                    public T next() {
                        for(int i=0;i<step-1;++i) posn.next();
                        return posn.next();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
