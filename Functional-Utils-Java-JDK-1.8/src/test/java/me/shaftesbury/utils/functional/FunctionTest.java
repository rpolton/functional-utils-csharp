package me.shaftesbury.utils.functional;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        final Function<Integer,String> stringify = new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return Functional.Stringify(integer);
            }
        };

        final Function<String,Iterable<Double>> expander =
                new Function<String, Iterable<Double>>() {
                    @Override
                    public List<Double> apply(String s) {
                        Double m = Double.parseDouble(s);
                        return Arrays.asList(m-1.0,m,m+1.0);
                    }
                };

        final Function<Integer,Iterable<Double>> composition = stringify.andThen(expander);

        final Iterable2<Integer> input = IterableHelper.asList(-1,0,1);
        final Iterable2<Double> expected = IterableHelper.asList(-2.0,-1.0,0.0,-1.0,0.0,1.0,0.0,1.0,2.0);

        final Iterable2<Double> output = input.collect(composition);

        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test
    public void BinaryFunctionDecompositionTest1()
    {
        final BiFunction<Double,Integer,String> f =
                new BiFunction<Double,Integer, String>() {
                    @Override
                    public String apply(Double d, Integer i) {
                        return Functional.Stringify((int)(i * d));
                    }
                };

        final Function<Integer,Function<Double,String>> decomposed = BinaryFunction.toFunc(f);
        final Function<Double,String> decomposed1 = decomposed.apply(2);
        final Iterable2<String> output = IterableHelper.asList(1.0,2.0,3.0,4.0,5.0).map(decomposed1);
        final Iterable<String> expected = Arrays.asList("2","4","6","8","10");

        AssertIterable.assertIterableEquals(expected,output);
    }

    @Test
    public void BinaryFunctionDecompositionTest2()
    {
        final BiFunction<Double,Integer,String> f =
                new BiFunction<Double,Integer, String>() {
                    @Override
                    public String apply(Double d, Integer i) {
                        return Functional.Stringify((int)(i * d));
                    }
                };

        final Function<Double,String> decomposed = BinaryFunction.toFunc(f,2);
        final Iterable2<String> output = IterableHelper.asList(1.0,2.0,3.0,4.0,5.0).map(decomposed);
        final Iterable<String> expected = Arrays.asList("2","4","6","8","10");

        AssertIterable.assertIterableEquals(expected,output);
    }
}
