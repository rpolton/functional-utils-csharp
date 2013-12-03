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
public class UmodifiableMapTest
{
    private Map<Integer,String> l;
    private UnmodifiableMap<Integer,String> u;

    @Before
    public void setup()
    {
        l = new HashMap<Integer, String>();
        l.put(1,"1"); l.put(2,"2"); l.put(3,"3");
        u = new UnmodifiableMap<Integer,String>(l);
    }

    @Test
    public void UnmodifiableMapSizeTest1()
    {
        Assert.assertEquals(l.size(),u.size());
    }

    @Test
    public void UnmodifiableMapIsEmptyTest1()
    {
        Assert.assertEquals(l.isEmpty(),u.isEmpty());
        Assert.assertFalse(u.isEmpty());
    }

    @Test
    public void UnmodifiableMapIsEmptyTest2()
    {
        Assert.assertTrue(new UnmodifiableMap(new HashMap<Integer,String>()).isEmpty());
    }

    @Test
    public void UnmodifiableMapContainsKeyTest1()
    {
        Assert.assertEquals(l.containsKey(2),u.containsKey(2));
        Assert.assertTrue(l.containsKey(2));
    }

    @Test
    public void UnmodifiableMapContainsKeyTest2()
    {
        Assert.assertEquals(l.containsKey(0),u.containsKey(0));
        Assert.assertFalse(l.containsKey(0));
    }

    @Test
    public void UnmodifiableMapContainsValueTest1()
    {
        Assert.assertEquals(l.containsValue("2"),u.containsValue("2"));
        Assert.assertTrue(l.containsValue("2"));
    }

    @Test
    public void UnmodifiableMapContainsValueTest2()
    {
        Assert.assertEquals(l.containsValue("0"),u.containsValue("0"));
        Assert.assertFalse(l.containsValue("0"));
    }

    @Test
    public void UnmodifiableMapGetTest1()
    {
        Assert.assertEquals("1",u.get(1));
        Assert.assertEquals("2",u.get(2));
        Assert.assertEquals("3", u.get(3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableMapPutTest1()
    {
        u.put(0, "0");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableMapRemoveTest1()
    {
        u.remove((Integer) 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableMapPutAllTest1()
    {
        u.putAll(l);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void UnmodifiableMapClearTest1()
    {
        u.clear();
    }

    @Test
    public void UnmodifiableMapKeySetTest1()
    {
        Set<Integer> keySet = u.keySet();
        Assert.assertArrayEquals(l.keySet().toArray(),keySet.toArray());
        AssertIterable.assertIterableEquals(l.keySet(),keySet);
    }

    @Test
    public void UnmodifiableMapValuesTest1()
    {
        Collection<String> values = u.values();
        Assert.assertArrayEquals(l.values().toArray(),values.toArray());
        AssertIterable.assertIterableEquals(l.values(),values);
    }

    @Test
    public void UnmodifiableMapEntrySetTest1()
    {
        Set<Map.Entry<Integer,String>> entrySet = u.entrySet();
        Assert.assertArrayEquals(l.entrySet().toArray(),entrySet.toArray());
        AssertIterable.assertIterableEquals(l.entrySet(),entrySet);
    }

    @Test
    public void UnmodifiableCollectionEqualsTest1()
    {
        Assert.assertTrue(u.equals(l));
        Assert.assertTrue(new UnmodifiableMap(l).equals(u));
    }

    @Test
    public void UnmodifiableCollectionEqualsTest2()
    {
        Map<Integer,String> dummy = new HashMap<Integer, String>();
        dummy.put(1,"2");
        UnmodifiableMap<Integer,String> d = new UnmodifiableMap<Integer, String>(dummy);
        Assert.assertFalse(u.equals(d));
        Assert.assertFalse(d.equals(u));
    }
}
