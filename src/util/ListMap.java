package util;

import java.io.*;
import java.util.*;

import org.apache.commons.collections.*;
import org.apache.commons.collections.map.*;


public class ListMap<K, V>
		implements Map<K, V>, Serializable {
	
	private ListOrderedMap listmap = new ListOrderedMap();

	public ListMap() {}
	
	public ListMap(Map map) {
		listmap.putAll(map);
	}
	
	
	
	@Override
	public int size() {
		return listmap.size();
	}

	@Override
	public boolean isEmpty() {
		return listmap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return listmap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return listmap.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return (V) listmap.get(key);
	}

	@Override
	public V put(K key, V value) {
		return (V) listmap.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return (V) listmap.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		listmap.putAll(m);
		
	}

	
	public void putAll(int index, Map<? extends K, ? extends V> m) {
		if (index > listmap.size()) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + listmap.size());
		}
		int i = index;
      for (Map.Entry entry : m.entrySet()) {
         put(i, (K) entry.getKey(), (V) entry.getValue());
         i++;
     }
	}
	
	@Override
	public void clear() {
		listmap.clear();
		
	}

	@Override
	public Set<K> keySet() {
		return listmap.keySet();
	}

	@Override
	public Collection<V> values() {
		return listmap.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
			return listmap.entrySet();
	}

	/**
	 * @param object
	 * @return
	 * @see org.apache.commons.collections.map.AbstractMapDecorator#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		return this.listmap.equals(object);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.AbstractMapDecorator#hashCode()
	 */
	public int hashCode() {
		return this.listmap.hashCode();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#mapIterator()
	 */
	public MapIterator mapIterator() {
		return this.listmap.mapIterator();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#orderedMapIterator()
	 */
	public OrderedMapIterator orderedMapIterator() {
		return this.listmap.orderedMapIterator();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#firstKey()
	 */
	public K firstKey() {
		return (K) this.listmap.firstKey();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#lastKey()
	 */
	public K lastKey() {
		return (K) this.listmap.lastKey();
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#nextKey(java.lang.Object)
	 */
	public K nextKey(Object key) {
		return (K) this.listmap.nextKey(key);
	}

	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#previousKey(java.lang.Object)
	 */
	public K previousKey(Object key) {
		return (K) this.listmap.previousKey(key);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#keyList()
	 */
	public List<K> keyList() {
		return this.listmap.keyList();
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#valueList()
	 */
	public List<V> valueList() {
		return this.listmap.valueList();
	}
	
	public Set<V> valueSet() {
		return Collections.unmodifiableSet(new HashSet(values()));
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#toString()
	 */
	public String toString() {
		return this.listmap.toString();
	}

	/**
	 * @param index
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#get(int)
	 */
	public K getKey(int index) {
		return (K) this.listmap.get(index);
	}

	/**
	 * @param index
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#getValue(int)
	 */
	public V getValue(int index) {
		return (V) this.listmap.getValue(index);
	}

	public V get(int index) {
		return (V) this.listmap.getValue(index);
	}

	
	/**
	 * @param key
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#indexOf(java.lang.Object)
	 */
	public int indexOf(K key) {
		return this.listmap.indexOf(key);
	}

	public int indexOfValue(V value) {
		return this.listmap.valueList().indexOf(value);
	}
	
	/**
	 * @param index
	 * @param value
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#setValue(int, java.lang.Object)
	 */
	public V setValue(int index, V value) {
		return (V) this.listmap.setValue(index, value);
	}

	/**
	 * @param index
	 * @param key
	 * @param value
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#put(int, java.lang.Object, java.lang.Object)
	 */
	public V put(int index, K key, V value) {
		return (V) this.listmap.put(index, key, value);
	}

	/**
	 * @param index
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#remove(int)
	 */
	public V remove(int index) {
		return (V) this.listmap.remove(index);
	}

	/**
	 * @return
	 * @see org.apache.commons.collections.map.ListOrderedMap#asList()
	 */
	@Deprecated
	public List<K> asList() {
		return this.listmap.asList();
	}
}
