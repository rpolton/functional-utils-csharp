package me.shaftesbury.utils.functional;

import org.junit.Test;
import org.junit.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 04/12/13
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public class OptionTest
{
    @Test
    public void equalityTest1()
    {
        Option<String> a = Option.toOption("TEST");
        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void equalityTest2()
    {
        Option<String> a = Option.toOption("TEST");
        Option<String> b = Option.toOption("TEST");
        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void equalityTest3()
    {
        Option<Integer> a = Option.toOption(1);
        Option<Integer> b = Option.toOption(2);
        Assert.assertTrue(a.equals(a));
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void OptionTestValueType1()
    {
        final int expected = 10;
        Option<Integer> a = Option.toOption(expected);
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        Assert.assertEquals((Integer)expected, a.Some());
    }

    @Test
    public void OptionTestStringType1()
    {
        final String expected = "ll";
        Option<String> a = Option.toOption(expected);
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        Assert.assertEquals(expected, a.Some());
    }

    @Test
    public void OptionTestValueType2()
    {
        Option<Integer> a = Option.None();
        Assert.assertTrue(a.isNone());
        Assert.assertFalse(a.isSome());
    }

    class tmp{}

    @Test
    public void OptionTestReferenceType1()
    {
        Option<tmp> a = Option.toOption(new tmp());
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        a.Some();
    }

    @Test
    public void OptionTestReferenceType2()
    {
        Option<tmp> a = new Option<tmp>(null);
        Assert.assertTrue(a.isNone());
        Assert.assertFalse(a.isSome());
    }

//    @Test
//    public void OptionMonadTest1()
//    {
//        var e = from a in 10.toOption()
//        from b in 2.toOption()
//        select a + b;
//        Assert.assertTrue(e.isSome());
//        Assert.assertFalse(e.isNone());
//        Assert.assertEquals(12, e.Some());
//    }

//    @Test(expected = OptionNoValueAccessException.class)
//    public void OptionMonadTest2()
//    {
//        var e = from a in Option<Integer>.None()
//        from b in 2.toOption()
//        select a + b;
//        Assert.assertTrue(e.isNone());
//        Assert.assertFalse(e.isSome());
//    }

//    @Test(expected = OptionNoValueAccessException.class)
//    public void OptionMonadTest3()
//    {
//        var e = from a in 10.toOption()
//        from b in Option<Integer>.None()
//        select a + b;
//        Assert.assertTrue(e.isNone());
//        Assert.assertFalse(e.isSome());
//    }

//    @Test(expected = OptionNoValueAccessException.class)
//    public void OptionMonadTest4()
//    {
//        var e = from a in Option<Integer>.None()
//        from b in Option<Integer>.None()
//        select a + b;
//        Assert.assertTrue(e.isNone());
//        Assert.assertFalse(e.isSome());
//    }

    @Test
    public void OptionNoneisNoneTest1()
    {
        Option<Integer> none = Option.None();
        Assert.assertTrue(none.isNone());
        Assert.assertFalse(none.isSome());
    }

    @Test
    public void OptionNoneisNoneTest2()
    {
        Option<String> none = Option.None();
        Assert.assertTrue(none.isNone());
        Assert.assertFalse(none.isSome());
    }

    @Test
    public void OptionNoneisNoneTest3()
    {
        Option<tmp> none = Option.None();
        Assert.assertTrue(none.isNone());
        Assert.assertFalse(none.isSome());
    }

    // Java doesn't support custom value types at the moment ...
//    struct tmp2{}
//
//    @Test
//    public void OptionNoneisNone()Test4()
//    {
//        var none = Option<tmp2>.None();
//        Assert.assertTrue(none.isNone());
//        Assert.assertFalse(none.isSome());
//    }
//
//    @Test
//    public void OptionBindTest1()
//    {
//        var a = 1.toOption();
//        var b = a.Bind(o => (o*2).toOption());
//        Assert.assertTrue(b.isSome());
//        Assert.assertEquals(2,b.Some());
//    }
//
//    @Test
//    public void OptionBindTest2()
//    {
//        var a = Option<Integer>.None();
//        var b = a.Bind(o => (o * 2).toOption());
//        Assert.assertTrue(b.isNone());
//    }
//
//    @Test
//    public void OptionBindTest3()
//    {
//        var input = new[] {1, 2, 3, 4, 5, 6};
//        Func<int, bool> isEven = i => i%2 == 0;
//        var expected = new[] {2, 4, 6};
//
//        var output = input.Select(i => i.toOption().Bind(j => isEven(j) ? j.toOption() : Option<Integer>.None()));
//        CollectionAssert.AreEquivalent(expected, output.Where(o=>o.isSome()).Select(o=>o.Some()).ToList());
//    }
}
