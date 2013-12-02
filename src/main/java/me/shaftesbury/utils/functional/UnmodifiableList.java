package me.shaftesbury.utils.functional;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 09:27
 * To change this template use File | Settings | File Templates.
 */

public final class UnmodifiableList<T> implements List<T>
{
    private final List<T> _collection;

    public UnmodifiableList(final List<T> input) { _collection = input; }

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
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("");
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
    public Object[] toArray(Object[] a) {
        return _collection.toArray(a);
    }

    public final boolean equals(Object o)
    {
        return o instanceof UnmodifiableList<?> &&
                _collection.containsAll((UnmodifiableList)o) &&
                ((UnmodifiableList) o).containsAll(_collection);
    }

    public final int hashCode() { return 13 * _collection.hashCode(); }

    @Override
    public T get(int index) {
        return _collection.get(index);
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public int indexOf(Object o) {
        return _collection.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return _collection.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return _collection.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return _collection.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new UnmodifiableList<T>(_collection.subList(fromIndex,toIndex));
    }
}
