<#macro constant type name><#assign code><#nested></#assign>${addConstant(type,name,code)}</#macro>
<#macro require name>${addRequiredMethod(name)}</#macro>
<#macro import name>${addImport(name)}</#macro>
<#macro concat seq delimiter=","><#list seq as item><#nested item/><#if item_has_next>${delimiter}</#if></#list></#macro>

<#macro attributesField fieldName attributes>
	<@require "CREATE_ATTRIBUTES"/>
	<@constant "Attributes" "${fieldName}">${CREATE_ATTRIBUTES}()<#list attributes as attribute>
	     .${attribute.builderMethod}("${attribute.name}","${attribute.componentAttribute}"<#list attribute.behaviors as behavior>,"${behavior}"</#list>)
	     <#if attribute.defaultValue?exists >.defaultValue(${attribute.defaultValue})</#if>
	</#list></@constant>
</#macro>
