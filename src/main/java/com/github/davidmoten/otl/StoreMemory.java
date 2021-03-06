package com.github.davidmoten.otl;

import java.util.HashMap;
import java.util.Map;

final class StoreMemory implements Store {

    private final Map<String, String> map = new HashMap<>();

    static final StoreMemory INSTANCE = new StoreMemory();

    @Override
    public void put(String key, String value, long expiryTime) {
        map.put(key, value);
    }

    @Override
    public synchronized String get(String key) {
        return map.remove(key);
    }

    @Override
    public void cleanup() {
        // TODO
    }
}
