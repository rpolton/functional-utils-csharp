package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 04/12/13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public final class Case<A, B>
{
    private final Func<A,Boolean> check;
    private final Func<A, B> result;
    public Case(final Func<A,Boolean> chk,final Func<A, B> res) {this.check = chk; this.result=res;}
    public final Boolean predicate(final A a) { return check.apply(a); }
    public final B results(final A a) { return result.apply(a); }
}
