package ${package.name};

<#list imports as import><#if !import.default >
import<#if import.static> static</#if> ${import.className};
</#if></#list>

<@renderCommonJavaElementStuff element=.data_model />class ${simpleName} <#if superClass.name != 'java.lang.Object'>extends ${superClass.simpleName} </#if>{
	<#list fields as field>
	<@renderCommonJavaElementStuff element=field />${field.type} ${field.name}<#if field.value??> = ${field.value}</#if>;
	</#list>
	
	<#list methods as method>
	<@renderCommonJavaElementStuff element=method />${method.returnType} ${method.name}(<#list method.arguments as argument>${argument.type} ${argument.name}<#if argument_has_next>, </#if></#list>) 
	<#if !method.exceptions.empty>
		throws <#list method.exceptions as exception>${exception}<#if exception_has_next>, </#if></#list>
	</#if> {
		${method.methodBody!}
	}
	</#list>
}

<#macro renderCommonJavaElementStuff element>

<#if !element.comments.empty>
/*
<#list element.comments as comment>
 * ${comment.value}
</#list>
 */
</#if>
<#list element.annotations as annotation>
@${annotation.type}<#if !annotation.arguments.empty>(<#list annotation.arguments as argument>${argument}<#if argument_has_next>, </#if></#list>)</#if>
</#list>
<#list element.modifiers as modifier>${modifier} </#list></#macro>