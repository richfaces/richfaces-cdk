package org.richfaces.cdk.templatecompiler.model;

import java.io.Serializable;

import org.richfaces.cdk.CdkException;

public interface ModelElement extends Serializable {
    void visit(TemplateVisitor visitor) throws CdkException;
}