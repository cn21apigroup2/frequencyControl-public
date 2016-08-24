package com.cn21.data.cache;

public class ILruCache<K,V> extends Cache<K,V>{
	private LruCache<K,V> lruCache;
	
	public ILruCache(){
		this(200000000);
	}
	
	public ILruCache(int size){
		
	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V set(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(K key) {
		// TODO Auto-generated method stub
		return null;
	}

}
