<#if value.literal && value == "true">
  ${responseWriterVariable}.writeAttribute("${attributeName}","${attributeName}",null);
<#else>
      <#if value.type == "boolean" >
      if(${value}) {
      <#elseif value.type == "Boolean" >
      if(Boolean.TRUE.equals(${value})) {
      <#else>
      if(Boolean.valueOf(String.valueOf(${value}))) {
      </#if>
        ${responseWriterVariable}.writeAttribute("${attributeName}","${attributeName}",null);
      }
</#if>
