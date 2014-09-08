package me.shaftesbury.utils.functional;

/**
 * The Func2 interface is designed to represent lambda / anonymous functions which are defined and used in situ. This models
 * a function that takes two arguments.
 * See <a href="http://en.wikipedia.org/wiki/Anonymous_function">Lambda function</a>
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the return value
 * @see <a href="http://en.wikipedia.org/wiki/Closure_(computer_programming)">Closure</a>
 */
public interface Func2<A, B, C> {
    /**
     * Call <tt>apply</tt> to evaluate the function object
     * @param a the first input value
     * @param b the second input value
     * @return an element of type C
     */
    C apply(A a, B b);
}
