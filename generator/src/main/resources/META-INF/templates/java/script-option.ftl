<@util.require "ADD_TO_SCRIPT_HASH" />
${ADD_TO_SCRIPT_HASH}(${parent.name}, "${name}", ${value}, <#if defaultValue?exists>${defaultValue}<#else>null</#if>, <#if wrapper?exists>ScriptHashVariableWrapper.${wrapper}<#else>null</#if>);
