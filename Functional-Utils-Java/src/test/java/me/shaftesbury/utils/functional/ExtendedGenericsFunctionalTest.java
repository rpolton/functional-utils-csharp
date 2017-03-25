package me.shaftesbury.utils.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Bob on 13/02/14.
 */
public final class ExtendedGenericsFunctionalTest
{
    @Test
    public final void chooseTestParseStringToNumber()
    {
        final Function<String, Option<Number>> toNumber = new Function<String, Option<Number>>() {

            public Option<Number> apply(String s) {
                try {
                    if(s.contains("."))// then it's a float
                        return Option.<Number>toOption(Double.parseDouble(s));
                    else return Option.<Number>toOption(Integer.parseInt(s));
                } catch(final NumberFormatException t) {
                    return Option.None();
                }
            }
        };

        final List<Number> expected = Arrays.<Number>asList(new Integer(1), new Integer(2), new Double(4.5));
        final List<Number> output = Functional.choose(toNumber, Arrays.asList("1","2","at","4.5"));

        Assert.assertArrayEquals(expected.toArray(),output.toArray());
    }

    @Test
    public final void chooseTestParseObjectToNumber()
    {
        final Function<Object, Option<Number>> toNumber = new Function<Object, Option<Number>>() {

            public Option<Number> apply(Object o) {
                final String s = o.toString();
                try {
                    if(s.contains("."))// then it's a float
                        return Option.<Number>toOption(Double.parseDouble(s));
                    else return Option.<Number>toOption(Integer.parseInt(s));
                } catch(final NumberFormatException t) {
                    return Option.None();
                }
            }
        };

        final List<Number> expected = Arrays.<Number>asList(new Integer(1), new Integer(2), new Double(4.5), new Double(4.5), new Integer(1), new Integer(10));
        final List<Number> output = Functional.choose(toNumber, Arrays.<Object>asList("1","2","at","4.5",4.5,1,new Integer(10)));

        Assert.assertArrayEquals(expected.toArray(),output.toArray());
    }
}
