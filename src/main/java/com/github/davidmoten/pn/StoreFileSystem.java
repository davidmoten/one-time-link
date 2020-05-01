package com.github.davidmoten.pn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.github.davidmoten.guavamini.Preconditions;

public final class StoreFileSystem implements Store {

    private final File directory;

    public StoreFileSystem(File directory) {
        this.directory = directory;
    }

    @Override
    public void put(String key, String value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        directory.mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file(key))) {
            fos.write(value.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File file(String key) {
        validateKey(key);
        return new File(directory, "private-note-" + key + ".encrypted");
    }

    private void validateKey(String key) {
        // validation step important to avoid injection of path modifiers like .. or /
        // into the key
        for (char ch : key.toCharArray()) {
            if (!(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z')) {
                throw new RuntimeException("key can only contain lower or uppercase A-Z characters");
            }
        }
    }

    @Override
    public String get(String key) {
        File file = file(key);
        if (file.exists()) {
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                if (!file.delete()) {
                    throw new IOException("could not delete file " + file);
                } else {
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            System.out.println("file does not exist: " + file);
            return null;
        }
    }

}
