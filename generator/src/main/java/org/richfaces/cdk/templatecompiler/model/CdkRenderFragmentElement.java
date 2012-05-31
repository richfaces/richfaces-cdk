package org.richfaces.cdk.templatecompiler.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.richfaces.cdk.CdkException;

/**
 * @author Lukas Fryc
 */
@XmlRootElement(name = "renderFragment", namespace = Template.CDK_NAMESPACE)
public class CdkRenderFragmentElement implements ModelElement {

    private String name;
    private Map<QName, String> attributes;

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAnyAttribute
    public Map<QName, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<QName, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void visit(TemplateVisitor visitor) throws CdkException {
        visitor.visitElement(this);
    }
}
