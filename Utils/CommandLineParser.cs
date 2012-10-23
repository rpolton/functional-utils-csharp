using System;
using System.Collections.Generic;
using System.Linq;

namespace Utils
{
    public class Argument : IEquatable<Argument>
    {
        public Argument()
        {
            Name = string.Empty;
            ExpectsValue = false;
            Value = string.Empty;
        }
        public string Name { get; set; }
        public bool ExpectsValue { get; set; }
        public string Value { get; set; }

        public bool Equals(Argument other)
        {
            throw new NotImplementedException();
        }
    }

    public static class ArgumentParser
    {
        public static Tuple<List<Argument>, List<Argument>, HashSet<string>, HashSet<string>> SplitArgsInto(this IList<string> input, IEnumerable<Argument> mandatory, IEnumerable<Argument> optional)
        {
            var mandatoryAndValues = new List<Argument>();
            var optionalAndValues = new List<Argument>();
            var unrecognised = new HashSet<string>();

            for (int i = 0; i < input.Count; ++i)
            {
                var arg = input[i];
                if (mandatory.Any(m => m.Name == arg))
                {
                    var mArg = mandatory.First(m => m.Name == arg);
                    var newArg = new Argument() { Name = mArg.Name, ExpectsValue = mArg.ExpectsValue, Value = mArg.ExpectsValue ? input[++i] : string.Empty }; // hmmm might be missing the value
                    mandatoryAndValues.Add(newArg);
                }
                else if (optional.Any(m => m.Name == arg))
                {
                    var oArg = optional.First(o => o.Name == arg);
                    var newArg = new Argument() { Name = oArg.Name, ExpectsValue = oArg.ExpectsValue, Value = oArg.ExpectsValue ? input[++i] : string.Empty }; // hmmm might be missing the value
                    optionalAndValues.Add(newArg);
                }
                else unrecognised.Add(arg);
            }

            var mandatoryMissing = mandatory.Select(m => m.Name).Except(mandatoryAndValues.Select(m => m.Name)).ToHashSet();

            return Tuple.Create(mandatoryAndValues, optionalAndValues, unrecognised, mandatoryMissing);
        }
    }
}
