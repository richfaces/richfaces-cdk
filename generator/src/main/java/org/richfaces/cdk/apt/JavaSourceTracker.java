package org.richfaces.cdk.apt;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

public interface JavaSourceTracker {

    void putChanged(JavaFileObject sourceObject);

    boolean isChanged(Element element);
}
