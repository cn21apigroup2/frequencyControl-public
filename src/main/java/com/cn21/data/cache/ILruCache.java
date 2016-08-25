package com.cn21.data.cache;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

public class ILruCache<K,V> extends Cache<K,V>{
	private LruCache<K,V> lruCache;
	private int entrySize=1;
	
	public ILruCache(int maxSize){
		this(maxSize,1);
	}
	
	public ILruCache(int maxSize,int entrySize){
		final int size=entrySize;
		lruCache=new LruCache<K, V>(maxSize){
			
			@Override
			protected int sizeOf(K key, V value) {
				// TODO Auto-generated method stub
				return size;
			}
			
			@Override
			protected V create(K key) {
				// TODO Auto-generated method stub
				if(listener!=null) 
					return (V) listener.onEntryCreate(key);
				return super.create(key);
			}
			
			@Override
			protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
				// TODO Auto-generated method stub
				if(listener!=null){
					if(evicted)
						listener.onEntryEvicted(key, oldValue, newValue);
					else  listener.onEntryRemove(key, oldValue, newValue);
				}
				else  super.entryRemoved(evicted, key, oldValue, newValue);
			}
			
		};
	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return lruCache.get(key);
	}

	@Override
	public V set(K key, V value) {
		// TODO Auto-generated method stub
		return lruCache.put(key, value);
	}

	@Override
	public V remove(K key) {
		// TODO Auto-generated method stub
		return lruCache.remove(key);
	}

}
