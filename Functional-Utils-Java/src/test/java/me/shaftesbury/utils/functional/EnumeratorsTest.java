package me.shaftesbury.utils.functional;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 08/11/13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class EnumeratorsTest
{
    private static final Functional.Func<Integer,Integer> DoublingGenerator =
            new Functional.Func<Integer,Integer>()
            {
                @Override public Integer apply(Integer a) { return 2*(a + 1);}
            };

    @Test
    public void ReverseTest1() throws Exception
    {
        List<Integer> li = Functional.init(DoublingGenerator, 10);
        Collection<Integer> expected = Arrays.asList(new Integer[]{20,18,16,14,12,10,8,6,4,2});
        Iterable<Integer> output = Enumerators.ReverseEnum(li);
        AssertIterable.assertIterableEquals(expected,output);
    }
}
