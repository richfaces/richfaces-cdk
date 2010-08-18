for (${varType} ${var}: ${itemsExpression}) {
	<#list statements as subStatement>
	${subStatement.code}
	</#list>
}