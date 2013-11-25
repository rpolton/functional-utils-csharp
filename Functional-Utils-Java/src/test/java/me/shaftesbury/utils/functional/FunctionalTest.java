package me.shaftesbury.utils.functional;

import org.javatuples.Pair;
import org.javatuples.Triplet;
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
    private static final Func<Integer,Integer> DoublingGenerator =
            new Func<Integer,Integer>()
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

    private static final Func<Integer, Integer> TriplingGenerator =
            new Func<Integer, Integer>() {
                @Override
                public Integer apply(Integer a) {
                    return 3 * (a + 1);
                }
            };

    private static final Func<Integer, Integer> QuadruplingGenerator =
            new Func<Integer, Integer>() {
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
                    new Func2<Integer, Integer, Boolean>() {
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

    private static Func2<Integer, Integer, Boolean> dBothAreLessThan10 =
            new Func2<Integer, Integer, Boolean>() {
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
                new Func2<Integer, Integer, Boolean>() {
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
                                new Func2<Integer, Integer, Boolean>() {
                                    @Override
                                    public Boolean apply(Integer a, Integer b) {
                                        return a > lowerLimit && b > lowerLimit;
                                    }
                                }
                        ), l, m));
        Assert.assertTrue(
                Functional.forAll2(
                        Functional.not2(
                                new Func2<Integer, Integer, Boolean>() {
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
        org.javatuples.Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.IsOdd, m);

        Integer[] left = {3, 9, 15};
        Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getValue0().toArray());
        Assert.assertArrayEquals(right, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest2()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        org.javatuples.Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.IsEven, l);
        Assert.assertArrayEquals(l.toArray(), r.getValue0().toArray());
        Assert.assertArrayEquals(new Integer[]{}, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest3()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator, 5);
        org.javatuples.Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.IsEven, l);
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
                new Func<Integer, Option<String>>() {
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
                        new Func<Integer, Option<Integer>>() {
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

    private final static <B, C>Func<C, Boolean> curried_fn(final B b)
    {
        return new Func<C, Boolean>() {
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

    private static final Func<Integer, Integer> curried_adder_int(final int c)
    {
        return new Func<Integer, Integer>() {
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
                new Func<Integer, Integer>() {
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
                new Func2<String, Integer, String>() {
                    @Override
                    public String apply(String s1, Integer s2) {
                        return csv(s1, s2);
                    }
                }, "", li);
        Assert.assertEquals(s1, s2);
    }

    private final Func<Collection<Integer>, String> concatenate =
            new Func<Collection<Integer>, String>() {
                @Override
                public String apply(Collection<Integer> l) {
                    return Functional.fold(new Func2<String, Integer, String>() {
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

    private final Func<Collection<Integer>, Collection<Integer>> evens_f =
            new Func<Collection<Integer>, Collection<Integer>>() {
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
                new Func<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return " ";
                    }
                }, level);
        Assert.assertEquals(Functional.join("", indentation), "     ");

        String s = Functional.fold(
                new Func2<String, String, String>() {
                    @Override
                    public String apply(String state, String str) {
                        return state + str;
                    }
                }, "", indentation);
        Assert.assertEquals(s, expectedResult);

        Func<Collection<String>, String> folder =
                new Func<Collection<String>, String>() {
                    @Override
                    public String apply(Collection<String> l) {
                        return Functional.fold(new Func2<String, String, String>() {
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
                        new Func<Integer, Option<Integer>>() {
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
                new Func<Integer, myInt>() {
                    @Override
                    public myInt apply(Integer a) {
                        return new myInt(3 * (a + 1));
                    }
                }, 5);
        Pair<Double, List<myInt>> output = Functional.foldAndChoose(
                new Func2<Double, myInt, Pair<Double, Option<myInt>>>() {
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
                        new Func<myInt, Integer>() {
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
        Func<Integer, String> f =
                new Func<Integer, String>() {
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
        Functional.Then(new Func<Integer,Integer>()
        {
            @Override
            public Integer apply(Integer i) { return }
        })
    } */

    @Test
    public void seqFilterTest1()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator,5);
        Iterable<Integer> oddElems = Functional.seq.filter(Functional.IsOdd, l);

        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
    }

    @Test
    public void seqFilterTest2()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator,5);
        Iterable<Integer> evenElems = Functional.seq.filter(Functional.IsEven, l);

        Collection<Integer> expected = Arrays.asList(new Integer[]{2,4,6,8,10});
        AssertIterable.assertIterableEquals(expected, evenElems);
    }

    @Test
    public void seqFilterTest3()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator,5);
        final Integer limit = 5;
        Iterable<Integer> highElems = Functional.seq.filter(
                new Func<Integer,Boolean>()
                {
                    @Override
                    public Boolean apply(Integer a) { return a > limit;}
                }, l);

        Collection<Integer> expected = Arrays.asList(new Integer[]{6,8,10});
        AssertIterable.assertIterableEquals(expected,highElems);
    }

    @Test
    public void seqFilterTest4()
    {
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        final Integer limit = 10;
        Iterable<Integer> output = Functional.seq.filter(
                new Func<Integer,Boolean>()
                {
                    @Override public Boolean apply(Integer a) {return a > limit;}
                }, li);

        Assert.assertFalse(output.iterator().hasNext());
    }

    @Test
    public void seqFilterTest5()
    {
        Collection<Integer> li = Functional.init(DoublingGenerator, 10);
        Collection<Integer> expected = Arrays.asList(new Integer[]{4,8,12,16,20});
        Iterable<Integer> output = Functional.seq.filter(
                new Func<Integer,Boolean>()
                {
                    @Override public Boolean apply(Integer a) {return a % 4 ==0;}
                }, li);

        AssertIterable.assertIterableEquals(expected, output);
    }

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
        Func<Integer,Integer> doubler = new Func<Integer, Integer>() {
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


        @Test
        public void seqChooseTest1() throws Exception
        {
            Collection<Integer> li = Functional.init(TriplingGenerator,5);
            Iterable<String> output = Functional.seq.choose(
                    new Func<Integer,Option<String>>()
                    {
                            @Override
                            public Option<String> apply(Integer i)
                            {
                                return i%2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
                            }
                    }, li);

            Collection<String> expected = Arrays.asList(new String[]{"6", "12"});
            AssertIterable.assertIterableEquals(expected, output);
        }

        @Test
        public void seqInitTest1()
        {
            Iterable<Integer> output = Functional.seq.init(DoublingGenerator, 5);
            AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2,4,6,8,10}), output);
        }

    @Test
    public void FwdPipelineTest3()
    {
        Collection<Integer> input = Functional.init(DoublingGenerator, 5);
        Collection<String> output = Functional.In(input,
                new Func<Collection<Integer>, Collection<String>>() {
                    @Override
                    public Collection<String> apply(Collection<Integer> integers) {
                        return Functional.map(Functional.dStringify, integers);
                    }
                });

        Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest4()
    {
        Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
        Iterable<String> output = Functional.In(input,
                new Func<Iterable<Integer>, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(Iterable<Integer> integers) {
                        try {
                            return Functional.seq.map(Functional.dStringify, integers);
                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            return null; // Argh!!!
                        }
                    }
                });

        Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest5()
    {
        Iterable<Integer> l = Functional.seq.init(DoublingGenerator, 5);
        Iterable<Integer> oddElems = Functional.In(l,
                new Func<Iterable<Integer>,Iterable<Integer>>(){
                    @Override
                public Iterable<Integer> apply(Iterable<Integer> ints){return Functional.filter(Functional.IsOdd, ints);}});

        Assert.assertFalse(oddElems.iterator().hasNext());
    }
    /*
    private class Test1
    {
        public readonly int i;

        public Test1(int j)
        {
            i = j;
        }
    }

    private static Func<object, string> fn1()
    {
        return delegate(object o)
        {
            if (o.GetType() == typeof (Test1))
                return fn2(o as Test1);
            if (o.GetType() == typeof (string))
                return fn3(o as string);
            return null;
        };
    }

    private static string fn2(Test1 i)
    {
        return i.i.ToString();
    }

    private static string fn3(string s)
    {
        return s;
    }

    [Test]
    public void FwdPipelineTest6()
    {
        Func<object, string> fn = fn1();
        var i = new Test1(10);
        const string s = "test";
        Assert.AreEqual("10", i.In(fn));
        Assert.AreEqual("test", s.In(fn));
    } */

    @Test
    public void seqInitTest2()
    {
        Iterable<Integer> output = Functional.seq.init(DoublingGenerator);
        AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2,4,6,8,10,12,14,16,18,20,22}),Functional.take(11,output));
    }

    @Test
    public void ConstantInitialiserTest1()
    {
        final int howMany = 6;
        final int initValue = -1;
        Collection<Integer> l = Functional.init(Functional.Constant(initValue),howMany);
        Assert.assertEquals(howMany, l.size());
        for(int i : l)
            Assert.assertEquals(initValue, i);
    }

    /*[Test]
    public void SwitchTest1()
    {
        Assert.AreEqual(1,
                Functional.Switch(10,
                        new[]
        {
            Case.ToCase((int a) => a < 5, a => -1),
            Case.ToCase((int a) => a > 5, a => 1)
        }, a => 0));
    } */

    /*[Test]
    public void TryTest1()
    {
        int zero = 0;
        int results = Functional.Try<int,int,DivideByZeroException>(10, a => a/zero, a => a);
        Assert.AreEqual(10, results);
    }*/
    private class A
    {
        public String name;
        public int id;
    }
    /*[Test]
    public void CaseTest2()
    {
        var c1 = new List<Func<A, object>> {(A a) => (object) a.name, (A a) => (object) a.id};

        Func<A, IEnumerable<Func<int, object>>> c2 =
                a => c1.Select<Func<A, object>, Func<int, object>>(f => j => f(a));

        Func<A, IEnumerable<Functional.Case<int, object>>> cases =
                a => c2(a).Select((f, i) => Case.ToCase(i.Equals, f));

        var theA = new A {id = 1, name = "one"};

        IEnumerable<object> results =
                Enumerable.Range(0, 3).Select(i => Functional.Switch(i, cases(theA), aa => "oh dear"));
        var expected = new object[] {"one", 1, "oh dear"};
        CollectionAssert.AreEquivalent(expected, results);
    }*/

    /*[Test]
    public void IgnoreTest1()
    {
        var input = new[] {1, 2, 3, 4, 5};
        var output = new List<string>();
        Func<int, string> format = i => string.Format("Discarding {0}", i);
        List<string> expected = new[] {1, 2, 3, 4, 5}.Select(format).ToList();
        Func<int, bool> f = i =>
        {
            output.Add(format(i));
            return true;
        };
        input.Select(f).Ignore();
        CollectionAssert.AreEquivalent(expected, output);
    }*/
    @Test
    public void ChooseTest1() throws OptionNoValueAccessException
    {
        Collection<Integer> input = Arrays.asList(new Integer[] {1, 2, 3, 4, 5});
        Collection<Integer> expected = Arrays.asList(new Integer[] {1, 3, 5});
        Collection<Integer> output =
                Functional.choose(
                        new Func<Integer, Option<Integer>>() {
                            @Override
                            public Option<Integer> apply(Integer i) {
                                return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        }, input);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void ChooseTest2() throws OptionNoValueAccessException
    {
        Collection<String> input = Arrays.asList(new String[] {"abc", "def"});
        Collection<Character> expected = Arrays.asList(new Character[]{'a'});
        Collection<Character> output =
                Functional.choose(
                        new Func<String, Option<Character>>() {
                            @Override
                            public Option<Character> apply(String str) {
                                return str.startsWith("a") ? Option.toOption('a') : Option.<Character>None();
                            }
                        }
                        ,input
                );
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void ChooseTest3() throws OptionNoValueAccessException
    {
        Collection<Integer> input = Arrays.asList(new Integer[] {1, 2, 3, 4, 5});
        Collection<Integer> expected = Arrays.asList(new Integer[] {1, 3, 5});
        Collection<Integer> output = Functional.choose(
                new Func<Integer, Option<Integer>>() {
                    @Override
                    public Option<Integer> apply(Integer i) {
                        return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
                    }
                }, input)  ;

        AssertIterable.assertIterableEquals(expected, output);
    }

    /*[Test]
    public void CurryTest1()
    {
        Func<int, int, bool> f = (i, j) => i > j;
        Func<int, Func<int, bool>> g = i => j => f(i, j);
        bool t = 10.In(g(5));
        Assert.IsFalse(t);
    }*/

    /*[Test]
    public void CurryTest2()
    {
        Func<int, int, bool> f = (i, j) => i < j;
        Func<int, Func<int, bool>> g = i => j => f(i, j);
        bool t = 10.In(g(5));
        Assert.IsTrue(t);
    }*/

    /*[Test]
    public void CompositionTest1()
    {
        Func<int, int, int> add = (x, y) => x + y;
        Func<int, Func<int, int>> add1 = y => x => add(x, y);
        Func<int, int, int> mult = (x, y) => x*y;
        Func<int, Func<int, int>> mult1 = y => x => mult(x, y);
        int expected = mult(add(1, 2), 3);
        Assert.AreEqual(9, expected);
        Assert.AreEqual(expected, 2.In(add1(1).Then(mult1(3))));
    }*/

    @Test
    public void UnzipTest1()
    {
        Collection<org.javatuples.Pair<String,Integer>> input =
                new ArrayList<org.javatuples.Pair<String,Integer>>();
        input.add(new org.javatuples.Pair<String,Integer>("1", 1));
        input.add(new org.javatuples.Pair<String,Integer>("2", 2));

        org.javatuples.Pair<Collection<String>,Collection<Integer>> expected =
                new org.javatuples.Pair(
                        Arrays.asList(new String[]{"1", "2"}),
                        Arrays.asList(new Integer[]{1,2}));

        org.javatuples.Pair<List<String>,List<Integer>> output = Functional.unzip(input);

        AssertIterable.assertIterableEquals(expected.getValue0(), output.getValue0());
        AssertIterable.assertIterableEquals(expected.getValue1(), output.getValue1());
    }

    @Test
    public void ZipTest1()
    {
        Collection<Integer> input1 = Arrays.asList(new Integer[] {1, 2, 3, 4, 5});
        Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});

        Collection<org.javatuples.Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
        expected.add(new Pair<Integer, Character>(1, 'a'));
        expected.add(new Pair<Integer, Character>(2, 'b'));
        expected.add(new Pair<Integer, Character>(3, 'c'));
        expected.add(new Pair<Integer, Character>(4, 'd'));
        expected.add(new Pair<Integer, Character>(5, 'e'));

        Collection<org.javatuples.Pair<Integer,Character>> output = Functional.zip(input1, input2);

        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void Zip3Test1()
    {
        Collection<Integer> input1 = Arrays.asList(new Integer[] {1, 2, 3, 4, 5});
        Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
        Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});

        Collection<Triplet<Integer,Character,Double>> expected = new ArrayList<Triplet<Integer, Character, Double>>();
        expected.add(new Triplet<Integer, Character, Double>(1, 'a', 1.0));
        expected.add(new Triplet<Integer, Character, Double>(2, 'b', 2.0));
        expected.add(new Triplet<Integer, Character, Double>(3, 'c', 2.5));
        expected.add(new Triplet<Integer, Character, Double>(4, 'd', 3.0));
        expected.add(new Triplet<Integer, Character, Double>(5, 'e', 3.5));

        Collection<Triplet<Integer,Character,Double>> output = Functional.zip3(input1, input2, input3);

        AssertIterable.assertIterableEquals(expected, output);
    }

    /*[ExpectedException(typeof(ArgumentException))]
        [Test]
    public void Zip3Test2()
    {
        var input1 = new[] { 1, 2, 3, 4, 5 };
        var input2 = new[] { 'a', 'b', 'd', 'e' };
        var input3 = new[] { 1.0, 2.0, 2.5, 3.0, 3.5 };
        var expected = new[]
        {
            Tuple.Create(1, 'a', 1.0), Tuple.Create(2, 'b', 2.0), Tuple.Create(3, 'c', 2.5),
                    Tuple.Create(4, 'd', 3.0), Tuple.Create(5, 'e', 3.5)
        }.ToList();

        var output = Functional.Zip3(input1, input2, input3).ToList();

        CollectionAssert.AreEquivalent(expected, output);
    }*/

    /*[ExpectedException(typeof(ArgumentException))]
        [Test]
    public void Zip3Test3()
    {
        var input1 = new[] { 1, 2, 3, 4, 5 };
        var input2 = new[] { 'a', 'b', 'c', 'd', 'e' };
        var input3 = new[] { 1.0, 2.0, 2.5, 3.5 };
        var expected = new[]
        {
            Tuple.Create(1, 'a', 1.0), Tuple.Create(2, 'b', 2.0), Tuple.Create(3, 'c', 2.5),
                    Tuple.Create(4, 'd', 3.0), Tuple.Create(5, 'e', 3.5)
        }.ToList();

        var output = Functional.Zip3(input1, input2, input3).ToList();

        CollectionAssert.AreEquivalent(expected, output);
    }*/

    @Test
    public void findTest1() throws Exception
    {
        final String trueMatch = "6";
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Collection<String> ls = Functional.map(Functional.dStringify, li);
        Assert.assertEquals(trueMatch,
                Functional.find(
                new Func<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) {
                        return s.equals(trueMatch);
                    }
                }, ls));
    }

    @Test(expected = KeyNotFoundException.class)
    public void findTest2() throws Exception
    {
        final String falseMatch = "7";
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Collection<String> ls = Functional.map(Functional.dStringify, li);
        Functional.find(
                new Func<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) {
                        return s.equals(falseMatch);
                    }
                }, ls);
    }

    @Test
    public void findIndexTest1() throws Exception
    {
        final String trueMatch = "6";
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Collection<String> ls = Functional.map(Functional.dStringify, li);
        Assert.assertEquals(2,
                Functional.findIndex(
                        new Func<String, Boolean>() {
                            @Override
                            public Boolean apply(String s) {
                                return s.equals(trueMatch);
                            }
                        }, ls));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIndexTest2() throws Exception
    {
        final String falseMatch = "7";
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Collection<String> ls = Functional.map(Functional.dStringify, li);
        Functional.findIndex(
                new Func<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) {
                        return s.equals(falseMatch);
                    }
                }, ls);
    }

    @Test
    public void pickTest1() throws Exception
    {
        final int trueMatch = 6;
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Assert.assertEquals(((Integer)trueMatch).toString(),
            Functional.pick(
                    new Func<Integer, Option<String>>() {
                        @Override
                        public Option<String> apply(Integer a) {
                            return a == trueMatch ? new Option<String>(a.toString()) : Option.<String>None();
                        }
                    }, li));
    }

    @Test(expected = KeyNotFoundException.class)
    public void pickTest2() throws Exception
    {
        final int falseMatch = 7;
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        Functional.pick(
                new Func<Integer, Option<String>>() {
                    @Override
                    public Option<String> apply(Integer a) {
                        return a == falseMatch ? new Option<String>(a.toString()) : Option.<String>None();
                    }
                }, li);
    }

    @Test
    public void curryFnTest1()
    {
        final int state=0;
        final Func<Integer,Boolean> testForPosInts = new Func<Integer, Boolean>() {
            @Override public Boolean apply(Integer integer) { return integer > state; }
        };

        final Func<Iterable<Integer>,List<Integer>> curriedTestForPosInts = Functional.filter(testForPosInts);

        final Collection<Integer> l = Arrays.asList(new Integer[]{-3,-2,0,1,5});
        final Collection<Integer> posInts = curriedTestForPosInts.apply(l);

        final Collection<Integer> expected = Arrays.asList(new Integer[]{1,5});
        AssertIterable.assertIterableEquals(expected, posInts);
    }

    @Test
    public void MapDictTest1()
    {
        Collection<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        Map<String,String> output = Functional.map_dict(
                new Func<Integer, Map.Entry<String, String>>() {
                    @Override
                    public Map.Entry<String, String> apply(final Integer i) {
                        return new Map.Entry<String,String>(){
                            public String setValue(String v){throw new UnsupportedOperationException(); }
                            public String getValue() { return Functional.dStringify.apply(i); }
                            public String getKey() { return Functional.dStringify.apply(i); }
                        };
                    }
                }, input);

        List<String> keys = new ArrayList<String>(output.keySet());
        Collections.sort(keys);
        AssertIterable.assertIterableEquals(Arrays.asList(new String[]{"1","2","3","4","5"}),keys);
    }

    @Test
    public void ToListTest1()
    {
        Iterable<Integer> output = Functional.init(DoublingGenerator, 5);
        List<Integer> output_ints = Functional.toList(output);
        AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2,4,6,8,10}), output_ints);
    }

    public static final Func<Integer,Iterable<Integer>> intToList(final int howMany)
    {
        return new Func<Integer, Iterable<Integer>>() {
            @Override
            public Iterable<Integer> apply(final Integer integer) {
                return Functional.init(
                        new Func<Integer, Integer>() {
                            @Override
                            public Integer apply(Integer counter) {
                                return integer;
                            }
                        }, howMany);
            }
        };
    }

    @Test
    public void CollectTest1()
    {
        Iterable<Integer> input = Functional.init(DoublingGenerator, 5);
        List<Integer> output = Functional.collect(intToList(3), input);
        List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void seqCollectTest1()
    {
        Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
        Iterable<Integer> output = Functional.seq.collect(intToList(3), input);
        List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void seqCollectTest2()
    {
        Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
        Iterable<Integer> output1 = Functional.seq.collect(intToList(3), input);
        Iterable<Integer> output2 = output1;
        List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output1);
        AssertIterable.assertIterableEquals(expected, output2);
    }

    @Test
    public void takeNandYieldTest1()
    {
        Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
        me.shaftesbury.utils.functional.Pair<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input,2);
        List<Integer> expectedList = Arrays.asList(2,4);
        List<Integer> expectedRemainder = Arrays.asList(6,8,10);
        AssertIterable.assertIterableEquals(expectedList,output.getValue0());
        AssertIterable.assertIterableEquals(expectedRemainder,output.getValue1());
    }

    @Test
    public void takeNandYieldTest2()
    {
        Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
        me.shaftesbury.utils.functional.Pair<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input,0);
        List<Integer> expectedList = Arrays.asList();
        List<Integer> expectedRemainder = Arrays.asList(2,4,6,8,10);
        AssertIterable.assertIterableEquals(expectedList,output.getValue0());
        AssertIterable.assertIterableEquals(expectedRemainder,output.getValue1());
    }

    @Test
    public void recFilterTest1()
    {
        Collection<Integer> l = Functional.init(DoublingGenerator,5);
        Iterable<Integer> oddElems = Functional.rec.filter(Functional.IsOdd, l);

        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
    }

    @Test
    public void recMapTest1()
    {
        Collection<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        Iterable<String> output = Functional.rec.map(Functional.dStringify, input);
        AssertIterable.assertIterableEquals(Arrays.asList("1","2","3","4","5"),output);
    }

    @Test
    public void recFoldvsMapTest1()
    {
        Collection<Integer> li = Functional.init(DoublingGenerator, 5);
        String s1 = Functional.join(",", Functional.rec.map(Functional.dStringify, li));
        Assert.assertEquals("2,4,6,8,10", s1);
        String s2 = Functional.rec.fold(
                new Func2<String, Integer, String>() {
                    @Override
                    public String apply(String s1, Integer s2) {
                        return csv(s1, s2);
                    }
                }, "", li);
        Assert.assertEquals(s1, s2);
    }
}