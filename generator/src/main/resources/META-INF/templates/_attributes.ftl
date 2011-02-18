<#assign hasKeyword=false/>
    protected enum Properties {
        <@util.concat seq=generatedAttributes delimiter=",\n        "; attribute>${attribute.name}<#if utils.isKeyword(attribute.name)>Value("${attribute.name}")<#assign hasKeyword=true/></#if></@util.concat>
        <#if hasKeyword>;
    String toString;

    Properties(String toString) {
        this.toString = toString;
    }

    Properties() {
    }

    public String toString() {
        return ((this.toString != null) ? this.toString : super.toString());
    }</#if>
    }
    <#assign passThroughCount=0>
    <#list generatedAttributes as attribute>
        <#if attribute.passThrough >
           <#assign passThroughCount=passThroughCount+1/>
        </#if>
        <#include "_attribute_accessors.ftl">
    </#list>
    <#if passThroughCount gt 0 >
    private static final String ATTRIBUTES_THAT_ARE_SET_KEY = "javax.faces.component.UIComponentBase.attributesThatAreSet";
    
    private void handleAttribute(String name, Object value) {
        List<String> setAttributes = (List<String>) this.getAttributes().get(ATTRIBUTES_THAT_ARE_SET_KEY);
        if (setAttributes == null) {
                setAttributes = new ArrayList<String>(${passThroughCount});
                this.getAttributes().put(ATTRIBUTES_THAT_ARE_SET_KEY, setAttributes);
        }
        if (value == null) {
                ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                }
            } else if (!setAttributes.contains(name)) {
                setAttributes.add(name);
        }
    }    
    </#if>