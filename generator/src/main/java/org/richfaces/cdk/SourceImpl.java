package org.richfaces.cdk;

import java.lang.annotation.Annotation;

public class SourceImpl implements Source {
    private final Sources value;

    public SourceImpl(Source value) {
        this.value = value.value();
    }

    public SourceImpl(Sources value) {
        this.value = value;
    }

    @Override
    public Sources value() {
        return value;
    }

    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Source)) {
            return false;
        }

        Source other = (Source) o;
        return value.equals(other.value());
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Source.class;
    }
}
