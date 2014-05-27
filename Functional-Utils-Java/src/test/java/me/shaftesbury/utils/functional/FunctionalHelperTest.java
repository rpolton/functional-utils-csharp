package me.shaftesbury.utils.functional;

import org.junit.Assert;
import org.junit.Test;

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
}