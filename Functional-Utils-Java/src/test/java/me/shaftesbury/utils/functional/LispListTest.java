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
        Assert.assertEquals(list(2,LispList.<Integer>nil()), l.tail());
    }
}
