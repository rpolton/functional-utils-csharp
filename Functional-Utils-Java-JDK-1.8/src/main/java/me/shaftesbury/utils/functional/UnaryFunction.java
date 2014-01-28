package me.shaftesbury.utils.functional;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 01/12/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class UnaryFunction<A,B> implements Function<A,B>
{
    public <C>UnaryFunction<A,C> then(final Function<B, C> f)
    {
        return compose(this,f);
    }

    private static final <A,B,C>UnaryFunction<A,C> compose(final Function<A,B> f, final Function<B,C> g)
    {
        return new UnaryFunction<A,C>()
        {
            public C apply(final A a) { return g.apply(f.apply(a));}
        };
    }

    public static final <A,B>Supplier<B> delay(final Function<A,B> f, final A a)
    {
        return new Supplier<B>()
        {
            @Override
            public B get() {
                return f.apply(a);
            }
        };
    }
}
