package me.shaftesbury.utils.functional;

/**
 * An implementation of {@link me.shaftesbury.utils.functional.Func} that is intended for use by the {@link me.shaftesbury.utils.functional.MException}
 * @param <A> the type of the argument of the function
 * @param <B> the type of the return value of the function
 */
public abstract class UnaryFunction<A,B> implements Func<A,B>
{
    /**
     * Functional composition. Return a new function which, when evaluated, returns the composition of the current {@link me.shaftesbury.utils.functional.UnaryFunction}
     * and the supplied {@link me.shaftesbury.utils.functional.Func} parameter.
     * @param f the single-argument function to be composed with this
     * @param <C> the type of the return value of f
     * @return a {@link me.shaftesbury.utils.functional.UnaryFunction} that accepts a parameter of type A and returns a datum of type C
     * @see <a href="http://en.wikipedia.org/wiki/Function_composition_(computer_science)">Function Composition</a>
     */
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

    /**
     * Given a {@link me.shaftesbury.utils.functional.Func} and its argument, produce a function of no arguments that
     * can be evaluated at a later point.
     * @param f the one-parameter function
     * @param a the parameter of f
     * @param <A> the type of the parameter of f
     * @param <B> the type of the return value of the delayed function
     * @return a function that takes no arguments and returns a value of type B
     */
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
