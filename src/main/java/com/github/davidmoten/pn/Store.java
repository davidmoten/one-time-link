package com.github.davidmoten.pn;

import java.util.HashMap;
import java.util.Map;

final class Store {
    
    private final Map<String,String> map = new HashMap<>();
    
    static final Store INSTANCE = new Store();
    
    void put(String key, String value) {
        map.put(key, value);
    }
    
    
    synchronized String get(String key) {
        return map.remove(key);
    }

}
