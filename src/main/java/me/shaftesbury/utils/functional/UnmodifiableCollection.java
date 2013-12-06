package me.shaftesbury.utils.functional;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 09:27
 * To change this template use File | Settings | File Templates.
 */

public final class UnmodifiableCollection<T> implements Collection<T>
{
    private final Collection<T> _collection;

    public UnmodifiableCollection(final Collection<T> input) { _collection = input; }

    @Override
    public final int size() { return _collection.size(); }

    @Override
    public boolean isEmpty() {
        return _collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _collection.contains(o);
    }

    @Override
    public Iterator iterator() {
        return _collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return _collection.toArray();
    }

    @Override
    public boolean add(Object o) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return _collection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public <U>U[] toArray(U[] a) {
        return _collection.toArray(a);
    }

    public final boolean equals(Object o)
    {
        try{
        return o instanceof Collection<?> &&
                //_collection.containsAll((Collection)o) &&
                //((Collection) o).containsAll(_collection);
        Functional.forAll2(new Func2<Object, Object, Boolean>() {
            @Override
            public Boolean apply(Object o, Object o2) {
                return o.equals(o2);
            }
        },ArrayIterable.create(((Collection<?>)o).toArray()),ArrayIterable.create(_collection.toArray()));
        } catch(Exception e ) { return false;}
    }

    public final int hashCode() { return 13 * _collection.hashCode(); }
}
