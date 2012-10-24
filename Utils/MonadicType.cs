using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Utils
{
    public class MonadicType<T>
    {
        public static MonadicType<T> Return(T t)
        {
            return new MonadicType<T>(t);
        }

        public static MonadicType<U> Bind<U>(MonadicType<T> t, Func<T, MonadicType<U>> tfm)
        {
            return t.Bind(tfm);
        }

        public MonadicType<U> Bind<U>(Func<T, MonadicType<U>> tfm)
        {
            return tfm(Value);
        }
        
        private T Value { get; set; }
        private MonadicType(T t)
        {
            Value = t;
        }
    }

    public static class MonadicType
    {
        public static MonadicType<T> Return<T>(this T t)
        {
            return MonadicType<T>.Return(t);
        }
    }
}
