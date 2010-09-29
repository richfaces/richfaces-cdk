<#if value.literal >
  <#if value == '"true"' || value == '"${attributeName}"' >
  ${responseWriterVariable}.writeAttribute("${attributeName}","${attributeName}",null);
  </#if>
<#else>
      <#if value.type == "boolean" >
      if(${value}) {
      <#elseif value.type == "Boolean" >
      if(Boolean.TRUE.equals(${value})) {
      <#elseif value.type == "String" >
      if("${attributeName}".equals(${value}) || Boolean.valueOf(${value}) ) {
      <#else>
      if(null != ${value} && ( Boolean.valueOf(${value}.toString()) || "${attributeName}".equals(${value}.toString())) ) {
      </#if>
        ${responseWriterVariable}.writeAttribute("${attributeName}","${attributeName}",null);
      }
</#if>
