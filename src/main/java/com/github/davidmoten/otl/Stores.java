package com.github.davidmoten.otl;

import java.io.File;

public final class Stores {

    private static Store instance;
    
    private Stores() {
        // prevent instantiation
    }

    synchronized static Store instance() {
        if (instance == null) {
            instance = create();
        }
        return instance;
    }

    private static Store create() {
        return new StoreFileSystem(new File(System.getProperty("java.io.tmpdir"), "one-time-link"));
    }
}
