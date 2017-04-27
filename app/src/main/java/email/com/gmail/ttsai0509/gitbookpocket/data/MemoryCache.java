package email.com.gmail.ttsai0509.gitbookpocket.data;

import java.util.HashMap;
import java.util.Map;

public class MemoryCache<K, V> implements DataLayer<K, V> {

    static class Entry<K, V> {
        Entry<K, V> next, prev;
        K key;
        V val;

        void addAfter(Entry<K, V> entry) {
            this.prev = entry;
            this.next = entry.next;
            this.prev.next = this;
            this.next.prev = this;
        }

        void remove() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
        }

    }

    private final int capacity;
    private final Map<K, Entry<K, V>> data;
    private final Entry<K, V> head;
    private final Entry<K, V> tail;
    private final DataLayer<K, V> nextDataLayer;

    public MemoryCache(int capacity, DataLayer<K, V> nextDataLayer) {
        this.capacity = capacity;
        this.data = new HashMap<>();
        this.head = new Entry<>();
        this.tail = new Entry<>();
        this.head.next = this.tail;
        this.tail.prev = this.head;
        this.nextDataLayer = nextDataLayer;
    }

    @Override
    public void get(K key, final DataListener<K, V> callback) {
        V val = find(key);

        if (val != null) {
            callback.onDataGet(key, val);
            return;
        }

        nextDataLayer.get(key, new DataListener<K, V>() {
            @Override
            public void onDataGet(K key, V val) {
                store(key, val);
                callback.onDataGet(key, val);
            }

            @Override
            public void onDataFail(Throwable error) {
                callback.onDataFail(error);
            }
        });
    }

    private synchronized V find(K key) {
        V result = null;
        Entry<K, V> entry = data.get(key);
        if (entry != null) {
            entry.remove();
            entry.addAfter(head);
            result = entry.val;
        }
        return result;
    }

    private synchronized void store(K key, V val) {
        Entry<K, V> entry = data.get(key);
        if (entry == null) {
            entry = new Entry<>();
            entry.key = key;
            entry.val = val;
            data.put(key, entry);
        } else {
            entry.remove();
        }
        entry.addAfter(head);
        purge();
    }

    private synchronized void purge() {
        if (data.size() > capacity) {
            Entry<K, V> purge = tail.prev;
            purge.remove();
            data.remove(purge.key);
            purge.next = null;
            purge.prev = null;
            purge.key = null;
            purge.val = null;
        }
    }
}
