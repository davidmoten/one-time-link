package com.github.davidmoten.pn;

public interface Store {

    void put(String key, String value);
    
    String get(String key);
    
}
