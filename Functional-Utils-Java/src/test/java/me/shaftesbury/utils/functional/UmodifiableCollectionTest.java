package me.shaftesbury.utils.functional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 02/12/13
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
 */
public class UmodifiableCollectionTest
{
    private Collection<Integer> l;
    private UnmodifiableCollection<Integer> u;

    @Before
    public void setup()
    {
        l = Arrays.asList(1,2,3);
        u = new UnmodifiableCollection<Integer>(l);
    }

    @Test
    public void UnmodifiableCollectionSizeTest1()
    {
        Assert.assertEquals(l.size(), u.size());
    }

    @Test
    public void UnmodifiableCollectionIsEmptyTest1()
    {
        Assert.assertEquals(l.isEmpty(),u.isEmpty());
        Assert.assertFalse(u.isEmpty());
    }

    @Test
    public void UnmodifiableCollectionIsEmptyTest2()
    {
        Assert.assertTrue(new UnmodifiableCollection(Arrays.asList(new Integer[]{})).isEmpty());
    }

    @Test
    public void UnmodifiableCollectionContainsTest1()
    {
        Assert.assertEquals(l.contains(2),u.contains(2));
        Assert.assertTrue(l.contains(2));
    }

    @Test
    public void UnmodifiableCollectionContainsTest2()
    {
        Assert.assertEquals(l.contains(0),u.contains(0));
        Assert.assertFalse(l.contains(0));
    }

    @Test
    public void UnmodifiableCollectionIteratorTest1()
    {
        List<Integer> ll = new ArrayList<Integer>();
        for(Integer i:u) ll.add(i);
        Assert.assertArrayEquals(l.toArray(), ll.toArray());
        AssertIterable.assertIterableEquals(l,ll);
    }

    @Test
    public void UnmodifiableCollectionToArrayTest1()
    {
        Object[] ii = u.toArray();
        Assert.assertArrayEquals(l.toArray(), ii);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableCollectionAddTest1()
    {
        u.add(4);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableCollectionRemoveTest1()
    {
        u.remove((Integer) 1);
    }

    @Test
    public void UnmodifiableCollectionContainsAllTest1()
    {
        Assert.assertTrue(u.containsAll(l));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableCollectionAddAllTest1()
    {
        u.addAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableCollectionRemoveAllTest1()
    {
        u.removeAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableCollectionRetainAllTest1()
    {
        u.retainAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableCollectionClearTest1()
    {
        u.clear();
    }

    @Test
    public void UnmodifiableCollectionToArrayTest2()
    {
        Integer[] ii = u.toArray(new Integer[]{});
        Assert.assertArrayEquals(l.toArray(new Integer[]{}), ii);
    }

    @Test
    public void UnmodifiableCollectionEqualsTest1()
    {
        Assert.assertTrue(u.equals(l));
        Assert.assertTrue(new UnmodifiableCollection(l).equals(u));
    }

    @Test
    public void UnmodifiableCollectionEqualsTest2()
    {
        Collection<Integer> d = Arrays.asList(1,2,3,4,5,6);
        UnmodifiableCollection<Integer> dummy = new UnmodifiableCollection<Integer>(d);
        Assert.assertFalse(u.equals(dummy));
        Assert.assertFalse(dummy.equals(u));
    }
}
