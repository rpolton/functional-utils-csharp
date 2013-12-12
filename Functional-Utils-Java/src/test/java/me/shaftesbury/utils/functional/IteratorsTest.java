package me.shaftesbury.utils.functional;

import org.junit.Test;

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
                @Override public Integer apply(Integer a) { return 2*(a + 1);}
            };

    @Test
    public void ReverseTest1() throws Exception
    {
        List<Integer> li = Functional.init(DoublingGenerator, 10);
        Collection<Integer> expected = Arrays.asList(new Integer[]{20,18,16,14,12,10,8,6,4,2});
        Iterable<Integer> output = Iterators.ReverseIterator(li);
        AssertIterable.assertIterableEquals(expected,output);
    }

    private final Map<Integer, String> list = new HashMap<Integer, String>();
    /*
    @TestSetup
    public void Initialise()
    {
        list.put(1, "one");
        list.put(2, "two");
        list.put(10, "ten");
        list.put(100, "one hundred");
        list.put(97, "ninety seven");
        list.put(-1, "minus one");
        list.put(0, "zero zero UFO");
    }

    @TestTearDown
    public void Clear()
    {
        list.clear();
    }

    @Test
    public void SteppedEnumTest1()
    {
        StringBuilder sb = new StringBuilder();
        for (KeyValuePair<int, string> pair : Enumerators.SteppedIterator(list, 3))
        sb.Append(pair.ToString());

        string expected =
                new StringBuilder("[-1, minus one]").Append("[2, two]").Append("[100, one hundred]").ToString();
        Assert.AreEqual(expected, sb.ToString());
    }
             */
}
