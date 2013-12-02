package me.shaftesbury.utils.functional;

import org.javatuples.Triplet;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 29/11/13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class IterableHelper<T>
{
    public static final <T>Iterable2<T> create(final java.lang.Iterable<T> it)
    {
        return new Iterable2<T>()
        {
            private final java.lang.Iterable<T> i = it;
            @Override
            public Iterator<T> iterator() { return i.iterator(); }

            public final Iterable2<T> filter(final Func<T,Boolean> f) { return create(Functional.seq.filter(f,i)); }
            public final <U>Iterable2<U> map(final Func<T,U> f) { return create(Functional.seq.map(f, i)); }
            public final <U>Iterable2<U> choose(final Func<T,Option<U>> f) { return create(Functional.seq.choose(f, i)); }
            public final boolean exists(final Func<T,Boolean> f) { return Functional.exists(f, i); }
            public final boolean forAll(final Func<T,Boolean> f) { return Functional.forAll(f, i); }
            public final <U>U fold(final Func2<U,T,U> f, final U seed) { return Functional.fold(f, seed, i); }
            public final List<T> toList() { return Functional.toList(i); }
            public final Iterable2<T> sortWith(final Comparator<T> f) { return create(Functional.sortWith(f,toList())); }
            public final Iterable2<T> concat(final Iterable2<T> list2) { return create(Functional.seq.concat(i,list2));}
            public final T find(final Func<T,Boolean> f) { return Functional.find(f,i);}
            public int findIndex(final Func<T,Boolean> f) { return Functional.findIndex(f,i);}
            public final <B>B pick(final Func<T,Option<B>> f){return Functional.pick(f,i);}
            public final Iterable2<T> take(final int howMany){return create(Functional.take(howMany, i));}

            public final <U>Iterable2<org.javatuples.Pair<T,U>> zip(final Iterable2<U> l2) { return create(Functional.zip(i,l2));}
            //public final <U>org.javatuples.Pair<List<T>,List<U>> unzip(){return Functional.unzip(i);}
            public final <U,V>Iterable2<Triplet<T,U,V>> zip3(final Iterable<U> l2, final Iterable<V> l3){return create(Functional.zip3(i,l2,l3));}

            public final <U>Iterable2<U> collect(final Func<T,Iterable<U>> f){return create(Functional.collect(f,i));}
            public <U>U in(final Func<Iterable2<T>, U> f){ return f.apply(this); }
        };
    }

    public static final <T>Iterable2<T> init(Func<Integer,T> f, int howMany) { return create(Functional.seq.init(f, howMany)); }
    public final static <T>Iterable2<T> init(final Func<Integer,T> f) { return create(Functional.seq.init(f));}
    @SafeVarargs
    public static <T>Iterable2<T> asList(T... a) { return create(Arrays.asList(a)); }
}