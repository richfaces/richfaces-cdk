package org.richfaces.cdk.templatecompiler.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.richfaces.cdk.CdkException;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * All classes that are used in template bodey should be presented in the {@link XmlSeeAlso} annotation
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@XmlSeeAlso( { CdkCallElement.class, CdkBodyElement.class, CdkIfElement.class, CdkObjectElement.class,
    CdkChooseElement.class, CdkWhenElement.class, CdkOtherwiseElement.class, CdkForEachElement.class,
    CdkSwitchElement.class, CdkCaseElement.class, CdkDefaultElement.class })
public class ModelFragment implements LeafModelElement {

    private List<Object> children = Lists.newArrayList();

    @XmlAnyElement(lax = true, value = ElementsHandler.class)
    @XmlMixed
    public List<Object> getChildren() {
        return this.children;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param body
     *            the body to set
     */
    public void setChildren(List<Object> body) {
        this.children = body;
    }

    @Override
    public void visit(TemplateVisitor visitor) throws CdkException {
        beforeVisit(visitor);
        List<Object> childrenList = getChildren();
        for (Object child : childrenList) {
            if (child instanceof String) {
                visitor.visitElement((String) child);
            } else if (child instanceof ModelElement) {
                ((ModelElement) child).visit(visitor);
            } else {
                throw new CdkException("Unknown type of element in renderer template " + child.getClass());
            }
        }
        afterVisit(visitor);
    }

    public void afterVisit(TemplateVisitor visitor) throws CdkException {
    }

    public void beforeVisit(TemplateVisitor visitor) throws CdkException {
    }

}