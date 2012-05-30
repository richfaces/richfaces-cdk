package org.richfaces.cdk.templatecompiler.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.richfaces.cdk.CdkException;

/**
 * @author Lukas Fryc
 */
@XmlRootElement(name = "fragment", namespace = Template.CDK_NAMESPACE)
public class CdkFragmentElement extends ModelFragment implements Serializable {

    private static final long serialVersionUID = -1885511982050527608L;

    private String name;
    private CompositeFragmentInterface fragmentInterface;
    private CompositeFragmentImplementation fragmentImplementation;

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "interface", namespace = Template.COMPOSITE_NAMESPACE)
    public CompositeFragmentInterface getFragmentInterface() {
        return fragmentInterface;
    }

    public void setFragmentInterface(CompositeFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    @XmlElement(name = "implementation", namespace = Template.COMPOSITE_NAMESPACE)
    public CompositeFragmentImplementation getFragmentImplementation() {
        return fragmentImplementation;
    }

    public void setFragmentImplementation(CompositeFragmentImplementation fragmentImplementation) {
        this.fragmentImplementation = fragmentImplementation;
    }

    @Override
    public void beforeVisit(TemplateVisitor visitor) throws CdkException {
        visitor.preProcess(this);
    }

    @Override
    public void afterVisit(TemplateVisitor visitor) throws CdkException {
        visitor.postProcess(this);
    }

}
