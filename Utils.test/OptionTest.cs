using System.Linq;
using NUnit.Framework;

namespace Shaftesbury.Functional.Utils.Test
{
    [TestFixture]
    public class OptionTest
    {
        [Test]
        public void OptionTestValueType1()
        {
            const int expected = 10;
            var a = expected.ToOption();
            Assert.IsTrue(a.IsSome);
            Assert.IsFalse(a.IsNone);
            Assert.AreEqual(expected, a.Some);
        }

        [Test]
        public void OptionTestNullableType1()
        {
            var a = new Option<int?>(null);
            Assert.IsFalse(a.IsSome);
            Assert.IsTrue(a.IsNone);
            Assert.Throws<OptionValueAccessException>(()=>a.Some.Ignore());
        }

        [Test]
        public void OptionTestStringType1()
        {
            const string expected = "ll";
            var a = expected.ToOption();
            Assert.IsTrue(a.IsSome);
            Assert.IsFalse(a.IsNone);
            Assert.AreEqual(expected, a.Some);
        }

        [Test]
        public void OptionTestValueType2()
        {
            var a = Option<int>.None;
            Assert.IsTrue(a.IsNone);
            Assert.IsFalse(a.IsSome);
            Assert.Throws<OptionValueAccessException>(() => a.Some.Ignore());
        }

        class tmp{}

        [Test]
        public void OptionTestReferenceType1()
        {
            var a = new tmp().ToOption();
            Assert.IsTrue(a.IsSome);
            Assert.IsFalse(a.IsNone);
            a.Some.Ignore();
        }

        [Test]
        public void OptionTestReferenceType2()
        {
            var a = new Option<tmp>(null);
            Assert.IsTrue(a.IsNone);
            Assert.IsFalse(a.IsSome);
            Assert.Throws<OptionValueAccessException>(() => a.Some.Ignore());
        }

        [Test]
        public void OptionMonadTest1()
        {
            var e = from a in 10.ToOption()
                    from b in 2.ToOption()
                    select a + b;
            Assert.IsTrue(e.IsSome);
            Assert.IsFalse(e.IsNone);
            Assert.AreEqual(12, e.Some);
        }

        [Test]
        public void OptionMonadTest2()
        {
            var e = from a in Option<int>.None
                    from b in 2.ToOption()
                    select a + b;
            Assert.IsTrue(e.IsNone);
            Assert.IsFalse(e.IsSome);
            Assert.Throws<OptionValueAccessException>(() => e.Some.Ignore());
        }

        [Test]
        public void OptionMonadTest3()
        {
            var e = from a in 10.ToOption()
                    from b in Option<int>.None
                    select a + b;
            Assert.IsTrue(e.IsNone);
            Assert.IsFalse(e.IsSome);
            Assert.Throws<OptionValueAccessException>(() => e.Some.Ignore());
        }

        [Test]
        public void OptionMonadTest4()
        {
            var e = from a in Option<int>.None
                    from b in Option<int>.None
                    select a + b;
            Assert.IsTrue(e.IsNone);
            Assert.IsFalse(e.IsSome);
            Assert.Throws<OptionValueAccessException>(() => e.Some.Ignore());
        }

        [Test]
        public void OptionNoneIsNoneTest1()
        {
            var none = Option<int>.None;
            Assert.IsTrue(none.IsNone);
            Assert.IsFalse(none.IsSome);
        }

        [Test]
        public void OptionNoneIsNoneTest2()
        {
            var none = Option<string>.None;
            Assert.IsTrue(none.IsNone);
            Assert.IsFalse(none.IsSome);
        }

        [Test]
        public void OptionNoneIsNoneTest3()
        {
            var none = Option<tmp>.None;
            Assert.IsTrue(none.IsNone);
            Assert.IsFalse(none.IsSome);
        }

        struct tmp2{}

        [Test]
        public void OptionNoneIsNoneTest4()
        {
            var none = Option<tmp2>.None;
            Assert.IsTrue(none.IsNone);
            Assert.IsFalse(none.IsSome);
        }

        [Test]
        public void OptionBindTest1()
        {
            var a = 1.ToOption();
            var b = a.Bind(o => (o*2).ToOption());
            Assert.IsTrue(b.IsSome);
            Assert.AreEqual(2,b.Some);
        }

        [Test]
        public void OptionBindTest2()
        {
            var a = Option<int>.None;
            var b = a.Bind(o => (o * 2).ToOption());
            Assert.IsTrue(b.IsNone);
        }

        [Test]
        public void OptionBindTest3()
        {
            var input = new[] {1, 2, 3, 4, 5, 6};
            System.Func<int, bool> isEven = i => i%2 == 0;
            var expected = new[] {2, 4, 6};

            var output = input.Select(i => i.ToOption().Bind(j => isEven(j) ? j.ToOption() : Option<int>.None));
            CollectionAssert.AreEquivalent(expected, output.Where(o=>o.IsSome).Select(o=>o.Some).ToList());
        }

    }
}
