<#if default>
	default:
<#else>
	<#list values as value>
	case ${value}:
	</#list>
</#if>
	<#list statements as subStatement>
	${subStatement.code}
	</#list>
	
	break;