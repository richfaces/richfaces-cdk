package org.richfaces.cdk.generate.taglib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.MessageFormat;

import javax.el.ValueExpression;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.PropertyBase;

/**
 * User: Gleb Galkin Date: 23.02.11
 */
public enum ListenerAttribute {

    @Description(derivator = NoneDerivator.class, base = "If present, this attribute refers to the value of one of the exposed attached objects within the composite component inside of which this tag is nested.")
    FOR("for", false, ClassName.get(String.class)),

    @Description(derivator = ListenerNameDerivator.class, base = "Value binding expression that evaluates to an object that implements {0}.")
    BINDING("binding", false, ClassName.get(ValueExpression.class)),

    @Description(derivator = EventNameDerivator.class, base = "MethodExpression representing an action listener method that will be notified when this component is activated by the user. The expression must evaluate to a public method that takes an {0} parameter, with a return type of void, or to a public method that takes no arguments with a return type of void")
    LISTENER("listener", false, ClassName.get(ValueExpression.class)),

    @Description(derivator = ListenerNameDerivator.class, base = "Fully qualified Java class name of an {0} to be created and registered.")
    TYPE("type", false, ClassName.get(ValueExpression.class));

    private AttributeModel attribute;

    ListenerAttribute(String name, boolean required, ClassName type) {
        this.attribute = new AttributeModel(name, required, type, "");
    }

    public PropertyBase derivateProperty(EventModel model) {
        String description = getDescription(model);
        return new AttributeModel(attribute.getName(), attribute.isRequired(), attribute.getType(), description);
    }

    private String getDescription(EventModel model) {
        try {
            Description annotation = ListenerAttribute.class.getField(this.toString()).getAnnotation(Description.class);
            Derivator derivator = annotation.derivator().newInstance();
            return derivator.derive(annotation.base(), model);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public abstract static class Derivator {

        public String derive(String descriptionBase, EventModel model) {
            return MessageFormat.format(descriptionBase, derivateParameters(model));
        }

        public abstract Object[] derivateParameters(EventModel model);
    }

    static class NoneDerivator extends Derivator {
        @Override
        public Object[] derivateParameters(EventModel model) {
            return new Object[] {};
        }
    }

    static class ListenerNameDerivator extends Derivator {
        @Override
        public Object[] derivateParameters(EventModel model) {
            return new Object[] { model.getListenerInterface().getSimpleName() };
        }
    }

    static class EventNameDerivator extends Derivator {
        @Override
        public Object[] derivateParameters(EventModel model) {
            return new Object[] { model.getType().getSimpleName() };
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    @Inherited
    private @interface Description {
        String base();

        Class<? extends Derivator> derivator();
    }
}
