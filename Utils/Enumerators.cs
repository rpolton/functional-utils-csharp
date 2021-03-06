using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace Shaftesbury.Functional.Utils
{
    // http://www.codeproject.com/KB/collections/Enumerators.aspx
    public static class Enumerators
    {
// ReSharper disable FunctionNeverReturns
        public static IEnumerable<T> CircularEnum<T>(this IEnumerable<T> enumerable)
        {
            #region Precondition
            if (enumerable == null) throw new ArgumentNullException("enumerable");
            #endregion
            while (true)
            {
                var enu = enumerable.GetEnumerator();
                while (enu.MoveNext())
                {
                    yield return enu.Current;
                }
            }
        }
// ReSharper restore FunctionNeverReturns

        public static IEnumerable ConstrainedEnum(this IEnumerable enumerable, int start)
        {
            #region Precondition
            if (enumerable == null) throw new ArgumentNullException("enumerable");
            #endregion
            if (start < 0)
                throw new ArgumentException("Invalid step value, must be positive or zero.");

            IEnumerator enu = enumerable.GetEnumerator();
            while (enu.MoveNext())
            {
                if (--start < 0)
                    yield return enu.Current;
            }
        }

        public static IEnumerable ConstrainedEnum(this IEnumerable enumerable, int start, int count)
        {
            #region Precondition
            if (enumerable == null) throw new ArgumentNullException("enumerable");
            #endregion
            if (start < 0)
                throw new ArgumentException("Invalid step value, must be positive or zero.");
            if (count < 0)
                throw new ArgumentException("Invalid count value, must be positive or zero.");

            if (count > 0)
            {
                IEnumerator enu = enumerable.GetEnumerator();
                if (enu.MoveNext())
                {
                    while (--start > 0)
                    {
                        if (!enu.MoveNext())
                            break;
                    }
                    if (start <= 0)
                    {
                        while (--count >= 0)
                        {
                            if (enu.MoveNext())
                                yield return enu.Current;
                            else
                                break;
                        }
                    }
                }
            }
        }

        public static IEnumerable SteppedEnum(this IEnumerable enumerable, int step)
        {
            #region Precondition
            if (enumerable == null) throw new ArgumentNullException("enumerable");
            #endregion
            if (step < 1)
                throw new ArgumentException("Invalid step value, must be greater than zero.");

            IEnumerator enu = enumerable.GetEnumerator();
            while (enu.MoveNext())
            {
                yield return enu.Current;

                for (int i = step; i > 1; i--)
                    if (!enu.MoveNext())
                        break;
            }
        }

        /*
        public static IEnumerable ReverseEnum(IEnumerable enumerable)
        {
            System.Reflection.PropertyInfo countprop = enumerable.GetType().GetProperty("Count", typeof(int));
            if (countprop == null)
                throw new ArgumentException("Collection doesn't have a Count property, cannot enumerate.");

            int count = (int)countprop.GetValue(enumerable, null);
            if (count < 1)
                throw new ArgumentException("Collection is empty");

            System.Reflection.PropertyInfo indexer = enumerable.GetType().GetProperty("Item", new Type[] { typeof(int) });
            if (indexer == null)
                throw new ArgumentException("Collection doesn't have a proper indexed property, cannot enumerate.");

            for (int i = count - 1; i >= 0; i--)
                yield return indexer.GetValue(enumerable, new object[] { i });
        }
        */

        public static IEnumerable ReverseEnum(this IList list)
        {
            #region Precondition
            if (list == null) throw new ArgumentNullException("list");
            #endregion
            if (list.Count < 1)
                throw new ArgumentException("Collection is empty");

            for (int i = list.Count - 1; i >= 0; i--)
                yield return list[i];
        }

        public static IEnumerable<T> ReverseEnum<T>(this IList<T> list)
        {
            #region Precondition
            if (list == null) throw new ArgumentNullException("list");
            #endregion
            if (list.Count < 1)
                throw new ArgumentException("Collection is empty");

            for (int i = list.Count - 1; i >= 0; i--)
                yield return list[i];
        }

        public static IEnumerable<T> IgnoreLast<T>(this IList<T> list)
        {
            return IgnoreLastN(list, 1);
        }

        public static IEnumerable<T> IgnoreLastN<T>(this IList<T> list, int ignoreNElements)
        {
            #region Precondition
            if (list == null) throw new ArgumentNullException("list");
            #endregion
            for (int i = 0; i < list.Count - ignoreNElements; ++i)
                yield return list[i];
        }

        public static IEnumerable<T> IgnoreFirstAndLast<T>(this IList<T> list)
        {
            #region Precondition
            if (list == null) throw new ArgumentNullException("list");
            #endregion
            for (int i = 1; i < list.Count - 1; ++i)
                yield return list[i];
        }

        public static IEnumerable<T> Concat<T>(this T first, IEnumerable<T> second)
        {
            return new[] {first}.Concat(second);
        }    
    }
}