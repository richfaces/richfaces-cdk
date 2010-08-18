package ${tag.targetClass.package};

import java.io.IOException;
import java.io.Serializable;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import ${model.type};
import ${model.listenerInterface};
import ${model.sourceInterface};

public class ${tag.targetClass.simpleName} extends TagHandler {

    @SuppressWarnings("serial")
    public static final class LazyListener implements ${model.listenerInterface.simpleName}, Serializable {

        private final String type;

        private final ValueExpression binding;

        public LazyListener(String type, ValueExpression binding) {
            this.type = type;
            this.binding = binding;
        }

        public void ${model.listenerMethod}(${model.type} event) throws AbortProcessingException {
            ${model.listenerInterface.simpleName} instance = null;
            FacesContext faces = FacesContext.getCurrentInstance();
            if (faces == null) {
                return;
            }

            if (this.binding != null) {
                instance = (${model.listenerInterface.simpleName}) binding.getValue(faces.getELContext());
            }

            if (instance == null && this.type != null) {
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if (null == classLoader) {
                        classLoader = ${model.listenerInterface.simpleName}.class.getClassLoader();
                    }
                    instance = classLoader.loadClass(this.type).asSubclass(${model.listenerInterface.simpleName}.class).newInstance();
                } catch (Exception e) {
                    throw new AbortProcessingException("Couldn't lazily instantiate ${model.listenerInterface.simpleName}", e);
                }

                if (this.binding != null) {
                    binding.setValue(faces.getELContext(), instance);
                }
            }

            if (instance != null) {
                instance.${model.listenerMethod}(event);
            }
        }
    }

    @SuppressWarnings("serial")
    public static final class MethodExpressionListener implements ${model.listenerInterface.simpleName}, Serializable {

        private MethodExpression methodExpression;

        public MethodExpressionListener(MethodExpression methodExpression) {
            super();
            this.methodExpression = methodExpression;
        }

        public void ${model.listenerMethod}(${model.type} actionEvent) throws AbortProcessingException {
            if (actionEvent == null) {
                throw new NullPointerException();
            }
            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            try {
                methodExpression.invoke(elContext, new Object[] { actionEvent });
            } catch (Exception e) {
                new AbortProcessingException(e);
            }
        }
    }

    private TagAttribute binding;

    private String listenerType;

    private TagAttribute listenerMethod;

    public TestListenerHandler(TagConfig config) {
        super(config);

        this.binding = this.getAttribute("binding");

        TagAttribute type = this.getAttribute("type");
        if (type != null) {
            if (!type.isLiteral()) {
                throw new TagAttributeException(type, "Must be a literal class name of type ${model.listenerInterface.simpleName}");
            } else {
                // test it out
            }

            this.listenerType = type.getValue();
        } else {
            this.listenerType = null;
        }

        this.listenerMethod = this.getAttribute("listener");

        if (this.listenerMethod != null && this.binding != null) {
            throw new TagException(this.tag, "Attributes 'listener' and 'binding' cannot be used simultaneously");
        }

        if (this.listenerMethod != null && this.listenerType != null) {
            throw new TagException(this.tag, "Attributes 'listener' and 'type' cannot be used simultaneously");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.view.facelets.FaceletHandler#apply(javax.faces.view.facelets.FaceletContext,
     * javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        if (null != parent && ComponentHandler.isNew(parent)) {
            if (!(parent instanceof ${model.sourceInterface.simpleName})) {
                throw new TagException(this.tag, "Parent is not of type ${model.sourceInterface.simpleName}, type is: " + parent);
            }

            ${model.sourceInterface.simpleName} as = (${model.sourceInterface.simpleName}) parent;

            if (this.listenerMethod != null) {
                MethodExpression listenerMethodExpression =
                    this.listenerMethod.getMethodExpression(ctx, Void.TYPE, new Class<?>[] { ${model.type}.class });

                as.add${model.listenerInterface.simpleName}(new MethodExpressionListener(listenerMethodExpression));
            } else {
                ValueExpression b = null;
                if (this.binding != null) {
                    b = this.binding.getValueExpression(ctx, ${model.listenerInterface.simpleName}.class);
                }
                ${model.listenerInterface.simpleName} listener = new LazyListener(this.listenerType, b);
                as.add${model.listenerInterface.simpleName}(listener);
            }
        }
    }

}
