/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.cdk.templatecompiler.el;

import static org.richfaces.cdk.templatecompiler.statements.HelperMethod.TO_STRING_CONVERSION;
import static org.richfaces.cdk.util.JavaUtils.getEscapedString;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.jboss.el.parser.AstAnd;
import org.jboss.el.parser.AstBracketSuffix;
import org.jboss.el.parser.AstChoice;
import org.jboss.el.parser.AstCompositeExpression;
import org.jboss.el.parser.AstDeferredExpression;
import org.jboss.el.parser.AstDiv;
import org.jboss.el.parser.AstDynamicExpression;
import org.jboss.el.parser.AstEmpty;
import org.jboss.el.parser.AstEqual;
import org.jboss.el.parser.AstFalse;
import org.jboss.el.parser.AstFloatingPoint;
import org.jboss.el.parser.AstFunction;
import org.jboss.el.parser.AstGreaterThan;
import org.jboss.el.parser.AstGreaterThanEqual;
import org.jboss.el.parser.AstIdentifier;
import org.jboss.el.parser.AstInteger;
import org.jboss.el.parser.AstLessThan;
import org.jboss.el.parser.AstLessThanEqual;
import org.jboss.el.parser.AstLiteralExpression;
import org.jboss.el.parser.AstMethodSuffix;
import org.jboss.el.parser.AstMinus;
import org.jboss.el.parser.AstMod;
import org.jboss.el.parser.AstMult;
import org.jboss.el.parser.AstNegative;
import org.jboss.el.parser.AstNot;
import org.jboss.el.parser.AstNotEqual;
import org.jboss.el.parser.AstNull;
import org.jboss.el.parser.AstOr;
import org.jboss.el.parser.AstPlus;
import org.jboss.el.parser.AstPropertySuffix;
import org.jboss.el.parser.AstString;
import org.jboss.el.parser.AstTrue;
import org.jboss.el.parser.AstValue;
import org.jboss.el.parser.ELParser;
import org.jboss.el.parser.Node;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.builder.model.Variables;
import org.richfaces.cdk.templatecompiler.el.node.AstBracketSuffixTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstChoiceTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstCompositeComponentAttributesTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstCompositeComponentTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstDeferredOrDynamicExpressionTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstEmptyTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstFloatingPointTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstFunctionTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstIdentifierTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstIntegerTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstLiteralTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstMethodSuffixTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstNegativeTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstNotTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstPropertySuffixTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstStringTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.AstValueTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.BinaryArithmeticIntegerOperationTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.BinaryArithmeticOperationTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.BinaryBooleanOperationTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.BinaryBooleanResultOperationTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.ConstantValueTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.EqualityTestTreeNode;
import org.richfaces.cdk.templatecompiler.el.node.ITreeNode;
import org.richfaces.cdk.templatecompiler.el.types.ELPropertyDescriptor;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.statements.HelperMethod;
import org.richfaces.cdk.templatecompiler.statements.StatementsContainer;
import org.richfaces.cdk.templatecompiler.statements.TypedTemplateStatement;

/**
 * Entry point for parsing EL expressions. @see parse() method.
 *
 * @author amarkhel
 */
public final class ELVisitor implements TypedTemplateStatement {
    private String parsedExpression = null;
    private ELType expressionType = null;
    private Variables variables = null;
    private Set<HelperMethod> usedHelperMethods = EnumSet.noneOf(HelperMethod.class);
    private final Logger log;
    private final TypesFactory typesFactory;
    private boolean mixedExpression;
    private boolean literal = true;
    private StatementsContainer parent;

    public ELVisitor(Logger log, TypesFactory typesFactory) {
        this.log = log;
        this.typesFactory = typesFactory;
    }

    public boolean isMixedExpression() {
        return mixedExpression;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the literal
     */
    @Override
    public boolean isLiteral() {
        return this.literal;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param literal the literal to set
     */
    public void setLiteral(boolean literal) {
        this.literal = literal;
    }

    public ELType getType() {
        return expressionType;
    }

    public void setExpressionType(ELType variableType) {
        this.expressionType = variableType;
    }

    public ELType getVariable(String name) throws ParsingException {
        ELType variableType;
        if (variables.isDefined(name)) {
            variableType = variables.getVariable(name);
        } else {
            log.warn(MessageFormat.format("No type found in context for identifier ''{0}'', handling as generic Object", name));
            variableType = TypesFactory.OBJECT_TYPE;
        }

        return variableType;
    }

    /**
     * Parse specified EL expression and return Java code, that represent this expression
     *
     * @param expression - expression to resolve
     * @param contextMap - Map<String, Class<?>> - context for search classes.
     * @return generated Java code.
     * @throws ParsingException - if error occurred during parsing.
     */
    public void parse(String expression, Variables contextVariables, ELType expectedType) throws ParsingException {
        reset();

        Node ret = ELParser.parse(expression);
        variables = contextVariables;

        if (ret instanceof AstCompositeExpression && ret.jjtGetNumChildren() >= 2) {
            // AstCompositeExpression with 2+ children is a mixed expression
            usedHelperMethods.add(TO_STRING_CONVERSION);
            this.mixedExpression = true;
            this.literal = false;
        }

        if (ret != null && ret.jjtGetNumChildren() > 0) {
            parsedExpression = this.visit(ret);
        } else {
            parsedExpression = getEscapedString("");
            expressionType = TypesFactory.STRING_TYPE;
        }

        parsedExpression = coerceToType(parsedExpression, expectedType);
    }

    public String coerceToType(String valueString, ELType expectedType) {
        if (!expectedType.isAssignableFrom(getType())) {
            for (HelperMethod conversionMethod : HelperMethod.getConversionMethods()) {
                ELType returnType = typesFactory.getType(conversionMethod.getReturnType());
                if (expectedType.isAssignableFrom(returnType)) {
                    usedHelperMethods.add(conversionMethod);
                    setExpressionType(returnType);
                    return conversionMethod.getName() + "(" + valueString + ")";
                }
            }
            setLiteral(false);
        }

        return valueString;
    }

    private String visit(Node node) throws ParsingException {
        int numChildren = node.jjtGetNumChildren();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numChildren; i++) {
            Node child = node.jjtGetChild(i);

            ITreeNode treeNode = determineNodeType(child);

            treeNode.visit(sb, this);

            if (i != numChildren - 1) {
                sb.append(" + ");
            }
        }

        return sb.toString();
    }

    private void reset() {
        parsedExpression = null;
        usedHelperMethods.clear();
        variables = null;
        expressionType = null;
    }

    @Override
    public String getCode() {
        return parsedExpression;
    }

    @Override
    public Iterable<JavaImport> getRequiredImports() {
        return getType().getRequiredImports();
    }

    @Override
    public Iterable<JavaField> getRequiredFields() {
        return Collections.emptySet();
    }

    @Override
    public Iterable<HelperMethod> getRequiredMethods() {
        return usedHelperMethods;
    }

    public ELType getMatchingVisibleMethodReturnType(String methodName, ELType[] parameterTypes) throws ParsingException {

        return typesFactory.getMatchingVisibleMethodReturnType(getType(), methodName, parameterTypes);
    }

    public ELPropertyDescriptor getPropertyDescriptor(String propertyName) throws ParsingException {
        return typesFactory.getPropertyDescriptor(getType(), propertyName);
    }

    public void addHelperMethods(HelperMethod helper) {
        usedHelperMethods.add(helper);
    }

    /**
     * This method determine type of parsed node and create wrapper for them, that extends AbstractTreeNode. If node type is not
     * recognized - throws ParsingException.
     *
     * @param child - parsed node
     * @throws ParsingException - if node type is not recognized.
     * @return wrapper for parsed node(if node type is recognized), that implement ITreeNode interface.
     */
    public ITreeNode determineNodeType(Node child) throws ParsingException {
        ITreeNode treeNode = null;

        if (child instanceof AstIdentifier) {
            if (isCompositeComponent(child)) {
                treeNode = new AstCompositeComponentTreeNode(child);
            } else {
                treeNode = new AstIdentifierTreeNode(child);
            }
        } else if (child instanceof AstValue) {
            treeNode = new AstValueTreeNode(child);
        } else if (child instanceof AstInteger) {
            treeNode = new AstIntegerTreeNode(child);
        } else if (child instanceof AstString) {
            treeNode = new AstStringTreeNode(child);
        } else if (child instanceof AstFunction) {
            treeNode = new AstFunctionTreeNode(child);
        } else if (child instanceof AstDeferredExpression || child instanceof AstDynamicExpression) {
            treeNode = new AstDeferredOrDynamicExpressionTreeNode(child);
        } else if (child instanceof AstNot) {
            treeNode = new AstNotTreeNode(child);
        } else if (child instanceof AstChoice) {
            treeNode = new AstChoiceTreeNode(child);
        } else if (child instanceof AstEmpty) {
            treeNode = new AstEmptyTreeNode(child);
        } else if (child instanceof AstLiteralExpression) {
            treeNode = new AstLiteralTreeNode(child);
        } else if (child instanceof AstFalse) {
            treeNode = ConstantValueTreeNode.FALSE_NODE;
        } else if (child instanceof AstTrue) {
            treeNode = ConstantValueTreeNode.TRUE_NODE;
        } else if (child instanceof AstNull) {
            treeNode = ConstantValueTreeNode.NULL_NODE;
        } else if (child instanceof AstAnd) {
            treeNode = new BinaryBooleanOperationTreeNode(child, ELNodeConstants.AND_OPERATOR);
        } else if (child instanceof AstEqual) {
            treeNode = new EqualityTestTreeNode(child);
        } else if (child instanceof AstGreaterThan) {
            treeNode = new BinaryBooleanResultOperationTreeNode(child, ELNodeConstants.GREATER_THEN_OPERATOR);
        } else if (child instanceof AstGreaterThanEqual) {
            treeNode = new BinaryBooleanResultOperationTreeNode(child, ELNodeConstants.GREATER_THEN_OR_EQUALITY_OPERATOR);
        } else if (child instanceof AstLessThan) {
            treeNode = new BinaryBooleanResultOperationTreeNode(child, ELNodeConstants.LESS_THEN_OPERATOR);
        } else if (child instanceof AstLessThanEqual) {
            treeNode = new BinaryBooleanResultOperationTreeNode(child, ELNodeConstants.LESS_THEN_OR_EQUALITY_OPERATOR);
        } else if (child instanceof AstNotEqual) {
            treeNode = new EqualityTestTreeNode(child, true);
        } else if (child instanceof AstOr) {
            treeNode = new BinaryBooleanOperationTreeNode(child, ELNodeConstants.OR_OPERATOR);
        } else if (child instanceof AstDiv) {
            treeNode = new BinaryArithmeticOperationTreeNode(child, ELNodeConstants.DIV_OPERATOR);
        } else if (child instanceof AstMult) {
            treeNode = new BinaryArithmeticOperationTreeNode(child, ELNodeConstants.MULT_OPERATOR);
        } else if (child instanceof AstMod) {
            treeNode = new BinaryArithmeticIntegerOperationTreeNode(child, ELNodeConstants.MOD_OPERATOR);
        } else if (child instanceof AstPlus) {
            treeNode = new BinaryArithmeticOperationTreeNode(child, ELNodeConstants.PLUS_OPERATOR);
        } else if (child instanceof AstMinus) {
            treeNode = new BinaryArithmeticOperationTreeNode(child, ELNodeConstants.MINUS_OPERATOR);
        } else if (child instanceof AstBracketSuffix) {
            treeNode = new AstBracketSuffixTreeNode(child);
        } else if (child instanceof AstNegative) {
            treeNode = new AstNegativeTreeNode(child);
        } else if (child instanceof AstFloatingPoint) {
            treeNode = new AstFloatingPointTreeNode(child);
        } else if (child instanceof AstMethodSuffix) {
            treeNode = new AstMethodSuffixTreeNode(child);
        } else if (child instanceof AstPropertySuffix) {
            if (isCompositeComponentAttributesMap(child)) {
                treeNode = new AstCompositeComponentAttributesTreeNode(child);
            } else if (isCompositeComponentAttribute(child)) {
                treeNode = new AstIdentifierTreeNode(child);
            } else {
                treeNode = new AstPropertySuffixTreeNode(child);
            }
        } else {
            throw new ParsingException("Node " + child.getClass().getSimpleName() + "[" + child.getImage()
                    + "] is not recognized;");
        }

        return treeNode;
    }

    private boolean isCompositeComponent(Node node) {
        return "cc".equals(node.getImage());
    }

    private boolean isCompositeComponentAttributesMap(Node node) {
        return "attrs".equals(node.getImage()) && isCompositeComponent(node.jjtGetParent().jjtGetChild(0));
    }

    private boolean isCompositeComponentAttribute(Node node) {
        return isCompositeComponentAttributesMap(node.jjtGetParent().jjtGetChild(1))
                && node == node.jjtGetParent().jjtGetChild(2);
    }

    @Override
    public void setParent(StatementsContainer parent) {
        this.parent = parent;
    }
}
