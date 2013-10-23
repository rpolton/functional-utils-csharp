/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 16/10/13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
package me.shaftesbury.utils;

import org.javatuples.Pair;

import java.util.*;

public final class functional
{
    private functional() {}

    public static boolean isNullOrEmpty(final String s)
    {
        return s==null || s.isEmpty();
    }

    public static <T>String join(final String delimiter, final Iterable<T> strs)
    {
        if(strs==null) return "";
        Iterator<T> it = strs.iterator();
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while(it.hasNext())
        {
            if(!isFirst) sb.append(delimiter);
            sb.append(it.next());
            isFirst=false;
        }
        return sb.toString();
    }

    public static String indentBy(final int howMany, final String unitOfIndentation, final String indentThis)
    {
        Collection<String> indentation = init(
                new Func<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return unitOfIndentation;
                    }
                }, howMany);
        return fold(new Func2<String, String, String>() {
            @Override
            public String apply(String state, String str) {
                return str + state;
            }
        },indentThis, indentation);
    }

    public static <A, B>Pair<A,Collection<B>> foldAndChoose(
        Func2<A, B, Pair<A,Option<B>>> f,
        A initialValue, Iterable<B> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        A state = initialValue;
        Collection<B> results = new ArrayList<B>();
        for (B b : input)
        {
            Pair<A, Option<B>> intermediate = f.apply(state, b);
            state = intermediate.getValue0();
            if (!intermediate.getValue1().isNone())
                results.add(intermediate.getValue1().Some());
        }
        return new Pair<A, Collection<B>>(state, results);
    }

    public static final <T>List<T> convert(final Enumeration<T> input)
    {
        final List<T> output = new ArrayList<T>();
        while(input.hasMoreElements())
            output.add(input.nextElement());
        return output;
    }

    /// <summary>
    /// Analogue of string.Join for List&lt;T&gt; with the addition of a user-defined map function
    /// </summary>
    /// <typeparam name="T"></typeparam>
    /// <param name="separator"></param>
    /// <param name="l"></param>
    /// <param name="fn"></param>
    /// <returns></returns>
    public static <T>String join(String separator, Iterable<T> l, Func<T, String> fn) throws Exception
    {
        if (l == null) throw new /*ArgumentNull*/Exception("l");
        if (fn == null) throw new /*ArgumentNull*/Exception("fn");

        return join(separator, map(fn,l));
    }

    /// <summary>return lowerBound &lt; val &lt; upperBound</summary>
    public static <T extends Comparable<T>>boolean between(T lowerBound, T upperBound, T val) throws Exception
    {
        if (val == null) throw new /*ArgumentNull*/Exception("val");

        return val.compareTo(lowerBound) == 1 && val.compareTo(upperBound) == -1;
    }

    /// <summary> findLast: (A -> bool) -> A list -> A</summary>
    public static <A>A findLast(Func<A,Boolean> f, List<A> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        for (A a : Enumerators.ReverseEnum(input))
            if (f.apply(a))
                return a;
        throw new /*KeyNotFound*/Exception();
    }

    public interface Func<A,R>
    {
        public R apply(final A a);
    }

    public static <A, B> B In( final A input, final Func<A, B> f)
    {
        return f.apply(input);
    }

    public static <A, B, C> Func<A, C> Then(final Func<A, B> f, final Func<B, C> g)
    {
        return new Func<A, C>()
        {
            @Override
            public C apply(A x)
            {
                return g.apply(f.apply(x));
            }
        };
    }

    public static final Func<Integer,Boolean> IsEven = new Func<Integer, Boolean>()
    {
        @Override
        public Boolean apply(Integer i)
        {
            return i % 2 == 0;
        }
    };

    public static final Func<Integer,Boolean> IsOdd = new Func<Integer, Boolean>()
    {
        @Override
        public Boolean apply(Integer i)
        {
            return i % 2 != 0;
        }
    };

    public interface Func2<A,B,C> // disappointing! This should be related to Func<A,Func<B,C>>
    {
        public C apply(final A a, final B b);
    }


    /// <summary> init: int -> (int -> A) -> A list</summary>
    public static <T>Collection<T> init(final Func<Integer,T> f,final int howMany)
    {
        //if (f == null) throw new ArgumentNullException("f");

        Collection<T> output = new ArrayList<T>();
        for(int i=0; i<howMany; ++i)
            output.add(f.apply(i));
        return output;
    }

    /// <summary> map: (A -> B) -> A list -> B list</summary>
    public static <A,B> Collection<B> map(final Func<A, B> f, final Iterable<A> input)
    {
        Collection<B> output = new ArrayList<B>();
        for(A a : input)
            output.add(f.apply(a));
        return output;
    }

    /// <summary> sortWith: (A -> A -> int) -> A list -> A list</summary>
    public static <A>Collection<A> sortWith(final Comparator<A> f, final List<A> input)
    {
        List<A> output = new ArrayList<A>(input);
        Collections.sort(output, f);
        return output;
    }

    public static <A extends Comparable<A>>int Sorter(final A left, final A right)
    {
        return left.compareTo(right);
    }
    public static Comparator<Integer> dSorter = new Comparator<Integer>()
    {
        @Override public int compare(Integer i, Integer j) { return Sorter(i, j); }
    };

    public static <T> String Stringify(final T a) { return a.toString(); }
    public static Func<Integer, String> dStringify = new Func<Integer, String>()
    {
        @Override public String apply(Integer i) { return Stringify(i); }
    };

    /// <summary> forAll2: (A -> B -> bool) -> A list -> B list -> bool</summary>
    public static <A, B>boolean forAll2(final Func2<A, B,Boolean> f, final Iterable<A> input1, final Iterable<B> input2) throws Exception
    {
        Iterator<A> enum1 = input1.iterator();
        Iterator<B> enum2 = input2.iterator();
        boolean enum1Moved = false, enum2Moved = false;
        do
        {
            enum1Moved = enum1.hasNext();
            enum2Moved = enum2.hasNext();
            if (enum1Moved && enum2Moved && !f.apply(enum1.next(), enum2.next()))
                return false;
        } while (enum1Moved && enum2Moved);
        if( enum1Moved != enum2Moved)
            throw new /*Argument*/Exception();
        return true;
    }

    public static <A>Collection<A> filter(final Func<A,Boolean> pred, final Iterable<A> input)
    {
        Collection<A> output = new ArrayList<A>();
        for(A element : input)
        {
            if(pred.apply(element))
                output.add(element);
        }
        return output;
    }

    /// <summary> exists: (A -> bool) -> A list -> bool</summary>
    public static <A>boolean exists(final Func<A,Boolean> f, final Iterable<A> input)
    {
        for(A a : input)
            if(f.apply(a))
                return true;
        return false;
    }

    /// <summary> not: (A -> bool) -> (A -> bool)</summary>
    public static <A>Func<A,Boolean> not(final Func<A,Boolean> f)
    {
        return new Func<A,Boolean>(){@Override public Boolean apply(A a) { return !f.apply(a);}};
    }

    /// <summary> forAll: (A -> bool) -> A list -> bool</summary>
    public static <A>boolean forAll(final Func<A,Boolean> f, final Iterable<A> input)
    {
        return !exists(not(f), input);
    }

    /// <summary> not2: (A -> B -> bool) -> (A -> B -> bool)</summary>
    public static <A,B>Func2<A,B,Boolean> not2(final Func2<A,B,Boolean> f)
    {
        return new Func2<A,B,Boolean>(){@Override public Boolean apply(A a, B b) { return !f.apply(a,b);}};
    }

    /// <summary> partition: (A -> bool) -> A list -> A list * A list</summary>
    /// <returns> (list * list). The first list contains all items for which f(a) is true. The second list contains the remainder.</returns>
    public static <A>org.javatuples.Pair<Collection<A>,Collection<A>> partition(final Func<A,Boolean> f, final Iterable<A> input)
    {
        Collection<A> left = new ArrayList<A>();
        Collection<A> right = new ArrayList<A>();
        for (A a : input)
            if (f.apply(a))
                left.add(a);
            else
                right.add(a);
        return new org.javatuples.Pair<Collection<A>,Collection<A>>(left, right);
    }
    /*
    /// <summary> choose: (A -> B option) -> A list -> B list</summary>
    public static <A, B>Collection<B> choose(Func<A, Option<B>> f, Iterable<A> input)
    {
        Collection<B> results = new ArrayList<B>();
        for(A a : input)
        {
            Option<B> intermediate = f.apply(a);
            if (!intermediate.None)
                results.add(intermediate.Some);
        }
        return results;
    }
    */

    /// <summary> fold: (A -> B -> A) -> A -> B list -> A</summary>
    public static <A, B>A fold(Func2<A, B, A> f, A initialValue, Iterable<B> input)
    {
        A state = initialValue;
        for (B a : input)
            state = f.apply(state, a);
        return state;
    }
}