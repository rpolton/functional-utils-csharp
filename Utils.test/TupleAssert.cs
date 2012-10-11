using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using NUnit.Framework;

namespace Utils.Test
{
    public class TupleAssert
    {
        private static void Chk<T>(IEnumerable<T> ex, IEnumerable<T> ac)
        {
            CollectionAssert.AreEquivalent(ex, ac);
            Assert.AreEqual(0, ac.Zip(ex, (a, e) => !a.Equals(e)).Count());
        }

        private static void ItemsAreEquivalent<T>(T expected, T actual)
        {
            var expectedAsEnumerable = expected as IEnumerable;
            var actualAsEnumerable = actual as IEnumerable;

            if (expectedAsEnumerable != null && actualAsEnumerable != null)
            {
                var expectedEnumerator = expectedAsEnumerable.GetEnumerator();
                var actualEnumerator = actualAsEnumerable.GetEnumerator();
                if(expectedEnumerator.MoveNext() && actualEnumerator.MoveNext())
                {
                    do
                    {
                        Assert.AreEqual(expectedEnumerator.Current,actualEnumerator.Current);                        
                    } while (expectedEnumerator.MoveNext() && actualEnumerator.MoveNext());
                }
            }
            else
                Assert.AreEqual(expected, actual);
        }

        public static void AreEquivalent<T1>(Tuple<T1> expected, Tuple<T1> actual)
        {
            ItemsAreEquivalent(expected.Item1, actual.Item1);
        }

        public static void AreEquivalent<T1, T2>(Tuple<T1, T2> expected, Tuple<T1, T2> actual)
        {
            ItemsAreEquivalent(expected.Item1, actual.Item1);
            ItemsAreEquivalent(expected.Item2, actual.Item2);
        }

        public static void AreEquivalent<T1, T2, T3>(Tuple<T1, T2, T3> expected, Tuple<T1, T2, T3> actual)
        {
            ItemsAreEquivalent(expected.Item1, actual.Item1);
            ItemsAreEquivalent(expected.Item2, actual.Item2);
            ItemsAreEquivalent(expected.Item3, actual.Item3);
        }

        public static void AreEquivalent<T1, T2, T3, T4>(Tuple<T1, T2, T3, T4> expected, Tuple<T1, T2, T3, T4> actual)
        {
            ItemsAreEquivalent(expected.Item1, actual.Item1);
            ItemsAreEquivalent(expected.Item2, actual.Item2);
            ItemsAreEquivalent(expected.Item3, actual.Item3);
            ItemsAreEquivalent(expected.Item4, actual.Item4);
        }
    }
}
