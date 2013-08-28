using System;
using System.Collections.Generic;
using System.Linq;

namespace Shaftesbury.Functional.Utils
{
    public static class FieldMarshal
    {
        public class DiscriminatedFieldContainer<A>
        {
            public HashSet<A> MajorFieldsPresent { get; internal set; }
            public HashSet<A> MinorFieldsPresent { get; internal set; }
            public HashSet<A> AWOLFields { get; internal set; }
            public HashSet<A> UnexpectedFields { get; internal set; }
            public bool AreAllMandatoryFieldsPresent { get { return AWOLFields.Count == 0; } }
        }

        /// <summary>
        /// For a given input sequence of field values, partition them into two sets, one of required fields (MajorFields) and one of optional fields (MinorFields).
        /// While the partitioning is proceeding, the function builds up an enhanced result set which also indicates which required fields are missing, if any, and
        /// which additional fields are present, if any.
        /// </summary>
        /// <typeparam name="A"></typeparam>
        /// <param name="input">A collection of input parameters in which we expect to find the mandatory and optional arguments specified in MajorFields and MinorFields. 
        /// This collection must be multiply traversable.</param>
        /// <param name="MajorFields">A concrete collection containing those fields which must be present in the input</param>
        /// <param name="MinorFields">A concrete collection containing those fields which can optionally be present in the input</param>
        /// <returns></returns>
        public static DiscriminatedFieldContainer<A> PartitionWithVerify<A>(this IEnumerable<A> input, IEnumerable<A> MajorFields, IEnumerable<A> MinorFields)
        {
            #region Precondition
            if (input == null) throw new ArgumentNullException("input");
            if (MajorFields == null) throw new ArgumentNullException("MajorFields");
            if (MinorFields == null) throw new ArgumentNullException("MinorFields");
            #endregion
            var intersect = MajorFields.Intersect(MinorFields).ToList();
            if(intersect.Any()) throw new ArgumentException("Fields cannot be both required and optional: {0}",String.Join(", ",intersect));
            return new DiscriminatedFieldContainer<A>()
                       {
                           MajorFieldsPresent = input.Intersect(MajorFields).ToHashSet(),
                           MinorFieldsPresent = input.Intersect(MinorFields).ToHashSet(),
                           AWOLFields = MajorFields.Except(input).ToHashSet(),
                           UnexpectedFields = input.Except(MajorFields.Union(MinorFields)).ToHashSet()
                       };
        }
    }
}
