package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 01/12/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class BinaryFunction<A,B,C> implements Func2<A,B,C>
{
    public Func<B,Func<A,C>> toFunc()
    {
        return toUnaryFunc(this);
    }

    public Func<A,C> toFunc(B b)
    {
        return toUnaryFunc(this).apply(b);
    }

    private static final <A,B,C>Func<B,Func<A,C>> toUnaryFunc(final BinaryFunction<A,B,C> f)
    {
        return new Func<B,Func<A,C>>()
        {
            @Override
            public Func<A, C> apply(final B b) {
                return new Func<A, C>() {
                    @Override
                    public C apply(final A a) {
                        return f.apply(a,b);
                    }
                };
            }
        };
    }
}
