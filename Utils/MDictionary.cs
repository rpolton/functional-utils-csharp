using System;
using System.Collections.Generic;

namespace Shaftesbury.Functional.Utils
{
    public class MDictionary<K, V> //: IDictionary<K,V>
    {
        public MDictionary(Dictionary<K, V> dict)
        {
            Exceptions = new List<Exception>();
            Dictionary = dict;
        }
        public List<Exception> Exceptions { get; private set; }
        public Dictionary<K, V> Dictionary { get; private set; }
    }

    public static class ToDictionary
    {
        // this is Return
        public static MDictionary<K, V> ToMDictionary<K, V>(this Dictionary<K, V> dict)
        {
            return new MDictionary<K, V>(dict);
        }

        // this is Bind
        public static MDictionary<K, V> ToDictionaryReportingDuplicates<T, K, V>(this IEnumerable<T> input, Func<T, K> keyFn, Func<T, V> valueFn)
        {
            var mDict = new Dictionary<K, V>().ToMDictionary();
            foreach (var elem in input)
            {
                var key = keyFn(elem);
                if (mDict.Dictionary.ContainsKey(key))
                    mDict.Exceptions.Add(new Exception(String.Format("Key {0} already present", key)));
                else
                    mDict.Dictionary.Add(key, valueFn(elem));
            }
            return mDict;
        }
    }
}
