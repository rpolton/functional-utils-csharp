package me.shaftesbury.utils.test;

import me.shaftesbury.utils.Option;
import me.shaftesbury.utils.functional;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 16/10/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class functionalTest
{
    private static final functional.Func<Integer,Integer> DoublingGenerator =
            new functional.Func<Integer,Integer>()
            {
                @Override public Integer apply(Integer a) { return 2*(a + 1);}
            };

    @Test
    public void InitTest1()
    {
        Collection<Integer> output = functional.init(DoublingGenerator, 5);
        Assert.assertArrayEquals(new Integer[]{2,4,6,8,10}, output.toArray());
    }


    @Test
    public void MapTest1()
    {
        Collection<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        Collection<String> output = functional.map(functional.dStringify, input);
        Assert.assertArrayEquals(new String[]{"1","2","3","4","5"},output.toArray());
    }

    @Test
    public void SortWithTest1()
    {
        List<Integer> i = Arrays.asList(new Integer[]{1,6,23,7,4});
        Collection<Integer> output = functional.sortWith(
                new Comparator<Integer>()
                {
                    @Override public int compare(Integer a, Integer b) { return Integer.compare(a,b);}
                }, i);
        Assert.assertArrayEquals(new Integer[]{1,4,6,7,23}, output.toArray());
    }

    @Test
    public void SortWithTest2()
    {
        List<Integer> i = Arrays.asList(new Integer[] { 1, 6, 23, 7, 4 });
        Collection<Integer> j = functional.sortWith(
                new Comparator<Integer>(){@Override public int compare(Integer a, Integer b) { return a - b;}}, i);
        Assert.assertArrayEquals(new Integer[]{1,4,6,7,23}, j.toArray());
    }

    @Test
    public void SortWithTest3()
    {
        List<Integer> i = Arrays.asList(new Integer[] { 1, 6, 23, 7, 4 });
        Collection<Integer> j = functional.sortWith(functional.dSorter, i);
        Assert.assertArrayEquals(new Integer[]{1,4,6,7,23}, j.toArray());
    }

    private static final functional.Func<Integer, Integer> TriplingGenerator =
            new functional.Func<Integer, Integer>() {
                @Override
                public Integer apply(Integer a) {
                    return 3 * (a + 1);
                }
            };

    private static final functional.Func<Integer, Integer> QuadruplingGenerator =
            new functional.Func<Integer, Integer>() {
                @Override
                public Integer apply(Integer a) {
                    return 4 * (a + 1);
                }
            };


    private static boolean BothAreEven(int a, int b)
    {
        return functional.IsEven.apply(a) && functional.IsEven.apply(b);
    }


    @Test
    public void ForAll2Test1()
    {
        Collection<Integer> l = functional.init(DoublingGenerator,5);
        Collection<Integer> m = functional.init(QuadruplingGenerator,5);
        try
        {
            Assert.assertTrue(functional.forAll2(
                    new functional.Func2<Integer,Integer,Boolean>()
                    {
                        @Override public Boolean apply(Integer a, Integer b){return BothAreEven(a,b);}
                    }, l, m));
        }
        catch (Exception e)
        {}
    }

    private static boolean BothAreLessThan10(int a, int b)
    {
        return a < 10 && b < 10;
    }

    private static functional.Func2<Integer, Integer, Boolean> dBothAreLessThan10 =
            new functional.Func2<Integer, Integer, Boolean>() {
    @Override
    public Boolean apply(Integer a, Integer b) {
        return BothAreLessThan10(a, b);
    }
}   ;

    @Test
    public void ForAll2Test2() throws Exception
    {
        Collection<Integer> l = functional.init(DoublingGenerator, 5);
        Collection<Integer> m = functional.init(TriplingGenerator, 5);

        Assert.assertFalse(functional.forAll2(     dBothAreLessThan10
, l, m));
    }

    @Test(expected=Exception.class)
    public void ForAll2Test3() throws Exception
    {
        Collection<Integer> l = functional.init(DoublingGenerator, 5);
        Collection<Integer> m = functional.init(QuadruplingGenerator, 7);

        functional.forAll2(
            new functional.Func2<Integer, Integer, Boolean>() {
                @Override
                public Boolean apply(Integer a, Integer b) {
                    return BothAreEven(a, b);
                }
            }, l, m);
    }

    @Test
    public void CompositionTest1A()
    {
        Collection<Integer> i = Arrays.asList(1, 2, 3, 45, 56, 6);

        boolean allOdd = functional.forAll(functional.IsOdd, i);
        boolean notAllOdd = functional.exists(functional.not(functional.IsOdd), i);

        Assert.assertFalse(allOdd);
        Assert.assertTrue(notAllOdd);
    }

    @Test
    public void CompositionTest2() throws Exception
    {
        Collection<Integer> l = functional.init(DoublingGenerator, 5);
        Collection<Integer> m = functional.init(TriplingGenerator, 5);
        Assert.assertFalse(functional.forAll2(functional.not2(dBothAreLessThan10), l, m));
        // equivalent to BothAreGreaterThanOrEqualTo10

        final int lowerLimit = 1;
        final int upperLimit = 16;
        Assert.assertFalse(
                functional.forAll2(
                        functional.not2(
                                new functional.Func2<Integer, Integer, Boolean>()
                                {
                                    @Override public Boolean apply(Integer a, Integer b)
                                    {
                                        return a > lowerLimit && b > lowerLimit;
                                    }
                                }
                        ), l, m));
        Assert.assertTrue(
                functional.forAll2(
                        functional.not2(
                                new functional.Func2<Integer,Integer,Boolean>()
                                {
                                    @Override public Boolean apply(Integer a, Integer b)
                                    {
                                        return a > upperLimit && b > upperLimit;
                                    }
                                }
                        ),l,m));
    }

    @Test
    public void PartitionTest1()
    {
        Collection<Integer> m = functional.init(TriplingGenerator, 5);
        org.javatuples.Pair<Collection<Integer>, Collection<Integer>> r = functional.partition(functional.IsOdd, m);

        Integer[] left = {3, 9, 15};
        Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getValue0().toArray());
        Assert.assertArrayEquals(right, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest2()
    {
        Collection<Integer> l = functional.init(DoublingGenerator, 5);
        org.javatuples.Pair<Collection<Integer>, Collection<Integer>> r = functional.partition(functional.IsEven, l);
        Assert.assertArrayEquals(l.toArray(), r.getValue0().toArray());
        Assert.assertArrayEquals(new Integer[]{}, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest3()
    {
        Collection<Integer> l = functional.init(DoublingGenerator, 5);
        org.javatuples.Pair<Collection<Integer>, Collection<Integer>> r = functional.partition(functional.IsEven, l);
        Assert.assertArrayEquals(functional.filter(functional.IsEven, l).toArray(), r.getValue0().toArray());
    }

    @Test
    public void ToStringTest1()
    {
        Collection<Integer> li = functional.init(DoublingGenerator,5);
        Collection<String> ls = functional.map(functional.dStringify,li);
        //String s = String.Join(",", ls);
        Assert.assertArrayEquals(new String[]{"2","4","6","8","10"}, ls.toArray());
    }

    @Test
    public void ChooseTest1B()
    {   /*
        Collection<Integer> li = functional.init(TriplingGenerator, 5);
        Collection<String> o = functional.choose(i => i%2 == 0 ? i.ToString().ToOption() : Option<string>.None, li);
        String[] expected = {"6", "12"};
        Assert.assertArrayEquals(o.toArray(), expected);        */
    }

    @Test
    public void ChooseTest2A()
    {    /*
        List<int> li = TriplingGenerator.Repeat(5).ToList();
        var o = li.Choose( i => i%2 == 0 ? i.ToOption() : Option<int>.None).ToDictionary(i => i, i => i.ToString());
        var expected = new Dictionary<int, string>();
        expected[6] = "6";
        expected[12] = "12";
        CollectionAssert.AreEquivalent(o, expected);*/
    }

    private final static <B, C>boolean Fn(final B b, final C c)
    {
        return b.equals(c);
    }

    private final static <B, C>functional.Func<C, Boolean> curried_fn(final B b)
    {
        return new functional.Func<C, Boolean>() {
            @Override
            public Boolean apply(C c) {
                return Fn(b,c);
            }
        };
    }

    @Test
    public void CurriedFnTest1()
    {
        boolean test1a = Fn(1, 2);
        boolean test1b = curried_fn(1).apply(2);
        Assert.assertEquals(test1a, test1b);
    }

    private static int adder_int(int left, int right) { return left + right; }

    private static final functional.Func<Integer, Integer> curried_adder_int(final int c)
    {
        return new functional.Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer p) {
                return adder_int(c, p);
            }
        };
    }

    @Test
    public void CurriedFnTest2()
    {
        List<Integer> a = Arrays.asList(1, 2, 3, 4, 5);
        Collection<Integer> b = functional.map(
        new functional.Func<Integer, Integer>() {
            @Override
            public Integer apply(final Integer a1) {
                return adder_int(2, a1);
            }
        }, a);
        Collection<Integer> c = functional.map(curried_adder_int(2), a);
        Assert.assertArrayEquals(b.toArray(), c.toArray());
    }

    private static String csv(final String state, final Integer a)
    {
        return functional.isNullOrEmpty(state) ? a.toString() : state + "," + a;
    }

    @Test
    public void FoldvsMapTest1()
    {
        Collection<Integer> li = functional.init(DoublingGenerator, 5);
        String s1 = functional.join(",", functional.map(functional.dStringify, li));
        Assert.assertEquals("2,4,6,8,10", s1);
        String s2 = functional.fold(
                new functional.Func2<String, Integer, String>() {
                    @Override
                    public String apply(String s1, Integer s2) {
                        return csv(s1,s2);
                    }
                }, "", li);
        Assert.assertEquals(s1, s2);
    }

    private final functional.Func<Collection<Integer>, String> concatenate =
            new functional.Func<Collection<Integer>, String>() {
                @Override
                public String apply(Collection<Integer> l) {
                    return functional.fold(new functional.Func2<String, Integer, String>() {
                        @Override
                        public String apply(String s1, Integer s2) {
                            return csv(s1,s2);
                        }
                    },"",l);
                }
            };

    @Test
    public void FwdPipelineTest1()
    {
        Collection<Integer> li = functional.init(DoublingGenerator,5);
        String s1 = functional.In(li,concatenate);
        Assert.assertEquals("2,4,6,8,10", s1);
    }

    private final functional.Func<Collection<Integer>, Collection<Integer>> evens_f =
            new functional.Func<Collection<Integer>, Collection<Integer>>() {
                @Override
                public Collection<Integer> apply(Collection<Integer> l) {
                    return functional.filter(functional.IsEven,l);
                }
            };

    @Test
    public void FwdPipelineTest2()
    {
        Collection<Integer> li = functional.init(TriplingGenerator, 5);
        Collection<Integer> evens = functional.In(li,evens_f);
        String s1 = functional.In(evens,concatenate);
        String s2 = functional.In(li,functional.Then(evens_f,concatenate));
        Assert.assertEquals("6,12", s1);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void CompositionTest3()
    {
        Collection<Integer> li = functional.init(TriplingGenerator, 5);
        String s = functional.In(li,functional.Then(evens_f,concatenate));
        Assert.assertEquals("6,12", s);
    }

    @Test
    public void CompositionTest4()
    {
        Collection<Integer> li = functional.init(TriplingGenerator, 5);
        String s = functional.Then(evens_f,concatenate).apply(li);
        Assert.assertEquals("6,12", s);
    }

    @Test
    public void IndentTest1()
    {
        final int level = 5;
        final String expectedResult = "     ";

        String indentedName = "";
        for (int i = 0; i < level; ++i)
        {
            indentedName += " ";
        }
        Assert.assertEquals(indentedName, expectedResult);

        Collection<String> indentation = functional.init(
                new functional.Func<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return " ";
                    }
                }, level);
        Assert.assertEquals(functional.join("", indentation), "     ");

        String s = functional.fold(
                new functional.Func2<String, String, String>() {
                    @Override
                    public String apply(String state, String str) {
                        return state + str;
                    }
                }, "", indentation);
        Assert.assertEquals(s, expectedResult);

        functional.Func<Collection<String>, String> folder =
                new functional.Func<Collection<String>, String>() {
                    @Override
                    public String apply(Collection<String> l) {
                        return functional.fold(new functional.Func2<String, String, String>() {
                            @Override
                            public String apply(String state, String str) {
                                return state + str;
                            }
                        }, "", l);
                    }
                };

        String s1 = functional.In(indentation,folder);
        Assert.assertEquals(s1, expectedResult);
    }

    @Test
    public void IndentTest2()
    {
        final int level = 5;
        final String expectedResult = "     BOB";
        Assert.assertEquals(expectedResult, functional.indentBy(level, " ", "BOB"));
    }

    /*
    [Test]
    public void ChooseTest3A()
    {
        List<int> li = TriplingGenerator.Repeat(5).ToList();
        List<int> o = li.Choose(i => i%2 == 0 ? i.ToOption() : Option<int>.None).ToList();
        Assert.AreEqual(o[0], 6);
        Assert.AreEqual(o[1], 12);
    }
*/
/*
        [Test]
        public void TryGetValueTest1()
        {
            var d = new Dictionary<string, int>();
            d["one"] = 1;
            Assert.AreEqual(1, FunctionalHelpers.TryGetValue_nullable("one", d));
        }

 */

    /*
            [Test]
        public void TryGetValueTest2()
        {
            var d = new Dictionary<string, int>();
            d["one"] = 1;
            Assert.IsNull(FunctionalHelpers.TryGetValue_nullable("two", d));
        }

     */
/*
        [Test]
        public void TryGetValueTest3()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.AreEqual("ONE", FunctionalHelpers.TryGetValue("one", d).Some);
        }

 */

    /*
            [Test]
        public void TryGetValueTest4()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.IsTrue(FunctionalHelpers.TryGetValue("two", d).None);
        }

     */

    /*
            [Test]
        public void TryGetValueTest5()
        {
            var d = new Dictionary<string, List<int>>();
            var l = new List<int>(new[] {1, 2, 3});
            d["one"] = l;
            Assert.AreEqual(l, FunctionalHelpers.TryGetValue("one", d).Some);
        }
*/
    /*
        [Test]
        public void TryGetValueTest6()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.IsTrue(FunctionalHelpers.TryGetValue("two", d).None);
        }

     */
/*
    @Test
    public void find_noExceptTest1()
    {
        final String trueMatch = "6";
        Collection<Integer> li = functional.init(DoublingGenerator,5);
        Collection<String> ls = functional.map(functional.dStringify,li);
        Assert.assertTrue(FunctionalHelpers.find_noExcept(a => a == trueMatch, ls).Some == trueMatch);
    }
  */
    /*
            [Test]
        public void find_noExceptTest2()
        {
            const string falseMatch = "7";
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            List<string> ls = li.Select(Functional.dStringify).ToList();
            Assert.IsTrue(FunctionalHelpers.find_noExcept(a => a == falseMatch, ls).None);
        }

     */

/*
        [Test]
        public void pick_noExceptTest1()
        {
            const string trueMatch = "6";
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            List<string> ls = li.Select(Functional.dStringify).ToList();
            Assert.IsTrue(
                FunctionalHelpers.pick_noExcept(
                    a => a == trueMatch ? a : OptionType<string>.Null, ls).Some == trueMatch);
        }

        [Test]
        public void pick_noExceptTest2()
        {
            const string falseMatch = "7";
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            List<string> ls = li.Select(Functional.dStringify).ToList();
            Assert.IsTrue(
                FunctionalHelpers.pick_noExcept(
                    a => a == falseMatch ? a : OptionType<string>.Null, ls).None);
        }

 */

    private class myInt
    {
        private final int _i;

        public myInt(int i)
        {
            _i = i;
        }

        public final int i()
        {
            return _i;
        }
    }


    @Test
    public void foldAndChooseTest1() throws Exception
    {
        Dictionary<Integer, Double> missingPricesPerDate = new Hashtable<Integer, Double>();
        Collection<Integer> openedDays = functional.init(TriplingGenerator, 5);
        Double last = 10.0;
        for (int day : openedDays)
        {
            Double value = day%2 == 0 ? (Double)((double)(day/2)) : null;
            if (value!=null)
                last = value;
            else
                missingPricesPerDate.put(day, last);
        }

        Collection<myInt> openedDays2 = functional.init(
                new functional.Func<Integer, myInt>() {
                    @Override
                    public myInt apply(Integer a) {
                        return new myInt(3 * (a + 1));
                    }
                }, 5);
        Pair<Double, Collection<myInt>> output = functional.foldAndChoose(
                new functional.Func2<Double, myInt, Pair<Double, Option<myInt>>>() {
                    @Override
                    public Pair<Double, Option<myInt>> apply(Double state, myInt day) {
                        Double value = day.i() % 2 == 0 ? (Double) ((double) (day.i() / 2)) : null;
                        return value != null
                                ? new Pair<Double, Option<myInt>>(value, Option.<myInt>None())
                                : new Pair<Double, Option<myInt>>(state, Option.toOption(day));
                    }
                }, 10.0, openedDays2);

        Assert.assertEquals(last, output.getValue0());
        List<Integer> keys = functional.convert(missingPricesPerDate.keys());
        Collections.sort(keys);
        Assert.assertArrayEquals(keys.toArray(),
                functional.map(
                        new functional.Func<myInt, Integer>() {
                            @Override
                            public Integer apply(myInt i) {
                                return i.i();
                            }
                        }, output.getValue1()).toArray());
    }

    @Test
    public void joinTest1()
    {
        Collection<Integer> ids = functional.init(TriplingGenerator, 5);
        final String expected = "3,6,9,12,15";
        Assert.assertEquals(expected, functional.join(",", functional.map(functional.dStringify, ids)));
        Assert.assertEquals(expected, functional.join(",", ids));
    }

    @Test
    public void joinTest2() throws Exception
    {
        Collection<Integer> ids = functional.init(TriplingGenerator, 5);
        final String expected = "'3','6','9','12','15'";
        functional.Func<Integer, String> f =
                new functional.Func<Integer, String>() {
                    @Override
                    public String apply(Integer id) {
                        return "'" + id + "'";
                    }
                };
        Assert.assertEquals(expected, functional.join(",", functional.map(f, ids)));
        Assert.assertEquals(expected, functional.join(",", ids, f));
    }

    @Test
    public void betweenTest1() throws Exception
    {
        final int lowerBound = 2, upperBound = 4;
        Assert.assertTrue(functional.between(lowerBound, upperBound, 3));
    }

    @Test
    public void betweenTest2() throws Exception
    {
        final int lowerBound = 2, upperBound = 4;
        Assert.assertFalse(functional.between(lowerBound, upperBound, 1));
    }

    @Test
    public void betweenTest3() throws Exception
    {
        final double lowerBound = 2.5, upperBound = 2.6;
        Assert.assertTrue(functional.between(lowerBound, upperBound, 2.55));
    }







    @Test
    public void testIsEven_withEvenNum()
    {
        Assert.assertTrue(functional.IsEven.apply(2));
    }

    @Test
    public void testIn() throws Exception
    {
        Integer a = 10;
        Assert.assertTrue(functional.In(a,functional.IsEven));
    }


    /*@Test
    public void testThen() throws Exception
    {
        // mult(two,three).Then(add(four)) =>
        // Then(mult(two,three),add(four))
        // 2 * 3 + 4 = 10
        Integer two = 2;
        Integer three = 3;
        Integer four = 4;
        functional.Then(new functional.Func<Integer,Integer>()
        {
            @Override
            public Integer apply(Integer i) { return }
        })
    } */

/*    @Test
    public void seqFilterTest1()
    {
        Collection<Integer> l = functional.init(DoublingGenerator,5);
        Iterable<Integer> oddElems = functional.filter(functional.IsOdd, l);
        Assert.assertEquals(0, oddElems.Aggregate(0, functional.dCount));
    }*/

    /*[Test]
    public void seqFilterTest2()
    {
        List<int> l = DoublingGenerator.Repeat(5).ToList();
        Iterable<int> oddElems = functional.filter(functional.IsEven, l);
        Assert.assertEquals(5, oddElems.Aggregate(0, functional.dCount));
    }                      */

    /*[Test]
    public void seqFilterTest3()
    {
        List<Integer> l = DoublingGenerator.Repeat(5).ToList();
        final Integer limit = 5;
        Iterable<Integer> highElems = functional.filter(
                new functional.Func<Integer,Boolean>()
                {
                    @Override
                    public Boolean apply(Integer a) { return a > limit;}
                }, l);
        Assert.assertEquals(3, highElems.Aggregate(0, Functional.dCount));
    } */

    @Test(expected=/*functional.KeyNotFound*/Exception.class)
    public void findLastTest1() throws Exception
    {
        List<Integer> l = new ArrayList<Integer>(functional.init(DoublingGenerator,5));
        Assert.assertEquals((Integer) 5, functional.findLast(functional.IsOdd, l));
    }

    @Test
    public void findLastTest2() throws Exception
    {
        List<Integer> l = new ArrayList<Integer>(functional.init(DoublingGenerator,5));
        Assert.assertEquals((Integer)10, functional.findLast(functional.IsEven, l));
    }


}