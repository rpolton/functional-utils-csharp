package me.shaftesbury.utils.functional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 02/12/13
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
 */
public class UmodifiableListTest
{
    private List<Integer> l;
    private UnmodifiableList<Integer> u;

    @Before
    public void setup()
    {
        l = Arrays.asList(1,2,3);
        u = new UnmodifiableList<Integer>(l);
    }

    @Test
    public void UnmodifiableListSizeTest1()
    {
        Assert.assertEquals(l.size(),u.size());
    }

    @Test
    public void UnmodifiableListIsEmptyTest1()
    {
        Assert.assertEquals(l.isEmpty(),u.isEmpty());
        Assert.assertFalse(u.isEmpty());
    }

    @Test
    public void UnmodifiableListIsEmptyTest2()
    {
        Assert.assertTrue(new UnmodifiableList(Arrays.asList(new Integer[]{})).isEmpty());
    }

    @Test
    public void UnmodifiableListContainsTest1()
    {
        Assert.assertEquals(l.contains(2),u.contains(2));
        Assert.assertTrue(l.contains(2));
    }

    @Test
    public void UnmodifiableListContainsTest2()
    {
        Assert.assertEquals(l.contains(0),u.contains(0));
        Assert.assertFalse(l.contains(0));
    }

    @Test
    public void UnmodifiableListIteratorTest1()
    {
        List<Integer> ll = new ArrayList<Integer>();
        for(Integer i:u) ll.add(i);
        Assert.assertArrayEquals(l.toArray(),ll.toArray());
        AssertIterable.assertIterableEquals(l,ll);
    }

    @Test
    public void UnmodifiableListToArrayTest1()
    {
        Object[] ii = u.toArray();
        Assert.assertArrayEquals(l.toArray(),ii);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListAddTest1()
    {
        u.add(4);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListRemoveTest1()
    {
        u.remove((Integer) 1);
    }

    @Test
    public void UnmodifiableListContainsAllTest1()
    {
        Assert.assertTrue(u.containsAll(l));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListAddAllTest1()
    {
        u.addAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListRemoveAllTest1()
    {
        u.removeAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListRetainAllTest1()
    {
        u.retainAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListClearTest1()
    {
        u.clear();
    }

    @Test
    public void UnmodifiableListToArrayTest2()
    {
        Integer[] ii = u.toArray(new Integer[]{});
        Assert.assertArrayEquals(l.toArray(new Integer[]{}),ii);
    }

    @Test
    public void UnmodifiableListEqualsTest1()
    {
        Assert.assertTrue(u.equals(l));
        Assert.assertTrue(l.equals(u));
    }

    @Test
    public void UnmodifiableCollectionEqualsTest2()
    {
        List<Integer> d = Arrays.asList(1,2,3,4,5,6);
        UnmodifiableList<Integer> dummy = new UnmodifiableList<Integer>(d);
        Assert.assertFalse(u.equals(dummy));
        Assert.assertFalse(dummy.equals(u));
    }

    @Test
    public void UnmodifiableListGetTest1()
    {
        Assert.assertEquals((Integer)1,u.get(0));
        Assert.assertEquals((Integer)2,u.get(1));
        Assert.assertEquals((Integer)3,u.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListSetTest1()
    {
        u.set(0, (Integer) 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListAddTest2()
    {
        u.add(0, (Integer) 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableListRemoveTest2()
    {
        u.remove(0);
    }

    @Test
    public void UnmodifiableListIndexOfTest1()
    {
        Assert.assertEquals(0,u.indexOf(1));
        Assert.assertEquals(1,u.indexOf(2));
        Assert.assertEquals(2,u.indexOf(3));
    }

    @Test
    public void UnmodifiableListLastIndexOfTest1()
    {
        Assert.assertEquals(0,u.lastIndexOf(1));
        Assert.assertEquals(1,u.lastIndexOf(2));
        Assert.assertEquals(2,u.lastIndexOf(3));
    }

    @Test
    public void UnmodifiableListListIteratorTest1()
    {
        List<Integer> ll = new ArrayList<Integer>();
        ListIterator li = u.listIterator();
        while(li.hasNext()) ll.add((Integer)li.next());
        Assert.assertArrayEquals(l.toArray(), ll.toArray());
    }

    @Test
    public void UnmodifiableListListIteratorTest2()
    {
        List<Integer> ll = new ArrayList<Integer>();
        ListIterator<Integer> li = u.listIterator(1);
        Assert.assertTrue(li.hasNext());
        Assert.assertEquals(l.listIterator(1).next(),li.next());
    }
}
