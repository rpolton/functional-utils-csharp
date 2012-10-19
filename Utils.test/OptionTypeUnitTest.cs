using System;
using NUnit.Framework;

namespace Utils.Test
{
    [TestFixture]
    class OptionTypeUnitTest
    {
        class TestClass
        {
            private string _s = string.Empty;
            public TestClass() { }
            public TestClass(string s) { _s = s; }
            public static implicit operator string(TestClass t) { return t._s; }
        }

        public OptionType<T> funcNull<T>(T t) where T : class { return new OptionType<T>(); }
        public OptionType<T> func_notNull<T>(T t) where T : class { return new OptionType<T>(t); }
        public T func_imp<T>(T t) { return t; }

        [Test]
        public void OptionTypeTest1()
        {
            OptionType<TestClass> t = funcNull(new TestClass());
            Assert.IsTrue(t.None);
        }

        [ExpectedException(typeof(EmptyOptionTypeException))]
        [Test]
        public void OptionTypeTest2()
        {
            OptionType<TestClass> t = funcNull(new TestClass());
            TestClass s = t.Some;
        }

        [Test]
        public void OptionTypeTest3()
        {
            OptionType<TestClass> t = func_notNull(new TestClass());
            Assert.IsFalse(t.None);
        }

        [Test]
        public void OptionTypeTest4()
        {
            TestClass tc = new TestClass();
            OptionType<TestClass> t = func_notNull(tc);
            Assert.AreEqual(tc, t.Some);
        }

        [Test]
        public void OptionTypeTest5()
        {
            OptionType<TestClass> t = new OptionType<TestClass>(null);
            Assert.IsTrue(t.None);
        }

        [ExpectedException(typeof(EmptyOptionTypeException))]
        [Test]
        public void OptionTypeTest6()
        {
            OptionType<TestClass> t = new OptionType<TestClass>(null);
            TestClass s = t.Some;
        }
        /*
        [Test]
        public void OptionTypeTest7()
        {
            string s = func_imp(new OptionType<string>("test"));
        }*/

        [Test]
        public void OptionTypeTest8()
        {
            OptionType<TestClass> t = OptionType<TestClass>.Null;
            Assert.IsTrue(t.None);
        }

        [ExpectedException(typeof(EmptyOptionTypeException))]
        [Test]
        public void OptionTypeTest9()
        {
            OptionType<TestClass> t = OptionType<TestClass>.Null;
            TestClass s = t.Some;
        }

        private bool testfn(OptionType<TestClass> p)
        {
            return !p.None;
        }

        [Test]
        public void OptionTypeTest10()
        {
            Assert.IsTrue(testfn(new TestClass("bob")));
        }

        private string testfn2(OptionType<TestClass> p)
        {
            return p.Some;
        }

        [Test]
        public void OptionTypeTest11()
        {
            Assert.AreEqual("bob", testfn2(new TestClass("bob")));
        }

        [Test]
        public void OptionTypeTest12()
        {
            Assert.IsFalse(Check.IsNull(new TestClass()));
            Assert.IsFalse(Check.IsNull(new string(new char[] { 'd', 'f', 'g', 'h' })));
            Assert.IsTrue(Check.IsNull(string.Empty));
        }

        [Test]
        public void OptionTypeTest13()
        {
            Assert.IsTrue(Check.IsNotNull(new TestClass()));
            Assert.IsTrue(Check.IsNotNull(new string(new char[] { 'd', 'f', 'g', 'h' })));
            Assert.IsFalse(Check.IsNotNull(string.Empty));
        }

        [Test]
        public void OptionTypeTest14()
        {
            OptionType<string> a = new OptionType<string>(string.Empty);
            OptionType<string> b = new OptionType<string>();
            OptionType<string> c = new OptionType<string>("test");
            Assert.IsTrue(a.None);
            Assert.IsTrue(b.None);
            Assert.IsFalse(c.None);
        }

        private interface IInterface
        {
            object data();
        }
        private class TestDouble : IInterface
        {
            private double _d;
            public TestDouble(double d)
            {
                _d = d;
            }
            public double d { get { return _d; } }
            public object data()
            {
                return d;
            }
        }
        private class TestInt : IInterface
        {
            private int _d;
            public TestInt(int d)
            {
                _d = d;
            }
            public int d { get { return _d; } }
            public object data()
            {
                return d;
            }
        }

        [Test]
        public void DoubleCastTest1()
        {
            object o = new TestDouble(3.14);
            IInterface d = o as TestDouble;
            IInterface i = o as TestInt;
            Assert.IsNotNull(d);
            Assert.IsNull(i);
        }

        [ExpectedException(typeof(InvalidCastException))]
        [Test]
        public void DoubleCastTest2()
        {
            object o = new TestDouble(3.14);
            OptionType<TestDouble> d = (TestDouble)(o);
            OptionType<TestInt> i = (TestInt)o;
            Assert.IsNotNull(d);
            Assert.IsNull(i);
        }

        // Sadly, neither of these tests work because we do not appear to be able to enforce a non-null OptionType
        private static OptionType<TestClass> f15(OptionType<TestClass> o) { return o; }
        [Ignore]
        [Test]
        public void OptionTypeTest15()
        {
            OptionType<TestClass> o = f15(null);
            Assert.IsTrue(o.None);
        }

        private static bool f16(OptionType<TestClass> o) { return o.None; }
        [Ignore]
        [Test]
        public void OptionTypeTest16()
        {
            Assert.IsTrue(f16(null));
        }

        [Test]
        public void OptionTypeTest17()
        {
            OptionType<TestClass> t = null;
            Assert.IsTrue(t.None);
        }

        [ExpectedException(typeof(EmptyOptionTypeException))]
        [Test]
        public void OptionTypeTest18()
        {
            OptionType<TestClass> t = null;
            TestClass s = t.Some;
        }


        private class TestException : Exception { }
        [Test]
        [ExpectedException(typeof(TestException))]
        public void ActionTest1()
        {
            Opt.act(OptionType<TestClass>.Null, delegate { throw new TestException(); });
        }

        [Test]
        public void ActionTest2()
        {
            TestClass t = Opt.act(OptionType<TestClass>.Null, delegate { Console.WriteLine("Null"); });
            Assert.IsEmpty(t);
        }

        [Test]
        public void ActionTest3()
        {
            const string expected = "test";
            TestClass t = Opt.act(new OptionType<TestClass>(new TestClass(expected)), delegate { throw new TestException(); });
            Assert.AreEqual(expected, (string)t);
        }

        [Test]
        [ExpectedException(typeof(TestException))]
        public void ActionTest4()
        {
            Opt.act((int?)null, delegate { throw new TestException(); });
        }

        [Test]
        public void ActionTest5()
        {
            int t = Opt.act((int?)null, delegate { Console.WriteLine("Null"); });
            Assert.AreEqual(0, t);
        }

        [Test]
        public void ActionTest6()
        {
            const int expected = 5;
            int t = Opt.act((int?)expected, delegate { throw new TestException(); });
            Assert.AreEqual(expected, t);
        }

        private static string fn7() { return "this is a string"; }
        [Test]
        [ExpectedException(typeof(Exception), ExpectedMessage = "this is a string")]
        public void ActionTest7()
        {
            int t = Opt.act((int?)null, delegate { throw new Exception(fn7()); });
        }

        [Test]
        [ExpectedException(typeof(Exception), ExpectedMessage = "this is a string")]
        public void ActionTest8()
        {
            int t = Opt.act((int?)null, new Exception(fn7()));
        }

        [Test]
        [ExpectedException(typeof(Exception), ExpectedMessage = "this is a string")]
        public void ActionTest9()
        {
            int t = Opt.act((int?)null, fn7(), typeof(Exception));
        }

        [Test]
        public void CheckTest1()
        {
            const string expected = "expected";
            string s = Opt.chk<string>(expected, delegate { throw new TestException(); });
            Assert.AreEqual(expected, s);
        }

        [Test]
        public void CheckTest2()
        {
            const string expected = "expected";
            string s = Opt.chk<string>(string.Empty, delegate { return expected; });
            Assert.AreEqual(expected, s);
        }

        [Test]
        public void CheckTest3()
        {
            const string expected = "expected";
            string s = Opt.chk(OptionType<string>.Null, delegate { return expected; });
            Assert.AreEqual(expected, s);
        }

        [Test]
        [ExpectedException(typeof(TestException))]
        public void CheckTest4()
        {
            string s = Opt.chk(OptionType<string>.Null, delegate { throw new TestException(); });
        }

        [Test]
        public void CheckTest5()
        {
            string s = Opt.chk<string>((string) null, "alternative");
            Assert.AreEqual("alternative",s);
        }

        [Test]
        public void CheckTest6()
        {
            string s = Opt.chk<string>("", "alternative");
            Assert.AreEqual("alternative", s);
        }
    }
}
