package me.shaftesbury.utils.functional;

import org.junit.Assert;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 31/10/13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public final class AssertIterable
{
    private AssertIterable(){}

    public static final <T,T1 extends T,T2 extends T>void assertIterableEquals(Iterable<T1> it1, Iterable<T2> it2)
    {
        if(it1==null||it2==null) throw new AssertionError("Unexpected null iterable passed to Assert");
        final Iterator<T1> iterator1 = it1.iterator();
        final Iterator<T2> iterator2 = it2.iterator();
        assert iterator1!=null;
        assert iterator2!=null;

        while(iterator1.hasNext() && iterator2.hasNext()) Assert.assertEquals(iterator1.next(), iterator2.next());

        if(iterator1.hasNext()||iterator2.hasNext()) Assert.fail("The two iterables containers have differing numbers of elements");
    }
}
