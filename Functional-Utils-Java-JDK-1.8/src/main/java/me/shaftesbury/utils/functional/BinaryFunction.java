package me.shaftesbury.utils.functional;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 01/12/13
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class BinaryFunction<A,B,C> implements BiFunction<A,B,C>
{
    public static final <A,B,C>Function<A,C> toFunc(final BiFunction<A, B, C> f, B b)
    {
        return toFunc(f).apply(b);
    }

    public static final <A,B,C>Function<B,Function<A,C>> toFunc(final BiFunction<A, B, C> f)
    {
        return new Function<B,Function<A,C>>()
        {
            @Override
            public Function<A, C> apply(final B b) {
                return new Function<A, C>() {
                    @Override
                    public C apply(final A a) {
                        return f.apply(a,b);
                    }
                };
            }
        };
    }

    public static final <A,B,C>Supplier<C> delay(final BiFunction<A, B, C> f, final A a, final B b)
    {
        return new Supplier<C>() {
            @Override
            public C get() {
                return f.apply(a,b);
            }
        };
    }
}
