package me.shaftesbury.utils.functional.primitive;

/**
 * The Func interface is designed to represent lambda / anonymous functions which are defined and used in situ. This models
 * a function that takes one argument and returns a primitive int.
 * See <a href="http://en.wikipedia.org/wiki/Anonymous_function">Lambda function</a>
 * @see <a href="http://en.wikipedia.org/wiki/Closure_(computer_programming)">Closure</a>
 */
public interface Func_int_Option_int {
    /**
     * Call <tt>apply</tt> to evaluate the function object
     * @param a the input value
     * @return an integer (int)
     */
    Option_int apply(int a);
}
