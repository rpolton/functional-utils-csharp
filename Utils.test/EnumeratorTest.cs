using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;

namespace Shaftesbury.Functional.Utils.Test
{
    [TestFixture]
    internal class EnumeratorTest
    {
        private readonly SortedList<int, string> list = new SortedList<int, string>();

        [SetUp]
        public void Initialise()
        {
            list.Add(1, "one");
            list.Add(2, "two");
            list.Add(10, "ten");
            list.Add(100, "one hundred");
            list.Add(97, "ninety seven");
            list.Add(-1, "minus one");
            list.Add(0, "zero zero UFO");
        }

        [TearDown]
        public void Clear()
        {
            list.Clear();
        }

        [Test]
        public void CircularEnumTest1()
        {
            var sb = new StringBuilder();
            const int max = 15; // Max number of iterations to stop circular enumerator
            int i = 0;
            foreach (var pair in Enumerators.CircularEnum(list))
            {
                sb.Append(pair.ToString());
                if (++i >= max)
                    break; // stop circular enumerator, will be infinite if not
            }
            string expected =
                new StringBuilder("[-1, minus one]").Append("[0, zero zero UFO]").Append("[1, one]").Append("[2, two]").
                    Append("[10, ten]").Append("[97, ninety seven]").Append("[100, one hundred]").Append(
                        "[-1, minus one]").
                    Append("[0, zero zero UFO]").Append("[1, one]").Append("[2, two]").Append("[10, ten]").Append(
                        "[97, ninety seven]").
                    Append("[100, one hundred]").Append("[-1, minus one]").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void ConstrainedEnumTest1()
        {
            var sb = new StringBuilder();
            foreach (KeyValuePair<int, string> pair in Enumerators.ConstrainedEnum(list, 2, 5))
                sb.Append(pair.ToString());

            string expected = new StringBuilder("[1, one]").Append("[2, two]").
                Append("[10, ten]").Append("[97, ninety seven]").Append("[100, one hundred]").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void SteppedEnumTest1()
        {
            var sb = new StringBuilder();
            foreach (KeyValuePair<int, string> pair in Enumerators.SteppedEnum(list, 3))
                sb.Append(pair.ToString());

            string expected =
                new StringBuilder("[-1, minus one]").Append("[2, two]").Append("[100, one hundred]").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void ReverseEnumTest1()
        {
            var sb = new StringBuilder();
            foreach (string value in Enumerators.ReverseEnum(list.Values))
                sb.Append(value);

            string expected = new StringBuilder("one hundred").Append("ninety seven").Append("ten").Append("two").
                Append("one").Append("zero zero UFO").Append("minus one").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void IgnoreLastElementTest1()
        {
            var sb = new StringBuilder();
            foreach (string name in Enumerators.IgnoreLast(list.Values))
                sb.Append(name);

            string expected = new StringBuilder("minus one").Append("zero zero UFO").Append("one").Append("two").
                Append("ten").Append("ninety seven").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void AugmentedIteratorTest1()
        {
            var s1 = new[] {"one", "two", "three"};
            var s2 = new[] {"ten", "nine", "eight"};

            var sb = new StringBuilder();
            foreach (var s in s1.Concat(s2))
                sb.Append(s);

            const string expected = "onetwothreetennineeight";
            Assert.AreEqual(expected, sb.ToString());
        }

        private IEnumerable<string> YieldList()
        {
            // because we cannot simply type
            // yield return list.Values;
            // which is poor show!!
            return list.Values;
        }

        [Test]
        public void ReturnListTest1()
        {
            string expected = new StringBuilder("minus one").Append("zero zero UFO").Append("one").Append("two").
                Append("ten").Append("ninety seven").Append("one hundred").ToString();
            var sb = new StringBuilder();
            foreach (string s in YieldList())
                sb.Append(s);
            Assert.AreEqual(expected, sb.ToString());
        }

        private IEnumerable<string> CompositeLoop1()
        {
            foreach (string s in list.Values)
            {
                if (s == "two")
                {
                    yield return s;
                    continue;
                }

                yield return s + "22";
            }
        }

        private IEnumerable<string> CompositeLoop2()
        {
            foreach (string s in list.Values)
            {
                if (s == "two")
                    yield return s;

                yield return s + "22";
            }
        }

        [Test]
        public void CompositeLoopTest1()
        {
            var sb = new StringBuilder();
            foreach (string s in CompositeLoop1())
                sb.Append(s);
            string expected = new StringBuilder("minus one22").Append("zero zero UFO22").Append("one22").Append("two").
                Append("ten22").Append("ninety seven22").Append("one hundred22").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void CompositeLoopTest2()
        {
            var sb = new StringBuilder();
            foreach (string s in CompositeLoop2())
                sb.Append(s);
            string expected =
                new StringBuilder("minus one22").Append("zero zero UFO22").Append("one22").Append("two").Append("two22")
                    .
                    Append("ten22").Append("ninety seven22").Append("one hundred22").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        private IEnumerable<int> EmptyFn()
        {
            return list.Keys.Where(i => i < -100);
        }

        [Test]
        public void EmptyIteratorTest1()
        {
            IEnumerable<int> ie = EmptyFn();
            Assert.IsNotNull(ie);
            var i = ie.Aggregate(0, Functional.dSum);
            Assert.AreEqual(0, i);
        }

        [Test]
        public void IgnoreFirstElementTest1()
        {
            var sb = new StringBuilder();
            foreach (string name in list.Values.Skip(1))
                sb.Append(name);

            string expected = new StringBuilder().Append("zero zero UFO").Append("one").Append("two").
                Append("ten").Append("ninety seven").Append("one hundred").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void IgnoreLast2ElementsTest1()
        {
            var sb = new StringBuilder();
            foreach (string name in Enumerators.IgnoreLastN(list.Values, 2))
                sb.Append(name);

            string expected = new StringBuilder("minus one").Append("zero zero UFO").Append("one").Append("two").
                Append("ten").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void IgnoreFirstAndLastTest1()
        {
            var sb = new StringBuilder();
            foreach (string name in Enumerators.IgnoreFirstAndLast(list.Values))
                sb.Append(name);

            string expected = new StringBuilder().Append("zero zero UFO").Append("one").Append("two").
                Append("ten").Append("ninety seven").ToString();
            Assert.AreEqual(expected, sb.ToString());
        }

        [Test]
        public void AugmentedIteratorTest2()
        {
            const string s1 = "one";
            var s2 = new[] {"ten", "nine", "eight"};

            var sb = new StringBuilder();
            foreach (string s in Enumerators.Concat(s1, s2))
                sb.Append(s);

            const string expected = "onetennineeight";
            Assert.AreEqual(expected, sb.ToString());
        }
    }
}
