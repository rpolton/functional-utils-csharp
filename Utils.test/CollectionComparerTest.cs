using System;
using System.Collections.Generic;
using NUnit.Framework;

namespace Utils.Test
{
    [TestFixture]
    internal class CollectionComparerTest
    {
        private const double epsilon = 0.0000001;

        [Test]
        public static void CollectionComparerTest1()
        {
            var left = new Dictionary<int, double>();
            var right = new Dictionary<int, double>();
            left[1] = 1.0;
            right[1] = 1.0;
            left[2] = 2.0;
            right[2] = 2.0;
            left[3] = 4.0;
            right[3] = 4.0;
            left[4] = 6.5;
            right[4] = 6.5 + (epsilon/2.0);
            left[5] = 10.756;
            right[5] = 10.756;
            Assert.IsTrue(Compare(left, right));
        }

        [Test]
        public static void CollectionComparerTest2()
        {
            var left = new Dictionary<int, double>();
            var right = new Dictionary<int, double>();
            left[1] = 1.0;
            right[1] = 1.0;
            left[2] = 2.0;
            right[2] = 2.0;
            left[3] = 4.0;
            right[3] = 4.0 + (epsilon*2.0);
            left[4] = 6.5;
            right[4] = 6.5;
            left[5] = 10.756;
            right[5] = 10.756;
            Assert.IsTrue(Compare(left, right));
        }

        [Test]
        public static void CollectionComparerTest3()
        {
            var left = new Dictionary<int, double>();
            var right = new Dictionary<int, double>();
            left[1] = 1.0;
            right[1] = 1.0;
            left[2] = 2.0;
            right[2] = 2.0;
            left[3] = 4.0;
            right[3] = 4.0;
            left[4] = 6.5;
            right[4] = 6.5;
            left[5] = 10.756;
            Assert.IsTrue(Compare(left, right));
        }

        private static bool Compare(IDictionary<int, double> left, IDictionary<int, double> right)
        {
            bool match1 = left.Count != 0 && right.Count != 0 &&
                          CollectionComparer.DictEqual(left, right,
                                                       delegate(double a, double b) { return Math.Abs(a - b) <= epsilon ? 0 : 1; });

            bool match2 = true;
            if (left.Count == 0 || right.Count == 0 || left.Count != right.Count)
                match2 = false;
            if (match2)
            {
                foreach (int key in left.Keys)
                {
                    if (!right.ContainsKey(key)
                        || Math.Abs(right[key] - left[key]) > epsilon)
                    {
                        match2 = false;
                        break;
                    }
                }
            }
            if (match2)
            {
                foreach (int key in right.Keys)
                {
                    if (!left.ContainsKey(key)
                        || Math.Abs(left[key] - right[key]) > epsilon)
                    {
                        match2 = false;
                        break;
                    }
                }
            }

            return match1 == match2;
        }
    }
}
