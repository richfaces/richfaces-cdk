${type} ${name} = new ${implementationType}(<#if base?exists>${base}</#if>); 
<#list statements as subStatement>
${subStatement.code}
</#list>