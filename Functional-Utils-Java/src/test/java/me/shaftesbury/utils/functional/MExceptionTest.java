package me.shaftesbury.utils.functional;


import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Bob on 09/12/13.
 */
public class MExceptionTest
{
    @Test
    public void returnTest1()
    {
        final MException<Object> mex = MException.toMException(
            new Supplier<Object>() {

                public Object get() {
                    return null;
                }
            }
        );

        Assert.assertFalse(mex.hasException());
        Assert.assertEquals(null, mex.read());
    }

    @Test(expected = RuntimeException.class)
    public void readMExceptionInErrorTest()
    {
        final MException<Object> m = MException.toMException(new Supplier<Object>() {

            public Object get() {
                throw new RuntimeException();
            }
        });

        m.read();
    }

    @Test
    public void returnTest2()
    {
        final MException<Integer> mex = MException.toMException(
                new Supplier<Integer>() {

                    public Integer get() {
                        return 10;
                    }
                }
        );

        Assert.assertFalse(mex.hasException());
        Assert.assertEquals(new Integer(10), mex.read());
    }

    @Test
    public void returnWithFuncTest1()
    {
        for(int i=0;i<10;++i)
        {
            final Integer ii = i;
            final Supplier<Integer> f = new Supplier<Integer>() {

                public Integer get() {
                    return new Integer(ii);
                }
            };
            final MException<Integer> mex = MException.toMException(f);
            Assert.assertFalse(mex.hasException());
            Assert.assertEquals(ii, mex.read());
        }
    }

    private static Function<Integer,Integer> DoublingGenerator =
            new Function<Integer, Integer>()
            {
                 public Integer apply(Integer a) { return 2*a;}
            };

    @Test
    public void returnWithFuncTest2()
    {
        Iterable2<Integer> it = IterableHelper.init(DoublingGenerator,10);
        java.util.List<MException<Integer>> l = it.map(
                new Function<Integer, MException<Integer>>() {

                    public MException<Integer> apply(final Integer ii) {
                        final Supplier<Integer> f = new Supplier<Integer>() {

                            public Integer get() {
                                if(ii==8||ii==10||ii==16) throw new IllegalArgumentException("value");
                                return new Integer(ii);
                            }
                        };
                        final MException<Integer> mex = MException.toMException(f);
                        return mex;
                    }}).toList();
        Assert.assertEquals(10,l.size());
        for(int i=1;i<=10;++i)
            if(i!=4 && i!=5 && i!=8) Assert.assertFalse(l.get(i-1).hasException());
            else Assert.assertTrue(l.get(i-1).hasException());
    }

    @Test
    public void bindTest1()
    {
        java.util.List<MException<Integer>> l = new ArrayList<MException<Integer>>();
        for(int i=1;i<4;++i)
        {
            final Integer ii=i;
            final MException<Integer> m = MException.toMException(
                    new Supplier<Integer>() {

                        public Integer get() {
                            return new Integer(ii);
                        }
                    }
            );

            for(int j=-i;j<=i;++j)
            {
                final Integer jj = j;
                final MException<Integer> m1 = m.bind(new Function<Integer, MException<Integer>>() {

                    public MException<Integer> apply(final Integer integer) {
                        return MException.toMException(
                                new Supplier<Integer>() {

                                    public Integer get() {
                                        return integer / jj;
                                    }
                                });
                    }
                });

                l.add(m1);
            }
        }

        final java.util.List<MException<Integer>> l1 = Functional.filter(
                new Function<MException<Integer>,Boolean>() {
                    public Boolean apply(final MException<Integer> m) {
                        return !m.hasException();
                    }
                }, l);

        Assert.assertFalse(
                Functional.forAll(
                        new Function<MException<Integer>, Boolean>() {
                            public Boolean apply(final MException<Integer> m2) {
                                return m2.hasException();
                            }
                        }, l1));

        Assert.assertEquals(3, IterableHelper.create(l).filter(new Function<MException<Integer>, Boolean>() {
            public Boolean apply(final MException<Integer> m) {
                return m.hasException();
            }
        }).toList().size());
    }

    @Test
    public void bindTestExceptionThrownInFunc()
    {
        final MException<Integer> m = MException.toMException(
                new Supplier<Integer>() {

                    public Integer get() {
                        return new Integer(1);
                    }
                }
        );

        final MException<Integer> m1 = m.bind(new Function<Integer, MException<Integer>>() {

            public MException<Integer> apply(final Integer integer) {
                throw new RuntimeException("Argh");
            }});

        Assert.assertTrue(m1.hasException());
        final Pair<RuntimeException, StackTraceElement[]> exceptionWithStackTrace = m1.getExceptionWithStackTrace();
        Assert.assertEquals("Argh", exceptionWithStackTrace.getLeft().getMessage());
    }

    @Test
    public void bindTestThisMExceptionHasExceptionAlready()
    {
        final MException<Integer> m = MException.toMException(
                new Supplier<Integer>() {

                    public Integer get() {
                        throw new RuntimeException("Argh");
                    }
                }
        );

        final MException<Integer> m1 = m.bind(new Function<Integer, MException<Integer>>() {

            public MException<Integer> apply(final Integer integer) {
                return MException.toMException(new Supplier<Integer>() {

                    public Integer get() {
                        return new Integer(integer);
                    }
                });
            }});

        Assert.assertTrue(m1.hasException());
        Assert.assertTrue(m.hasException());
        final Pair<RuntimeException, StackTraceElement[]> exceptionWithStackTrace = m1.getExceptionWithStackTrace();
        Assert.assertEquals("Argh",exceptionWithStackTrace.getLeft().getMessage());
    }

    @Test
    public void liftTest1()
    {
        final Integer valu = 10;
        final MException<Integer> a = MException.toMException(new Supplier<Integer>() {

            public Integer get() {
                return 10 / valu;
            }
        });
        final MException<Integer> b = MException.toMException(new Supplier<Integer>() {

            public Integer get() {
                return 20 / valu;
            }
        });
        final MException<Integer> c = MException.lift(new BiFunction<Integer, Integer, Integer>() {

            public Integer apply(Integer o, Integer o2) {
                return o + o2;
            }
        }, a, b);
        Assert.assertFalse(c.hasException());
        Assert.assertEquals(new Integer(3),c.read());
    }
}
