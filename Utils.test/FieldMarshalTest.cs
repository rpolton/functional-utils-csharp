using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;

namespace Utils.Test
{
    [TestFixture]
    class FieldMarshalTest
    {
        [Test]
        public void FieldMarshalTest1()
        {
            var input = new[] {"field1", "field2", "field3"};
            var mandatoryFields = new[] {"field1"};
            var optionalFields = new[] {"field2"};
            
            var results = input.PartitionWithVerify(mandatoryFields, optionalFields);
            Assert.IsTrue(results.AreAllMandatoryFieldsPresent);
            CollectionAssert.AreEquivalent(mandatoryFields, results.MajorFieldsPresent);
            CollectionAssert.AreEquivalent(optionalFields, results.MinorFieldsPresent);
            CollectionAssert.AreEquivalent(mandatoryFields, results.MajorFieldsPresent.Union(results.AWOLFields));
            CollectionAssert.AreEquivalent(new[]{"field3"},results.UnexpectedFields);
            CollectionAssert.AreEquivalent(input,results.MajorFieldsPresent.Union(results.MinorFieldsPresent).Union(results.UnexpectedFields));
            Assert.AreEqual(0, results.AWOLFields.Count);
        }

        [Test]
        public void FieldMarshalTest2()
        {
            var input = new[] { "field1", "field2", "field3" };
            var mandatoryFields = new[] { "field2" };
            var optionalFields = new string[] { };

            var results = input.PartitionWithVerify(mandatoryFields, optionalFields);
            Assert.IsTrue(results.AreAllMandatoryFieldsPresent);
            CollectionAssert.AreEquivalent(mandatoryFields, results.MajorFieldsPresent);
            CollectionAssert.AreEquivalent(optionalFields, results.MinorFieldsPresent);
            CollectionAssert.AreEquivalent(mandatoryFields, results.MajorFieldsPresent.Union(results.AWOLFields));
            CollectionAssert.AreEquivalent(new[] { "field1", "field3" }, results.UnexpectedFields);
            CollectionAssert.AreEquivalent(input, results.MajorFieldsPresent.Union(results.MinorFieldsPresent).Union(results.UnexpectedFields));
            Assert.AreEqual(0, results.AWOLFields.Count);
        }

        [Test]
        public void FieldMarshalTest3()
        {
            var input = new[] { "field1", "field2", "field3" };
            var mandatoryFields = new[] { "field1","field4" };
            var optionalFields = new [] { "field2","field5"};

            var results = input.PartitionWithVerify(mandatoryFields, optionalFields);
            Assert.IsFalse(results.AreAllMandatoryFieldsPresent);
            CollectionAssert.AreEquivalent(new[]{"field1"}, results.MajorFieldsPresent);
            CollectionAssert.AreEquivalent(new[]{"field2"}, results.MinorFieldsPresent);
            CollectionAssert.AreEquivalent(mandatoryFields, results.MajorFieldsPresent.Union(results.AWOLFields));
            CollectionAssert.AreEquivalent(new[] { "field3" }, results.UnexpectedFields);
            CollectionAssert.AreEquivalent(input, results.MajorFieldsPresent.Union(results.MinorFieldsPresent).Union(results.UnexpectedFields));
            CollectionAssert.AreEquivalent(new[]{"field4"},results.AWOLFields);
        }

    }
}
