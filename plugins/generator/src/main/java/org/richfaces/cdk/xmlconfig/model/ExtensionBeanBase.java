package org.richfaces.cdk.xmlconfig.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConfigExtension;

public class ExtensionBeanBase extends ConfigExtension {

    private ClassName baseClass;
    private Boolean generate;

    public ExtensionBeanBase() {
        super();
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the baseClass
     */
    @XmlElement(name = "base-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getBaseClass() {
        return baseClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param baseClass the baseClass to set
     */
    public void setBaseClass(ClassName baseClass) {
        this.baseClass = baseClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the generate
     */
    @XmlElement(name = "generate", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public Boolean getGenerate() {
        return generate;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param generate the generate to set
     */
    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }

}