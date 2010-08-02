    <#list generatedAttributes as attribute>
    private ${attribute.typeName} ${attribute.name};
    <#if (attribute.isPrimitive)>private boolean is${attribute.capitalizeName}Setted = false;</#if>
    </#list>

    <#list generatedAttributes as attribute>
    
        <#if (attribute.isPrimitive)>
            <#include "_attribute-accessor-primitive.ftl" />
        <#else>
            <#include "_attribute-accessor.ftl" />
        </#if>
    </#list>

    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private static ValueBinding getValueBinding(String name) {
        return getFacesContext().getApplication().createValueBinding(name);
    }
