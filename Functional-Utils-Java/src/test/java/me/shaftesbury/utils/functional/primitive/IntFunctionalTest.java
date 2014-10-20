package me.shaftesbury.utils.functional.primitive;

import me.shaftesbury.utils.functional.Func;
import me.shaftesbury.utils.functional.Option;
import me.shaftesbury.utils.functional.OptionNoValueAccessException;
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
public class IntFunctionalTest
{
    public static final Func_int_int DoublingGenerator =
            new Func_int_int()
            {
                @Override public int apply(final int a) { return 2*a;}
            };

    @Test
    public void InitTest1()
    {
        final IntList output = Functional.init(DoublingGenerator, 5);
        Assert.assertArrayEquals(new int[]{2,4,6,8,10}, output.toArray());
    }

    @Test
    public void rangeTest1()
    {
        final IntList output = Functional.init(Functional.range(0),5);
        Assert.assertArrayEquals(new int[]{0,1,2,3,4},output.toArray());
    }

    @Test
    public void MapTest1()
    {
        final IntList input = new IntList(new int[]{1, 2, 3, 4, 5});
        final Collection<String> output = Functional.map(Functional.dStringify(), input);
        Assert.assertArrayEquals(new String[]{"1","2","3","4","5"},output.toArray());
    }

    @Test
    public void MapiTest1()
    {
        final IntList input = new IntList(new int[]{1, 2, 3, 4, 5});
        final Collection<Pair<Integer,String>> output = Functional.mapi(new Func2_int_int_T<Pair<Integer, String>>() {
            @Override
            public Pair<Integer, String> apply(final int pos, final int i) {
                return Pair.with(pos, Integer.toString(i));
            }
        }, input);
        Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, me.shaftesbury.utils.functional.Functional.map(new Func<Pair<Integer, String>, String>() {
            @Override
            public String apply(final Pair<Integer, String> o) {
                return o.getValue1();
            }
        }, output).toArray());
        Assert.assertArrayEquals(new int[]{0,1,2,3,4},Functional.map(new Func_T_int<Pair<Integer,String>>() {
            @Override
            public int apply(final Pair<Integer,String> o) {
                return o.getValue0();
            }
        },output).toArray());
    }

//    @Test
//    public void seqMapiTest1()
//    {
//        final IntList input = new IntList(new int[]{1, 2, 3, 4, 5});
//        final Collection<Pair<Integer,String>> output = Functional.toList(Functional.seq.mapi(new Func2<Integer, Integer, Pair<Integer, String>>() {
//            @Override
//            public Pair<Integer, String> apply(Integer pos, Integer i) {
//                return Pair.with(pos, i.toString());
//            }
//        }, input));
//        Assert.assertArrayEquals(new String[]{"1","2","3","4","5"},Functional.map(new Func<Pair<Integer,String>, String>() {
//            @Override
//            public String apply(Pair<Integer,String> o) {
//                return o.getValue1();
//            }
//        },output).toArray());
//        Assert.assertArrayEquals(new Integer[]{0,1,2,3,4},Functional.map(new Func<Pair<Integer,String>, Integer>() {
//            @Override
//            public Integer apply(Pair<Integer,String> o) {
//                return o.getValue0();
//            }
//        },output).toArray());
//    }

//    @Test
//    public void seqMapiTest2()
//    {
//        final IntList input = new IntList(new int[]{1, 2, 3, 4, 5});
//        final Iterable<Pair<Integer, String>> mapi = Functional.seq.mapi(new Func2<Integer, Integer, Pair<Integer, String>>() {
//            @Override
//            public Pair<Integer, String> apply(Integer pos, Integer i) {
//                return Pair.with(pos, i.toString());
//            }
//        }, input);
//        final Iterator<Pair<Integer, String>> iterator = mapi.iterator();
//
//        for(int i=0;i<10;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        Pair<Integer, String> next = iterator.next();
//        Assert.assertEquals("1",next.getValue1());
//        next = iterator.next();
//        Assert.assertEquals("2",next.getValue1());
//        next = iterator.next();
//        Assert.assertEquals("3",next.getValue1());
//        next = iterator.next();
//        Assert.assertEquals("4",next.getValue1());
//        next = iterator.next();
//        Assert.assertEquals("5",next.getValue1());
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }

    public static final Func_int_int TriplingGenerator =
            new Func_int_int() {
                @Override
                public int apply(final int a) {
                    return 3 * a;
                }
            };

    public static final Func_int_int QuadruplingGenerator =
            new Func_int_int() {
                @Override
                public int apply(final int a) {
                    return 4 * a;
                }
            };


    private static boolean BothAreEven(final int a, final int b)
    {
        return Functional.isEven.apply(a) && Functional.isEven.apply(b);
    }


    @Test
    public void ForAll2Test1()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        final IntList m = Functional.init(QuadruplingGenerator, 5);
        try
        {
            Assert.assertTrue(Functional.forAll2(
                    new Func2_int_int_T<Boolean>() {
                        @Override
                        public Boolean apply(final int a, final int b) {
                            return BothAreEven(a, b);
                        }
                    }, l, m));
        }
        catch (final Exception e)
        {
            Assert.fail();
        }
    }

//    @Test
//    public void ForAll2NoExceptionTest1()
//    {
//        final IntList l = Functional.init(DoublingGenerator, 5);
//        final IntList m = Functional.init(QuadruplingGenerator, 5);
//        try
//        {
//            Assert.assertTrue(Functional.noException.forAll2(
//                    new Func2<Integer, Integer, Boolean>() {
//                        @Override
//                        public Boolean apply(Integer a, Integer b) {
//                            return BothAreEven(a, b);
//                        }
//                    }, l, m).Some());
//        }
//        catch (final Exception e)
//        {
//            Assert.fail();
//        }
//    }

    private static boolean BothAreLessThan10(final int a, final int b)
    {
        return a < 10 && b < 10;
    }

    private static Func2_int_int_T<Boolean> dBothAreLessThan10 =
            new Func2_int_int_T<Boolean>() {
    @Override
    public Boolean apply(final int a, final int b) {
        return BothAreLessThan10(a, b);
    }
}   ;

    @Test
    public void ForAll2Test2()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        final IntList m = Functional.init(TriplingGenerator, 5);

        Assert.assertFalse(Functional.forAll2(dBothAreLessThan10
                , l, m));
    }

    @Test(expected=Exception.class)
    public void ForAll2Test3()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        final IntList m = Functional.init(QuadruplingGenerator, 7);

        Functional.forAll2(
                new Func2_int_int_T<Boolean>() {
                    @Override
                    public Boolean apply(final int a, final int b) {
                        return BothAreEven(a, b);
                    }
                }, l, m);
    }

//    @Test
//    public void ForAll2NoExceptionTest2()
//    {
//        final IntList l = Functional.init(DoublingGenerator, 5);
//        final IntList m = Functional.init(TriplingGenerator, 5);
//
//        Assert.assertFalse(Functional.noException.forAll2(dBothAreLessThan10
//                , l, m).Some());
//    }

//    @Test
//    public void ForAll2NoExceptionTest3()
//    {
//        final IntList l = Functional.init(DoublingGenerator, 5);
//        final IntList m = Functional.init(QuadruplingGenerator, 7);
//
//        Assert.assertTrue(Functional.noException.forAll2(
//                new Func2<Integer, Integer, Boolean>() {
//                    @Override
//                    public Boolean apply(Integer a, Integer b) {
//                        return BothAreEven(a, b);
//                    }
//                }, l, m).isNone());
//    }

    @Test
    public void CompositionTest1A()
    {
        final IntList i = new IntList(new int[]{1, 2, 3, 45, 56, 6});

        final boolean allOdd = Functional.forAll(Functional.isOdd, i);
        final boolean notAllOdd = Functional.exists(Functional.not(Functional.isOdd), i);

        Assert.assertFalse(allOdd);
        Assert.assertTrue(notAllOdd);
    }

//    @Test
//    public void CompositionTest2()
//    {
//        final IntList l = Functional.init(DoublingGenerator, 5);
//        final IntList m = Functional.init(TriplingGenerator, 5);
//        Assert.assertFalse(Functional.forAll2(Functional.not2(dBothAreLessThan10), l, m));
//        // equivalent to BothAreGreaterThanOrEqualTo10
//
//        final int lowerLimit = 1;
//        final int upperLimit = 16;
//        Assert.assertFalse(
//                Functional.forAll2(
//                        Functional.not2(
//                                new Func2<Integer, Integer, Boolean>() {
//                                    @Override
//                                    public Boolean apply(Integer a, Integer b) {
//                                        return a > lowerLimit && b > lowerLimit;
//                                    }
//                                }
//                        ), l, m));
//        Assert.assertTrue(
//                Functional.forAll2(
//                        Functional.not2(
//                                new Func2<Integer, Integer, Boolean>() {
//                                    @Override
//                                    public Boolean apply(Integer a, Integer b) {
//                                        return a > upperLimit && b > upperLimit;
//                                    }
//                                }
//                        ), l, m));
//    }
//
    @Test
    public void PartitionTest1()
    {
        final IntList m = Functional.init(TriplingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.isOdd, m);

        final Integer[] left = {3, 9, 15};
        final Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getValue0().toArray());
        Assert.assertArrayEquals(right, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest2()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.isEven, l);
        Assert.assertArrayEquals(l.toArray(new Integer[0]), r.getValue0().toArray());
        Assert.assertArrayEquals(new Integer[]{}, r.getValue1().toArray());
    }

    @Test
    public void PartitionTest3()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.isEven, l);
        Assert.assertArrayEquals(Functional.filter(Functional.isEven, l).toArray(new Integer[0]), r.getValue0().toArray());
    }

    @Test
    public void ToStringTest1()
    {
        final IntList li = Functional.init(DoublingGenerator, 5);
        final Collection<String> ls = Functional.map(Functional.dStringify(), li);
        //String s = String.Join(",", ls);
        Assert.assertArrayEquals(new String[]{"2","4","6","8","10"}, ls.toArray());
    }

    @Test
    public void ChooseTest1B() throws OptionNoValueAccessException
    {
        final IntList li = Functional.init(TriplingGenerator, 5);
        final Collection<String> o = Functional.choose(
                new Func_int_T<Option<String>>() {
                    @Override
                    public Option<String> apply(final int i) {
                        return i % 2 == 0 ? Option.toOption(Integer.toString(i)) : Option.<String>None();
                    }
                }, li);
        final String[] expected = {"6", "12"};
        Assert.assertArrayEquals(o.toArray(), expected);
    }

    @Test
    public void ChooseTest2A() //throws OptionNoValueAccessException
    {
        Map<Integer,String> o=null;
        try{
            final IntList li = Functional.init(TriplingGenerator, 5);
        o = Functional.toDictionary(Functional.identity(), Functional.dStringify(),
                Functional.filter(
                        new Func_int_T<Boolean>() {
                            @Override
                            public Boolean apply(final int i) {
                                return i % 2 == 0;
                            }
                        }, li));
        }catch(final Exception e){}
        final Map<Integer,String> expected = new HashMap<Integer,String>();
        expected.put(6, "6");
        expected.put(12, "12");
        Assert.assertTrue(expected.size()==o.size());
        for(final Integer expectedKey : expected.keySet())
        {
            Assert.assertTrue(o.containsKey(expectedKey));
            final String expectedValue=expected.get(expectedKey);
            //Assert.assertEquals(expectedValue,o.get(expectedKey),"Expected '"+expectedValue+"' but got '"+o.get(expectedKey)+"'");
            Assert.assertTrue(o.get(expectedKey).equals(expectedValue));
        }
    }

//    private final static <B, C>boolean Fn(final B b, final C c)
//    {
//        return b.equals(c);
//    }
//
//    private final static <B, C>Func<C, Boolean> curried_fn(final B b)
//    {
//        return new Func<C, Boolean>() {
//            @Override
//            public Boolean apply(C c) {
//                return Fn(b,c);
//            }
//        };
//    }
//
//    @Test
//    public void CurriedFnTest1()
//    {
//        final boolean test1a = Fn(1, 2);
//        final boolean test1b = curried_fn(1).apply(2);
//        Assert.assertEquals(test1a, test1b);
//    }
//
//    private static int adder_int(int left, int right) { return left + right; }
//
//    private static final Func<Integer, Integer> curried_adder_int(final int c)
//    {
//        return new Func<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer p) {
//                return adder_int(c, p);
//            }
//        };
//    }
//
//    @Test
//    public void CurriedFnTest2()
//    {
//        final List<Integer> a = Arrays.asList(1, 2, 3, 4, 5);
//        final IntList b = Functional.map(
//                new Func<Integer, Integer>() {
//                    @Override
//                    public Integer apply(final Integer a1) {
//                        return adder_int(2, a1);
//                    }
//                }, a);
//        final IntList c = Functional.map(curried_adder_int(2), a);
//        Assert.assertArrayEquals(b.toArray(), c.toArray());
//    }
//
//    private static String csv(final String state, final Integer a)
//    {
//        return Functional.isNullOrEmpty(state) ? a.toString() : state + "," + a;
//    }
//
//    @Test
//    public void FoldvsMapTest1()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final String s1 = Functional.join(",", Functional.map(Functional.<Integer>dStringify(), li));
//        Assert.assertEquals("2,4,6,8,10", s1);
//        final String s2 = Functional.fold(
//                new Func2<String, Integer, String>() {
//                    @Override
//                    public String apply(String s1, Integer s2) {
//                        return csv(s1, s2);
//                    }
//                }, "", li);
//        Assert.assertEquals(s1, s2);
//    }
//
//    private final Func<IntList, String> concatenate =
//            new Func<IntList, String>() {
//                @Override
//                public String apply(IntList l) {
//                    return Functional.fold(new Func2<String, Integer, String>() {
//                        @Override
//                        public String apply(String s1, Integer s2) {
//                            return csv(s1, s2);
//                        }
//                    }, "", l);
//                }
//            };
//
//    @Test
//    public void FwdPipelineTest1()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final String s1 = Functional.in(li, concatenate);
//        Assert.assertEquals("2,4,6,8,10", s1);
//    }
//
//    private final Func<IntList, IntList> evens_f =
//            new Func<IntList, IntList>() {
//                @Override
//                public IntList apply(IntList l) {
//                    return Functional.filter(Functional.isEven, l);
//                }
//            };
//
//    @Test
//    public void FwdPipelineTest2()
//    {
//        final IntList li = Functional.init(TriplingGenerator, 5);
//        final IntList evens = Functional.in(li, evens_f);
//        final String s1 = Functional.in(evens, concatenate);
//        final String s2 = Functional.in(li, Functional.then(evens_f, concatenate));
//        Assert.assertEquals("6,12", s1);
//        Assert.assertEquals(s1, s2);
//    }
//
//    @Test
//    public void CompositionTest3()
//    {
//        final IntList li = Functional.init(TriplingGenerator, 5);
//        final String s = Functional.in(li, Functional.then(evens_f, concatenate));
//        Assert.assertEquals("6,12", s);
//    }
//
//    @Test
//    public void CompositionTest4()
//    {
//        final IntList li = Functional.init(TriplingGenerator, 5);
//        final String s = Functional.then(evens_f, concatenate).apply(li);
//        Assert.assertEquals("6,12", s);
//    }
//
//    @Test
//    public void IndentTest1()
//    {
//        final int level = 5;
//        final String expectedResult = "     ";
//
//        String indentedName = "";
//        for (int i = 0; i < level; ++i)
//        {
//            indentedName += " ";
//        }
//        Assert.assertEquals(indentedName, expectedResult);
//
//        final Collection<String> indentation = Functional.init(
//                new Func<Integer, String>() {
//                    @Override
//                    public String apply(Integer integer) {
//                        return " ";
//                    }
//                }, level);
//        Assert.assertEquals(Functional.join("", indentation), "     ");
//
//        final String s = Functional.fold(
//                new Func2<String, String, String>() {
//                    @Override
//                    public String apply(String state, String str) {
//                        return state + str;
//                    }
//                }, "", indentation);
//        Assert.assertEquals(s, expectedResult);
//
//        final Func<Collection<String>, String> folder =
//                new Func<Collection<String>, String>() {
//                    @Override
//                    public String apply(Collection<String> l) {
//                        return Functional.fold(new Func2<String, String, String>() {
//                            @Override
//                            public String apply(String state, String str) {
//                                return state + str;
//                            }
//                        }, "", l);
//                    }
//                };
//
//        final String s1 = Functional.in(indentation, folder);
//        Assert.assertEquals(s1, expectedResult);
//    }
//
//    @Test
//    public void IndentTest2()
//    {
//        final int level = 5;
//        final String expectedResult = "     BOB";
//        Assert.assertEquals(expectedResult, Functional.indentBy(level, " ", "BOB"));
//    }
//
//
//    @Test
//    public void ChooseTest3A() throws OptionNoValueAccessException
//    {
//        final IntList li = Functional.init(TriplingGenerator, 5);
//        final IntList o =
//                Functional.choose(
//                        new Func<Integer, Option<Integer>>() {
//                            public Option<Integer> apply(Integer i) {
//                                return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
//                            }
//                        }, li);
//
//        final Integer[] expected = new Integer[]{6,12};
//        Assert.assertArrayEquals(expected, o.toArray());
//    }
//
///*
//        [Test]
//        public void TryGetValueTest1()
//        {
//            var d = new Dictionary<string, int>();
//            d["one"] = 1;
//            Assert.AreEqual(1, FunctionalHelpers.TryGetValue_nullable("one", d));
//        }
//
// */
//
//    /*
//            [Test]
//        public void TryGetValueTest2()
//        {
//            var d = new Dictionary<string, int>();
//            d["one"] = 1;
//            Assert.IsNull(FunctionalHelpers.TryGetValue_nullable("two", d));
//        }
//
//     */
///*
//        [Test]
//        public void TryGetValueTest3()
//        {
//            var d = new Dictionary<string, string>();
//            d["one"] = "ONE";
//            Assert.AreEqual("ONE", FunctionalHelpers.TryGetValue("one", d).Some);
//        }
//
// */
//
//    /*
//            [Test]
//        public void TryGetValueTest4()
//        {
//            var d = new Dictionary<string, string>();
//            d["one"] = "ONE";
//            Assert.IsTrue(FunctionalHelpers.TryGetValue("two", d).None);
//        }
//
//     */
//
//    /*
//            [Test]
//        public void TryGetValueTest5()
//        {
//            var d = new Dictionary<string, List<int>>();
//            var l = new List<int>(new[] {1, 2, 3});
//            d["one"] = l;
//            Assert.AreEqual(l, FunctionalHelpers.TryGetValue("one", d).Some);
//        }
//*/
//    /*
//        [Test]
//        public void TryGetValueTest6()
//        {
//            var d = new Dictionary<string, string>();
//            d["one"] = "ONE";
//            Assert.IsTrue(FunctionalHelpers.TryGetValue("two", d).None);
//        }
//
//     */
//
//    private class myInt
//    {
//        private final int _i;
//
//        public myInt(int i)
//        {
//            _i = i;
//        }
//
//        public final int i()
//        {
//            return _i;
//        }
//    }
//
//
//    @Test
//    public void foldAndChooseTest1()
//    {
//        final Map<Integer, Double> missingPricesPerDate = new Hashtable<Integer, Double>();
//        final IntList openedDays = Functional.init(TriplingGenerator, 5);
//        Double last = 10.0;
//        for (final int day : openedDays)
//        {
//            Double value = day%2 == 0 ? (Double)((double)(day/2)) : null;
//            if (value!=null)
//                last = value;
//            else
//                missingPricesPerDate.put(day, last);
//        }
//
//        final Collection<myInt> openedDays2 = Functional.init(
//                new Func<Integer, myInt>() {
//                    @Override
//                    public myInt apply(Integer a) {
//                        return new myInt(3 * a);
//                    }
//                }, 5);
//        final Pair<Double, List<myInt>> output = Functional.foldAndChoose(
//                new Func2<Double, myInt, Pair<Double, Option<myInt>>>() {
//                    @Override
//                    public Pair<Double, Option<myInt>> apply(Double state, myInt day) {
//                        final Double value = day.i() % 2 == 0 ? (Double) ((double) (day.i() / 2)) : null;
//                        return value != null
//                                ? new Pair<Double, Option<myInt>>(value, Option.<myInt>None())
//                                : new Pair<Double, Option<myInt>>(state, Option.toOption(day));
//                    }
//                }, 10.0, openedDays2);
//
//        Assert.assertEquals(last, output.getValue0());
//        final List<Integer> keys = new ArrayList<Integer>(missingPricesPerDate.keySet());
//        Collections.sort(keys);
//        Assert.assertArrayEquals(keys.toArray(),
//                Functional.map(
//                        new Func<myInt, Integer>() {
//                            @Override
//                            public Integer apply(myInt i) {
//                                return i.i();
//                            }
//                        }, output.getValue1()).toArray());
//    }
//
//    @Test
//    public void joinTest1()
//    {
//        final IntList ids = Functional.init(TriplingGenerator, 5);
//        final String expected = "3,6,9,12,15";
//        Assert.assertEquals(expected, Functional.join(",", Functional.map(Functional.<Integer>dStringify(), ids)));
//        Assert.assertEquals(expected, Functional.join(",", ids));
//    }
//
//    @Test
//    public void joinTest2()
//    {
//        final IntList ids = Functional.init(TriplingGenerator, 5);
//        final String expected = "'3','6','9','12','15'";
//        final Func<Integer, String> f =
//                new Func<Integer, String>() {
//                    @Override
//                    public String apply(Integer id) {
//                        return "'" + id + "'";
//                    }
//                };
//        Assert.assertEquals(expected, Functional.join(",", Functional.map(f, ids)));
//        Assert.assertEquals(expected, Functional.join(",", ids, f));
//    }
//
//    @Test
//    public void betweenTest1()
//    {
//        final int lowerBound = 2, upperBound = 4;
//        Assert.assertTrue(Functional.between(lowerBound, upperBound, 3));
//    }
//
//    @Test
//    public void betweenTest2()
//    {
//        final int lowerBound = 2, upperBound = 4;
//        Assert.assertFalse(Functional.between(lowerBound, upperBound, 1));
//    }
//
//    @Test
//    public void betweenTest3()
//    {
//        final double lowerBound = 2.5, upperBound = 2.6;
//        Assert.assertTrue(Functional.between(lowerBound, upperBound, 2.55));
//    }
//
//    @Test
//    public void testIsEven_withEvenNum()
//    {
//        Assert.assertTrue(Functional.isEven.apply(2));
//    }
//
//    @Test
//    public void testIn()
//    {
//        final Integer a = 10;
//        Assert.assertTrue(Functional.in(a, Functional.isEven));
//    }
//
//
//    /*@Test
//    public void testThen()
//    {
//        // mult(two,three).then(add(four)) =>
//        // then(mult(two,three),add(four))
//        // 2 * 3 + 4 = 10
//        Integer two = 2;
//        Integer three = 3;
//        Integer four = 4;
//        Functional.then(new Func<Integer,Integer>()
//        {
//            @Override
//            public Integer apply(Integer i) { return }
//        })
//    } */
//
//    @Test
//    public void seqFilterTest1()
//    {
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Iterable<Integer> oddElems = Functional.seq.filter(Functional.isOdd, l);
//
//        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
//    }
//
//    @Test
//    public void seqFilterTest2()
//    {
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Iterable<Integer> evenElems = Functional.seq.filter(Functional.isEven, l);
//
//        final IntList expected = new int[]{2,4,6,8,10});
//        AssertIterable.assertIterableEquals(expected, evenElems);
//    }
//
//    @Test
//    public void seqFilterTest3()
//    {
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Integer limit = 5;
//        final Iterable<Integer> highElems = Functional.seq.filter(
//                new Func<Integer,Boolean>()
//                {
//                    @Override
//                    public Boolean apply(Integer a) { return a > limit;}
//                }, l);
//
//        final IntList expected = new int[]{6,8,10});
//        AssertIterable.assertIterableEquals(expected,highElems);
//    }
//
//    @Test
//    public void seqFilterTest4()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Integer limit = 10;
//        final Iterable<Integer> output = Functional.seq.filter(
//                new Func<Integer,Boolean>()
//                {
//                    @Override public Boolean apply(Integer a) {return a > limit;}
//                }, li);
//
//        Assert.assertFalse(output.iterator().hasNext());
//    }
//
//    @Test
//    public void seqFilterTest5()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 10);
//        final IntList expected = new int[]{4,8,12,16,20});
//        final Iterable<Integer> output = Functional.seq.filter(
//                new Func<Integer,Boolean>()
//                {
//                    @Override public Boolean apply(Integer a) {return a % 4 ==0;}
//                }, li);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqFilterTest6()
//    {
//        final IntList input = Functional.init(DoublingGenerator, 10);
//        final Iterable<Integer> output = Functional.seq.filter(
//                new Func<Integer,Boolean>()
//                {
//                    @Override public Boolean apply(Integer a) {return a % 4 ==0;}
//                }, input);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        int next = iterator.next();
//        Assert.assertEquals(4,next);
//        next = iterator.next();
//        Assert.assertEquals(8,next);
//        next = iterator.next();
//        Assert.assertEquals(12,next);
//        next = iterator.next();
//        Assert.assertEquals(16,next);
//        next = iterator.next();
//        Assert.assertEquals(20,next);
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
    @Test(expected=NoSuchElementException.class)
    public void findLastTest1()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        Assert.assertEquals( 5, Functional.findLast(Functional.isOdd, l));
    }

    @Test
    public void findLastTest2()
    {
        final IntList l = Functional.init(DoublingGenerator, 5);
        Assert.assertEquals(10, Functional.findLast(Functional.isEven, l));
    }
//
//    @Test
//    public void findLastNoExceptionTest1()
//    {
//        final List<Integer> l = new ArrayList<Integer>(Functional.init(DoublingGenerator, 5));
//        Assert.assertTrue(Functional.noException.findLast(Functional.isOdd, l).isNone());
//    }
//
//    @Test
//    public void findLastNoExceptionTest2()
//    {
//        final List<Integer> l = new ArrayList<Integer>(Functional.init(DoublingGenerator, 5));
//        Assert.assertEquals((Integer)10, Functional.noException.findLast(Functional.isEven, l).Some());
//    }
//
//    @Test(expected=NoSuchElementException.class)
//    public void findLastIterableTest1()
//    {
//        final Iterable<Integer> l = new ArrayList<Integer>(Functional.init(DoublingGenerator, 5));
//        Assert.assertEquals((Integer) 5, Functional.findLast(Functional.isOdd, l));
//    }
//
//    @Test
//    public void findLastIterableTest2()
//    {
//        final Iterable<Integer> l = new ArrayList<Integer>(Functional.init(DoublingGenerator, 5));
//        Assert.assertEquals((Integer)10, Functional.findLast(Functional.isEven, l));
//    }
//
//    @Test
//    public void seqMapTest1()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5}); //Enumerable.Range(1, 5).ToList();
//        final List<String> expected = Arrays.asList(new String[] {"1", "2", "3", "4", "5"});
//        final Iterable<String> output = Functional.seq.map(Functional.<Integer>dStringify(), input);
//        final Iterator<String> it = output.iterator();
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(it.hasNext());
//
//        for(int i=0; i<expected.size(); ++i)
//            Assert.assertEquals(expected.get(i), it.next());
//
//        Assert.assertFalse(it.hasNext());
//        try {
//            it.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void toArrayTest1()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5});
//        final Iterable<String> strs = Functional.seq.map(Functional.<Integer>dStringify(), input);
//        final List<String> expected = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});
//
//        final Object[] output = Functional.toArray(strs);
//
//        Assert.assertEquals(expected.size(),output.length);
//        for(int i=0; i<expected.size(); ++i)
//            Assert.assertEquals(expected.get(i), output[i]);
//    }
//
//    @Test
//    public void lastTest1()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5});
//        Assert.assertEquals(5,(long) Functional.last(input));
//    }
//
//    @Test
//    public void lastTest2()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5});
//        final Iterable<String> strs = Functional.seq.map(Functional.<Integer>dStringify(), input);
//        Assert.assertEquals("5", Functional.last(strs));
//    }
//
//    @Test
//    public void concatTest1()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5});
//        final List<Integer> expected = new int[]{1,2,3,4,5,1,2,3,4,5});
//        AssertIterable.assertIterableEquals(expected, Functional.concat(input, input));
//    }
//
//    @Test
//    public void seqConcatTest1()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5});
//        final Func<Integer,Integer> doubler = new Func<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer i) {
//                return i * 2;
//            }
//        };
//        final List<String> expected = Arrays.asList(new String[]{"1","2","3","4","5","2","4","6","8","10"});
//
//        final Iterable<String> strs = Functional.seq.map(Functional.<Integer>dStringify(), input);
//        final Iterable<String> output = Functional.seq.concat(strs, Functional.seq.map(Functional.<Integer>dStringify(), Functional.seq.map(doubler, input)));
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqConcatTest2()
//    {
//        final List<Integer> input = new int[]{1,2,3,4,5});
//        final Func<Integer,Integer> doubler = new Func<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer i) {
//                return i * 2;
//            }
//        };
//        final List<String> expected = Arrays.asList(new String[]{"1","2","3","4","5","2","4","6","8","10"});
//
//        final Iterable<String> strs = Functional.seq.map(Functional.<Integer>dStringify(), input);
//        final Iterable<String> output = Functional.seq.concat(strs, Functional.seq.map(Functional.<Integer>dStringify(), Functional.seq.map(doubler, input)));
//        final Iterator<String> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        String next = iterator.next();
//        Assert.assertEquals("1",next);
//        next = iterator.next();
//        Assert.assertEquals("2",next);
//        next = iterator.next();
//        Assert.assertEquals("3",next);
//        next = iterator.next();
//        Assert.assertEquals("4",next);
//        next = iterator.next();
//        Assert.assertEquals("5",next);
//        next = iterator.next();
//        Assert.assertEquals("2",next);
//        next = iterator.next();
//        Assert.assertEquals("4",next);
//        next = iterator.next();
//        Assert.assertEquals("6",next);
//        next = iterator.next();
//        Assert.assertEquals("8",next);
//        next = iterator.next();
//        Assert.assertEquals("10",next);
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void arrayIterableTest1()
//    {
//        final Integer[] input = new Integer[]{1,2,3,4,5};
//        final List<Integer> expected = new int[]{1,2,3,4,5});
//
//        final ArrayIterable<Integer> ait = ArrayIterable.create(input);
//        final List<Integer> output = new ArrayList<Integer>();
//        for(final Integer i:ait) output.add(i);
//        Assert.assertArrayEquals(expected.toArray(), output.toArray());
//    }
//
//
//    @Test
//    public void seqChooseTest1()
//    {
//        final IntList li = Functional.init(TriplingGenerator,5);
//        final Iterable<String> output = Functional.seq.choose(
//                new Func<Integer,Option<String>>()
//                {
//                        @Override
//                        public Option<String> apply(Integer i)
//                        {
//                            return i%2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
//                        }
//                }, li);
//
//        final Collection<String> expected = Arrays.asList(new String[]{"6", "12"});
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqChooseTest2()
//    {
//        final IntList li = Functional.init(TriplingGenerator, 5);
//        final Iterable<String> output = Functional.seq.choose(
//                new Func<Integer, Option<String>>() {
//                    @Override
//                    public Option<String> apply(Integer i) {
//                        return i % 2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
//                    }
//                }, li);
//        final Iterator<String> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        String next = iterator.next();
//        Assert.assertEquals("6",next);
//        next = iterator.next();
//        Assert.assertEquals("12",next);
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void seqInitTest1()
//    {
//        final Iterable<Integer> output = Functional.seq.init(DoublingGenerator, 5);
//        AssertIterable.assertIterableEquals(new int[]{2,4,6,8,10}), output);
//    }
//
//    @Test
//    public void seqInitTest3()
//    {
//        final Iterable<Integer> output = Functional.seq.init(DoublingGenerator, 5);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        int next = iterator.next();
//        Assert.assertEquals(2,next);
//        next = iterator.next();
//        Assert.assertEquals(4,next);
//        next = iterator.next();
//        Assert.assertEquals(6,next);
//        next = iterator.next();
//        Assert.assertEquals(8,next);
//        next = iterator.next();
//        Assert.assertEquals(10,next);
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void FwdPipelineTest3()
//    {
//        final IntList input = Functional.init(DoublingGenerator, 5);
//        final Collection<String> output = Functional.in(input,
//                new Func<IntList, Collection<String>>() {
//                    @Override
//                    public Collection<String> apply(IntList integers) {
//                        return Functional.map(Functional.<Integer>dStringify(), integers);
//                    }
//                });
//
//        final Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void FwdPipelineTest4()
//    {
//        final Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
//        final Iterable<String> output = Functional.in(input,
//                new Func<Iterable<Integer>, Iterable<String>>() {
//                    @Override
//                    public Iterable<String> apply(Iterable<Integer> integers) {
//                        try {
//                            return Functional.seq.map(Functional.<Integer>dStringify(), integers);
//                        } catch (final Exception e) {
//                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                            return null; // Argh!!!
//                        }
//                    }
//                });
//
//        final Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void FwdPipelineTest5()
//    {
//        final Iterable<Integer> l = Functional.seq.init(DoublingGenerator, 5);
//        final Iterable<Integer> oddElems = Functional.in(l,
//                new Func<Iterable<Integer>, Iterable<Integer>>() {
//                    @Override
//                    public Iterable<Integer> apply(Iterable<Integer> ints) {
//                        return Functional.filter(Functional.isOdd, ints);
//                    }
//                });
//
//        Assert.assertFalse(oddElems.iterator().hasNext());
//    }
//    /*
//    private class Test1
//    {
//        public readonly int i;
//
//        public Test1(int j)
//        {
//            i = j;
//        }
//    }
//
//    private static Func<object, string> fn1()
//    {
//        return delegate(object o)
//        {
//            if (o.GetType() == typeof (Test1))
//                return fn2(o as Test1);
//            if (o.GetType() == typeof (string))
//                return fn3(o as string);
//            return null;
//        };
//    }
//
//    private static string fn2(Test1 i)
//    {
//        return i.i.ToString();
//    }
//
//    private static string fn3(string s)
//    {
//        return s;
//    }
//
//    [Test]
//    public void FwdPipelineTest6()
//    {
//        Func<object, string> fn = fn1();
//        var i = new Test1(10);
//        const string s = "test";
//        Assert.AreEqual("10", i.in(fn));
//        Assert.AreEqual("test", s.in(fn));
//    } */
//
//    @Test
//    public void seqInitTest2()
//    {
//        final Iterable<Integer> output = Functional.seq.init(DoublingGenerator);
//        AssertIterable.assertIterableEquals(new int[]{2,4,6,8,10,12,14,16,18,20,22}),Functional.take(11,output));
//    }
//
//    @Test
//    public void takeTest1()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        final List<Integer> output = Functional.collect(new Func<Integer, List<Integer>>() {
//            @Override
//            public List<Integer> apply(Integer o) {
//                return Functional.take(o, input);
//            }
//        }, input);
//        final List<Integer> expected = Arrays.asList(1,1,2,1,2,3,1,2,3,4,1,2,3,4,5,1,2,3,4,5,6,1,2,3,4,5,6,7,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,10);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void takeNoExceptionTest1()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        final List<Integer> output = Functional.collect(new Func<Integer, List<Integer>>() {
//            @Override
//            public List<Integer> apply(Integer o) {
//                return Functional.noException.take(o, input);
//            }
//        }, input);
//        final List<Integer> expected = Arrays.asList(1,1,2,1,2,3,1,2,3,4,1,2,3,4,5,1,2,3,4,5,6,1,2,3,4,5,6,7,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,10);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void takeNoExceptionTest2()
//    {
//        final List<Integer> input = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        final List<Integer> output = Functional.collect(new Func<Integer, List<Integer>>() {
//            @Override
//            public List<Integer> apply(Integer o) {
//                return Functional.noException.take(o+5, input);
//            }
//        }, input);
//        final List<Integer> expected = Arrays.asList(1,2,3,4,5,6,1,2,3,4,5,6,7,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqTakeTest1()
//    {
//        final List<Integer> input = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        final List<Integer> output = Functional.collect(new Func<Integer, List<Integer>>() {
//            @Override
//            public List<Integer> apply(Integer o) {
//                return Functional.toList(Functional.seq.take(o, input));
//            }
//        }, input);
//        final List<Integer> expected = Arrays.asList(1,1,2,1,2,3,1,2,3,4,1,2,3,4,5,1,2,3,4,5,6,1,2,3,4,5,6,7,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,10);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqTakeTest2()
//    {
//        final List<Integer> input = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        final List<Integer> output = Functional.collect(new Func<Integer, List<Integer>>() {
//            @Override
//            public List<Integer> apply(Integer o) {
//                return Functional.toList(Functional.seq.take(o, input));
//            }
//        }, input);
//        final List<Integer> expected = Arrays.asList(1, 1, 2, 1, 2, 3, 1, 2, 3, 4, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final int element : expected)
//        {
//            final int next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void takeWhileTest1()
//    {
//        final List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final List<Integer> output = Functional.takeWhile(Functional.isEven, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(1);
//            final List<Integer> output = Functional.takeWhile(Functional.isOdd, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(1,2,3,4);
//            final List<Integer> output = Functional.takeWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <= 4;
//                }
//            }, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
//            final List<Integer> output = Functional.takeWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <= 6;
//                }
//            }, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void takeWhileTest2()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        Functional.takeWhile(null, input);
//    }
//
//    @Test
//    public void seqTakeWhileTest1()
//    {
//        final List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final List<Integer> output = Functional.toList(Functional.seq.takeWhile(Functional.isEven, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(1);
//            final List<Integer> output = Functional.toList(Functional.seq.takeWhile(Functional.isOdd, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(1,2,3,4);
//            final List<Integer> output = Functional.toList(Functional.seq.takeWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <= 4;
//                }
//            }, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
//            final List<Integer> output = Functional.toList(Functional.seq.takeWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <= 6;
//                }
//            }, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void seqTakeWhileTest2()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        Functional.seq.takeWhile(null, input);
//    }
//
//    @Test
//    public void seqTakeWhileTest3()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        int counter=10;
//        final Iterable<Integer> integers = Functional.seq.takeWhile(Functional.constant(true), input);
//        final Iterator<Integer> iterator = integers.iterator();
//        while(counter>=0)
//        {
//            Assert.assertTrue(iterator.hasNext());
//            --counter;
//        }
//        int next = iterator.next();
//        Assert.assertEquals(1,next);
//        next = iterator.next();
//        Assert.assertEquals(2,next);
//        next = iterator.next();
//        Assert.assertEquals(3,next);
//        next = iterator.next();
//        Assert.assertEquals(4, next);
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void iterableHasNextTest()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        int counter=10;
//        final Iterator<Integer> iterator = input.iterator();
//        while(counter>=0)
//        {
//            Assert.assertTrue(iterator.hasNext());
//            --counter;
//        }
//        int next = iterator.next();
//        Assert.assertEquals(1,next);
//        next = iterator.next();
//        Assert.assertEquals(2,next);
//        next = iterator.next();
//        Assert.assertEquals(3,next);
//        next = iterator.next();
//        Assert.assertEquals(4,next);
//        Assert.assertFalse(iterator.hasNext());
//    }
//
//    @Test
//    public void ConstantInitialiserTest1()
//    {
//        final int howMany = 6;
//        final int initValue = -1;
//        final IntList l = Functional.init(Functional.constant(initValue),howMany);
//        Assert.assertEquals(howMany, l.size());
//        for(final int i : l)
//            Assert.assertEquals(initValue, i);
//    }
//
//    /*[Test]
//    public void SwitchTest1()
//    {
//        Assert.AreEqual(1,
//                Functional.Switch(10,
//                        new[]
//        {
//            Case.ToCase((int a) => a < 5, a => -1),
//            Case.ToCase((int a) => a > 5, a => 1)
//        }, a => 0));
//    } */
//
//    /*[Test]
//    public void TryTest1()
//    {
//        int zero = 0;
//        int results = Functional.Try<int,int,DivideByZeroException>(10, a => a/zero, a => a);
//        Assert.AreEqual(10, results);
//    }*/
//    private class A
//    {
//        public String name;
//        public int id;
//    }
//    /*[Test]
//    public void CaseTest2()
//    {
//        var c1 = new List<Func<A, object>> {(A a) => (object) a.name, (A a) => (object) a.id};
//
//        Func<A, IEnumerable<Func<int, object>>> c2 =
//                a => c1.Select<Func<A, object>, Func<int, object>>(f => j => f(a));
//
//        Func<A, IEnumerable<Functional.Case<int, object>>> cases =
//                a => c2(a).Select((f, i) => Case.ToCase(i.Equals, f));
//
//        var theA = new A {id = 1, name = "one"};
//
//        IEnumerable<object> results =
//                Enumerable.Range(0, 3).Select(i => Functional.Switch(i, cases(theA), aa => "oh dear"));
//        var expected = new object[] {"one", 1, "oh dear"};
//        CollectionAssert.AreEquivalent(expected, results);
//    }*/
//
//    /*[Test]
//    public void IgnoreTest1()
//    {
//        var input = new[] {1, 2, 3, 4, 5};
//        var output = new List<string>();
//        Func<int, string> format = i => string.Format("Discarding {0}", i);
//        List<string> expected = new[] {1, 2, 3, 4, 5}.Select(format).ToList();
//        Func<int, bool> f = i =>
//        {
//            output.Add(format(i));
//            return true;
//        };
//        input.Select(f).Ignore();
//        CollectionAssert.AreEquivalent(expected, output);
//    }*/
//    @Test
//    public void ChooseTest1() throws OptionNoValueAccessException
//    {
//        final IntList input = new int[] {1, 2, 3, 4, 5});
//        final IntList expected = new int[] {1, 3, 5});
//        final IntList output =
//                Functional.choose(
//                        new Func<Integer, Option<Integer>>() {
//                            @Override
//                            public Option<Integer> apply(Integer i) {
//                                return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
//                            }
//                        }, input);
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void ChooseTest2() throws OptionNoValueAccessException
//    {
//        final Collection<String> input = Arrays.asList(new String[] {"abc", "def"});
//        final Collection<Character> expected = Arrays.asList(new Character[]{'a'});
//        final Collection<Character> output =
//                Functional.choose(
//                        new Func<String, Option<Character>>() {
//                            @Override
//                            public Option<Character> apply(String str) {
//                                return str.startsWith("a") ? Option.toOption('a') : Option.<Character>None();
//                            }
//                        }
//                        ,input
//                );
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void ChooseTest3() throws OptionNoValueAccessException
//    {
//        final IntList input = new int[] {1, 2, 3, 4, 5});
//        final IntList expected = new int[] {1, 3, 5});
//        final IntList output = Functional.choose(
//                new Func<Integer, Option<Integer>>() {
//                    @Override
//                    public Option<Integer> apply(Integer i) {
//                        return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
//                    }
//                }, input)  ;
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    /*[Test]
//    public void CurryTest1()
//    {
//        Func<int, int, bool> f = (i, j) => i > j;
//        Func<int, Func<int, bool>> g = i => j => f(i, j);
//        bool t = 10.in(g(5));
//        Assert.IsFalse(t);
//    }*/
//
//    /*[Test]
//    public void CurryTest2()
//    {
//        Func<int, int, bool> f = (i, j) => i < j;
//        Func<int, Func<int, bool>> g = i => j => f(i, j);
//        bool t = 10.in(g(5));
//        Assert.IsTrue(t);
//    }*/
//
//    /*[Test]
//    public void CompositionTest1()
//    {
//        Func<int, int, int> add = (x, y) => x + y;
//        Func<int, Func<int, int>> add1 = y => x => add(x, y);
//        Func<int, int, int> mult = (x, y) => x*y;
//        Func<int, Func<int, int>> mult1 = y => x => mult(x, y);
//        int expected = mult(add(1, 2), 3);
//        Assert.AreEqual(9, expected);
//        Assert.AreEqual(expected, 2.in(add1(1).then(mult1(3))));
//    }*/
//
//    @Test
//    public void UnzipTest1()
//    {
//        final Collection<Pair<String,Integer>> input =
//                new ArrayList<Pair<String,Integer>>();
//        input.add(new Pair<String,Integer>("1", 1));
//        input.add(new Pair<String,Integer>("2", 2));
//
//        final Pair<Collection<String>,IntList> expected =
//                new Pair(
//                        Arrays.asList(new String[]{"1", "2"}),
//                        new int[]{1,2}));
//
//        final Pair<List<String>,List<Integer>> output = Functional.unzip(input);
//
//        AssertIterable.assertIterableEquals(expected.getValue0(), output.getValue0());
//        AssertIterable.assertIterableEquals(expected.getValue1(), output.getValue1());
//    }
//
//    @Test
//    public void Unzip3Test1()
//    {
//        final Collection<Triplet<String,Integer,String>> input =
//                new ArrayList<Triplet<String,Integer,String>>();
//        input.add(new Triplet<String,Integer,String>("1", 1,"L"));
//        input.add(new Triplet<String,Integer,String>("2", 2,"M"));
//        input.add(new Triplet<String,Integer,String>("3", 3,"K"));
//
//        final Triplet<Collection<String>,IntList,Collection<String>> expected =
//                new Triplet(
//                        Arrays.asList(new String[]{"1", "2","3"}),
//                        new int[]{1,2,3}),
//                        Arrays.asList(new String[]{"L","M","K"}));
//
//        final Triplet<List<String>,List<Integer>,List<String>> output = Functional.unzip3(input);
//
//        AssertIterable.assertIterableEquals(expected.getValue0(), output.getValue0());
//        AssertIterable.assertIterableEquals(expected.getValue1(), output.getValue1());
//        AssertIterable.assertIterableEquals(expected.getValue2(), output.getValue2());
//    }
//
//    @Test
//    public void ZipTest1()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//
//        final Collection<Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
//        expected.add(new Pair<Integer, Character>(1, 'a'));
//        expected.add(new Pair<Integer, Character>(2, 'b'));
//        expected.add(new Pair<Integer, Character>(3, 'c'));
//        expected.add(new Pair<Integer, Character>(4, 'd'));
//        expected.add(new Pair<Integer, Character>(5, 'e'));
//
//        final Collection<Pair<Integer,Character>> output = Functional.zip(input1, input2);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void ZipNoExceptionTest1()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//
//        final Collection<Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
//        expected.add(new Pair<Integer, Character>(1, 'a'));
//        expected.add(new Pair<Integer, Character>(2, 'b'));
//        expected.add(new Pair<Integer, Character>(3, 'c'));
//        expected.add(new Pair<Integer, Character>(4, 'd'));
//        expected.add(new Pair<Integer, Character>(5, 'e'));
//
//        final Collection<Pair<Integer,Character>> output = Functional.noException.zip(input1, input2);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void ZipNoExceptionTest2()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5, 6});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//
//        final Collection<Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
//        expected.add(new Pair<Integer, Character>(1, 'a'));
//        expected.add(new Pair<Integer, Character>(2, 'b'));
//        expected.add(new Pair<Integer, Character>(3, 'c'));
//        expected.add(new Pair<Integer, Character>(4, 'd'));
//        expected.add(new Pair<Integer, Character>(5, 'e'));
//
//        final Collection<Pair<Integer,Character>> output = Functional.noException.zip(input1, input2);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqZipTest1()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//
//        final Collection<Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
//        expected.add(new Pair<Integer, Character>(1, 'a'));
//        expected.add(new Pair<Integer, Character>(2, 'b'));
//        expected.add(new Pair<Integer, Character>(3, 'c'));
//        expected.add(new Pair<Integer, Character>(4, 'd'));
//        expected.add(new Pair<Integer, Character>(5, 'e'));
//
//        final Collection<Pair<Integer,Character>> output = Functional.toList(Functional.seq.zip(input1, input2));
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqZipTest2()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[]{'a', 'b', 'c', 'd', 'e'});
//
//        final Collection<Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
//        expected.add(new Pair<Integer, Character>(1, 'a'));
//        expected.add(new Pair<Integer, Character>(2, 'b'));
//        expected.add(new Pair<Integer, Character>(3, 'c'));
//        expected.add(new Pair<Integer, Character>(4, 'd'));
//        expected.add(new Pair<Integer, Character>(5, 'e'));
//
//        final Collection<Pair<Integer,Character>> output = Functional.toList(Functional.seq.zip(input1, input2));
//        final Iterator<Pair<Integer,Character>> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final Pair<Integer,Character> element : expected)
//        {
//            final Pair<Integer,Character> next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void seqZipFnTest1()
//    {
//        final IntList input = new int[] {1, 2, 3, 4, 5});
//
//        final Collection<Pair<Integer,String>> expected = new ArrayList<Pair<Integer, String>>();
//        expected.add(new Pair<Integer, String>(1, "1"));
//        expected.add(new Pair<Integer, String>(2, "2"));
//        expected.add(new Pair<Integer, String>(3, "3"));
//        expected.add(new Pair<Integer, String>(4, "4"));
//        expected.add(new Pair<Integer, String>(5, "5"));
//
//        final List<Pair<Integer, String>> output = Functional.toList(Functional.seq.zip(Functional.<Integer>identity(), Functional.dStringify(), input));
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void Zip3Test1()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//        final Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});
//
//        final Collection<Triplet<Integer,Character,Double>> expected = new ArrayList<Triplet<Integer, Character, Double>>();
//        expected.add(new Triplet<Integer, Character, Double>(1, 'a', 1.0));
//        expected.add(new Triplet<Integer, Character, Double>(2, 'b', 2.0));
//        expected.add(new Triplet<Integer, Character, Double>(3, 'c', 2.5));
//        expected.add(new Triplet<Integer, Character, Double>(4, 'd', 3.0));
//        expected.add(new Triplet<Integer, Character, Double>(5, 'e', 3.5));
//
//        final Collection<Triplet<Integer,Character,Double>> output = Functional.zip3(input1, input2, input3);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void Zip3NoExceptionTest1()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//        final Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});
//
//        final Collection<Triplet<Integer,Character,Double>> expected = new ArrayList<Triplet<Integer, Character, Double>>();
//        expected.add(new Triplet<Integer, Character, Double>(1, 'a', 1.0));
//        expected.add(new Triplet<Integer, Character, Double>(2, 'b', 2.0));
//        expected.add(new Triplet<Integer, Character, Double>(3, 'c', 2.5));
//        expected.add(new Triplet<Integer, Character, Double>(4, 'd', 3.0));
//        expected.add(new Triplet<Integer, Character, Double>(5, 'e', 3.5));
//
//        final Collection<Triplet<Integer,Character,Double>> output = Functional.noException.zip3(input1, input2, input3);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void Zip3NoExceptionTest2()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5, 6});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//        final Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});
//
//        final Collection<Triplet<Integer,Character,Double>> expected = new ArrayList<Triplet<Integer, Character, Double>>();
//        expected.add(new Triplet<Integer, Character, Double>(1, 'a', 1.0));
//        expected.add(new Triplet<Integer, Character, Double>(2, 'b', 2.0));
//        expected.add(new Triplet<Integer, Character, Double>(3, 'c', 2.5));
//        expected.add(new Triplet<Integer, Character, Double>(4, 'd', 3.0));
//        expected.add(new Triplet<Integer, Character, Double>(5, 'e', 3.5));
//
//        final Collection<Triplet<Integer,Character,Double>> output = Functional.noException.zip3(input1, input2, input3);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqZip3Test1()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//        final Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});
//
//        final Collection<Triplet<Integer,Character,Double>> expected = new ArrayList<Triplet<Integer, Character, Double>>();
//        expected.add(new Triplet<Integer, Character, Double>(1, 'a', 1.0));
//        expected.add(new Triplet<Integer, Character, Double>(2, 'b', 2.0));
//        expected.add(new Triplet<Integer, Character, Double>(3, 'c', 2.5));
//        expected.add(new Triplet<Integer, Character, Double>(4, 'd', 3.0));
//        expected.add(new Triplet<Integer, Character, Double>(5, 'e', 3.5));
//
//        final Collection<Triplet<Integer,Character,Double>> output = Functional.toList(Functional.seq.zip3(input1, input2, input3));
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqZip3Test2()
//    {
//        final IntList input1 = new int[] {1, 2, 3, 4, 5});
//        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
//        final Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});
//
//        final Collection<Triplet<Integer,Character,Double>> expected = new ArrayList<Triplet<Integer, Character, Double>>();
//        expected.add(new Triplet<Integer, Character, Double>(1, 'a', 1.0));
//        expected.add(new Triplet<Integer, Character, Double>(2, 'b', 2.0));
//        expected.add(new Triplet<Integer, Character, Double>(3, 'c', 2.5));
//        expected.add(new Triplet<Integer, Character, Double>(4, 'd', 3.0));
//        expected.add(new Triplet<Integer, Character, Double>(5, 'e', 3.5));
//
//        final Collection<Triplet<Integer,Character,Double>> output = Functional.toList(Functional.seq.zip3(input1, input2, input3));
//        final Iterator<Triplet<Integer,Character,Double>> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final Triplet<Integer,Character,Double> element : expected)
//        {
//            final Triplet<Integer,Character,Double> next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    /*[ExpectedException(typeof(ArgumentException))]
//        [Test]
//    public void Zip3Test2()
//    {
//        var input1 = new[] { 1, 2, 3, 4, 5 };
//        var input2 = new[] { 'a', 'b', 'd', 'e' };
//        var input3 = new[] { 1.0, 2.0, 2.5, 3.0, 3.5 };
//        var expected = new[]
//        {
//            Tuple.Create(1, 'a', 1.0), Tuple.Create(2, 'b', 2.0), Tuple.Create(3, 'c', 2.5),
//                    Tuple.Create(4, 'd', 3.0), Tuple.Create(5, 'e', 3.5)
//        }.ToList();
//
//        var output = Functional.Zip3(input1, input2, input3).ToList();
//
//        CollectionAssert.AreEquivalent(expected, output);
//    }*/
//
//    /*[ExpectedException(typeof(ArgumentException))]
//        [Test]
//    public void Zip3Test3()
//    {
//        var input1 = new[] { 1, 2, 3, 4, 5 };
//        var input2 = new[] { 'a', 'b', 'c', 'd', 'e' };
//        var input3 = new[] { 1.0, 2.0, 2.5, 3.5 };
//        var expected = new[]
//        {
//            Tuple.Create(1, 'a', 1.0), Tuple.Create(2, 'b', 2.0), Tuple.Create(3, 'c', 2.5),
//                    Tuple.Create(4, 'd', 3.0), Tuple.Create(5, 'e', 3.5)
//        }.ToList();
//
//        var output = Functional.Zip3(input1, input2, input3).ToList();
//
//        CollectionAssert.AreEquivalent(expected, output);
//    }*/
//
//    @Test
//    public void findTest1()
//    {
//        final String trueMatch = "6";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Assert.assertEquals(trueMatch,
//                Functional.find(
//                        new Func<String, Boolean>() {
//                            @Override
//                            public Boolean apply(String s) {
//                                return s.equals(trueMatch);
//                            }
//                        }, ls));
//    }
//
//    @Test(expected = NoSuchElementException.class)
//    public void findTest2()
//    {
//        final String falseMatch = "7";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Functional.find(
//                new Func<String, Boolean>() {
//                    @Override
//                    public Boolean apply(String s) {
//                        return s.equals(falseMatch);
//                    }
//                }, ls);
//    }
//
//    @Test
//    public void findNoExceptionTest1()
//    {
//        final String trueMatch = "6";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Assert.assertEquals(trueMatch,
//                Functional.noException.find(
//                        new Func<String, Boolean>() {
//                            @Override
//                            public Boolean apply(String s) {
//                                return s.equals(trueMatch);
//                            }
//                        }, ls).Some());
//    }
//
//    @Test
//    public void findNoExceptionTest2()
//    {
//        final String falseMatch = "7";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Assert.assertTrue(Functional.noException.find(
//                new Func<String, Boolean>() {
//                    @Override
//                    public Boolean apply(String s) {
//                        return s.equals(falseMatch);
//                    }
//                }, ls).isNone());
//    }
//
//    @Test
//    public void findIndexTest1()
//    {
//        final String trueMatch = "6";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Assert.assertEquals(2,
//                Functional.findIndex(
//                        new Func<String, Boolean>() {
//                            @Override
//                            public Boolean apply(String s) {
//                                return s.equals(trueMatch);
//                            }
//                        }, ls));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void findIndexTest2()
//    {
//        final String falseMatch = "7";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Functional.findIndex(
//                new Func<String, Boolean>() {
//                    @Override
//                    public Boolean apply(String s) {
//                        return s.equals(falseMatch);
//                    }
//                }, ls);
//    }
//
//    @Test
//    public void findIndexNoExceptionTest1()
//    {
//        final String trueMatch = "6";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Assert.assertEquals((Integer) 2,
//                Functional.noException.findIndex(
//                        new Func<String, Boolean>() {
//                            @Override
//                            public Boolean apply(String s) {
//                                return s.equals(trueMatch);
//                            }
//                        }, ls).Some());
//    }
//
//    @Test
//    public void findIndexNoExceptionTest2()
//    {
//        final String falseMatch = "7";
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Collection<String> ls = Functional.map(Functional.<Integer>dStringify(), li);
//        Assert.assertTrue(Functional.noException.findIndex(
//                new Func<String, Boolean>() {
//                    @Override
//                    public Boolean apply(String s) {
//                        return s.equals(falseMatch);
//                    }
//                }, ls).isNone());
//    }
//
//    @Test
//    public void pickTest1()
//    {
//        final int trueMatch = 6;
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        Assert.assertEquals(((Integer) trueMatch).toString(),
//                Functional.pick(
//                        new Func<Integer, Option<String>>() {
//                            @Override
//                            public Option<String> apply(Integer a) {
//                                return a == trueMatch ? Option.toOption(a.toString()) : Option.<String>None();
//                            }
//                        }, li));
//    }
//
//    @Test(expected = NoSuchElementException.class)
//    public void pickTest2()
//    {
//        final int falseMatch = 7;
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        Functional.pick(
//                new Func<Integer, Option<String>>() {
//                    @Override
//                    public Option<String> apply(Integer a) {
//                        return a == falseMatch ? Option.toOption(a.toString()) : Option.<String>None();
//                    }
//                }, li);
//    }
//
//    @Test
//    public void pickNoExceptionTest1()
//    {
//        final int trueMatch = 6;
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        Assert.assertEquals(((Integer) trueMatch).toString(),
//                Functional.noException.pick(
//                        new Func<Integer, Option<String>>() {
//                            @Override
//                            public Option<String> apply(Integer a) {
//                                return a == trueMatch ? Option.toOption(a.toString()) : Option.<String>None();
//                            }
//                        }, li).Some());
//    }
//
//    @Test
//    public void pickNoExceptionTest2()
//    {
//        final int falseMatch = 7;
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        Assert.assertTrue(Functional.noException.pick(
//                new Func<Integer, Option<String>>() {
//                    @Override
//                    public Option<String> apply(Integer a) {
//                        return a == falseMatch ? Option.toOption(a.toString()) : Option.<String>None();
//                    }
//                }, li).isNone());
//    }
//
//    @Test
//    public void curryFnTest1()
//    {
//        final int state=0;
//        final Func<Integer,Boolean> testForPosInts = new Func<Integer, Boolean>() {
//            @Override public Boolean apply(Integer integer) { return integer > state; }
//        };
//
//        final Func<Iterable<Integer>,List<Integer>> curriedTestForPosInts = Functional.filter(testForPosInts);
//
//        final IntList l = new int[]{-3,-2,0,1,5});
//        final IntList posInts = curriedTestForPosInts.apply(l);
//
//        final IntList expected = new int[]{1, 5});
//        AssertIterable.assertIterableEquals(expected, posInts);
//    }
//
//    @Test
//    public void MapDictTest1()
//    {
//        final IntList input = new int[]{1, 2, 3, 4, 5});
//        final Map<String,String> output = Functional.map_dict(
//                new Func<Integer, Map.Entry<String, String>>() {
//                    @Override
//                    public Map.Entry<String, String> apply(final Integer i) {
//                        return new Map.Entry<String, String>() {
//                            public String setValue(String v) {
//                                throw new UnsupportedOperationException();
//                            }
//
//                            public String getValue() {
//                                return Functional.<Integer>dStringify().apply(i);
//                            }
//
//                            public String getKey() {
//                                return Functional.<Integer>dStringify().apply(i);
//                            }
//                        };
//                    }
//                }, input);
//
//        final List<String> keys = new ArrayList<String>(output.keySet());
//        Collections.sort(keys);
//        AssertIterable.assertIterableEquals(Arrays.asList(new String[]{"1", "2", "3", "4", "5"}), keys);
//    }
//
//    @Test
//    public void ToListTest1()
//    {
//        final Iterable<Integer> output = Functional.init(DoublingGenerator, 5);
//        final List<Integer> output_ints = Functional.toList(output);
//        AssertIterable.assertIterableEquals(new int[]{2,4,6,8,10}), output_ints);
//    }
//
//    public static final Func<Integer,List<Integer>> repeat(final int howMany)
//    {
//        return new Func<Integer, List<Integer>>() {
//            @Override
//            public List<Integer> apply(final Integer integer) {
//                return Functional.init(
//                        new Func<Integer, Integer>() {
//                            @Override
//                            public Integer apply(Integer counter) {
//                                return integer;
//                            }
//                        }, howMany);
//            }
//        };
//    }
//
//    @Test
//    public void CollectTest1()
//    {
//        final List<Integer> input = Functional.init(DoublingGenerator, 5);
//        final List<Integer> output = Functional.collect(repeat(3), input);
//        final List<Integer> expected = Arrays.asList(2, 2, 2, 4, 4, 4, 6, 6, 6, 8, 8, 8, 10, 10, 10);
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqCollectTest1()
//    {
//        final Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
//        final Iterable<Integer> output = Functional.seq.collect(repeat(3), input);
//        final List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void seqCollectTest2()
//    {
//        final Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
//        final Iterable<Integer> output1 = Functional.seq.collect(repeat(3), input);
//        final Iterable<Integer> output2 = output1;
//        final List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
//        AssertIterable.assertIterableEquals(expected, output1);
//        AssertIterable.assertIterableEquals(expected, output2);
//    }
//
//    @Test
//    public void seqCollectTest3()
//    {
//        final Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
//        final Iterable<Integer> output = Functional.seq.collect(repeat(3), input);
//        final List<Integer> expected = Arrays.asList(2, 2, 2, 4, 4, 4, 6, 6, 6, 8, 8, 8, 10, 10, 10);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final int element : expected)
//        {
//            final int next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void takeNandYieldTest1()
//    {
//        final Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
//        final Pair<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input, 2);
//        final List<Integer> expectedList = Arrays.asList(2,4);
//        final List<Integer> expectedRemainder = Arrays.asList(6, 8, 10);
//        AssertIterable.assertIterableEquals(expectedList,output.getValue0());
//        AssertIterable.assertIterableEquals(expectedRemainder, output.getValue1());
//    }
//
//    @Test
//    public void takeNandYieldTest2()
//    {
//        final Iterable<Integer> input = Functional.seq.init(DoublingGenerator, 5);
//        final Pair<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input, 0);
//        final List<Integer> expectedList = Arrays.asList();
//        final List<Integer> expectedRemainder = Arrays.asList(2,4,6,8,10);
//        AssertIterable.assertIterableEquals(expectedList,output.getValue0());
//        AssertIterable.assertIterableEquals(expectedRemainder,output.getValue1());
//    }
//
//    @Test
//    public void recFilterTest1()
//    {
//        final IntList l = Functional.init(DoublingGenerator, 5);
//        final Iterable<Integer> oddElems = Functional.rec.filter(Functional.isOdd, l);
//
//        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
//    }
//
//    @Test
//    public void recMapTest1()
//    {
//        final IntList input = new int[]{1, 2, 3, 4, 5});
//        final Iterable<String> output = Functional.rec.map(Functional.<Integer>dStringify(), input);
//        AssertIterable.assertIterableEquals(Arrays.asList("1", "2", "3", "4", "5"), output);
//    }
//
//    @Test
//    public void recFoldvsMapTest1()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final String s1 = Functional.join(",", Functional.rec.map(Functional.<Integer>dStringify(), li));
//        Assert.assertEquals("2,4,6,8,10", s1);
//        final String s2 = Functional.rec.fold(
//                new Func2<String, Integer, String>() {
//                    @Override
//                    public String apply(String s1, Integer s2) {
//                        return csv(s1, s2);
//                    }
//                }, "", li);
//        Assert.assertEquals(s1, s2);
//    }
//
//    @Test
//    public void IfTest1()
//    {
//        final Integer input = 1;
//        final Iterable2<Integer> i = IterableHelper.asList(0, 1, 2);
//        final Iterable2<Integer> result = i.map(
//                new Func<Integer, Integer>() {
//                    public Integer apply(Integer ii) {
//                        return Functional.If(input, Functional.greaterThan(ii), DoublingGenerator, TriplingGenerator);
//                    }
//                });
//        final List<Integer> expected = Arrays.asList(2, 3, 3);
//        AssertIterable.assertIterableEquals(expected, result);
//    }
//
//    @Test
//    public void switchTest1()
//    {
//        Assert.assertEquals(new Integer(1),
//                Functional.Switch(new Integer(10),
//                        Arrays.asList(
//
//                                Functional.toCase(Functional.lessThan(new Integer(5)), Functional.constant(new Integer(-1))),
//                                Functional.toCase(Functional.greaterThan(new Integer(5)), Functional.constant(new Integer(1)))
//                        ), Functional.constant(new Integer(0))));
//    }
//
//    @Test
//    public void setFilterTest1()
//    {
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Set<Integer> sl = new HashSet<Integer>(l);
//        final Set<Integer> oddElems = Functional.set.filter(Functional.isOdd, sl);
//
//        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
//    }
//
//    @Test
//    public void setFilterTest2()
//    {
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Set<Integer> sl = new HashSet<Integer>(l);
//        final Set<Integer> evenElems = Functional.set.filter(Functional.isEven, sl);
//
//        final IntList expected = new int[]{2,4,6,8,10});
//        Assert.assertTrue(expected.containsAll(evenElems));
//        Assert.assertTrue(evenElems.containsAll(expected));
//    }
//
//    @Test
//    public void setFilterTest3()
//    {
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Integer limit = 5;
//        final Set<Integer> sl = new HashSet<Integer>(l);
//        final Set<Integer> highElems = Functional.set.filter(
//                new Func<Integer,Boolean>()
//                {
//                    @Override
//                    public Boolean apply(Integer a) { return a > limit;}
//                }, sl);
//
//        final IntList expected = new int[]{6,8,10});
//        Assert.assertTrue(expected.containsAll(highElems));
//        Assert.assertTrue(highElems.containsAll(expected));
//    }
//
//    @Test
//    public void setFilterTest4()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 5);
//        final Integer limit = 10;
//        final Set<Integer> sl = new HashSet<Integer>(li);
//        final Set<Integer> output = Functional.set.filter(
//                new Func<Integer, Boolean>() {
//                    @Override
//                    public Boolean apply(Integer a) {
//                        return a > limit;
//                    }
//                }, sl);
//
//        Assert.assertFalse(output.iterator().hasNext());
//    }
//
//    @Test
//    public void setFilterTest5()
//    {
//        final IntList li = Functional.init(DoublingGenerator, 10);
//        final IntList expected = new int[]{4, 8, 12, 16, 20});
//        final Set<Integer> sl = new HashSet<Integer>(li);
//        final Set<Integer> output = Functional.set.filter(
//                new Func<Integer,Boolean>()
//                {
//                    @Override public Boolean apply(Integer a) {return a % 4 ==0;}
//                }, sl);
//
//        Assert.assertTrue(expected.containsAll(output));
//        Assert.assertTrue(output.containsAll(expected));
//    }
//
//    @Test
//    public void setCollectTest1()
//    {
//        final Iterable<Integer> input = Functional.init(DoublingGenerator, 5);
//        final Set<Integer> output = Functional.set.collect(repeat(3), input);
//        final Set<Integer> expected = new HashSet<Integer>(Arrays.asList(2,4,6,8,10));
//
//        Assert.assertTrue(expected.containsAll(output));
//        Assert.assertTrue(output.containsAll(expected));
//    }
//
//    @Test
//    public void setCollectTest2()
//    {
//        final Iterable<Integer> input = Functional.init(DoublingGenerator, 5);
//        final Set<Integer> output1 = Functional.set.collect(repeat(3), input);
//        final Set<Integer> output2 = output1;
//        final Set<Integer> expected = new HashSet<Integer>(Arrays.asList(2,4,6,8,10));
//
//        Assert.assertTrue(expected.containsAll(output1));
//        Assert.assertTrue(output1.containsAll(expected));
//        Assert.assertTrue(expected.containsAll(output2));
//        Assert.assertTrue(output2.containsAll(expected));
//    }
//
//    @Test
//    public void setMapTest1()
//    {
//        final Set<Integer> input = new HashSet<Integer>(new int[]{1, 2, 3, 4, 5}));
//        final Set<String> output = Functional.set.map(Functional.<Integer>dStringify(), input);
//        final Set<String> expected = new HashSet<String>(Arrays.asList("1","2","3","4","5"));
//        Assert.assertTrue(expected.containsAll(output));
//        Assert.assertTrue(output.containsAll(expected));
//    }
//
//    @Test
//    public void setConcatTest1()
//    {
//        final Set<Integer> input = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//        final Func<Integer,Integer> doubler = new Func<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer i) {
//                return i * 2;
//            }
//        };
//        final Set<String> expected = new HashSet<String>(Arrays.asList("1","2","3","4","5","6","8","10"));
//
//        final Set<String> strs = Functional.set.map(Functional.<Integer>dStringify(), input);
//        final Set<String> output = Functional.set.concat(strs, Functional.set.map(Functional.<Integer>dStringify(), Functional.set.map(doubler, input)));
//
//        Assert.assertTrue(expected.containsAll(output));
//        Assert.assertTrue(output.containsAll(expected));
//    }
//
//    @Test
//    public void setIntersectionTest1()
//    {
//        final Set<Integer> input1 = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//        final Set<Integer> input2 = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//
//        final Set<Integer> intersection = Functional.set.intersection(input1,input2);
//        Assert.assertTrue(intersection.containsAll(input1));
//        Assert.assertTrue(input1.containsAll(intersection));
//    }
//
//    @Test
//    public void setIntersectionTest2()
//    {
//        final Set<Integer> input1 = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//        final Set<Integer> input2 = new HashSet<Integer>(Arrays.asList(4,5,6,7,8));
//        final Set<Integer> expected = new HashSet<Integer>(Arrays.asList(4,5));
//
//        final Set<Integer> intersection = Functional.set.intersection(input1, input2);
//        Assert.assertTrue(intersection.containsAll(expected));
//        Assert.assertTrue(expected.containsAll(intersection));
//    }
//
//    @Test
//    public void setIntersectionTest3()
//    {
//        final Set<Integer> input1 = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//        final Set<Integer> input2 = new HashSet<Integer>(Arrays.asList(6,7,8));
//        final Set<Integer> expected = Collections.emptySet();
//
//        final Set<Integer> intersection = Functional.set.intersection(input1, input2);
//        Assert.assertTrue(intersection.containsAll(expected));
//        Assert.assertTrue(expected.containsAll(intersection));
//    }
//
//    @Test
//    public void setAsymmetricDifferenceTest1()
//    {
//        final Set<Integer> input1 = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//        final Set<Integer> input2 = new HashSet<Integer>(Arrays.asList(4,5,6,7,8));
//        final Set<Integer> expected = new HashSet<Integer>(Arrays.asList(1,2,3));
//
//        final Set<Integer> diff = Functional.set.asymmetricDifference(input1, input2);
//        Assert.assertTrue(diff.containsAll(expected));
//        Assert.assertTrue(expected.containsAll(diff));
//        Assert.assertFalse(input2.containsAll(diff));
//    }
//
//    @Test
//    public void setAsymmetricDifferenceTest2()
//    {
//        final Set<Integer> input1 = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
//        final Set<Integer> input2 = new HashSet<Integer>(Arrays.asList(6,7,8));
//        final Set<Integer> expected = Collections.emptySet();
//
//        final Set<Integer> diff = Functional.set.asymmetricDifference(input1, input2);
//        Assert.assertTrue(diff.containsAll(input1));
//        Assert.assertTrue(input1.containsAll(diff));
//    }
//
//    @Test
//    public void appendTest1()
//    {
//        final Integer i = 1;
//        final IntList l = Functional.init(DoublingGenerator,5);
//        final Iterable<Integer> output = Functional.append(i, l);
//        final List<Integer> expected = Arrays.asList(1, 2, 4, 6, 8, 10);
//
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void unfoldTest1()
//    {
//        final Integer seed = 0;
//        final Func<Integer,Pair<Integer,Integer>> unspool = new Func<Integer, Pair<Integer, Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(Integer integer) {
//                return Pair.with(integer+1,integer+1);
//            }
//        };
//        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer) {
//                return integer==10;
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        final List<Integer> output = Functional.unfold(unspool,finished,seed);
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void unfoldAsDoublingGeneratorTest1()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Pair<Integer,Integer>> doubler = new Func<Integer, Pair<Integer, Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(Integer integer) {
//                return Pair.with(integer * 2, integer+1);
//            }
//        };
//        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer) {
//                return integer>10;
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final List<Integer> output = Functional.unfold(doubler,finished,seed);
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void unfoldAsDoublingGeneratorTest2()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Option<Pair<Integer,Integer>>> doubler = new Func<Integer, Option<Pair<Integer, Integer>>>() {
//            @Override
//            public Option<Pair<Integer, Integer>> apply(Integer integer) {
//                return integer>10 ? Option.<Pair<Integer, Integer>>None() : Option.toOption(Pair.with(integer * 2, integer+1));
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final List<Integer> output = Functional.unfold(doubler,seed);
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqUnfoldTest1()
//    {
//        final Integer seed = 0;
//        final Func<Integer,Pair<Integer,Integer>> unspool = new Func<Integer, Pair<Integer, Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(Integer integer) {
//                return Pair.with(integer+1,integer+1);
//            }
//        };
//        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer) {
//                return integer==10;
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        final Iterable<Integer> output = Functional.seq.unfold(unspool, finished, seed);
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqUnfoldTest2()
//    {
//        final Integer seed = 0;
//        final Func<Integer,Pair<Integer,Integer>> unspool = new Func<Integer, Pair<Integer, Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(Integer integer) {
//                return Pair.with(integer+1,integer+1);
//            }
//        };
//        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer) {
//                return integer==10;
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        final Iterable<Integer> output = Functional.seq.unfold(unspool, finished, seed);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final int element : expected)
//        {
//            final int next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void seqUnfoldAsDoublingGeneratorTest1()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Pair<Integer,Integer>> doubler = new Func<Integer, Pair<Integer, Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(Integer integer) {
//                return Pair.with(integer * 2, integer+1);
//            }
//        };
//        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer) {
//                return integer>10;
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final Iterable<Integer> output = Functional.seq.unfold(doubler,finished,seed);
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqUnfoldAsDoublingGeneratorTest2()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Option<Pair<Integer,Integer>>> doubler = new Func<Integer, Option<Pair<Integer, Integer>>>() {
//            @Override
//            public Option<Pair<Integer, Integer>> apply(Integer integer) {
//                return integer>10 ? Option.<Pair<Integer, Integer>>None() : Option.toOption(Pair.with(integer * 2, integer+1));
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final Iterable<Integer> output = Functional.seq.unfold(doubler,seed);
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqUnfoldAsDoublingGeneratorTest3()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Option<Pair<Integer,Integer>>> doubler = new Func<Integer, Option<Pair<Integer, Integer>>>() {
//            @Override
//            public Option<Pair<Integer, Integer>> apply(Integer integer) {
//                return integer>10 ? Option.<Pair<Integer, Integer>>None() : Option.toOption(Pair.with(integer * 2, integer+1));
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final Iterable<Integer> output = Functional.seq.unfold(doubler,seed);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final int element : expected)
//        {
//            final int next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void recUnfoldAsDoublingGeneratorTest1()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Pair<Integer,Integer>> doubler = new Func<Integer, Pair<Integer, Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(Integer integer) {
//                return Pair.with(integer * 2, integer+1);
//            }
//        };
//        final Func<Integer,Boolean> finished = new Func<Integer, Boolean>() {
//            @Override
//            public Boolean apply(Integer integer) {
//                return integer>10;
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final List<Integer> output = Functional.rec.unfold(doubler, finished, seed);
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void recUnfoldAsDoublingGeneratorTest2()
//    {
//        final Integer seed = 1;
//        final Func<Integer,Option<Pair<Integer,Integer>>> doubler = new Func<Integer, Option<Pair<Integer, Integer>>>() {
//            @Override
//            public Option<Pair<Integer, Integer>> apply(Integer integer) {
//                return integer>10 ? Option.<Pair<Integer, Integer>>None() : Option.toOption(Pair.with(integer * 2, integer+1));
//            }
//        };
//
//        final List<Integer> expected = Arrays.asList(2,4,6,8,10,12,14,16,18,20);
//        final List<Integer> output = Functional.rec.unfold(doubler, seed);
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void MapInTermsOfFoldTest1()
//    {
//        final IntList input = new int[]{1, 2, 3, 4, 5});
//        final Collection<String> output = Functional.inTermsOfFold.map(Functional.<Integer>dStringify(), input);
//        Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, output.toArray());
//    }
//
//    @Test
//    public void FilterInTermsOfFoldTest1()
//    {
//        final IntList l = Functional.init(DoublingGenerator, 5);
//        final Iterable<Integer> oddElems = Functional.inTermsOfFold.filter(Functional.isOdd, l);
//        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
//    }
//
//    @Test
//    public void InitInTermsOfUnfoldTest1()
//    {
//        final IntList output = Functional.inTermsOfFold.init(new Func<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer integer) {
//                return integer * 2;
//            }
//        }, 5);
//        Assert.assertArrayEquals(new Integer[]{2, 4, 6, 8, 10}, output.toArray());
//    }
//
//    @Test
//    public void skipTest1()
//    {
//        final List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);
//        {
//            final List<Integer> expected = Arrays.asList(1,2,3,4,5);
//            final List<Integer> output = Functional.skip(0,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(2,3,4,5);
//            final List<Integer> output = Functional.skip(1,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(3,4,5);
//            final List<Integer> output = Functional.skip(2,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(4,5);
//            final List<Integer> output = Functional.skip(3,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(5);
//            final List<Integer> output = Functional.skip(4,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final List<Integer> output = Functional.skip(5,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final List<Integer> output = Functional.skip(6,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void skipTest2()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        Functional.skip(-1, input);
//    }
//
//    @Test
//    public void seqSkipTest1()
//    {
//        final List<Integer> l = Arrays.asList(1,2,3,4,5);
//        {
//            final List<Integer> expected = Arrays.asList(1,2,3,4,5);
//            final Iterable<Integer> output = Functional.seq.skip(0, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(2,3,4,5);
//            final Iterable<Integer> output = Functional.seq.skip(1,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(3,4,5);
//            final Iterable<Integer> output = Functional.seq.skip(2,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(4,5);
//            final Iterable<Integer> output = Functional.seq.skip(3,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(5);
//            final Iterable<Integer> output = Functional.seq.skip(4,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final Iterable<Integer> output = Functional.seq.skip(5,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final Iterable<Integer> output = Functional.seq.skip(6,l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void seqSkipTest2()
//    {
//        final List<Integer> input = Arrays.asList(1,2,3,4);
//        Functional.seq.skip(-1, input);
//    }
//
//    @Test
//    public void seqSkipTest3()
//    {
//        final List<Integer> l = Arrays.asList(1,2,3,4,5);
//        final List<Integer> expected = Arrays.asList(3,4,5);
//        final Iterable<Integer> output = Functional.seq.skip(2,l);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final int element : expected)
//        {
//            final int next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void skipWhileTest1()
//    {
//        final List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);
//        {
//            final List<Integer> expected = Arrays.asList(1,2,3,4,5);
//            final List<? extends Integer> output = Functional.skipWhile(Functional.isEven, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(2,3,4,5);
//            final List<? extends Integer> output = Functional.skipWhile(Functional.isOdd, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(3,4,5);
//            final List<? extends Integer> output = Functional.skipWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <=2;
//                }
//            }, l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final List<? extends Integer> output = Functional.skipWhile(new Func<Integer,Boolean>() { public Boolean apply(final Integer i) { return i<=6;} },l);
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void skipWhileTest2()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        Functional.skipWhile(null, input);
//    }
//
//    @Test
//    public void skipWhileTest3()
//    {
//        final List<Number> input = new ArrayList<Number>();
//        for(int i=1;i<10;++i)
//            input.add(Integer.valueOf(i));
//
//        final List<? extends Number> output = Functional.skipWhile(new Func<Object, Boolean>() {
//            @Override
//            public Boolean apply(final Object number) {
//                return ((number instanceof Integer) && ((Integer)number % 2) == 1);
//            }
//        }, input);
//
//        final List<Integer> expected = Arrays.asList(2,3,4,5,6,7,8,9);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqSkipWhileTest1()
//    {
//        final List<Integer> l = Arrays.asList(1, 2, 3, 4, 5);
//        {
//            final List<Integer> expected = Arrays.asList(1,2,3,4,5);
//            final List<? extends Integer> output = Functional.toList(Functional.seq.skipWhile(Functional.isEven, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(2,3,4,5);
//            final List<? extends Integer> output = Functional.toList(Functional.seq.skipWhile(Functional.isOdd, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = Arrays.asList(3,4,5);
//            final List<? extends Integer> output = Functional.toList(Functional.seq.skipWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <= 2;
//                }
//            }, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//        {
//            final List<Integer> expected = new ArrayList<Integer>();
//            final List<? extends Integer> output = Functional.toList(Functional.seq.skipWhile(new Func<Integer, Boolean>() {
//                public Boolean apply(final Integer i) {
//                    return i <= 6;
//                }
//            }, l));
//            AssertIterable.assertIterableEquals(expected, output);
//        }
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void seqSkipWhileTest2()
//    {
//        final List<Integer> input = Arrays.asList(1, 2, 3, 4);
//        Functional.seq.skipWhile(null, input);
//    }
//
//    @Test
//    public void seqSkipWhileTest3()
//    {
//        final List<Number> input = new ArrayList<Number>();
//        for(int i=1;i<10;++i)
//            input.add(Integer.valueOf(i));
//
//        final List<? extends Number> output = Functional.toList(Functional.seq.skipWhile(new Func<Object, Boolean>() {
//            @Override
//            public Boolean apply(final Object number) {
//                return ((number instanceof Integer) && ((Integer) number % 2) == 1);
//            }
//        }, input));
//
//        final List<Integer> expected = Arrays.asList(2,3,4,5,6,7,8,9);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqSkipWhileTest4()
//    {
//        final List<Integer> l = Arrays.asList(1,2,3,4,5);
//        final List<Integer> expected = Arrays.asList(3,4,5);
//        final Iterable<Integer> output = Functional.seq.skipWhile(new Func<Integer, Boolean>() {
//            public Boolean apply(final Integer i) {
//                return i <= 2;
//            }
//        }, l);
//        final Iterator<Integer> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final int element : expected)
//        {
//            final int next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void extractFirstOfPair()
//    {
//        final List<Pair<Integer,String>> input = new ArrayList<Pair<Integer,String>>();
//        for(int i=0;i<5;++i) input.add(Pair.with(i, new Integer(i).toString()));
//        final List<Integer> output = Functional.map(Functional.<Integer,String>first(),input);
//        final List<Integer> expected = Arrays.asList(0,1,2,3,4);
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void extractSecondOfPair()
//    {
//        final List<Pair<Integer,String>> input = new ArrayList<Pair<Integer,String>>();
//        for(int i=0;i<5;++i) input.add(Pair.with(i, new Integer(i).toString()));
//        final List<String> output = Functional.map(Functional.<Integer,String>second(),input);
//        final List<String> expected = Arrays.asList("0","1","2","3","4");
//        AssertIterable.assertIterableEquals(expected, output);
//    }
//
//    @Test
//    public void groupByOddVsEvenInt()
//    {
//        final List<Integer> input = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        final Map<Boolean,List<Integer>> output = Functional.groupBy(Functional.isEven,input);
//        final Map<Boolean,List<Integer>> expected = new HashMap<Boolean, List<Integer>>();
//        expected.put(false,Arrays.asList(1,3,5,7,9));
//        expected.put(true, Arrays.asList(2, 4, 6, 8, 10));
//        AssertIterable.assertIterableEquals(expected.get(true), output.get(true));
//        AssertIterable.assertIterableEquals(expected.get(false),output.get(false));
//    }
//
//    @Test
//    public void groupByStringFirstTwoChar()
//    {
//        final List<String> input = Arrays.asList("aa","aab","aac","def");
//        final Map<String,List<String>> output = Functional.groupBy(new Func<String, String>() {
//            @Override
//            public String apply(final String s) {
//                return s.substring(0,1);
//            }
//        },input);
//        final Map<String,List<String>> expected = new HashMap<String, List<String>>();
//        expected.put("a",Arrays.asList("aa","aab","aac"));
//        expected.put("d",Arrays.asList("def"));
//        AssertIterable.assertIterableEquals(expected.get("a"), output.get("a"));
//        AssertIterable.assertIterableEquals(expected.get("d"),output.get("d"));
//        AssertIterable.assertIterableEquals(new TreeSet<String>(expected.keySet()),new TreeSet<String>(output.keySet()));
//    }
//
//    @Test
//    public void partitionRangesOfInt()
//    {
//        final int noElems = 13;
//        final int noPartitions = 5;
//        final List<Functional.Range<Integer>> partitions = Functional.partition(noElems,noPartitions);
//
//        final List<Integer> expectedStart = Arrays.asList(0,3,6,9,11);
//        final List<Integer> expectedEnd = Arrays.asList(3,6,9,11,13);
//        final List<Pair<Integer,Integer>> expected = Functional.zip(expectedStart,expectedEnd);
//
//        final List<Pair<Integer,Integer>> output = Functional.map(new Func<Functional.Range<Integer>, Pair<Integer,Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(final Functional.Range<Integer> range) {
//                return Pair.with(range.from(), range.to());
//            }
//        }, partitions);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void exactlyEvenPartitionRangesOfInt()
//    {
//        final int noElems = 10;
//        final int noPartitions = 5;
//        final List<Functional.Range<Integer>> partitions = Functional.partition(noElems,noPartitions);
//
//        final List<Integer> expectedStart = Arrays.asList(0,2,4,6,8);
//        final List<Integer> expectedEnd = Arrays.asList(2,4,6,8,10);
//        final List<Pair<Integer,Integer>> expected = Functional.zip(expectedStart,expectedEnd);
//
//        final List<Pair<Integer,Integer>> output = Functional.map(new Func<Functional.Range<Integer>, Pair<Integer,Integer>>() {
//            @Override
//            public Pair<Integer, Integer> apply(final Functional.Range<Integer> range) {
//                return Pair.with(range.from(), range.to());
//            }
//        }, partitions);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void partitionFewerRangesOfIntThanPartitionsRequested()
//    {
//        final int noElems = 7;
//        final int noPartitions = 10;
//        final List<Functional.Range<Integer>> partitions = Functional.partition(noElems,noPartitions);
//
//        final List<Integer> expectedStart = Arrays.asList(0,1,2,3,4,5,6);
//        final List<Integer> expectedEnd = Arrays.asList(1,2,3,4,5,6,7);
//        final List<Functional.Range<Integer>> expected =
//                Functional.concat(
//                    Functional.map(
//                        new Func<Pair<Integer,Integer>, Functional.Range<Integer>>() {
//                            public Functional.Range<Integer> apply(final Pair<Integer,Integer> pair)
//                            {
//                                return new Functional.Range<Integer>(pair.getValue0(), pair.getValue1());
//                            }
//                        }, Functional.zip(expectedStart, expectedEnd)),
//                    Functional.init(Functional.constant(new Functional.Range<Integer>(7,7)),3));
//
//        AssertIterable.assertIterableEquals(expected,partitions);
//    }
//
//    @Test
//    public void partitionRangesOfString()
//    {
//        final int noElems = 13;
//        final int noPartitions = 5;
//        final List<Functional.Range<String>> partitions =
//                Functional.partition(
//                        new Func<Integer, String>() {
//                            public String apply(final Integer i) {
//                                return new Integer(i - 1).toString();
//                            }
//                        },
//                        noElems, noPartitions);
//
//        final List<String> expectedStart = Arrays.asList("0","3","6","9","11");
//        final List<String> expectedEnd = Arrays.asList("3","6","9","11","13");
//        final List<Pair<String,String>> expected = Functional.zip(expectedStart,expectedEnd);
//
//        final List<Pair<String,String>> output = Functional.map(new Func<Functional.Range<String>, Pair<String,String>>() {
//            @Override
//            public Pair<String, String> apply(final Functional.Range<String> range) {
//                return Pair.with(range.from(), range.to());
//            }
//        }, partitions);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void partitionWithEmptySource()
//    {
//        Functional.partition(0,1);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void partitionWithZeroOutputRanges()
//    {
//        Functional.partition(1,0);
//    }
//
//    @Test
//    public void seqPartitionRangesOfString()
//    {
//        final int noElems = 13;
//        final int noPartitions = 5;
//        final List<Functional.Range<String>> partitions = Functional.toList(
//                Functional.seq.partition(
//                        new Func<Integer, String>() {
//                            public String apply(final Integer i) {
//                                return new Integer(i - 1).toString();
//                            }
//                        },
//                        noElems, noPartitions));
//
//        final List<String> expectedStart = Arrays.asList("0","3","6","9","11");
//        final List<String> expectedEnd = Arrays.asList("3","6","9","11","13");
//        final List<Pair<String,String>> expected = Functional.zip(expectedStart,expectedEnd);
//
//        final List<Pair<String,String>> output = Functional.map(new Func<Functional.Range<String>, Pair<String,String>>() {
//            @Override
//            public Pair<String, String> apply(final Functional.Range<String> range) {
//                return Pair.with(range.from(), range.to());
//            }
//        }, partitions);
//
//        AssertIterable.assertIterableEquals(expected,output);
//    }
//
//    @Test
//    public void seqPartitionRangesOfString2()
//    {
//        final int noElems = 13;
//        final int noPartitions = 5;
//        final Iterable<Functional.Range<String>> output =
//                Functional.seq.partition(
//                        new Func<Integer, String>() {
//                            public String apply(final Integer i) {
//                                return new Integer(i - 1).toString();
//                            }
//                        },
//                        noElems, noPartitions);
//
//        final List<String> expectedStart = Arrays.asList("0","3","6","9","11");
//        final List<String> expectedEnd = Arrays.asList("3","6","9","11","13");
//        final List<Pair<String,String>> expected_ = Functional.zip(expectedStart,expectedEnd);
//
//        final List<Functional.Range < String >> expected = Functional.map(new Func<Pair<String, String>, Functional.Range < String >> ()
//        {
//            @Override
//            public Functional.Range < String > apply(final Pair<String, String> pair) {
//                return new Functional.Range<String>(pair.getValue0(),pair.getValue1());
//            }
//        }, expected_);
//        final Iterator<Functional.Range<String>> iterator = output.iterator();
//
//        for(int i=0;i<20;++i)
//            Assert.assertTrue(iterator.hasNext());
//
//        for(final Functional.Range<String> element : expected)
//        {
//            final Functional.Range<String> next = iterator.next();
//            Assert.assertEquals(element,next);
//        }
//
//        Assert.assertFalse(iterator.hasNext());
//        try {
//            iterator.next();
//        } catch(final NoSuchElementException e) {
//            return;
//        }
//
//        Assert.fail("Should not reach this point");
//    }
//
//    @Test
//    public void toMutableDictionaryTest()
//    {
//        final Map<Integer,String> expected = new HashMap<Integer,String>();
//        expected.put(6, "6");
//        expected.put(12, "12");
//        final Map<Integer,String> output = Functional.toMutableDictionary(expected);
//        Assert.assertTrue(expected.entrySet().containsAll(output.entrySet()));
//        Assert.assertTrue(output.entrySet().containsAll(expected.entrySet()));
//        output.put(24, "24");
//        Assert.assertTrue(output.containsKey(24));
//        Assert.assertTrue(output.containsValue("24"));
//   }
//
//    @Test
//    public void toMutableListTest()
//    {
//        final List<String> expected = Arrays.asList("0","3","6","9","11");
//        final List<String> output = Functional.toMutableList(expected);
//        AssertIterable.assertIterableEquals(output,expected);
//        output.add("24");
//        Assert.assertTrue(output.contains("24"));
//    }
//
//    @Test
//    public void toMutableSetTest()
//    {
//        final List<String> expected = Arrays.asList("0","3","6","9","11");
//        final Set<String> output = Functional.toMutableSet(expected);
//        Assert.assertTrue(expected.containsAll(output));
//        Assert.assertTrue(output.containsAll(expected));
//        output.add("24");
//        Assert.assertTrue(output.contains("24"));
//    }
}