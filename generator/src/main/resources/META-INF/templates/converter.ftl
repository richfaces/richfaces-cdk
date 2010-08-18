<#include "_copyright.ftl">

package ${targetClass.package};

import javax.annotation.Generated;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.richfaces.PartialStateHolderHelper;
<#include "_attributes_import.ftl">

/**
 * ${description?if_exists}
 **/
@Generated({"RichFaces CDK", "4.0.0-SNAPSHOT"})
public class ${targetClass.simpleName} extends ${baseClass} implements Converter, PartialStateHolder {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return null;
    }

    <#include "_attributes.ftl">

    <#include "_state-holder-methods.ftl">
}