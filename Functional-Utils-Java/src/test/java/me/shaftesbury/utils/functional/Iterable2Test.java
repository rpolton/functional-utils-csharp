package me.shaftesbury.utils.functional;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 16/10/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class Iterable2Test
{
    private static Function<Integer,Integer> DoublingGenerator =
            new Function<Integer,Integer>()
            {
                 public Integer apply(Integer a) { return 2*a;}
            };

    @Test
    public void InitTest1()
    {
        final Iterable2<Integer> output = IterableHelper.init(DoublingGenerator, 5);
        AssertIterable.assertIterableEquals(Arrays.asList(2,4,6,8,10),output);
    }


    @Test
    public void MapTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1, 2, 3, 4, 5});
        final Iterable2<String> output = input.map(Functional.<Integer>dStringify());
        AssertIterable.assertIterableEquals(Arrays.asList("1","2","3","4","5"),output);
    }

    @Test
    public void SortWithTest1()
    {
        final Iterable2<Integer> i = IterableHelper.asList(new Integer[]{1,6,23,7,4});
        final Iterable2<Integer> output = i.sortWith(
                new Comparator<Integer>() {

                    public int compare(Integer a, Integer b) {
                        return Integer.compare(a, b);
                    }
                });
        AssertIterable.assertIterableEquals(Arrays.asList(1,4,6,7,23), output);
    }

    @Test
    public void SortWithTest2()
    {
        final Iterable2<Integer> i = IterableHelper.asList(new Integer[] { 1, 6, 23, 7, 4 });
        final Iterable2<Integer> j = i.sortWith(
                new Comparator<Integer>() {

                    public int compare(Integer a, Integer b) {
                        return a - b;
                    }
                });
        AssertIterable.assertIterableEquals(Arrays.asList(1,4,6,7,23), j);
    }

    @Test
    public void SortWithTest3()
    {
        final Iterable2<Integer> i = IterableHelper.asList(new Integer[] { 1, 6, 23, 7, 4 });
        final Iterable2<Integer> j = i.sortWith(Functional.dSorter);
        AssertIterable.assertIterableEquals(Arrays.asList(1,4,6,7,23), j);
    }

    private static Function<Integer, Integer> TriplingGenerator =
            new Function<Integer, Integer>() {

                public Integer apply(final Integer a) {
                    return 3 * a;
                }
            };

    private static Function<Integer, Integer> QuadruplingGenerator =
            new Function<Integer, Integer>() {

                public Integer apply(final Integer a) {
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
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> m = IterableHelper.init(QuadruplingGenerator, 5);
        try
        {
            Assert.assertTrue(l.forAll2(
                    new BiFunction<Integer, Integer, Boolean>() {

                        public Boolean apply(Integer a, Integer b) {
                            return BothAreEven(a, b);
                        }
                    }, m));
        }
        catch (Exception e)
        {
            Assert.fail("Shouldn't reach this point");
        }
    }

    private static boolean BothAreLessThan10(final int a, final int b)
    {
        return a < 10 && b < 10;
    }

    private static BiFunction<Integer, Integer, Boolean> dBothAreLessThan10 =
            new BiFunction<Integer, Integer, Boolean>() {

                public Boolean apply(Integer a, Integer b) {
                    return BothAreLessThan10(a, b);
                }
            }   ;

    @Test
    public void ForAll2Test2()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);

        Assert.assertFalse(Functional.forAll2(dBothAreLessThan10, l, m));
    }

    @Test(expected=Exception.class)
    public void ForAll2Test3()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> m = IterableHelper.init(QuadruplingGenerator, 7);

        Functional.forAll2(
                new BiFunction<Integer, Integer, Boolean>() {

                    public Boolean apply(Integer a, Integer b) {
                        return BothAreEven(a, b);
                    }
                }, l, m);
    }

    @Test
    public void CompositionTest1A()
    {
        final Iterable2<Integer> i = IterableHelper.asList(1, 2, 3, 45, 56, 6);

        final boolean allOdd = i.forAll(Functional.isOdd);
        final boolean notAllOdd = i.exists(Functional.not(Functional.isOdd));

        Assert.assertFalse(allOdd);
        Assert.assertTrue(notAllOdd);
    }

    @Test
    public void CompositionTest2() {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
        Assert.assertFalse(Functional.forAll2(Functional.not2(dBothAreLessThan10), l, m));
        // equivalent to BothAreGreaterThanOrEqualTo10
    }

    @Test
    public void CompositionTest2a() {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
        final int lowerLimit = 1;
        final int upperLimit = 16;
        Assert.assertFalse(
                Functional.forAll2(
                        Functional.not2(
                                new BiFunction<Integer, Integer, Boolean>() {

                                    public Boolean apply(Integer a, Integer b) {
                                        return a > lowerLimit && b > lowerLimit;
                                    }
                                }
                        ), l, m));
    }

    @Test
    public void CompositionTest2b() {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
        final int lowerLimit = 1;
        final int upperLimit = 16;
        Assert.assertTrue(
                Functional.forAll2(
                        Functional.not2(
                                new BiFunction<Integer, Integer, Boolean>() {

                                    public Boolean apply(Integer a, Integer b) {
                                        return a > upperLimit && b > upperLimit;
                                    }
                                }
                        ), l, m));
    }

    @Test
    public void PartitionTest1()
    {
        final Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.isOdd, m);

        final Integer[] left = {3, 9, 15};
        final Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getLeft().toArray());
        Assert.assertArrayEquals(right, r.getRight().toArray());
    }

    @Test
    public void PartitionTest1a()
    {
        final Iterable2<Integer> m = IterableHelper.init(TriplingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = m.partition(Functional.isOdd);

        final Integer[] left = {3, 9, 15};
        final Integer[] right = {6, 12};
        Assert.assertArrayEquals(left, r.getLeft().toArray());
        Assert.assertArrayEquals(right, r.getRight().toArray());
    }

    @Test
    public void PartitionTest2()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.isEven, l);
        final Iterable2<Integer> expected = IterableHelper.init(DoublingGenerator, 5);
        AssertIterable.assertIterableEquals(expected, r.getLeft());
        Assert.assertArrayEquals(new Integer[]{}, r.getRight().toArray());
    }

    @Test
    public void PartitionTest3()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Pair<List<Integer>, List<Integer>> r = Functional.partition(Functional.isEven, l);
        final Iterable2<Integer> expected = IterableHelper.init(DoublingGenerator, 5).filter(Functional.isEven);
        AssertIterable.assertIterableEquals(expected, r.getLeft());
    }

    @Test
    public void ToStringTest1()
    {
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> ls = li.map(Functional.<Integer>dStringify());
        //String s = String.Join(",", ls);
        AssertIterable.assertIterableEquals(IterableHelper.asList("2","4","6","8","10"), ls);
    }

    @Test
    public void ChooseTest1B() throws OptionNoValueAccessException
    {
        final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        final Iterable2<String> o = li.choose(
                new Function<Integer, Option<String>>() {

                    public Option<String> apply(Integer i) {
                        return i % 2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
                    }
                });
        final Iterable2<String> expected = IterableHelper.asList("6", "12");
        AssertIterable.assertIterableEquals(o, expected);
    }

    @Test
    public void ChooseTest2A() //throws OptionNoValueAccessException
    {
        Map<Integer,String> o=null;
        try{
            final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
            o = Functional.toDictionary(Functional.<Integer>identity(), Functional.<Integer>dStringify(),
                    li.choose(
                            new Function<Integer, Option<Integer>>() {

                                public Option<Integer> apply(Integer i) {
                                    return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
                                }
                            }));
        }catch(Exception e){}
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

    private final static <B, C>boolean Fn(final B b, final C c)
    {
        return b.equals(c);
    }

    private final static <B, C>Function<C, Boolean> curried_fn(final B b)
    {
        return new Function<C, Boolean>() {

            public Boolean apply(C c) {
                return Fn(b,c);
            }
        };
    }

    @Test
    public void CurriedFnTest1()
    {
        final boolean test1a = Fn(1, 2);
        final boolean test1b = curried_fn(1).apply(2);
        Assert.assertEquals(test1a, test1b);
    }

    private static int adder_int(final int left, final int right) { return left + right; }

    private static Function<Integer, Integer> curried_adder_int(final int c)
    {
        return new Function<Integer, Integer>() {

            public Integer apply(Integer p) {
                return adder_int(c, p);
            }
        };
    }

    @Test
    public void CurriedFnTest2()
    {
        final Iterable2<Integer> a = IterableHelper.asList(1, 2, 3, 4, 5);
        final Iterable2<Integer> b = a.map(
                new Function<Integer, Integer>() {

                    public Integer apply(final Integer a1) {
                        return adder_int(2, a1);
                    }
                });
        final Iterable2<Integer> c = a.map(curried_adder_int(2));
        AssertIterable.assertIterableEquals(b, c);
    }

    private static String csv(final String state, final Integer a)
    {
        return Functional.isNullOrEmpty(state) ? a.toString() : state + "," + a;
    }

    @Test
    public void FoldvsMapTest1()
    {
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final String s1 = Functional.join(",", li.map(Functional.<Integer>dStringify()));
        Assert.assertEquals("2,4,6,8,10", s1);
        final String s2 = IterableHelper.init(DoublingGenerator, 5).fold(
                new BiFunction<String, Integer, String>() {

                    public String apply(String s1, Integer s2) {
                        return csv(s1, s2);
                    }
                }, "");
        Assert.assertEquals(s1, s2);
    }

    private final Function<Iterable2<Integer>, String> concatenate =
            new Function<Iterable2<Integer>, String>() {

                public String apply(Iterable2<Integer> l) {
                    return l.fold(new BiFunction<String, Integer, String>() {

                        public String apply(String s1, Integer s2) {
                            return csv(s1, s2);
                        }
                    }, "");
                }
            };

    @Test
    public void FwdPipelineTest1()
    {
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final String s1 = li.in(concatenate);
        Assert.assertEquals("2,4,6,8,10", s1);
    }

    private final UnaryFunction<Iterable2<Integer>, Iterable2<Integer>> evens_f =
            new UnaryFunction<Iterable2<Integer>, Iterable2<Integer>>() {

                public Iterable2<Integer> apply(Iterable2<Integer> l) {
                    return l.filter(Functional.isEven);
                }
            };

    @Test
    public void FwdPipelineTest2()
    {
        String s1;
        {
            final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
            final Iterable2<Integer> evens = li.in(evens_f);
            s1 = evens.in(concatenate);
        }
        final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        final String s2 = li.in(evens_f.then(concatenate));
        Assert.assertEquals("6,12", s1);
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void CompositionTest3()
    {
        final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        final String s = li.in( evens_f.then(concatenate));
        Assert.assertEquals("6,12", s);
    }

    @Test
    public void CompositionTest4()
    {
        final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        final String s = evens_f.then(concatenate).apply(li);
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

        {
            final Iterable2<String> indentation = IterableHelper.init(
                    new Function<Integer, String>() {

                        public String apply(Integer integer) {
                            return " ";
                        }
                    }, level);
            Assert.assertEquals(Functional.join("", indentation), "     ");
        }
        {
            final Iterable2<String> indentation = IterableHelper.init(
                    new Function<Integer, String>() {

                        public String apply(Integer integer) {
                            return " ";
                        }
                    }, level);
            final String s = indentation.fold(
                    new BiFunction<String, String, String>() {

                        public String apply(String state, String str) {
                            return state + str;
                        }
                    }, "");
            Assert.assertEquals(s, expectedResult);
        }
        {
            final Iterable2<String> indentation = IterableHelper.init(
                    new Function<Integer, String>() {

                        public String apply(Integer integer) {
                            return " ";
                        }
                    }, level);
            final Function<Iterable2<String>, String> folder =
                    new Function<Iterable2<String>, String>() {

                        public String apply(final Iterable2<String> l) {
                            return l.fold(new BiFunction<String, String, String>() {

                                public String apply(final String state, final String str) {
                                    return state + str;
                                }
                            }, "");
                        }
                    };

            final String s1 = indentation.in(folder);
            Assert.assertEquals(s1, expectedResult);
        }
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
        final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator, 5);
        final Iterable2<Integer> o =
                li.choose(
                        new Function<Integer, Option<Integer>>() {
                            public Option<Integer> apply(Integer i) {
                                return i % 2 == 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        });

        final Integer[] expected = new Integer[]{6,12};
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
    public void foldAndChooseTest1()
    {
        final Map<Integer, Double> missingPricesPerDate = new Hashtable<Integer, Double>();
        final Iterable2<Integer> openedDays = IterableHelper.init(TriplingGenerator, 5);
        Double last = 10.0;
        for (final int day : openedDays)
        {
            final Double value = day%2 == 0 ? (Double)((double)(day/2)) : null;
            if (value!=null)
                last = value;
            else
                missingPricesPerDate.put(day, last);
        }

        final Iterable2<myInt> openedDays2 = IterableHelper.init(
                new Function<Integer, myInt>() {

                    public myInt apply(final Integer a) {
                        return new myInt(3 * a);
                    }
                }, 5);
        final Pair<Double, List<myInt>> output = Functional.foldAndChoose(
                new BiFunction<Double, myInt, Pair<Double, Option<myInt>>>() {

                    public Pair<Double, Option<myInt>> apply(final Double state, final myInt day) {
                        final Double value = day.i() % 2 == 0 ? (Double) ((double) (day.i() / 2)) : null;
                        return value != null
                                ? Pair.of(value,Option.<myInt>None())
                                : Pair.of(state, Option.toOption(day));
                    }
                }, 10.0, openedDays2);

        Assert.assertEquals(last, output.getLeft());
        final List<Integer> keys = new ArrayList<Integer>(missingPricesPerDate.keySet());
        Collections.sort(keys);
        Assert.assertArrayEquals(keys.toArray(),
                Functional.map(
                        i -> i.i(), output.getRight()).toArray());
    }

    @Test
    public void joinTest1()
    {
        final String expected = "3,6,9,12,15";
        {
            final Iterable2<Integer> ids = IterableHelper.init(TriplingGenerator, 5);
            Assert.assertEquals(expected, Functional.join(",", ids.map(Functional.<Integer>dStringify())));
        }
        final Iterable2<Integer> ids = IterableHelper.init(TriplingGenerator, 5);
        Assert.assertEquals(expected, ids.join(","));
    }

    @Test
    public void joinTest2()
    {
        final String expected = "'3','6','9','12','15'";
        final Function<Integer, String> f =
                new Function<Integer, String>() {

                    public String apply(Integer id) {
                        return "'" + id + "'";
                    }
                };
        {
            final Iterable2<Integer> ids = IterableHelper.init(TriplingGenerator, 5);
            Assert.assertEquals(expected, Functional.join(",", ids.map(f)));
        }
        final Iterable2<Integer> ids = IterableHelper.init(TriplingGenerator, 5);
        Assert.assertEquals(expected, Functional.join(",", ids, f));
    }

    @Test
    public void betweenTest1()
    {
        final int lowerBound = 2, upperBound = 4;
        Assert.assertTrue(Functional.between(lowerBound, upperBound, 3));
    }

    @Test
    public void betweenTest2()
    {
        final int lowerBound = 2, upperBound = 4;
        Assert.assertFalse(Functional.between(lowerBound, upperBound, 1));
    }

    @Test
    public void betweenTest3()
    {
        final double lowerBound = 2.5, upperBound = 2.6;
        Assert.assertTrue(Functional.between(lowerBound, upperBound, 2.55));
    }

    @Test
    public void testIsEven_withEvenNum()
    {
        Assert.assertTrue(Functional.isEven.apply(2));
    }

    @Test
    public void testIn()
    {
        final Integer a = 10;
        Assert.assertTrue(Functional.in(a, Functional.isEven));
    }


    /*@Test
    public void testThen()
    {
        // mult(two,three).then(add(four)) =>
        // then(mult(two,three),add(four))
        // 2 * 3 + 4 = 10
        Integer two = 2;
        Integer three = 3;
        Integer four = 4;
        Iterable2.then(new Function<Integer,Integer>()
        {

            public Integer apply(Integer i) { return }
        })
    } */


    @Test(expected=NoSuchElementException.class)
    public void findLastTest1()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Assert.assertEquals((Integer) 5, Functional.findLast(Functional.isOdd, l));
    }

    @Test
    public void findLastTest2()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        Assert.assertEquals((Integer)10, l.findLast(Functional.isEven));
    }

    @Test
    public void toArrayTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1, 2, 3, 4, 5});
        final Iterable2<String> strs = input.map(Functional.<Integer>dStringify());
        final List<String> expected = Arrays.asList(new String[]{"1", "2", "3", "4", "5"});

        final Object[] output = strs.toArray();

        Assert.assertEquals(expected.size(),output.length);
        for(int i=0; i<expected.size(); ++i)
            Assert.assertEquals(expected.get(i), output[i]);
    }

    @Test
    public void toSetTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        final Set<Integer> integerSet = input.toSet();
        final Set<Integer> expected = new HashSet<Integer>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        expected.add(5);

        Assert.assertTrue(expected.containsAll(integerSet));
        Assert.assertTrue(integerSet.containsAll(expected));
    }

    @Test
    public void toDictionaryTest()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1, 2, 3, 4, 5});
        final Map<Integer,String> output = input.toDictionary(Functional.<Integer>identity(), Functional.<Integer>dStringify());

        final Map<Integer, String> expected = new HashMap<Integer, String>();
        expected.put(1, "1");
        expected.put(2, "2");
        expected.put(3, "3");
        expected.put(4, "4");
        expected.put(5, "5");
        Assert.assertTrue(expected.size() == output.size());
        for (final Integer expectedKey : expected.keySet()) {
            Assert.assertTrue(output.containsKey(expectedKey));
            final String expectedValue = expected.get(expectedKey);
            //Assert.assertEquals(expectedValue,o.get(expectedKey),"Expected '"+expectedValue+"' but got '"+o.get(expectedKey)+"'");
            Assert.assertTrue(output.get(expectedKey).equals(expectedValue));
        }
    }

    @Test
    public void lastTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Assert.assertEquals(5,(long) Functional.last(input));
    }

    @Test
    public void lastTest1a()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        Assert.assertEquals(5,(long) input.last());
    }

    @Test
    public void lastTest2()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        final Iterable<String> strs = input.map(Functional.<Integer>dStringify());
        Assert.assertEquals("5", Functional.last(strs));
    }

    @Test
    public void concatTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        final Iterable2<Integer> expected = IterableHelper.asList(new Integer[]{1,2,3,4,5,1,2,3,4,5});
        AssertIterable.assertIterableEquals(expected, input.concat(input));
    }

    @Test
    public void seqConcatTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1,2,3,4,5});
        final Function<Integer,Integer> doubler = new Function<Integer, Integer>() {

            public Integer apply(Integer i) {
                return i * 2;
            }
        };
        final Iterable2<String> expected = IterableHelper.asList(new String[]{"1","2","3","4","5","2","4","6","8","10"});

        final Iterable2<String> strs = input.map(Functional.<Integer>dStringify());
        final Iterable2<String> output = strs.concat(input.map(doubler).map(Functional.<Integer>dStringify()));

        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void arrayIterableTest1()
    {
        final Integer[] input = new Integer[]{1,2,3,4,5};
        final Iterable2<Integer> expected = IterableHelper.asList(new Integer[]{1,2,3,4,5});

        final ArrayIterable<Integer> ait = ArrayIterable.create(input);
        final List<Integer> output = new ArrayList<Integer>();
        for(final Integer i:ait) output.add(i);
        AssertIterable.assertIterableEquals(expected, output);
    }


    @Test
    public void seqChooseTest1()
    {
        final Iterable2<Integer> li = IterableHelper.init(TriplingGenerator,5);
        final Iterable2<String> output = li.choose(
                new Function<Integer,Option<String>>()
                {

                    public Option<String> apply(Integer i)
                    {
                        return i%2 == 0 ? Option.toOption(i.toString()) : Option.<String>None();
                    }
                });

        final Collection<String> expected = Arrays.asList(new String[]{"6", "12"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest3()
    {
        final Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> output = input.in(
                new Function<Iterable2<Integer>, Iterable2<String>>() {

                    public Iterable2<String> apply(Iterable2<Integer> integers) {
                        return integers.map(Functional.<Integer>dStringify());
                    }
                });

        final Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest4()
    {
        final Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> output = input.in(
                new Function<Iterable2<Integer>, Iterable2<String>>() {

                    public Iterable2<String> apply(Iterable2<Integer> integers) {
                        try {
                            return integers.map(Functional.<Integer>dStringify());
                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            return null; // Argh!!!
                        }
                    }
                });

        final Collection<String> expected = Arrays.asList(new String[] {"2", "4", "6", "8", "10"});
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void FwdPipelineTest5()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> oddElems = l.in(
                new Function<Iterable2<Integer>, Iterable2<Integer>>() {

                    public Iterable2<Integer> apply(Iterable2<Integer> ints) {
                        return ints.filter(Functional.isOdd);
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

    private static Function<object, string> fn1()
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
        Function<object, string> fn = fn1();
        var i = new Test1(10);
        const string s = "test";
        Assert.AreEqual("10", i.in(fn));
        Assert.AreEqual("test", s.in(fn));
    } */

    @Test
    public void seqInitTest2()
    {
        final Iterable2<Integer> output = IterableHelper.init(DoublingGenerator);
        AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2,4,6,8,10,12,14,16,18,20,22}),output.take(11));
    }

    @Test
    public void ConstantInitialiserTest1()
    {
        final int howMany = 6;
        final int initValue = -1;
        final Iterable2<Integer> l = IterableHelper.init(Functional.constant(initValue), howMany);
        Assert.assertEquals(howMany, l.toList().size());
        for(final int i : IterableHelper.init(Functional.constant(initValue), howMany))
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
        var c1 = new List<Function<A, object>> {(A a) => (object) a.name, (A a) => (object) a.id};

        Function<A, IEnumerable<Function<int, object>>> c2 =
                a => c1.Select<Function<A, object>, Function<int, object>>(f => j => f(a));

        Function<A, IEnumerable<Iterable2.Case<int, object>>> cases =
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
        Function<int, string> format = i => string.Format("Discarding {0}", i);
        List<string> expected = new[] {1, 2, 3, 4, 5}.Select(format).ToList();
        Function<int, bool> f = i =>
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
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[] {1, 2, 3, 4, 5});
        final Collection<Integer> expected = Arrays.asList(new Integer[] {1, 3, 5});
        final Iterable2<Integer> output =
                input.choose(
                        new Function<Integer, Option<Integer>>() {

                            public Option<Integer> apply(Integer i) {
                                return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
                            }
                        });
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void ChooseTest2() throws OptionNoValueAccessException
    {
        final Iterable2<String> input = IterableHelper.asList(new String[] {"abc", "def"});
        final Collection<Character> expected = Arrays.asList(new Character[]{'a'});
        final Iterable2<Character> output =
                input.choose(
                        new Function<String, Option<Character>>() {

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
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[] {1, 2, 3, 4, 5});
        final Collection<Integer> expected = Arrays.asList(new Integer[] {1, 3, 5});
        final Iterable2<Integer> output = input.choose(
                new Function<Integer, Option<Integer>>() {

                    public Option<Integer> apply(Integer i) {
                        return i%2 != 0 ? Option.toOption(i) : Option.<Integer>None();
                    }
                })  ;

        AssertIterable.assertIterableEquals(expected, output);
    }

    /*[Test]
    public void CurryTest1()
    {
        Function<int, int, bool> f = (i, j) => i > j;
        Function<int, Function<int, bool>> g = i => j => f(i, j);
        bool t = 10.in(g(5));
        Assert.IsFalse(t);
    }*/

    /*[Test]
    public void CurryTest2()
    {
        Function<int, int, bool> f = (i, j) => i < j;
        Function<int, Function<int, bool>> g = i => j => f(i, j);
        bool t = 10.in(g(5));
        Assert.IsTrue(t);
    }*/

    /*[Test]
    public void CompositionTest1()
    {
        Function<int, int, int> add = (x, y) => x + y;
        Function<int, Function<int, int>> add1 = y => x => add(x, y);
        Function<int, int, int> mult = (x, y) => x*y;
        Function<int, Function<int, int>> mult1 = y => x => mult(x, y);
        int expected = mult(add(1, 2), 3);
        Assert.AreEqual(9, expected);
        Assert.AreEqual(expected, 2.in(add1(1).then(mult1(3))));
    }*/

    @Test
    public void UnzipTest1()
    {
        final Collection<Pair<String,Integer>> input =
                new ArrayList<Pair<String,Integer>>();
        input.add(Pair.of("1", 1));
        input.add(Pair.of("2", 2));

        final Pair<Collection<String>,Collection<Integer>> expected =
                Pair.of(
                        Arrays.asList(new String[]{"1", "2"}),
                        Arrays.asList(new Integer[]{1,2}));

        final Pair<List<String>,List<Integer>> output = Functional.unzip(input);

        AssertIterable.assertIterableEquals(expected.getLeft(), output.getLeft());
        AssertIterable.assertIterableEquals(expected.getRight(), output.getRight());
    }

    @Test
    public void ZipTest1()
    {
        final Iterable2<Integer> input1 = IterableHelper.asList(new Integer[] {1, 2, 3, 4, 5});
        final Iterable2<Character> input2 = IterableHelper.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});

        final Collection<Pair<Integer,Character>> expected = new ArrayList<Pair<Integer, Character>>();
        expected.add(Pair.of(1, 'a'));
        expected.add(Pair.of(2, 'b'));
        expected.add(Pair.of(3, 'c'));
        expected.add(Pair.of(4, 'd'));
        expected.add(Pair.of(5, 'e'));

        final Iterable2<Pair<Integer,Character>> output = input1.zip(input2);

        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void Zip3Test1()
    {
        final Collection<Integer> input1 = Arrays.asList(new Integer[] {1, 2, 3, 4, 5});
        final Collection<Character> input2 = Arrays.asList(new Character[] {'a', 'b', 'c', 'd', 'e'});
        final Collection<Double> input3 = Arrays.asList(new Double[] {1.0, 2.0, 2.5, 3.0, 3.5});

        final Collection<Triple<Integer,Character,Double>> expected = new ArrayList<Triple<Integer, Character, Double>>();
        expected.add(Triple.of(1, 'a', 1.0));
        expected.add(Triple.of(2, 'b', 2.0));
        expected.add(Triple.of(3, 'c', 2.5));
        expected.add(Triple.of(4, 'd', 3.0));
        expected.add(Triple.of(5, 'e', 3.5));

        final Iterable2<Triple<Integer,Character,Double>> output = IterableHelper.create(input1).zip3(input2, input3);

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
    public void findTest1()
    {
        final String trueMatch = "6";
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> ls = li.map(Functional.<Integer>dStringify());
        Assert.assertEquals(trueMatch,
                ls.find(
                        new Function<String, Boolean>() {

                            public Boolean apply(String s) {
                                return s.equals(trueMatch);
                            }
                        }));
    }

    @Test(expected = NoSuchElementException.class)
    public void findTest2()
    {
        final String falseMatch = "7";
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> ls = li.map(Functional.<Integer>dStringify());
        ls.find(
                new Function<String, Boolean>() {

                    public Boolean apply(String s) {
                        return s.equals(falseMatch);
                    }
                });
    }

    @Test
    public void findIndexTest1()
    {
        final String trueMatch = "6";
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> ls = li.map(Functional.<Integer>dStringify());
        Assert.assertEquals(2,
                ls.findIndex(
                        new Function<String, Boolean>() {

                            public Boolean apply(String s) {
                                return s.equals(trueMatch);
                            }
                        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findIndexTest2()
    {
        final String falseMatch = "7";
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<String> ls = li.map(Functional.<Integer>dStringify());
        ls.findIndex(
                new Function<String, Boolean>() {

                    public Boolean apply(String s) {
                        return s.equals(falseMatch);
                    }
                });
    }

    @Test
    public void pickTest1()
    {
        final int trueMatch = 6;
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        Assert.assertEquals(((Integer) trueMatch).toString(),
                li.pick(
                        new Function<Integer, Option<String>>() {

                            public Option<String> apply(Integer a) {
                                return a == trueMatch ? Option.toOption(a.toString()) : Option.<String>None();
                            }
                        }));
    }

    @Test(expected = NoSuchElementException.class)
    public void pickTest2()
    {
        final int falseMatch = 7;
        final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
        li.pick(
                new Function<Integer, Option<String>>() {

                    public Option<String> apply(Integer a) {
                        return a == falseMatch ? Option.toOption(a.toString()) : Option.<String>None();
                    }
                });
    }

    @Test
    public void curryFnTest1()
    {
        final int state=0;
        final Function<Integer,Boolean> testForPosInts = new Function<Integer, Boolean>() {
             public Boolean apply(Integer integer) { return integer > state; }
        };

        final Function<Iterable<Integer>,List<Integer>> curriedTestForPosInts = Functional.filter(testForPosInts);

        final Collection<Integer> l = Arrays.asList(new Integer[]{-3,-2,0,1,5});
        final Collection<Integer> posInts = curriedTestForPosInts.apply(l);

        final Collection<Integer> expected = Arrays.asList(new Integer[]{1, 5});
        AssertIterable.assertIterableEquals(expected, posInts);
    }

    @Test
    public void MapDictTest1()
    {
        final Collection<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        final Map<String,String> output = Functional.map_dict(
                new Function<Integer, Map.Entry<String, String>>() {

                    public Map.Entry<String, String> apply(final Integer i) {
                        return new Map.Entry<String,String>(){
                            public String setValue(String v){throw new UnsupportedOperationException(); }
                            public String getValue() { return Functional.<Integer>dStringify().apply(i); }
                            public String getKey() { return Functional.<Integer>dStringify().apply(i); }
                        };
                    }
                }, input);

        final List<String> keys = new ArrayList<String>(output.keySet());
        Collections.sort(keys);
        AssertIterable.assertIterableEquals(Arrays.asList(new String[]{"1", "2", "3", "4", "5"}), keys);
    }

    @Test
    public void ToListTest1()
    {
        final Iterable2<Integer> output = IterableHelper.init(DoublingGenerator, 5);
        final List<Integer> output_ints = output.toList();
        AssertIterable.assertIterableEquals(Arrays.asList(new Integer[]{2, 4, 6, 8, 10}), output_ints);
    }

    public static Function<Integer,Iterable<Integer>> intToList(final int howMany)
    {
        return new Function<Integer, Iterable<Integer>>() {

            public Iterable<Integer> apply(final Integer integer) {
                return IterableHelper.init(
                        new Function<Integer, Integer>() {

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
        final Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> output = input.collect(intToList(3));
        final List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void seqCollectTest1()
    {
        final Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        final Iterable2<Integer> output = input.collect(intToList(3));
        final List<Integer> expected = Arrays.asList(2,2,2,4,4,4,6,6,6,8,8,8,10,10,10);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void takeNandYieldTest1()
    {
        final Iterable2<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        final Pair<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input, 2);
        final List<Integer> expectedList = Arrays.asList(2,4);
        final List<Integer> expectedRemainder = Arrays.asList(6,8,10);
        AssertIterable.assertIterableEquals(expectedList,output.getLeft());
        AssertIterable.assertIterableEquals(expectedRemainder,output.getRight());
    }

    @Test
    public void takeNandYieldTest2()
    {
        final Iterable<Integer> input = IterableHelper.init(DoublingGenerator, 5);
        final Pair<List<Integer>,Iterable<Integer>> output = Functional.takeNAndYield(input, 0);
        final List<Integer> expectedList = Arrays.asList();
        final List<Integer> expectedRemainder = Arrays.asList(2,4,6,8,10);
        AssertIterable.assertIterableEquals(expectedList,output.getLeft());
        AssertIterable.assertIterableEquals(expectedRemainder,output.getRight());
    }

    @Test
    public void recFilterTest1()
    {
        final Iterable2<Integer> l = IterableHelper.init(DoublingGenerator,5);
        final Iterable<Integer> oddElems = l.filter(Functional.isOdd);

        AssertIterable.assertIterableEquals(new ArrayList<Integer>(), oddElems);
    }

    @Test
    public void recMapTest1()
    {
        final Iterable2<Integer> input = IterableHelper.asList(new Integer[]{1, 2, 3, 4, 5});
        final Iterable<String> output = input.map(Functional.<Integer>dStringify());
        AssertIterable.assertIterableEquals(Arrays.asList("1","2","3","4","5"),output);
    }

    @Test
    public void recFoldvsMapTest1()
    {
        String s1, s2;
        {
            final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
            s1 = Functional.join(",", li.map(Functional.<Integer>dStringify()));
            Assert.assertEquals("2,4,6,8,10", s1);
        }
        {
            final Iterable2<Integer> li = IterableHelper.init(DoublingGenerator, 5);
            s2 = li.fold(
                    new BiFunction<String, Integer, String>() {

                        public String apply(String s1, Integer s2) {
                            return csv(s1, s2);
                        }
                    }, "");
        }
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void EmptySeqTestHasNoElements()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty();

        int i=0;
        final Iterator<Integer> it = l.iterator();
        while(it.hasNext()) ++i;

        Assert.assertEquals(0, i);
    }

    @Test
    public void EmptySeqTestEquals()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty();
        final Iterable2<Integer> ll = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(l, ll);
    }

    @Test
    public void EmptySeqTestFilter()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Integer> l1 = l.filter(Functional.isEven); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other, l1);
    }

    @Test
    public void EmptySeqTestMap()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<String> l1 = l.map(Functional.<Integer>dStringify()); // this filter needed to be fed from a statically-typed variable
        final Iterable2<String> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other, l1);
    }

    @Test
    public void EmptySeqTestChoose()
    {
        final Function<Integer,Option<String>> chooser = new Function<Integer, Option<String>>() {

            public Option<String> apply(Integer integer) {
                return Option.toOption(integer.toString());
            }
        };
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<String> l1 = l.choose(chooser); // this filter needed to be fed from a statically-typed variable
        final Iterable2<String> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other,l1);
    }

    @Test
    public void EmptySeqTestExists1()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final boolean b = l.exists(Functional.isEven); // this filter needed to be fed from a statically-typed variable

        Assert.assertFalse(b);
    }

    @Test
    public void EmptySeqTestExists2()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final boolean b = l.exists(Functional.isOdd); // this filter needed to be fed from a statically-typed variable

        Assert.assertFalse(b);
    }

    @Test
    public void EmptySeqTestForAll1()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final boolean b = l.forAll(Functional.isEven); // this filter needed to be fed from a statically-typed variable

        Assert.assertFalse(b);
    }

    @Test
    public void EmptySeqTestForAll2()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final boolean b = l.forAll(Functional.isOdd); // this filter needed to be fed from a statically-typed variable

        Assert.assertFalse(b);
    }

    @Test
    public void EmptySeqTestFold()
    {
        final BiFunction<String,Integer,String> folder = new BiFunction<String,Integer,String>() {

            public String apply(String s, Integer integer) {
                return s+integer.toString();
            }
        };
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final String l1 = l.fold(folder, ""); // this filter needed to be fed from a statically-typed variable
        final Iterable2<String> other = IterableHelper.createEmpty();

        Assert.assertEquals("", l1);
    }

    @Test
    public void EmptySeqTestToList()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final List<Integer> l1 = l.toList(); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other, l1);
    }

    @Test
    public void EmptySeqTestSortWith()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Integer> l1 = l.sortWith(new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        }); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other,l1);
    }

    @Test
    public void EmptySeqTestConcat()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Integer> l1 = l.concat(IterableHelper.create(Arrays.asList(1, 2, 3))); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.create(Arrays.asList(1, 2, 3));

        AssertIterable.assertIterableEquals(other,l1);
    }

    @Test(expected = NoSuchElementException.class)
    public void EmptySeqTestFind()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Integer i = l.find(new Function<Integer, Boolean>() {

            public Boolean apply(Integer integer) {
                return true;
            }
        }); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();
    }

    @Test(expected = NoSuchElementException.class)
    public void EmptySeqTestFindIndex()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final int i = l.findIndex(new Function<Integer, Boolean>() {

            public Boolean apply(Integer integer) {
                return true;
            }
        }); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();
    }

    @Test(expected = NoSuchElementException.class)
    public void EmptySeqTestPick()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final String i = l.pick(new Function<Integer, Option<String>>() {

            public Option<String> apply(Integer integer) {
                return Option.toOption(integer.toString());
            }
        }); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();
    }

    @Test
    public void EmptySeqTestTake()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Integer> l1 = l.take(10); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other, l1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void EmptySeqTestZip()
    {
        final Iterable2<Integer> k = IterableHelper.create(Arrays.asList(1, 2, 3));
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Pair<Integer,Integer>> l1 = l.zip(k); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void EmptySeqTestZip3()
    {
        final Iterable2<Integer> j = IterableHelper.create(Arrays.asList(1,2,3));
        final Iterable2<Integer> k = IterableHelper.create(Arrays.asList(1,2,3));
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Triple<Integer,Integer,Integer>> l1 = l.zip3(k, j); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();
    }

    @Test
    public void EmptySeqTestCollect()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty(); // for some reason the generic type inference failed and so
        final Iterable2<Integer> l1 = l.collect(new Function<Integer, Iterable<Integer>>() {

            public Iterable<Integer> apply(Integer integer) {
                return Arrays.asList(1, 2, 3, integer);
            }
        }); // this filter needed to be fed from a statically-typed variable
        final Iterable2<Integer> other = IterableHelper.createEmpty();

        AssertIterable.assertIterableEquals(other, l1);
    }

    @Test
    public void EmptySeqTestIn()
    {
        final Iterable2<Integer> l = IterableHelper.createEmpty();
        final String u = l.in(new Function<Iterable2<Integer>, String>() {

            public String apply(Iterable2<Integer> integers) {
                return integers.fold(new BiFunction<String, Integer, String>() {

                    public String apply(String s, Integer integer) {
                        return s + integer.toString();
                    }
                }, "");
            }
        });
        Assert.assertEquals("", u);
    }

    @Test
    public void groupByOddVsEvenInt()
    {
        final Iterable2<Integer> input = IterableHelper.create(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        final Map<Boolean,List<Integer>> output = input.groupBy(Functional.isEven);
        final Map<Boolean,List<Integer>> expected = new HashMap<Boolean, List<Integer>>();
        expected.put(false,Arrays.asList(1,3,5,7,9));
        expected.put(true,Arrays.asList(2,4,6,8,10));
        AssertIterable.assertIterableEquals(expected.get(true), output.get(true));
        AssertIterable.assertIterableEquals(expected.get(false), output.get(false));
    }

    @Test
    public void groupByStringFirstTwoChar()
    {
        final Iterable2<String> input = IterableHelper.create(Arrays.asList("aa","aab","aac","def"));
        final Map<String,List<String>> output = input.groupBy(new Function<String, String>() {

            public String apply(final String s) {
                return s.substring(0,1);
            }
        });
        final Map<String,List<String>> expected = new HashMap<String, List<String>>();
        expected.put("a",Arrays.asList("aa","aab","aac"));
        expected.put("d",Arrays.asList("def"));
        AssertIterable.assertIterableEquals(expected.get("a"), output.get("a"));
        AssertIterable.assertIterableEquals(expected.get("d"),output.get("d"));
        AssertIterable.assertIterableEquals(new TreeSet<String>(expected.keySet()),new TreeSet<String>(output.keySet()));
    }

    @Test
    public void seqMapiTest1() {
        final Collection<Integer> input = Arrays.asList(new Integer[]{1, 2, 3, 4, 5});
        final Iterable<Pair<Integer, String>> output = IterableHelper.create(input).mapi(new BiFunction<Integer, Integer, Pair<Integer, String>>() {

            public Pair<Integer, String> apply(final Integer pos, final Integer i) {
                return Pair.of(pos, i.toString());
            }
        });
        Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, Functional.map(new Function<Pair<Integer, String>, String>() {

            public String apply(final Pair<Integer, String> o) {
                return o.getRight();
            }
        }, output).toArray());
    }

    @Test
    public void takeWhileTest1() {
        final Iterable2<Integer> l = IterableHelper.create(Arrays.asList(1, 2, 3, 4, 5));
        final List<Integer> expected = new ArrayList<Integer>();
        final Iterable<Integer> output = l.takeWhile(Functional.isEven);
        AssertIterable.assertIterableEquals(expected, output);
   }

    @Test
    public void skipTest1() {
        final Iterable2<Integer> l = IterableHelper.create(Arrays.asList(1, 2, 3, 4, 5));

        final List<Integer> expected = Arrays.asList(2, 3, 4, 5);
        final Iterable<Integer> output = l.skip(1);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test
    public void skipWhileTest1()
    {
        final Iterable2<Integer> l = IterableHelper.create(Arrays.asList(1, 2, 3, 4, 5));
        final List<Integer> expected = Arrays.asList(1,2,3,4,5);
        final Iterable<Integer> output = l.skipWhile(Functional.isEven);
        AssertIterable.assertIterableEquals(expected, output);
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyListIterator()
    {
        IterableHelper.createEmpty().iterator().next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptyListIteratorCantRemove()
    {
        IterableHelper.createEmpty().iterator().remove();
    }

    /**
     * These two are not equal because the real Iterable2 actually has an iterator which might not be subsequently
     * recreatable and so it is not generally possible to determine whether the real Iterable2 is empty or not.
     */
    @Test
    public void isEmptyListEqualToListWithNoElements()
    {
        final Iterable2<Integer> emptyList = IterableHelper.createEmpty();
        final Iterable2<Integer> listWithNoElements = IterableHelper.create(new ArrayList<Integer>());

        Assert.assertNotEquals(emptyList, listWithNoElements);
    }

    @Test
    public void emptyListMapiTest()
    {
        Assert.assertEquals(IterableHelper.createEmpty(),
                IterableHelper.createEmpty().mapi(new BiFunction<Integer, Object, Object>() {

                    public Object apply(Integer integer, Object o) {
                        return null;
                    }
                }));
    }

    @Test
    public void forAll2EmptyListTest()
    {
        Assert.assertFalse(IterableHelper.createEmpty().forAll2(new BiFunction<Object, Object, Boolean>() {

            public Boolean apply(Object o, Object o2) {
                return null;
            }
        }, Arrays.asList(1)));
    }

    @Test
    public void toArrayEmptyListTest()
    {
        Assert.assertArrayEquals(new Integer[]{}, IterableHelper.createEmpty().toArray());
    }

    @Test
    public void toSetEmptyListTest()
    {
        Assert.assertEquals(new HashSet<Integer>(), IterableHelper.createEmpty().toSet());
    }

    @Test
    public void toDictionaryEmptyListTest()
    {
        Assert.assertEquals(new HashMap<Integer, String>(), IterableHelper.<Integer>createEmpty().toDictionary(new Function<Integer, Integer>() {

            public Integer apply(Integer integer) {
                return null;
            }
        }, new Function<Integer, String>() {

            public String apply(Integer integer) {
                return null;
            }
        }));
    }

    @Test(expected = NoSuchElementException.class)
    public void lastOfEmptyListTest()
    {
        IterableHelper.createEmpty().last();
    }

    @Test
    public void emptyListTakeWhileTest()
    {
        Assert.assertEquals(IterableHelper.createEmpty(), IterableHelper.<Integer>createEmpty().takeWhile(Functional.isOdd));
    }

    @Test
    public void emptyListSkipTest()
    {
        Assert.assertEquals(IterableHelper.createEmpty(),IterableHelper.<Integer>createEmpty().skip(1));
    }

    @Test
    public void emptyListSkipWhileTest()
    {
        Assert.assertEquals(IterableHelper.createEmpty(),IterableHelper.<Integer>createEmpty().skipWhile(Functional.isOdd));
    }

    @Test
    public void emptyListJoinTest()
    {
        Assert.assertEquals("", IterableHelper.createEmpty().join("a"));
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyListFindLastTest()
    {
        IterableHelper.<Integer>createEmpty().findLast(Functional.isOdd);
    }

    @Test
    public void emptyListPartitionTest()
    {
        final Pair<List<Object>, List<Object>> pair = IterableHelper.createEmpty().partition(new Function<Object, Boolean>() {

            public Boolean apply(Object o) {
                return null;
            }
        });
        Assert.assertTrue(pair.getLeft().isEmpty());
        Assert.assertTrue(pair.getRight().isEmpty());
    }

    @Test
    public void emptyListGroupByTest()
    {
        final Map<Object, List<Object>> grp = IterableHelper.createEmpty().groupBy(new Function<Object, Object>() {

            public Object apply(Object o) {
                return null;
            }
        });

        Assert.assertTrue(grp.isEmpty());
    }

    @Test
    public void emptyListInACollection()
    {
        final Map<Iterable2<Integer>,Integer> map = new HashMap<Iterable2<Integer>, Integer>();
        map.put(IterableHelper.<Integer>createEmpty(), 0);
        Assert.assertEquals(Integer.valueOf(0), map.get(IterableHelper.createEmpty()));
    }

    @Test
    public void emptyListToString()
    {
        Assert.assertEquals("()",IterableHelper.createEmpty().toString());
    }
}