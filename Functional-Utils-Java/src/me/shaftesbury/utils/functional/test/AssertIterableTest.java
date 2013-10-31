package me.shaftesbury.utils.functional.test;

import me.shaftesbury.utils.functional.AssertIterable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 10:50
 * To change this template use File | Settings | File Templates.
 */
public final class AssertIterableTest
{
    @Test
    public void assertEqualsTest1()
    {
        List<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        AssertIterable.assertIterableEquals(input,input);
    }

    @Test(expected = AssertionError.class)
    public void assertUnequalTest1()
    {
        List<Integer> input1 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        List<Integer> input2 = Arrays.asList(new Integer[]{1,2,3});
        AssertIterable.assertIterableEquals(input1,input2);
    }

    @Test(expected = AssertionError.class)
    public void assertUnequalTest2()
    {
        List<Integer> input1 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        List<Integer> input2 = Arrays.asList(new Integer[]{1,2,3});
        AssertIterable.assertIterableEquals(input2,input1);
    }

    @Test(expected = AssertionError.class)
    public void assertUnequalTest3()
    {
        List<Integer> input1 = null;
        List<Integer> input2 = Arrays.asList(new Integer[]{1,2,3});
        AssertIterable.assertIterableEquals(input2,input1);
    }

    @Test(expected = AssertionError.class)
    public void assertUnequalTest4()
    {
        List<Integer> input1 = Arrays.asList(new Integer[]{1,2,3});
        List<Integer> input2 = null;
        AssertIterable.assertIterableEquals(input2,input1);
    }

    @Test(expected = AssertionError.class)
    public void assertUnequalTest5()
    {
        List<Integer> input1 = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        List<Integer> input2 = Arrays.asList(new Integer[]{1,2,3,5,4});
        AssertIterable.assertIterableEquals(input1,input2);
    }
}