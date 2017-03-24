package me.shaftesbury.utils.functional;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CircularArrayListTest
{
    @Test
    public void CircularArrayListInitialiseTest1()
    {
        final List<Integer> input = Arrays.asList(1,2,3,4,5);
        final List<Integer> output = new CircularArrayList<Integer>(input,4);
        final List<Integer> expected = Arrays.asList(1,2,3,4);
        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test
    public void CircularArrayListInitialiseTest2()
    {
        final List<Integer> input = Arrays.asList(1,2,3,4,5);
        final List<Integer> output = new CircularArrayList<Integer>(input,6);
        final List<Integer> expected = Arrays.asList(1,2,3,4,5,1);
        AssertIterable.assertIterableEquals(expected,output);
    }
}
