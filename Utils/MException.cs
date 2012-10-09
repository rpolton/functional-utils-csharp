using System;

namespace Utils
{
    public class MException<T>
    {
        private T value;
        private readonly Exception exception;
        public bool HasException { get; private set; }
        
        public MException(T t)
        {
            value = t;
            HasException = false;
        }

        public MException(Exception exception)
        {
            this.exception = exception;
            HasException = true;
        }

        public Exception ShowException { get { return exception; }}
        public T Value { get { if (HasException)throw new MExceptionValueAccessException(); else return value; } }
    }

    public class MExceptionValueAccessException : Exception
    {
    }

    public static class MException
    {
        public static MException<T> ToMException<T>(this T t)
        {
            return new MException<T>(t);
        }

        public static MException<B> Bind<A, B>(this MException<A> input, Func<A, MException<B>> tfm)
        {
            if(input.HasException)
            {
                return new MException<B>(input.ShowException);
            }
            try
            {return tfm(input.Value);}
            catch(Exception ex)
            {return new MException<B>(ex);}
        }

        public static MException<C> SelectMany<A, B, C>(this MException<A> a, Func<A, MException<B>> tfm, Func<A, B, C> select)
        {
            return a.Bind(aval => tfm(aval).Bind(bval => select(aval, bval).ToMException()));
        }
    }
}