using System;

namespace Utils
{
    public class ExceptionAccessViolation:Exception { }

    public class TryCatchFinallyMonad<T>
    {
        private readonly Tuple<T,Exception> _state;
        public TryCatchFinallyMonad(T t)
        {
            _state = Tuple.Create(t,null as Exception);
        }
        public TryCatchFinallyMonad(Exception ex)
        {
            _state = Tuple.Create(default(T), ex);
        }
        public bool HasException { get { return _state.Item2 != null; } }
        public Exception ShowException { get { return _state.Item2; } }
        public T Value 
        {
            get 
            {
                if (HasException) throw new ExceptionAccessViolation();
                return _state.Item1;
            } 
        }
    }

    public static class TryCatchFinallyMonadBuilder
    {
        private static TryCatchFinallyMonad<T> Return<T> (this T t)
        {
            return new TryCatchFinallyMonad<T>(t);
        }

        public static TryCatchFinallyMonad<U> Try</*T,*/U>(Func</*T is void,*/U> tfm)
        {
            try
            {
                return tfm().Return();
            }
            catch (Exception ex)
            {
                return new TryCatchFinallyMonad<U>(ex);
            }
        }

        public static TryCatchFinallyMonad<T> Catch<Ex, T>(this TryCatchFinallyMonad<T> tcf, Func<Ex, T> catchClause) where Ex : Exception
        {
            return tcf.HasException && ((tcf.ShowException is Ex) || tcf.ShowException.GetType().IsSubclassOf(typeof(Ex)))
                ? catchClause(tcf.ShowException as Ex).Return() : Return(tcf.Value);
        }

        public static TryCatchFinallyMonad<T> Finally<T>(this TryCatchFinallyMonad<T> t, Action<T> tfm)
        {
            if (t.HasException)
            {
                tfm(default(T));
                return t;
            }
            //return tfm(t.Value).Return(); // possibly this should combine t with the tfm output
            return t;
        }
    }
}
