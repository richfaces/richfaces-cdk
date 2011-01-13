package org.richfaces.cdk.apt;

import java.lang.annotation.Annotation;

import javax.lang.model.element.AnnotationMirror;

import org.richfaces.cdk.apt.SourceUtils.ACCESS_TYPE;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;
import org.richfaces.cdk.model.ClassName;

public final class DummyPropertyImpl implements BeanProperty {
    private final String name;

    public DummyPropertyImpl(String name) {
        this.name = name;
    }

    @Override
    public boolean isExists() {
        return false;
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return false;
    }

    @Override
    public ClassName getType() {
        return ClassName.get(Object.class);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDocComment() {
        return null;
    }

    @Override
    public AnnotationMirror getAnnotationMirror(Class<? extends Annotation> annotationType) {
        return null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return null;
    }

    @Override
    public ACCESS_TYPE getAccessType() {
        return ACCESS_TYPE.readWrite;
    }
}