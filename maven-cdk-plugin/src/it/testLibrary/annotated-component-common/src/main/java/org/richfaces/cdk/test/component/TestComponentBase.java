package org.richfaces.cdk.test.component;

import javax.faces.component.UIComponentBase;
import javax.faces.convert.Converter;

import org.richfaces.cdk.annotations.Attribute;

public abstract class TestComponentBase extends UIComponentBase {
    
    String baseProperty;

    /**
     * <p class="changed_added_4_0"></p>
     * @return the baseProperty
     */
    @Attribute
    public String getBaseProperty() {
        return this.baseProperty;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param baseProperty the baseProperty to set
     */
    public void setBaseProperty(String baseProperty) {
        this.baseProperty = baseProperty;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the abstractBaseProperty
     */
    @Attribute
    public abstract String getAbstractBaseProperty();

    /**
     * <p class="changed_added_4_0"></p>
     * @param abstractBaseProperty the abstractBaseProperty to set
     */
    public abstract void setAbstractBaseProperty(String abstractBaseProperty);

    @Attribute
    public Converter getRowKeyConverter() {
        return null;
    }

    public void setRowKeyConverter(Converter converter) {
    }


}
