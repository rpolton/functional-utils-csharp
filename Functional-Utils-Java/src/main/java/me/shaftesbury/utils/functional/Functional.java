/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 16/10/13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 16/10/13
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
package me.shaftesbury.utils.functional;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.*;

public final class Functional
{
    private Functional() {}

    public final static boolean isNullOrEmpty(final String s)
    {
        return s==null || s.isEmpty();
    }

    public final static <T>String join(final String delimiter, final Iterable<T> strs)
    {
        if(strs==null) return "";
        final Iterator<T> it = strs.iterator();
        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while(it.hasNext())
        {
            if(!isFirst) sb.append(delimiter);
            sb.append(it.next());
            isFirst=false;
        }
        return sb.toString();
    }

    public final static String indentBy(final int howMany, final String unitOfIndentation, final String indentThis)
    {
        final Collection<String> indentation = init(
                new Func<Integer, String>() {
                    @Override
                    public String apply(final Integer integer) {
                        return unitOfIndentation;
                    }
                }, howMany);
        return fold(new Func2<String, String, String>() {
            @Override
            public String apply(final String state, final String str) {
                return str + state;
            }
        }, indentThis, indentation);
    }

    public final static <A, B>Pair<A,List<B>> foldAndChoose(
            final Func2<A, B, Pair<A,Option<B>>> f,
            final A initialValue, final Iterable<B> input) throws OptionNoValueAccessException
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        A state = initialValue;
        final List<B> results = new ArrayList<B>();
        for (final B b : input)
        {
            final Pair<A, Option<B>> intermediate = f.apply(state, b);
            state = intermediate.getValue0();
            if (!intermediate.getValue1().isNone())
                results.add(intermediate.getValue1().Some());
        }
        return new Pair<A, List<B>>(state, Collections.unmodifiableList(results));
    }

    public static final <T>List<T> toList(final Enumeration<T> input)
    {
        final List<T> output = new ArrayList<T>();
        while(input.hasMoreElements())
            output.add(input.nextElement());
        return Collections.unmodifiableList(output);
    }

    /// <summary>
    /// Analogue of string.Join for List&lt;T&gt; with the addition of a user-defined map function
    /// </summary>
    /// <typeparam name="T"></typeparam>
    /// <param name="separator"></param>
    /// <param name="l"></param>
    /// <param name="fn"></param>
    /// <returns></returns>
    public final static <T>String join(final String separator, final Iterable<T> l, final Func<? super T, String> fn)
    {
        if (l == null) throw new IllegalArgumentException("l");
        if (fn == null) throw new IllegalArgumentException("fn");

        return join(separator, map(fn,l));
    }

    /// <summary>return lowerBound &lt; val &lt; upperBound</summary>
    public final static <T extends Comparable<T>>boolean between(final T lowerBound, final T upperBound, final T val)
    {
        if (val == null) throw new IllegalArgumentException("val");

        return val.compareTo(lowerBound) == 1 && val.compareTo(upperBound) == -1;
    }

    /// <summary> find: (A -> bool) -> A list -> A</summary>
    public final static <A>A find(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        for(final A a : input)
            if(f.apply((a)))
                return a;
        throw new NoSuchElementException();
    }

    public final static <A>Func<Iterable<A>,A> find(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, A>() {
            @Override
            public A apply(final Iterable<A> input) {
                return Functional.find(f,input);
            }
        };
    }

    /// <summary> findIndex: (A -> bool) -> A list -> int</summary>
    public static <A>int findIndex(final Func<A,Boolean> f, final Iterable<? extends A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        int pos = 0;
        for (final A a : input)
            if (f.apply(a))
                return pos;
            else pos++;
        throw new IllegalArgumentException();
    }

    /// <summary> findLast: (A -> bool) -> A list -> A</summary>
    public final static <A>A findLast(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        final Pair<List<A>,Iterable<A>> p = takeNAndYield(input,1);
        final Pair<A,Boolean> seed = Pair.with(p.getValue0().get(0),f.apply(p.getValue0().get(0)));
        final Pair<A,Boolean> result = fold(new Func2<Pair<A,Boolean>,A,Pair<A,Boolean>>(){
            @Override public Pair<A,Boolean> apply(final Pair<A,Boolean> state, final A item){return f.apply(item)?Pair.with(item,true):state;}
        },seed,p.getValue1());

        if(result.getValue1()) return result.getValue0();
        throw new NoSuchElementException();
    }

    /// <summary> findLast: (A -> bool) -> A list -> A</summary>
    public final static <A>A findLast(final Func<? super A,Boolean> f, final List<A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        for (final A a : Iterators.reverse(input))
            if (f.apply(a))
                return a;
        throw new NoSuchElementException();
    }

    public final static <A>Func<List<A>,A> findLast(final Func<A,Boolean> f)
    {
        return new Func<List<A>, A>() {
            @Override
            public A apply(final List<A> input) {
                return Functional.findLast(f,input);
            }
        };
    }

    /// <summary> pick: (A -> B option) -> A list -> B</summary>
    public static <A, B>B pick(final Func<A,Option<B>> f, final Iterable<? extends A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        for(final A a : input)
        {
            final Option<B> intermediate = f.apply(a); // which is, effectively, if(f(a)) return f(a), but without evaluating f twice
            if (!intermediate.isNone())
                return intermediate.Some();
        }
        throw new NoSuchElementException();
    }

    public static <A,B>Func<Iterable<A>,B> pick(final Func<? super A,Option<B>> f)
    {
        return new Func<Iterable<A>, B>() {
            @Override
            public B apply(final Iterable<A> input) {
                return Functional.pick(f,input);
            }
        };
    }

    public final static <A, B, AA extends A> B in(final AA input, final Func<A, B> f)
    {
        return f.apply(input);
    }

    public final static <A, B, C> Func<A, C> then(final Func<A, ? extends B> f, final Func<B, C> g)
    {
        return new Func<A, C>()
        {
            @Override
            public C apply(final A x)
            {
                return g.apply(f.apply(x));
            }
        };
    }

    public final static <T>Func<T,T> identity()
    {
        return new Func<T, T>() {
            @Override
            public T apply(final T t) {
                return t;
            }
        };
    }

    public static final Func<Integer,Boolean> isEven = new Func<Integer, Boolean>()
    {
        @Override
        public Boolean apply(final Integer i)
        {
            return i % 2 == 0;
        }
    };
    public static final Func<Integer,Boolean> isOdd = new Func<Integer, Boolean>()
    {
        @Override
        public Boolean apply(final Integer i)
        {
            return i % 2 != 0;
        }
    };
    public static final Func2<Integer,Integer,Integer> count = new Func2<Integer, Integer, Integer>() {
                @Override
                public Integer apply(final Integer state, final Integer b) {
                    return state + 1;
                }
            };
    public static final Func2<Integer,Integer,Integer> sum = new Func2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(final Integer state, final Integer b) {
            return state + b;
        }
    };

    public static final <T extends Comparable<T>>Func<T,Boolean> greaterThan(final T that)
    {
        return new Func<T, Boolean>()
        {
            @Override
            public Boolean apply(final T ths)
            {
                return ths.compareTo(that)>0;
            }
        };
    }

    public static final <T extends Comparable<T>>Func<T,Boolean> greaterThanOrEqual(final T that)
    {
        return new Func<T, Boolean>()
        {
            @Override
            public Boolean apply(final T ths)
            {
                return ths.compareTo(that)>=0;
            }
        };
    }

    public static final <T extends Comparable<T>>Func<T,Boolean> lessThan(final T that)
    {
        return new Func<T, Boolean>()
        {
            @Override
            public Boolean apply(final T ths)
            {
                return ths.compareTo(that)<0;
            }
        };
    }

    public static final <T extends Comparable<T>>Func<T,Boolean> lessThanOrEqual(final T that)
    {
        return new Func<T, Boolean>()
        {
            @Override
            public Boolean apply(final T ths)
            {
                return ths.compareTo(that)<=0;
            }
        };
    }

    /// <summary> init: int -> (int -> A) -> A list</summary>
    public final static <T>List<T> init(final Func<Integer,T> f,final int howMany)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if(howMany<1) throw new IllegalArgumentException("howMany");

        final List<T> output = new ArrayList<T>();
        for(int i=1; i<=howMany; ++i)
            output.add(f.apply(i));
        return Collections.unmodifiableList(output);
    }

    /// <summary> map: (A -> B) -> A list -> B list</summary>
    public final static <A,B> List<B> map(final Func<A, ? extends B> f, final Iterable<? extends A> input)
    {
        final List<B> output = new ArrayList<B>();
        for(final A a : input)
            output.add(f.apply(a));
        return Collections.unmodifiableList(output);
    }

    public final static <A,B> Func<Iterable<A>,List<B>> map(final Func<? super A, ? extends B> f)
    {
        return new Func<Iterable<A>, List<B>>() {
            @Override
            public List<B> apply(final Iterable<A> input) {
                return Functional.map(f,input);
            }
        };
    }

    /// <summary> mapi: (int -> A -> B) -> A list -> B list</summary>
    public final static <A,B> List<B> mapi(final Func2<Integer, A, ? extends B> f, final Iterable<? extends A> input)
    {
        final List<B> output = new ArrayList<B>();
        int pos = 0;
        for(final A a : input)
            output.add(f.apply(pos++, a));
        return Collections.unmodifiableList(output);
    }

    public final static <A,B> Func<Iterable<A>,List<B>> mapi(final Func2<Integer, ? super A, ? extends B> f)
    {
        return new Func<Iterable<A>, List<B>>() {
            @Override
            public List<B> apply(final Iterable<A> input) {
                return Functional.mapi(f,input);
            }
        };
    }

    /// <summary> sortWith: (A -> A -> int) -> A list -> A list</summary>
    public final static <A, AA extends A>List<AA> sortWith(final Comparator<A> f, final List<AA> input)
    {
        final List<AA> output = new ArrayList<AA>(input);
        Collections.sort(output, f);
        return Collections.unmodifiableList(output);
    }

    public final static <A extends Comparable<A>>int Sorter(final A left, final A right)
    {
        return left.compareTo(right);
    }

    public final static Comparator<Integer> dSorter = new Comparator<Integer>()
    {
        @Override public int compare(final Integer i, final Integer j) { return Sorter(i, j); }
    };

    public final static <T> String Stringify(final T a) { return a.toString(); }
    public final static <T>Func<T, String> dStringify()
    {
        return new Func<T, String>()
        {
            @Override public String apply(final T i) { return Stringify(i); }
        };
    }

    /// <summary> forAll2: (A -> B -> bool) -> A list -> B list -> bool</summary>
    public final static <A, B,AA extends A,BB extends B>boolean forAll2(final Func2<A, B,Boolean> f, final Iterable<AA> input1, final Iterable<BB> input2)
    {
        final Iterator<AA> enum1 = input1.iterator();
        final Iterator<BB> enum2 = input2.iterator();
        boolean enum1Moved = false, enum2Moved = false;
        do
        {
            enum1Moved = enum1.hasNext();
            enum2Moved = enum2.hasNext();
            if (enum1Moved && enum2Moved && !f.apply(enum1.next(), enum2.next()))
                return false;
        } while (enum1Moved && enum2Moved);
        if( enum1Moved != enum2Moved)
            throw new IllegalArgumentException();
        return true;
    }

    public final static <A>List<A> filter(final Func<? super A,Boolean> pred, final Iterable<A> input)
    {
        final List<A> output = new ArrayList<A>();
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return Collections.unmodifiableList(output);
    }

    public static final <T>Func<Iterable<T>,List<T>> filter(final Func<? super T,Boolean> f)
    {
        return new Func<Iterable<T>, List<T>>() {
            @Override
            public List<T> apply(final Iterable<T> input) {
                return Functional.filter(f,input);
            }
        };
    }

    /// <summary> exists: (A -> bool) -> A list -> bool</summary>
    public final static <A>boolean exists(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        for(final A a : input)
            if(f.apply(a))
                return true;
        return false;
    }

    public final static <A>Func<Iterable<A>,Boolean> exists(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, Boolean>() {
            @Override
            public Boolean apply(final Iterable<A> input) {
                return Functional.exists(f,input);
            }
        };
    }

    /// <summary> not: (A -> bool) -> (A -> bool)</summary>
    public final static <A>Func<A,Boolean> not(final Func<A,Boolean> f)
    {
        return new Func<A,Boolean>(){@Override public Boolean apply(final A a) { return !f.apply(a);}};
    }

    /// <summary> forAll: (A -> bool) -> A list -> bool</summary>
    public final static <A>boolean forAll(final Func<A,Boolean> f, final Iterable<? extends A> input)
    {
        return !exists(not(f), input);
    }

    public final static <A>Func<Iterable<A>,Boolean> forAll(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, Boolean>() {
            @Override
            public Boolean apply(final Iterable<A> input) {
                return Functional.forAll(f,input);
            }
        };
    }

    /// <summary> not2: (A -> B -> bool) -> (A -> B -> bool)</summary>
    public final static <A,B> Func2<A,B,Boolean> not2(final Func2<A,B,Boolean> f)
    {
        return new Func2<A,B,Boolean>(){@Override public Boolean apply(final A a, final B b) { return !f.apply(a,b);}};
    }

    /// <summary> partition: (A -> bool) -> A list -> A list * A list</summary>
    /// <returns> (list * list). The first list contains all items for which f(a) is true. The second list contains the remainder.</returns>
    public final static <A>Pair<List<A>,List<A>> partition(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        final List<A> left = new ArrayList<A>();
        final List<A> right = new ArrayList<A>();
        for (final A a : input)
            if (f.apply(a))
                left.add(a);
            else
                right.add(a);
        return new Pair<List<A>,List<A>>(Collections.unmodifiableList(left), Collections.unmodifiableList(right));
    }

    public final static <A>Func<Iterable<A>,Pair<List<A>,List<A>>> partition(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, Pair<List<A>, List<A>>>() {
            @Override
            public Pair<List<A>, List<A>> apply(final Iterable<A> input) {
                return Functional.partition(f,input);
            }
        };
    }

    /// <summary> choose: (A -> B option) -> A list -> B list</summary>
    public final static <A, B>List<B> choose(final Func<? super A, Option<B>> f, final Iterable<A> input)
    {
        final List<B> results = new ArrayList<B>();
        for(final A a : input)
        {
            final Option<B> intermediate = f.apply(a);
            if (!intermediate.isNone())
                results.add(intermediate.Some());
        }
        return Collections.unmodifiableList(results);
    }

    public final static <A, B>Func<Iterable<A>,List<B>> choose(final Func<? super A, Option<B>> f)
    {
        return new Func<Iterable<A>, List<B>>() {
            @Override
            public List<B> apply(final Iterable<A> input) {
                return Functional.choose(f,input);
            }
        };
    }

    /// <summary> fold: (A -> B -> A) -> A -> B list -> A</summary>
    public final static <A, B>A fold(final Func2<? super A, ? super B, ? extends A> f, final A initialValue, final Iterable<B> input)
    {
        A state = initialValue;
        for (final B b : input)
            state = f.apply(state, b);
        return state;
    }

    public final static <A, B>Func<Iterable<B>,A> fold(final Func2<? super A, ? super B, ? extends A> f, final A initialValue)
    {
        return new Func<Iterable<B>, A>() {
            @Override
            public A apply(final Iterable<B> input) {
                return Functional.fold(f,initialValue,input);
            }
        };
    }

    // http://en.wikipedia.org/wiki/Anamorphism
    // unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
    public final static <A,B>List<A> unfold(final Func<? super B,Pair<A,B>> unspool, final Func<? super B,Boolean> finished, final B seed)
    {
        if(unspool==null) throw new IllegalArgumentException("unspool");
        if(finished==null) throw new IllegalArgumentException("finished");

        B next = seed;
        final List<A> results = new ArrayList<A>();
        while(!finished.apply(next)) {
            final Pair<A,B> t = unspool.apply(next);
            results.add(t.getValue0());
            next = t.getValue1();
        }
        return results;
    }

    public final static <A,B>List<A> unfold(final Func<? super B,Option<Pair<A,B>>> unspool, final B seed)
    {
        if(unspool==null) throw new IllegalArgumentException("unspool");

        B next = seed;
        final List<A> results = new ArrayList<A>();
        while(true) {
            final Option<Pair<A,B>> t = unspool.apply(next);
            if(t.isNone()) break;
            results.add(t.Some().getValue0());
            next = t.Some().getValue1();
        }
        return results;
    }

    public final static <T,K,V>Map<K,V> toDictionary(final Func<? super T,? extends K> keyFn, final Func<? super T,? extends V> valueFn, final Iterable<T> input)
    {
        if(keyFn==null) throw new IllegalArgumentException("keyFn");
        if(valueFn==null) throw new IllegalArgumentException("valueFn");

        final Map<K,V> output = new HashMap<K,V>();
        for(final T element : input) output.put(keyFn.apply(element),valueFn.apply(element));
        return Collections.unmodifiableMap(output);
    }

    //public final static <T>T[] toArray(final Iterable<T> input)
    public final static <T>Object[] toArray(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toArray(Iterable<T>): input is null");

        final List<T> output = new ArrayList<T>();
        for(final T element: input) output.add(element);

        return output.toArray(); // this needs to be output.toArray(new T[0]) but that doesn't appear to be allowable Java :-(
    }

    public static final <T>List<T> toMutableList(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toMutableList(Iterable<T>): input is null");

        if(input instanceof List<?>) return Collections.unmodifiableList((List<T>)input);

        final List<T> output = new ArrayList<T>();
        for(final T element: input) output.add(element);

        return output;
    }

    public static final <T>Set<T> toMutableSet(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toMutableSet(Iterable<T>): input is null");

        if(input instanceof Set<?>) return Collections.unmodifiableSet((Set<T>)input);

        final Set<T> output = new HashSet<T>();
        for(final T element: input) output.add(element);

        return output;
    }

    public static final <T>List<T> toList(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toList(Iterable<T>): input is null");
        return Collections.unmodifiableList(toMutableList(input));
    }

    public static final <T>Set<T> toSet(final Iterable<T> input)
    {
        //Sets.newSetFromMap();
        if(input==null) throw new IllegalArgumentException("Functional.toSet(Iterable<T>): input is null");
        return Collections.unmodifiableSet(toMutableSet(input));
    }

    public static final <T>T last(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.last(Iterable<T>): input is null");

        T state = null;
        for(final T element: input) state = element;

        return state;
    }

    public static final <T>T last(final T[] input)
    {
        if(input==null||input.length==0) throw new IllegalArgumentException("Functional.last(Iterable<T>): input is null or empty");

        return input[input.length-1];
    }

    public static final <T>List<T> concat(final Iterable<? extends T> list1, final Iterable<? extends T> list2)
    {
        if(list1==null) throw new IllegalArgumentException("Functional.concat(List<T>,List<T>): list1 is null");
        if(list2==null) throw new IllegalArgumentException("Functional.concat(List<T>,List<T>): list2 is null");

        final List<T> newList = new ArrayList<T>(Functional.toList(list1));
        final boolean didItChange = newList.addAll(Functional.toList(list2));
        return Collections.unmodifiableList(newList);
    }

    public static final<T>List<T> take(final int howMany, final Iterable<? extends T> list)
    {
        if(howMany<0) throw new IllegalArgumentException("Functional.take(int,Iterable<T>): howMany is negative");
        if(list==null) throw new IllegalArgumentException("Functional.take(int,Iterable<T>): list is null");

        if(howMany==0) return new ArrayList<T>(0);

        final List<T> output = new ArrayList<T>(howMany);
        final Iterator<? extends T> iterator = list.iterator();
        for(int i=0;i<howMany;++i)
        {
            if(iterator.hasNext())
                output.add(iterator.next());
            else
                throw new java.util.NoSuchElementException("Cannot take "+howMany+" elements from input list with fewer elements");
        }
        return Collections.unmodifiableList(output);
    }

    public static final<T>Func<Iterable<? extends T>,List<T>> take(final int howMany)
    {
        return new Func<Iterable<? extends T>, List<T>>() {
            @Override
            public List<T> apply(final Iterable<? extends T> input) {
                return Functional.take(howMany,input);
            }
        };
    }

    public static final <T>List<T> skip(final int howMany, final List<? extends T> list)
    {
        if(howMany<0) throw new IllegalArgumentException("Functional.skip(int,List<T>): howMany is negative");
        if(list==null) throw new IllegalArgumentException("Functional.skip(int,List<T>): list is null");

        if(howMany==0) return Collections.unmodifiableList(list);
        final int outputListSize = list.size()-howMany;
        if(outputListSize<=0) return new ArrayList<T>();

        return Collections.unmodifiableList(list.subList(howMany,list.size()));
    }

    public static final<T>Func<List<? extends T>,List<T>> skip(final int howMany)
    {
        return new Func<List<? extends T>, List<T>>() {
            @Override
            public List<T> apply(final List<? extends T> input) {
                return Functional.skip(howMany,input);
            }
        };
    }

    public static final <T>Func<Integer,T> constant(final T constant)
    {
        return new Func<Integer, T>() {
            @Override
            public T apply(final Integer integer) {
                return constant;
            }
        };
    }

    public static final Func<Integer,Integer> range(final Integer startFrom)
    {
        return new Func<Integer,Integer>(){
            private final Integer start = startFrom;
            public Integer apply(final Integer input) {
                return (start-1)+input; // because init starts counting from 1
            }
        };
    }


    public static final <A,B>List<Pair<A,B>> zip(final Iterable<? extends A> l1, final Iterable<? extends B> l2)
    {
        if(l1==null) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l1 is null");
        if(l2==null) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l2 is null");

        final List<Pair<A,B>> output = new ArrayList<Pair<A, B>>();
        final Iterator<? extends A> l1_it = l1.iterator();
        final Iterator<? extends B> l2_it = l2.iterator();

        while(l1_it.hasNext() && l2_it.hasNext()) output.add(new Pair(l1_it.next(),l2_it.next()));
        if(l1_it.hasNext() || l2_it.hasNext()) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l1 and l2 have differing numbers of elements");

        return Collections.unmodifiableList(output);
    }

    public static final <A,B,C>List<Triplet<A,B,C>> zip3(final Iterable<? extends A> l1, final Iterable<? extends B> l2, final Iterable<? extends C> l3)
    {
        if(l1==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1 is null");
        if(l2==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l2 is null");
        if(l3==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l3 is null");

        final List<Triplet<A,B,C>> output = new ArrayList<Triplet<A, B,C>>();
        final Iterator<? extends A> l1_it = l1.iterator();
        final Iterator<? extends B> l2_it = l2.iterator();
        final Iterator<? extends C> l3_it = l3.iterator();

        while(l1_it.hasNext() && l2_it.hasNext() && l3_it.hasNext()) output.add(new Triplet(l1_it.next(),l2_it.next(),l3_it.next()));
        if(l1_it.hasNext() || l2_it.hasNext() || l3_it.hasNext())
            throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1, l2 and l3 have differing numbers of elements");

        return Collections.unmodifiableList(output);
    }

    public static final <A,B>Pair<List<A>,List<B>> unzip(final Iterable<Pair<A,B>> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.unzip(Iterable<Pair<A,B>>): input is null");

        final List<A> l1 = new ArrayList<A>();
        final List<B> l2 = new ArrayList<B>();

        for(final Pair<A,B> pair:input)
        {
            l1.add(pair.getValue0());
            l2.add(pair.getValue1());
        }

        return new Pair(Collections.unmodifiableList(l1),Collections.unmodifiableList(l2));
    }

    public static final <A,B,C>Triplet<List<A>,List<B>,List<C>> unzip3(final Iterable<Triplet<A,B,C>> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.unzip(Iterable<Pair<A,B>>): input is null");

        final List<A> l1 = new ArrayList<A>();
        final List<B> l2 = new ArrayList<B>();
        final List<C> l3 = new ArrayList<C>();

        for(final Triplet<A,B,C> triplet:input)
        {
            l1.add(triplet.getValue0());
            l2.add(triplet.getValue1());
            l3.add(triplet.getValue2());
        }

        return new Triplet(Collections.unmodifiableList(l1),Collections.unmodifiableList(l2),Collections.unmodifiableList(l3));
    }

    public static final <T,U>List<U> collect(final Func<? super T,? extends Iterable<U>> f, final Iterable<T> input)
    {
        List<U> output = new ArrayList<U>();
        for(final T element : input)
            output = Functional.concat(output, Functional.toList(f.apply(element)));
        return Collections.unmodifiableList(output);
    }

    public static final <T,U>Func<Iterable<T>,List<U>> collect(final Func<? super T,? extends Iterable<U>> f)
    {
        return new Func<Iterable<T>, List<U>>() {
            @Override
            public List<U> apply(final Iterable<T> input) {
                return Functional.collect(f,input);
            }
        };
    }

    public static final <A>Pair<List<A>,Iterable<A>> takeNAndYield(final Iterable<A> input, final int howMany)
    {
        if (input == null) throw new IllegalArgumentException("Functional.takeNAndYield: input is null");

        int counter = 0;
        final List<A> output = new ArrayList<A>();
        final Iterator<A> position = input.iterator();
        if(howMany>0&&position.hasNext())
        {
            while(counter<howMany)
            {
                output.add(position.next());
                counter++;
                if (counter < howMany && !position.hasNext()) break;
            }
            return Pair.with(output, (Iterable<A>) new Iterable<A>() {
                @Override
                public Iterator<A> iterator() {
                    return position;
                }
            });
        }
        return Pair.with(output, input);
    }

    public static final <T>Iterable<T> append(final T t, final Iterable<T> input)
    {
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>(){
                    private int counter=0;
                    private Iterator<? extends T> iterator=input.iterator();
                    @Override
                    public boolean hasNext() {
                        return counter==0||iterator.hasNext();
                    }

                    @Override
                    public T next() {
                        return counter++==0 ? t : iterator.next();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Functional.append(T,Iterable<T>): it is not possible to remove elements from this sequence");
                    }
                };
            }
        };
    }

    public static final <T,U>Map<U,List<T>> groupBy(final Func<? super T, ? extends U> keyFn, final Iterable<T> input)
    {
        if (keyFn == null) throw new IllegalArgumentException("Functional.groupBy(Func,Iterable): keyFn is null");
        if (input == null) throw new IllegalArgumentException("Functional.groupBy(Func,Iterable): input is null");

        final Map<U,List<T>> intermediateResults = new HashMap<U,List<T>>();
        for(final T element : input)
        {
            final U key = keyFn.apply(element);
            if(intermediateResults.containsKey(key))
                intermediateResults.get(key).add(element);
            else
            {
                final List<T> list = new ArrayList<T>();
                list.add(element);
                intermediateResults.put(key, list);
            }
        }
        final Map<U,List<T>> output = new HashMap<U,List<T>>();
        for(final Map.Entry<U,List<T>> entry : intermediateResults.entrySet())
             output.put(entry.getKey(),Collections.unmodifiableList(entry.getValue()));
        return Collections.unmodifiableMap(output);
    }

    public static final class seq
    {
        public static final <T,U>Iterable<U> map(final Func<? super T,? extends U> f, final Iterable<T> input)
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<U>() {
                @Override
                public final Iterator<U> iterator() {
                    return new Iterator<U>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func<? super T,? extends U> _f = f;
                        @Override
                        public final boolean hasNext() {
                            return _input.hasNext();
                        }

                        @Override
                        public final U next() {
                            return _f.apply(_input.next());
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.map(Func<T,U>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        public static final <T,U>Func<Iterable<T>,Iterable<U>> map(final Func<? super T,? extends U> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.map(f, input);
                }
            };
        }

        public static final <T>Iterable<T> concat(final Iterable<? extends T> list1, final Iterable<? extends T> list2)
        {
            if(list1==null) throw new IllegalArgumentException("Functional.seq.concat(Iterable<T>,Iterable<T>): list1 is null");
            if(list2==null) throw new IllegalArgumentException("Functional.seq.concat(Iterable<T>,Iterable<T>): list2 is null");

            return new Iterable<T>()
            {
                public Iterator<T> iterator()
                {
                    return new Iterator<T>() {
                        private final Iterator<? extends T> _s1 = list1.iterator();
                        private final Iterator<? extends T> _s2 = list2.iterator();
                        @Override
                        public boolean hasNext() {
                            return _s1.hasNext() || _s2.hasNext();
                        }

                        @Override
                        public T next() {
                            return _s1.hasNext() ? _s1.next() : _s2.next();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.concat(Iterable<T>,Iterable<T>): remove is not supported");
                        }
                    };
                }
            };
        }

        public static final <T>Iterable<T> filter(final Func<? super T,Boolean> f, final Iterable<T> input) //throws NoSuchElementException, IllegalArgumentException, UnsupportedOperationException
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<T>() {
                @Override
                public final Iterator<T> iterator() {
                    return new Iterator<T>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func<? super T,Boolean> _f = f;
                        private T _next = null;
                        @Override
                        public final boolean hasNext() {
                            while(_next==null && // ie we haven't already read the next element
                                _input.hasNext())
                            {
                                final T next = _input.next();
                                if(_f.apply(next))
                                {
                                    _next=next;
                                    return true;
                                }
                            }
                            return _next!=null;
                        }

                        @Override
                        public final T next() {
                            if(hasNext())
                            {
                                final T next = _next;
                                _next=null;
                                return next;
                            }
                            throw new java.util.NoSuchElementException();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.filter(Func<T,Boolean>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        public static final <T>Func<Iterable<T>,Iterable<T>> filter(final Func<? super T,Boolean> f)
        {
            return new Func<Iterable<T>,Iterable<T>>(){
                @Override
                public Iterable<T> apply(final Iterable<T> input) {
                    return Functional.seq.filter(f,input);
                }
            };
        }

        public static final <T,U>Iterable<U> choose(final Func<? super T,Option<U>> f, final Iterable<T> input)
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<U>() {
                @Override
                public final Iterator<U> iterator() {
                    return new Iterator<U>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func<? super T,Option<U>> _f = f;
                        private Option<U> _next = Option.None();
                        @Override
                        public final boolean hasNext() {
                            while(_next.isNone() && // ie we haven't already read the next element
                                    _input.hasNext())
                            {
                                final Option<U> next = _f.apply(_input.next());
                                if(next.isSome())
                                {
                                    _next=next;
                                    return true;
                                }
                            }
                            return _next.isSome();
                        }

                        @Override
                        public final U next()
                        {
                            if(hasNext())
                            {
                                final Option<U> next = _next;
                                _next=Option.None();
                                try {
                                    return next.Some();
                                } catch(final OptionNoValueAccessException e) { throw new java.util.NoSuchElementException(); }
                            }
                            throw new java.util.NoSuchElementException();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.choose(Func<T,U>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        public static final <T,U>Func<Iterable<T>,Iterable<U>> choose(final Func<? super T,Option<U>> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.choose(f,input);
                }
            };
        }

        /// <summary> init: int -> (int -> A) -> A list</summary>
        public final static <T>Iterable<T> init(final Func<Integer,? extends T> f,final int howMany)
        {
            if(f==null) throw new IllegalArgumentException("f");
            if(howMany<1) throw new IllegalArgumentException("howMany");

            return new Iterable<T>()
            {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>()
                    {
                        private int _counter=1;
                        private final Func<Integer,? extends T> _f = f;
                        @Override
                        public boolean hasNext() {
                            return _counter<=howMany;
                        }

                        @Override
                        public T next() {
                            return _f.apply(_counter++);
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.init(Func<T,U>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        /// <summary> init: int -> (int -> A) -> A list</summary>
        public final static <T>Iterable<T> init(final Func<Integer,? extends T> f)
        {
            if(f==null) throw new IllegalArgumentException("f");

            return new Iterable<T>()
            {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>()
                    {
                        private int _counter=1;
                        private final Func<Integer,? extends T> _f = f;
                        @Override
                        public boolean hasNext() {
                            return true;
                        }

                        @Override
                        public T next() {
                            return _f.apply(_counter++);
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.init(Func<T,U>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        public static final <T,U>Iterable<U> collect(final Func<? super T,? extends Iterable<U>> f, final Iterable<T> input)
        {
            if(f==null) throw new IllegalArgumentException("Functional.seq.collect: f is null");
            if(input==null) throw new IllegalArgumentException("Functional.seq.collect: input is null");

            return new Iterable<U>(){

                @Override
                public Iterator<U> iterator() {
                    return new Iterator<U>(){
                        private final Iterator<T> it = input.iterator();
                        private List<U> cache = new ArrayList<U>();
                        private Iterator<U> cacheIterator = cache.iterator();
                        @Override
                        public boolean hasNext() {
                            return it.hasNext() || cacheIterator.hasNext();
                        }

                        @Override
                        public U next() {
                            if(cacheIterator.hasNext()) return cacheIterator.next();
                            cache = toList(f.apply(it.next()));
                            cacheIterator=cache.iterator();
                            return cacheIterator.next();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.collect: remove is not supported");
                        }
                    };
                }
            };
        }

        public static final <T,U>Func<Iterable<T>,Iterable<U>> collect(final Func<? super T,? extends Iterable<U>> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.collect(f,input);
                }
            };
        }

        public static final <T>Iterable<T> skip(final int howMany, final Iterable<T> input)
        {
            if(howMany<0) throw new IllegalArgumentException("Functional.skip(int,Iterable<T>): howMany is negative");
            if(input==null) throw new IllegalArgumentException("Functional.skip(int,Iterable<T>): input is null");

            return new Iterable<T>(){
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        private final Iterator<T> it = input.iterator();
                        private boolean haveWeSkipped = false;
                        @Override
                        public boolean hasNext() {
                            if(haveWeSkipped && it.hasNext()) return true;
                            if(haveWeSkipped) return false;
                            for(int i=0;i<howMany;++i)
                                if(it.hasNext()) it.next();
                                else return false;
                            haveWeSkipped = true;
                            return it.hasNext();
                        }

                        @Override
                        public T next() {
                            final boolean another = hasNext();
                            return it.next();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.skip: remove is not supported");
                        }
                    };
                }
            };
        }
    }


    // Recursive implementations of the functions
    public final static class rec
    {
        private static final <A>Iterable<A> filter(final Func<? super A,Boolean> f, final Iterator<A> input, final List<A> accumulator)
        {
            if(input.hasNext())
            {
                A next = input.next();
                if(f.apply(next)) accumulator.add(next);
                return filter(f,input,accumulator);
            }
            else return accumulator;
        }

        public static final <A>Iterable<A> filter(final Func<? super A,Boolean> f, final Iterable<A> input)
        {
            return filter(f,input.iterator(),new ArrayList<A>());
        }

        private static final <A,B>Iterable<B> map(final Func<? super A,? extends B> f, final Iterator<A> input, final List<B> accumulator)
        {
            if(input.hasNext())
            {
                accumulator.add(f.apply(input.next()));
                return map(f,input,accumulator);
            }
            else return accumulator;
        }

        public static final <A,B>Iterable<B> map(final Func<? super A,? extends B> f, final Iterable<A> input)
        {
            return map(f,input.iterator(),new ArrayList<B>());
        }

        private final static <A,B>A fold(final Func2<? super A,? super B,? extends A> f, final A initialValue, final Iterator<B> input)
        {
            if(input.hasNext())
            {
                B next = input.next();
                return fold(f,f.apply(initialValue,next),input);
            }
            else return initialValue;
        }

        /// <summary> fold: (A -> B -> A) -> A -> B list -> A</summary>
        public final static <A, B>A fold(final Func2<? super A, ? super B, ? extends A> f, final A initialValue, final Iterable<B> input)
        {
            return fold(f,initialValue,input.iterator());
        }

        private final static <A,B>List<A> unfold(final Func<? super B,Pair<A,B>> unspool, final Func<? super B,Boolean> finished, final B seed, final List<A> accumulator)
        {
            if(finished.apply(seed)) return accumulator;
            final Pair<A,B> p = unspool.apply(seed);
            accumulator.add(p.getValue0());
            return unfold(unspool,finished,p.getValue1(),accumulator);
        }

        public final static <A,B>List<A> unfold(final Func<? super B,Pair<A,B>> unspool, final Func<? super B,Boolean> finished, final B seed)
        {
            return unfold(unspool,finished,seed,new ArrayList<A>());
        }

        private final static <A,B>List<A> unfold(final Func<? super B,Option<Pair<A,B>>> unspool, final B seed, final List<A> accumulator)
        {
            final Option<Pair<A,B>> p = unspool.apply(seed);
            if(p.isNone()) return accumulator;
            accumulator.add(p.Some().getValue0());
            return unfold(unspool,p.Some().getValue1(),accumulator);
        }

        public final static <A,B>List<A> unfold(final Func<? super B,Option<Pair<A,B>>> unspool, final B seed)
        {
            return unfold(unspool,seed,new ArrayList<A>());
        }
    }
        /*
        // Following are functions for non-list collections
        */

    public static final <A, B, C>Map<B, C> map_dict(final Func<? super A,Map.Entry<B,C>> f, final Iterable<A> input)
    {
        final Map<B, C> results = new HashMap<B, C>();
        for (final A a : input)
        {
            final Map.Entry<B, C> intermediate = f.apply(a);
            results.put(intermediate.getKey(), intermediate.getValue());
        }
        return results;
    }

    public static final class set
    {
        public final static <A>Set<A> filter(final Func<? super A,Boolean> pred, final Iterable<A> input)
        {
            final Set<A> output = new HashSet<A>();
            for(final A element : input)
            {
                if(pred.apply(element))
                    output.add(element);
            }
            return Collections.unmodifiableSet(output);
        }

        public static final <T,U>Set<U> collect(final Func<? super T,? extends Iterable<U>> f, final Iterable<T> input)
        {
            Set<U> output = new HashSet<U>();
            for(final T element : input)
                output.addAll(Functional.toSet(f.apply(element)));
            return Collections.unmodifiableSet(output);
        }

        public final static <A,B> Set<B> map(final Func<? super A, ? extends B> f, final Iterable<A> input)
        {
            final Set<B> output = new HashSet<B>();
            for(final A a : input)
                output.add(f.apply(a));
            return Collections.unmodifiableSet(output);
        }

        public static final <T>Set<T> concat(final Set<? extends T> list1, final Set<? extends T> list2)
        {
            if(list1==null) throw new IllegalArgumentException("Functional.concat(Set<T>,List<T>): list1 is null");
            if(list2==null) throw new IllegalArgumentException("Functional.concat(Set<T>,List<T>): list2 is null");

            if(list1.size()==0) return Collections.unmodifiableSet(list2);
            if(list2.size()==0) return Collections.unmodifiableSet(list1);

            final Set<T> newList = new HashSet<T>(list1);
            final boolean didItChange = newList.addAll(list2);
            return Collections.unmodifiableSet(newList);
        }

        /*
        * Non-destructive wrappers for set intersection and set difference
         */

        public final static <E>Set<E> intersection(final Set<? extends E> e1, final Set<? extends E> e2)
        {
            Set<E> i = new HashSet<E>(e1);
            i.retainAll(e2);
            return Collections.unmodifiableSet(i);
        }

        public final static <E>Set<E> asymmetricDifference(final Set<? extends E> inSet, final Set<? extends E> notInSet)
        {
            Set<E> i = new HashSet<E>(inSet);
            i.removeAll(notInSet);
            return Collections.unmodifiableSet(i);
        }
    }

    public static final class inTermsOfFold
    {
        public static final <T,U>List<T> map(final Func<? super U,? extends T> f, final Iterable<U> l)
        {
            final List<T> l2 = Functional.fold(new Func2<List<T>,U,List<T>>() {
                @Override
                public List<T> apply(final List<T> state, final U o2) {
                    state.add(f.apply(o2));
                    return state;
                }
            }, new ArrayList<T>(), l);
            return l2;
        }

        public static final <T>List<T> filter(final Func<? super T,Boolean> predicate, final Iterable<T> l)
        {
            final List<T> l2 = Functional.fold(new Func2<List<T>, T, List<T>>() {
                @Override
                public List<T> apply(final List<T> ts, final T o) {
                    if(predicate.apply(o)) ts.add(o);
                    return ts;
                }
            }, new ArrayList<T>(), l);
            return l2;
        }

        public static final <A>List<A> init(final Func<Integer,? extends A> f, final int howMany)
        {
            return Functional.unfold(new Func<Integer, Option<Pair<A,Integer>>>() {
                @Override
                public Option<Pair<A,Integer>> apply(final Integer a) {
                    return a<=howMany ? Option.toOption(Pair.with(f.apply(a), a + 1)) : Option.<Pair<A,Integer>>None();
                }
            }, new Integer(1));
        }

    }

    /*
    // Following are control structures, eg if, switch
     */

    public static final <A,B>B If(final A a, final Func<? super A,Boolean> predicate, final Func<? super A, ? extends B> thenClause, final Func<? super A, ? extends B> elseClause)
    {
        if (a == null) throw new IllegalArgumentException("a");
        if (predicate == null) throw new IllegalArgumentException("predicate");
        if (thenClause == null) throw new IllegalArgumentException("thenClause");
        if (elseClause == null) throw new IllegalArgumentException("elseClause");

        return predicate.apply(a) ? thenClause.apply(a) : elseClause.apply(a);
    }

    public static final <A,B>Case<A,B> toCase(final Func<A,Boolean> pred, final Func<A, B> result)
    {
        if (pred == null) throw new IllegalArgumentException("pred");
        if (result == null) throw new IllegalArgumentException("res");

        return new Case<A, B> ( pred, result );
    }

    public static <A, B>B Switch(final A input, final Iterable<Case<A, B>> cases, final Func<A,B> defaultCase)
    {
        return Switch(input,IterableHelper.create(cases),defaultCase);
    }

    public static <A, B>B Switch(final A input, final Iterable2<Case<A, B>> cases, final Func<A, B> defaultCase)
    {
        if (input == null) throw new IllegalArgumentException("input");
        if (cases == null) throw new IllegalArgumentException("cases");
        if (defaultCase == null) throw new IllegalArgumentException("defaultCase");

        //return Try<InvalidOperationException>.ToTry(input, a => cases.First(chk => chk.check(a)).results(a), defaultCase);
        try {
            return cases.find(new Func<Case<A, B>, Boolean>() {
                @Override
                public Boolean apply(final Case<A, B> abCase) {
                    return abCase.predicate(input);
                }
            }).results(input);
        } catch(final NoSuchElementException k) { return defaultCase.apply(input); }
    }

    public static final <A,B>Func<Pair<A,B>,A> first()
    {
        return new Func<Pair<A, B>, A>() {
            @Override
            public A apply(Pair<A, B> pair) {
                return pair.getValue0();
            }
        };
    }

    public static final <A,B>Func<Pair<A,B>,B> second()
    {
        return new Func<Pair<A, B>, B>() {
            @Override
            public B apply(Pair<A, B> pair) {
                return pair.getValue1();
            }
        };
    }
}