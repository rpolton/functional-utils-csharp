using System;
using System.Collections.Generic;
using NUnit.Framework;

namespace Utils.Test
{
    [TestFixture]
    class TryCatchFinallyMonadTest
    {
        [Test]
        public void TryInsertingIntoDictionaryTest1()
        {
            var a = TryCatchFinallyMonadBuilder.Try(() => new Dictionary<int, string> {{1, "one"}, {2, "two"}});
            Assert.IsFalse(a.HasException);
            CollectionAssert.AreEquivalent(new[]{"one","two"},a.Value.Values);
        }

        [Test]
        public void TryInsertingIntoDictionaryTest2()
        {
            var a = TryCatchFinallyMonadBuilder.Try(() => new Dictionary<int, string> {{1, "one"}, {2, "two"}}).
                Catch((Exception ex) => new Dictionary<int, string>());
            Assert.IsFalse(a.HasException);
            CollectionAssert.AreEquivalent(new[] { "one", "two" }, a.Value.Values);
        }

        [Test]
        public void TryInsertingIntoDictionaryTest3()
        {
            var a = TryCatchFinallyMonadBuilder.Try(() => new Dictionary<int, string> {{1, "one"}, {2, "two"}}).
                Catch((Exception ex) => new Dictionary<int, string>()).
                Finally(dict => Console.WriteLine("In finally"));
            Assert.IsFalse(a.HasException);
            CollectionAssert.AreEquivalent(new[] { "one", "two" }, a.Value.Values);
        }
    }
}
