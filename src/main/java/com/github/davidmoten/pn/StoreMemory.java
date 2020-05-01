package com.github.davidmoten.pn;

import java.util.HashMap;
import java.util.Map;

final class StoreMemory implements Store {

    private final Map<String, String> map = new HashMap<>();

    static final StoreMemory INSTANCE = new StoreMemory();

    public void put(String key, String value, long expiryTime) {
        map.put(key, value);
    }

    public synchronized String get(String key) {
        return map.remove(key);
    }
}
