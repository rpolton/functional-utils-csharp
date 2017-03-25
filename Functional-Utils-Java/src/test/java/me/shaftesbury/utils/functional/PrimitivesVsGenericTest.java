package me.shaftesbury.utils.functional;

import me.shaftesbury.utils.functional.primitive.Func_int_T;
import me.shaftesbury.utils.functional.primitive.IntList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.lang.Math.pow;

public class PrimitivesVsGenericTest
{
    private static double oneMillion = pow(10,6);
    @Test
    public void integerListVsPrimitiveListToString()
    {
        for(final int howMany : new int[]{1000,10000,100000,1000000,10000000}) {
            System.out.println("Testing stringify with "+howMany+" elements");
            final String s1, s2, s3;
            {
                final long beforeInitialisation = System.nanoTime();

                final int[] ints = new int[howMany];
                Arrays.fill(ints, 10);

                final long beforeTransformation = System.nanoTime();

                final StringBuilder builder = new StringBuilder();
                for (final int i : ints)
                    builder.append(i);

                s1 = builder.toString();

                final long afterTransformation = System.nanoTime();
                System.out.println("Primitive array initialisation time " + (beforeTransformation - beforeInitialisation) / oneMillion + " ms");
                System.out.println("Primitive array transformation took " + (afterTransformation - beforeTransformation) / oneMillion + " ms");
            }

            {
                final long beforeInitialisation = System.nanoTime();

                final IntList ints_l = me.shaftesbury.utils.functional.primitive.Functional.init(10, howMany);

                final long beforeTransformation = System.nanoTime();

                s2 = me.shaftesbury.utils.functional.primitive.Functional.join("", ints_l);

                final long afterTransformation = System.nanoTime();
                System.out.println("Primitive list initialisation time " + (beforeTransformation - beforeInitialisation) / oneMillion + " ms");
                System.out.println("Primitive list transformation took " + (afterTransformation - beforeTransformation) / oneMillion + " ms");
            }

            {
                final long beforeInitialisation = System.nanoTime();

                final List<Integer> ints = Functional.init(Functional.constant(10), howMany);

                final long beforeTransformation = System.nanoTime();

                s3 = Functional.join("", ints);

                final long afterTransformation = System.nanoTime();
                System.out.println("Integer list initialisation time " + (beforeTransformation - beforeInitialisation) / oneMillion + " ms");
                System.out.println("Integer list transformation took " + (afterTransformation - beforeTransformation) / oneMillion + " ms");
            }

            Assert.assertEquals(s1, s2);
            Assert.assertEquals(s1, s3);
            System.out.println("-----------------");
        }
    }

    @Test
    public void primitiveVsGenericFindLast()
    {
        for(final int howMany : new int[]{1000,10000,100000,1000000,10000000}) {
            System.out.println("Testing findLast with " + howMany + " elements");
            int s1=Integer.MIN_VALUE;
            final int s2, s3;
            {
                final long beforeInitialisation = System.nanoTime();

                final int[] ints = new int[howMany];
                for(int i=0;i<howMany;++i)
                    ints[i]=i*2;

                final long beforeTransformation = System.nanoTime();

                for (int i = ints.length - 1; i >= 0; --i)
                    if (ints[i] < (howMany / 2)) {
                        s1 = ints[i];
                        break;
                    }

                final long afterTransformation = System.nanoTime();
                System.out.println("Primitive array initialisation time " + (beforeTransformation - beforeInitialisation) / oneMillion + " ms");
                System.out.println("Primitive array transformation took " + (afterTransformation - beforeTransformation) / oneMillion + " ms");
            }

            {
                final long beforeInitialisation = System.nanoTime();

                final IntList ints = me.shaftesbury.utils.functional.primitive.Functional.init(me.shaftesbury.utils.functional.primitive.IntFunctionalTest.DoublingGenerator, howMany);

                final long beforeTransformation = System.nanoTime();

                s2 = me.shaftesbury.utils.functional.primitive.Functional.findLast(new Func_int_T<Boolean>() {

                    public Boolean apply(final int a) {
                        return a<howMany/2;
                    }
                }, ints);

                final long afterTransformation = System.nanoTime();
                System.out.println("Primitive list initialisation time " + (beforeTransformation - beforeInitialisation) / oneMillion + " ms");
                System.out.println("Primitive list transformation took " + (afterTransformation - beforeTransformation) / oneMillion + " ms");
            }

            {
                final long beforeInitialisation = System.nanoTime();

                final List<Integer> ints = Functional.init(FunctionalTest.DoublingGenerator, howMany);

                final long beforeTransformation = System.nanoTime();

                s3 = Functional.findLast(new Function<Integer, Boolean>() {

                    public Boolean apply(final Integer a) {
                        return a<howMany/2;
                    }
                }, ints);

                final long afterTransformation = System.nanoTime();
                System.out.println("Generic list initialisation time " + (beforeTransformation - beforeInitialisation) / oneMillion + " ms");
                System.out.println("Generic list transformation took " + (afterTransformation - beforeTransformation) / oneMillion + " ms");
            }

            Assert.assertEquals(s1, s2);
            Assert.assertEquals(s1, s3);
            System.out.println("-----------------");
        }
    }
}
