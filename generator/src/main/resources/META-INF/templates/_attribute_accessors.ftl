
<#if attribute.aliasFor?exists>
     <#assign propertyKey=attribute.aliasFor>
<#else>
     <#assign propertyKey=attribute.name>
</#if>
<#if utils.isKeyword(propertyKey) >
     <#assign propertyKey>${propertyKey}Value</#assign>
</#if>
 
    public ${attribute.typeName} ${attribute.getterName}() {
        <#if attribute.bindingAttribute || attribute.literal >
        ${attribute.typeForCasting} value = (${attribute.typeForCasting}) getStateHelper().get(Properties.${propertyKey});
        <#else>
        ${attribute.typeForCasting} value = (${attribute.typeForCasting}) getStateHelper().eval(Properties.${propertyKey}<#if attribute.defaultValue?exists>, ${attribute.defaultValue}</#if>);
        </#if>
        return value;
    }
    
<#if ! attribute.readOnly >
    public void ${attribute.setterName}(${attribute.typeName} ${propertyKey}) {
        getStateHelper().put(Properties.${propertyKey}, ${propertyKey});
        <#if attribute.passThrough >
        handleAttribute("${attribute.name}",${propertyKey});
        </#if>
    }
</#if>