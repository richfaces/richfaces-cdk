if (component.getFacet("${name}") != null) {
    component.getFacet("${name}").encodeAll(facesContext);
} else {
    <#list statements as subStatement>
	${subStatement.code}
	</#list>
}