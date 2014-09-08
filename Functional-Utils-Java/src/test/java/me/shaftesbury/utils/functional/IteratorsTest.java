package me.shaftesbury.utils.functional;

import org.junit.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 08/11/13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class IteratorsTest
{
    private static final Func<Integer,Integer> DoublingGenerator =
            new Func<Integer,Integer>()
            {
                @Override public Integer apply(Integer a) { return 2*a;}
            };

    @Test
    public void ReverseTest1()
    {
        final List<Integer> li = Functional.init(DoublingGenerator, 10);
        final Collection<Integer> expected = Arrays.asList(new Integer[]{20,18,16,14,12,10,8,6,4,2});
        final Iterable<Integer> output = Iterators.reverse(li);
        AssertIterable.assertIterableEquals(expected,output);
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
}
