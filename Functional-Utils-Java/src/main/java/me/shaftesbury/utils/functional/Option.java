package me.shaftesbury.utils.functional;

/**
 * Option is an implementation of the <tt>option monad</tt>.
 * See http://en.wikipedia.org/wiki/Option_type
 */
public final class Option<T>
{
    private final T _t;
    private Option()
    {
        _t=null;
    }
    private Option(final T t)
    {
        _t = t;
    }

    /**
     * @throws me.shaftesbury.utils.functional.OptionNoValueAccessException
     * @return
     */
    public T Some()
    {
        if(_t!=null) return _t;
        else throw new OptionNoValueAccessException();
    }

    /**
     *
     * @param t
     * @return
     * @throws me.shaftesbury.utils.functional.OptionNoValueAccessException
     */
    public static <U>Option<U> Some(final U t)
    {
        if(t==null) throw new OptionNoValueAccessException();
        return toOption(t);
    }

    /**
     *
     * @param <U>
     * @return
     */
    public static <U>Option<U> None()
    {
        return new Option<U>();
    }

    /**
     *
     * @return
     */
    public boolean isSome()
    {
        return _t!=null;
    }

    /**
     *
     * @return
     */
    public boolean isNone()
    {
        return _t==null;
    }

    /**
     *
     * @param o
     * @return
     */
    public boolean equals(final Object o)
    {
        if(o instanceof Option<?>)
        {
            final Option<?> other = (Option<?>)o;
            try
            {
                return isSome()==other.isSome() && Some().equals(other.Some());
            }
            catch(final OptionNoValueAccessException ex)
            {
                return isNone() && other.isNone(); // every None is considered to be the same
            }
        }
        else return false;
    }

    /**
     *
     * @return
     */
    public int hashCode()
    {
        return isNone() ? 0 : 31 * _t.hashCode();
    }

    /**
     *
     * @return
     */
    public String toString()
    {
        return isSome()
                ? "Option( "+ Some().toString() + " )"
                : "None";
    }

    /**
     *
     * @param t
     * @param <U>
     * @return
     */
    public static <U>Option<U> toOption(U t)
    {
        return new Option<U>(t);
    }

    /**
     *
     * @param f
     * @param <U>
     * @return
     */
    public <U>Option<U> bind(final Func<T,Option<U>> f)
    {
        if(isSome()) return f.apply(Some());
        else return Option.None();
    }

    /**
     *
     * @param f
     * @param o1
     * @param o2
     * @param <A>
     * @param <B>
     * @param <C>
     * @return
     */
    public static <A,B,C>Option<C>lift(final Func2<A,B,C> f, final Option<A> o1, final Option<B> o2)
    {
        if(o1.isSome() && o2.isSome()) return toOption(f.apply(o1.Some(),o2.Some()));
        else return None();
    }
}
