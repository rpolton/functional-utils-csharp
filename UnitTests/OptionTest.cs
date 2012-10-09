using NUnit.Framework;
using Utils;

namespace UnitTests
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
    }
}
