
namespace Utils
{
    public static class StringUtils
    {
        public static string ToLower(this string input)
        {
            return string.IsNullOrEmpty(input) ? string.Empty : input.ToLower();
        }

        public static string[] Split(this string input,char separator)
        {
            return string.IsNullOrEmpty(input) ? new string[]{} : input.Split(separator);
        }
    }
}
