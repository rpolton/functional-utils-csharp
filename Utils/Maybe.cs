using System;

namespace Utils
{
    public interface Maybe<T>
    {
    }

    public class Nothing<T> : Maybe<T>
    {
        
    }

    public class Something<T> : Maybe<T>
    {
        public T Value { get; private set; }
        public Something(T value)
        {
            Value = value;
        }
    }

    public static class Maybe
    {
        public static Maybe<T> ToMaybe<T>(this T t)
        {
            return new Something<T>(t);
        }

        public static Maybe<B> Bind<A,B>(this Maybe<A> input, Func<A,Maybe<B>> tfm)
        {
            var inputAsSmthg = input as Something<A>;
            return inputAsSmthg == null ? new Nothing<B>() : tfm(inputAsSmthg.Value);
        }

        public static Maybe<C> SelectMany<A,B,C>(this Maybe<A> a, Func<A,Maybe<B>> tfm, Func<A,B,C> select)
        {
            return a.Bind(aval => tfm(aval).Bind(bval => select(aval, bval).ToMaybe()));
        }
    }

    public static class MaybeFns
    {
        public static Maybe<int> Div(int top, int bottom )
        {
            return bottom == 0 ? new Nothing<int>() : (top/bottom).ToMaybe();
        }
    }
}
