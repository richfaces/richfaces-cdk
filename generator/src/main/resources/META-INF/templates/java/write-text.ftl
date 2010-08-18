<#if value.literal>
  ${responseWriterVariable}.writeText(${value},null);
<#else>
{
	Object text = ${value};
	if (text != null) {
		${responseWriterVariable}.writeText(text, null); 
	}
}
</#if>