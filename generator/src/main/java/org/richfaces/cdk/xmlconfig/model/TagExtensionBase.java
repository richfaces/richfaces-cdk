package org.richfaces.cdk.xmlconfig.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.TagModel;

import com.google.common.collect.Lists;

public class TagExtensionBase extends ExtensionBeanBase {
    private List<TagModel> tags = Lists.newArrayList();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the tags
     */
    @XmlElement(name = "tag", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public List<TagModel> getTags() {
        return this.tags;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param tags the tags to set
     */
    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }
}