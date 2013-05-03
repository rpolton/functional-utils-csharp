using NUnit.Framework;

namespace Shaftesbury.Functional.Utils.Test
{
    [TestFixture]
    class StringUtilsTest
    {
        [Test]
        public void ToLowerTest1()
        {
            Assert.AreEqual(StringUtils.ToLower(null),string.Empty);
        }

        [Test]
        public void ToLowerTest2()
        {
            Assert.AreEqual(StringUtils.ToLower(string.Empty), string.Empty);
        }

        [Test]
        public void ToLowerTest3()
        {
            Assert.AreEqual(StringUtils.ToLower("76GFgf"), "76gfgf");
        }

        [Test]
        public void ToLowerTest4()
        {
            Assert.AreNotEqual(StringUtils.ToLower("76GFgf"), "76GFgf");
        }

        [Test]
        public void SplitTest1()
        {
            Assert.AreEqual(StringUtils.Split(null, ','), new string[]{});
        }

        [Test]
        public void SplitTest2()
        {
            Assert.AreEqual(StringUtils.Split(string.Empty, ','), new string[] { });
        }

        [Test]
        public void SplitTest3()
        {
            const string testString = "dfhgudehf,ip,asopf";
            Assert.AreEqual(new [] { "dfhgudehf", "ip", "asopf" }, StringUtils.Split(testString, ','));
        }

        [Test]
        public void EqualsNullTest1()
        {
            Assert.IsFalse("X".Equals(null));
        }
    }
}
