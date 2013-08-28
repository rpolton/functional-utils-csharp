using System;
using System.Collections.Generic;
using System.Linq;

namespace Shaftesbury.Functional.Utils
{
    public static class FunctionalHelpers
    {
        public delegate V adder_fn<V>(V left, V right);
        public static int adder_int(int left, int right) { return left + right; }
        public static double adder_double(double left, double right) { return left + right; }

        /// <summary>
        /// Take the key and the value and insert them into the dictionary. If the key already exists in the dictionary then use the adder
        /// function to insert instead. For example, the adder function could be an identity, a summation etc.
        /// </summary>
        /// <typeparam name="K">key type of the dictionary</typeparam>
        /// <typeparam name="V">value type of the dictionary</typeparam>
        /// <param name="key">the key to be inserted into the dictionary</param>
        /// <param name="value">the value associated with the key to be inserted into the dictionary</param>
        /// <param name="dict">the dictionary</param>
        /// <param name="adder">an adder function used to determine how to process the value if the key already exists in the dictionary</param>
        public static void AddKeyValueToDict<K, V>(K key, V value, IDictionary<K, V> dict, adder_fn<V> adder)
        {
            #region Precondition
            if (dict == null) throw new ArgumentNullException("dict");
            if (adder == null) throw new ArgumentNullException("adder");
            #endregion
            if (dict.ContainsKey(key))
            {
                V oldV = dict[key];
                dict[key] = adder(oldV,value);
            }
            else
                dict[key] = value;
        }

        // snippety snip http://msmvps.com/blogs/jon_skeet/archive/2008/02/05/a-simple-extension-method-but-a-beautiful-one.aspx
        public static V GetOrCreate<K, V>(K key, IDictionary<K, V> dictionary) where V : new()
        {
            #region Precondition
            if (dictionary == null) throw new ArgumentNullException("dictionary");
            #endregion
            V ret;
            if (!dictionary.TryGetValue(key, out ret))
            {
                ret = new V();
                dictionary[key] = ret;
            }
            return ret;
        }

        public delegate V factory_fn<V>();
        public static V GetOrCreate<K, V>(K key, factory_fn<V> factory, IDictionary<K, V> dictionary)
        {
            #region Precondition
            if (dictionary == null) throw new ArgumentNullException("dictionary");
            if (factory == null) throw new ArgumentNullException("factory");
            #endregion
            V ret;
            if (!dictionary.TryGetValue(key, out ret))
            {
                ret = factory();
                dictionary[key] = ret;
            }
            return ret;
        }

        public static Tuple<bool,V> GetOrCreate_x<K, V>(K key, factory_fn<V> factory, IDictionary<K, V> dictionary)
        {
            #region Precondition
            if (dictionary == null) throw new ArgumentNullException("dictionary");
            if (factory == null) throw new ArgumentNullException("factory");
            #endregion
            V ret;
            bool s=dictionary.TryGetValue(key, out ret);
            if (!s)
            {
                ret = factory();
                dictionary[key] = ret;
            }
            return new Tuple<bool, V>(s, ret);
        }

        public delegate void update_fn<V>(V orig);
        // See comments below. This is (probably) the preferred method to use.
        public static V InsertOrUpdate<K, V>(K key, factory_fn<V> inserter, update_fn<V> updater, IDictionary<K, V> dictionary)
        {
            #region Precondition
            if (dictionary == null) throw new ArgumentNullException("dictionary");
            if (inserter == null) throw new ArgumentNullException("inserter");
            if (updater == null) throw new ArgumentNullException("updater");
            #endregion
            V ret;
            if (!dictionary.TryGetValue(key, out ret))
            {
                ret = inserter();
                dictionary[key] = ret;
            }
            else
                updater(ret); // Ewww! This modifies the value of ret!
            return ret;
        }

        /// <summary>
        /// Take the key and the value and insert them into the dictionary. If the key already exists in the dictionary then append the value to
        /// the list.
        /// </summary>
        /// <typeparam name="K"></typeparam>
        /// <typeparam name="V"></typeparam>
        /// <param name="key"></param>
        /// <param name="value"></param>
        /// <param name="dict"></param>
        /// <param name="size">If supplied, this indicates the initial capacity of the new List</param>
        public static void AddKeyValueToDictOfList<K,V>(K key, V value, IDictionary<K,List<V>> dict, params int[] size)
        {
            #region Precondition
            if (dict == null) throw new ArgumentNullException("dict");
            #endregion
            if (!dict.ContainsKey(key))
                dict[key] = size!=null && size.Length==1 ? new List<V>(size[0]) : new List<V>();
            dict[key].Add(value);
        }

        /*
         * Version 1 -- the original code
         * 
                ChairPosition chairPos;
                if (!reportGroup.Positions.TryGetValue(instrumentId, out chairPos))
                    reportGroup.Positions.Add(instrumentId, 
                        new ChairPosition(instrumentId, quantity, equity.GetReference().StringValue, null, null, string.Empty, null));
                else
                    chairPos.Quantity += quantity;
         * 
         * Version 2 -- using AddKeyValueToDict - not so good because of the confusion in the delegate when updating the value in the dictionary
         * 
                FunctionalHelpers.AddKeyValueToDict(instrumentId, 
                    new ChairPosition(instrumentId, quantity, equity.GetReference().StringValue, null, null, string.Empty, null),
                    reportGroup.Positions, 
                    delegate(ChairPosition left, ChairPosition right) 
                    {
                        left.Quantity += right.Quantity; // Ewwwwwwwwww!
                        return left;
                    });
         * 
         * Version 3 -- using InsertOrUpdate. The most compact
         * 
                FunctionalHelpers.InsertOrUpdate(instrumentId,
                    delegate() { return new ChairPosition(instrumentId, quantity, equity.GetReference().StringValue, null, null, string.Empty, null); },
                    delegate(ChairPosition orig) { orig.Quantity += quantity; }, reportGroup.Positions);
         * 
         * Version 4 -- using GetOrCreate. This doesn't give you anything over and above InsertOrUpdate; they're both evil ;-)
         * 
                Tuple<bool,ChairPosition> t = 
                FunctionalHelpers.GetOrCreate_x(instrumentId,
                    delegate() { return new ChairPosition(instrumentId, quantity, equity.GetReference().StringValue, null, null, string.Empty, null); },
                    reportGroup.Positions);
                if (t.field1) t.field2.Quantity += quantity;
         * 
         * Fundamentally the problem with all of the above is that the dictionary class is not a functional data structure and, as such, expects
         * to be updated in place. This means that any functional attempt is doomed to ignominious failure. Best of a bad job ... pick v3.
         */

        public static string indentBy(int howMany, string unitOfIndentation, string indentThis)
        {
            var indentation = Enumerable.Repeat(unitOfIndentation, howMany).ToList();
            return indentation.Aggregate(indentThis, (state, str) => str + state);
        }

        // These two methods mean that it will not be possible to enter nulls as values in the dictionary.
        // The parameters are reversed from the .NET implementation - just in case we want to do some pipelining
        public static OptionType<V> TryGetValue<K,V>(K key, IDictionary<K,V> dict) where V:class
        {
            #region Precondition
            if (dict == null) throw new ArgumentNullException("dict");
            #endregion
            V v;
            bool s = dict.TryGetValue(key, out v);
            return s ? v : OptionType<V>.Null;
        }

        public static V? TryGetValue_nullable<K, V>(K key, IDictionary<K, V> dict) where V : struct 
        {
            #region Precondition
            if (dict == null) throw new ArgumentNullException("dict");
            #endregion
            V v;
            bool s = dict.TryGetValue(key, out v);
            return s ? v : (V?)null;
        }

        // For those occasions when you just can't live without knowing if the TryGetValue call succeeded
        public static Tuple<bool,OptionType<V>> TryGetValue_x<K, V>(K key, IDictionary<K, V> dict) where V : class
        {
            #region Precondition
            if (dict == null) throw new ArgumentNullException("dict");
            #endregion
            V v;
            bool s = dict.TryGetValue(key, out v);
            return new Tuple<bool,OptionType<V>>(s, s ? v : OptionType<V>.Null);
        }

        /// <summary> find_noExcept: (A -> bool) -> A list -> A option</summary>
        public static OptionType<A> find_noExcept<A>(System.Func<A,bool> f, IEnumerable<A> input) where A:class 
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            foreach (A a in input)
                if (f(a))
                    return a;
            return OptionType<A>.Null;
        }

        /// <summary> pick: (A -> B option) -> A list -> B option</summary>
        public static OptionType<B> pick_noExcept<A, B>(System.Func<A,OptionType<B>> f, IEnumerable<A> input) where B : class
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            foreach (A a in input)
            {
                OptionType<B> intermediate = f(a); // which is, effectively, if(f(a)) return f(a), but without evaluating f twice
                if (!intermediate.None)
                    return intermediate;
            }
            return OptionType<B>.Null;
        }

        public delegate Tuple<A, OptionType<B>> foldAndChoose_fn<A, B>(A a, B b) where B:class;
        public static Tuple<A,List<B>> foldAndChoose<A, B>(foldAndChoose_fn<A, B> f, A initialValue, IEnumerable<B> input) where B:class
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            A state = initialValue;
            var results = new List<B>();
            foreach (B b in input)
            {
                Tuple<A, OptionType<B>> intermediate = f(state, b);
                state = intermediate.Item1;
                if (!intermediate.Item2.None)
                    results.Add(intermediate.Item2.Some);
            }
            return new Tuple<A, List<B>>(state, results);
        }

        /// <summary>
        /// Analogue of string.Join for List&lt;T&gt;
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="separator"></param>
        /// <param name="l"></param>
        /// <returns></returns>
        public static string join<T>(string separator, IEnumerable<T> l)
        {
            #region Precondition
            if (l == null) throw new ArgumentNullException("l");
            #endregion
            return join(separator, l, id => id.ToString());
        }

        /// <summary>
        /// Analogue of string.Join for List&lt;T&gt; with the addition of a user-defined map function
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="separator"></param>
        /// <param name="l"></param>
        /// <param name="fn"></param>
        /// <returns></returns>
        public static string join<T>(string separator, IEnumerable<T> l, System.Func<T, string> fn)
        {
            #region Precondition
            if (l == null) throw new ArgumentNullException("l");
            if (fn == null) throw new ArgumentNullException("fn");
            #endregion
            return string.Join(separator, l.Select(fn));
        }

        /// <summary>return lowerBound &lt; val &lt; upperBound</summary>
        public static bool between<T>(T lowerBound, T upperBound, T val) where T : IComparable
        {
            #region Precondition
            if (val == null) throw new ArgumentNullException("val");
            #endregion
            return val.CompareTo(lowerBound) == 1 && val.CompareTo(upperBound) == -1;
        }

        /// <summary> findLast: (A -> bool) -> A list -> A</summary>
        public static A findLast<A>(System.Func<A,bool> f, IList<A> input)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            foreach (A a in Enumerators.ReverseEnum(input))
                if (f(a))
                    return a;
            throw new KeyNotFoundException();
        }
    }
}
