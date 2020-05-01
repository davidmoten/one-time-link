package com.github.davidmoten.otl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import com.github.davidmoten.guavamini.Preconditions;

public final class StoreFileSystem implements Store {

    private static final long MAX_DAYS_TO_LIVE = 30;

    private final File directory;

    public StoreFileSystem(File directory) {
        this.directory = directory;
    }

    @Override
    public void put(String key, String value, long expiryTime) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        directory.mkdirs();
        validateKey(key);
        File valueFile = valueFile(key);
        if (valueFile.exists()) {
            throw new RuntimeException("key already exists: " + key);
        }
        try (FileOutputStream fos = new FileOutputStream(valueFile)) {
            fos.write(value.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        try (FileOutputStream fos = new FileOutputStream(expiryFile(key))) {
            long t = Math.min(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(MAX_DAYS_TO_LIVE), expiryTime);
            fos.write(Long.toString(t).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File valueFile(String key) {
        return new File(directory, "one-time-link-" + key + ".encrypted");
    }

    private File expiryFile(String key) {
        return new File(directory, "one-time-link-" + key + ".expiry");
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
        File expiryFile = expiryFile(key);
        File file = valueFile(key);

        try {
            if (!expiryFile.exists()) {
                file.delete();
                return null;
            } else {
                byte[] bytes = Files.readAllBytes(expiryFile.toPath());
                long expiryTime = Long.parseLong(new String(bytes, StandardCharsets.UTF_8));
                if (expiryTime < System.currentTimeMillis()) {
                    expiryFile.delete();
                    file.delete();
                    return null;
                }
            }
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                if (!file.delete()) {
                    throw new IOException("could not delete file " + file);
                } else {
                    return new String(bytes, StandardCharsets.UTF_8);
                }

            } else {
                return null;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
