package org.richfaces.cdk.templatecompiler.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Lukas Fryc
 */
@XmlRootElement(name = "interface", namespace = Template.COMPOSITE_NAMESPACE)
public class CompositeFragmentInterface extends ModelFragment implements Serializable {

    private static final long serialVersionUID = -1885511982050527608L;

    private List<CompositeAttribute> attributes;

    @XmlElement(name = "attribute", namespace = Template.COMPOSITE_NAMESPACE)
    public List<CompositeAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CompositeAttribute> attributes) {
        this.attributes = attributes;
    }
}
