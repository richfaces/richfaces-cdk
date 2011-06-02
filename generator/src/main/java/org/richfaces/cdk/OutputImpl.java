package org.richfaces.cdk;

import java.lang.annotation.Annotation;

public class OutputImpl implements Output {
    private final Outputs value;

    public OutputImpl(Output value) {
        this.value = value.value();
    }

    public OutputImpl(Outputs value) {
        this.value = value;
    }

    @Override
    public Outputs value() {
        return value;
    }

    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Output)) {
            return false;
        }

        Output other = (Output) o;
        return value.equals(other.value());
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Output.class;
    }
}
