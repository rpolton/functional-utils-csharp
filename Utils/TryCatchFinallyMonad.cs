using System;

namespace Shaftesbury.Functional.Utils
{
    public static class TryCatchFinallyMonadBuilder
    {
        private static MException<T> Return<T>(this T t)
        {
            return new MException<T>(t);
        }

        public static MException<U> Try</*T,*/U>(Func</*T is void,*/U> tfm)
        {
            #region Precondition
            if (tfm == null) throw new ArgumentNullException("tfm");
            #endregion
            try
            {
                return tfm().Return();
            }
            catch (Exception ex)
            {
                return new MException<U>(ex);
            }
        }

        public static MException<T> Catch<Ex, T>(this MException<T> tcf, Func<Ex, T> catchClause) where Ex : Exception
        {
            #region Precondition
            if (tcf == null) throw new ArgumentNullException("tcf");
            if (catchClause == null) throw new ArgumentNullException("catchClause");
            #endregion
            return tcf.HasException && ((tcf.ShowException is Ex) || tcf.ShowException.GetType().IsSubclassOf(typeof(Ex)))
                ? catchClause(tcf.ShowException as Ex).Return() : Return(tcf.Value);
        }

        public static MException<T> Finally<T>(this MException<T> t, Action<T> tfm)
        {
            #region Precondition
            if (tfm == null) throw new ArgumentNullException("tfm");
            #endregion
            tfm(t.HasException ? default(T) : t.Value);
            return t;
        }
    }
}
