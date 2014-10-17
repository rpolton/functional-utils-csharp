package me.shaftesbury.utils.functional.primitive;

import java.util.*;

public class IntList implements IntIterable
{
    private final int[] backingStore;
    public IntList(){backingStore=new int[0];}
    public IntList(final int size) {backingStore=new int[size];}
    public IntList(final int[]array) {backingStore = Arrays.copyOf(array, array.length);}

    public int size() {
        return backingStore.length;
    }

    public boolean isEmpty() {
        return backingStore.length==0;
    }

    public boolean contains(final int i) {
        return Arrays.binarySearch(backingStore,i)>=0;
    }

    @Override
    public IntIterator iterator() {
        return new IntIteratorImpl(backingStore);
    }

    public int[] toArray() {
        return Arrays.copyOf(backingStore, backingStore.length);
    }

    @SuppressWarnings("unchecked")
    public <T>T[] toArray(T[] a) {
        if (a.length < backingStore.length) {
            // Make a new array of a's runtime type, but my contents:
            final Integer[] temp = new Integer[backingStore.length];
            for(int i=0;i<backingStore.length;++i) temp[i]=backingStore[i];
            return (T[]) Arrays.copyOf(temp, backingStore.length, a.getClass());
        }
        System.arraycopy(backingStore, 0, a, 0, backingStore.length);
        if (a.length > backingStore.length)
            a[backingStore.length] = null;
        return a;
    }

    public boolean add(final int integer) {
        return false;
    }

//    public boolean remove(Object o) {
//        return false;
//    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends Integer> c) {
        return false;
    }

    public boolean addAll(int index, Collection<? extends Integer> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public int get(final int index) {
        return backingStore[index];
    }

    public int set(final int index, final int element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, Integer element) {

    }

    public Integer remove(int index) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<Integer> listIterator() {
        return null;
    }

    public ListIterator<Integer> listIterator(int index) {
        return null;
    }

    public List<Integer> subList(int fromIndex, int toIndex) {
        return null;
    }
}
