package org.richfaces.cdk.templatecompiler.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;

public interface LeafModelElement extends ModelElement {

    /* (non-Javadoc)
     * @see org.richfaces.cdk.templatecompiler.model.ModelElement#getChildren()
     */
    @XmlAnyElement(lax = true, value = ElementsHandler.class)
    @XmlMixed
    public List<Object> getChildren();

}