package me.shaftesbury.utils;

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
    public Option()
    {
        _t=null;
    }
    public Option(final T t)
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
    public final boolean equals(final Option<T> other)
    {
        try
        {
            return isSome() == other.isSome() && Some()==other.Some();
        }
        catch(OptionNoValueAccessException o)
        {
            return true; // every None is considered to be the same
        }
    }
    public final int hashCode()
    {
        return isNone() ? 0 : 31 * _t.hashCode();
    }

    public final static <T>Option<T> toOption(T t)
    {
        return new Option<T>(t);
    }
}
