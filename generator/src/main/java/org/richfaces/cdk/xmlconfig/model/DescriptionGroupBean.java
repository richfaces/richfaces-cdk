package org.richfaces.cdk.xmlconfig.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.DescriptionGroup;

@XmlType(name = "descriptionType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
        propOrder = {"description", "displayname", "icon"})
public class DescriptionGroupBean implements DescriptionGroup {
    private String description;
    private String displayname;
    private Icon icon;

    public DescriptionGroupBean() {
        super();
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the icon
     */
    @XmlElement(name = "icon", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public final Icon getIcon() {
        return icon;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param icon the icon to set
     */
    public final void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the description
     */
    @XmlElement(name = "description", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public final String getDescription() {
        return description;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param description the description to set
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the displayname
     */
    @XmlElement(name = "display-name", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public final String getDisplayname() {
        return displayname;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param displayname the displayname to set
     */
    public final void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
