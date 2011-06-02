package org.richfaces.cdk.templatecompiler;

import javax.xml.namespace.QName;

import org.richfaces.cdk.templatecompiler.builder.model.JavaStatement;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class JavaClassModelWrapper extends BeansWrapper implements ObjectWrapper {
    public JavaClassModelWrapper() {
    }

    @Override
    public TemplateModel wrap(Object obj) throws TemplateModelException {

        TemplateModel templateModel;

        if (obj instanceof JavaStatement) {
            templateModel = new MethodBodyTemplateModel((JavaStatement) obj, this);
        } else if (obj instanceof ELType) {
            templateModel = new ELTypeTemplateModel((ELType) obj, this);
        } else if (obj instanceof QName) {
            templateModel = new QNameTemplateModel((QName) obj, this);
        } else {
            templateModel = super.wrap(obj);
        }

        return templateModel;
    }
}
