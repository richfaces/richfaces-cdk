
<#if attribute.aliasFor?exists>
     <#assign propertyKey=attribute.aliasFor>
<#else>
     <#assign propertyKey=attribute.name>
</#if>
 
    public ${attribute.typeName} ${attribute.getterName}() {
        <#if attribute.bindingAttribute || attribute.literal >
        return (${attribute.typeForCasting}) getStateHelper().get(Properties.${propertyKey});
        <#else>
        return (${attribute.typeForCasting}) getStateHelper().eval(Properties.${propertyKey}<#if attribute.defaultValue?exists>, ${attribute.defaultValue}</#if>);
        </#if>
    }
    
<#if ! attribute.readOnly >
    public void ${attribute.setterName}(${attribute.typeName} ${attribute.name}) {
        getStateHelper().put(Properties.${propertyKey}, ${attribute.name});
    }
</#if>