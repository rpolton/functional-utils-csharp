package me.shaftesbury.utils.functional;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.*;

/**
 * Herein are contained some standard algorithms from functional programming.
 * See <a href="http://en.wikipedia.org/wiki/Functional_programming">Functional Programming</a>
 * for more information
 */
public final class Functional
{
    private Functional() {}

    /**
     * A simple predicate which checks the contents of the string parameter.
     * @param s the input string
     * @return true if s is either null or s is the empty string; false otherwise.
     */
    public final static boolean isNullOrEmpty(final String s)
    {
        return s==null || s.isEmpty();
    }

    /**
     * Concatenate all of the input elements into a single string where each element is separated from the next by the supplied delimiter
     * @param delimiter used to separate consecutive elements in the output
     * @param strs input sequence, each element of which must be convertible to a string
     * @param <T> the type of the element in the input sequence
     * @return a string containing the string representation of each input element separated by the supplied delimiter
     */
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

    /**
     * A string function: generate a string that contains the 'unitOfIndentation' repeated 'howMany' times prepended to 'indentThis'
     * @param howMany times should the unitOfIndentation be prefixed to the supplied 'indentThis' string
     * @param unitOfIndentation the indentation
     * @param indentThis the input string that should be indented
     * @return a string indenting the input string by the indicated number of units
     */
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

    /**
     * foldAndChoose: <tt>fold</tt> except that instead of folding every element in the input sequence, <tt>fold</tt>
     * only those for which the fold function 'f' returns a Some value (see <tt>Option</tt>)
     * @param f is the fold function modified such that the return value contains an Option in addition to the state
     * @param initialValue the seed for the fold function
     * @param input the input sequence
     * @param <A> the type of the initialValue / seed
     * @param <B> the type of the element in the input sequence
     * @return the folded value paired with those transformed elements which are Some
     * @throws OptionNoValueAccessException
     */
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

    /**
     * Analogue of string.Join for List<T> with the addition of a user-defined map function
     *
     * @param <T> the type of the element in the input sequence
     * @param separator inserted between each transformed element
     * @param l the input sequence
     * @param fn map function (see <tt>map</tt>) which is used to transform the input sequence
     * @return a string containing the transformed string value of each input element separated by the supplied separator
     */
    public final static <T>String join(final String separator, final Iterable<T> l, final Func<? super T, String> fn)
    {
        if (l == null) throw new IllegalArgumentException("l");
        if (fn == null) throw new IllegalArgumentException("fn");

        return join(separator, map(fn, l));
    }

    /**
     * @param lowerBound
     * @param upperBound
     * @param val
     * @param <T> the type of the input element
     * @return lowerBound < val < upperBound
     */
    public final static <T extends Comparable<T>>boolean between(final T lowerBound, final T upperBound, final T val)
    {
        if (val == null) throw new IllegalArgumentException("val");

        return val.compareTo(lowerBound) == 1 && val.compareTo(upperBound) == -1;
    }

    /**
     * Find the first element from the input sequence for which the supplied predicate returns true
     * find: (A -> bool) -> A list -> A
     * @param f predicate
     * @param input sequence
     * @param <A> the type of the element in the input sequence
     * @throws java.lang.IllegalArgumentException if f or input are null
     * @throws java.util.NoSuchElementException if no element is found that satisfies the predicate
     * @return the first element from the input sequence for which the supplied predicate returns true
     */
    public final static <A>A find(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        for(final A a : input)
            if(f.apply((a)))
                return a;
        throw new NoSuchElementException();
    }

    /**
     * Curried find.
     * Find the first element from the input sequence for which the supplied predicate returns true
     * find: (A -> bool) -> A list -> A
     * @param f predicate
     * @param <A> the type of the element in the input sequence
     * @throws java.lang.IllegalArgumentException if f or input are null
     * @throws java.util.NoSuchElementException if no element is found that satisfies the predicate
     * @return a curried function that expects an input sequence which it feeds to the predicate f
     *          which returns the first element from the input sequence for which the supplied predicate returns true
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A>Func<Iterable<A>,A> find(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, A>() {
            @Override
            public A apply(final Iterable<A> input) {
                return Functional.find(f,input);
            }
        };
    }

    /**
     * As <tt>find</tt> except that here we return the zero-based position in the input sequence of the found element
     * findIndex: (A -> bool) -> A list -> int
     * @param f predicate
     * @param input sequence
     * @param <A> the type of the element in the input sequence
     * @throws java.lang.IllegalArgumentException if f or input are null
     * @throws java.util.NoSuchElementException if no element is found that satisfies the predicate
     * @return the position in the input sequence of the first element from the input sequence for which the supplied predicate
     * returns true
     */
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

    /**
     * As <tt>find</tt> except that here we return the last element in the input sequence that satisfies the predicate 'f'
     * findLast: (A -> bool) -> A seq -> A
     * @param f predicate
     * @param input sequence
     * @param <A> the type of the element in the input sequence
     * @throws java.lang.IllegalArgumentException if f or input are null
     * @throws java.util.NoSuchElementException if no element is found that satisfies the predicate
     * @return the last element in the input sequence for which the supplied predicate returns true
     */
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

    /**
     * As <tt>find</tt> except that here we return the last element in the input sequence that satisfies the predicate 'f'
     * findLast: (A -> bool) -> A list -> A
     * @param f predicate
     * @param input sequence
     * @param <A> the type of the element in the input sequence
     * @throws java.lang.IllegalArgumentException if f or input are null
     * @throws java.util.NoSuchElementException if no element is found that satisfies the predicate
     * @return the last element in the input sequence for which the supplied predicate returns true
     */
    public final static <A>A findLast(final Func<? super A,Boolean> f, final List<A> input)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if (input == null) throw new IllegalArgumentException("input");

        for (final A a : Iterators.reverse(input))
            if (f.apply(a))
                return a;
        throw new NoSuchElementException();
    }

    /**
     * A curried version of findLast.
     * As <tt>find</tt> except that here we return the last element in the input sequence that satisfies the predicate 'f'
     * findLast: (A -> bool) -> A list -> A
     * @param f predicate
     * @param <A> the type of the element in the input sequence
     * @throws java.lang.IllegalArgumentException if f or input are null
     * @throws java.util.NoSuchElementException if no element is found that satisfies the predicate
     * @return the last element in the input sequence for which the supplied predicate returns true
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A>Func<List<A>,A> findLast(final Func<A,Boolean> f)
    {
        return new Func<List<A>, A>() {
            @Override
            public A apply(final List<A> input) {
                return Functional.findLast(f,input);
            }
        };
    }

    /**
     * 'pick' is an analogue of <tt>find</tt>. Instead of a predicate, 'pick' is passed a map function which returns an <tt>Option</tt>.
     * Each element of the input sequence is supplied in turn to the map function 'f' and the first non-None Option to be returned from
     * the map function is returned by 'pick' to the calling code.
     * pick: (A -> B option) -> A seq -> B
     *
     * @param f the map function.
     * @param input the input sequence
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the output element
     * @return the first non-None transformed element of the input sequence
     */
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

    /**
     * 'pick' is an analogue of <tt>find</tt>. Instead of a predicate, 'pick' is passed a map function which returns an <tt>Option</tt>.
     * Each element of the input sequence is supplied in turn to the map function 'f' and the first non-None Option to be returned from
     * the map function is returned by 'pick' to the calling code.
     *
     * This is a curried implementation of 'pick'
     *
     * pick: (A -> B option) -> A seq -> B
     *
     * @param f the map function.
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the output element
     * @return the first non-None transformed element of the input sequence
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static <A,B>Func<Iterable<A>,B> pick(final Func<? super A,Option<B>> f)
    {
        return new Func<Iterable<A>, B>() {
            @Override
            public B apply(final Iterable<A> input) {
                return Functional.pick(f,input);
            }
        };
    }

    /**
     * In, used for functional composition. This is the simple reversal function. y(x) is equivalent to x.In(y)
     * See <a href="http://en.wikipedia.org/wiki/Function_composition_(computer_science)">Function Composition</a>
     * @param input the object which we wish to pass to the function parameter
     * @param f the function we wish to evaluate
     * @param <A> the base type of the input element. That is <tt>AA extends A</tt>
     * @param <B> the type of the output of the function <tt>f</tt>
     * @param <AA> the type of the input
     * @return f(input)
     */
    public final static <A, B, AA extends A> B in(final AA input, final Func<A, B> f)
    {
        return f.apply(input);
    }

    /**
     * Then, the functional composition operator. Execute the first function then execute the second, passing the results
     * of the first as the input to the second.
     * See <a href="http://en.wikipedia.org/wiki/Function_composition_(computer_science)">Function Composition</a>
     * @param f the first function to execute.
     * @param g the second function to execute. The input to this function will be the result of the first function, f
     * @param <A> the type of the input to <tt>f</tt>
     * @param <B> the type of the input to <tt>g</tt> and a base class of the output of <tt>f</tt>
     * @param <C> the type of the output of <tt>g</tt>
     * @return a function equivalent to g(f(x))
     */
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

    /**
     * Convolution of functions. That is, apply two transformation functions 'simultaneously' and return a list of pairs,
     * each of which contains one part of the results.
     * @param f the transformation function that generates the first value in the resultant pair
     * @param g the transformation function that generates the second value in the resultant pair
     * @param input the input list to be transformed
     * @param <A> a type that all the elements in the input list extend and that both of the transformation functions accept as input
     * @param <B> the resulting type of the first transformation
     * @param <C> the resulting type of the second transformation
     * @return a list of pairs containing the two transformed sequences
     */
    public static <A,B,C>List<Pair<B,C>> zip(final Func<? super A,B> f, final Func<? super A,C> g, final Collection<? extends A> input)
    {
        final List<Pair<B,C>> output = new ArrayList<Pair<B,C>>(input.size());

        for(final A element : input)
            output.add(Pair.with(f.apply(element), g.apply(element)));

        return output;
    }

    /**
     * The identity transformation function: that is, the datum supplied as input is returned as output
     * @param <T> the type of the input element
     * @return a function which is the identity transformation
     */
    public final static <T>Func<T,T> identity()
    {
        return new Func<T, T>() {
            @Override
            public T apply(final T t) {
                return t;
            }
        };
    }

    /**
     * <tt>isEven</tt> a function that accepts an integer and returns a boolean that indicates whether the passed integer
     * is or is not an even integer
     */
    public static final Func<Integer,Boolean> isEven = new Func<Integer, Boolean>()
    {
        @Override
        public Boolean apply(final Integer i)
        {
            return i % 2 == 0;
        }
    };
    /**
     * <tt>isOdd</tt> a function that accepts an integer and returns a boolean that indicates whether the passed integer
     * is or is not an odd integer
     */
    public static final Func<Integer,Boolean> isOdd = new Func<Integer, Boolean>()
    {
        @Override
        public Boolean apply(final Integer i)
        {
            return i % 2 != 0;
        }
    };
    /**
     * <tt>count</tt> a function that accepts a counter and another integer and returns 1 + counter
     */
    public static final Func2<Integer,Integer,Integer> count = new Func2<Integer, Integer, Integer>() {
                @Override
                public Integer apply(final Integer state, final Integer b) {
                    return state + 1;
                }
            };
    /**
     * <tt>sum</tt> a function that accepts two integers and returns the sum of them
     */
    public static final Func2<Integer,Integer,Integer> sum = new Func2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(final Integer state, final Integer b) {
            return state + b;
        }
    };

    /**
     * @param <T> the type of <tt>that</tt>, the input argument
     * @return a function that compares its supplied argument with the 'that' argument and returns true if 'this' is greater than
     * 'that' or false otherwise
     */
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

    /**
     * @param <T> the type of <tt>that</tt>, the input argument
     * @return a function that compares its supplied argument with the 'that' argument and returns true if 'this' is greater than
     * or equal to 'that' or false otherwise
     */
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

    /**
     * @param <T> the type of <tt>that</tt>, the input argument
     * @return a function that compares its supplied argument with the 'that' argument and returns true if 'this' is less than
     * 'that' or false otherwise
     */
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

    /**
     * @param <T> the type of <tt>that</tt>, the input argument
     * @return a function that compares its supplied argument with the 'that' argument and returns true if 'this' is less than
     * or equal to 'that' or false otherwise
     */
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

    /**
     * The init function, not dissimilar to list comprehensions, which is used to return a new finite list whose contents are
     * determined by successive calls to the function f.
     * init: (int -> A) -> int -> A list
     * @param f generator function used to produce the individual elements of the output list. This function is called by init
     *          with the unity-based position of the current element in the output list being produced. Therefore, the first time
     *          f is called it will receive a literal '1' as its argument; the second time '2'; etc.
     * @param howMany the number of elements in the output list
     * @param <T> the type of the element in the output sequence
     * @return a list of 'howMany' elements of type 'T' which were generated by the function 'f'
     */
    public final static <T>List<T> init(final Func<Integer,T> f,final int howMany)
    {
        if (f == null) throw new IllegalArgumentException("f");
        if(howMany<1) throw new IllegalArgumentException("howMany");

        final List<T> output = new ArrayList<T>(howMany);
        for(int i=1; i<=howMany; ++i)
            output.add(f.apply(i));
        return Collections.unmodifiableList(output);
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
     * map: (A -> B) -> A list -> B list
     * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
     * @param input a sequence to be fed into f
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the element in the output sequence
     * @return a list of type B containing the transformed values.
     */
    public final static <A,B> List<B> map(final Func<A, ? extends B> f, final Iterable<? extends A> input)
    {
        final List<B> output = input instanceof Collection<?> ? new ArrayList<B>(((Collection) input).size()) : new ArrayList<B>();
        for(final A a : input)
            output.add(f.apply(a));
        return Collections.unmodifiableList(output);
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
     * map: (A -> B) -> A list -> B list
     * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the element in the output sequence
     * @return a curried function that expects an input sequence which it feeds to the transformation f which returns a list of type B
     *          containing the transformed values.
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A,B> Func<Iterable<A>,List<B>> map(final Func<? super A, ? extends B> f)
    {
        return new Func<Iterable<A>, List<B>>() {
            @Override
            public List<B> apply(final Iterable<A> input) {
                return Functional.map(f,input);
            }
        };
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
     * mapi: (int -> A -> B) -> A list -> B list
     * @param f a transformation function which is passed each input object of type A along with its position in the input sequence
     *          (starting from zero) and returns an object, presumably related, of type B
     * @param input a sequence to be fed into f
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the element in the output sequence
     * @return a list of type B containing the transformed values.
     */
    public final static <A,B> List<B> mapi(final Func2<Integer, A, ? extends B> f, final Iterable<? extends A> input)
    {
        final List<B> output = input instanceof Collection<?> ? new ArrayList<B>(((Collection) input).size()) : new ArrayList<B>();
        int pos = 0;
        for(final A a : input)
            output.add(f.apply(pos++, a));
        return Collections.unmodifiableList(output);
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
     * mapi: (int -> A -> B) -> A list -> B list
     * @param f a transformation function which is passed each input object of type A along with its position in the input sequence
     *          (starting from zero) and returns an object, presumably related, of type B
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the element in the output sequence
     * @return a curried function that expects an input sequence which it feeds to the transformation f which returns a list of type B
     *          containing the transformed values.
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
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

    /**
     * sortWith: a wrapper for <tt>Collections.sort</tt> which preserves the input sequence.
     * @param f the <tt>Comparator</tt> to use for the sort
     * @param input the input
     * @param <A> the type of the <tt>Comparator</tt>
     * @param <AA> the type of the element in the input sequence
     * @return a sorted list containing all the elements of 'input' sorted using <tt>Collections.sort</tt> and 'f'
     */
    public final static <A, AA extends A>List<AA> sortWith(final Comparator<A> f, final Collection<AA> input)
    {
        final List<AA> output = new ArrayList<AA>(input);
        Collections.sort(output, f);
        return Collections.unmodifiableList(output);
    }

    /**
     * A simple function which wraps left.compareTo(right) so that this can be used as a sort function.
     * @param left input element
     * @param right input element
     * @param <A> the type of the elements to be compared
     * @return left.compareTo(right)
     */
    public final static <A extends Comparable<A>>int Sorter(final A left, final A right)
    {
        return left.compareTo(right);
    }

    /**
     * A Comparator that encapsulates <tt>Sorter</tt> above
     */
    public final static Comparator<Integer> dSorter = new Comparator<Integer>()
    {
        @Override public int compare(final Integer i, final Integer j) { return Sorter(i, j); }
    };

    /**
     * A wrapper around <tt>toString()</tt>
     * @param a the element to be turned into a string using T.toString()
     * @param <T> the type of element 'a'
     * @return a.toString()
     */
    public final static <T> String Stringify(final T a) { return a.toString(); }

    /**
     * A transformation function that wraps <tt>Stringify</tt>
     * @param <T> the type of the element which we will render as a String
     * @return a function that calls <tt>Stringify</tt>
     */
    public final static <T>Func<T, String> dStringify()
    {
        return new Func<T, String>()
        {
            @Override public String apply(final T i) { return Stringify(i); }
        };
    }

    /**
     * forAll2: the predicate 'f' is applied to all elements in the input sequences input1 and input2 as pairs. If the predicate returns
     * true for all pairs and there is the same number of elements in both input sequences then forAll2 returns true. If the predicate
     * returns false at any point then the traversal of the input sequences halts and forAll2 returns false.
     * forAll2: (A -> B -> bool) -> A list -> B list -> bool
     * @param f predicate to which each successive pair (input1_i, input2_i) is applied
     * @param input1 input sequence
     * @param input2 input sequence
     * @param <A> the base type of the element in the first input sequence
     * @param <B> the base type of the element in the second input sequence
     * @param <AA> the type of the element in the first input sequence
     * @param <BB> the type of the element in the second input sequence
     * @return true if the predicate 'f' evaluates true for all pairs, false otherwise
     * @throws java.lang.IllegalArgumentException if the predicate returns true for all pairs and the sequences contain differing numbers
     * of elements
     */
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

    /**
     * See <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</a>
     * @param pred a filter function. This is passed each input element in turn and returns either true or false. If true then
     *             the input element is passed through to the output otherwise it is ignored.
     * @param input a sequence of objects
     * @param <A> the type of the element in the input sequence
     * @return a list which contains zero or more of the elements of the input sequence. Each element is included only if the filter
     *          function returns true for the element.
     */
    public final static <A>List<A> filter(final Func<? super A,Boolean> pred, final Iterable<A> input)
    {
        final List<A> output = input instanceof Collection<?> ? new ArrayList<A>(((Collection) input).size()) : new ArrayList<A>();
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return Collections.unmodifiableList(output);
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</a>
     * @param f a filter function. This is passed each input element in turn and returns either true or false. If true then
     *             the input element is passed through to the output otherwise it is ignored.
     * @param <T> the type of the element in the input sequence
     * @return a curried function that expects an input sequence which it feeds to the filter predicate which then returns
     *          a list which contains zero or more of the elements of the input sequence. Each element is included only if the filter
     *          function returns true for the element.
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static final <T>Func<Iterable<T>,List<T>> filter(final Func<? super T,Boolean> f)
    {
        return new Func<Iterable<T>, List<T>>() {
            @Override
            public List<T> apply(final Iterable<T> input) {
                return Functional.filter(f,input);
            }
        };
    }

    /**
     * The converse operation to <tt>forAll</tt>. If the predicate returns true then 'exists' returns true and halts the traveral of the
     * input sequence. Otherwise return false.
     * exists: (A -> bool) -> A list -> bool
     * @param f predicate
     * @param input input sequence
     * @param <A> the type of the element in the input sequence
     * @return true if the predicate returns true for any element in the input sequence, false otherwise
     */
    public final static <A>boolean exists(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        for(final A a : input)
            if(f.apply(a))
                return true;
        return false;
    }

    /**
     * The converse operation to <tt>forAll</tt>. If the predicate returns true then 'exists' returns true and halts the traveral of the
     * input sequence. Otherwise return false.
     * exists: (A -> bool) -> A list -> bool
     * This is the curried implementation.
     * @param f predicate
     * @param <A> the type of the element in the input sequence
     * @return true if the predicate returns true for any element in the input sequence, false otherwise
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A>Func<Iterable<A>,Boolean> exists(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, Boolean>() {
            @Override
            public Boolean apply(final Iterable<A> input) {
                return Functional.exists(f,input);
            }
        };
    }

    /**
     * not reverses the result of the applied predicate
     * not: (A -> bool) -> (A -> bool)
     * @param f the applied predicate
     * @param <A> the type of the input to the function <tt>f</tt>
     * @return true if f returns false, false if f returns true
     */
    public final static <A>Func<A,Boolean> not(final Func<A,Boolean> f)
    {
        return new Func<A,Boolean>(){@Override public Boolean apply(final A a) { return !f.apply(a);}};
    }

    /**
     * The converse operation to <tt>exists</tt>. If the predicate returns true for all elements in the input sequence then 'forAll'
     * returns true otherwise return false.
     * forAll: (A -> bool) -> A list -> bool
     * @param f predicate
     * @param input input sequence
     * @param <A> the type of the element in the input sequence
     * @return true if the predicate returns true for all elements in the input sequence, false otherwise
     */
    public final static <A>boolean forAll(final Func<A,Boolean> f, final Iterable<? extends A> input)
    {
        return !exists(not(f), input);
    }

    /**
     * The converse operation to <tt>exists</tt>. If the predicate returns true for all elements in the input sequence then 'forAll'
     * returns true otherwise return false.
     * forAll: (A -> bool) -> A list -> bool
     * This is a curried implementation of 'forAll
     * @param f predicate
     * @param <A> the type of the element in the input sequence
     * @return true if the predicate returns true for all elements in the input sequence, false otherwise
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A>Func<Iterable<A>,Boolean> forAll(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, Boolean>() {
            @Override
            public Boolean apply(final Iterable<A> input) {
                return Functional.forAll(f,input);
            }
        };
    }

    /**
     * not2 reverses the result of the applied predicate
     * not2: (A -> B -> bool) -> (A -> B -> bool)
     * @param f the applied predicate
     * @param <A> the type of the first input to the function <tt>f</tt>
     * @param <B> the type of the second input to the function <tt>f</tt>
     * @return true if f returns false, false if f returns true
     */
    public final static <A,B> Func2<A,B,Boolean> not2(final Func2<A,B,Boolean> f)
    {
        return new Func2<A,B,Boolean>(){@Override public Boolean apply(final A a, final B b) { return !f.apply(a,b);}};
    }

    /// <summary> </summary>
    /// <returns> (list * list). The first list contains all items for which f(a) is true. The second list contains the remainder.</returns>

    /**
     * partition is a group function. Given a predicate and an input sequence, 'partition' returns a pair of lists, the first list
     * containing those elements from the input sequence for which the predicate returned true, the second list containing those
     * elements from the input sequence for which the predicate returned false.
     * partition: (A -> bool) -> A list -> A list * A list
     * @param f predicate used to split the input sequence into two groups
     * @param input the input sequence
     * @param <A> the type of the element in the input sequence
     * @return a pair of lists, the first being the 'true' and the second being the 'false'
     */
    public final static <A>Pair<List<A>,List<A>> partition(final Func<? super A,Boolean> f, final Iterable<A> input)
    {
        final List<A> left;
        final List<A> right;
        if(input instanceof Collection<?>)
        {
            left = new ArrayList<A>(((Collection) input).size());
            right = new ArrayList<A>(((Collection) input).size());
        }
        else
        {
            left = new ArrayList<A>();
            right = new ArrayList<A>();
        }
        for (final A a : input)
            if (f.apply(a))
                left.add(a);
            else
                right.add(a);
        return new Pair<List<A>,List<A>>(Collections.unmodifiableList(left), Collections.unmodifiableList(right));
    }

    /**
     * partition is a group function. Given a predicate and an input sequence, 'partition' returns a pair of lists, the first list
     * containing those elements from the input sequence for which the predicate returned true, the second list containing those
     * elements from the input sequence for which the predicate returned false.
     * partition: (A -> bool) -> A list -> A list * A list
     * This is a curried implementation of 'forAll
     * @param f predicate used to split the input sequence into two groups
     * @param <A> the type of the element in the input sequence
     * @return a pair of lists, the first being the 'true' and the second being the 'false'
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A>Func<Iterable<A>,Pair<List<A>,List<A>>> partition(final Func<? super A,Boolean> f)
    {
        return new Func<Iterable<A>, Pair<List<A>, List<A>>>() {
            @Override
            public Pair<List<A>, List<A>> apply(final Iterable<A> input) {
                return Functional.partition(f,input);
            }
        };
    }

    /**
     * choose: this is a map transformation with the difference being that the number of elements in the output sequence may
     * be between zero and the number of elements in the input sequence.
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * choose: (A -> B option) -> A list -> B list
     * @param f map function. This transforms the input element into an Option
     * @param input input sequence
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the element in the output sequence
     * @return a list of transformed elements, numbering less than or equal to the number of input elements
     */
    public final static <A, B>List<B> choose(final Func<? super A, Option<B>> f, final Iterable<A> input)
    {
        final List<B> results = input instanceof Collection<?> ? new ArrayList<B>(((Collection) input).size()) : new ArrayList<B>();
        for(final A a : input)
        {
            final Option<B> intermediate = f.apply(a);
            if (!intermediate.isNone())
                results.add(intermediate.Some());
        }
        return Collections.unmodifiableList(results);
    }

    /**
     * choose: this is a curried implementation of choose.
     * choose is a map transformation with the difference being that the number of elements in the output sequence may
     * be between zero and the number of elements in the input sequence.
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * choose: (A -> B option) -> A list -> B list
     * @param f map function. This transforms the input element into an Option
     * @param <A> the type of the element in the input sequence
     * @param <B> the type of the element in the output sequence
     * @return a list of transformed elements, numbering less than or equal to the number of input elements
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A, B>Func<Iterable<A>,List<B>> choose(final Func<? super A, Option<B>> f)
    {
        return new Func<Iterable<A>, List<B>>() {
            @Override
            public List<B> apply(final Iterable<A> input) {
                return Functional.choose(f, input);
            }
        };
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
     * fold: aggregate the elements of the input sequence given a seed and an aggregation function.
     * fold: (A -> B -> A) -> A -> B list -> A
     * @param f aggregation function
     * @param initialValue seed for the algorithm
     * @param input input sequence
     * @param <A> the type of the initialValue / seed
     * @param <B> the type of the element in the input sequence
     * @return aggregated value
     */
    public final static <A, B>A fold(final Func2<? super A, ? super B, ? extends A> f, final A initialValue, final Iterable<B> input)
    {
        A state = initialValue;
        for (final B b : input)
            state = f.apply(state, b);
        return state;
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
     * fold: aggregate the elements of the input sequence given a seed and an aggregation function.
     * This is the curried implementation
     * fold: (A -> B -> A) -> A -> B list -> A
     * @param f aggregation function
     * @param initialValue seed for the algorithm
     * @param <A> the type of the initialValue / seed
     * @param <B> the type of the element in the output sequence
     * @return aggregated value
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public final static <A, B>Func<Iterable<B>,A> fold(final Func2<? super A, ? super B, ? extends A> f, final A initialValue)
    {
        return new Func<Iterable<B>, A>() {
            @Override
            public A apply(final Iterable<B> input) {
                return Functional.fold(f, initialValue, input);
            }
        };
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Unfold_(higher-order_function)">Unfold</a> and
     * <a href="http://en.wikipedia.org/wiki/Anamorphism">Anamorphism</a>
     * This is the converse of <tt>fold</tt>
     * unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
     */
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

    /**
     * See <a href="http://en.wikipedia.org/wiki/Unfold_(higher-order_function)">Unfold</a>
     * and <a href="http://en.wikipedia.org/wiki/Anamorphism">Anamorphism</a>
     * This is the converse of <tt>fold</tt>
     * unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
     */
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

    /**
     * toDictionary: given each element from the input sequence apply the keyFn and valueFn to generate a (key,value) pair.
     * The resulting dictionary (java.util.Map) contains all these pairs.
     * @param keyFn function used to generate the key
     * @param valueFn function used to generate the value
     * @param input input sequence
     * @param <T> the type of the element in the input sequence
     * @param <K> the type of the key elements
     * @param <V> the type of the value elements
     * @return a java.util.Map containing the transformed input sequence
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     */
    public final static <T,K,V>Map<K,V> toDictionary(final Func<? super T,? extends K> keyFn, final Func<? super T,? extends V> valueFn, final Iterable<T> input)
    {
        if(keyFn==null) throw new IllegalArgumentException("keyFn");
        if(valueFn==null) throw new IllegalArgumentException("valueFn");

        final Map<K,V> output = new HashMap<K,V>();
        for(final T element : input) output.put(keyFn.apply(element), valueFn.apply(element));
        return Collections.unmodifiableMap(output);
    }

    /**
     * toArray: create an array containing all the objects in the input sequence
     * @param input input sequence
     * @param <T> the type of the element in the input sequence
     * @return an array containing all the elements of the input sequence
     */
    public final static <T>Object[] toArray(final Iterable<T> input)
    //public final static <T>T[] toArray(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toArray(Iterable<T>): input is null");

        if(input instanceof Collection<?>)
            return ((Collection<T>)input).toArray();

        final List<T> output = new ArrayList<T>();
        for(final T element: input) output.add(element);

        return output.toArray(); // this needs to be output.toArray(new T[0]) but that doesn't appear to be allowable Java :-(
    }

    public static final <T>List<T> toMutableList(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toMutableList(Iterable<T>): input is null");

        if(input instanceof Collection<?>)
        {
            final Collection<T> input_ = (Collection<T>)input;
            final List<T> output = new ArrayList<T>(input_.size());
            output.addAll(input_);
            return output;
        }

        final List<T> output = new ArrayList<T>();
        for(final T element: input) output.add(element);

        return output;
    }

    public static final <K,V>Map<K,V> toMutableDictionary(final Map<K,V> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toMutableDictionary(Map<K,V>): input is null");

        final Map<K,V> output = new HashMap<K,V>(input.size());
        output.putAll(input);
        return output;
    }

    public static final <T>Set<T> toMutableSet(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toMutableSet(Iterable<T>): input is null");

        if(input instanceof Collection<?>)
        {
            final Collection<T> input_ = (Collection<T>)input;
            final Set<T> output = new HashSet<T>(input_.size());
            output.addAll(input_);
            return output;
        }

        final Set<T> output = new HashSet<T>();
        for(final T element: input) output.add(element);

        return output;
    }

    /**
     * Create a java.util.List which contains all of the elements in the input sequence
     * @param input input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list containing the elements of the input sequence
     */
    public static final <T>List<T> toList(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.toList(Iterable<T>): input is null");
        return Collections.unmodifiableList(toMutableList(input));
    }

    /**
     * Create a java.util.Set which contains all of the elements in the input sequence
     * @param input input sequence
     * @param <T> the type of the element in the input sequence
     * @return a set containing the elements of the input sequence
     */
    public static final <T>Set<T> toSet(final Iterable<T> input)
    {
        //Sets.newSetFromMap();
        if(input==null) throw new IllegalArgumentException("Functional.toSet(Iterable<T>): input is null");
        return Collections.unmodifiableSet(toMutableSet(input));
    }

    /**
     * Return the final element from the input sequence
     * @param input input sequence
     * @param <T> the type of the element in the input sequence
     * @return the last element from the input sequence
     */
    public static final <T>T last(final Iterable<T> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.last(Iterable<T>): input is null");

        T state = null;
        for(final T element: input) state = element;

        return state;
    }

    /**
     * Return the final element from the input array
     * @param input input array
     * @param <T> the type of the element in the input sequence
     * @return the last element from the input array
     */
    public static final <T>T last(final T[] input)
    {
        if(input==null||input.length==0) throw new IllegalArgumentException("Functional.last(T[]): input is null or empty");

        return input[input.length-1];
    }

    /**
     * Concatenate two sequences and return a new list containing the concatenation.
     * @param list1 first input sequence
     * @param list2 second input sequence
     * @param <T> the type of the element in the input sequences
     * @return a list containing the elements of the first sequence followed by the elements of the second sequence
     */
    public static final <T>List<T> concat(final Iterable<? extends T> list1, final Iterable<? extends T> list2)
    {
        if(list1==null) throw new IllegalArgumentException("Functional.concat(List<T>,List<T>): list1 is null");
        if(list2==null) throw new IllegalArgumentException("Functional.concat(List<T>,List<T>): list2 is null");

        final List<T> newList = new ArrayList<T>(toList(list1));
        final boolean didItChange = newList.addAll(toList(list2));
        return Collections.unmodifiableList(newList);
    }

    /**
     * take: given a list return another list containing the first 'howMany' elements
     * @param howMany a positive number of elements to be returned from the input sequence
     * @param list the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list containing the first 'howMany' elements of 'list'
     * @throws java.util.NoSuchElementException if more elements are requested than are present in the input sequence
     */
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

    /**
     * take: given a list return another list containing the first 'howMany' elements
     * This is the curried implementation
     * @param howMany a positive number of elements to be returned from the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list containing the first 'howMany' elements of 'list'
     * @throws java.util.NoSuchElementException if more elements are requested than are present in the input sequence
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static final<T>Func<Iterable<? extends T>,List<T>> take(final int howMany)
    {
        return new Func<Iterable<? extends T>, List<T>>() {
            @Override
            public List<T> apply(final Iterable<? extends T> input) {
                return Functional.take(howMany,input);
            }
        };
    }

    /**
     * takeWhile: given a list return another list containing the first elements up and not including the first element for which
     * the predicate returns false
     * @param predicate the predicate to use
     * @param list the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list
     */
    public static final<T>List<T> takeWhile(final Func<? super T, Boolean> predicate, final List<T> list)
    {
        if(predicate==null) throw new IllegalArgumentException("Functional.take(Func,Iterable<T>): predicate is null");
        if(list==null) throw new IllegalArgumentException("Functional.take(Func,Iterable<T>): list is null");

        if(list.size()==0) return new ArrayList<T>();

        for(int i=0;i<list.size();++i)
        {
            final T element = list.get(i);
            if(!predicate.apply(element))
            {
                if(i==0) return new ArrayList<T>();
                return Collections.unmodifiableList(list.subList(0,i));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * takeWhile: given a list return another list containing the first elements up and not including the first element for which
     * the predicate returns false
     * This is the curried implementation
     * @param predicate the predicate to use
     * @param <T> the type of the element in the input sequence
     * @return a list
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static final<T>Func<List<T>,List<T>> takeWhile(final Func<? super T, Boolean> predicate)
    {
        return new Func<List<T>, List<T>>() {
            @Override
            public List<T> apply(final List<T> input) {
                return Functional.takeWhile(predicate, input);
            }
        };
    }

    /**
     * skip: the converse of <tt>take</tt>. Given a list return another list containing those elements that follow the
     * first 'howMany' elements. That is, if we skip(1,[1,2,3]) then we have [2,3]
     * @param howMany a non-negative number of elements to be discarded from the input sequence
     * @param list the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list containing the remaining elements after the first 'howMany' elements of 'list' or an empty list if more elements
     * are skipped than are present in the 'list'
     */
    public static final <T>List<T> skip(final int howMany, final List<? extends T> list)
    {
        if(howMany<0) throw new IllegalArgumentException("Functional.skip(int,List<T>): howMany is negative");
        if(list==null) throw new IllegalArgumentException("Functional.skip(int,List<T>): list is null");

        if(howMany==0) return Collections.unmodifiableList(list);
        final int outputListSize = list.size()-howMany;
        if(outputListSize<=0) return new ArrayList<T>();

        return Collections.unmodifiableList(list.subList(howMany,list.size()));
    }

    /**
     * skip: the converse of <tt>take</tt>. Given a list return another list containing those elements that follow the
     * first 'howMany' elements. That is, if we skip(1,[1,2,3]) then we have [2,3]
     * This is the curried implementation
     * @param howMany a non-negative number of elements to be discarded from the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list containing the remaining elements after the first 'howMany' elements of 'list' or an empty list if more elements
     * are skipped than are present in the 'list'
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static final<T>Func<List<? extends T>,List<T>> skip(final int howMany)
    {
        return new Func<List<? extends T>, List<T>>() {
            @Override
            public List<T> apply(final List<? extends T> input) {
                return Functional.skip(howMany, input);
            }
        };
    }

    /**
     * skipWhile: the converse of <tt>takeWhile</tt>. Given a list return another list containing all those elements from,
     * and including, the first element for which the predicate returns false. That is, if we skip(isOdd,[1,2,3]) then we have [2,3]
     * @param predicate ignore elements in the input while the predicate is true.
     * @param list the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a list containing the remaining elements after and including the first element for which the predicate returns false
     */
    public static final <T>List<T> skipWhile(final Func<? super T, Boolean> predicate, final List<T> list)
    {
        if(predicate==null) throw new IllegalArgumentException("Functional.skipWhile(Func,List<T>): predicate is null");
        if(list==null) throw new IllegalArgumentException("Functional.skipWhile(Func,List<T>): list is null");

        for(int counter=0; counter<list.size();++counter)
            if(!predicate.apply(list.get(counter)))
                return Collections.unmodifiableList(list.subList(counter,list.size()));

        return Collections.unmodifiableList(new ArrayList<T>(0));
    }

    /**
     * skipWhile: the converse of <tt>takeWhile</tt>. Given a list return another list containing all those elements from,
     * and including, the first element for which the predicate returns false. That is, if we skip(isOdd,[1,2,3]) then we have [2,3]
     * @param predicate ignore elements in the input while the predicate is true.
     * @param <T> the type of the element in the input sequence
     * @return a list containing the remaining elements after and including the first element for which the predicate returns false
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static final<T>Func<List<T>,List<T>> skipWhile(final Func<? super T, Boolean> predicate)
    {
        return new Func<List<T>, List<T>>() {
            @Override
            public List<T> apply(final List<T> input) {
                return Functional.skipWhile(predicate, input);
            }
        };
    }

    /**
     * constant: a function that returns a map function f(n) that returns the supplied 'constant'. Typically this would be
     * used in <tt>init</tt>
     * @param constant the desired constant value to be returned
     * @param <T> the type of the constant
     * @return a function that returns a function that returns the supplied constant
     */
    public static final <T>Func<Integer,T> constant(final T constant)
    {
        return new Func<Integer, T>() {
            @Override
            public T apply(final Integer integer) {
                return constant;
            }
        };
    }

    /**
     * range: a function that returns a map function f(n) that returns an integer from the open-ended range [startFrom+n, infinity).
     * Typically this would be used in <tt>init</tt>
     * @param startFrom the lower bound of the range
     * @return a function that returns a function that returns an integer from the range [startFrom+n, infinity)
     */
    public static final Func<Integer,Integer> range(final Integer startFrom)
    {
        return new Func<Integer,Integer>(){
            private final Integer start = startFrom;
            public Integer apply(final Integer input) {
                return (start-1)+input; // because init starts counting from 1
            }
        };
    }

    /**
     * The Convolution operator
     * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
     * @param l1 input sequence
     * @param l2 input sequence
     * @param <A> the type of the element in the first input sequence
     * @param <B> the type of the element in the second input sequence
     * @throws java.lang.IllegalArgumentException if either input sequence is null or if the sequences have differing lengths.
     * @return list of pairs; the first element from each of the two input sequences is the first pair in the output sequence and so on,
     *          in order. If the sequences do not have the same number of elements then an exception is thrown.
     */
    public static final <A,B>List<Pair<A,B>> zip(final Iterable<? extends A> l1, final Iterable<? extends B> l2)
    {
        if(l1==null) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l1 is null");
        if(l2==null) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l2 is null");

        final List<Pair<A,B>> output;
        if(l1 instanceof Collection<?> && l2 instanceof Collection<?>) {
            if (((Collection) l1).size() != ((Collection) l2).size())
                throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l1 and l2 have differing numbers of elements");

            output = new ArrayList<Pair<A, B>>(((Collection) l1).size());
        }
        else output = new ArrayList<Pair<A, B>>();
        final Iterator<? extends A> l1_it = l1.iterator();
        final Iterator<? extends B> l2_it = l2.iterator();

        while(l1_it.hasNext() && l2_it.hasNext()) output.add(new Pair(l1_it.next(),l2_it.next()));
        if(l1_it.hasNext() || l2_it.hasNext()) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l1 and l2 have differing numbers of elements");

        return Collections.unmodifiableList(output);
    }

    /**
     * The Convolution operator
     * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
     * @param l1 input sequence
     * @param l2 input sequence
     * @param l3 input sequence
     * @param <A> the type of the element in the first input sequence
     * @param <B> the type of the element in the second input sequence
     * @param <C> the type of the element in the third input sequence
     * @throws java.lang.IllegalArgumentException if any input sequence is null or if the sequences have differing lengths.
     * @return list of triplets; the first element from each of the input sequences is the first triplet in the output sequence and so on,
     *          in order. If the sequences do not have the same number of elements then an exception is thrown.
     */
    public static final <A,B,C>List<Triplet<A,B,C>> zip3(final Iterable<? extends A> l1, final Iterable<? extends B> l2, final Iterable<? extends C> l3)
    {
        if(l1==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1 is null");
        if(l2==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l2 is null");
        if(l3==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l3 is null");

        final List<Triplet<A,B,C>> output;
        if(l1 instanceof Collection<?> && l2 instanceof Collection<?> && l3 instanceof Collection<?>) {
            if (((Collection) l1).size() != ((Collection) l2).size())
                throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1, l2 and l3 have differing numbers of elements");

            output = new ArrayList<Triplet<A, B,C>>(((Collection) l1).size());
        }
        else output = new ArrayList<Triplet<A, B,C>>();
        final Iterator<? extends A> l1_it = l1.iterator();
        final Iterator<? extends B> l2_it = l2.iterator();
        final Iterator<? extends C> l3_it = l3.iterator();

        while(l1_it.hasNext() && l2_it.hasNext() && l3_it.hasNext()) output.add(new Triplet(l1_it.next(),l2_it.next(),l3_it.next()));
        if(l1_it.hasNext() || l2_it.hasNext() || l3_it.hasNext())
            throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1, l2 and l3 have differing numbers of elements");

        return Collections.unmodifiableList(output);
    }

    /**
     * The converse of the Convolution operator
     * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
     * @param input sequence of pairs
     * @param <A> the type of the first element in the pair
     * @param <B> the type of the second element in the pair
     * @throws java.lang.IllegalArgumentException if the input sequence is null
     * @return pair of lists; the first element from each of the two output sequences is the first pair in the input sequence and so on,
     *          in order.
     */
    public static final <A,B>Pair<List<A>,List<B>> unzip(final Iterable<Pair<A,B>> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.unzip(Iterable<Pair<A,B>>): input is null");

        final List<A> l1;
        final List<B> l2;
        if(input instanceof Collection<?>) {
            final int size = ((Collection) input).size();
            l1 = new ArrayList<A>(size);
            l2 = new ArrayList<B>(size);
        }
        else {
            l1 = new ArrayList<A>();
            l2 = new ArrayList<B>();
        }
        for(final Pair<A,B> pair:input)
        {
            l1.add(pair.getValue0());
            l2.add(pair.getValue1());
        }

        return new Pair(Collections.unmodifiableList(l1),Collections.unmodifiableList(l2));
    }

    /**
     * The converse of the Convolution operator
     * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
     * @param input sequence of triplets
     * @param <A> the type of the first element in the triplet
     * @param <B> the type of the second element in the triplet
     * @param <C> the type of the third element in the triplet
     * @throws java.lang.IllegalArgumentException if the input sequence is null
     * @return triplet of lists; the first element from each of the output sequences is the first triplet in the input sequence and so on,
     *          in order.
     */
    public static final <A,B,C>Triplet<List<A>,List<B>,List<C>> unzip3(final Iterable<Triplet<A,B,C>> input)
    {
        if(input==null) throw new IllegalArgumentException("Functional.unzip(Iterable<Pair<A,B>>): input is null");

        final List<A> l1;
        final List<B> l2;
        final List<C> l3;
        if(input instanceof Collection<?>) {
            final int size = ((Collection) input).size();
            l1 = new ArrayList<A>(size);
            l2 = new ArrayList<B>(size);
            l3 = new ArrayList<C>(size);
        }
        else {
            l1 = new ArrayList<A>();
            l2 = new ArrayList<B>();
            l3 = new ArrayList<C>();
        }

        for(final Triplet<A,B,C> triplet:input)
        {
            l1.add(triplet.getValue0());
            l2.add(triplet.getValue1());
            l3.add(triplet.getValue2());
        }

        return new Triplet(Collections.unmodifiableList(l1),Collections.unmodifiableList(l2),Collections.unmodifiableList(l3));
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into a sequence of output elements.
     * These sequences are concatenated into one final output sequence at the end of the transformation.
     * map: (T -> U list) -> T list -> U list
     * @param f a transformation function which takes a object of type T and returns a sequence of objects, presumably related, of type U
     * @param input a sequence to be fed into f
     * @param <T> the type of the element in the input sequence
     * @param <U> the type of the element in the output sequence
     * @return a list of type U containing the concatenated sequences of transformed values.
     */
    public static final <T,U>List<U> collect(final Func<? super T,? extends Iterable<U>> f, final Iterable<T> input)
    {
        List<U> output = input instanceof Collection<?> ? new ArrayList<U>(((Collection) input).size()) : new ArrayList<U>();
        for(final T element : input)
            output = Functional.concat(output, Functional.toList(f.apply(element)));
        return Collections.unmodifiableList(output);
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
     * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into a sequence of output elements.
     * These sequences are concatenated into one final output sequence at the end of the transformation.
     * map: (T -> U list) -> T list -> U list
     * This is a curried implementation of 'collect'
     * @param f a transformation function which takes a object of type T and returns a sequence of objects, presumably related, of type U
     * @param <T> the type of the element in the input sequence
     * @param <U> the type of the element in the output sequence
     * @return a list of type U containing the concatenated sequences of transformed values.
     * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
     */
    public static final <T,U>Func<Iterable<T>,List<U>> collect(final Func<? super T,? extends Iterable<U>> f)
    {
        return new Func<Iterable<T>, List<U>>() {
            @Override
            public List<U> apply(final Iterable<T> input) {
                return Functional.collect(f,input);
            }
        };
    }

    /**
     * takeNAndYield: given an input sequence and an integer, return two sequences. The first output sequence returned is a list
     * containing the first 'howMany' elements of the 'input' sequence and the second output sequence contains all the remaining
     * elements of the 'input' sequence. The 'input' sequence is traversed only as far as is required to produce the first list
     * and so the remainder of the 'input' sequence remains unevaluated. If 'howMany' is greater than the number of elements in
     * 'input' then the output list will contain all the elements of the input and the output sequence will be empty.
     * This is like <tt>take</tt> but leaves the user with the ability to continue the traversal of the input sequence from the point
     * at which the 'take' stopped.
     * @param input the input sequence
     * @param howMany the number of elements to be included in the first output list
     * @param <A> the type of the element in the input sequence
     * @return a pair: (list, seq) - the list contains 'howMany' elements of 'input' and the sequence contains the remainder
     */
    public static final <A>Pair<List<A>,Iterable<A>> takeNAndYield(final Iterable<A> input, final int howMany)
    {
        if (input == null) throw new IllegalArgumentException("Functional.takeNAndYield: input is null");

        int counter = 0;
        final List<A> output = new ArrayList<A>(howMany);
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

    /**
     * append: given the input sequence and an item, return a new, lazily-evaluated sequence containing the input with the item
     * as the final element.
     * @param t the item to be appended
     * @param input the input sequence
     * @param <T> the type of the element in the input sequence
     * @return a sequence containing all the elements of 'input' followed by 't'
     * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
     */
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

    /**
     * groupBy: similar to {@link #partition(Func,Iterable)} in that the input is grouped according to a function. This is more general than
     * <tt>partition</tt> though as the output can be an arbitrary number of groups, up to and including one group per item in the
     * input data set. The 'keyFn' is the grouping operator and it is used to determine the key at which any given element from
     * the input data set should be added to the output dictionary / map.
     * @param keyFn the grouping function. Given an element return the key to be used when storing this element in the dictionary
     * @param input the input sequence
     * @param <T> the type of the element in the input sequence
     * @param <U> the type of the element in the key
     * @return a java.util.Map containing a list of elements for each key
     */
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
        final Map<U,List<T>> output = new HashMap<U,List<T>>(intermediateResults.size());
        for(final Map.Entry<U,List<T>> entry : intermediateResults.entrySet())
             output.put(entry.getKey(),Collections.unmodifiableList(entry.getValue()));
        return Collections.unmodifiableMap(output);
    }

    /**
     * The Range class holds an inclusive lower bound and an exclusive upper bound. That is lower <= pos < upper
     */
    public static class Range<T>
    {
        private final T lowerBound;
        private final T upperExBound;

        /**
         * Create a new Range object
         * @param lower the inclusive lower bound of the Range
         * @param upperEx the exclusive upper bound of the Range
         */
        public Range(final T lower, final T upperEx)
        {
            this.lowerBound=lower;
            this.upperExBound=upperEx;
        }

        /**
         * Return the inclusive lower bound
         * @return the inclusive lower bound
         */
        public T from(){return lowerBound;}

        /**
         * return the exclusive upper bound
         * @return the exclusive upper bound
         */
        public T to(){return upperExBound;}

        public boolean equals(final Object other)
        {
            if(! (other instanceof Range<?>)) return false;
            final Range<?> otherRange = (Range<?>)other;
            return from().equals(otherRange.from()) && to().equals(otherRange.to());
        }

        public int hashCode()
        {
            return 13 * from().hashCode() + 7 * to().hashCode();
        }
    }

    /**
     * This list generator returns a list of Range objects which split the interval [1-'howManyElements') into 'howManyPartitions' Range objects.
     * If the interval cannot be divided exactly then the remainder is allocated evenly across the first
     * 'howManyElements' % 'howManyPartitions' Range objects.
     * @param howManyElements defines the exclusive upper bound of the interval to be split
     * @param howManyPartitions defines the number of Range objects to generate to cover the interval
     * @return a list of Range objects
     */
    public static List<Range<Integer>> partition(final int howManyElements, final int howManyPartitions)
    {
        return partition(Functional.range(0), howManyElements, howManyPartitions);
    }

    /**
     * This sequence generator returns a sequence of Range objects which split the interval [1-'howManyElements') into 'howManyPartitions'
     * Range objects. If the interval cannot be divided exactly then the remainder is allocated evenly across the first
     * 'howManyElements' % 'howManyPartitions' Range objects.
     * @param generator a function which generates members of the input sequence
     * @param howManyElements defines the exclusive upper bound of the interval to be split
     * @param howManyPartitions defines the number of Range objects to generate to cover the interval
     * @return a list of Range objects
     */
    public static <T>List<Range<T>> partition(final Func<Integer,T> generator, final int howManyElements, final int howManyPartitions)
    {
        if(howManyElements<=0) throw new IllegalArgumentException("Functional.partition() howManyElements cannot be non-positive");
        if(howManyPartitions<=0) throw new IllegalArgumentException("Functional.partition() howManyPartitions cannot be non-positive");

        final int size = howManyElements/howManyPartitions;
        final int remainder = howManyElements % howManyPartitions;

        assert size*howManyPartitions + remainder == howManyElements;

        final Integer seed = 0;
        final Func<Integer,Pair<T,Integer>> boundsCalculator = new Func<Integer, Pair<T, Integer>>() {
            @Override
            public Pair<T, Integer> apply(final Integer integer) {
                return Pair.with(
                        generator.apply(1 + (integer * size + (integer <= remainder ? integer : remainder))),
                        integer+1);
            }
        };
        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer>howManyPartitions;
            }
        };

        final Iterable<T> output = Functional.seq.unfold(boundsCalculator,finished,seed);

        final Iterator<T> iterator = output.iterator();
        if(iterator==null || !iterator.hasNext()) throw new IllegalStateException("Somehow we have no entries in our sequence of bounds");
        T last = iterator.next();
        final List<Range<T>> retval = new ArrayList<Range<T>>(howManyPartitions);
        for(int i=0;i<howManyPartitions;++i)
        {
            if(!iterator.hasNext()) throw new IllegalStateException(String.format("Somehow we have fewer entries (%d) in our sequence of bounds than expected (%d)",i,howManyPartitions));
            final T next = iterator.next();
            retval.add(new Range(last, next));
            last = next;
        }
        return retval;

//        return Functional.seq.init(new Func<Integer, Range<T>>() {
//            @Override
//            public Range<T> apply(final Integer integer) {
//// inefficient - the upper bound is computed twice (once at the end of an iteration and once at the beginning of the next iteration)
//                return new Range<T>( // 1 + the value because the init function expects the control range to start from one.
//                        generator.apply(1 + ((integer - 1) * size + (integer <= remainder + 1 ? integer - 1 : remainder))),
//                        generator.apply(1 + (integer * size + (integer <= remainder ? integer : remainder))));
//            }
//        }, howManyPartitions);
    }

    /**
     * Lazily-evaluated implementations of various of the algorithms
     * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
     */
    public static final class seq
    {
        /**
         * See <A href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</A>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
         * map: (T -> U) -> T seq -> U seq
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param input a sequence to be fed into f
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a lazily-evaluated sequence of type B containing the transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
         * map: (T -> U) -> T seq -> U seq
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a curried function that expects an input sequence which it feeds to the transformation f which returns a lazily-evaluated
         * sequence of type U containing the transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T,U>Func<Iterable<T>,Iterable<U>> map(final Func<? super T,? extends U> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.map(f, input);
                }
            };
        }

        /**
         * See <A href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</A>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
         * mapi: (Integer -> T -> U) -> T seq -> U seq
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param input a sequence to be fed into f
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a lazily-evaluated sequence of type B containing the transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T,U>Iterable<U> mapi(final Func2<Integer,? super T,? extends U> f, final Iterable<T> input)
        {
            if(f==null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            return new Iterable<U>() {
                @Override
                public final Iterator<U> iterator() {
                    return new Iterator<U>() {
                        private final Iterator<T> _input=input.iterator();
                        private final Func2<Integer,? super T,? extends U> _f = f;
                        private int counter = 0;
                        @Override
                        public final boolean hasNext() {
                            return _input.hasNext();
                        }

                        @Override
                        public final U next() {
                            return _f.apply(counter++,_input.next());
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.map(Func<T,U>,Iterable<T>): Removing elements is strictly prohibited");
                        }
                    };
                }
            };
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output sequence.
         * mapi: (int -> T -> U) -> T seq -> U seq
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a curried function that expects an input sequence which it feeds to the transformation f which returns a lazily-evaluated
         * sequence of type U containing the transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Currying">Currying</a>
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T,U>Func<Iterable<T>,Iterable<U>> mapi(final Func2<Integer,? super T,? extends U> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.mapi(f, input);
                }
            };
        }

        /**
         * Concatenate two sequences and return a new sequence containing the concatenation.
         * @param list1 first input sequence
         * @param list2 second input sequence
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the elements of the first sequence followed by the elements of the second sequence
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</a>
         * @param f a filter function. This is passed each input element in turn and returns either true or false. If true then
         *             the input element is passed through to the output otherwise it is ignored.
         * @param input a sequence of objects
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence which contains zero or more of the elements of the input sequence. Each element is included only if
         * the filter function returns true for the element.
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</a>
         * @param f a filter function. This is passed each input element in turn and returns either true or false. If true then
         *             the input element is passed through to the output otherwise it is ignored.
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence which contains zero or more of the elements of the input sequence. Each element is included only if
         * the filter function returns true for the element.
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T>Func<Iterable<T>,Iterable<T>> filter(final Func<? super T,Boolean> f)
        {
            return new Func<Iterable<T>,Iterable<T>>(){
                @Override
                public Iterable<T> apply(final Iterable<T> input) {
                    return Functional.seq.filter(f,input);
                }
            };
        }

        /**
         * choose: this is a map transformation with the difference being that the number of elements in the output sequence may
         * be between zero and the number of elements in the input sequence.
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * choose: (A -> B option) -> A list -> B list
         * @param f map function. This transforms the input element into an Option
         * @param input input sequence
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a lazily-evaluated sequence of transformed elements, numbering less than or equal to the number of input elements
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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

        /**
         * choose: this is a map transformation with the difference being that the number of elements in the output sequence may
         * be between zero and the number of elements in the input sequence.
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * choose: (A -> B option) -> A list -> B list
         * @param f map function. This transforms the input element into an Option
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a lazily-evaluated sequence of transformed elements, numbering less than or equal to the number of input elements
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T,U>Func<Iterable<T>,Iterable<U>> choose(final Func<? super T,Option<U>> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.choose(f, input);
                }
            };
        }

        /**
         * The init function, not dissimilar to list comprehensions, which is used to return a new finite sequence whose contents are
         * determined by successive calls to the function f.
         * init: (int -> T) -> int -> T seq
         * @param f generator function used to produce the individual elements of the output sequence.
         *          This function is called by init with the unity-based position of the current element in the output sequence being
         *          produced. Therefore, the first time f is called it will receive a literal '1' as its argument; the second time
         *          '2'; etc.
         * @param howMany the number of elements in the output sequence
         * @param <T> the type of the element in the output sequence
         * @return a lazily-evaluated sequence which will contain no more than 'howMany' elements of type 'T' which were generated by the function 'f'
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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
                            if(!hasNext())
                                throw new NoSuchElementException();
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

        /**
         * The init function, not dissimilar to list comprehensions, which is used to return a new infinite sequence whose contents are
         * determined by successive calls to the function f.
         * init: (int -> T) -> T seq
         * @param f generator function used to produce the individual elements of the output sequence.
         *          This function is called by init with the unity-based position of the current element in the output sequence being
         *          produced. Therefore, the first time f is called it will receive a literal '1' as its argument; the second time
         *          '2'; etc.
         * @param <T> the type of the element in the output sequence
         * @return a potentially infinite sequence containing elements of type 'T' which were generated by the function 'f'
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into a sequence of output elements.
         * These sequences are concatenated into one final output sequence at the end of the transformation.
         * map: (T -> U list) -> T list -> U list
         * @param f a transformation function which takes a object of type T and returns a sequence of objects, presumably related, of type U
         * @param input a sequence to be fed into f
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a lazily-evaluated sequence of type U containing the concatenated sequences of transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into a sequence of output elements.
         * These sequences are concatenated into one final output sequence at the end of the transformation.
         * map: (T -> U list) -> T list -> U list
         * @param f a transformation function which takes a object of type T and returns a sequence of objects, presumably related, of type U
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a function returning a lazily-evaluated sequence of type U containing the concatenated sequences of transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T,U>Func<Iterable<T>,Iterable<U>> collect(final Func<? super T,? extends Iterable<U>> f)
        {
            return new Func<Iterable<T>, Iterable<U>>() {
                @Override
                public Iterable<U> apply(final Iterable<T> input) {
                    return Functional.seq.collect(f,input);
                }
            };
        }

        /**
         * skip: the converse of <tt>take</tt>. Given a list return another list containing those elements that follow the
         * first 'howMany' elements. That is, if we skip(1,[1,2,3]) then we have [2,3]
         * @param howMany a non-negative number of elements to be discarded from the input sequence
         * @param input the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the remaining elements after the first 'howMany' elements of 'list' or an empty list if more elements
         * are skipped than are present in the 'list'
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T>Iterable<T> skip(final int howMany, final Iterable<T> input) {
            if (howMany < 0)
                throw new IllegalArgumentException("Functional.skip(int,Iterable<T>): howMany is negative");
            if (input == null) throw new IllegalArgumentException("Functional.skip(int,Iterable<T>): input is null");

            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        private final Iterator<T> it = input.iterator();
                        private boolean haveWeSkipped = false;

                        @Override
                        public boolean hasNext() {
                            if (haveWeSkipped && it.hasNext()) return true;
                            if (haveWeSkipped) return false;
                            for (int i = 0; i < howMany; ++i)
                                if (it.hasNext()) it.next();
                                else return false;
                            haveWeSkipped = true;
                            return it.hasNext();
                        }

                        @Override
                        public T next() {
                            if(!hasNext()) throw new NoSuchElementException();
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

        /**
         * skip: the converse of <tt>take</tt>. Given a list return another list containing those elements that follow the
         * first 'howMany' elements. That is, if we skip(1,[1,2,3]) then we have [2,3]
         * @param howMany a non-negative number of elements to be discarded from the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the remaining elements after the first 'howMany' elements of 'list' or an empty list if more elements
         * are skipped than are present in the 'list'
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T>Func<Iterable<T>,Iterable<T>> skip(final int howMany)
        {
            return new Func<Iterable<T>, Iterable<T>>() {
                @Override
                public Iterable<T> apply(final Iterable<T> input) {
                    return Functional.seq.skip(howMany, input);
                }
            };
        }

        /**
         * skipWhile: the converse of <tt>takeWhile</tt>. Given a list return another list containing all those elements from,
         * and including, the first element for which the predicate returns false. That is, if we skip(isOdd,[1,2,3]) then we have [2,3]
         * @param predicate ignore elements in the input while the predicate is true.
         * @param input the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the remaining elements after and including the first element for which
         * the predicate returns false
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static <T>Iterable<T> skipWhile(final Func<? super T,Boolean> predicate, final Iterable<T> input) {
            if (predicate == null)
                throw new IllegalArgumentException("Functional.skipWhile(Func,Iterable<T>): predicate is null");
            if (input == null) throw new IllegalArgumentException("Functional.skipWhile(Func,Iterable<T>): input is null");

            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        private final Iterator<T> it = input.iterator();
                        private boolean haveWeSkipped = false;
                        private boolean haveWeReadFirstValue = false;
                        private T firstValue = null;

                        @Override
                        public boolean hasNext() {
                            if (haveWeSkipped && it.hasNext()) return true;
                            if (haveWeSkipped) return false;
                            while(true)
                            {
                                if(it.hasNext())
                                {
                                    final T next = it.next();
                                    if(!predicate.apply(next))
                                    {
                                        haveWeSkipped = true;
                                        firstValue = next;
                                        return true;
                                    }
                                }
                                else
                                {
                                    haveWeSkipped = true;
                                    return false;
                                }
                            }
                        }

                        @Override
                        public T next() {
                            if(haveWeSkipped && !haveWeReadFirstValue && firstValue!=null)
                            {
                                haveWeReadFirstValue = true;
                                return firstValue;
                            }
                            if(haveWeSkipped && !haveWeReadFirstValue) throw new NoSuchElementException();
                            if(haveWeSkipped) return it.next();
                            final boolean another = hasNext();
                            return next();
                        }
                    };
                }
            };
        }

        /**
         * skipWhile: the converse of <tt>takeWhile</tt>. Given a list return another list containing all those elements from,
         * and including, the first element for which the predicate returns false. That is, if we skip(isOdd,[1,2,3]) then we have [2,3]
         * @param predicate ignore elements in the input while the predicate is true.
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the remaining elements after and including the first element for which
         * the predicate returns false
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T>Func<Iterable<T>,Iterable<T>> skipWhile(final Func<? super T,Boolean> predicate)
        {
            return new Func<Iterable<T>, Iterable<T>>() {
                @Override
                public Iterable<T> apply(final Iterable<T> input) {
                    return Functional.seq.skipWhile(predicate, input);
                }
            };
        }

        /**
         * take: given a sequence return another sequence containing the first 'howMany' elements
         * @param howMany a positive number of elements to be returned from the input sequence
         * @param list the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a sequence containing the first 'howMany' elements of 'list'
         * @throws java.util.NoSuchElementException if more elements are requested than are present in the input sequence
         */
        public static final<T>Iterable<T> take(final int howMany, final Iterable<? extends T> list)
        {
            if(howMany<0) throw new IllegalArgumentException("Functional.seq.take(int,Iterable<T>): howMany is negative");
            if(list==null) throw new IllegalArgumentException("Functional.seq.take(int,Iterable<T>): list is null");

            if(howMany==0) return new ArrayList<T>(0);

            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>(){
                        private final Iterator<? extends T> it = list.iterator();
                        private int howManyHaveWeRetrievedAlready = 0;
                        @Override
                        public boolean hasNext() {
                            return howManyHaveWeRetrievedAlready<howMany && it.hasNext();
                        }

                        @Override
                        public T next() {
                            if(howManyHaveWeRetrievedAlready>=howMany)
                                throw new java.util.NoSuchElementException("Cannot request additional elements from input");
                            final T next = it.next();
                            howManyHaveWeRetrievedAlready++;
                            return next;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.take: remove is not supported");
                        }
                    };
                }
            };
        }

        /**
         * take: given a sequence return another sequence containing the first 'howMany' elements
         * @param howMany a positive number of elements to be returned from the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a sequence containing the first 'howMany' elements of 'list'
         * @throws java.util.NoSuchElementException if more elements are requested than are present in the input sequence
         */
        public static final <T>Func<Iterable<T>,Iterable<T>> take(final int howMany)
        {
            return new Func<Iterable<T>, Iterable<T>>() {
                @Override
                public Iterable<T> apply(final Iterable<T> input) {
                    return Functional.seq.take(howMany, input);
                }
            };
        }

        /**
         * takeWhile: the converse of <tt>takeWhile</tt>. Given a list return another list containing all those elements from,
         * and including, the first element for which the predicate returns false. That is, if we skip(isOdd,[1,2,3]) then we have [2,3]
         * @param predicate ignore elements in the input while the predicate is true.
         * @param input the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the remaining elements after and including the first element for which
         * the predicate returns false
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static <T>Iterable<T> takeWhile(final Func<? super T,Boolean> predicate, final Iterable<T> input) {
            if (predicate == null)
                throw new IllegalArgumentException("Functional.takeWhile(Func,Iterable<T>): predicate is null");
            if (input == null) throw new IllegalArgumentException("Functional.takeWhile(Func,Iterable<T>): input is null");

            return new Iterable<T>() {
                @Override
                public Iterator<T> iterator() {
                    return new Iterator<T>() {
                        private final Iterator<T> it = input.iterator();
                        private boolean haveWeFinished = false;
                        private T next = null;
                        private boolean haveWeCheckedTheCurrentElement = false;

                        @Override
                        public boolean hasNext() {
                            if(!haveWeFinished)
                            {
                                if (!haveWeCheckedTheCurrentElement)
                                {
                                    if(it.hasNext()) {
                                        next = it.next();
                                        if (predicate.apply(next)) {
                                            haveWeCheckedTheCurrentElement = true;
                                            return true;
                                        } else {
                                            haveWeCheckedTheCurrentElement = true;
                                            haveWeFinished = true;
                                            return false;
                                        }
                                    }
                                    else
                                    {
                                        haveWeFinished = true;
                                        return false;
                                    }
                                }
                                else
                                {
                                    return true;
                                }
                            }
                            else
                            {
                                return false;
                            }
                        }

                        @Override
                        public T next() {
                            if(!haveWeFinished)
                            {
                                if(hasNext()) {
                                    haveWeCheckedTheCurrentElement = false;
                                    return next;
                                }
                                else throw new NoSuchElementException();
                            }
                            throw new NoSuchElementException();
                        }
                    };
                }
            };
        }

        /**
         * takeWhile: the converse of <tt>takeWhile</tt>. Given a list return another list containing all those elements from,
         * and including, the first element for which the predicate returns false. That is, if we skip(isOdd,[1,2,3]) then we have [2,3]
         * @param predicate ignore elements in the input while the predicate is true.
         * @param <T> the type of the element in the input sequence
         * @return a lazily-evaluated sequence containing the remaining elements after and including the first element for which
         * the predicate returns false
         * @see <a href="http://en.wikipedia.org/wiki/Lazy_evaluation">Lazy evaluation</a>
         */
        public static final <T>Func<Iterable<T>,Iterable<T>> takeWhile(final Func<? super T,Boolean> predicate)
        {
            return new Func<Iterable<T>, Iterable<T>>() {
                @Override
                public Iterable<T> apply(final Iterable<T> input) {
                    return Functional.seq.takeWhile(predicate, input);
                }
            };
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Unfold_(higher-order_function)">Unfold</a> and
         * <a href="http://en.wikipedia.org/wiki/Anamorphism">Anamorphism</a>
         * This is the converse of <tt>fold</tt>
         * unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
         */
        public final static <A,B>Iterable<A> unfold(final Func<? super B,Pair<A,B>> unspool, final Func<? super B,Boolean> finished, final B seed)
        {
            if(unspool==null) throw new IllegalArgumentException("unspool");
            if(finished==null) throw new IllegalArgumentException("finished");

            return new Iterable<A>() {
                @Override
                public Iterator<A> iterator() {
                    return new Iterator<A>() {
                        B next = seed;
                        @Override
                        public boolean hasNext() {
                            return !finished.apply(next);
                        }

                        @Override
                        public A next() {
                            if(!hasNext()) throw new NoSuchElementException();
                            final Pair<A,B> t = unspool.apply(next);
                            next = t.getValue1();
                            return t.getValue0();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.unfold(Func,Func,B): it is not possible to remove elements from this sequence");
                        }
                    };
                }
            };
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Unfold_(higher-order_function)">Unfold</a>
         * and <a href="http://en.wikipedia.org/wiki/Anamorphism">Anamorphism</a>
         * This is the converse of <tt>fold</tt>
         * unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
         */
        public final static <A,B>Iterable<A> unfold(final Func<? super B,Option<Pair<A,B>>> unspool, final B seed)
        {
            if(unspool==null) throw new IllegalArgumentException("unspool");

            return new Iterable<A>() {
                @Override
                public Iterator<A> iterator() {
                    return new Iterator<A>() {
                        B next = seed;
                        @Override
                        public boolean hasNext() {
                            return unspool.apply(next).isSome();
                        }

                        @Override
                        public A next() {
                            final Option<Pair<A,B>> temp = unspool.apply(next);
                            if(temp.isNone()) throw new NoSuchElementException();
                            next = temp.Some().getValue1();
                            return temp.Some().getValue0();
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.unfold(Func,B): it is not possible to remove elements from this sequence");
                        }
                    };
                }
            };
        }

        /**
         * This sequence generator returns a list of Range objects which split the interval [1-'howManyElements') into 'howManyPartitions' Range objects.
         * If the interval cannot be divided exactly then the remainder is allocated evenly across the first
         * 'howManyElements' % 'howManyPartitions' Range objects.
         * @param howManyElements defines the exclusive upper bound of the interval to be split
         * @param howManyPartitions defines the number of Range objects to generate to cover the interval
         * @return a list of Range objects
         */
        public static Iterable<Range<Integer>> partition(final int howManyElements, final int howManyPartitions)
        {
            return partition(Functional.range(0), howManyElements, howManyPartitions);
        }

        /**
         * This sequence generator returns a sequence of Range objects which split the interval [1-'howManyElements') into 'howManyPartitions'
         * Range objects. If the interval cannot be divided exactly then the remainder is allocated evenly across the first
         * 'howManyElements' % 'howManyPartitions' Range objects.
         * @param generator a function which generates members of the input sequence
         * @param howManyElements defines the exclusive upper bound of the interval to be split
         * @param howManyPartitions defines the number of Range objects to generate to cover the interval
         * @return a list of Range objects
         */
        public static <T>Iterable<Range<T>> partition(final Func<Integer,T> generator, final int howManyElements, final int howManyPartitions)
        {
            if(howManyElements<=0) throw new IllegalArgumentException("Functional.partition() howManyElements cannot be non-positive");
            if(howManyPartitions<=0) throw new IllegalArgumentException("Functional.partition() howManyPartitions cannot be non-positive");

            final int size = howManyElements/howManyPartitions;
            final int remainder = howManyElements % howManyPartitions;

            assert size*howManyPartitions + remainder == howManyElements;

            final Integer seed = 0;
            final Func<Integer,Pair<T,Integer>> boundsCalculator = new Func<Integer, Pair<T, Integer>>() {
                @Override
                public Pair<T, Integer> apply(final Integer integer) {
                    return Pair.with(
                            generator.apply(1 + (integer * size + (integer <= remainder ? integer : remainder))),
                            integer+1);
                }
            };
            final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
                @Override
                public Boolean apply(Integer integer) {
                    return integer>howManyPartitions;
                }
            };

            final Iterable<T> output = Functional.seq.unfold(boundsCalculator,finished,seed);

            return new Iterable<Range<T>>() {
                @Override
                public Iterator<Range<T>> iterator() {
                    return new Iterator<Range<T>>() {
                        final Iterator<T> iterator = output.iterator();
                        T last = iterator.next();
                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public Range<T> next() {
                            final T next = iterator.next();
                            final Range<T> retval = new Range(last, next);
                            last = next;
                            return retval;
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.partition(Func,int,int): it is not possible to remove elements from this sequence");
                        }
                    };
                }
            };
        }

        /**
         * The Convolution operator
         * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
         * @param l1 input sequence
         * @param l2 input sequence
         * @param <A> the type of the element in the first input sequence
         * @param <B> the type of the element in the second input sequence
         * @throws java.lang.IllegalArgumentException if either input sequence is null or if the sequences have differing lengths.
         * @return list of pairs; the first element from each of the two input sequences is the first pair in the output sequence and so on,
         *          in order. If the sequences do not have the same number of elements then an exception is thrown.
         */
        public static final <A,B>Iterable<Pair<A,B>> zip(final Iterable<? extends A> l1, final Iterable<? extends B> l2)
        {
            if(l1==null) throw new IllegalArgumentException("Functional.seq.zip(Iterable<A>,Iterable<B>): l1 is null");
            if(l2==null) throw new IllegalArgumentException("Functional.seq.zip(Iterable<A>,Iterable<B>): l2 is null");

            return new Iterable<Pair<A, B>>() {
                @Override
                public Iterator<Pair<A, B>> iterator() {
                    return new Iterator<Pair<A, B>>() {
                        private final Iterator<? extends A> l1_it = l1.iterator();
                        private final Iterator<? extends B> l2_it = l2.iterator();
                        @Override
                        public boolean hasNext() {
                            final boolean l1_it_hasNext = l1_it.hasNext();
                            final boolean l2_it_hasNext = l2_it.hasNext();
                            if(l1_it_hasNext != l2_it_hasNext) throw new IllegalArgumentException("Functional.seq.zip(Iterable<A>,Iterable<B>): l1 and l2 have differing numbers of elements");
                            return l1_it_hasNext && l2_it_hasNext;
                        }

                        @Override
                        public Pair<A, B> next() {
                            return Pair.with(l1_it.next(),l2_it.next());
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.zip(Iterable,Iterable): it is not possible to remove elements from this sequence");
                        }

//                        @Override
//                        public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
//
//                        }
                    };
                }
            };
        }

        public static final <A,B>Func<Iterable<B>,Iterable<Pair<A,B>>> zip(final Iterable<? extends A> l1)
        {
            return new Func<Iterable<B>,Iterable<Pair<A,B>>>() {
                @Override
                public Iterable<Pair<A,B>> apply(final Iterable<B> l2) {
                    return Functional.seq.zip(l1, l2);
                }
            };
        }

        /**
         * The Convolution operator
         * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
         * @param l1 input sequence
         * @param l2 input sequence
         * @param l3 input sequence
         * @param <A> the type of the element in the first input sequence
         * @param <B> the type of the element in the second input sequence
         * @param <C> the type of the element in the third input sequence
         * @throws java.lang.IllegalArgumentException if either input sequence is null or if the sequences have differing lengths.
         * @return list of pairs; the first element from each of the two input sequences is the first pair in the output sequence and so on,
         *          in order. If the sequences do not have the same number of elements then an exception is thrown.
         */
        public static final <A,B,C>Iterable<Triplet<A,B,C>> zip3(final Iterable<? extends A> l1, final Iterable<? extends B> l2, final Iterable<? extends C> l3)
        {
            if(l1==null) throw new IllegalArgumentException("Functional.seq.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1 is null");
            if(l2==null) throw new IllegalArgumentException("Functional.seq.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l2 is null");
            if(l3==null) throw new IllegalArgumentException("Functional.seq.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l3 is null");

            return new Iterable<Triplet<A, B, C>>() {
                @Override
                public Iterator<Triplet<A, B, C>> iterator() {
                    return new Iterator<Triplet<A, B, C>>() {
                        private final Iterator<? extends A> l1_it = l1.iterator();
                        private final Iterator<? extends B> l2_it = l2.iterator();
                        private final Iterator<? extends C> l3_it = l3.iterator();
                        @Override
                        public boolean hasNext() {
                            final boolean l1_it_hasNext = l1_it.hasNext();
                            final boolean l2_it_hasNext = l2_it.hasNext();
                            final boolean l3_it_hasNext = l3_it.hasNext();
                            if(l1_it_hasNext != l2_it_hasNext || l1_it_hasNext != l3_it_hasNext) throw new IllegalArgumentException("Functional.seq.zip3(Iterable<A>,Iterable<B>,Iterable<C>): the input sequences have differing numbers of elements");
                            return l1_it_hasNext && l2_it_hasNext && l3_it_hasNext;
                        }

                        @Override
                        public Triplet<A, B, C> next() {
                            return Triplet.with(l1_it.next(),l2_it.next(),l3_it.next());
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException("Functional.seq.zip3(Iterable,Iterable,Iterable): it is not possible to remove elements from this sequence");
                        }

//                        @Override
//                        public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
//
//                        }
                    };
                }
            };
        }

        public static final <A,B,C>Func<Iterable<C>,Iterable<Triplet<A,B,C>>> zip3(final Iterable<? extends A> l1,final Iterable<? extends B> l2)
        {
            return new Func<Iterable<C>,Iterable<Triplet<A,B,C>>>() {
                @Override
                public Iterable<Triplet<A,B,C>> apply(final Iterable<C> l3) {
                    return Functional.seq.zip3(l1, l2, l3);
                }
            };
        }

        /**
         * Convolution of functions
         * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
         * @param f the first transformation function
         * @param g the second transformation function
         * @param input the input sequence
         * @param <A> the type of the input sequence
         * @param <B> the result type of the first transformation
         * @param <C> the result type of the second transformation
         * @return a sequence of pairs. The first value of the pair is the result of the first transformation and the seocnd value of the
         * pair of the result of the second transformation.
         */
        public static <A,B,C>Iterable<Pair<B,C>> zip(final Func<? super A,B> f, final Func<? super A,C> g, final Iterable<? extends A> input)
        {
            return new Iterable<Pair<B, C>>() {
                @Override
                public Iterator<Pair<B, C>> iterator() {
                    return new Iterator<Pair<B, C>>() {
                        private final Iterator<? extends A> iterator = input.iterator();
                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public Pair<B, C> next() {
                            final A next = iterator.next();
                            return Pair.with(f.apply(next), g.apply(next));
                        }
                    };
                }
            };
        }
    }

    /**
     * See <a href="http://en.wikipedia.org/wiki/Recursion_(computer_science)">Recursion</a>
     * Recursive implementations of (some of) the algorithms contained herein
     */
    public final static class rec
    {
        private static final <A>Iterable<A> filter(final Func<? super A,Boolean> f, final Iterator<A> input, final Collection<A> accumulator)
        {
            if(input.hasNext())
            {
                A next = input.next();
                if(f.apply(next)) accumulator.add(next);
                return filter(f,input,accumulator);
            }
            else return accumulator;
        }

        /**
         * See <A href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</A>
         * This is a recursive implementation of filter.
         * See <a href="http://en.wikipedia.org/wiki/Recursion_(computer_science)">Recursion</a>
         * @param f a filter function. This is passed each input element in turn and returns either true or false. If true then
         *             the input element is passed through to the output otherwise it is ignored.
         * @param <A> the type of the element in the input sequence
         * @return a sequence which contains zero or more of the elements of the input sequence. Each element is included only if
         * the filter function returns true for the element.
         */
        public static final <A>Iterable<A> filter(final Func<? super A,Boolean> f, final Iterable<A> input)
        {
            return filter(f,input.iterator(),input instanceof Collection<?> ? new ArrayList<A>(((Collection) input).size()) : new ArrayList<A>());
        }

        private static final <A,B>Iterable<B> map(final Func<? super A,? extends B> f, final Iterator<A> input, final Collection<B> accumulator)
        {
            if(input.hasNext())
            {
                accumulator.add(f.apply(input.next()));
                return map(f,input,accumulator);
            }
            else return accumulator;
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a recursive implementation of the map function. It is a 1-to-1 transformation.
         * Every element in the input sequence will be transformed into an element in the output sequence.
         * map: (A -> B) -> A seq -> B seq
         * See <a href="http://en.wikipedia.org/wiki/Recursion_(computer_science)">Recursion</a>
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param input a sequence to be fed into f
         * @param <A> the type of the element in the input sequence
         * @param <B> the type of the element in the output sequence
         * @return a seq of type B containing the transformed values.
         */
        public static final <A,B>Iterable<B> map(final Func<? super A,? extends B> f, final Iterable<A> input)
        {
            return map(f,input.iterator(),input instanceof Collection<?> ? new ArrayList<B>(((Collection) input).size()) : new ArrayList<B>());
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
         * fold: (A -> B -> A) -> A -> B list -> A
         * This is a recursive implementation of fold
         * See <a href="http://en.wikipedia.org/wiki/Recursion_(computer_science)">Recursion</a>
         * @param f the aggregation function
         * @param initialValue the seed for the aggregation
         * @param <A> the type of the initialValue / seed
         * @param <B> the type of the element in the output sequence
         * @return the aggregated value
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Unfold_(higher-order_function)">Unfold</a>
         * and <a href="http://en.wikipedia.org/wiki/Anamorphism">Anamorphism</a>
         * unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
         * This is a recursive implementation of unfold
         * See <a href="http://en.wikipedia.org/wiki/Recursion_(computer_science)">Recursion</a>
         */
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

        /**
         * See <a href="http://en.wikipedia.org/wiki/Unfold_(higher-order_function)">Unfold</a>
         * and <a href="http://en.wikipedia.org/wiki/Anamorphism">Anamorphism</a>
         * unfold: (b -> (a, b)) -> (b -> Bool) -> b -> [a]
         * This is a recursive implementation of unfold
         * See <a href="http://en.wikipedia.org/wiki/Recursion_(computer_science)">Recursion</a>
         */
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

    /**
     * Implementations of the algorithms contained herein which return sets
     * See <a href="http://en.wikipedia.org/wiki/Set_(computer_science)">Set</a>
     */
    public static final class set
    {
        /**
         * See <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</a>
         * @param pred a filter function. This is passed each input element in turn and returns either true or false. If true then
         *             the input element is passed through to the output otherwise it is ignored.
         * @param input a sequence of objects
         * @param <A> the type of the element in the input sequence
         * @return a set which contains zero or more of the elements of the input sequence. Each element is included only if the filter
         *          function returns true for the element.
         */
        public final static <A>Set<A> filter(final Func<? super A,Boolean> pred, final Iterable<A> input)
        {
            final Set<A> output = input instanceof Collection<?> ? new HashSet<A>(((Collection) input).size()) : new HashSet<A>();
            for(final A element : input)
            {
                if(pred.apply(element))
                    output.add(element);
            }
            return Collections.unmodifiableSet(output);
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into a sequence of output elements.
         * These sequences are concatenated into one final output sequence at the end of the transformation.
         * map: (T -> U list) -> T list -> U list
         * @param f a transformation function which takes a object of type T and returns a sequence of objects, presumably related, of type U
         * @param input a sequence to be fed into f
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a set of type U containing the concatenated sequences of transformed values.
         */
        public static final <T,U>Set<U> collect(final Func<? super T,? extends Iterable<U>> f, final Iterable<T> input)
        {
            Set<U> output = input instanceof Collection<?> ? new HashSet<U>(((Collection) input).size()) : new HashSet<U>();
            for(final T element : input)
                output.addAll(Functional.toSet(f.apply(element)));
            return Collections.unmodifiableSet(output);
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This implementation of the map function returns a set instead of an ordered sequence. It is a 1-to-1 transformation.
         * Every element in the input sequence will be transformed into an element in the output sequence.
         * map: (A -> B) -> A seq -> B set
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param input a sequence to be fed into f
         * @param <A> the type of the element in the input sequence
         * @param <B> the type of the element in the output sequence
         * @return a set of type B containing the transformed values.
         * @throws UnsupportedOperationException if the <tt>add</tt> operation
         *         is not supported by this set
         * @throws ClassCastException if the class of the specified element
         *         prevents it from being added to this set
         * @throws NullPointerException if the specified element is null and this
         *         set does not permit null elements
         * @throws IllegalArgumentException if some property of the specified element
         *         prevents it from being added to this set
         */
        public final static <A,B> Set<B> map(final Func<? super A, ? extends B> f, final Iterable<A> input)
        {
            final Set<B> output = input instanceof Collection<?> ? new HashSet<B>(((Collection) input).size()) : new HashSet<B>();
            for(final A a : input)
                output.add(f.apply(a));
            return Collections.unmodifiableSet(output);
        }

        /**
         * Concatenate two sequences and return a new list containing the concatenation.
         * @param list1 first input sequence
         * @param list2 second input sequence
         * @param <T> the type of the element in the input sequence
         * @return a set containing the elements of the first sequence and the elements of the second sequence
         */
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

        /**
         * Non-destructive wrapper for set intersection
         * @param e1 input set
         * @param e2 input set
         * @param <E>
         * @return a set containing those elements which are contained within both sets 'e1' and 'e2'
         */
        public final static <E>Set<E> intersection(final Set<? extends E> e1, final Set<? extends E> e2)
        {
            Set<E> i = new HashSet<E>(e1);
            i.retainAll(e2);
            return Collections.unmodifiableSet(i);
        }

        /**
         * Non-destructive wrapper for set difference
         * @param inSet input set
         * @param notInSet input set
         * @param <E>
         * @return a set of those elements which are in 'inSet' and not in 'notInSet'
         */
        public final static <E>Set<E> asymmetricDifference(final Set<? extends E> inSet, final Set<? extends E> notInSet)
        {
            Set<E> i = new HashSet<E>(inSet);
            i.removeAll(notInSet);
            return Collections.unmodifiableSet(i);
        }
    }

    /**
     * Implementations of the algorithms contained herein in terms of 'fold'
     * See <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
     */
    public static final class inTermsOfFold
    {
        /**
         * See <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">Map</a>
         * This is a 1-to-1 transformation. Every element in the input sequence will be transformed into an element in the output
         * sequence.
         * map: (A -> B) -> A seq -> B list
         * @param f a transformation function which takes a object of type A and returns an object, presumably related, of type B
         * @param l a sequence to be fed into f
         * @param <T> the type of the element in the input sequence
         * @param <U> the type of the element in the output sequence
         * @return a list of type B containing the transformed values.
         * @see <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
         */
        public static final <T,U>List<T> map(final Func<? super U,? extends T> f, final Iterable<U> l)
        {
            final List<T> l2 = Functional.fold(new Func2<List<T>,U,List<T>>() {
                @Override
                public List<T> apply(final List<T> state, final U o2) {
                    state.add(f.apply(o2));
                    return state;
                }
            }, l instanceof Collection<?> ? new ArrayList<T>(((Collection) l).size()) : new ArrayList<T>(), l);
            return l2;
        }

        /**
         * See <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">Filter</a>
         * @param predicate a filter function. This is passed each input element in turn and returns either true or false. If true then
         *             the input element is passed through to the output otherwise it is ignored.
         * @param l a sequence of objects
         * @param <T> the type of the element in the input sequence
         * @return a list which contains zero or more of the elements of the input sequence. Each element is included only if the filter
         *          function returns true for the element.
         * @see <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
         */
        public static final <T>List<T> filter(final Func<? super T,Boolean> predicate, final Iterable<T> l)
        {
            final List<T> l2 = Functional.fold(new Func2<List<T>, T, List<T>>() {
                @Override
                public List<T> apply(final List<T> ts, final T o) {
                    if(predicate.apply(o)) ts.add(o);
                    return ts;
                }
            }, l instanceof Collection<?> ? new ArrayList<T>(((Collection) l).size()) : new ArrayList<T>(), l);
            return l2;
        }

        /**
         * The init function, not dissimilar to list comprehensions, which is used to return a new finite list whose contents are
         * determined by successive calls to the function f.
         * init: (int -> A) -> int -> A list
         * @param f generator function used to produce the individual elements of the output list. This function is called by init
         *          with the unity-based position of the current element in the output list being produced. Therefore, the first time
         *          f is called it will receive a literal '1' as its argument; the second time '2'; etc.
         * @param howMany the number of elements in the output list
         * @param <A> the type of the element in the output sequence
         * @return a list of 'howMany' elements of type 'T' which were generated by the function 'f'
         * @see <a href="http://en.wikipedia.org/wiki/Fold_(higher-order_function)">Fold</a>
         */
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

    /**
     * This class provides alternative implementations of those standard functions which would ordinarily throw an exception
     * in the event of an unexpected failure. The functions in this class will indicate the failure in a different manner, typically
     * using the Option type.
     */
    public final static class noException
    {
        /**
         * Find the first element from the input sequence for which the supplied predicate returns true
         * find: (A -> bool) -> A list -> A option
         * @param f predicate
         * @param input sequence
         * @param <A> the type of the element in the input sequence
         * @throws java.lang.IllegalArgumentException if f or input are null
         * @return the first element from the input sequence for which the supplied predicate returns true or None
         * if no element is found that satisfies the predicate
         */
        public final static <A>Option<A> find(final Func<? super A,Boolean> f, final Iterable<A> input)
        {
            if (f == null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            for(final A a : input)
                if(f.apply((a)))
                    return Option.toOption(a);
            return Option.None();
        }

        /**
         * As <tt>find</tt> except that here we return the zero-based position in the input sequence of the found element
         * findIndex: (A -> bool) -> A list -> int option
         * @param f predicate
         * @param input sequence
         * @param <A> the type of the element in the input sequence
         * @throws java.lang.IllegalArgumentException if f or input are null
         * @return the position in the input sequence of the first element from the input sequence for which the supplied predicate
         * returns true or None if no element is found that satisfies the predicate
         */
        public static <A>Option<Integer> findIndex(final Func<A,Boolean> f, final Iterable<? extends A> input)
        {
            if (f == null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            int pos = 0;
            for (final A a : input)
                if (f.apply(a))
                    return Option.toOption(pos);
                else pos++;
            return Option.None();
        }

        /**
         * As <tt>find</tt> except that here we return the last element in the input sequence that satisfies the predicate 'f'
         * findLast: (A -> bool) -> A seq -> A option
         * @param f predicate
         * @param input sequence
         * @param <A> the type of the element in the input sequence
         * @throws java.lang.IllegalArgumentException if f or input are null
         * @return the last element in the input sequence for which the supplied predicate returns true or None
         * if no element is found that satisfies the predicate
         */
        public final static <A>Option<A> findLast(final Func<? super A,Boolean> f, final Iterable<A> input)
        {
            if (f == null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            final Pair<List<A>,Iterable<A>> p = takeNAndYield(input,1);
            final Pair<A,Boolean> seed = Pair.with(p.getValue0().get(0),f.apply(p.getValue0().get(0)));
            final Pair<A,Boolean> result = fold(new Func2<Pair<A,Boolean>,A,Pair<A,Boolean>>(){
                @Override public Pair<A,Boolean> apply(final Pair<A,Boolean> state, final A item){return f.apply(item)?Pair.with(item,true):state;}
            },seed,p.getValue1());

            if(result.getValue1()) return Option.toOption(result.getValue0());
            return Option.None();
        }

        /**
         * As <tt>find</tt> except that here we return the last element in the input sequence that satisfies the predicate 'f'
         * findLast: (A -> bool) -> A list -> A option
         * @param f predicate
         * @param input sequence
         * @param <A> the type of the element in the input sequence
         * @throws java.lang.IllegalArgumentException if f or input are null
         * @return the last element in the input sequence for which the supplied predicate returns true or None
         * if no element is found that satisfies the predicate
         */
        public final static <A>Option<A> findLast(final Func<? super A,Boolean> f, final List<A> input)
        {
            if (f == null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            for (final A a : Iterators.reverse(input))
                if (f.apply(a))
                    return Option.toOption(a);
            return Option.None();
        }

        /**
         * 'pick' is an analogue of <tt>find</tt>. Instead of a predicate, 'pick' is passed a map function which returns an <tt>Option</tt>.
         * Each element of the input sequence is supplied in turn to the map function 'f' and the first non-None Option to be returned from
         * the map function is returned by 'pick' to the calling code.
         * pick: (A -> B option) -> A seq -> B option
         *
         * @param f the map function.
         * @param input the input sequence
         * @param <A> the type of the element in the input sequence
         * @param <B> the type of the output element
         * @return the first non-None transformed element of the input sequence or None if no such element exists
         */
        public static <A, B>Option<B> pick(final Func<A,Option<B>> f, final Iterable<? extends A> input)
        {
            if (f == null) throw new IllegalArgumentException("f");
            if (input == null) throw new IllegalArgumentException("input");

            for(final A a : input)
            {
                final Option<B> intermediate = f.apply(a); // which is, effectively, if(f(a)) return f(a), but without evaluating f twice
                if (!intermediate.isNone())
                    return intermediate;
            }
            return Option.None();
        }

        /**
         * forAll2: the predicate 'f' is applied to all elements in the input sequences input1 and input2 as pairs. If the predicate returns
         * true for all pairs and there is the same number of elements in both input sequences then forAll2 returns true. If the predicate
         * returns false at any point then the traversal of the input sequences halts and forAll2 returns false.
         * forAll2: (A -> B -> bool) -> A list -> B list -> bool option
         * @param f predicate to which each successive pair (input1_i, input2_i) is applied
         * @param input1 input sequence
         * @param input2 input sequence
         * @param <A> the base type of the element in the first input sequence
         * @param <B> the base type of the element in the second input sequence
         * @param <AA> the type of the element in the first input sequence
         * @param <BB> the type of the element in the second input sequence
         * @return true if the predicate 'f' evaluates true for all pairs, false otherwise or None
         * if the predicate returns true for all pairs and the sequences contain differing numbers
         * of elements
         */
        public final static <A, B,AA extends A,BB extends B>Option<Boolean> forAll2(final Func2<A, B,Boolean> f, final Iterable<AA> input1, final Iterable<BB> input2)
        {
            final Iterator<AA> enum1 = input1.iterator();
            final Iterator<BB> enum2 = input2.iterator();
            boolean enum1Moved = false, enum2Moved = false;
            do
            {
                enum1Moved = enum1.hasNext();
                enum2Moved = enum2.hasNext();
                if (enum1Moved && enum2Moved && !f.apply(enum1.next(), enum2.next()))
                    return Option.toOption(false);
            } while (enum1Moved && enum2Moved);
            if( enum1Moved != enum2Moved)
                return Option.None();
            return Option.toOption(true);
        }

        /**
         * take: given a list return another list containing the first 'howMany' elements or fewer if there are not enough elements
         * in the input sequence
         * @param howMany a positive upper bound for the number of elements to be returned from the input sequence
         * @param list the input sequence
         * @param <T> the type of the element in the input sequence
         * @return a list containing the first 'howMany' elements of 'list'
         */
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
                    break;
            }
            return Collections.unmodifiableList(output);
        }

        /**
         * The Convolution operator
         * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
         * @param l1 input sequence
         * @param l2 input sequence
         * @param <A> the type of the element in the first input sequence
         * @param <B> the type of the element in the second input sequence
         * @throws java.lang.IllegalArgumentException
         * @return list of pairs; the first element from each of the two input sequences is the first pair in the output sequence and so on,
         *          in order. If the sequences do not have the same number of elements the results thus far are returned
         */
        public static final <A,B>List<Pair<A,B>> zip(final Iterable<? extends A> l1, final Iterable<? extends B> l2)
        {
            if(l1==null) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l1 is null");
            if(l2==null) throw new IllegalArgumentException("Functional.zip(Iterable<A>,Iterable<B>): l2 is null");

            final List<Pair<A,B>> output = (l1 instanceof Collection<?> && l2 instanceof Collection<?>)
                    ? new ArrayList<Pair<A,B>>(((Collection) l1).size())
                    : new ArrayList<Pair<A, B>>();
            final Iterator<? extends A> l1_it = l1.iterator();
            final Iterator<? extends B> l2_it = l2.iterator();

            while(l1_it.hasNext() && l2_it.hasNext()) output.add(new Pair(l1_it.next(),l2_it.next()));

            return Collections.unmodifiableList(output);
        }

        /**
         * The Convolution operator
         * See <a href="http://en.wikipedia.org/wiki/Zip_(higher-order_function)">Zip</a>
         * @param l1 input sequence
         * @param l2 input sequence
         * @param l3 input sequence
         * @param <A> the type of the element in the first input sequence
         * @param <B> the type of the element in the second input sequence
         * @param <C> the type of the element in the third input sequence
         * @throws java.lang.IllegalArgumentException if any input sequence is null or if the sequences have differing lengths.
         * @return list of triplets; the first element from each of the input sequences is the first triplet in the output sequence and so on,
         *          in order. If the sequences do not have the same number of elements then the results thus far are returned
         */
        public static final <A,B,C>List<Triplet<A,B,C>> zip3(final Iterable<? extends A> l1, final Iterable<? extends B> l2, final Iterable<? extends C> l3)
        {
            if(l1==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l1 is null");
            if(l2==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l2 is null");
            if(l3==null) throw new IllegalArgumentException("Functional.zip3(Iterable<A>,Iterable<B>,Iterable<C>): l3 is null");

            final List<Triplet<A,B,C>> output = (l1 instanceof Collection<?> && l2 instanceof Collection<?> && l3 instanceof Collection<?>)
                    ? new ArrayList<Triplet<A,B,C>>(((Collection) l1).size())
                    : new ArrayList<Triplet<A,B,C>>();
            final Iterator<? extends A> l1_it = l1.iterator();
            final Iterator<? extends B> l2_it = l2.iterator();
            final Iterator<? extends C> l3_it = l3.iterator();

            while(l1_it.hasNext() && l2_it.hasNext() && l3_it.hasNext()) output.add(new Triplet(l1_it.next(),l2_it.next(),l3_it.next()));

            return Collections.unmodifiableList(output);
        }
    }

    /*
    // Following are control structures, eg if, switch
     */

    /**
     * A functional 'if' statement. Given 'a', evaluate the predicate. If the predicate evaluates to true then return the value of
     * evaluating the 'thenClause' with 'a' otherwise return the value of evaluating the 'elseClause' with 'a'
     * @param a value to be tested with the predicate and thence passed to one of the 'thenClause' and 'elseClause'
     * @param predicate function
     * @param thenClause function
     * @param elseClause function
     * @param <A> the type of the element which we are passing to the predicate
     * @param <B> the type of the result of the <tt>thenClause</tt> and <tt>elseClause</tt>
     * @return the results of evaluating the 'thenClause' or the 'elseClause', depending on whether the 'predicate' evaluates to true
     * or false respectively
     */
    public static final <A,B>B If(final A a, final Func<? super A,Boolean> predicate, final Func<? super A, ? extends B> thenClause, final Func<? super A, ? extends B> elseClause)
    {
        if (a == null) throw new IllegalArgumentException("a");
        if (predicate == null) throw new IllegalArgumentException("predicate");
        if (thenClause == null) throw new IllegalArgumentException("thenClause");
        if (elseClause == null) throw new IllegalArgumentException("elseClause");

        return predicate.apply(a) ? thenClause.apply(a) : elseClause.apply(a);
    }

    /**
     * toCase: a Case builder function.
     * @param pred predicate
     * @param result the result function to be applied if the predicate evaluates to true
     * @param <A> the type of the element being passed to the predicate
     * @param <B> the type of the result of the transformation function
     * @return a new Case object
     */
    public static final <A,B>Case<A,B> toCase(final Func<A,Boolean> pred, final Func<A, B> result)
    {
        if (pred == null) throw new IllegalArgumentException("pred");
        if (result == null) throw new IllegalArgumentException("res");

        return new Case<A, B> ( pred, result );
    }

    /**
     * Functional switch statement. Provide a sequence of Cases and a function which will be evaluated if none of the Cases are true.
     * @param input the value to be tested
     * @param cases sequence of Case objects
     * @param defaultCase function to be evaluated if none of the Cases are true
     * @param <A> the type of the element being passed to the predicates in the {@link me.shaftesbury.utils.functional.Case}
     * @param <B> the type of the result
     * @return the result of the appropriate Case or the result of the 'defaultCase' function
     */
    public static <A, B>B Switch(final A input, final Iterable<Case<A, B>> cases, final Func<A,B> defaultCase)
    {
        return Switch(input,IterableHelper.create(cases),defaultCase);
    }

    /**
     * Functional switch statement. Provide a sequence of Cases and a function which will be evaluated if none of the Cases are true.
     * @param input the value to be tested
     * @param cases sequence of Case objects
     * @param defaultCase function to be evaluated if none of the Cases are true
     * @param <A> the type of the element passed to the predicate in the {@link me.shaftesbury.utils.functional.Case}
     * @param <B> the type of the result
     * @return the result of the appropriate Case or the result of the 'defaultCase' function
     */
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

    /**
     * Helper function to return the first element in a Pair
     * @param <A> the type of the first element in the pair
     * @param <B> the type of the second element in the pair
     * @return a function that returns the first element in a Pair
     */
    public static final <A,B>Func<Pair<A,B>,A> first()
    {
        return new Func<Pair<A, B>, A>() {
            @Override
            public A apply(Pair<A, B> pair) {
                return pair.getValue0();
            }
        };
    }

    /**
     *  Helper function to return the second element in a Pair
     * @param <A> the type of the first element in the pair
     * @param <B> the type of the second element in the pair
     * @return a function that returns the second element in a Pair
     */
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