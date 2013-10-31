package me.shaftesbury.utils.functional.test;

import me.shaftesbury.utils.functional.*;
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
public class FunctionalTest
{
    private static final Functional.Func<Integer,Integer> DoublingGenerator =
            new Functional.Func<Integer,Integer>()
            {
                @Override public Integer apply(Integer a) { return 2*(a + 1);}
            };

    @Test
    public void InitTest1()
    {
        Collection<Integer> output = Functional.init(DoublingGenerator, 5);
        Assert.assertArrayEquals(new Integer[]{2,4,6,8,10}, output.toArray());
    }


    @Test
    public void MapTest1()
    {
        Collection<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        Collection<String> output = Functional.map(Functional.dStringify, input);
        Assert.assertArrayEquals(new String[]{"1","2","3","4","5"},output.toArray());
    }

    @Test
    public void SortWithTest1()
    {
        List<Integer> i = Arrays.asList(new Integer[]{1,6,23,7,4});
        Collection<Integer> output = Functional.sortWith(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer a, Integer b) {
                        return Integer.compare(a, b);
                    }
                }, i);
        Assert.assertArrayEquals(new Integer[]{1,4,6,7,23}, output.toArray());
    }

    @Test
    public void SortWithTest2()
    {
        List<Integer> i = Arrays.asList(new Integer[] { 1, 6, 23, 7, 4 });
        Collection<Integer> j = Functional.sortWith(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer a, Integer b) {
                        return a - b;
                    }
                }, i);
        Assert.assertArrayEquals(new Integer[]{1,4,6,7,23}, j.toArray());
    }

    @Test
    public void SortWithTest3()
    {
        List<Integer> i = Arrays.asList(new Integer[] { 1, 6, 23, 7, 4 });
        Collection<Integer> j = Functional.sortWith(Functional.dSorter, i);
        Assert.assertArrayEquals(new Integer[]{1,4,6,7,23}, j.toArray());
    }

    private static final Functional.Func<Integer, Integer> TriplingGenerator =
            new Functional.Func<Integer, Integer>() {
                @Override
                public Integer apply(Integer a) {
                    return 3 * (a + 1);
                }
            };

    private static final Functional.Func<Integer, Integer> QuadruplingGenerator =
            new Functional.Func<Integer, Integer>() {
                @Override
                public Integer apply(Integer a) {
                    return 4 * (a + 1);
                }
            };


    private static boolean BothAreEven(int a, int b)
    {
        return Functional.IsEven.apply(a) && Functional.IsEven.apply(b);
    }


    @Test
    public void ForAll2Test1()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        Collection<Integer> m = Functional.init(QuadruplingGenerator, 5);
        try
        {
            Assert.assertTrue(Functional.forAll2(
                    new Functional.Func2<Integer, Integer, Boolean>() {
                        @Override
                        public Boolean apply(Integer a, Integer b) {
                            return BothAreEven(a, b);
                        }
                    }, l, m));
        }
        catch (Exception e)
        {}
    }

    private static boolean BothAreLessThan10(int a, int b)
    {
        return a < 10 && b < 10;
    }

    private static Functional.Func2<Integer, Integer, Boolean> dBothAreLessThan10 =
            new Functional.Func2<Integer, Integer, Boolean>() {
    @Override
    public Boolean apply(Integer a, Integer b) {
        return BothAreLessThan10(a, b);
    }
}   ;

    @Test
    public void ForAll2Test2() throws Exception
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        Collection<Integer> m = Functional.init(TriplingGenerator, 5);

        Assert.assertFalse(Functional.forAll2(dBothAreLessThan10
                , l, m));
    }

    @Test(expected=Exception.class)
    public void ForAll2Test3() throws Exception
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        Collection<Integer> m = Functional.init(QuadruplingGenerator, 7);

        Functional.forAll2(
                new Functional.Func2<Integer, Integer, Boolean>() {
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

        boolean allOdd = Functional.forAll(Functional.IsOdd, i);
        boolean notAllOdd = Functional.exists(Functional.not(Functional.IsOdd), i);

        Assert.assertFalse(allOdd);
        Assert.assertTrue(notAllOdd);
    }

    @Test
    public void CompositionTest2() throws Exception
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        Collection<Integer> m = Functional.init(TriplingGenerator, 5);
        Assert.assertFalse(Functional.forAll2(Functional.not2(dBothAreLessThan10), l, m));
        // equivalent to BothAreGreaterThanOrEqualTo10

        final int lowerLimit = 1;
        final int upperLimit = 16;
        Assert.assertFalse(
                Functional.forAll2(
                        Functional.not2(
                                new Functional.Func2<Integer, Integer, Boolean>() {
                                    @Override
                                    public Boolean apply(Integer a, Integer b) {
                                        return a > lowerLimit && b > lowerLimit;
                                    }
                                }
                        ), l, m));
        Assert.assertTrue(
                Functional.forAll2(
                        Functional.not2(
                                new Functional.Func2<Integer, Integer, Boolean>() {
                                    @Override
                                    public Boolean apply(Integer a, Integer b) {
                                        return a > upperLimit && b > upperLimit;
                                    }
                                }
                        ), l, m));
    }

    @Test
    public void PartitionTest1()
    {
        Collection<Integer> m = Functional.init(TriplingGenerator, 5);
        org.javatuples.Pair<Collection<Integer>, Collection<Integer>> r = Functional.partition(Functional.IsOdd, m);

        Integer[] left = {3, 9, 15};
        Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getValue0().toArray());
        Assert.assertArrayEquals(right, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest2()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        org.javatuples.Pair<Collection<Integer>, Collection<Integer>> r = Functional.partition(Functional.IsEven, l);
        Assert.assertArrayEquals(l.toArray(), r.getValue0().toArray());
        Assert.assertArrayEquals(new Integer[]{}, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest3()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        org.javatuples.Pair<Collection<Integer>, Collection<Integer>> r = Functional.partition(Functional.IsEven, l);
        Assert.assertArrayEquals(Functional.filter(Functional.IsEven, l).toArray(), r.getValue0().toArray());
    }

    @Test
    public void ToStringTest1()
    {
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Collection<String> ls = Functional.map(Functional.dStringify, li);
        //String s = String.Join(",", ls);
        Assert.assertArrayEquals(new String[]{"2","4","6","8","10"}, ls.toArray());
    }

    @Test
    public void ChooseTest1B() throws OptionNoValueAccessException
    {
        Collection<Integer> li = Functional.init(TriplingGenerator, 5);
        Collection<String> o = Functional.choose(
                new Functional.Func<Integer, Option<String>>() {
                    @Override
                    public Option<String> apply(Integer i) {
                        return i % 2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
                    }
                }, li);
        String[] expected = {"6", "12"};
        Assert.assertArrayEquals(o.toArray(), expected);
    }

    @Test
    public void ChooseTest2A() //throws OptionNoValueAccessException
    {
        Map<Integer,String> o=null;
        try{
        Collection<Integer> li = Functional.init(TriplingGenerator, 5);
        o = Functional.toDictionary(Functional.<Integer>Identity(), Functional.dStringify,
                Functional.choose(
                        new Functional.Func<Integer, Option<Integer>>() {
                            @Override
                            public Option<Integer> apply(Integer i) {
                                return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        }, li));
        }catch(Exception e){}
        Map<Integer,String> expected = new HashMap<Integer,String>();
        expected.put(6, "6");
        expected.put(12, "12");
        Assert.assertTrue(expected.size()==o.size());
        for(Integer expectedKey : expected.keySet())
        {
            Assert.assertTrue(o.containsKey(expectedKey));
            String expectedValue=expected.get(expectedKey);
            //Assert.assertEquals(expectedValue,o.get(expectedKey),"Expected '"+expectedValue+"' but got '"+o.get(expectedKey)+"'");
            Assert.assertTrue(o.get(expectedKey).equals(expectedValue));
        }
    }

    private final static <B, C>boolean Fn(final B b, final C c)
    {
        return b.equals(c);
    }

    private final static <B, C>Functional.Func<C, Boolean> curried_fn(final B b)
    {
        return new Functional.Func<C, Boolean>() {
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

    private static final Functional.Func<Integer, Integer> curried_adder_int(final int c)
    {
        return new Functional.Func<Integer, Integer>() {
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
        Collection<Integer> b = Functional.map(
                new Functional.Func<Integer, Integer>() {
                    @Override
                    public Integer apply(final Integer a1) {
                        return adder_int(2, a1);
                    }
                }, a);
        Collection<Integer> c = Functional.map(curried_adder_int(2), a);
        Assert.assertArrayEquals(b.toArray(), c.toArray());
    }

    private static String csv(final String state, final Integer a)
    {
        return Functional.isNullOrEmpty(state) ? a.toString() : state + "," + a;
    }

    @Test
    public void FoldvsMapTest1()
    {
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        String s1 = Functional.join(",", Functional.map(Functional.dStringify, li));
        Assert.assertEquals("2,4,6,8,10", s1);
        String s2 = Functional.fold(
                new Functional.Func2<String, Integer, String>() {
                    @Override
                    public String apply(String s1, Integer s2) {
                        return csv(s1, s2);
                    }
                }, "", li);
        Assert.assertEquals(s1, s2);
    }

    private final Functional.Func<Collection<Integer>, String> concatenate =
            new Functional.Func<Collection<Integer>, String>() {
                @Override
                public String apply(Collection<Integer> l) {
                    return Functional.fold(new Functional.Func2<String, Integer, String>() {
                        @Override
                        public String apply(String s1, Integer s2) {
                            return csv(s1, s2);
                        }
                    }, "", l);
                }
            };

    @Test
    public void FwdPipelineTest1()
    {
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        String s1 = Functional.In(li, concatenate);
        Assert.assertEquals("2,4,6,8,10", s1);
    }

    private final Functional.Func<Collection<Integer>, Collection<Integer>> evens_f =
            new Functional.Func<Collection<Integer>, Collection<Integer>>() {
                @Override
                public Collection<Integer> apply(Collection<Integer> l) {
                    return Functional.filter(Functional.IsEven, l);
                }
            };

    @Test
    public void FwdPipelineTest2()
    {
        Collection<Integer> li = Functional.init(TriplingGenerator, 5);
        Collection<Integer> evens = Functional.In(li, evens_f);
        String s1 = Functional.In(evens, concatenate);
        String s2 = Functional.In(li, Functional.Then(evens_f, concatenate));
        Assert.assertEquals("6,12", s1);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void CompositionTest3()
    {
        Collection<Integer> li = Functional.init(TriplingGenerator, 5);
        String s = Functional.In(li, Functional.Then(evens_f, concatenate));
        Assert.assertEquals("6,12", s);
    }

    @Test
    public void CompositionTest4()
    {
        Collection<Integer> li = Functional.init(TriplingGenerator, 5);
        String s = Functional.Then(evens_f, concatenate).apply(li);
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

        Collection<String> indentation = Functional.init(
                new Functional.Func<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return " ";
                    }
                }, level);
        Assert.assertEquals(Functional.join("", indentation), "     ");

        String s = Functional.fold(
                new Functional.Func2<String, String, String>() {
                    @Override
                    public String apply(String state, String str) {
                        return state + str;
                    }
                }, "", indentation);
        Assert.assertEquals(s, expectedResult);

        Functional.Func<Collection<String>, String> folder =
                new Functional.Func<Collection<String>, String>() {
                    @Override
                    public String apply(Collection<String> l) {
                        return Functional.fold(new Functional.Func2<String, String, String>() {
                            @Override
                            public String apply(String state, String str) {
                                return state + str;
                            }
                        }, "", l);
                    }
                };

        String s1 = Functional.In(indentation, folder);
        Assert.assertEquals(s1, expectedResult);
    }

    @Test
    public void IndentTest2()
    {
        final int level = 5;
        final String expectedResult = "     BOB";
        Assert.assertEquals(expectedResult, Functional.indentBy(level, " ", "BOB"));
    }


    @Test
    public void ChooseTest3A() throws OptionNoValueAccessException
    {
        Collection<Integer> li = Functional.init(TriplingGenerator, 5);
        Collection<Integer> o =
                Functional.choose(
                        new Functional.Func<Integer, Option<Integer>>() {
                            public Option<Integer> apply(Integer i) {
                                return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        }, li);

        Integer[] expected = new Integer[]{6,12};
        Assert.assertArrayEquals(expected, o.toArray());
    }

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
        Collection<Integer> li = Functional.init(DoublingGenerator,5);
        Collection<String> ls = Functional.map(Functional.dStringify,li);
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
        Map<Integer, Double> missingPricesPerDate = new Hashtable<Integer, Double>();
        Collection<Integer> openedDays = Functional.init(TriplingGenerator, 5);
        Double last = 10.0;
        for (int day : openedDays)
        {
            Double value = day%2 == 0 ? (Double)((double)(day/2)) : null;
            if (value!=null)
                last = value;
            else
                missingPricesPerDate.put(day, last);
        }

        Collection<myInt> openedDays2 = Functional.init(
                new Functional.Func<Integer, myInt>() {
                    @Override
                    public myInt apply(Integer a) {
                        return new myInt(3 * (a + 1));
                    }
                }, 5);
        Pair<Double, Collection<myInt>> output = Functional.foldAndChoose(
                new Functional.Func2<Double, myInt, Pair<Double, Option<myInt>>>() {
                    @Override
                    public Pair<Double, Option<myInt>> apply(Double state, myInt day) {
                        Double value = day.i() % 2 == 0 ? (Double) ((double) (day.i() / 2)) : null;
                        return value != null
                                ? new Pair<Double, Option<myInt>>(value, Option.<myInt>None())
                                : new Pair<Double, Option<myInt>>(state, Option.toOption(day));
                    }
                }, 10.0, openedDays2);

        Assert.assertEquals(last, output.getValue0());
        List<Integer> keys = new ArrayList<Integer>(missingPricesPerDate.keySet());
        Collections.sort(keys);
        Assert.assertArrayEquals(keys.toArray(),
                Functional.map(
                        new Functional.Func<myInt, Integer>() {
                            @Override
                            public Integer apply(myInt i) {
                                return i.i();
                            }
                        }, output.getValue1()).toArray());
    }

    @Test
    public void joinTest1()
    {
        Collection<Integer> ids = Functional.init(TriplingGenerator, 5);
        final String expected = "3,6,9,12,15";
        Assert.assertEquals(expected, Functional.join(",", Functional.map(Functional.dStringify, ids)));
        Assert.assertEquals(expected, Functional.join(",", ids));
    }

    @Test
    public void joinTest2() throws Exception
    {
        Collection<Integer> ids = Functional.init(TriplingGenerator, 5);
        final String expected = "'3','6','9','12','15'";
        Functional.Func<Integer, String> f =
                new Functional.Func<Integer, String>() {
                    @Override
                    public String apply(Integer id) {
                        return "'" + id + "'";
                    }
                };
        Assert.assertEquals(expected, Functional.join(",", Functional.map(f, ids)));
        Assert.assertEquals(expected, Functional.join(",", ids, f));
    }

    @Test
    public void betweenTest1() throws Exception
    {
        final int lowerBound = 2, upperBound = 4;
        Assert.assertTrue(Functional.between(lowerBound, upperBound, 3));
    }

    @Test
    public void betweenTest2() throws Exception
    {
        final int lowerBound = 2, upperBound = 4;
        Assert.assertFalse(Functional.between(lowerBound, upperBound, 1));
    }

    @Test
    public void betweenTest3() throws Exception
    {
        final double lowerBound = 2.5, upperBound = 2.6;
        Assert.assertTrue(Functional.between(lowerBound, upperBound, 2.55));
    }







    @Test
    public void testIsEven_withEvenNum()
    {
        Assert.assertTrue(Functional.IsEven.apply(2));
    }

    @Test
    public void testIn() throws Exception
    {
        Integer a = 10;
        Assert.assertTrue(Functional.In(a, Functional.IsEven));
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
        Functional.Then(new Functional.Func<Integer,Integer>()
        {
            @Override
            public Integer apply(Integer i) { return }
        })
    } */

/*    @Test
    public void seqFilterTest1()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator,5);
        Iterable<Integer> oddElems = Functional.filter(Functional.IsOdd, l);
        Assert.assertEquals(0, oddElems.Aggregate(0, Functional.dCount));
    }*/

    /*[Test]
    public void seqFilterTest2()
    {
        List<int> l = DoublingGenerator.Repeat(5).ToList();
        Iterable<int> oddElems = Functional.filter(Functional.IsEven, l);
        Assert.assertEquals(5, oddElems.Aggregate(0, Functional.dCount));
    }                      */

    /*[Test]
    public void seqFilterTest3()
    {
        List<Integer> l = DoublingGenerator.Repeat(5).ToList();
        final Integer limit = 5;
        Iterable<Integer> highElems = Functional.filter(
                new Functional.Func<Integer,Boolean>()
                {
                    @Override
                    public Boolean apply(Integer a) { return a > limit;}
                }, l);
        Assert.assertEquals(3, highElems.Aggregate(0, Functional.dCount));
    } */

    @Test(expected=/*Functional.KeyNotFound*/Exception.class)
    public void findLastTest1() throws Exception
    {
        List<Integer> l = new ArrayList<Integer>(Functional.init(DoublingGenerator, 5));
        Assert.assertEquals((Integer) 5, Functional.findLast(Functional.IsOdd, l));
    }

    @Test
    public void findLastTest2() throws Exception
    {
        List<Integer> l = new ArrayList<Integer>(Functional.init(DoublingGenerator, 5));
        Assert.assertEquals((Integer)10, Functional.findLast(Functional.IsEven, l));
    }

    @Test
    public void seqMapTest1() throws Exception
    {
        List<Integer> input = Arrays.asList(new Integer[]{1,2,3,4,5}); //Enumerable.Range(1, 5).ToList();
        List<String> expected = Arrays.asList(new String[] {"1", "2", "3", "4", "5"});
        Iterable<String> output = Functional.seq.map(Functional.dStringify, input);
        Iterator<String> it = output.iterator();
        for(int i=0; i<expected.size(); ++i)
            Assert.assertEquals(expected.get(i), it.next());
    }

    @Test
    public void toArrayTest1() throws Exception
    {
        List<Integer> input = Arrays.asList(new Integer[]{1,2,3,4,5});
        Iterable<String> strs = Functional.seq.map(Functional.dStringify, input);
        List<String> expected = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});

        Object[] output = Functional.toArray(strs);

        Assert.assertEquals(expected.size(),output.length);
        for(int i=0; i<expected.size(); ++i)
            Assert.assertEquals(expected.get(i), output[i]);
    }

    @Test
    public void lastTest1() throws Exception
    {
        List<Integer> input = Arrays.asList(new Integer[]{1,2,3,4,5});
        Assert.assertEquals(5,(long) Functional.last(input));
    }

    @Test
    public void lastTest2() throws Exception
    {
        List<Integer> input = Arrays.asList(new Integer[]{1,2,3,4,5});
        Iterable<String> strs = Functional.seq.map(Functional.dStringify, input);
        Assert.assertEquals("5", Functional.last(strs));
    }

    @Test
    public void concatTest1() throws Exception
    {
        List<Integer> input = Arrays.asList(new Integer[]{1,2,3,4,5});
        List<Integer> expected = Arrays.asList(new Integer[]{1,2,3,4,5,1,2,3,4,5});
        AssertIterable.assertIterableEquals(expected, Functional.concat(input, input));
    }

    @Test
    public void seqConcatTest1() throws Exception
    {
        List<Integer> input = Arrays.asList(new Integer[]{1,2,3,4,5});
        Functional.Func<Integer,Integer> doubler = new Functional.Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer i) {
                return i * 2;
            }
        };
        List<String> expected = Arrays.asList(new String[]{"1","2","3","4","5","2","4","6","8","10"});

        Iterable<String> strs = Functional.seq.map(Functional.dStringify, input);
        Iterable<String> output = Functional.seq.concat(strs, Functional.seq.map(Functional.dStringify, Functional.seq.map(doubler, input)));

        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void arrayIterableTest1() throws Exception
    {
        Integer[] input = new Integer[]{1,2,3,4,5};
        List<Integer> expected = Arrays.asList(new Integer[]{1,2,3,4,5});

        ArrayIterable<Integer> ait = ArrayIterable.create(input);
        List<Integer> output = new ArrayList<Integer>();
        for(Integer i:ait) output.add(i);
        Assert.assertArrayEquals(expected.toArray(), output.toArray());
    }


    /*
            [Test]
        public void seqChooseTest1()
        {
            var li = TriplingGenerator.Repeat(5).ToList();
            var o = li.Choose(i => i%2 == 0 ? i.ToString().ToOption() : Option<string>.None).ToList();
            string[] expected = {"6", "12"};
            CollectionAssert.AreEquivalent(expected,o);
        }

        [Test]
        public void seqInitTest1()
        {
            var output = DoublingGenerator.Repeat(5).ToList();
            CollectionAssert.AreEquivalent(new[]{2,4,6,8,10},output);
        }
     */
}