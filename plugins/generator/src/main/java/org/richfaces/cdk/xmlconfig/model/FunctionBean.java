package org.richfaces.cdk.xmlconfig.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;

@XmlType(name = "functionType", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
public class FunctionBean extends DescriptionGroupBean {

    private String name;
    
    private String signature;

    private TagType type;

    private ClassName functionClass;

    /**
     * <p class="changed_added_4_0"></p>
     * @return the name
     */
    @XmlElement(name = "function-name", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public String getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the signature
     */
    @XmlElement(name = "function-signature", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public String getSignature() {
        return this.signature;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the type
     */
    public TagType getType() {
        return this.type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param type the type to set
     */
    public void setType(TagType type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the functionClass
     */
    @XmlElement(name = "function-class", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
    public ClassName getFunctionClass() {
        return this.functionClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param functionClass the functionClass to set
     */
    public void setFunctionClass(ClassName functionClass) {
        this.functionClass = functionClass;
    }
}
