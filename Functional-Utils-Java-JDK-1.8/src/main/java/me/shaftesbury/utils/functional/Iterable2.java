package me.shaftesbury.utils.functional;

import org.javatuples.Triplet;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 01/12/13
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public interface Iterable2<T> extends java.lang.Iterable<T>
{
    Iterable2<T> filter(Function<T,Boolean> f);
    <U>Iterable2<U> map(Function<T,U> f);
    <U>Iterable2<U> choose(Function<T,Option<U>> f);
    boolean exists(Function<T,Boolean> f);
    boolean forAll(Function<T,Boolean> f);
    <U>U fold(BiFunction<U,T,U> f, U seed);
    List<T> toList();
    Iterable2<T> sortWith(final Comparator<T> f);
    Iterable2<T> concat(final Iterable2<T> list2);
    T find(Function<T,Boolean> f);
    int findIndex(Function<T,Boolean> f);
    <U>U pick(final Function<T,Option<U>> f);
    <U>Iterable2<U> collect(final Function<T,Iterable<U>> f);

    Iterable2<T> take(final int howMany);

    <U>Iterable2<org.javatuples.Pair<T,U>> zip(final Iterable2<U> l2);
    //<U>org.javatuples.Pair<List<T>,List<U>> unzip();
    <U,V>Iterable2<Triplet<T,U,V>> zip3(final Iterable<U> l2, final Iterable<V> l3);
    <U>U in(final Function<Iterable2<T>, U> f);
}
