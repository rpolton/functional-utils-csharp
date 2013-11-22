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

    public final static <A, B>Pair<A,List<B>> foldAndChoose(
            final me.shaftesbury.utils.functional.Func2<A, B, Pair<A,Option<B>>> f,
            final A initialValue, final Iterable<B> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        A state = initialValue;
        final List<B> results = new ArrayList<B>();
        for (final B b : input)
        {
            final Pair<A, Option<B>> intermediate = f.apply(state, b);
            state = intermediate.getValue0();
            if (!intermediate.getValue1().isNone())
                results.add(intermediate.getValue1().Some());
        }
        return new Pair<A, List<B>>(state, new UnmodifiableList(results));
    }

    public static final <T>List<T> toList(final Enumeration<T> input)
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
    public final static <T>String join(final String separator, final Iterable<T> l, final Func<T, String> fn) throws Exception
    {
        if (l == null) throw new /*ArgumentNull*/Exception("l");
        if (fn == null) throw new /*ArgumentNull*/Exception("fn");

        return join(separator, map(fn,l));
    }

    /// <summary>return lowerBound &lt; val &lt; upperBound</summary>
    public final static <T extends Comparable<T>>boolean between(final T lowerBound, final T upperBound, final T val) throws Exception
    {
        if (val == null) throw new /*ArgumentNull*/Exception("val");

        return val.compareTo(lowerBound) == 1 && val.compareTo(upperBound) == -1;
    }

    /// <summary> find: (A -> bool) -> A list -> A</summary>
    public final static <A>A find(Func<A,Boolean> f, Iterable<A> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        for(final A a : input)
            if(f.apply((a)))
                return a;
        throw new KeyNotFoundException();
    }

    /// <summary> findIndex: (A -> bool) -> A list -> int</summary>
    public static <A>int findIndex(Func<A,Boolean> f, Iterable<A> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        int pos = 0;
        for (final A a : input)
            if (f.apply(a))
                return pos;
            else pos++;
        throw new IllegalArgumentException();
    }

    /// <summary> findLast: (A -> bool) -> A list -> A</summary>
    public final static <A>A findLast(final Func<A,Boolean> f, final List<A> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        for (final A a : Enumerators.ReverseEnum(input))
            if (f.apply(a))
                return a;
        throw new KeyNotFoundException();
    }

    /// <summary> pick: (A -> B option) -> A list -> B</summary>
    public static <A, B>B pick(Func<A,Option<B>> f, Iterable<A> input) throws Exception
    {
        if (f == null) throw new /*ArgumentNull*/Exception("f");
        if (input == null) throw new /*ArgumentNull*/Exception("input");

        for(final A a : input)
        {
            final Option<B> intermediate = f.apply(a); // which is, effectively, if(f(a)) return f(a), but without evaluating f twice
            if (!intermediate.isNone())
                return intermediate.Some();
        }
        throw new KeyNotFoundException();
    }

    public final static <A, B> B In( final A input, final Func<A, B> f)
    {
        return f.apply(input);
    }

    public final static <A, B, C> Func<A, C> Then(final Func<A, B> f, final Func<B, C> g)
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

    public final static <T>Func<T,T> Identity()
    {
        return new Func<T, T>() {
            @Override
            public T apply(T t) {
                return t;
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
    public static final me.shaftesbury.utils.functional.Func2<Integer,Integer,Integer> Count =
            new Func2<Integer, Integer, Integer>() {
                @Override
                public Integer apply(Integer state, Integer b) {
                    return state + 1;
                }
            };

    /// <summary> init: int -> (int -> A) -> A list</summary>
    public final static <T>List<T> init(final Func<Integer,T> f,final int howMany)
    {
        //if (f == null) throw new ArgumentNullException("f");

        final List<T> output = new ArrayList<T>();
        for(int i=0; i<howMany; ++i)
            output.add(f.apply(i));
        return new UnmodifiableList(output);
    }

    /// <summary> map: (A -> B) -> A list -> B list</summary>
    public final static <A,B> List<B> map(final Func<A, B> f, final Iterable<A> input)
    {
        final List<B> output = new ArrayList<B>();
        for(final A a : input)
            output.add(f.apply(a));
        return new UnmodifiableList(output);
    }

    /// <summary> sortWith: (A -> A -> int) -> A list -> A list</summary>
    public final static <A>List<A> sortWith(final Comparator<A> f, final List<A> input)
    {
        final List<A> output = new ArrayList<A>(input);
        Collections.sort(output, f);
        return new UnmodifiableList(output);
    }

    public final static <A extends Comparable<A>>int Sorter(final A left, final A right)
    {
        return left.compareTo(right);
    }
    public final static Comparator<Integer> dSorter = new Comparator<Integer>()
    {
        @Override public int compare(Integer i, Integer j) { return Sorter(i, j); }
    };

    public final static <T> String Stringify(final T a) { return a.toString(); }
    public final static Func<Integer, String> dStringify = new Func<Integer, String>()
    {
        @Override public String apply(Integer i) { return Stringify(i); }
    };

    /// <summary> forAll2: (A -> B -> bool) -> A list -> B list -> bool</summary>
    public final static <A, B>boolean forAll2(final me.shaftesbury.utils.functional.Func2<A, B,Boolean> f, final Iterable<A> input1, final Iterable<B> input2) throws Exception
    {
        final Iterator<A> enum1 = input1.iterator();
        final Iterator<B> enum2 = input2.iterator();
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

    public final static <A>List<A> filter(final Func<A,Boolean> pred, final Iterable<A> input)
    {
        final List<A> output = new ArrayList<A>();
        for(final A element : input)
        {
            if(pred.apply(element))
                output.add(element);
        }
        return new UnmodifiableList(output);
    }

    /// <summary> exists: (A -> bool) -> A list -> bool</summary>
    public final static <A>boolean exists(final Func<A,Boolean> f, final Iterable<A> input)
    {
        for(final A a : input)
            if(f.apply(a))
                return true;
        return false;
    }

    /// <summary> not: (A -> bool) -> (A -> bool)</summary>
    public final static <A>Func<A,Boolean> not(final Func<A,Boolean> f)
    {
        return new Func<A,Boolean>(){@Override public Boolean apply(A a) { return !f.apply(a);}};
    }

    /// <summary> forAll: (A -> bool) -> A list -> bool</summary>
    public final static <A>boolean forAll(final Func<A,Boolean> f, final Iterable<A> input)
    {
        return !exists(not(f), input);
    }

    /// <summary> not2: (A -> B -> bool) -> (A -> B -> bool)</summary>
    public final static <A,B> me.shaftesbury.utils.functional.Func2<A,B,Boolean> not2(final me.shaftesbury.utils.functional.Func2<A,B,Boolean> f)
    {
        return new Func2<A,B,Boolean>(){@Override public Boolean apply(A a, B b) { return !f.apply(a,b);}};
    }

    /// <summary> partition: (A -> bool) -> A list -> A list * A list</summary>
    /// <returns> (list * list). The first list contains all items for which f(a) is true. The second list contains the remainder.</returns>
    public final static <A>org.javatuples.Pair<List<A>,List<A>> partition(final Func<A,Boolean> f, final Iterable<A> input)
    {
        final List<A> left = new ArrayList<A>();
        final List<A> right = new ArrayList<A>();
        for (final A a : input)
            if (f.apply(a))
                left.add(a);
            else
                right.add(a);
        return new org.javatuples.Pair<List<A>,List<A>>(new UnmodifiableList(left), new UnmodifiableList(right));
    }

    /// <summary> choose: (A -> B option) -> A list -> B list</summary>
    public final static <A, B>List<B> choose(final Func<A, Option<B>> f, final Iterable<A> input) throws OptionNoValueAccessException
    {
        final List<B> results = new ArrayList<B>();
        for(final A a : input)
        {
            final Option<B> intermediate = f.apply(a);
            if (!intermediate.isNone())
                results.add(intermediate.Some());
        }
        return new UnmodifiableList(results);
    }


    /// <summary> fold: (A -> B -> A) -> A -> B list -> A</summary>
    public final static <A, B>A fold(final me.shaftesbury.utils.functional.Func2<A, B, A> f, final A initialValue, final Iterable<B> input)
    {
        A state = initialValue;
        for (final B a : input)
            state = f.apply(state, a);
        return state;
    }

    public final static <T,K,V>Map<K,V> toDictionary(final Func<T,K> keyFn, final Func<T,V> valueFn, Iterable<T> input) throws Exception
    {
        if(keyFn==null) throw new Exception("keyFn");
        if(valueFn==null) throw new Exception("valueFn");

        final Map<K,V> output = new HashMap<K,V>();
        for(final T element : input) output.put(keyFn.apply(element),valueFn.apply(element));
        return new UnmodifiableMap<K, V>(output);
    }

    //public final static <T>T[] toArray(final Iterable<T> input)
    public final static <T>Object[] toArray(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toArray(Iterable<T>): input is null");

        final List<T> output = new ArrayList<T>();
        for(final T element: input) output.add(element);

        return output.toArray(); // this needs to be output.toArray(new T[0]) but that doesn't appear to be allowable Java :-(
    }

    public static final <T>List<T> toList(Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toList(Iterable<T>): input is null");

        if(input instanceof List<?>) return (List<T>)input;

        final List<T> output = new ArrayList<T>();
        for(final T element: input) output.add(element);

        return output;
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

    public static final <T>List<T> concat(final List<T> list1, final List<T> list2)
    {
        if(list1==null) throw new IllegalArgumentException("Functional.concat(List<T>,List<T>): list1 is null");
        if(list2==null) throw new IllegalArgumentException("Functional.concat(List<T>,List<T>): list2 is null");

        if(list1.size()==0) return new UnmodifiableList<T>(list2);
        if(list2.size()==0) return new UnmodifiableList<T>(list1);

        final List<T> newList = new ArrayList<T>(list1);
        final boolean didItChange = newList.addAll(list2);
        return new UnmodifiableList<T>(newList);
    }

    public static final<T>List<T> take(final int howMany, final Iterable<T> list)
    {
        if(howMany<0) throw new IllegalArgumentException("Functional.take(int,Iterable<T>): howMany is negative");
        if(list==null) throw new IllegalArgumentException("Functional.take(int,Iterable<T>): list is null");

        if(howMany==0) return new ArrayList<T>(0);

        final List<T> output = new ArrayList<T>(howMany);
        final Iterator<T> iterator = list.iterator();
        for(int i=0;i<howMany;++i)
        {
            if(iterator.hasNext())
                output.add(iterator.next());
            else
                throw new NoSuchElementException("Cannot take "+howMany+" elements from input list with fewer elements");
        }
        return new UnmodifiableList(output);
    }

    public static final <T>Func<Integer,T> Constant(final T constant)
    {
        return new Func<Integer, T>() {
            @Override
            public T apply(Integer integer) {
                return constant;
            }
        };
    }

    public static final <A,B>List<org.javatuples.Pair<A,B>> zip(final Collection<A> l1, final Collection<B> l2)
    {
        if(l1==null) throw new IllegalArgumentException("Functional.zip(Collection<A>,Collection<B>): l1 is null");
        if(l2==null) throw new IllegalArgumentException("Functional.zip(Collection<A>,Collection<B>): l2 is null");

        if(l1.size()!=l2.size()) throw new IllegalArgumentException("Functional.zip(Collection<A>,Collection<B>): l1 and l2 have differing numbers of elements");

        final List<org.javatuples.Pair<A,B>> output = new ArrayList<org.javatuples.Pair<A, B>>(l1.size());
        final Iterator<A> l1_it = l1.iterator();
        final Iterator<B> l2_it = l2.iterator();

        while(l1_it.hasNext() && l2_it.hasNext()) output.add(new org.javatuples.Pair(l1_it.next(),l2_it.next()));

        return new UnmodifiableList(output);
    }

    public static final <A,B,C>List<Triplet<A,B,C>> zip3(final Collection<A> l1, final Collection<B> l2, final Collection<C> l3)
    {
        if(l1==null) throw new IllegalArgumentException("Functional.zip3(Collection<A>,Collection<B>,Collection<C>): l1 is null");
        if(l2==null) throw new IllegalArgumentException("Functional.zip3(Collection<A>,Collection<B>,Collection<C>): l2 is null");
        if(l3==null) throw new IllegalArgumentException("Functional.zip3(Collection<A>,Collection<B>,Collection<C>): l3 is null");

        if(l1.size()!=l2.size() || l1.size()!=l3.size())
            throw new IllegalArgumentException("Functional.zip3(Collection<A>,Collection<B>,Collection<C>): l1, l2 and l3 have differing numbers of elements");

        final List<org.javatuples.Triplet<A,B,C>> output = new ArrayList<org.javatuples.Triplet<A, B,C>>(l1.size());
        final Iterator<A> l1_it = l1.iterator();
        final Iterator<B> l2_it = l2.iterator();
        final Iterator<C> l3_it = l3.iterator();

        while(l1_it.hasNext() && l2_it.hasNext() && l3_it.hasNext()) output.add(new org.javatuples.Triplet(l1_it.next(),l2_it.next(),l3_it.next()));

        return new UnmodifiableList(output);
    }

    public static final <A,B>org.javatuples.Pair<List<A>,List<B>> unzip(final Collection<org.javatuples.Pair<A,B>> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.unzip(Collection<Pair<A,B>>): input is null");

        final List<A> l1 = new ArrayList<A>();
        final List<B> l2 = new ArrayList<B>();

        for(org.javatuples.Pair<A,B> pair:input)
        {
            l1.add(pair.getValue0());
            l2.add(pair.getValue1());
        }

        return new org.javatuples.Pair(new UnmodifiableList(l1),new UnmodifiableList(l2));
    }

    public static final <T>List<T> collect(final Func<T,Iterable<T>> f, final Iterable<T> input)
    {
        List<T> output = new ArrayList<T>();
        for(final T element : input)
            output = Functional.concat(output, Functional.toList(f.apply(element)));
        return new UnmodifiableList<T>(output);
    }

    public static final class seq
    {
        public static final <T,U>Iterable<U> map(final Func<T,U> f, final Iterable<T> input) throws Exception
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<U>() {
                @Override
                public final Iterator<U> iterator() {
                    return new Iterator<U>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func<T,U> _f = f;
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

        public static final <T>Iterable<T> concat(final Iterable<T> list1, final Iterable<T> list2)
        {
            if(list1==null) throw new IllegalArgumentException("Functional.seq.concat(Iterable<T>,Iterable<T>): list1 is null");
            if(list2==null) throw new IllegalArgumentException("Functional.seq.concat(Iterable<T>,Iterable<T>): list2 is null");

            return new Iterable<T>()
            {
                public Iterator<T> iterator()
                {
                    return new Iterator<T>() {
                        private final Iterator<T> _s1 = list1.iterator();
                        private final Iterator<T> _s2 = list2.iterator();
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

        public static final <T>Iterable<T> filter(final Func<T,Boolean> f, final Iterable<T> input) throws NoSuchElementException, IllegalArgumentException, UnsupportedOperationException
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<T>() {
                @Override
                public final Iterator<T> iterator() {
                    return new Iterator<T>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func<T,Boolean> _f = f;
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
                            throw new NoSuchElementException();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.filter(Func<T,Boolean>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        public static final <T,U>Iterable<U> choose(final Func<T,Option<U>> f, final Iterable<T> input) throws Exception
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<U>() {
                @Override
                public final Iterator<U> iterator() {
                    return new Iterator<U>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func<T,Option<U>> _f = f;
                        private Option<U> _next = Option.<U>None();
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
                                _next=Option.<U>None();
                                try {
                                    return next.Some();
                                } catch(OptionNoValueAccessException e) { throw new NoSuchElementException(); }
                            }
                            throw new NoSuchElementException();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.choose(Func<T,U>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        /// <summary> init: int -> (int -> A) -> A list</summary>
        public final static <T>Iterable<T> init(final Func<Integer,T> f,final int howMany)
        {
            if(f==null) throw new IllegalArgumentException("f");
            if(howMany<0) throw new IllegalArgumentException("howMany");

            return new Iterable<T>()
            {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>()
                    {
                        private int _counter=0;
                        private final Func<Integer,T> _f = f;
                        @Override
                        public boolean hasNext() {
                            return _counter<howMany;
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
        public final static <T>Iterable<T> init(final Func<Integer,T> f)
        {
            if(f==null) throw new IllegalArgumentException("f");

            return new Iterable<T>()
            {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>()
                    {
                        private int _counter=0;
                        private final Func<Integer,T> _f = f;
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

        public static final <T>Iterable<T> collect(final Func<T,Iterable<T>> f, final Iterable<T> input)
        {
            if(f==null) throw new IllegalArgumentException("Functional.seq.collect: f is null");
            if(input==null) throw new IllegalArgumentException("Functional.seq.collect: input is null");

            return new Iterable<T>(){

                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>(){
                        private final Iterator<T> it = input.iterator();
                        private List<T> cache = new ArrayList<T>();
                        private Iterator<T> cacheIterator = cache.iterator();
                        @Override
                        public boolean hasNext() {
                            return it.hasNext() || cacheIterator.hasNext();
                        }

                        @Override
                        public T next() {
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
    }

    public static final <T>Func<Iterable<T>,List<T>> filter(final Func<T,Boolean> f)
    {
        return new Func<Iterable<T>, List<T>>() {
            @Override
            public List<T> apply(final Iterable<T> input) {
                return Functional.filter(f,input);
            }
        };
    }

        /*
        // Following are functions for non-list collections
        */

    public static final <A, B, C>Map<B, C> map_dict(Func<A,Map.Entry<B,C>> f, Iterable<A> input)
    {
        final Map<B, C> results = new HashMap<B, C>();
        for (final A a : input)
        {
            final Map.Entry<B, C> intermediate = f.apply(a);
            results.put(intermediate.getKey(), intermediate.getValue());
        }
        return results;
    }
}