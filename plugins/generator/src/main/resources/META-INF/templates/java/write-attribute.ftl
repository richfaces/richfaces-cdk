<#if value.literal>
  ${responseWriterVariable}.writeAttribute("${attributeName}",${value},null);
<#else>
   {
      ${value.type} value = ${value};
      if(<#if defaultValue?exists>null == value || !(<#else>null != value &&</#if>
      <#if value.type == "Integer" || value.type == "int" >
         value != Integer.MIN_VALUE
      <#elseif value.type == "Double" || value.type == "double">
         value != Double.MIN_VALUE
      <#elseif value.type == "Character" || value.type == "char">
         value != Character.MIN_VALUE
      <#elseif value.type == "Float" || value.type == "float">
         value != Float.MIN_VALUE
      <#elseif value.type == "Long" || value.type == "long">
         value != Long.MIN_VALUE
      <#elseif value.type == "Short" || value.type == "short">
         value != Short.MIN_VALUE
      <#elseif value.type == "Byte" || value.type == "byte">
         value != Byte.MIN_VALUE
      <#elseif value.type == "String" >
         value.length()>0
      <#else>
      <@util.require "SHOULD_RENDER_ATTRIBUTE"/>${SHOULD_RENDER_ATTRIBUTE}(value)
      </#if>
      <#if defaultValue?exists>
      )) {
         value="${defaultValue}";
      }
      ${responseWriterVariable}.writeAttribute("${attributeName}",value,null);
      <#else>
      ) {
        ${responseWriterVariable}.writeAttribute("${attributeName}",value,null);
      }
      </#if>
      
   }
</#if>
