package me.shaftesbury.utils.functional;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static me.shaftesbury.utils.functional.FunctionalHelper.*;

/**
 * Created by Bob on 16/05/14.
 */
public final class FunctionalHelperTest
{
    @Test
    public void theyAreAllNone()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return Option.None();
            }
        }, 5);

        Assert.assertTrue(allNone(input));
    }

    @Test
    public void theyAreNotAllNone()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return integer==1 ? Option.<Object>toOption(new Integer(1)) : Option.None();
            }
        }, 5);

        Assert.assertFalse(allNone(input));
    }

    @Test
    public void theyAreAllSome()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return Option.<Object>toOption(new Integer(1));
            }
        }, 5);

        Assert.assertTrue(allSome(input));
    }

    @Test
    public void theyAreNotAllSome()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return integer==1 ? Option.<Object>toOption(new Integer(1)) : Option.None();
            }
        }, 5);

        Assert.assertFalse(allSome(input));
    }

    @Test
    public void justTheOnesThatAreNone()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return integer==1 ? Option.<Object>toOption(new Integer(1)) : Option.None();
            }
        }, 5);

        final List<Option<Object>> output = areNone(input).toList();

        Assert.assertEquals(input.size() - 1, output.size());
    }

    @Test
    public void justTheOnesThatAreSome()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return integer==1 ? Option.<Object>toOption(new Integer(1)) : Option.None();
            }
        }, 5);

        final List<Option<Object>> output = areSome(input).toList();

        Assert.assertEquals(1, output.size());
    }

    @Test
    public void theValueOfJustTheOnesThatAreSome()
    {
        final List<Option<Object>> input = Functional.init(new Func<Integer, Option<Object>>() {
            @Override
            public Option<Object> apply(Integer integer) {
                return integer==1 ? Option.<Object>toOption(new Integer(1)) : Option.None();
            }
        }, 5);

        final Iterable2<Option<Object>> output = areSome(input);
        final List<Object> finalOutput = some(output).toList();

        AssertIterable.assertIterableEquals(Arrays.asList((Object)1),finalOutput);
    }

    @Test
    public void partitionRangesOfInt()
    {
        final int noElems = 13;
        final int noPartitions = 5;
        final List<Range<Integer>> partitions = FunctionalHelper.partition(noElems,noPartitions);

        final List<Integer> expectedStart = Arrays.asList(0,3,6,9,11);
        final List<Integer> expectedEnd = Arrays.asList(3,6,9,11,13);
        final List<Pair<Integer,Integer>> expected = Functional.zip(expectedStart,expectedEnd);

        final List<Pair<Integer,Integer>> output = Functional.map(new Func<Range<Integer>, Pair<Integer,Integer>>() {
            @Override
            public Pair<Integer, Integer> apply(final Range<Integer> range) {
                return Pair.with(range.from(), range.to());
            }
        }, partitions);

        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test
    public void partitionRangesOfString()
    {
        final int noElems = 13;
        final int noPartitions = 5;
        final List<Range<String>> partitions = Functional.toList(
                FunctionalHelper.partition(
                        new Func<Integer,String>(){public String apply(final Integer i) {return new Integer(i-1).toString(); }},
                        noElems,noPartitions));

        final List<String> expectedStart = Arrays.asList("0","3","6","9","11");
        final List<String> expectedEnd = Arrays.asList("3","6","9","11","13");
        final List<Pair<String,String>> expected = Functional.zip(expectedStart,expectedEnd);

        final List<Pair<String,String>> output = Functional.map(new Func<Range<String>, Pair<String,String>>() {
            @Override
            public Pair<String, String> apply(final Range<String> range) {
                return Pair.with(range.from(), range.to());
            }
        }, partitions);

        AssertIterable.assertIterableEquals(expected,output);
    }
}
