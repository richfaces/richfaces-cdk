<#if iterable>
	for (${varType} ${var} : ${itemsExpression}) {
		<#list statements as subStatement>
		${subStatement.code}
		</#list>
	}
<#elseif iterator>
	while (${itemsExpression}.hasNext()) {
	    ${varType} ${var} = ${itemsExpression}.next();
	    <#list statements as subStatement>
		${subStatement.code}
		</#list>
	}
<#else>
	${varType} ${var} = ${itemsExpression};
	<#list statements as subStatement>
	${subStatement.code}
	</#list>
</#if>