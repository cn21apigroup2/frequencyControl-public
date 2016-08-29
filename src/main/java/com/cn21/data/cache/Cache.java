package com.cn21.data.cache;

import java.util.Map;

public abstract class Cache<K,V> {
	protected CacheListener listener=null;
	
	abstract public V get(K key);
	
	abstract public V set(K key,V value);
	
	abstract public V remove(K key);
	
	abstract public Map<K,V> getData();
	
	public CacheListener getListener() {
		return listener;
	}

	public void setListener(CacheListener listener) {
		this.listener = listener;
	}

	public interface CacheListener<K,V>{
		
		public void onEntryRemove(K key, V oldValue, V newValue);
		
		public V onEntryEvicted(K key, V oldValue, V newValue);
		
		public V onEntryCreate(K key);
	}
	
}
