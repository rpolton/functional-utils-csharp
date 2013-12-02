package me.shaftesbury.utils.functional;

import org.junit.Assert;
import org.junit.Test;

import static me.shaftesbury.utils.functional.LispList.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 26/11/13
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class LispListTest
{
    @Test
    public void headTest1()
    {
        List<Integer> l = list(1, LispList.<Integer>nil());
        Assert.assertEquals(new Integer(1), l.head());
    }

    @Test
    public void tailTest1()
    {
        List<Integer> l = list(1,list(2,LispList.<Integer>nil()));
        Assert.assertEquals(list(2,LispList.nil()), l.tail());
    }

    @Test
    public void mapTest1()
    {
        List<Integer> input = list(1, list(2, list(3, list(4, list(5, LispList.<Integer>nil())))));
        List<String> output = map(Functional.dStringify, input);
        Assert.assertEquals(list("1",list("2",list("3",list("4",list("5",LispList.nil()))))),output);
    }

    @Test
    public void filterTest1()
    {
        List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        List<Integer> oddElems = filter(Functional.IsOdd, input);
        Assert.assertEquals(LispList.<Integer>nil(), oddElems);
    }

    @Test
    public void filterTest2()
    {
        List<Integer> input = list(2,list(5,list(7,list(8,list(10,LispList.<Integer>nil())))));
        List<Integer> evenElems = filter(Functional.IsEven, input);

        List<Integer> expected = list(2,list(8,list(10,LispList.<Integer>nil())));
        Assert.assertEquals(expected, evenElems);
    }

    @Test
    public void filterTest3()
    {
        List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final Integer limit = 5;
        List<Integer> highElems = filter(
                new Func<Integer,Boolean>()
                {
                    @Override
                    public Boolean apply(Integer a) { return a > limit;}
                }, input);

        List<Integer> expected = list(6,list(8,list(10,LispList.<Integer>nil())));
        Assert.assertEquals(expected, highElems);
    }

    @Test
    public void filterTest4()
    {
        List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final Integer limit = 10;
        List<Integer> output = filter(
                new Func<Integer,Boolean>()
                {
                    @Override public Boolean apply(Integer a) {return a > limit;}
                }, input);

        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void filterTest5()
    {
        List<Integer> input = list(2,list(4,list(6,list(8,list(10,list(12,list(14,list(16,list(18,list(20,LispList.<Integer>nil()))))))))));
        List<Integer> expected = list(4,list(8,list(12,list(16,list(20,LispList.<Integer>nil())))));
        List<Integer> output = filter(
                new Func<Integer,Boolean>()
                {
                    @Override public Boolean apply(Integer a) {return a % 4 ==0;}
                }, input);

        Assert.assertEquals(expected, output);
    }
}
