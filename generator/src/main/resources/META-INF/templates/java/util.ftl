<#macro constant type name><#assign code><#nested></#assign>${addConstant(type,name,code)}</#macro>
<#macro require name>${addRequiredMethod(name)}</#macro>
<#macro import name>${addImport(name)}</#macro>
<#macro concat seq delimiter=","><#list seq as item><#nested item/><#if item_has_next>${delimiter}</#if></#list></#macro>

