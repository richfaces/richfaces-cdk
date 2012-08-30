package org.richfaces.cdk;

import java.lang.annotation.Annotation;

import org.richfaces.cdk.apt.CacheType;

public class CacheImpl implements Cache {
    private final CacheType value;

    public CacheImpl(Cache value) {
        this.value = value.value();
    }

    public CacheImpl(CacheType value) {
        this.value = value;
    }

    @Override
    public CacheType value() {
        return value;
    }

    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache other = (Cache) o;
        return value.equals(other.value());
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Cache.class;
    }
}
