/**
 * 
 */
package org.richfaces.cdk.templatecompiler.statements;

import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaStatement;
import org.richfaces.cdk.templatecompiler.builder.model.RequireImports;

/**
 * @author asmirnov
 *
 */
public interface TemplateStatement extends JavaStatement, RequireImports {
    
    
    Iterable<JavaField> getRequiredFields();

    Iterable<HelperMethod> getRequiredMethods();
    
    void setParent(StatementsContainer parent);

}
