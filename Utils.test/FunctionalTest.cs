using System;
using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;

// ReSharper disable InconsistentNaming
namespace Utils.Test
{
    [TestFixture]
    public class FunctionalTest
    {
        private static readonly Func<int,int> DoublingGenerator = a=>2*(a + 1);

        [Test]
        public void InitTest1()
        {
            var output = DoublingGenerator.Repeat(5).ToList();
            CollectionAssert.AreEquivalent(new[]{2,4,6,8,10},output);
        }

        [Test]
        public void MapTest1()
        {
            var input = Enumerable.Range(1, 5).ToList();
            var output = input.Select(Functional.dStringify);
            CollectionAssert.AreEquivalent(new[]{"1","2","3","4","5"},output);
        }

        [Test]
        public void SortWithTest1()
        {
            var i = new[] { 1, 6, 23, 7, 4 }.ToList();
            List<int> j = Functional.sortWith(delegate(int a, int b)
                                                  {
                                                      if (a < b) return -1;
                                                      if (a == b) return 0;
                                                      return 1;
                                                  }, i);
            CollectionAssert.AreEquivalent(new[]{1,4,6,7,23},j);
        }

        [Test]
        public void SortWithTest2()
        {
            var i = new[] { 1, 6, 23, 7, 4 }.ToList();
            var j = Functional.sortWith((a, b) => a - b, i);
            CollectionAssert.AreEquivalent(new[] { 1, 4, 6, 7, 23 }, j);
        }

        [Test]
        public void SortWithTest3()
        {
            var i = new[] { 1, 6, 23, 7, 4 }.ToList();
            var j = Functional.sortWith(Functional.Sorter, i);
            CollectionAssert.AreEquivalent(new[] { 1, 4, 6, 7, 23 }, j);
        }

        private static readonly Func<int, int> TriplingGenerator = a => 3 * (a + 1);
        private static readonly Func<int,int> QuadruplingGenerator= a=>4*(a + 1);

        private static bool BothAreEven(int a, int b)
        {
            return Functional.IsEven(a) && Functional.IsEven(b);
        }

        [Test]
        public void ForAll2Test1()
        {
            var l = DoublingGenerator.Repeat(5).ToList();
            var m = QuadruplingGenerator.Repeat(5).ToList();
            Assert.IsTrue(Functional.forAll2(BothAreEven, l, m));
        }

        private static bool BothAreLessThan10(int a, int b)
        {
            return a < 10 && b < 10;
        }

        [Test]
        public void ForAll2Test2()
        {
            var l = DoublingGenerator.Repeat(5).ToList();
            var m = TriplingGenerator.Repeat(5).ToList();
            Assert.IsFalse(Functional.forAll2(BothAreLessThan10, l, m));
        }

        [ExpectedException(typeof (ArgumentException))]
        [Test]
        public void ForAll2Test3()
        {
            var l = DoublingGenerator.Repeat(5).ToList();
            var m = QuadruplingGenerator.Repeat(7).ToList();
            Assert.IsTrue(Functional.forAll2(BothAreEven, l, m));
        }

        [Test]
        public void CompositionTest1A()
        {
            int[] i = {1, 2, 3, 45, 56, 6};

            bool allOdd = i.All(Functional.IsOdd);
            bool notAllOdd = i.Any(Functional.not(Functional.dIsOdd));

            Assert.IsFalse(allOdd);
            Assert.IsTrue(notAllOdd);
        }

        [Test]
        public void CompositionTest2()
        {
            Func<int, int, bool> dBothAreLessThan10 = BothAreLessThan10;
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            List<int> m = TriplingGenerator.Repeat(5).ToList();
            Assert.IsFalse(Functional.forAll2(Functional.not2(dBothAreLessThan10), l, m));
                // equivalent to BothAreGreaterThanOrEqualTo10

            const int lowerLimit = 1;
            const int upperLimit = 16;
            Assert.IsFalse(
                Functional.forAll2(
                    Functional.not2((int a, int b) => a > lowerLimit && b > lowerLimit), l, m));
            Assert.IsTrue(
                Functional.forAll2(
                    Functional.not2((int a, int b) => a > upperLimit && b > upperLimit), l, m));
        }

        [Test]
        public void PartitionTest1()
        {
            List<int> m = TriplingGenerator.Repeat(5).ToList();
            Tuple<List<int>, List<int>> r = Functional.partition(Functional.dIsOdd, m);

            int[] left = {3, 9, 15};
            int[] right = {6, 12};
            CollectionAssert.AreEquivalent(new List<int>(left), r.Item1);
            CollectionAssert.AreEquivalent(new List<int>(right), r.Item2);
        }

        [Test]
        public void PartitionTest2()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            Tuple<List<int>, List<int>> r = Functional.partition(Functional.dIsEven, l);
            CollectionAssert.AreEquivalent(l, r.Item1);
            CollectionAssert.AreEquivalent(new List<int>(), r.Item2);
        }

        [Test]
        public void PartitionTest3()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            Tuple<List<int>, List<int>> r = Functional.partition(Functional.dIsEven, l);
            CollectionAssert.AreEquivalent(l.Where(Functional.dIsEven), r.Item1);
        }

        [Test]
        public void ToStringTest1()
        {
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            List<string> ls = li.Select(Functional.dStringify).ToList();
            string s = string.Join(",", ls);
            Assert.AreEqual("2,4,6,8,10", s);
        }

        [Test]
        public void ChooseTest1B()
        {
            List<int> li = TriplingGenerator.Repeat(5).ToList();
            List<string> o = li.Choose(i => i%2 == 0 ? i.ToString().ToOption() : Option<string>.None).ToList();
            string[] expected = {"6", "12"};
            CollectionAssert.AreEquivalent(o, new List<string>(expected));
        }

        [Test]
        public void ChooseTest2A()
        {
            List<int> li = TriplingGenerator.Repeat(5).ToList();
            var o = li.Choose( i => i%2 == 0 ? i.ToOption() : Option<int>.None).ToDictionary(i => i, i => i.ToString());
            var expected = new Dictionary<int, string>();
            expected[6] = "6";
            expected[12] = "12";
            CollectionAssert.AreEquivalent(o, expected);
        }

        private static bool Fn<B, C>(B b, C c)
        {
            return b.Equals(c);
        }

        private static Func<C, bool> curried_fn<B, C>(B b)
        {
            return c => Fn(b, c);
        }

        [Test]
        public void CurriedFnTest1()
        {
            bool test1a = Fn(1, 2);
            bool test1b = curried_fn<int, int>(1)(2);
            Assert.AreEqual(test1a, test1b);
        }

        private static Func<int, int> curried_adder_int(int c)
        {
            return p => FunctionalHelpers.adder_int(c, p);
        }

        [Test]
        public void CurriedFnTest2()
        {
            int[] a = {1, 2, 3, 4, 5};
            List<int> b = a.Select(a1 => FunctionalHelpers.adder_int(2, a1)).ToList();
            List<int> c = a.Select(curried_adder_int(2)).ToList();
            CollectionAssert.AreEquivalent(b, c);
        }

        private static string csv(string state, int a)
        {
            return string.IsNullOrEmpty(state) ? a.ToString() : state + "," + a;
        }

        [Test]
        public void FoldvsMapTest1()
        {
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            string s1 = string.Join(",", li.Select(Functional.dStringify));
            Assert.AreEqual("2,4,6,8,10", s1);
            string s2 = li.Aggregate(string.Empty, csv);
            Assert.AreEqual(s1, s2);
        }

        private readonly Functional.Func<List<int>, string> concatenate =
            new Functional.Func<List<int>, string>(l => l.Aggregate(string.Empty, csv));

        [Test]
        public void FwdPipelineTest1()
        {
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            string s1 = li > concatenate;
            Assert.AreEqual("2,4,6,8,10", s1);
        }

        private readonly Functional.Func<List<int>, List<int>> evens_f =
            new Functional.Func<List<int>, List<int>>(l => l.Where(Functional.IsEven).ToList());

        [Test]
        public void FwdPipelineTest2()
        {
            List<int> li = TriplingGenerator.Repeat(5).ToList();
            List<int> evens = li > evens_f;
            string s1 = evens > concatenate;
            string s2 = li > evens_f > concatenate;
            Assert.AreEqual("6,12", s1);
            Assert.AreEqual(s1, s2);
        }

        [Test]
        public void CompositionTest3()
        {
            List<int> li = TriplingGenerator.Repeat(5).ToList();
            string s = li > evens_f.Then(concatenate);
            Assert.AreEqual("6,12", s);
        }

        [Test]
        public void CompositionTest4()
        {
            List<int> li = TriplingGenerator.Repeat(5).ToList();
            string s = evens_f.Then(concatenate).act(li);
            Assert.AreEqual("6,12", s);
        }

        [Test]
        public void IndentTest1()
        {
            const int level = 5;
            const string expectedResult = "     ";

            string indentedName = string.Empty;
            for (int i = 0; i < level; ++i)
            {
                indentedName += " ";
            }
            Assert.AreEqual(indentedName, expectedResult);


            List<string> indentation = Functional.Repeat(a => " ",level).ToList();
            Assert.AreEqual(string.Join(string.Empty, indentation.ToArray()), "     ");

            string s = indentation.Aggregate(string.Empty, (state, str) => state + str);
            Assert.AreEqual(s, expectedResult);

            var folder =
                new Functional.Func<List<string>, string>(l => l.Aggregate(string.Empty, (state, str) => state + str));
            string s1 = indentation > folder;
            Assert.AreEqual(s1, expectedResult);
        }

        [Test]
        public void IndentTest2()
        {
            const int level = 5;
            const string expectedResult = "     BOB";
            Assert.AreEqual(FunctionalHelpers.indentBy(level, " ", "BOB"), expectedResult);
        }

        [Test]
        public void ChooseTest3A()
        {
            List<int> li = TriplingGenerator.Repeat(5).ToList();
            List<int> o = li.Choose(i => i%2 == 0 ? i.ToOption() : Option<int>.None).ToList();
            Assert.AreEqual(o[0], 6);
            Assert.AreEqual(o[1], 12);
        }

        [Test]
        public void TryGetValueTest1()
        {
            var d = new Dictionary<string, int>();
            d["one"] = 1;
            Assert.AreEqual(1, FunctionalHelpers.TryGetValue_nullable("one", d));
        }

        [Test]
        public void TryGetValueTest2()
        {
            var d = new Dictionary<string, int>();
            d["one"] = 1;
            Assert.IsNull(FunctionalHelpers.TryGetValue_nullable("two", d));
        }

        [Test]
        public void TryGetValueTest3()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.AreEqual("ONE", FunctionalHelpers.TryGetValue("one", d).Some);
        }

        [Test]
        public void TryGetValueTest4()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.IsTrue(FunctionalHelpers.TryGetValue("two", d).None);
        }

        [Test]
        public void TryGetValueTest5()
        {
            var d = new Dictionary<string, List<int>>();
            var l = new List<int>(new[] {1, 2, 3});
            d["one"] = l;
            Assert.AreEqual(l, FunctionalHelpers.TryGetValue("one", d).Some);
        }

        [Test]
        public void TryGetValueTest6()
        {
            var d = new Dictionary<string, string>();
            d["one"] = "ONE";
            Assert.IsTrue(FunctionalHelpers.TryGetValue("two", d).None);
        }

        [Test]
        public void find_noExceptTest1()
        {
            const string trueMatch = "6";
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            List<string> ls = li.Select(Functional.dStringify).ToList();
            Assert.IsTrue(FunctionalHelpers.find_noExcept(a => a == trueMatch, ls).Some == trueMatch);
        }

        [Test]
        public void find_noExceptTest2()
        {
            const string falseMatch = "7";
            List<int> li = DoublingGenerator.Repeat(5).ToList();
            List<string> ls = li.Select(Functional.dStringify).ToList();
            Assert.IsTrue(FunctionalHelpers.find_noExcept(a => a == falseMatch, ls).None);
        }

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

        private class myInt
        {
            private readonly int _i;

            public myInt(int i)
            {
                _i = i;
            }

            public int i
            {
                get { return _i; }
            }
        }

        [Test]
        public void foldAndChooseTest1()
        {
            var missingPricesPerDate = new SortedList<int, double>();
            List<int> openedDays = TriplingGenerator.Repeat(5).ToList();
            double last = 10.0;
            foreach (int day in openedDays)
            {
                double? value = day%2 == 0 ? day/2 : (double?) null;
                if (value.HasValue)
                    last = value.Value;
                else
                    missingPricesPerDate.Add(day, last);
            }

            List<myInt> openedDays2 = Functional.Repeat(a => new myInt(3*(a + 1)), 5).ToList();
            Tuple<double, List<myInt>> output = FunctionalHelpers.foldAndChoose(delegate(double state, myInt day)
                                                                                    {
                                                                                        double? value = day.i%2 == 0
                                                                                                            ? day.i/2
                                                                                                            : (double?)
                                                                                                              null;
                                                                                        return value.HasValue
                                                                                                   ? new Tuple
                                                                                                         <double,
                                                                                                         OptionType
                                                                                                         <myInt>>(
                                                                                                         value.Value,
                                                                                                         OptionType
                                                                                                             <myInt>.
                                                                                                             Null)
                                                                                                   : new Tuple
                                                                                                         <double,
                                                                                                         OptionType
                                                                                                         <myInt>>(
                                                                                                         state, day);
                                                                                    }, 10.0, openedDays2);

            Assert.AreEqual(last, output.Item1);
            CollectionAssert.AreEqual(missingPricesPerDate.Keys, output.Item2.Select(i => i.i));
        }

        [Test]
        public void joinTest1()
        {
            List<int> ids = TriplingGenerator.Repeat(5).ToList();
            const string expected = "3,6,9,12,15";
            Assert.AreEqual(expected, string.Join(",", ids.Select(id => id.ToString()).ToArray()));
            Assert.AreEqual(expected, FunctionalHelpers.join(",", ids));
        }

        [Test]
        public void joinTest2()
        {
            List<int> ids = TriplingGenerator.Repeat(5).ToList();
            const string expected = "'3','6','9','12','15'";
            Func<int, string> f = id => "'" + id + "'";
            Assert.AreEqual(expected, string.Join(",", ids.Select(f).ToArray()));
            Assert.AreEqual(expected, FunctionalHelpers.join(",", ids, f));
        }

        [Test]
        public void betweenTest1()
        {
            const int lowerBound = 2, upperBound = 4;
            Assert.IsTrue(FunctionalHelpers.between(lowerBound, upperBound, 3));
        }

        [Test]
        public void betweenTest2()
        {
            const int lowerBound = 2, upperBound = 4;
            Assert.IsFalse(FunctionalHelpers.between(lowerBound, upperBound, 1));
        }

        [Test]
        public void betweenTest3()
        {
            const double lowerBound = 2.5, upperBound = 2.6;
            Assert.IsTrue(FunctionalHelpers.between(lowerBound, upperBound, 2.55));
        }

        [Test]
        public void seqFilterTest1()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            IEnumerable<int> oddElems = l.Where(Functional.IsOdd);
            Assert.AreEqual(0, oddElems.Aggregate(0, Functional.dCount));
        }

        [Test]
        public void seqFilterTest2()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            IEnumerable<int> oddElems = l.Where(Functional.IsEven);
            Assert.AreEqual(5, oddElems.Aggregate(0, Functional.dCount));
        }

        [Test]
        public void seqFilterTest3()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            const int limit = 5;
            IEnumerable<int> highElems = l.Where(a => a > limit);
            Assert.AreEqual(3, highElems.Aggregate(0, Functional.dCount));
        }

        [ExpectedException(typeof (KeyNotFoundException))]
        [Test]
        public void findLastTest1()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            Assert.AreEqual(5, FunctionalHelpers.findLast(Functional.IsOdd, l));
        }

        [Test]
        public void findLastTest2()
        {
            var l = DoublingGenerator.Repeat(5).ToList();
            Assert.AreEqual(10, FunctionalHelpers.findLast(Functional.IsEven, l));
        }

        [Test]
        public void seqMapTest1()
        {
            var input = Enumerable.Range(1, 5).ToList();
            var expected = new[] {"1", "2", "3", "4", "5"};
            var output = input.Select(Functional.dStringify);
            CollectionAssert.AreEquivalent(expected, output);
        }

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

        [Test]
        public void FwdPipelineTest3()
        {
            var input = DoublingGenerator.Repeat(5).ToList();
            var output = input > Functional.map(Functional.dStringify);
            var expected = new List<string>(new[] {"2", "4", "6", "8", "10"});
            CollectionAssert.AreEquivalent(expected, output.ToList());
        }

        [Test]
        public void FwdPipelineTest4()
        {
            var input = DoublingGenerator.Repeat(5);
            var output = input > Functional.map(Functional.dStringify);
            var expected = new List<string>(new[] {"2", "4", "6", "8", "10"});
            CollectionAssert.AreEquivalent(expected, output.ToList());
        }

        [Test]
        public void FwdPipelineTest5()
        {
            IEnumerable<int> l = DoublingGenerator.Repeat(5);
            var oddElems = l > Functional.filter<int>(Functional.IsOdd);
            Assert.IsTrue(oddElems.ToList().Count == 0);
        }

        private class Test1
        {
            public readonly int i;

            public Test1(int j)
            {
                i = j;
            }
        }

        private static Functional.Func<object, string> fn1()
        {
            return new Functional.Func<object, string>(delegate(object o)
                                                           {
                                                               if (o.GetType() == typeof (Test1))
                                                                   return fn2(o as Test1);
                                                               if (o.GetType() == typeof (string))
                                                                   return fn3(o as string);
                                                               return null;
                                                           });
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
            Functional.Func<object, string> fn = fn1();
            var i = new Test1(10);
            const string s = "test";
            Assert.AreEqual("10", i > fn);
            Assert.AreEqual("test", s > fn);
        }

        [Test]
        public void seqInitTest2()
        {
            var output = DoublingGenerator.Repeat();
            CollectionAssert.AreEquivalent(new[]{2,4,6,8,10,12,14,16,18,20,22},output.Take(11));
        }

        [Test]
        public void ConstantInitialiserTest1()
        {
            const int howMany = 6;
            const int initValue = -1;
            List<int> l = Functional.Constant(initValue).Repeat(howMany).ToList();
            Assert.AreEqual(howMany, l.Count);
            foreach (int i in l)
                Assert.AreEqual(initValue, i);
        }

        private static Functional.Func<string, int?> fn4()
        {
            return new Functional.Func<string, int?>(s => s.Length > 4 ? s.Length : (int?) null);
        }

        [Test]
        public void CurryingTest1()
        {
            const string value = "test";
            int? i = value > fn4();
            int? j = (value + "1") > fn4();
            Assert.IsNull(i);
            Assert.IsNotNull(j);
            Assert.AreEqual(5, j);
        }

        [Test]
        public void filtering()
        {
            List<int> l = DoublingGenerator.Repeat(5).ToList();
            IEnumerable<int> l1 = l.Where(i => i%2 == 0);
            Func<IEnumerable<int>, IEnumerable<int>> f = i => i.Where(j => j%2 == 0);
            IEnumerable<int> l2 = f(l);
            //            var k = new Lazy<int>(l);
        }

        [Test]
        public void fnArray()
        {
            var l = new List<int>();
            var bs = new[] {10, 20, 30, 40, 50};
            foreach (int a1 in new[] {1, 2, 3, 4, 5, 6, 7, 8, 9})
                foreach (int b1 in bs)
                    l.AddRange(new Func<int, int, int>[] {(a, b) => a + b, (a, b) => a*b}.Select(f => f(a1, b1)));
        }
        /*********************************************************
        private class PropTester
        {
            public int A { get; set; }
            public int B { get; set; }
            public double C { get; set; }
            public double D { get; set; }
            public string E { get; set; }
        }

        private class eq<A> : IEquatable<A>
        {
            public bool Equals(A a)
            {
                return true;
            }
        }

        private interface IGenFunc<A,B>
        {
            Func<A, B> fn { get; }
        }

        private class GenFunc<A, B> : IGenFunc<A,B>
        {
            public Func<A, B> fn { get; set; }

            public GenFunc(Func<A, B> f)
            {
                fn = f;
            }
        }

        private static class Ext
        {
            private static R ReturnType(this Func<A, R> f)
            {
                return new R();
            }
        }

        private static class FN
        {
            public static bool Equals<T>(T a, T b) //where T:IEquatable<T>
            {
                return a.Equals(b);
            }
        }

        [Test]
        public void fnArrayTest1()
        {
            // compare two objects of type PropTester
            // functionally equivalent to l.A==r.A && l.B==r.B && l.C==r.C && l.D==r.D && l.E==r.E
            var l = new PropTester();
            var r = new PropTester();
            Func<PropTester, int> a = p => p.A;
            Func<PropTester, int> b = p => p.B;
            Func<PropTester, double> c = p => p.C;
            Func<PropTester, double> d = p => p.D;
            Func<PropTester, string> e = p => p.E;
            Func<IEquatable<T>, IEquatable<T>, bool>
            f < T > = (p, q) => p.Equals(q);

            bool r1 = a(l).Equals(a(r));
            bool r2 = FN.Equals(a(l), a(r));
            bool r3 = FN.Equals(c(l), c(r));


            Func < IEquatable<T>,
            Func<IEquatable<T>, bool> > f < T > = p => p.Equals;
            Func<PropTester, PropTester, Func<PropTester, IEquatable<>>, bool>
            ff < T > = (x, y, z) => z(x) == z(y);

            var genArr = new IGenFunc[]
                             {
                                 new GenFunc<PropTester, int>(a), new GenFunc<PropTester, int>(b),
                                 new GenFunc<PropTester, double>(c),
                                 new GenFunc<PropTester, double>(d), new GenFunc<PropTester, string>(e)
                             };

            bool res_g = genArr.All(f => f.fn(l) == f.fn(r));

            Func<A, List<A>> tol = tolist;

            var arr = new[] {a, b, c, d, e};
            bool res1 = arr.All(f => f(l) == f(r));
            Func<A, A> equals = (x, y) => x.Equals(y);
            bool res2 =
        }*****************************************************/

        [Test]
        public void partitionTest1()
        {
            IEnumerable<int> input = Enumerable.Range(0, 10);
            IEnumerable<IGrouping<bool, int>> i = from item in input group item by item%2 == 0;
            Func<int, bool> isEven = ii => ii%2 == 0;
            IEnumerable<IGrouping<bool, int>> j = from item in input group item by isEven(item);

            //foreach(var group in Enumerable.Range(0,5).partitionAsGroup(i=>i%2==0))
        }

        [Test]
        public void SwitchTest1()
        {
            Assert.AreEqual(1,
                            Functional.Switch(10,
                                              new[]
                                                  {
                                                      Case.ToCase((int a) => a < 5, a => -1),
                                                      Case.ToCase((int a) => a > 5, a => 1)
                                                  }, a => 0));
        }

        [Test]
        public void TryTest1()
        {
            int zero = 0;
            int results = Functional.Try(10, a => a/zero).Catch<DivideByZeroException>(a => a);
            Assert.AreEqual(10, results);
        }

        [Test]
        public void MaybeLINQTest1()
        {
            IEnumerable<int> i = Enumerable.Range(0, 10);
            var j = from r1 in i
                    from i2 in Enumerable.Range(2, 9)
                    select new {A = r1, B = i2};
            //var k = j.SelectMany()
            //var r = from m in 
        }

        private struct A
        {
            public string name;
            public int id;
        }


        [Test]
        public void CaseTest1()
        {
            IEnumerable<Func<A, Functional.Case<int, object>>> cases =
                new Func<A, Functional.Case<int, object>>[]
                    {
                        (A a) => Case.ToCase((int pos) => 0.Equals(pos), pos => (object) a.name)
                    };
        }

        [Test]
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
        }

        [Test]
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
        }

        [Test]
        public void ChooseTest1()
        {
            var input = new[] {1, 2, 3, 4, 5};
            var expected = new[] {1, 3, 5};
            List<int> output =
                input.Choose(i => i%2 != 0 ? (Maybe<int>) new Something<int>(i) : new Nothing<int>()).ToList();
            CollectionAssert.AreEquivalent(expected, output);
        }

        [Test]
        public void ChooseTest2()
        {
            var input = new[] {"abc", "def"};
            var expected = new[] {'a'};
            List<char> output =
                input.Choose(str => str.StartsWith("a") ? (Maybe<char>) new Something<char>('a') : new Nothing<char>()).
                    ToList();
            CollectionAssert.AreEquivalent(expected, output);
        }

        [Test]
        public void ChooseTest3()
        {
            var input = new[] {1, 2, 3, 4, 5};
            var expected = new[] {1, 3, 5};
            List<int> output = input.Choose(i => i%2 != 0 ? i.ToOption() : Option<int>.None).ToList();
            CollectionAssert.AreEquivalent(expected, output);
        }

        [Test]
        public void ChooseTest4()
        {
            var input = new[] {"abc", "def"};
            var expected = new[] {'a'};
            List<char> output = input.Choose(str => str.StartsWith("a") ? 'a'.ToOption() : Option<char>.None).ToList();
            CollectionAssert.AreEquivalent(expected, output);
        }

        [Test]
        public void CurryTest1()
        {
            Func<int, int, bool> f = (i, j) => i > j;
            Func<int, Func<int, bool>> g = i => j => f(i, j);
            bool t = 10.In(g(5));
            Assert.IsFalse(t);
        }

        [Test]
        public void CurryTest2()
        {
            Func<int, int, bool> f = (i, j) => i < j;
            Func<int, Func<int, bool>> g = i => j => f(i, j);
            bool t = 10.In(g(5));
            Assert.IsTrue(t);
        }

        [Test]
        public void CompositionTest1()
        {
            Func<int, int, int> add = (x, y) => x + y;
            Func<int, Func<int, int>> add1 = y => x => add(x, y);
            Func<int, int, int> mult = (x, y) => x*y;
            Func<int, Func<int, int>> mult1 = y => x => mult(x, y);
            int expected = mult(add(1, 2), 3);
            Assert.AreEqual(9, expected);
            Assert.AreEqual(expected, 2.In(add1(1).Then(mult1(3))));
        }

        [Test]
        public void ToHashSetTest1()
        {
            var input = new[] {1, 2, 3, 4, 4, 5};
            var expected = new[] {1, 2, 3, 4, 5};
            HashSet<int> output = input.ToHashSet();
            CollectionAssert.AreEquivalent(expected, output);
        }
        [Ignore]
        [Test]
        public void ParseCommandLineArgsTest1()
        {
            var input = new[] { "arg1", "val1", "arg2", "val2", "arg3", "arg4", }.ToList();
            var mandatory = new [] {new Argument() { Name="arg1",ExpectsValue=true}, new Argument() {Name="arg3",ExpectsValue=false} };
            var optional = new[] { new Argument() { Name = "arg2", ExpectsValue = true }, new Argument() { Name = "arg4", ExpectsValue = false } };

            var expectedMandatory = new List<Argument> { new Argument() { Name = "arg1", ExpectsValue = true, Value = "val1" }, new Argument() { Name = "arg3", ExpectsValue = false} };
            var expectedOptional = new List<Argument> { new Argument() { Name = "arg2", ExpectsValue = true, Value = "val2" }, new Argument() { Name = "arg4", ExpectsValue = false } };
            var expectedUnrecognised = Enumerable.Empty<string>().ToHashSet();
            var expectedMandatoryMissing = Enumerable.Empty<string>().ToHashSet();
            var expected = Tuple.Create(expectedMandatory, expectedOptional, expectedUnrecognised, expectedMandatoryMissing);

            var output = input.SplitArgsInto(mandatory, optional);
//            TupleAssert.AreEquivalent(expected, output);
            CollectionAssert.AreEquivalent(expected.Item1, output.Item1);
            CollectionAssert.AreEquivalent(expected.Item2, output.Item2);
            CollectionAssert.AreEquivalent(expected.Item3, output.Item3);
            CollectionAssert.AreEquivalent(expected.Item4, output.Item4);
        }
    }
}
// ReSharper restore InconsistentNaming
