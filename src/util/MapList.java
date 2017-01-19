package util;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

/**
 * Deprecated. Use {@link util.ListMap} instead.
 * @author Darko
 *
 * @param <K>
 * @param <V>
 */
@Deprecated
public class MapList<K, V> implements Serializable, Iterable<V> {
//	public Map<K, V> map = new LinkedHashMap<K, V>();
	private List<V> list = new LinkedList<V>();

	private BiMap<Integer, K> indexKey = HashBiMap.create();

//	private Map<K, Integer> keyToIndex = new LinkedHashMap<K, Integer>();
//	private List<K> indexToKey = new LinkedList<K>();

	public void put(int index, K key, V value) {
		if (index > this.list.size()) {
			throw new IndexOutOfBoundsException("MapList has " + list.size() + "elements, index is "  + index);
		}
		if (indexKey.inverse().containsKey(key)) {
			throw new IllegalArgumentException("the key " + key + "is already bound");
		}
		for (int i = list.size() - 1; i >= index; i--) {
			K iKey = indexKey.get(i);
			indexKey.forcePut(i+1, iKey);
		}
		indexKey.put(index, key);
		
//		keyToIndex.put(key, index);
//		indexToKey.add(index, key);

		this.list.add(index, value);
//		return this.map.put(key, value);
	}

	public void put(K key, V value) {
		Integer index = this.indexKey.inverse().get(key);
		if (index == null) {
			index = this.list.size();
		}
		this.put(index, key, value);
	}
	
	public void put(int index, V value) {
		if (index >= this.list.size()) {
			throw new IndexOutOfBoundsException("MapList has " + list.size() + "elements, index is "  + index);
		}
		this.list.set(index, value);
	}
	
	public void remove(int index) {
		if (index >= list.size()) {
			throw new IndexOutOfBoundsException("MapList has " + list.size() + "elements, index is "  + index);
		}
		
		indexKey.remove(index);
		for (int i = index + 1; i < list.size(); i++) {
			K iKey = indexKey.get(i);
			indexKey.forcePut(i - 1, iKey);
		}
		
		this.list.remove(index);
	}
	
	public void remove(K key) {
		Integer index = indexKey.inverse().get(key);
		if (index == null) {
			throw new IllegalArgumentException("cannot remove key " + key + " - doesn't exists");
		}
		
		this.remove(index);
	}

	public void putAll(int index, MapList<? extends K, ? extends V> ml) {
		if (index > this.list.size()) {
			throw new IndexOutOfBoundsException("MapList has " + list.size() + "elements, index is "  + index);
		}
		
		for (int i = ml.list.size() - 1; i >=0; i--) {
			this.put(index, ml.find(i), ml.list.get(i));
		}
	}
	
	public void putAll(MapList<? extends K, ? extends V> ml) {
		this.putAll(0, ml);
	}

	public V get(int index) {
		return this.list.get(index);
	}

	public V get(Object key) {
		return this.list.get(indexKey.inverse().get(key));
	}

	public K find(int index) {
		return indexKey.get(index);
	}

	public int find(K key) {
		return indexKey.inverse().get(key);
	}

	public int findIndex(V value) {
		return list.indexOf(value);
	}

	public K findKey(V value) {
		int index = list.indexOf(value);
		return indexKey.get(index);
	}

	public int size() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean containsKey(K key) {
		return indexKey.inverse().containsKey(key);
	}

	/**
	 * The elements of the MapList are the same and in the same order
	 */
	public boolean equals(Object ml) {
		return this.list.equals(((MapList) ml).list);
	}

	/**
	 * The elements of the MapList are the same. It assumes that the IDs of the elements are used as keys
	 * 
	 * @param ml
	 * @return
	 */
	public boolean equalsSet(Object ml) {
		return new HashSet(this.list).equals(new HashSet(((MapList)ml).list));
	}
	
	public Iterator<V> iterator() {
		return this.list.iterator();
	}
	
	public Collection<K> keys() {
		return Collections.unmodifiableSet(this.indexKey.inverse().keySet());
	}
	
	public Collection<V> values() {
		return Collections.unmodifiableList(this.list);
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("[");
		if (!list.isEmpty()) {
			buffer.append(indexKey.get(0));
			buffer.append("=");
			buffer.append(list.get(0));

			for (int i = 1; i < list.size(); i++) {
				buffer.append(", ");
				buffer.append(indexKey.get(i));
				buffer.append("=");
				buffer.append(list.get(i));
			}
		}
		buffer.append("]");

		return buffer.toString();
	}
}
