using System;

namespace Shaftesbury.Functional.Utils
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
            #region Precondition
            if (exception == null) throw new ArgumentNullException("exception");
            #endregion
            this.exception = exception;
            HasException = true;
        }

        public Exception ShowException { get { return exception; }}
        public T Value { get { if (HasException)throw new MExceptionValueAccessException(ShowException); else return value; } }
    }

    public class MExceptionValueAccessException : Exception
    {
        public MExceptionValueAccessException(Exception inner):base("Trying to access a MException in error state",inner){}
    }

    public static class MException
    {
        public static MException<T> ToMException<T>(this T t)
        {
            return new MException<T>(t);
        }

        public static MException<B> Bind<A, B>(this MException<A> input, Func<A, MException<B>> tfm)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (tfm == null) throw new ArgumentNullException("tfm");
            #endregion
            return input.HasException ? new MException<B>(input.ShowException) : tfm(input.Value);
        }

        public static Func<A,MException<B>> Protect<A,B>(this Func<A,MException<B>> tfm)
        {
            #region Precondition
            if (tfm == null) throw new ArgumentNullException("tfm");
            #endregion
            return a =>
                       {
                           try
                           {
                               return tfm(a);
                           }
                           catch(Exception ex)
                           {
                               return new MException<B>(ex);
                           }
                       };
        }

        public static MException<B> BindWithProtect<A,B>(this MException<A> input, Func<A,MException<B>> tfm)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (tfm == null) throw new ArgumentNullException("tfm");
            #endregion
            return input.Bind(Protect(tfm));
        }

        public static MException<C> SelectMany<A, B, C>(this MException<A> a, Func<A, MException<B>> tfm, Func<A, B, C> select)
        {
            #region Precondition
            if (a == null) throw new ArgumentNullException("a");
            if (tfm == null) throw new ArgumentNullException("tfm");
            if (select == null) throw new ArgumentNullException("select");
            #endregion
            return a.BindWithProtect(aval => tfm(aval).Bind(bval => select(aval, bval).ToMException()));
        }

        public static A GetValueOrDefault<A>(this MException<A> a)
        {
            #region Precondition
            if (a == null) throw new ArgumentNullException("a");
            #endregion
            return a.HasException ? default(A) : a.Value;
        }

        public static A GetValueOrDefault<A>(this MException<A> a, A defValue)
        {
            #region Precondition
            if (a == null) throw new ArgumentNullException("a");
            #endregion
            return a.HasException ? defValue : a.Value;
        }

        public static MException<T> Convert<T>(this MException<T> t, Func<Exception,Exception> fn)
        {
            #region Precondition
            if (t == null) throw new ArgumentNullException("t");
            if (fn == null) throw new ArgumentNullException("fn");
            #endregion
            return t.HasException ? new MException<T>(fn(t.ShowException)) : t;
        }
    }
}