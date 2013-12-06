package me.shaftesbury.utils.functional;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 04/11/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class UnmodifiableMap<K,V> implements Map<K,V>
{
    private final Map<K,V> _map;
    public UnmodifiableMap(final Map<K,V> map) { _map = map; }

    @Override
    public int size() {
        return _map.size();
    }

    @Override
    public boolean isEmpty() {
        return _map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return _map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return _map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return _map.get(key);
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("Cannot put (K,V) into an UnmodifiableMap");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Cannot remove K into an UnmodifiableMap");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Cannot put Map into an UnmodifiableMap");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Cannot clear an UnmodifiableMap");
    }

    @Override
    public Set<K> keySet() {
        return _map.keySet();
    }

    @Override
    public Collection<V> values() {
        return _map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return _map.entrySet();
    }
    public final boolean equals(Object o)
    {
        return o instanceof Map<?,?> &&
                _map.entrySet().containsAll(((Map)o).entrySet()) &&
                ((Map) o).entrySet().containsAll(_map.entrySet());
    }

    public final int hashCode() { return 13 * _map.hashCode(); }
}
