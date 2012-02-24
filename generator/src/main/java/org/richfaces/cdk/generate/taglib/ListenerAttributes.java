package org.richfaces.cdk.generate.taglib;

import javax.el.ValueExpression;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyBase;

/**
 * User: Gleb Galkin Date: 23.02.11
 */
public enum ListenerAttributes {
    FOR(new AttributeModel("for", false, ClassName.get(String.class), "If present, this attribute refers to the value of one of the exposed attached objects within the composite component inside of which this tag is nested.")),
    BINDING(new AttributeModel("binding", false, ClassName.get(ValueExpression.class), "Value binding expression that evaluates to an object that implements javax.faces.event.ActionListener.")),
    LISTENER(new AttributeModel("listener", false, ClassName.get(ValueExpression.class), "MethodExpression representing an action listener method that will be notified when this component is activated by the user. The expression must evaluate to a public method that takes an ActionEvent parameter, with a return type of void, or to a public method that takes no arguments with a return type of void")),
    TYPE(new AttributeModel("type", false, ClassName.get(ValueExpression.class), "Fully qualified Java class name of an ActionListener to be created and registered."));
    private PropertyBase attribute;

    ListenerAttributes(PropertyBase attribute) {
        this.attribute = attribute;
    }

    public PropertyBase getAttribute() {
        return attribute;
    }
}
