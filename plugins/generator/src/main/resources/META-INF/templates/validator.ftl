<#include "_copyright.ftl">

package ${targetClass.package};

import javax.annotation.Generated;
import javax.faces.component.UIComponent;
import javax.faces.component.PartialStateHolder;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.el.ValueBinding;
<#include "_attributes_import.ftl">

/**
 * ${description?if_exists}
 **/
@Generated({"RichFaces CDK", "4.0.0-SNAPSHOT"})
public class ${targetClass.simpleName} extends ${baseClass} implements Validator, PartialStateHolder {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
    }

    <#include "_attributes-old.ftl">

    <#include "_state-holder-methods-old.ftl">
}