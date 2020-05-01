package com.github.davidmoten.pn;

public interface Store {

    void put(String key, String value, long expiryTime);
    
    String get(String key);
    
}
