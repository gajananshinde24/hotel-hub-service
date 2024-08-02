package com.example.demo.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public EntityCache(int capacity) {
       
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }
	
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

   
}
