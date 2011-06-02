/**
 *
 */
package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collections;

import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaStatement;
import org.richfaces.cdk.templatecompiler.builder.model.RequireImports;

import com.google.common.base.Function;

/**
 * @author asmirnov
 *
 */
public interface TemplateStatement extends JavaStatement, RequireImports {
    Function<TemplateStatement, Iterable<JavaField>> FIELDS_TRANSFORM = new Function<TemplateStatement, Iterable<JavaField>>() {
        public Iterable<JavaField> apply(TemplateStatement from) {
            if (from == null) {
                return Collections.emptyList();
            }

            return from.getRequiredFields();
        }

        ;
    };
    Function<TemplateStatement, Iterable<HelperMethod>> METHODS_TRANSFORM = new Function<TemplateStatement, Iterable<HelperMethod>>() {
        public Iterable<HelperMethod> apply(TemplateStatement from) {
            if (from == null) {
                return Collections.emptyList();
            }

            return from.getRequiredMethods();
        }

        ;
    };

    Iterable<JavaField> getRequiredFields();

    Iterable<HelperMethod> getRequiredMethods();

    void setParent(StatementsContainer parent);
}
