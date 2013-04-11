using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;
using permutations;

namespace Utils.Test
{
    [TestFixture]
    class PermutationsTest
    {
        [Test]
        public void Permutations1Test1()
        {
            var input = new[] { "abc", "hsh", "a" };
            var expected = new[]{new List<string>{"abc","acb","bac","bca","cab","cba"},
                new List<string>{"hsh", "hhs", "shh", "shh", "hhs", "hsh"},
                new List<string>{"a"}};
            var output = input.Select(Permutations1.permutations1).ToList();

            CollectionAssert.AreEquivalent(expected,output);            
        }

        [Test]
        public void Permutations2Test1()
        {
            var input = new List<int> { 1, 2, 1 };
            var expected = new[] { new[] { 1, 2, 1 }, new[] { 1, 2, 1 }, new[] { 2, 1, 1 }, new[] { 2, 1, 1 }, new[] { 1, 1, 2 }, new[] { 1, 1, 2 } };
            var output = Permutations2.permute(input);

            CollectionAssert.AreEquivalent(expected, output);                        
        }

        [Test]
        public void Permutations2Test2()
        {
            var input = new[] { "abc", "hsh", "a" }.Select(s=>s.ToArray().ToList());
            var expected = new[]{new List<string>{"abc","acb","bac","bca","cab","cba"},
                new List<string>{"hsh", "hhs", "shh", "shh", "hhs", "hsh"},
                new List<string>{"a"}};
            var output = input.Select(Permutations2.permute).ToList();

            CollectionAssert.AreEquivalent(expected, output);
        }
    }
}
