package me.shaftesbury.utils.functional;

import org.javatuples.Triplet;

import java.util.*;

/**
 * Created by Bob on 05/12/13.
 */
public class ListHelper
{
    public static final <T>List2<T> create(final java.util.List<T> l)
    {
        return new List2<T>() {
            private final java.util.List<T> list = l;

            public final Iterable2<T> filter(final Func<T,Boolean> f) { return IterableHelper.create(Functional.seq.filter(f,list)); }
            public final <U>Iterable2<U> map(final Func<T,U> f) { return IterableHelper.create(Functional.seq.map(f, list)); }
            public final <U>Iterable2<U> choose(final Func<T,Option<U>> f) { return IterableHelper.create(Functional.seq.choose(f, list)); }
            public final boolean exists(final Func<T,Boolean> f) { return Functional.exists(f, list); }
            public final boolean forAll(final Func<T,Boolean> f) { return Functional.forAll(f, list); }
            public final <U>U fold(final Func2<U,T,U> f, final U seed) { return Functional.fold(f, seed, list); }
            public final List<T> toList() { return list; }
            public final Iterable2<T> sortWith(final Comparator<T> f) { return create(Functional.sortWith(f,list)); }
            public final Iterable2<T> concat(final Iterable2<T> list2) { return IterableHelper.create(Functional.seq.concat(list,list2));}
            public final T find(final Func<T,Boolean> f) { return Functional.find(f,list);}
            public int findIndex(final Func<T,Boolean> f) { return Functional.findIndex(f,list);}
            public final <B>B pick(final Func<T,Option<B>> f){return Functional.pick(f,list);}
            public final Iterable2<T> take(final int howMany){return create(Functional.take(howMany, list));}

            public final <U>Iterable2<org.javatuples.Pair<T,U>> zip(final Iterable2<U> l2) { return create(Functional.zip(list,l2));}
            //public final <U>org.javatuples.Pair<List<T>,List<U>> unzip(){return Functional.unzip(i);}
            public final <U,V>Iterable2<Triplet<T,U,V>> zip3(final Iterable<U> l2, final Iterable<V> l3){return create(Functional.zip3(list,l2,l3));}

            public final <U>Iterable2<U> collect(final Func<T,Iterable<U>> f){return create(Functional.collect(f,list));}
            public <U>U in(final Func<Iterable2<T>, U> f){ return f.apply(this); }

            @Override
            public int size() {
                return list.size();
            }

            @Override
            public boolean isEmpty() {
                return list.isEmpty();
            }

            @Override
            public boolean contains(Object o) {
                return list.contains(o);
            }

            @Override
            public Iterator<T> iterator() {
                return list.iterator();
            }

            @Override
            public Object[] toArray() {
                return list.toArray();
            }

            @Override
            public <T1> T1[] toArray(T1[] a) {
                return list.toArray(a);
            }

            @Override
            public boolean add(T t) {
                return list.add(t);
            }

            @Override
            public boolean remove(Object o) {
                return list.remove(o);
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return list.containsAll(c);
            }

            @Override
            public boolean addAll(Collection<? extends T> c) {
                return list.addAll(c);
            }

            @Override
            public boolean addAll(int index, Collection<? extends T> c) {
                return list.addAll(index,c);
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return list.removeAll(c);
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return list.retainAll(c);
            }

            @Override
            public void clear() {
                list.clear();
            }

            @Override
            public T get(int index) {
                return list.get(index);
            }

            @Override
            public T set(int index, T element) {
                return list.set(index,element);
            }

            @Override
            public void add(int index, T element) {
                list.add(index,element);
            }

            @Override
            public T remove(int index) {
                return list.remove(index);
            }

            @Override
            public int indexOf(Object o) {
                return list.indexOf(o);
            }

            @Override
            public int lastIndexOf(Object o) {
                return list.lastIndexOf(o);
            }

            @Override
            public ListIterator<T> listIterator() {
                return list.listIterator();
            }

            @Override
            public ListIterator<T> listIterator(int index) {
                return list.listIterator(index);
            }

            @Override
            public List<T> subList(int fromIndex, int toIndex) {
                return list.subList(fromIndex,toIndex);
            }
        };
    }
}
