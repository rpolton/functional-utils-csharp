package me.shaftesbury.utils.functional;

/**
 * The Func interface is designed to represent lambda / anonymous functions which are defined and used in situ. This models
 * a function that takes one argument.
 * See <a href="http://en.wikipedia.org/wiki/Anonymous_function">Lambda function</a>
 * @param <A> the type of the input argument
 * @param <R> the type of the return value
 * @see <a href="http://en.wikipedia.org/wiki/Closure_(computer_programming)">Closure</a>
 */
public interface Func<A, R> {
    /**
     * Call <tt>apply</tt> to evaluate the function object
     * @param a the input value
     * @return an element of type R
     */
    R apply(A a);
}
