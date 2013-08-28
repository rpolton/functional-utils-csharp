using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace Shaftesbury.Functional.Utils
{
    /// <summary>
    /// http://msdn.microsoft.com/en-us/library/ee353738.aspx
    /// </summary>
    public static class Functional
    {
        /// <summary> partition: (A -> bool) -> A list -> A list * A list</summary>
        /// <returns> (list * list). The first list contains all items for which f(a) is true. The second list contains the remainder.</returns>
        public static Tuple<List<A>, List<A>> partition<A>(System.Func<A,bool> pred, IEnumerable<A> input)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (pred == null) throw new ArgumentNullException("pred");
            #endregion
            var left = new List<A>();
            var right = new List<A>();

            foreach (var element in input)
                (pred(element) ? left : right).Add(element);

            return Tuple.Create(left, right);
        }

        public static IEnumerable<IGrouping<K, V>> Partition<K, V>(this IEnumerable<V> input, System.Func<V, K> groupFn)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (groupFn == null) throw new ArgumentNullException("groupFn");
            #endregion
            return from item in input group item by groupFn(item);
        }

        public static IEnumerable<IGrouping<bool, B>> Select<A, B>(this IEnumerable<IGrouping<bool, A>> input, System.Func<A, B> left, System.Func<A, B> right)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (left == null) throw new ArgumentNullException("left");
            if (right == null) throw new ArgumentNullException("right");
            #endregion
            return input.SelectMany(grp => grp, (grp, element) => new { grp.Key, Value = (grp.Key ? left : right)(element) }).GroupBy(item => item.Key, item => item.Value);
        }

        public static IEnumerable<IGrouping<G, B>> Select<A, B, G>(this IEnumerable<IGrouping<G, A>> input, IDictionary<G, System.Func<A, B>> transformations)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (transformations == null) throw new ArgumentNullException("transformations");
            #endregion
            return input.Where(item => transformations.ContainsKey(item.Key)).
                SelectMany(grp => grp, (grp, element) => new { grp.Key, Value = transformations[grp.Key](element) }).
                GroupBy(item => item.Key, item => item.Value);
        }

        // An interesting alternative to using a HashSet
        public static IEnumerable<A> Unique<A,Key>(this IEnumerable<A> input, Func<A,Key> f)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return input.GroupBy<A, Key>(f).SelectMany(i => i.Take(1));
        }

        public static B Try<A, B, E>(this A input, System.Func<A, B> tryClause, System.Func<A, B> catchClause) where E:Exception
        {
            #region Precondition
            if (tryClause == null) throw new ArgumentNullException("tryClause");
            if (catchClause == null) throw new ArgumentNullException("catchClause");
            #endregion
            B results;
            try
            {
                results = tryClause(input);
            }
            catch (E)
            {
                results = catchClause(input);
            }
            return results;
        }

        public static B If<A, B>(this A a, System.Func<A,bool> predicate, System.Func<A, B> thenClause, System.Func<A, B> elseClause)
        {
            #region Precondition
            if (a == null) throw new ArgumentNullException("a");
            if (predicate == null) throw new ArgumentNullException("predicate");
            if (thenClause == null) throw new ArgumentNullException("thenClause");
            if (elseClause == null) throw new ArgumentNullException("elseClause");
            #endregion
            return predicate(a) ? thenClause(a) : elseClause(a);
        }

        public class Case<A, B>
        {
            public System.Func<A,bool> check { get; internal set; }
            public System.Func<A, B> results { get; internal set; }
        }

        public static B Switch<A, B>(this A input, IEnumerable<Case<A, B>> cases, System.Func<A, B> defaultCase)
        {
            #region Precondition
            if (cases == null) throw new ArgumentNullException("cases");
            if (defaultCase == null) throw new ArgumentNullException("defaultCase");
            #endregion
            return Try<InvalidOperationException>.ToTry(input, a => cases.First(chk => chk.check(a)).results(a), defaultCase);
        }

	    public static IEnumerable<B> Choose<A,B>(this IEnumerable<A> input, System.Func<A,Maybe<B>> chooseFn)
	    {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (chooseFn == null) throw new ArgumentNullException("chooseFn");
            #endregion
            return input.Select(chooseFn).OfType<Something<B>>().Select(intermediate => intermediate.Value);
	    }

        public static IEnumerable<B> Choose<A, B>(this IEnumerable<A> input, System.Func<A, Option<B>> chooseFn)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (chooseFn == null) throw new ArgumentNullException("chooseFn");
            #endregion
            return input.Select(chooseFn).Where(o => o.IsSome).Select(intermediate => intermediate.Some);
        }

        /// <summary> sortWith: (A -> A -> int) -> A list -> A list</summary>
        public static List<A> sortWith<A>(Comparison<A> f, List<A> input)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            List<A> output = new List<A>(input);    
            output.Sort(f);
            return output;
        }

        /// <summary> not: (A -> bool) -> (A -> bool)</summary>
        public static System.Func<A, bool> not<A>(System.Func<A, bool> f)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return a => !f(a);
        }

        /// <summary> not2: (A -> B -> bool) -> (A -> B -> bool)</summary>
        public static System.Func<A,B,bool> not2<A, B>(System.Func<A,B,bool> f)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return (a, b) => !f(a, b);
        }


        /// <summary> forAll2: (A -> B -> bool) -> A list -> B list -> bool</summary>
        public static bool forAll2<A, B>(System.Func<A,B,bool> f, IEnumerable<A> input1, IEnumerable<B> input2)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (input1 == null) throw new ArgumentNullException("input1");
            if (input2 == null) throw new ArgumentNullException("input2");
            #endregion
            IEnumerator<A> enum1 = input1.GetEnumerator();
            IEnumerator<B> enum2 = input2.GetEnumerator();
            bool enum1Moved = false, enum2Moved = false;
            do
            {
                enum1Moved = enum1.MoveNext();
                enum2Moved = enum2.MoveNext();
                if (enum1Moved && enum2Moved && !f(enum1.Current, enum2.Current))
                    return false;
            } while (enum1Moved && enum2Moved);
            if( enum1Moved != enum2Moved)
                throw new ArgumentException();
            return true;
        }

        /// <summary> pick: (A -> B option) -> A list -> B</summary>
        public static B pick<A, B>(System.Func<A, Maybe<B>> f, IEnumerable<A> input)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            return input.Choose(f).First();
        }

        public static Tuple<List<A>,List<B>> Unzip<A,B>(this IEnumerable<Tuple<A,B>> input)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            var left = new List<A>();
            var right = new List<B>();
            foreach(var tuple in input)
            {
                left.Add(tuple.Item1);
                right.Add(tuple.Item2);
            }
            return Tuple.Create(left, right);
        }

        /// <summary> Zip3: A list -> B list -> C list -> (A * B* C) list</summary>
        public static IEnumerable<Tuple<A,B,C>> Zip3<A, B, C>(IEnumerable<A> input1, IEnumerable<B> input2, IEnumerable<C> input3)
        {
            #region Precondition
            if (input1 == null) throw new ArgumentNullException("input1");
            if (input2 == null) throw new ArgumentNullException("input2");
            if (input3 == null) throw new ArgumentNullException("input3");
            #endregion
            IEnumerator<A> enum1 = input1.GetEnumerator();
            IEnumerator<B> enum2 = input2.GetEnumerator();
            IEnumerator<C> enum3 = input3.GetEnumerator();
            bool enum1Moved, enum2Moved, enum3Moved;
            do
            {
                enum1Moved = enum1.MoveNext();
                enum2Moved = enum2.MoveNext();
                enum3Moved = enum3.MoveNext();
                if (enum1Moved && enum2Moved && enum3Moved)
                    yield return Tuple.Create(enum1.Current, enum2.Current, enum3.Current);
            } while (enum1Moved && enum2Moved & enum3Moved);
            if (enum1Moved != enum2Moved || enum1Moved != enum3Moved || enum2Moved != enum3Moved)
                throw new ArgumentException();
        }

        public static IEnumerable<A> Unique<A>(this IEnumerable<A> input)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            return input.GroupBy(i => i).SelectMany(i => i.Take(1));
        }


        #region Standard predicates
        public static bool IsOdd(int v) { return v % 2 != 0; }
        public static bool IsEven(int v) { return v % 2 == 0; }
        public static bool IsNull<T>(T t) where T:class { return t == null; }
        public static bool IsNotNull<T>(T t) where T : class { return t != null; }
        
        public static bool AreEqual<A, B>(A a, B b) { return a.Equals(b); }

        public static System.Func<int,bool> dIsOdd = IsOdd;
        public static System.Func<int,bool> dIsEven = IsEven;
        public static System.Func<object,bool> dIsNull = IsNull;
        public static System.Func<object,bool> dIsNotNull = IsNotNull;
        #endregion

        #region Standard initialisers
        public static System.Func<int,T> Constant<T>(T i) { return j => i; }
        #endregion

        #region Standard map functions
        public static string Stringify(int a) { return a.ToString(); }
        public static System.Func<string, string> dQuote = s => "'" + s + "'";
        public static System.Func<int, string> dStringify = Stringify;
        #endregion

        #region Standard fold functions
        private static int Sum(int state, int b) { return state + b; }
        private static int Max(int state, int b) { return state < b ? b : state; }
        private static int Min(int state, int b) { return state > b ? b : state; }
        private static int Count(int state, int b) { return state + 1; }

        public static System.Func<int, int, int> dSum = Sum;
        public static System.Func<int, int, int> dMax = Max;
        public static System.Func<int, int, int> dMin = Min;
        public static System.Func<int, int, int> dCount = Count;
        #endregion

        #region Standard sort functions
        public static int Sorter<A>(A left, A right) where A:IComparable
        {
            return left.CompareTo(right);
        }
        #endregion

        /// <summary>Use Func for forward pipelining of lists and curried functions</summary>
        /// <typeparam name="A"></typeparam>
        /// <typeparam name="RetType"></typeparam>
        //public class Func<A, RetType>
        //{
        //    public static RetType operator >(A a, Func<A, RetType> f)
        //    {
        //        return f.act(a);
        //    }
        //    /// <summary>Not implemented because it makes no sense. C# compiler requires the matching operator but logic does not</summary>
        //    public static RetType operator <(A a, Func<A, RetType> f)
        //    {
        //        throw new NotImplementedException();
        //    }

        //    public RetType act(A a)
        //    {
        //        return _f(a);
        //    }

        //    private readonly System.Func<A, RetType> _f;
        //    public Func(System.Func<A, RetType> f)
        //    {
        //        _f = f;
        //    }
            
        //    public static implicit operator System.Func<A,RetType> (Func<A, RetType> f)
        //    {
        //        return f._f;
        //    }
        //    public static implicit operator Func<A, RetType> (System.Func<A, RetType> f)
        //    {
        //        return new Func<A, RetType>(f);
        //    }
        //}

        /* 
        // Curried functions for use with Func<A,B> 
        */
        public static Func<IEnumerable<A>, IEnumerable<A>> filter<A>(System.Func<A,bool> f)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return l => l.Where(f);
        }

        public static Func<IEnumerable<A>, IEnumerable<B>> map<A, B>(System.Func<A,B> f)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return l => l.Select(f);
        }

        /// <summary> choose: (A -> B option) -> A list -> B list</summary>
        public static Func<IEnumerable<A>, IEnumerable<B>> choose<A, B>(System.Func<A, Maybe<B>> f) where B : class
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return l => l.Choose(f);
        }

        /// <summary> init: int -> (int -> A) -> A list</summary>
        public static IEnumerable<T> Repeat<T>(this System.Func<int, T> f, int howMany)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return Enumerable.Range(0, howMany).Select(f);
        }

        /// <summary> init: int -> (int -> A) -> A list</summary>
        // ReSharper disable FunctionNeverReturns
        public static IEnumerable<T> Repeat<T>(this System.Func<int,T> f)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            for (int i = 0; ; ++i)
                yield return f(i);
        }
        // ReSharper restore FunctionNeverReturns
    
        public class Enumerate<A>:IEnumerable<A>
        {
            private readonly IEnumerator<A> enumerator;
            public Enumerate(IEnumerator<A> en)
            {
                enumerator = en;
            }
            #region Implementation of IEnumerable

            public IEnumerator<A> GetEnumerator()
            {
                return enumerator;
            }

            IEnumerator IEnumerable.GetEnumerator()
            {
                return GetEnumerator();
            }

            #endregion
        }

        public static Tuple<List<A>,IEnumerable<A>> TakeNAndYield<A>(this IEnumerable<A> input, int howMany)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            int counter = 0;
            var output = new List<A>();
            var enumerator = input.GetEnumerator();
            if(howMany>0&&enumerator.MoveNext())
            {
                while(counter<howMany)
                {
                    output.Add(enumerator.Current);
                    counter++;
                    if (counter < howMany && !enumerator.MoveNext()) break;
                }
                return Tuple.Create(output, new Enumerate<A>(enumerator).AsEnumerable());
            }
            return Tuple.Create(output, new A[] {}.AsEnumerable());
        }
    }

    public static class Case
    {
        public static Functional.Case<A, B> ToCase<A, B>(System.Func<A,bool> pred, Func<A, B> res)
        {
            #region Precondition
            if (pred == null) throw new ArgumentNullException("pred");
            if (res == null) throw new ArgumentNullException("res");
            #endregion
            return new Functional.Case<A, B> { check = pred, results = res };
        }
    }

    public static class Try<E> where E:Exception
    {
        public static B ToTry<A,B>(A input, Func<A, B> tryClause, Func<A, B> catchClause)
        {
            #region Precondition
            if (tryClause == null) throw new ArgumentNullException("tryClause");
            if (catchClause == null) throw new ArgumentNullException("catchClause");
            #endregion
            return Functional.Try<A, B, E>(input, tryClause, catchClause);
        }
    }

    public static class ToIgnore
    {
        public static void Ignore<T>(this T t)
        {
            var input = t as IEnumerable;
            if(input!=null)
                foreach (var i in input)
                    i.Ignore();
        }
    }

    public static class ToFunc
    {
        public static B In<A, B>(this A a, Func<A, B> f)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return f(a);
        }

        public static Func<A, C> Then<A, B, C>(this Func<A, B> f, Func<B, C> g)
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            if (g == null) throw new ArgumentNullException("g");
            #endregion
            return a => g(f(a));
        }

        /// <summary>
        /// Similarly to In and Then, use this in a chain when building objects, eg
        /// var a = Bob.Build(Today).ThenTransformIfTrue(MakeItBigger, i => true)
        /// </summary>
        public static T ThenTransformIfTrue<T, U>(this T input, Func<T, U> tfm, Func<T, bool> pred) where U : T
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (tfm == null) throw new ArgumentNullException("tfm");
            if (pred == null) throw new ArgumentNullException("pred");
            #endregion
            return pred(input) ? tfm(input) : input;
        }
    }

    public static class Converter
    {
        public static HashSet<T> ToHashSet<T>(this IEnumerable<T> input)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            #endregion
            return new HashSet<T>(input);
        }
    }

    public static class Numerical
    {
        public static bool Between<A>(this A item, A left, A right) where A : IComparable<A>
        {
            #region Precondition
            if (item == null) throw new ArgumentNullException("item");
            #endregion
            return item.CompareTo(left) >= 0 && item.CompareTo(right) <= 0;
        }
    }
}
