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
public class Iterable2Test
{
    private static final Func<Integer,Integer> DoublingGenerator =
            new Func<Integer,Integer>()
            {
                @Override public Integer apply(Integer a) { return 2*(a + 1);}
            };

    @Test
    public void InitTest1()
    {
        Iterable2<Integer> output = IterableHelper.init(DoublingGenerator, 5);
        AssertIterable.assertIterableEquals(Arrays.asList(2,4,6,8,10),output);
    }


    @Test
    public void MapTest1()
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1, 2, 3, 4, 5});
        Iterable2<String> output = input.map(Functional.dStringify);
        AssertIterable.assertIterableEquals(Arrays.asList("1","2","3","4","5"),output);
    }

    @Test
    public void SortWithTest1()
    {
        Iterable2<Integer> i = IterableHelper.asList(new Integer[]{1,6,23,7,4});
        Iterable2<Integer> output = i.sortWith(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer a, Integer b) {
                        return Integer.compare(a, b);
                    }
                });
        AssertIterable.assertIterableEquals(Arrays.asList(1,4,6,7,23), output);
    }

    @Test
    public void SortWithTest2()
    {
        Iterable2<Integer> i = IterableHelper.asList(new Integer[] { 1, 6, 23, 7, 4 });
        Iterable2<Integer> j = i.sortWith(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer a, Integer b) {
                        return a - b;
                    }
                });
        AssertIterable.assertIterableEquals(Arrays.asList(1,4,6,7,23), j);
    }

    @Test
    public void SortWithTest3()
    {
        Iterable2<Integer> i = IterableHelper.asList(new Integer[] { 1, 6, 23, 7, 4 });
        Iterable2<Integer> j = i.sortWith(Functional.dSorter);
        AssertIterable.assertIterableEquals(Arrays.asList(1,4,6,7,23), j);
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
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> m = IterableHelper.init(QuadruplingGenerator, 5);
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
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);

        Assert.assertFalse(Functional.forAll2(dBothAreLessThan10, l, m));
    }

    @Test(expected=Exception.class)
    public void ForAll2Test3() throws Exception
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> m = IterableHelper.init(QuadruplingGenerator, 7);

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
        Iterable2<Integer> i = IterableHelper.asList(1, 2, 3, 45, 56, 6);

        boolean allOdd = i.forAll(Functional.IsOdd);
        boolean notAllOdd = i.exists(Functional.not(Functional.IsOdd));

        Assert.assertFalse(allOdd);
        Assert.assertTrue(notAllOdd);
    }

    @Test
    public void CompositionTest2() throws Exception
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
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
        Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
        org.javatuples.Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.IsOdd, m);

        Integer[] left = {3, 9, 15};
        Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getValue0().toArray());
        Assert.assertArrayEquals(right, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest2()
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        org.javatuples.Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.IsEven, l);
        AssertIterable.assertIterableEquals(l, r.getValue0());
        Assert.assertArrayEquals(new Integer[]{}, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest3()
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        org.javatuples.Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.IsEven, l);
        AssertIterable.assertIterableEquals(l.filter(Functional.IsEven), r.getValue0());
    }

    @Test
    public void ToStringTest1()
    {
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> ls = li.map(Functional.dStringify);
        //String s = String.Join(",", ls);
        AssertIterable.assertIterableEquals(IterableHelper.asList("2","4","6","8","10"), ls);
    }

    @Test
    public void ChooseTest1B() throws OptionNoValueAccessException
    {
        Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        Iterable2<String> o = li.choose(
                new Func<Integer, Option<String>>() {
                    @Override
                    public Option<String> apply(Integer i) {
                        return i % 2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
                    }
                });
        Iterable2<String> expected = IterableHelper.asList("6", "12");
        AssertIterable.assertIterableEquals(o, expected);
    }

    @Test
    public void ChooseTest2A() //throws OptionNoValueAccessException
    {
        Map<Integer,String> o=null;
        try{
            Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
            o = Functional.toDictionary(Functional.<Integer>Identity(), Functional.dStringify,
                    li.choose(
                            new Func<Integer, Option<Integer>>() {
                                @Override
                                public Option<Integer> apply(Integer i) {
                                    return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
                                }
                            }));
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
        Iterable2<Integer> a = IterableHelper.asList(1, 2, 3, 4, 5);
        Iterable2<Integer> b = a.map(
                new Func<Integer, Integer>() {
                    @Override
                    public Integer apply(final Integer a1) {
                        return adder_int(2, a1);
                    }
                });
        Iterable2<Integer> c = a.map(curried_adder_int(2));
        AssertIterable.assertIterableEquals(b, c);
    }

    private static String csv(final String state, final Integer a)
    {
        return Functional.isNullOrEmpty(state) ? a.toString() : state + "," + a;
    }

    @Test
    public void FoldvsMapTest1()
    {
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        String s1 = Functional.join(",", li.map(Functional.dStringify));
        Assert.assertEquals("2,4,6,8,10", s1);
        String s2 = li.fold(
                new Func2<String, Integer, String>() {
                    @Override
                    public String apply(String s1, Integer s2) {
                        return csv(s1, s2);
                    }
                }, "");
        Assert.assertEquals(s1, s2);
    }

    private final Func<Iterable2<Integer>, String> concatenate =
            new Func<Iterable2<Integer>, String>() {
                @Override
                public String apply(Iterable2<Integer> l) {
                    return l.fold(new Func2<String, Integer, String>() {
                        @Override
                        public String apply(String s1, Integer s2) {
                            return csv(s1, s2);
                        }
                    }, "");
                }
            };

    @Test
    public void FwdPipelineTest1()
    {
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        String s1 = li.in(concatenate);
        Assert.assertEquals("2,4,6,8,10", s1);
    }

    private final UnaryFunction<Iterable2<Integer>, Iterable2<Integer>> evens_f =
            new UnaryFunction<Iterable2<Integer>, Iterable2<Integer>>() {
                @Override
                public Iterable2<Integer> apply(Iterable2<Integer> l) {
                    return l.filter(Functional.IsEven);
                }
            };

    @Test
    public void FwdPipelineTest2()
    {
        Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        Iterable2<Integer> evens = li.in(evens_f);
        String s1 = evens.in(concatenate);
        String s2 = li.in(evens_f.then(concatenate));
        Assert.assertEquals("6,12", s1);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void CompositionTest3()
    {
        Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        String s = li.in( evens_f.then(concatenate));
        Assert.assertEquals("6,12", s);
    }

    @Test
    public void CompositionTest4()
    {
        Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        String s = evens_f.then(concatenate).apply(li);
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

        Iterable2<String> indentation = IterableHelper.init(
                new Func<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return " ";
                    }
                }, level);
        Assert.assertEquals(Functional.join("", indentation), "     ");

        String s = indentation.fold(
                new Func2<String, String, String>() {
                    @Override
                    public String apply(String state, String str) {
                        return state + str;
                    }
                }, "");
        Assert.assertEquals(s, expectedResult);

        Func<Iterable2<String>, String> folder =
                new Func<Iterable2<String>, String>() {
                    @Override
                    public String apply(Iterable2<String> l) {
                        return l.fold(new Func2<String, String, String>() {
                            @Override
                            public String apply(String state, String str) {
                                return state + str;
                            }
                        }, "");
                    }
                };

        String s1 = indentation.in(folder);
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
        Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        Iterable2<Integer> o =
                li.choose(
                        new Func<Integer, Option<Integer>>() {
                            public Option<Integer> apply(Integer i) {
                                return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        });

        Integer[] expected = new Integer[]{6,12};
        AssertIterable.assertIterableEquals(Arrays.asList(expected), o);
    }

/*
        [Test]
        public void TryGetValueTest1()
        {
            var d = new Dictionary<string, int>();
            d["one"] = 1;
            Assert.AreEqual(1, Iterable2Helpers.TryGetValue_nullable("one", d));
        }

 */

    /*
            [Test]
        public void TryGetValueTest2()
        {
            var d = new Dictionary<string, int>();
            d["one"] = 1;
            Assert.IsNull(Iterable2Helpers.TryGetValue_nullable("two", d));
        }

     */
/*
        [Test]
        public void TryGetValueTest3()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.AreEqual("ONE", Iterable2Helpers.TryGetValue("one", d).Some);
        }

 */

    /*
            [Test]
        public void TryGetValueTest4()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.IsTrue(Iterable2Helpers.TryGetValue("two", d).None);
        }

     */

    /*
            [Test]
        public void TryGetValueTest5()
        {
            var d = new Dictionary<string, List<int>>();
            var l = new List<int>(new[] {1, 2, 3});
            d["one"] = l;
            Assert.AreEqual(l, Iterable2Helpers.TryGetValue("one", d).Some);
        }
*/
    /*
        [Test]
        public void TryGetValueTest6()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.IsTrue(Iterable2Helpers.TryGetValue("two", d).None);
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
        Iterable2<Integer> openedDays = IterableHelper.init(TriplingGenerator, 5);
        Double last = 10.0;
        for (int day : openedDays)
        {
            Double value = day%2 == 0 ? (Double)((double)(day/2)) : null;
            if (value!=null)
                last = value;
            else
                missingPricesPerDate.put(day, last);
        }

        Iterable2<myInt> openedDays2 = IterableHelper.init(
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
        Iterable2<Integer> ids = IterableHelper.init(TriplingGenerator, 5);
        final String expected = "3,6,9,12,15";
        Assert.assertEquals(expected, Functional.join(",", ids.map(Functional.dStringify)));
        Assert.assertEquals(expected, Functional.join(",", ids));
    }

    @Test
    public void joinTest2() throws Exception
    {
        Iterable2<Integer> ids = IterableHelper.init(TriplingGenerator, 5);
        final String expected = "'3','6','9','12','15'";
        Func<Integer, String> f =
                new Func<Integer, String>() {
                    @Override
                    public String apply(Integer id) {
                        return "'" + id + "'";
                    }
                };
        Assert.assertEquals(expected, Functional.join(",", ids.map(f)));
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
        // mult(two,three).then(add(four)) =>
        // then(mult(two,three),add(four))
        // 2 * 3 + 4 = 10
        Integer two = 2;
        Integer three = 3;
        Integer four = 4;
        Iterable2.then(new Func<Integer,Integer>()
        {
            @Override
            public Integer apply(Integer i) { return }
        })
    } */


    @Test(expected=KeyNotFoundException.class)
    public void findLastTest1() throws Exception
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Assert.assertEquals((Integer) 5, Functional.findLast(Functional.IsOdd, l));
    }

    @Test
    public void findLastTest2() throws Exception
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Assert.assertEquals((Integer)10, Functional.findLast(Functional.IsEven, l));
    }

    @Test
    public void toArrayTest1() throws Exception
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Iterable<String> strs = input.map(Functional.dStringify);
        List<String> expected = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});

        Object[] output = Functional.toArray(strs);

        Assert.assertEquals(expected.size(),output.length);
        for(int i=0; i<expected.size(); ++i)
            Assert.assertEquals(expected.get(i), output[i]);
    }

    @Test
    public void lastTest1() throws Exception
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Assert.assertEquals(5,(long) Functional.last(input));
    }

    @Test
    public void lastTest2() throws Exception
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Iterable<String> strs = input.map(Functional.dStringify);
        Assert.assertEquals("5", Functional.last(strs));
    }

    @Test
    public void concatTest1() throws Exception
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Iterable2<Integer> expected = IterableHelper.asList(new Integer[]{1,2,3,4,5,1,2,3,4,5});
        AssertIterable.assertIterableEquals(expected, input.concat(input));
    }

    @Test
    public void seqConcatTest1() throws Exception
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Func<Integer,Integer> doubler = new Func<Integer, Integer>() {
            @Override
            public Integer apply(Integer i) {
                return i * 2;
            }
        };
        Iterable2<String> expected = IterableHelper.asList(new String[]{"1","2","3","4","5","2","4","6","8","10"});

        Iterable2<String> strs = input.map(Functional.dStringify);
        Iterable2<String> output = strs.concat(input.map(doubler).map(Functional.dStringify));

        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void arrayIterableTest1() throws Exception
    {
        Integer[] input = new Integer[]{1,2,3,4,5};
        Iterable2<Integer> expected = IterableHelper.asList(new Integer[]{1,2,3,4,5});

        ArrayIterable<Integer> ait = ArrayIterable.create(input);
        List<Integer> output = new ArrayList<Integer>();
        for(Integer i:ait) output.add(i);
        AssertIterable.assertIterableEquals(expected, output);
    }


    @Test
    public void seqChooseTest1() throws Exception
    {
        Iterable2<Integer> li = IterableHelper.init(TriplingGenerator,5);
        Iterable2<String> output = li.choose(
                new Func<Integer,Option<String>>()
                {
                    @Override
                    public Option<String> apply(Integer i)
                    {
                        return i%2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
                    }
                });

        Collection<String> expected = Arrays.asList(new String[]{"6", "12"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest3()
    {
        Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> output = input.in(
                new Func<Iterable2<Integer>, Iterable2<String>>() {
                    @Override
                    public Iterable2<String> apply(Iterable2<Integer> integers) {
                        return integers.map(Functional.dStringify);
                    }
                });

        Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest4()
    {
        Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> output = input.in(
                new Func<Iterable2<Integer>, Iterable2<String>>() {
                    @Override
                    public Iterable2<String> apply(Iterable2<Integer> integers) {
                        try {
                            return integers.map(Functional.dStringify);
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
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> oddElems = l.in(
                new Func<Iterable2<Integer>, Iterable2<Integer>>() {
                    @Override
                    public Iterable2<Integer> apply(Iterable2<Integer> ints) {
                        return ints.filter(Functional.IsOdd);
                    }
                });

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
        Assert.AreEqual("10", i.in(fn));
        Assert.AreEqual("test", s.in(fn));
    } */

    @Test
    public void seqInitTest2()
    {
        Iterable2<Integer> output = IterableHelper.init(DoublingGenerator);
        AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2,4,6,8,10,12,14,16,18,20,22}),output.take(11));
    }

    @Test
    public void ConstantInitialiserTest1()
    {
        final int howMany = 6;
        final int initValue = -1;
        Iterable2<Integer> l = IterableHelper.init(Functional.Constant(initValue),howMany);
        Assert.assertEquals(howMany, l.toList().size());
        for(int i : l)
            Assert.assertEquals(initValue, i);
    }

    /*[Test]
    public void SwitchTest1()
    {
        Assert.AreEqual(1,
                Iterable2.Switch(10,
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
        int results = Iterable2.Try<int,int,DivideByZeroException>(10, a => a/zero, a => a);
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

        Func<A, IEnumerable<Iterable2.Case<int, object>>> cases =
                a => c2(a).Select((f, i) => Case.ToCase(i.Equals, f));

        var theA = new A {id = 1, name = "one"};

        IEnumerable<object> results =
                Enumerable.Range(0, 3).Select(i => Iterable2.Switch(i, cases(theA), aa => "oh dear"));
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
        Iterable2<Integer> input = IterableHelper.asList(new Integer[] {1, 2, 3, 4, 5});
        Collection<Integer> expected = Arrays.asList(new Integer[] {1, 3, 5});
        Iterable2<Integer> output =
                input.choose(
                        new Func<Integer, Option<Integer>>() {
                            @Override
                            public Option<Integer> apply(Integer i) {
                                return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        });
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void ChooseTest2() throws OptionNoValueAccessException
    {
        Iterable2<String> input = IterableHelper.asList(new String[] {"abc", "def"});
        Collection<Character> expected = Arrays.asList(new Character[]{'a'});
        Iterable2<Character> output =
                input.choose(
                        new Func<String, Option<Character>>() {
                            @Override
                            public Option<Character> apply(String str) {
                                return str.startsWith("a") ? Option.toOption('a') : Option.<Character>None();
                            }
                        }
                );
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void ChooseTest3() throws OptionNoValueAccessException
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[] {1, 2, 3, 4, 5});
        Collection<Integer> expected = Arrays.asList(new Integer[] {1, 3, 5});
        Iterable2<Integer> output = input.choose(
                new Func<Integer, Option<Integer>>() {
                    @Override
                    public Option<Integer> apply(Integer i) {
                        return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
                    }
                })  ;

        AssertIterable.assertIterableEquals(expected, output);
    }

    /*[Test]
    public void CurryTest1()
    {
        Func<int, int, bool> f = (i, j) => i > j;
        Func<int, Func<int, bool>> g = i => j => f(i, j);
        bool t = 10.in(g(5));
        Assert.IsFalse(t);
    }*/

    /*[Test]
    public void CurryTest2()
    {
        Func<int, int, bool> f = (i, j) => i < j;
        Func<int, Func<int, bool>> g = i => j => f(i, j);
        bool t = 10.in(g(5));
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
        Assert.AreEqual(expected, 2.in(add1(1).then(mult1(3))));
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
        Iterable2<Integer> input1 = IterableHelper.asList(new Integer[] {1, 2, 3, 4, 5});
        Iterable2<Character> input2 = IterableHelper.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});

        Collection<org.javatuples.Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
        expected.add(new Pair<Integer, Character>(1, 'a'));
        expected.add(new Pair<Integer, Character>(2, 'b'));
        expected.add(new Pair<Integer, Character>(3, 'c'));
        expected.add(new Pair<Integer, Character>(4, 'd'));
        expected.add(new Pair<Integer, Character>(5, 'e'));

        Iterable2<org.javatuples.Pair<Integer,Character>> output = input1.zip(input2);

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

        Iterable2<Triplet<Integer,Character,Double>> output = IterableHelper.create(input1).zip3(input2, input3);

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

        var output = Iterable2.Zip3(input1, input2, input3).ToList();

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

        var output = Iterable2.Zip3(input1, input2, input3).ToList();

        CollectionAssert.AreEquivalent(expected, output);
    }*/

    @Test
    public void findTest1() throws Exception
    {
        final String trueMatch = "6";
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> ls = li.map(Functional.dStringify);
        Assert.assertEquals(trueMatch,
                ls.find(
                        new Func<String, Boolean>() {
                            @Override
                            public Boolean apply(String s) {
                                return s.equals(trueMatch);
                            }
                        }));
    }

    @Test(expected = KeyNotFoundException.class)
    public void findTest2() throws Exception
    {
        final String falseMatch = "7";
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> ls = li.map(Functional.dStringify);
        ls.find(
                new Func<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) {
                        return s.equals(falseMatch);
                    }
                });
    }

    @Test
    public void findIndexTest1() throws Exception
    {
        final String trueMatch = "6";
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> ls = li.map(Functional.dStringify);
        Assert.assertEquals(2,
                ls.findIndex(
                        new Func<String, Boolean>() {
                            @Override
                            public Boolean apply(String s) {
                                return s.equals(trueMatch);
                            }
                        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIndexTest2() throws Exception
    {
        final String falseMatch = "7";
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<String> ls = li.map(Functional.dStringify);
        ls.findIndex(
                new Func<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) {
                        return s.equals(falseMatch);
                    }
                });
    }

    @Test
    public void pickTest1() throws Exception
    {
        final int trueMatch = 6;
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Assert.assertEquals(((Integer)trueMatch).toString(),
                li.pick(
                        new Func<Integer, Option<String>>() {
                            @Override
                            public Option<String> apply(Integer a) {
                                return a == trueMatch ? new Option<String>(a.toString()) : Option.<String>None();
                            }
                        }));
    }

    @Test(expected = KeyNotFoundException.class)
    public void pickTest2() throws Exception
    {
        final int falseMatch = 7;
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        li.pick(
                new Func<Integer, Option<String>>() {
                    @Override
                    public Option<String> apply(Integer a) {
                        return a == falseMatch ? new Option<String>(a.toString()) : Option.<String>None();
                    }
                });
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
        Iterable2<Integer> output = IterableHelper.init(DoublingGenerator, 5);
        List<Integer> output_ints = output.toList();
        AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2,4,6,8,10}), output_ints);
    }

    public static final Func<Integer,Iterable<Integer>> intToList(final int howMany)
    {
        return new Func<Integer, Iterable<Integer>>() {
            @Override
            public Iterable<Integer> apply(final Integer integer) {
                return IterableHelper.init(
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
        Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> output = input.collect(intToList(3));
        List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void seqCollectTest1()
    {
        Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> output = input.collect(intToList(3));
        List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void seqCollectTest2()
    {
        Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Iterable2<Integer> output1 = input.collect(intToList(3));
        Iterable<Integer> output2 = output1;
        List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output1);
        AssertIterable.assertIterableEquals(expected, output2);
    }

    @Test
    public void takeNandYieldTest1()
    {
        Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Tuple2<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input,2);
        List<Integer> expectedList = Arrays.asList(2,4);
        List<Integer> expectedRemainder = Arrays.asList(6,8,10);
        AssertIterable.assertIterableEquals(expectedList,output.getValue0());
        AssertIterable.assertIterableEquals(expectedRemainder,output.getValue1());
    }

    @Test
    public void takeNandYieldTest2()
    {
        Iterable<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        Tuple2<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input,0);
        List<Integer> expectedList = Arrays.asList();
        List<Integer> expectedRemainder = Arrays.asList(2,4,6,8,10);
        AssertIterable.assertIterableEquals(expectedList,output.getValue0());
        AssertIterable.assertIterableEquals(expectedRemainder,output.getValue1());
    }

    @Test
    public void recFilterTest1()
    {
        Iterable2<Integer> l = IterableHelper.init(DoublingGenerator,5);
        Iterable<Integer> oddElems = l.filter(Functional.IsOdd);

        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
    }

    @Test
    public void recMapTest1()
    {
        Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1, 2, 3, 4, 5});
        Iterable<String> output = input.map(Functional.dStringify);
        AssertIterable.assertIterableEquals(Arrays.asList("1","2","3","4","5"),output);
    }

    @Test
    public void recFoldvsMapTest1()
    {
        Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        String s1 = Functional.join(",", li.map(Functional.dStringify));
        Assert.assertEquals("2,4,6,8,10", s1);
        String s2 = li.fold(
                new Func2<String, Integer, String>() {
                    @Override
                    public String apply(String s1, Integer s2) {
                        return csv(s1, s2);
                    }
                }, "");
        Assert.assertEquals(s1, s2);
    }
}