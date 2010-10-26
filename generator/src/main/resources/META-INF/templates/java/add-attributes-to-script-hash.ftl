<@util.require "ADD_TO_SCRIPT_HASH_ATTRIBUTES"/>

<@util.attributesField "${fieldName}" attributes />

${ADD_TO_SCRIPT_HASH_ATTRIBUTES}(${parent.name}, ${facesContextVariable}, ${componentVariable}, ${fieldName}, <#if wrapper?exists>ScriptHashVariableWrapper.${wrapper}<#else>null</#if>);