using System;
using System.Collections.Generic;
using System.Linq;
using Utils;
using NUnit.Framework;

namespace UnitTests
{
    [TestFixture]
    public class FunctionalTest
    {
        [Test]
        public void partitionTest1()
        {
            var input = Enumerable.Range(0,10);
            var i = from item in input group item by item%2==0;
            Func<int,bool> isEven = ii=>ii%2==0;
            var j = from item in input group item by isEven(item);

            //foreach(var group in Enumerable.Range(0,5).partitionAsGroup(i=>i%2==0))
        }

        [Test]
        public void SwitchTest1()
        {
            Assert.AreEqual(1, Functional.Switch(10, new[] { Case.ToCase((int a) => a < 5, a => -1), Case.ToCase((int a) => a > 5, a => 1) }, a => 0));
        }

        [Test]
        public void TryTest1()
        {
            var zero = 0;
            var results = Functional.Try(10, a => a / zero).Catch<DivideByZeroException>(a=>a);
            Assert.AreEqual(10, results);
        }

        [Test]
        public void MaybeLINQTest1()
        {
            var i = Enumerable.Range(0, 10);
            var j = from r1 in i
                    from i2 in Enumerable.Range(2, 9)
                    select new {A = r1, B = i2};
            //var k = j.SelectMany()
            //var r = from m in 
        }

        struct A
        {
            public string name;
            public int id;
        }
            

        [Test]
        public void CaseTest1()
        {
            IEnumerable<System.Func<A, Functional.Case<int, object>>> cases =
                new System.Func<A, Functional.Case<int, object>>[]
                    {
                        (A a) => Case.ToCase((int pos) => 0.Equals(pos), pos => (object) a.name)
                    };
        }

        [Test]
        public void CaseTest2()
        {
            var c1 = new List<Func<A, object>>(){(A a)=>(object)a.name, (A a)=>(object)a.id};

            Func<A, IEnumerable<Func<int, object>>> c2 = a => c1.Select<Func<A, object>, Func<int, object>>(f => j => f(a));
            
            Func<A, IEnumerable<Functional.Case<int, object>>> cases = a => c2(a).Select((f, i) => Case.ToCase(i.Equals, f));

            var theA = new A() {id=1, name="one"};

            var results = Enumerable.Range(0, 3).Select(i => Functional.Switch(i, cases(theA), aa => "oh dear"));
            var expected = new object[] {"one", 1, "oh dear"};
            CollectionAssert.AreEquivalent(expected,results);
        }

        [Test]
        public void IgnoreTest1()
        {
            var input = new[] {1, 2, 3, 4, 5};
            var output = new List<string>();
            Func<int, string> format = i => string.Format("Discarding {0}", i);
            var expected = new[] {1, 2, 3, 4, 5}.Select(format).ToList();
            Func<int, bool> f = i => { output.Add(format(i)); return true; };
            input.Select(f).Ignore();
            CollectionAssert.AreEquivalent(expected, output);
        }

        [Test]
        public void ChooseTest1()
        {
            var input = new[] {1, 2, 3, 4, 5};
            var expected = new[] {1, 3, 5};
            var output = input.Choose(i => i%2 != 0 ? (Maybe<int>)new Something<int>(i) : new Nothing<int>()).ToList();
            CollectionAssert.AreEquivalent(expected,output);
        }

        [Test]
        public void ChooseTest2()
        {
            var input = new[] {"abc", "def"};
            var expected = new[] {'a'};
            var output = input.Choose(str => str.StartsWith("a") ? (Maybe<char>)new Something<char>('a') : new Nothing<char>()).ToList();
            CollectionAssert.AreEquivalent(expected,output);
        }

        [Test]
        public void ChooseTest3()
        {
            var input = new[] { 1, 2, 3, 4, 5 };
            var expected = new[] { 1, 3, 5 };
            var output = input.Choose(i => i % 2 != 0 ? i.ToOption() : Option<int>.None).ToList();
            CollectionAssert.AreEquivalent(expected, output);
        }

        [Test]
        public void ChooseTest4()
        {
            var input = new[] { "abc", "def" };
            var expected = new[] { 'a' };
            var output = input.Choose(str => str.StartsWith("a") ? 'a'.ToOption() : Option<char>.None).ToList();
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
            var expected = mult(add(1, 2), 3);
            Assert.AreEqual(9,expected);
            Assert.AreEqual(expected, 2.In(add1(1).Then(mult1(3))));
        }
    }

    public static class ToFunc
    {
        public static B In<A,B>(this A a, Func<A, B> f)
        {
            return f(a);
        }

        public static Func<A,C> Then<A,B,C>(this Func<A,B> f, Func<B,C> g)
        {
            return a => g(f(a));
        }
    }
}
