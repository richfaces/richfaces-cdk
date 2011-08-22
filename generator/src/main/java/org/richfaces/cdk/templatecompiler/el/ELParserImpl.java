/**
 *
 */
package org.richfaces.cdk.templatecompiler.el;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.builder.model.Variables;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.statements.TypedTemplateStatement;

import com.google.inject.Inject;

/**
 * @author asmirnov
 *
 */
public class ELParserImpl implements ELParser {
    private final TypesFactory typesFactory;
    private final Logger log;

    @Inject
    public ELParserImpl(TypesFactory typesFactory, Logger log) {
        this.typesFactory = typesFactory;
        this.log = log;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.ELParser#parse(java.lang.String,
     * org.richfaces.cdk.templatecompiler.builder.model.Variables, org.richfaces.cdk.templatecompiler.el.ELType)
     */
    @Override
    public TypedTemplateStatement parse(String expression, Variables variables, ELType expectedType) throws ParsingException {
        ELVisitor visitor = new ELVisitor(log, typesFactory);
        visitor.parse(expression, variables, expectedType);
        return visitor;
    }

    @Override
    public TypedTemplateStatement parse(String expression, Variables variables, String expectedType) throws ParsingException {
        return parse(expression, variables, typesFactory.getType(expectedType));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.ELParser#getType(java.lang.Class)
     */
    @Override
    public ELType getType(Class<?> targetClass) {
        return typesFactory.getType(targetClass);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.ELParser#getType(java.lang.String)
     */
    @Override
    public ELType getType(String classExpression) {
        return typesFactory.getType(classExpression);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.ELParser#getType(org.richfaces.cdk.model.ClassName)
     */
    @Override
    public ELType getType(ClassName targetClass) {
        return typesFactory.getType(targetClass.toString());
    }
}
