package com.cn21.data.cache;

public class RealApiCacheFactory {
	public static int entrySize=1;
	public static int maxSize=2000;
	
	public static Cache<RKey,RValue> createCache(String type,int maxSize){
		if(type.equals("lru"))
			return new ILruCache<RKey,RValue>(maxSize);
		return null;
	}
	
	public static class RKey{
		public String identity;
		public int interfaceId;
		private int hash;
		
		public RKey(String identity, int interfaceId) {
			this.identity = identity;
			this.interfaceId = interfaceId;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			if(this==obj) return true;
			if(obj instanceof RKey){
				RKey key=(RKey)obj;
				if(identity==key.identity&&interfaceId==key.interfaceId)
					return true;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return (identity+interfaceId).hashCode();
		}
		
	}
	
	public static class RValue{
		public int times;
		public long startTimes;
		
		public RValue(int times, long startTimes) {
			this.times = times;
			this.startTimes = startTimes;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			if(this==obj) return true;
			if(obj instanceof RValue){
				RValue key=(RValue)obj;
				if(times==key.times&&startTimes==key.startTimes)
					return true;
			}
			return false;
		}
		
	}
}
