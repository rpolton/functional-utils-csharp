package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 09:32
 * To change this template use File | Settings | File Templates.
 */
public final class Pair<T1,T2>
{
    final private T1 _t1;
    final private T2 _t2;

    private Pair(T1 t1, T2 t2)
    {
        _t1 = t1;
        _t2 = t2;
    }

    public final T1 getValue0() { return _t1; }
    public final T2 getValue1() { return _t2; }

    public static final <T1,T2>Pair<T1,T2> create(T1 t1,T2 t2) { return new Pair<T1,T2>(t1,t2); }

    public final int hashCode() { return 31 * _t1.hashCode() * _t2.hashCode(); }
    public final boolean equals(Object o)
    {
        if(o instanceof Pair<?,?>)
        {
            final Pair<?,?> o_asPair = (Pair<?,?>)o;
            return o_asPair.getValue0().equals(_t1) && o_asPair.getValue1().equals(_t2);
        }
        return false;
    }
}
