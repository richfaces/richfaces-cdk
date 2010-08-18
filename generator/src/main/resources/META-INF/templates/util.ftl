<#macro concat seq delimiter=","><#list seq as item><#nested item/><#if item_has_next>${delimiter}</#if></#list></#macro>
