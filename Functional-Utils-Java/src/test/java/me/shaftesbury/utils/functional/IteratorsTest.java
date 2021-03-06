package me.shaftesbury.utils.functional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 08/11/13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class IteratorsTest
{
    private static Function<Integer, Integer> DoublingGenerator =
            new Function<Integer, Integer>()
            {
                 public Integer apply(Integer a) { return 2*a;}
            };

    @Test
    public void ReverseTest1()
    {
        final List<Integer> li = Functional.init(DoublingGenerator, 10);
        final Collection<Integer> expected = Arrays.asList(new Integer[]{20,18,16,14,12,10,8,6,4,2});
        final Iterable<Integer> output = Iterators.reverse(li);
        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cantReverseAnEmptyList()
    {
        final List<Integer> li = new ArrayList<Integer>();
        Iterators.reverse(li);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cantRemoveFromReversedIterator()
    {
        final Iterable<Integer> rv = Iterators.reverse(Arrays.asList(1, 2, 3, 4));
        rv.iterator().remove();
    }

    private final Map<Integer, String> map = new LinkedHashMap<Integer, String>();
    private final List<String> list = new ArrayList<String>();

    @Before
    public void Initialise()
    {
        map.put(-1, "minus one");
        map.put(0, "zero zero UFO");
        map.put(1, "one");
        map.put(2, "two");
        map.put(10, "ten");
        map.put(97, "ninety seven");
        map.put(100, "one hundred");
        list.addAll(map.values());
        Collections.sort(list);
    }

    @After
    public void Clear()
    {
        map.clear();
        list.clear();
    }

    @Test
    public void SteppedEnumTest1()
    {
        final StringBuilder sb = new StringBuilder();
        for (final String s : Iterators.everyNth(3, list))
            sb.append(s.toString());

        final String expected =
                new StringBuilder("minus one").append("one hundred").append("zero zero UFO").toString();
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void SteppedEnumTest2()
    {
        final StringBuilder sb = new StringBuilder();
        for (final String s : Iterators.everyNth(2, list))
            sb.append(s.toString());

        final String expected =
                new StringBuilder("minus one").append("one").append("ten").append("zero zero UFO").toString();
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void SteppedEnumTest3()
    {
        final StringBuilder sb = new StringBuilder();
        for (final String s : Iterators.everyNth(1, list))
            sb.append(s.toString());

        final String expected =
                new StringBuilder("minus one").append("ninety seven").append("one").append("one hundred").append("ten").append("two").
                        append("zero zero UFO").toString();
        Assert.assertEquals(expected, sb.toString());
    }

    @Test
    public void SteppedEnumTest4()
    {
        final StringBuilder sb = new StringBuilder();
        for (final String s : Iterators.everyNth(4, Iterators.reverse(list)))
            sb.append(s.toString());

        final String expected =
                new StringBuilder("zero zero UFO").append("one").toString();
        Assert.assertEquals(expected, sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cantStepLessThanOne()
    {
        Iterators.everyNth(0,Arrays.asList(1,2,34));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cantRemoveFromEveryNth()
    {
        final Iterable<Integer> integers = Iterators.everyNth(2, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        integers.iterator().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cantRestartIteratorTest1()
    {
        final Iterable<Integer> rv = Iterators.reverse(Arrays.asList(1, 2, 3));
        try {
            rv.iterator();
        } catch(final UnsupportedOperationException e) { Assert.fail("Shouldn't reach here"); }
        rv.iterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cantRestartIteratorTest2()
    {
        final Iterable<Integer> rv = Iterators.everyNth(2, Arrays.asList(1, 2, 3));
        try {
            rv.iterator();
        } catch(final UnsupportedOperationException e) { Assert.fail("Shouldn't reach here"); }
        rv.iterator();
    }

    @Test
    public void everyNthRepeatedHasNextTest()
    {
        final Iterable<Integer> integers = Iterators.everyNth(2, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        final Iterator<Integer> iterator = integers.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(3), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());
    }

    @Test
    public void everyNthRepeatedNextTest()
    {
        final Iterable<Integer> integers = Iterators.everyNth(2, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        final Iterator<Integer> iterator = integers.iterator();
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertEquals(Integer.valueOf(3), iterator.next());
        Assert.assertEquals(Integer.valueOf(5), iterator.next());
    }
}
