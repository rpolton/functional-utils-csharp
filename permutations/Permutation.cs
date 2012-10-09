using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace permutations
{
    public static class Permutations1
    {
        public static List<string> permutations1(string input)
        {
            var collector = new List<string>();
            if (input.Count() > 1)
            {
                for (int i = 0; i < input.Count(); ++i)
                {
                    var hold = input[i];
                    // is there a Range operator for the string?
                    //var tmp = (i > 0 ? input.Substring(0, i) : string.Empty) + (i + 1 < input.Count() ? input.Substring(i + 1) : string.Empty);
                    var tmp = input.Remove(i, 1);
                    foreach (var perm in permutations1(tmp))
                        collector.Add(hold + perm);
                }
            }
            else collector.Add(input);
            return collector;
        }
    }

    public static class Permutations2
    {
        public static List<List<U>> permute<U>(List<U> u)
        {
            if (u.Count == 0) return new List<List<U>> {u};
            if (u.Count == 1) return new List<List<U>> {u};
            var output = new List<List<U>>();
            for (int i=0;i<u.Count;++i) // build a set of new lists with the ith element at the front
            {
                var u1 = new List<U> {u[i]}; // the ith element
                var u2 = u.Where((item, pos) => pos != i).ToList(); // the remainder
                output.AddRange(permute(u2).Select(u3 => u1.Concat(u3).ToList()));
            }
            return output;
        }
    }

    namespace Knuth_4a
    {
        public static class LexicographicPermutationGeneration
        {/*
            public static R Permutation(List<int> input)
            {
                // this assumes 1-based indexing
                var n = input.Count;
                Start:
                Visit(input.From(1, n));
                var j = n - 1;
                while (j>=0 && input[j] >= input[j + 1]) j--;
                if(j==0) return new R();
                var l = n;
                while (input[j] >= input[l]) l--;
                Swap(input[j], input[l]);
                var k = j + 1;
                l = n;
                while(k<l)
                {
                    Swap(input[k], input[l]);
                    k++;
                    l--;
                }
                goto Start;
            }*/
        }
    }
}

