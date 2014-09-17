package me.shaftesbury.utils.functional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SpeedTest
{
    public final static <A,B> List<B> mapWithIterable(final Func<A, ? extends B> f, final Iterable<? extends A> input)
    {
        final List<B> output = new ArrayList<B>();
        for(final A a : input)
            output.add(f.apply(a));
        return Collections.unmodifiableList(output);
    }

    public final static <A,B> List<B> mapWithCollection(final Func<A, ? extends B> f, final Collection<? extends A> input)
    {
        final List<B> output = new ArrayList<B>(input.size());
        for(final A a : input)
            output.add(f.apply(a));
        return Collections.unmodifiableList(output);
    }

    public final static <A,B> List<B> mapWithInstanceOf(final Func<A, ? extends B> f, final Iterable<? extends A> input)
    {
        final List<B> output = input instanceof Collection<?> ? new ArrayList<B>(((Collection) input).size()) : new ArrayList<B>();
        for(final A a : input)
            output.add(f.apply(a));
        return Collections.unmodifiableList(output);
    }


    @Test
    public void MapTest1()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(Functional.constant(10),1000000);
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<String> output = mapWithIterable(Functional.<Integer>dStringify(), input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to map using Iterable and whatever array resizing the JVM implements");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<String> output = mapWithCollection(Functional.<Integer>dStringify(), input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to map using Collection");
        }
        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<String> output = mapWithInstanceOf(Functional.<Integer>dStringify(), input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to map using a Collection and instanceof to determine the initial array size");
        }
    }

    private static final Func<Integer,Integer> DoublingGenerator =
            new Func<Integer,Integer>()
            {
                @Override public Integer apply(Integer a) { return 2*a;}
            };

    public final static <A>List<A> filterWithIterable(final Func<? super A,Boolean> pred, final Iterable<A> input)
    {
        final List<A> output = new ArrayList<A>();
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return Collections.unmodifiableList(output);
    }

    public final static <A>List<A> filterWithCollection(final Func<? super A,Boolean> pred, final Collection<A> input)
    {
        final List<A> output = new ArrayList<A>(input.size());
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return Collections.unmodifiableList(output);
    }

    public final static <A>List<A> filterWithHalfCollection(final Func<? super A,Boolean> pred, final Collection<A> input)
    {
        final List<A> output = new ArrayList<A>((input.size()/2)+1);
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return Collections.unmodifiableList(output);
    }

    public final static <A>List<A> filterWithInstanceOf(final Func<? super A,Boolean> pred, final Iterable<A> input)
    {
        final List<A> output = input instanceof Collection<?> ? new ArrayList<A>(((Collection) input).size()) : new ArrayList<A>();
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return Collections.unmodifiableList(output);
    }

    @Test
    public void filterTestWithEmptyResultsAndIterable()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(DoublingGenerator, 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithIterable(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using Iterable and whatever array resizing the JVM implements");
        }

        final Collection<Integer> output1 = filterWithIterable(Functional.isOdd, input);

        System.out.println("Size of output1 (Iterable) is "+output1.size());
    }

    @Test
    public void filterTestWithEmptyResultsAndCollection()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(DoublingGenerator, 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithCollection(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using Collection");
        }

        final Collection<Integer> output2 = filterWithCollection(Functional.isOdd, input);

        System.out.println("Size of output2 (Collection) is "+output2.size());
    }

    @Test
    public void filterTestWithEmptyResultsAndInstanceOf()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(DoublingGenerator, 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithInstanceOf(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using a Collection and instanceof to determine the initial array size");
        }

        final Collection<Integer> output3 = filterWithInstanceOf(Functional.isOdd, input);

        System.out.println("Size of output3 (instanceof) is "+output3.size());
    }

    @Test
    public void filterTestWithEmptyResultsAndHalfSizeCollection()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(DoublingGenerator, 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithHalfCollection(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using a Collection initialised to half-size to determine the initial array size");
        }

        final Collection<Integer> output4 = filterWithHalfCollection(Functional.isOdd, input);

        System.out.println("Size of output4 (Collection half-size) is "+output4.size());
    }

    @Test
    public void filterTestWithHalfSizeResultsAndIterable()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(Functional.<Integer>identity(), 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithIterable(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using Iterable and whatever array resizing the JVM implements");
        }

        final Collection<Integer> output1 = filterWithIterable(Functional.isOdd, input);

        System.out.println("Size of output1 (Iterable) is "+output1.size());
    }

    @Test
    public void filterTestWithHalfSizeResultsAndCollection()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(Functional.<Integer>identity(), 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithCollection(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using Collection");
        }

        final Collection<Integer> output2 = filterWithCollection(Functional.isOdd, input);

        System.out.println("Size of output2 (Collection) is "+output2.size());
    }

    @Test
    public void filterTestWithHalfSizeResultsAndInstanceOf()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(Functional.<Integer>identity(), 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithInstanceOf(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using a Collection and instanceof to determine the initial array size");
        }

        final Collection<Integer> output3 = filterWithInstanceOf(Functional.isOdd, input);

        System.out.println("Size of output3 (instanceof) is "+output3.size());
    }

    @Test
    public void filterTestWithHalfSizeResultsAndHalfSizeCollection()
    {
        final int howMany=10;
        final Collection<Integer> input = Functional.init(Functional.<Integer>identity(), 10000000);

        {
            final long start = System.currentTimeMillis();
            for (int i = 0; i < howMany; ++i) {
                final Collection<Integer> output = filterWithHalfCollection(Functional.isOdd, input);
            }
            final long howLong = System.currentTimeMillis() - start;
            System.out.println(howLong + "ms to filter using a Collection initialised to half-size to determine the initial array size");
        }

        final Collection<Integer> output4 = filterWithHalfCollection(Functional.isOdd, input);

        System.out.println("Size of output4 (Collection half-size) is "+output4.size());
    }
}
