    protected enum Properties {
        <#list generatedAttributes as attribute>${attribute.name}<#if attribute_has_next>${",\n        "}</#if></#list>
    }

    <#list generatedAttributes as attribute>
        <#include "_attribute_accessors.ftl">
    </#list>

    private StateHelper stateHelper = null;
        
    protected StateHelper getStateHelper() {
        if (stateHelper == null) {
            stateHelper = new PartialStateHolderHelper(this);
        }
        return stateHelper;
    }