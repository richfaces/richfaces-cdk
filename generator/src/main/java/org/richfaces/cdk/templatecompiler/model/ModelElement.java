package org.richfaces.cdk.templatecompiler.model;

import org.richfaces.cdk.CdkException;

public interface ModelElement {

    public void visit(TemplateVisitor visitor) throws CdkException;

}