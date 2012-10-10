using System;
using NUnit.Framework;

namespace Utils.Test
{
    [TestFixture]
    public class MExceptionTest
    {
        [Test]
        public void MExceptionTest1()
        {
            var a = new MException<int>(10);
            Assert.IsFalse(a.HasException);
            Assert.AreEqual(10,a.Value);
        }

        [Test]
        public void MExceptionTest2()
        {
            var a = new MException<int>(new Exception("arg"));
            Assert.IsTrue(a.HasException);
            Assert.Throws<MExceptionValueAccessException>(() => a.Value.Ignore());
        }

        [Test]
        public void MExceptionTest3()
        {
            var a = 10.ToMException().SelectMany(b => 0.ToMException(), (b, c) => b/c);
            Assert.IsTrue(a.HasException);
        }

        [Test]
            public void MExceptionTest4()
        {
            Func<int, int> times2 = a => a*2;

            var d = from b in 5.ToMException()
                    from c in times2.ToMException()
                    select c(b);
            Assert.IsFalse(d.HasException);
            Assert.AreEqual(10,d.Value);
        }

        [Test]
        public void MExceptionTest5()
        {
            int zero = 0;
            Func<int, int, int> div = (a,b) => a / b;
            Func<int, int> div0 = a => div(a, zero);

            var d = from b in 5.ToMException()
                    from c in div0.ToMException()
                    select c(b);
            Assert.IsTrue(d.HasException);
        }

        [Test]
        public void MExceptionTest6()
        {
            int zero = 0;
            MException<Func<int,int,int>> div = MException.ToMException<Func<int,int,int>>((a, b) => a / b);
            MException<Func<int, int>> div0 = div.Bind(divFn => MException.ToMException<Func<int, int>>(a => divFn(a, zero)));

            var d = from b in 5.ToMException()
                    from c in div0
                    select c(b);
            Assert.IsTrue(d.HasException);
        }
    }
}
