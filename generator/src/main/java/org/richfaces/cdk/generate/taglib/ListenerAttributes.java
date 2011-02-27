package org.richfaces.cdk.generate.taglib;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyBase;

import javax.el.ValueExpression;

/**
 * User: Gleb Galkin
 * Date: 23.02.11
 */
public enum ListenerAttributes {
    FOR(new AttributeModel("for", false, ClassName.get(String.class))),
    BINDING(new AttributeModel("binding", false, ClassName.get(ValueExpression.class))),
    LISTENER(new AttributeModel("listener", false, ClassName.get(ValueExpression.class))),
    TYPE(new AttributeModel("type", false, ClassName.get(ValueExpression.class)));

    private PropertyBase attribute;

    ListenerAttributes(PropertyBase attribute) {
        this.attribute = attribute;
    }

    public PropertyBase getAttribute() {
        return attribute;
    }
}
