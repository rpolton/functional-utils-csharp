using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.ComponentModel;

namespace Utils
{
    public static class EnumUtils
    {
        public static string StringValueOf(Enum value)
        {
            FieldInfo fi = value.GetType().GetField(value.ToString());
            var attributes = (DescriptionAttribute[])fi.GetCustomAttributes(typeof(DescriptionAttribute), false);
            return attributes.Length > 0 ? attributes[0].Description : value.ToString();
        }

        public static object EnumValueOf(string value, Type enumType)
        {
            try
            {
                return Enum.Parse(enumType, Enum.GetNames(enumType).First(
                    name => StringValueOf((Enum) Enum.Parse(enumType, name)).Equals(value)));
            }
            catch (KeyNotFoundException)
            {
                throw new ArgumentException("The string is not a description or value of the specified enum.");
            }
        }
    }
}
