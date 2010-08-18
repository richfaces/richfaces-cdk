<#if value.literal>
  ${responseWriterVariable}.writeURIAttribute("${attributeName}",${value},null);
<#else>
   {
      ${value.type} value = ${value};
      <#if value.type == "String" >
      if(null != value && value.length()>0) {
      <#else>
      if(null != value && value.toString().length()>0) {
      </#if>
        ${responseWriterVariable}.writeURIAttribute("${attributeName}",value,null);
      }
   }
</#if>
