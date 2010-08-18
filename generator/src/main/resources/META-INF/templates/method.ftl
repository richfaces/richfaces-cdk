<#list method.comments as comment>
//${comment}
</#list>
<#list method.modifiers as modifier>${modifier} </#list> ${method.returnType} ${method.name}(<#list method.arguments as argument>${argument.type} ${argument.name}<#if argument_has_next>, </#if></#list>) {
	${method.methodBody.toCode()}
}
