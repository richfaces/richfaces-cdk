package org.richfaces.cdk.apt;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.ENUM;
import static javax.lang.model.element.ElementKind.INTERFACE;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;

@Singleton
public class JavaSourceTrackerImpl implements JavaSourceTracker {

    private List<JavaFileObject> changed = Lists.newLinkedList();
    private Map<Element, Boolean> elements = Maps.newHashMap();

    public void putChanged(JavaFileObject sourceObject) {
        changed.add(sourceObject);
    }

    public boolean isChanged(Element element) {
        Boolean result = elements.get(element);
        if (result == null) {
            result = determineElementChanged(element);
            elements.put(element, result);
        }
        return result;
    }

    private boolean determineElementChanged(Element element) {
        ElementKind kind = element.getKind();
        if (kind == CLASS || kind == ENUM || kind == INTERFACE) {
            for (JavaFileObject sourceObject : changed) {
                if (sourceObject.isNameCompatible(element.getSimpleName().toString(), Kind.SOURCE)) {
                    elements.put(element, true);
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
