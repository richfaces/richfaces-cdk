<@util.require "RENDER_ATTRIBUTES_SET"/><@util.require "CREATE_ATTRIBUTES"/>
<@util.constant "Attributes" "${fieldName}">${CREATE_ATTRIBUTES}()<#list attributes as attribute>
     .${attribute.builderMethod}("${attribute.name}","${attribute.componentAttribute}"<#list attribute.behaviors as behavior>,"${behavior}"</#list>)
     <#if attribute.defaultValue?exists >.defaultValue("${attribute.defaultValue}")</#if>
</#list></@util.constant>
${RENDER_ATTRIBUTES_SET}(${facesContextVariable}, ${componentVariable}, 
	${fieldName});