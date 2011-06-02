package org.richfaces.cdk.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public interface DescriptionGroup {
    /**
     * <p class="changed_added_4_0">
     * Documentation description of that element.
     * </p>
     *
     * @return the description
     */
    @XmlElement
    @Merge
    String getDescription();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param description the description to set
     */
    void setDescription(String description);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the displayname
     */
    @XmlElement(name = "display-name")
    @Merge
    String getDisplayName();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param displayname the displayname to set
     */
    void setDisplayName(String displayname);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the icon
     */
    @XmlElement
    @Merge
    Icon getIcon();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param icon the icon to set
     */
    void setIcon(Icon icon);

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @author asmirnov@exadel.com
     */
    @XmlType(name = "iconType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE, propOrder = { "smallIcon", "largeIcon" })
    public static final class Icon {
        private String largeIcon;
        private String smallIcon;

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the smallIcon
         */
        @XmlElement(name = "small-icon", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
        public String getSmallIcon() {
            return smallIcon;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param smallIcon the smallIcon to set
         */
        public void setSmallIcon(String smallIcon) {
            this.smallIcon = smallIcon;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @return the largeIcon
         */
        @XmlElement(name = "large-icon", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
        public String getLargeIcon() {
            return largeIcon;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         *
         * @param largeIcon the largeIcon to set
         */
        public void setLargeIcon(String largeIcon) {
            this.largeIcon = largeIcon;
        }
    }
}
