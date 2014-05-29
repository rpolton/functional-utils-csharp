package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 22/10/13
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
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
    public final T Some() throws OptionNoValueAccessException
    {
        if(_t!=null) return _t;
        else throw new OptionNoValueAccessException();
    }
    public static final <T>Option<T> None()
    {
        return new Option<T>();
    }
    public final boolean isSome()
    {
        return _t!=null;
    }
    public final boolean isNone()
    {
        return _t==null;
    }
    public final boolean equals(final Object o)
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
    public final int hashCode()
    {
        return isNone() ? 0 : 31 * _t.hashCode();
    }

    public final static <U>Option<U> toOption(U t)
    {
        return new Option<U>(t);
    }

    public final <U>Option<U> bind(final Func<T,Option<U>> f)
    {
        if(isSome()) return f.apply(Some());
        else return Option.None();
    }

    public final static <A,B,C>Option<C>lift(final Func2<A,B,C> f, final Option<A> o1, final Option<B> o2)
    {
        if(o1.isSome() && o2.isSome()) return toOption(f.apply(o1.Some(),o2.Some()));
        else return None();
    }
}
