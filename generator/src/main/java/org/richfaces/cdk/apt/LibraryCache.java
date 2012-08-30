package org.richfaces.cdk.apt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.util.SerializationUtils;

import com.google.common.io.Files;
import com.google.inject.Inject;

public class LibraryCache {

    private boolean cachingEnabled = true;

    private CacheType cacheType;

    @Inject
    @Output(Outputs.LIBRARY_CACHE)
    private FileManager fileManager;
    
    private ComponentLibrary cachedLibrary = null; 

    public LibraryCache(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public boolean available() {
        try {
            if (cachedLibrary != null) {
                return true;
            }
            if (cachingEnabled && getCacheFile().exists()) {
                load();
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public long lastModified() {
        try {
            return available() ? getCacheFile().lastModified() : 0L;
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public ComponentLibrary load() {
        if (cachedLibrary != null) {
            return cachedLibrary;
        }
        
        try {
            byte[] bytes = Files.toByteArray(getCacheFile());
            String base64 = new String(bytes);
            cachedLibrary = SerializationUtils.deserializeFromBase64(base64);
            return cachedLibrary;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void save(ComponentLibrary library) {
        try {
            String base64 = SerializationUtils.serializeToBase64(library);
            Writer writer = fileManager.createOutput(getFilename(), System.currentTimeMillis());
            writer.write(base64);
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to library cache file " + getFilename(), e);
        }
    }

    private File getCacheFile() throws FileNotFoundException {
        String filename = getFilename();
        return fileManager.getFile(filename);
    }
    
    private String getFilename() {
        return cacheType + ".ser";
    }
}
