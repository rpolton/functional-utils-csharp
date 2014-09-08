package me.shaftesbury.utils.functional;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 01/12/13
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public interface Iterable2<T> extends java.lang.Iterable<T>
{
    Iterable2<T> filter(Func<? super T,Boolean> f);
    <U>Iterable2<U> map(Func<? super T,? extends U> f);
    <U> Iterable2<U> mapi(final Func2<Integer, T, ? extends U> f);
    <U>Iterable2<U> choose(Func<? super T,Option<U>> f);
    boolean exists(Func<? super T,Boolean> f);
    boolean forAll(Func<? super T,Boolean> f);
    <U>boolean forAll2(final Func2<? super U, ? super T,Boolean> f, final Iterable<U> input1);
    <U>U fold(Func2<? super U,? super T,? extends U> f, U seed);
    List<T> toList();
    Object[] toArray();
    Set<T> toSet();
    <K,V>Map<K,V> toDictionary(final Func<? super T,? extends K> keyFn, final Func<? super T,? extends V> valueFn);
    T last();
    Iterable2<T> sortWith(final Comparator<T> f);
    Iterable2<T> concat(final Iterable2<T> list2);
    T find(Func<? super T,Boolean> f);
    int findIndex(Func<? super T,Boolean> f);
    <U>U pick(final Func<? super T,Option<U>> f);
    <U>Iterable2<U> collect(final Func<? super T,? extends Iterable<U>> f);

    Iterable2<T> take(final int howMany);
    Iterable2<T> skip(final int howMany);

    String join(final String delimiter);
    T findLast(final Func<? super T,Boolean> f);
    Pair<List<T>,List<T>> partition(final Func<? super T,Boolean> f);

    <U>Iterable2<org.javatuples.Pair<T,U>> zip(final Iterable2<? extends U> l2);
    //<U>org.javatuples.Pair<List<T>,List<U>> unzip();
    <U,V>Iterable2<Triplet<T,U,V>> zip3(final Iterable<? extends U> l2, final Iterable<? extends V> l3);
    <U>U in(final Func<Iterable2<T>, U> f);

    <U>Map<U,List<T>> groupBy(final Func<? super T, ? extends U> keyFn);
}
