package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 09:32
 * To change this template use File | Settings | File Templates.
 */
public final class Tuple2<T1,T2> // deprecated in favour of org.javatuples.Tuple2
{
    final private T1 _t1;
    final private T2 _t2;

    private Tuple2(final T1 t1, final T2 t2)
    {
        _t1 = t1;
        _t2 = t2;
    }

    public final T1 getValue0() { return _t1; }
    public final T2 getValue1() { return _t2; }

    public static final <T1,T2>Tuple2<T1,T2> create(final T1 t1,final T2 t2) { return new Tuple2<T1,T2>(t1,t2); }

    public final int hashCode() { return 31 * _t1.hashCode() * _t2.hashCode(); }
    public final boolean equals(final Object o)
    {
        if(o instanceof Tuple2<?,?>)
        {
            final Tuple2<?,?> o_asTuple2 = (Tuple2<?,?>)o;
            return o_asTuple2.getValue0().equals(_t1) && o_asTuple2.getValue1().equals(_t2);
        }
        return false;
    }
}
