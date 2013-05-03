using System;

namespace Shaftesbury.Functional.Utils
{
    public class EmptyOptionTypeException : Exception {}

    /// <summary>
    /// Use OptionType&lt;T&gt; when the reference type T could be null. It behaves like System.Nullable&lt;T&gt;.
    /// If you want to use this for Value Types, use System.Nullable&lt;T&gt; instead. Also, OptionType&lt;string&gt; behaves slightly
    /// differently, in that the empty string is also considered as an empty OptionType, ie OptionType&lt;string&gt;(string.Empty).None is true.
    /// </summary>
    public struct OptionType<T> : IDisposable where T:class
    {
        private readonly T _t;
        public OptionType(T t)
        {
            _t = ((typeof (T) == typeof (string) && Check.IsNotNullString(t as string)) ||
                  (typeof (T) != typeof (string) && Check.IsNotNull(t)))
                     ? t
                     : null;
        }
        public T Some { get { if (_t!=null) return _t; else throw new EmptyOptionTypeException(); } }
        public bool None { get { return _t==null; } }
        public static OptionType<T> Null { get { return new OptionType<T>(); } }
        public static implicit operator OptionType<T>(T t) { return new OptionType<T>(t);}
        // We do not have an implicit operator T because we want to be explicit about checking for null
        public void Dispose() { if (typeof(T) is IDisposable && !None) ((IDisposable)_t).Dispose(); }
    }
}
