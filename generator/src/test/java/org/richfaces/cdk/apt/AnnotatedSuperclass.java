package org.richfaces.cdk.apt;

public class AnnotatedSuperclass {

    String value;

    /**
     * <p class="changed_added_4_0"></p>
     * @return the value
     */
    @TestMethodAnnotation("foo")
    public String getSuperValue() {
        return this.value;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param value the value to set
     */
    public void setSuperValue(String value) {
        this.value = value;
    }

}
