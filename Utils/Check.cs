using System;
using System.Reflection;

namespace Shaftesbury.Functional.Utils
{
    /// <summary>
    /// Opt provides chk, an implementation of '??' for OptionType, and act, which is similar except that it performs the specified action
    /// when the checked value is null. act is provided for both Nullable and OptionType.
    /// </summary>
    public static class Opt
    {
        public static T chk<T>(this OptionType<T> a, T t) where T : class // would rather be able to provide operator ?? but this isn't allowed (at the mo)
        {
            return a.None ? t : a.Some;
        }

        public static T chk<T>(this OptionType<T> a) where T : class, new()
        {
            return a.None ? new T() : a.Some;
        }

        public static string chk(this OptionType<string> a)
        {
            return a.None ? string.Empty : a.Some;
        }

        public static T chk<T>(this OptionType<T> a, Func<T> f) where T : class // for those cases when you shouldn't evaluate the 'else' branch unless it's actually needed
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            return a.None ? f() : a.Some;
        }

        public static string chk(string a, string def)
        {
            return string.IsNullOrEmpty(a) ? def : a;
        }

        /// <summary>
        /// If the OptionType has a value, return it, otherwise execute the action and return a new T.
        /// </summary>
        /// <typeparam name="T">must be a reference type with new()</typeparam>
        /// <param name="a">the value to be checked</param>
        /// <param name="f">the action to be executed</param>
        /// <returns>either a.Some or new T()</returns>
        /// <remarks>This is the preferred function to use when the action contains a throw clause in which the exception body is 
        /// constructed at the call-site using expensive functions.</remarks>
        public static T act<T>(this OptionType<T> a, Action f) where T : class, new()
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            if (a.None)
            {
                f();
                return new T();
            }
            return a.Some;
        }

        /// <summary>
        /// If the Nullable has a value, return it, otherwise execute the action and return the default value for T.
        /// </summary>
        /// <typeparam name="T">must be a value type</typeparam>
        /// <param name="a">the value to be checked</param>
        /// <param name="f">the action to be executed</param>
        /// <returns>either a.Value or default T</returns>
        /// <remarks>This is the preferred function to use when the action contains a throw clause in which the exception body is 
        /// constructed at the call-site using expensive functions.</remarks>
        public static T act<T>(this T? a, Action f) where T : struct
        {
            #region Precondition
            if (f == null) throw new ArgumentNullException("f");
            #endregion
            if (!a.HasValue)
            {
                f();
                return a.GetValueOrDefault(); // this will return the default T(), unless f() throws an uncaught exception of course ;-)
            }
            return a.Value;
        }

        /// <summary>
        /// If the Nullable has a value, return it, otherwise throw the specified exception.
        /// </summary>
        public static T act<T>(this T? a, Exception ex) where T : struct
        {
            #region Precondition
            if (ex == null) throw new ArgumentNullException("ex");
            #endregion
            if (!a.HasValue) throw ex;
            return a.Value;
        }

        /// <summary>
        /// If the Nullable has a value, return it, otherwise throw the specified exception with the specified message. This uses reflection.
        /// Additionally, there is the possibility of an InvalidCastException if the Type specified does not inherit from Exception. Use with 
        /// caution.
        /// </summary>
        public static T act<T>(this T? a, string message, Type ex) where T : struct
        {
            #region Precondition
            if (ex == null) throw new ArgumentNullException("ex");
            #endregion
            if (!a.HasValue) throw (Exception)ex.InvokeMember(null, BindingFlags.CreateInstance, null, null, new object[] { message });
            return a.Value;
        }

        public static T act<T>(this OptionType<T> a, string message, Type ex) where T : class
        {
            #region Precondition
            if (ex == null) throw new ArgumentNullException("ex");
            #endregion
            if (a.None) throw (Exception)ex.InvokeMember(null, BindingFlags.CreateInstance, null, null, new object[] { message });
            return a.Some;
        }
    }

    /// <summary>
    /// Generic null checking with a special case for string, where string.Empty is treated equivalently to (string)null.
    /// </summary>
    public static class Check
    {
        public static bool IsNull(this string s)
        {
            return string.IsNullOrEmpty(s);
        }

        public static bool IsNotNull(this string s)
        {
            return !IsNull(s);
        }

        public static bool IsNull<T>(this T t) where T : class
        {
            return t == null;
        }

        public static bool IsNotNull<T>(this T t) where T : class
        {
            return !IsNull(t);
        }

        // We need these two because OptionType<T> cannot call IsNull(string), it can only call IsNull<string>(string)
        public static bool IsNullString(this string s)
        {
            return IsNull(s);
        }
        public static bool IsNotNullString(this string s)
        {
            return IsNotNull(s);
        }

        // Handy checks used throughout GUI code
        public static bool IsNullAsString(this object o)
        {
            return o == null || string.IsNullOrEmpty(o.ToString());
        }
        public static bool IsNotNullAsString(this object o)
        {
            return o != null && !string.IsNullOrEmpty(o.ToString());
        }
    }
}
