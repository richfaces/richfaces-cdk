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

    <#list generatedAttributes as attribute>
        <#include "_attribute_accessors.ftl">
    </#list>
