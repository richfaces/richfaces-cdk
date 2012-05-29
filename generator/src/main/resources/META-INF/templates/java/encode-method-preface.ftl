<#if renderResponseWriter>
ResponseWriter ${responseWriterVariable} = ${facesContextVariable}.getResponseWriter();
</#if> 
String ${clientIdVariable} = ${componentVariable}.getClientId(${facesContextVariable});