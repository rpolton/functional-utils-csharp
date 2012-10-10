using NUnit.Framework;

namespace Utils.Test
{
    [TestFixture]
    class MonadTest
    {
        [Test]
        public void MaybeTest1()
        {
            int top = 10;
            var b1 = 0;
            var r = from t in top.ToMaybe()
                    from b in MaybeFns.Div(t, b1)
                    select b;
            Assert.IsInstanceOf(typeof(Nothing<int>), r);            
        }

        [Test]
        public void MaybeTest2()
        {
            int top = 10;
            var b1 = 2;
            var r = from t in top.ToMaybe()
                    from b in MaybeFns.Div(t, b1)
                    from c in MaybeFns.Div(t, b)
                    select c;
            Assert.IsInstanceOf(typeof(Something<int>), r);
            Assert.AreEqual(2, (r as Something<int>).Value);
        }

    }
}
