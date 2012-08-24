package org.richfaces.cdk.apt;

import java.io.File;
import java.io.IOException;

import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.util.SerializationUtils;

import com.google.common.io.Files;

public class LibraryCache {

    private File cacheFile = new File("target/library-cache.ser");

    public boolean available() {
        return cacheFile.exists();
    }

    public ComponentLibrary load() {
        try {
            byte[] bytes = Files.toByteArray(cacheFile);
            String base64 = new String(bytes);
            return SerializationUtils.deserializeFromBase64(base64);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void save(ComponentLibrary library) {
        try {
            String base64 = SerializationUtils.serializeToBase64(library);
            byte[] bytes = base64.getBytes();
            Files.write(bytes, cacheFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
