package org.richfaces.cdk.templatecompiler;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.templatecompiler.builder.model.Variables;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.statements.TypedTemplateStatement;

public interface ELParser {
    TypedTemplateStatement parse(String expression, Variables variables, ELType expectedType) throws ParsingException;

    TypedTemplateStatement parse(String expression, Variables variables, String expectedType) throws ParsingException;

    ELType getType(Class<?> targetClass);

    ELType getType(String classExpression);

    ELType getType(ClassName targetClass);
}
