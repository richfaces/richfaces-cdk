package org.richfaces;

import javax.el.MethodExpression;
import javax.faces.el.MethodBinding;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.TagAttribute;

public class MethodMetadata extends Metadata {

    public MethodMetadata(TagAttribute attribute, Class<String> class1, Class<Integer> class2) {
        // TODO Auto-generated constructor stub
    }

    public MethodMetadata(TagAttribute attribute, Class<String> class1) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void applyMetadata(FaceletContext ctx, Object instance) {
    }

    protected MethodBinding getMethodBinding(FaceletContext ctx) {
        // TODO Auto-generated method stub
        return null;
    }

    protected MethodExpression getMethodExpression(FaceletContext ctx) {
        // TODO Auto-generated method stub
        return null;
    }

}
