using System;

namespace Utils
{
    public class Option<T>
    {
        private static readonly Func<T, Maybe<T>> tfm;
        static Option()
        {
            var typeCode = Type.GetTypeCode(typeof(T));
            switch (typeCode)
            {
                case TypeCode.String:
                case TypeCode.Object:
                    // ReSharper disable CompareNonConstrainedGenericWithNull
                    // because we know that T is either an Object or a String
                    tfm = t => t == null ? (Maybe<T>)new Nothing<T>() : new Something<T>(t);
                    // ReSharper restore CompareNonConstrainedGenericWithNull
                    break;
                default:
                    tfm = t => new Something<T>(t);
                    break;
            }
        }

        private readonly Maybe<T> _t;

        public Option(T t)
        {
            _t = tfm(t);
        }

        public bool IsNone { get { return _t is Nothing<T>; } }
        public bool IsSome { get { return _t is Something<T>; } }

        public T Some
        {
            get
            {
                var tmp = _t as Something<T>;
                if (tmp == null) throw new OptionValueAccessException();
                return tmp.Value;
            }
        }

        public static Option<T> None { get { return new Option<T>(new Nothing<T>()); } }

        private Option(Maybe<T> t)
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