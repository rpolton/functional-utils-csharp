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
        final List<Integer> l = list(1, LispList.<Integer>nil());
        Assert.assertEquals(new Integer(1), l.head());
    }

    @Test
    public void tailTest1()
    {
        final List<Integer> l = list(1,list(2, LispList.<Integer>nil()));
        Assert.assertEquals(new Integer(2), l.tail().head());
    }

    @Test
    public void equalsTest1()
    {
        final List<Integer> l = list(1,list(2, LispList.<Integer>nil()));
        Assert.assertEquals(list(2,LispList.nil()), l.tail());
    }

    @Test
    public void mapTest1()
    {
        final List<Integer> input = list(1, list(2, list(3, list(4, list(5, LispList.<Integer>nil())))));
        final List<String> output = map(Functional.<Integer>dStringify(), input);
        Assert.assertEquals("1",output.head());
        Assert.assertEquals("2",output.tail().head());
        Assert.assertEquals("3",output.tail().tail().head());
        Assert.assertEquals("4",output.tail().tail().tail().head());
        Assert.assertEquals("5",output.tail().tail().tail().tail().head());
    }

    @Test
    public void equalsTest2()
    {
        final List<Integer> input = list(1, list(2, list(3, list(4, list(5, LispList.<Integer>nil())))));
        final List<String> output = map(Functional.<Integer>dStringify(), input);
        Assert.assertEquals(list("1",list("2",list("3",list("4",list("5",LispList.nil()))))),output);
    }

    @Test
    public void filterTest1()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final List<Integer> oddElems = filter(Functional.isOdd, input);
        Assert.assertEquals(LispList.<Integer>nil(), oddElems);
    }

    @Test
    public void filterTest2()
    {
        final List<Integer> input = list(2,list(5,list(7,list(8,list(10,LispList.<Integer>nil())))));
        final List<Integer> evenElems = filter(Functional.isEven, input);

        Assert.assertEquals(new Integer(2), evenElems.head());
        Assert.assertEquals(new Integer(8), evenElems.tail().head());
        Assert.assertEquals(new Integer(10), evenElems.tail().tail().head());
    }

    @Test
    public void equalsTest3()
    {
        final List<Integer> input = list(2,list(5,list(7,list(8,list(10,LispList.<Integer>nil())))));
        final List<Integer> evenElems = filter(Functional.isEven, input);

        final List<Integer> expected = list(2,list(8,list(10,LispList.<Integer>nil())));
        Assert.assertEquals(expected, evenElems);
    }

    @Test
    public void filterTest3()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final Integer limit = 5;
        final List<Integer> highElems = filter(
                new Func<Integer,Boolean>()
                {
                    @Override
                    public Boolean apply(Integer a) { return a > limit;}
                }, input);

        final List<Integer> expected = list(6,list(8,list(10,LispList.<Integer>nil())));
        Assert.assertEquals(expected, highElems);
    }

    @Test
    public void filterTest4()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final Integer limit = 10;
        final List<Integer> output = filter(
                new Func<Integer,Boolean>()
                {
                    @Override public Boolean apply(Integer a) {return a > limit;}
                }, input);

        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void filterTest5()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,list(12, list(14, list(16, list(18, list(20, LispList.<Integer>nil()))))))))));
        final List<Integer> expected = list(4,list(8,list(12,list(16,list(20,LispList.<Integer>nil())))));
        final List<Integer> output = filter(
                new Func<Integer,Boolean>()
                {
                    @Override public Boolean apply(Integer a) {return a % 4 ==0;}
                }, input);

        Assert.assertEquals(expected, output);
    }

    @Test
    public void consTest1()
    {
        final List<Integer> input = cons(2, cons(4, cons(6, compose(8, 10))));
        Assert.assertEquals(new Integer(2), input.head());
        Assert.assertEquals(new Integer(4), input.tail().head());
        Assert.assertEquals(new Integer(6), input.tail().tail().head());
        Assert.assertEquals(new Integer(8), input.tail().tail().tail().head());
        Assert.assertEquals(new Integer(10), input.tail().tail().tail().tail().head());
    }

    @Test
    public void carTest1()
    {
        final List<Integer> input = list(2, list(4, list(6, list(8, list(10, LispList.<Integer>nil())))));
        Assert.assertEquals(new Integer(2), car(input));
    }

    @Test
    public void cdrTest1()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        Assert.assertEquals(list(4,list(6,list(8,list(10,LispList.<Integer>nil())))),cdr(input));
    }

    @Test
    public void cadrTest1()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        Assert.assertEquals(new Integer(4),cadr(input));
    }

    @Test
    public void reverseTest1()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final List<Integer> expected = list(10,list(8,list(6,compose(4,2))));
        final List<Integer> output = reverse(input);
        Assert.assertEquals(expected,output);
    }

    @Test
    public void foldTest1()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,list(10,LispList.<Integer>nil())))));
        final Integer expected = 30;
        final Integer output = fold(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer state, Integer o) {
                return state+o;
            }
        },new Integer(0),input);
        Assert.assertEquals(expected,output);
    }

    @Test
    public void foldTest2()
    {
        final List<String> input = list("2",list("4",list("6",list("8",LispList.<String>nil()))));
        final String expected = "2468";
        final String output = fold(new Func2<String, String, String>() {
            @Override
            public String apply(String state, String o) {
                return state+o;
            }
        },"",input);
        Assert.assertEquals(expected,output);
    }

    @Test
    public void foldRightTest1()
    {
        final List<Integer> input = list(2,list(4,list(6,list(8,LispList.<Integer>nil()))));
        final String expected = "8642";
        final String output = foldRight(new Func2<Integer, String, String>() {
            @Override
            public String apply(Integer o, String state) {
                return state + o;
            }
        }, "", input);
        Assert.assertEquals(expected,output);
    }

    @Test(expected = EmptyListHasNoHead.class)
    public void EmptyListHasNoHeadTest1()
    {
        LispList.<Integer>nil().head();
    }

    @Test(expected = EmptyListHasNoTail.class)
    public void EmptyListHasNoTailTest1()
    {
        LispList.<Integer>nil().tail();
    }
}
