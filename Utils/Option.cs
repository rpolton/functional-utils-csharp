using System;

namespace Shaftesbury.Functional.Utils
{
    public struct Option<T>
    { // ReSharper disable InconsistentNaming
        private static readonly Func<T, Tuple<T, bool>> tfm; // ReSharper restore InconsistentNaming
        static Option()
        {
            var typeCode = Type.GetTypeCode(typeof(T));
            switch (typeCode)
            {
                case TypeCode.String:
                case TypeCode.Object:
                    // ReSharper disable CompareNonConstrainedGenericWithNull
                    // because we know that T is either an Object or a String
                    tfm = t => t == null ? Tuple.Create(t, false) : Tuple.Create(t, true);
                    // ReSharper restore CompareNonConstrainedGenericWithNull))
                    break;
                default:
                    tfm = t => Tuple.Create(t, true);
                    break;
            }
        }

        private readonly Tuple<T, bool> _t;

        public Option(T t)
        {
            _t = tfm(t);
        }

        public bool IsNone { get { return !IsSome; } }
        public bool IsSome { get { return _t.Item2; } }

        public T Some
        {
            get
            {
                if (IsNone) throw new OptionValueAccessException();
                return _t.Item1;
            }
        }

        public static Option<T> None { get { return new Option<T>(Tuple.Create(default(T), false)); } }

        private Option(Tuple<T, bool> t)
        {
            _t = t;
        }
    }

    public class OptionValueAccessException : Exception
    {
    }

    public static class Option
    {
        public static Option<T> ToOption<T>(this T t)
        {
            return new Option<T>(t);
        }

        public static Option<B> Bind<A, B>(this Option<A> input, Func<A, Option<B>> tfm)
        {
            return input.IsNone ? Option<B>.None : tfm(input.Some);
        }

        public static Option<C> SelectMany<A, B, C>(this Option<A> a, Func<A, Option<B>> tfm, Func<A, B, C> select)
        {
            return a.Bind(aval => tfm(aval).Bind(bval => select(aval, bval).ToOption()));
        }
    }
}