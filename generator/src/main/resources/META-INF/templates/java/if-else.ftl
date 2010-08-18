<#list statements as statement>
	<#if statement_index == 0>
	if (${statement.test}) {
	<#else>
	} else <#if statement.test?exists>if (${statement.test}) </#if> { 
	</#if>
	<#list statement.statements as subStatement>
	${subStatement.code}
	</#list>
</#list>}