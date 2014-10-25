package me.shaftesbury.utils.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

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
        final Option<String> a = Option.toOption("TEST");
        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void equalityTest2()
    {
        final Option<String> a = Option.toOption("TEST");
        final Option<String> b = Option.toOption("TEST");
        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void equalityTest3()
    {
        final Option<Integer> a = Option.toOption(1);
        final Option<Integer> b = Option.toOption(2);
        Assert.assertTrue(a.equals(a));
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void OptionTestValueType1()
    {
        final int expected = 10;
        final Option<Integer> a = Option.toOption(expected);
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        Assert.assertEquals((Integer)expected, a.Some());
    }

    @Test
    public void OptionTestStringType1()
    {
        final String expected = "ll";
        final Option<String> a = Option.toOption(expected);
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        Assert.assertEquals(expected, a.Some());
    }

    @Test
    public void OptionTestValueType2()
    {
        final Option<Integer> a = Option.None();
        Assert.assertTrue(a.isNone());
        Assert.assertFalse(a.isSome());
    }

    class tmp{}

    @Test
    public void OptionTestReferenceType1()
    {
        final Option<tmp> a = Option.toOption(new tmp());
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        a.Some();
    }

    @Test
    public void OptionTestReferenceType2()
    {
        final Option<tmp> a = Option.<tmp>toOption(null);
        Assert.assertTrue(a.isNone());
        Assert.assertFalse(a.isSome());
    }

    @Test(expected = OptionNoValueAccessException.class)
    public void OptionTestReferenceType3()
    {
        final Option<tmp> a = Option.<tmp>toOption(null);
        a.Some();
    }

    @Test
    public void compareOptionsForEqualityTest1()
    {
        final Option<Integer> some1 = Option.toOption(1);
        final Option<Integer> some2 = Option.toOption(1);

        Assert.assertEquals(some1, some2);
    }

    @Test
    public void compareOptionsForEqualityTest2()
    {
        final Option<Integer> some1 = Option.toOption(1);
        final Option<Integer> some2 = Option.toOption(2);

        Assert.assertNotEquals(some1, some2);
    }

    @Test
    public void compareOptionsForEqualityTest3()
    {
        final Option<Integer> some1 = Option.toOption(1);
        final Option<Double> some2 = Option.toOption(2.1);

        Assert.assertNotEquals(some1, some2);
    }

    @Test
    public void compareOptionsForEqualityTest4()
    {
        final Option<Integer> none1 = Option.None();
        final Option<Integer> none2 = Option.None();

        Assert.assertEquals(none1, none2);
    }

    @Test
    public void compareOptionsForEqualityTest5()
    {
        final Option<Integer> some = Option.toOption(2);
        final Integer theInt = 2;

        Assert.assertNotEquals(some,theInt);
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
        final Option<Integer> none = Option.None();
        Assert.assertTrue(none.isNone());
        Assert.assertFalse(none.isSome());
    }

    @Test
    public void OptionNoneisNoneTest2()
    {
        final Option<String> none = Option.None();
        Assert.assertTrue(none.isNone());
        Assert.assertFalse(none.isSome());
    }

    @Test
    public void OptionNoneisNoneTest3()
    {
        final Option<tmp> none = Option.None();
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
    @Test
    public void OptionBindTest1()
    {
        final Option<Integer> a = Option.toOption(1);
        final Option<Integer> b = a.bind(
                new Func<Integer, Option<Integer>>() {
                    @Override
                    public Option<Integer> apply(Integer integer) {
                        return Option.toOption(integer*2);
                    }
                });
        Assert.assertTrue(b.isSome());
        Assert.assertEquals(new Integer(2),b.Some());
    }

    @Test
    public void OptionBindTest2()
    {
        final Option<Integer> a = Option.<Integer>None();
        final Option<Integer> b = a.bind(
                new Func<Integer, Option<Integer>>() {
                    @Override
                    public Option<Integer> apply(Integer integer) {
                        return Option.toOption(integer*2);
                    }
                });
        Assert.assertTrue(b.isNone());
    }

    @Test
    public void OptionBindTest3()
    {
        final Iterable2<Integer> input = IterableHelper.asList(1, 2, 3, 4, 5, 6);
        final java.util.List<Integer> expected = Arrays.asList(2, 4, 6);

        // Note that this really ought to be an example of 'choose' but we use bind here to exercise the code ;-)

        final Iterable2<Option<Integer>> output = input.map(
                new Func<Integer, Option<Integer>>() {
                    @Override
                    public Option<Integer> apply(Integer integer) {
                        return Option.toOption(integer).bind(
                                new Func<Integer,Option<Integer>>(){
                                    public Option<Integer> apply(final Integer i) {
                                        return Functional.isEven.apply(i) ? Option.toOption(i) : Option.<Integer>None();
                                    } }); } } );


        AssertIterable.assertIterableEquals(expected,output.choose(
                new Func<Option<Integer>, Option<Integer>>() {
                    @Override
                    public Option<Integer> apply(Option<Integer> integerOption) {
                        return integerOption;
                    }
                }
        ));
    }

    private final static Func2<Integer,Integer,Integer> plus = new Func2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer1, Integer integer2) {
            return integer1 + integer2;
        }
    };

    @Test
    public void OptionLiftTest1()
    {
        final Option<Integer> a = Option.toOption(10);
        final Option<Integer> b = Option.toOption(100);
        final Option<Integer> c = Option.lift(plus,a,b);

        Assert.assertTrue(c.isSome());
        Assert.assertEquals(new Integer(110), c.Some());
    }

    @Test
    public void OptionLiftTest2()
    {
        final Option<Integer> a = Option.toOption(10);
        final Option<Integer> b = Option.<Integer>None();
        final Option<Integer> c = Option.lift(plus,a,b);

        Assert.assertTrue(c.isNone());
    }

    @Test
    public void OptionLiftTest3()
    {
        final Option<Integer> a = Option.<Integer>None();
        final Option<Integer> b = Option.toOption(10);
        final Option<Integer> c = Option.lift(plus,a,b);

        Assert.assertTrue(c.isNone());
    }

    @Test
    public void OptionLiftTest4()
    {
        final Option<Integer> a = Option.<Integer>None();
        final Option<Integer> b = Option.<Integer>None();
        final Option<Integer> c = Option.lift(plus,a,b);

        Assert.assertTrue(c.isNone());
    }

    @Test
    public void OptionToStringTest()
    {
        final Option<String> a = Option.toOption("string");
        final Option<Integer> b = Option.toOption(10);
        final Option<Boolean> c = Option.toOption(false);
        final Option<String> d = Option.None();

        Assert.assertEquals("Option( string )",a.toString());
        Assert.assertEquals("Option( 10 )",b.toString());
        Assert.assertEquals("Option( false )",c.toString());
        Assert.assertEquals("None",d.toString());
    }

    @Test
    public void OptionSomeCreatorTestValueType1()
    {
        final int expected = 10;
        final Option<Integer> a = Option.Some(expected);
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        Assert.assertEquals((Integer)expected, a.Some());
    }

    @Test
    public void OptionSomeCreatorTestStringType1()
    {
        final String expected = "ll";
        final Option<String> a = Option.Some(expected);
        Assert.assertTrue(a.isSome());
        Assert.assertFalse(a.isNone());
        Assert.assertEquals(expected, a.Some());
    }

    @Test(expected = OptionNoValueAccessException.class)
    public void OptionSomeCreatorTestValueType2()
    {
        final Option<Integer> a = Option.Some(null);
    }

}
