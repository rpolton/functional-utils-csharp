package me.shaftesbury.utils;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 23/10/13
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
public final class Enumerators
{
    private Enumerators(){}

    public static final <T>Iterable<T> ReverseEnum(final List<T> list) throws Exception
    {
        if (list == null) throw new /*ArgumentNull*/Exception("list");

        if (list.isEmpty())
            throw new /*Argument*/Exception("Collection is empty");

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


}
