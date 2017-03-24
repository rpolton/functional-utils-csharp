package me.shaftesbury.utils.functional;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 02/12/13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class FunctionTest
{
    @Test
    public void UnaryFunctionCompositionTest1()
    {
        final UnaryFunction<Integer,String> stringify = new UnaryFunction<Integer, String>() {

            public String apply(Integer integer) {
                return Functional.Stringify(integer);
            }
        };

        final UnaryFunction<String,Iterable<Double>> expander =
                new UnaryFunction<String, Iterable<Double>>() {

                    public List<Double> apply(String s) {
                        Double m = Double.parseDouble(s);
                        return Arrays.asList(m-1.0,m,m+1.0);
                    }
                };

        final UnaryFunction<Integer,Iterable<Double>> composition = stringify.then(expander);

        final Iterable2<Integer> input = IterableHelper.asList(-1,0,1);
        final Iterable2<Double> expected = IterableHelper.asList(-2.0,-1.0,0.0,-1.0,0.0,1.0,0.0,1.0,2.0);

        final Iterable2<Double> output = input.collect(composition);

        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test
    public void BinaryFunctionDecompositionTest1()
    {
        final BinaryFunction<Double,Integer,String> f =
                new BinaryFunction<Double,Integer, String>() {

                    public String apply(Double d, Integer i) {
                        return Functional.Stringify((int)(i * d));
                    }
                };

        final Func<Integer,Func<Double,String>> decomposed = f.toFunc();
        final Func<Double,String> decomposed1 = decomposed.apply(2);
        final Iterable2<String> output = IterableHelper.asList(1.0,2.0,3.0,4.0,5.0).map(decomposed1);
        final Iterable<String> expected = Arrays.asList("2","4","6","8","10");

        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test
    public void BinaryFunctionDecompositionTest2()
    {
        final BinaryFunction<Double,Integer,String> f =
                new BinaryFunction<Double,Integer, String>() {

                    public String apply(Double d, Integer i) {
                        return Functional.Stringify((int)(i * d));
                    }
                };

        final Func<Double,String> decomposed = f.toFunc(2);
        final Iterable2<String> output = IterableHelper.asList(1.0,2.0,3.0,4.0,5.0).map(decomposed);
        final Iterable<String> expected = Arrays.asList("2","4","6","8","10");

        AssertIterable.assertIterableEquals(expected,output);
    }
}
