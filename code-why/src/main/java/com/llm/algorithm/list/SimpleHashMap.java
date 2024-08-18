package com.llm.algorithm.list;

import java.util.ArrayList;
import java.util.List;

public class SimpleHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int capacity;
    private float loadFactor;
    private int size;
    private List<List<Entry>> table;

    private class Entry {
        final K key;
        V val;
        Entry next;

        public Entry(K key, V val, Entry next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public SimpleHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(new ArrayList<>());
        }
        this.size = 0;
    }

    public int hash(K k){
        return k.hashCode() & (capacity - 1);
    }

    public V put(K key,V val){
        if (size > capacity * loadFactor){
            resize();
        }
        int index = hash(key);
        List<Entry> bucket = table.get(index);
        for (Entry entry : bucket) {
            if (entry.key.equals(key)){
                V oldValue = entry.val;
                entry.val = val;
                return oldValue;
            }
        }
        Entry entry = new Entry(key, val, bucket.isEmpty() ? null : bucket.get(0));
        bucket.add(0, entry);
        size++;
        return null;
    }

    public V get(K k){
        int index = hash(k);
        List<Entry> entries = table.get(index);
        for (Entry entry : entries){
            if (entry.key.equals(k)){
                return entry.val;
            }
        }
        return null;
    }

    public void resize(){

    }
}
