package org.richfaces.cdk.apt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.util.SerializationUtils;

import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class LibraryCacheImpl implements LibraryCache {

    private CacheType cacheType;

    @Inject
    private Logger log;

    @Inject
    @Output(Outputs.LIBRARY_CACHE)
    private FileManager fileManager;

    @Inject(optional = true)
    @Named(CACHE_ENABLED_OPTION)
    private boolean cachingEnabled = true;

    private ComponentLibrary cachedLibrary = null;

    public LibraryCacheImpl(CacheType cacheType) {
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
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            cachingEnabled = false;
            log.info("Unable to load library cache " + getFilename() + ". Full build will be ran and cache rewritten.", e);
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

    public boolean storedBefore(long time) {
        return lastModified() < time;
    }

    public ComponentLibrary load() {
        if (cachedLibrary != null) {
            return cachedLibrary;
        }

        try {
            byte[] bytes = Files.toByteArray(getCacheFile());
            cachedLibrary = SerializationUtils.deserializeFromBytes(bytes);
            return cachedLibrary;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void save(ComponentLibrary library) {
        try {
            byte[] bytes = SerializationUtils.serializeToBytes(library);
            try {
                fileManager.createOutput(getFilename(), System.currentTimeMillis());
                Files.write(bytes, getCacheFile());
            } catch (IOException e) {
                log.warn("Can't write to library cache file " + getFilename(), e);
            }
        } catch (Exception e) {
            log.warn("can't serialize library model", e);
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
