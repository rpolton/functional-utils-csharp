using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;

namespace Shaftesbury.Functional.Utils.Test
{
    [TestFixture]
    class ToDictionaryReportingDuplicates
    {
        [Test]
        public void DictionaryWithNoDuplicatesTest1()
        {
            var expected = new[] { 1, 2, 3, 4 }.ToDictionary(i => i, i => i.ToString());
            var output = expected.ToMDictionary();
            CollectionAssert.IsEmpty(output.Exceptions);
            CollectionAssert.AreEquivalent(expected, output.Dictionary);
        }

        [Test]
        public void DictionaryWithNoDuplicatesTest2()
        {
            var expected = new[] { 1, 2, 3, 4 }.ToDictionary(i => i, i => i.ToString());
            var output = new[] { 1, 2, 3, 4 }.ToDictionaryReportingDuplicates(i => i, i => i.ToString());
            CollectionAssert.IsEmpty(output.Exceptions);
            CollectionAssert.AreEquivalent(expected, output.Dictionary);
        }

        [Test]
        public void DictionaryWithNoDuplicatesTest3()
        {
            var expected = new[] { 1, 2, 3, 4 }.ToDictionary(i => i, i => i.ToString());
            var output = new[] { 1, 2, 3, 4, 4, 3, 2, 1 }.ToDictionaryReportingDuplicates(i => i, i => i.ToString());
            CollectionAssert.IsNotEmpty(output.Exceptions);
            Assert.AreEqual(4,output.Exceptions.Count);
            CollectionAssert.AreEquivalent(expected, output.Dictionary);
        }

        [Test]
        public void KeyValuePairTest1()
        {
            var input = new KeyValuePair<int, string>(1, "one");
            var output = input.ToOption().Bind(
                kvp =>
                kvp.Key < 0
                    ? Option<KeyValuePair<string, int>>.None
                    : new KeyValuePair<string, int>(kvp.Value, kvp.Key).ToOption());
            var dict = new Dictionary<string, int> {{output.Some.Key,output.Some.Value}};
            Assert.AreEqual(1,dict.Count);
            Assert.AreEqual(1,dict["one"]);
        }
    }
}
