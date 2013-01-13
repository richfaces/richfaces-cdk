<#include "_copyright.ftl">

package ${targetClass.package};

import javax.annotation.Generated;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.el.ValueBinding;
<#include "_attributes_import.ftl">

/**
 * ${description?if_exists}
 **/
@Generated({"RichFaces CDK", "4.3.0.CR1"})
public class ${targetClass.simpleName} extends ${baseClass} implements Behavior, PartialStateHolder
<#if (implemented?size > 0)>, <@util.concat seq=implemented ; interface>${interface.simpleName}</@util.concat></#if> {

    <#if rendererType?exists >
    public String getRendererType() {
        return "${rendererType}";
    }
    </#if>
    <#include "_attributes-old.ftl">

    <#include "_state-holder-methods-old.ftl">
}