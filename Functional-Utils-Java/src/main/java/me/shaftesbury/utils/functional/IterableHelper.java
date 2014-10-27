package me.shaftesbury.utils.functional;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 29/11/13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class IterableHelper
{
    /**
     * Note this is not intended to be a wrapper for a restartable sequence. If you want a restartable sequence turn
     * the underlying container into a concrete collection first.
     * @param it the singly-traversable input sequence
     * @param <T> the type of the underlying data in the input sequence
     * @return an Iterable2
     */
    public static final <T>Iterable2<T> create(final java.lang.Iterable<T> it)
    {
        return new Iterable2<T>()
        {
            private final java.lang.Iterable<T> i = it;
            @Override
            public Iterator<T> iterator() { return i.iterator(); }

            public final Iterable2<T> filter(final Func<? super T,Boolean> f) { return create(Functional.seq.filter(f,i)); }
            public final <U>Iterable2<U> map(final Func<? super T,? extends U> f) { return create(Functional.seq.map(f, i)); }
            public <U> Iterable2<U> mapi(Func2<Integer, T, ? extends U> f) { return create(Functional.seq.mapi(f, i)); }
            public final <U>Iterable2<U> choose(final Func<? super T,Option<U>> f) { return create(Functional.seq.choose(f, i)); }
            public final boolean exists(final Func<? super T,Boolean> f) { return Functional.exists(f, i); }
            public final boolean forAll(final Func<? super T,Boolean> f) { return Functional.forAll(f, i); }
            public <U> boolean forAll2(Func2<? super U, ? super T, Boolean> f, Iterable<U> j) { return Functional.forAll2(f,j,i); }
            public final <U>U fold(final Func2<? super U,? super T,? extends U> f, final U seed) { return Functional.fold(f, seed, i); }
            public final List<T> toList() { return Functional.toList(i); }
            public Object[] toArray() { return Functional.toArray(i); }
            public Set<T> toSet() { return Functional.toSet(i); }
            public <K, V> Map<K, V> toDictionary(Func<? super T, ? extends K> keyFn, Func<? super T, ? extends V> valueFn) { return Functional.toDictionary(keyFn,valueFn,i); }
            public T last() { return Functional.last(i); }
            public final Iterable2<T> sortWith(final Comparator<T> f) { return create(Functional.sortWith(f,toList())); }
            public final Iterable2<T> concat(final Iterable2<T> list2) { return create(Functional.seq.concat(i,list2));}
            public final T find(final Func<? super T,Boolean> f) { return Functional.find(f,i);}
            public int findIndex(final Func<? super T,Boolean> f) { return Functional.findIndex(f,i);}
            public final <B>B pick(final Func<? super T,Option<B>> f){return Functional.pick(f,i);}
            public final Iterable2<T> take(final int howMany){return create(Functional.seq.take(howMany, i));}

            @Override
            public Iterable2<T> takeWhile(Func<? super T, Boolean> f) {
                return create(Functional.seq.takeWhile(f, i));
            }

            public Iterable2<T> skip(int howMany) { return create(Functional.seq.skip(howMany, i)); }

            @Override
            public Iterable2<T> skipWhile(Func<? super T, Boolean> f) {
                return create(Functional.seq.skipWhile(f,i));
            }

            public String join(String delimiter) { return Functional.join(delimiter,i); }
            public T findLast(Func<? super T, Boolean> f) { return Functional.findLast(f,i); }
            public Pair<List<T>, List<T>> partition(Func<? super T, Boolean> f) { return Functional.partition(f, i); }
            public final <U>Iterable2<org.javatuples.Pair<T,U>> zip(final Iterable2<? extends U> l2) { return create(Functional.seq.zip(i, l2));}
            //public final <U>org.javatuples.Pair<List<T>,List<U>> unzip(){return Functional.unzip(i);}
            public final <U,V>Iterable2<Triplet<T,U,V>> zip3(final Iterable<? extends U> l2, final Iterable<? extends V> l3){return create(Functional.seq.zip3(i, l2, l3));}

            public final <U>Iterable2<U> collect(final Func<? super T,? extends Iterable<U>> f){return create(Functional.seq.collect(f, i));}
            public <U>U in(final Func<Iterable2<T>, U> f){ return f.apply(this); }

            public <U> Map<U, List<T>> groupBy(Func<? super T, ? extends U> keyFn) { return Functional.groupBy(keyFn,i); }
        };
    }

    public static final <T>Iterable2<T> createEmpty()
    {
        return new EmptyList<T>();
    }

    private static final class EmptyList<T> implements Iterable2<T>
    {
        public Iterator<T> iterator() { return new Iterator<T>() {
            @Override public boolean hasNext() { return false; }
            @Override public T next() { throw new java.util.NoSuchElementException(); }
            @Override public void remove() { throw new UnsupportedOperationException("Can't remove objects from this container"); }
        }; }

        public final Iterable2<T> filter(final Func<? super T,Boolean> f) { return this; }
        public final <U>Iterable2<U> map(final Func<? super T,? extends U> f) { return new EmptyList<U>(); }
        public <U> Iterable2<U> mapi(Func2<Integer, T, ? extends U> f) { return new EmptyList<U>();}
        public final <U>Iterable2<U> choose(final Func<? super T,Option<U>> f) { return new EmptyList<U>(); }
        public final boolean exists(final Func<? super T,Boolean> f) { return false; }
        public final boolean forAll(final Func<? super T,Boolean> f) { return false; }
        public <U> boolean forAll2(Func2<? super U, ? super T, Boolean> f, Iterable<U> input1) { return false; }
        public final <U>U fold(final Func2<? super U,? super T,? extends U> f, final U seed) { return seed; }
        public final List<T> toList() { return Functional.toList(this); }
        public Object[] toArray() { return Functional.toArray(this); }
        public Set<T> toSet() { return Functional.toSet(this); }
        public <K, V> Map<K, V> toDictionary(Func<? super T, ? extends K> keyFn, Func<? super T, ? extends V> valueFn) { return Functional.toDictionary(keyFn,valueFn,this); }
        public T last() { throw new NoSuchElementException(); }
        public final Iterable2<T> sortWith(final Comparator<T> f) { return this; }
        public final Iterable2<T> concat(final Iterable2<T> list2) { return list2;}
        public final T find(final Func<? super T,Boolean> f) { throw new NoSuchElementException();}
        public int findIndex(final Func<? super T,Boolean> f) { throw new NoSuchElementException();}
        public final <B>B pick(final Func<? super T,Option<B>> f){throw new NoSuchElementException();}
        public final Iterable2<T> take(final int howMany){return this;}
        @Override
        public Iterable2<T> takeWhile(Func<? super T, Boolean> f) { return this; }
        public Iterable2<T> skip(int howMany) { return this; }
        @Override
        public Iterable2<T> skipWhile(Func<? super T, Boolean> f) { return this; }

        public String join(String delimiter) { return Functional.join(delimiter,this); }
        public T findLast(Func<? super T, Boolean> f) { throw new NoSuchElementException(); }
        public Pair<List<T>, List<T>> partition(Func<? super T, Boolean> f) { return Functional.partition(f,this); }

        public final <U>Iterable2<org.javatuples.Pair<T,U>> zip(final Iterable2<? extends U> l2) { throw new IllegalArgumentException("Iterable2.zip: It is not possible to zip an empty list with a non-empty list");}
        //public final <U>org.javatuples.Pair<List<T>,List<U>> unzip(){return Functional.unzip(i);}
        public final <U,V>Iterable2<Triplet<T,U,V>> zip3(final Iterable<? extends U> l2, final Iterable<? extends V> l3){throw new IllegalArgumentException("Iterable2.zip3: It is not possible to zip an empty list with a non-empty list");}

        public final <U>Iterable2<U> collect(final Func<? super T,? extends Iterable<U>> f){return new EmptyList<U>();}
        public <U>U in(final Func<Iterable2<T>, U> f){ return f.apply(this); }

        public <U> Map<U, List<T>> groupBy(Func<? super T, ? extends U> keyFn) { return Collections.EMPTY_MAP; }

        public boolean equals(Object o) { return o instanceof EmptyList<?>; }

        @Override
        public int hashCode() { return 0; }

        @Override
        public String toString() { return "()"; }
    }

    public static final <T>Iterable2<T> init(Func<Integer,T> f, int howMany) { return create(Functional.seq.init(f, howMany)); }
    public final static <T>Iterable2<T> init(final Func<Integer,T> f) { return create(Functional.seq.init(f));}
    @SafeVarargs
    public static <T>Iterable2<T> asList(T... a) { return create(Arrays.asList(a)); }
}