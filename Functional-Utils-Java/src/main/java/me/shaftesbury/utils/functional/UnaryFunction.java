package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 01/12/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class UnaryFunction<A,B> implements Func<A,B>
{
    public <C>UnaryFunction<A,C> then(final Func<B, C> f)
    {
        return compose(this,f);
    }

    private static final <A,B,C>UnaryFunction<A,C> compose(final Func<A,B> f, final Func<B,C> g)
    {
        return new UnaryFunction<A,C>()
        {
            public C apply(final A a) { return g.apply(f.apply(a));}
        };
    }

    public static final <A,B>Func0<B> delay(final Func<A,B> f, final A a)
    {
        return new Func0<B>()
        {
            @Override
            public B apply() {
                return f.apply(a);
            }
        };
    }
}
