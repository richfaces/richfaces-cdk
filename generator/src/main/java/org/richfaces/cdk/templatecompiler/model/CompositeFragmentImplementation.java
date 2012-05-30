package org.richfaces.cdk.templatecompiler.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.richfaces.cdk.CdkException;

/**
 * @author Lukas Fryc
 */
@XmlRootElement(name = "implementation", namespace = Template.COMPOSITE_NAMESPACE)
public class CompositeFragmentImplementation extends ModelFragment implements Serializable {

    private static final long serialVersionUID = -6604972558344909450L;

    @Override
    public void beforeVisit(TemplateVisitor visitor) throws CdkException {
        visitor.preProcess(this);
    }

    @Override
    public void afterVisit(TemplateVisitor visitor) throws CdkException {
        visitor.postProcess(this);
    }
}
