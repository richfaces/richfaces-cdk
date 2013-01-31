<#if loopObjectRequired>
	ForEachLoop<${varType}> ${loopObject} = ForEachLoop.getInstance(${itemsExpression});
	<#if begin??>
	${loopObject}.setBegin(${begin});
	</#if>
	<#if end??>
	${loopObject}.setEnd(${end});
	</#if>
	<#if step??>
	${loopObject}.setStep(${step});
	</#if>
	for (${varType} ${var} : ${loopObject}) {
		<#if varStatus??>
		ForEachLoop<${varType}>.Status ${varStatus} = ${loopObject}.getStatus();
		</#if>
		<#list statements as subStatement>
		${subStatement.code}
		</#list>
	}
<#else>
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
</#if>