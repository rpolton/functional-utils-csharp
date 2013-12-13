package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 14/11/13
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public interface Func2<A, B, C> {
    C apply(A a, B b);
}
