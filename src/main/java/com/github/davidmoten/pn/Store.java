package com.github.davidmoten.pn;

import java.util.HashMap;
import java.util.Map;

final class Store {

    private final Map<String, String> map = new HashMap<>();

    static final Store INSTANCE = new Store();

    void put(String key, String value) {
        System.out.println("stored key=" + key + ", value=" + value);
        map.put(key, value);
    }

    synchronized String get(String key) {
        String v = map.remove(key);
        System.out.println("returning value=" + v + " for key=" + key);
        return v;
    }

}
