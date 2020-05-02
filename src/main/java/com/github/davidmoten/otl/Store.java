package com.github.davidmoten.otl;

public interface Store {

    void put(String key, String value, long expiryTime);
    
    String get(String key);
    
    void cleanup();
    
}
