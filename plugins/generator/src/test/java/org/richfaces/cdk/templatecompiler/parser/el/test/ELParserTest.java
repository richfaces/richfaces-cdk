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

package org.richfaces.cdk.templatecompiler.parser.el.test;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.richfaces.cdk.templatecompiler.statements.HelperMethod.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.As;
import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.MockController;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.builder.model.Variables;
import org.richfaces.cdk.templatecompiler.el.ELParserImpl;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactoryImpl;
import org.richfaces.cdk.templatecompiler.statements.HelperMethod;
import org.richfaces.cdk.templatecompiler.statements.TypedTemplateStatement;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class ELParserTest extends CdkTestBase {

    @Inject
    @As(ELParserImpl.class)
    private ELParser parser;

    @Mock
    private Logger log;

    @Inject
    @As(TypesFactoryImpl.class)
    private TypesFactory typesFactory;

    @Inject
    private MockController controller;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(CdkClassLoader.class).toInstance(createClassLoader());
    }

    private TypedTemplateStatement parseExpression(String expression) throws ParsingException {
        return parseExpression(expression, Object.class);
    }

    private TypedTemplateStatement parseExpression(String expression, String expected, HelperMethod... requiredMethods)
        throws ParsingException {
        return parseExpression(expression, Object.class, expected, requiredMethods);
    }

    private TypedTemplateStatement parseExpression(String expression, String expected, Class<?> expectedType,
        HelperMethod... requiredMethods) throws ParsingException {
        return parseExpression(expression, Object.class, expected, expectedType, requiredMethods);
    }

    private TypedTemplateStatement parseExpression(String expression, Class<?> returnType, String expected,
        Class<?> expectedType, HelperMethod... requiredMethods) throws ParsingException {
        TypedTemplateStatement statement = parseExpression(expression, returnType, expected, requiredMethods);
        assertEquals(expectedType.getName(), statement.getType().getRawName());
        return statement;
    }

    private TypedTemplateStatement parseExpression(String expression, Class<?> returnType, String expected,
        HelperMethod... requiredMethods) throws ParsingException {
        controller.replay();
        TypedTemplateStatement parseExpression = parseExpression(expression, returnType);
        controller.verify();
        assertEquals(expected, parseExpression.getCode());
        for (HelperMethod helperMethod : requiredMethods) {
            assertTrue("Expect helper method " + helperMethod.getName(),
                Iterables.contains(parseExpression.getRequiredMethods(), helperMethod));
        }
        for (HelperMethod helperMethod : parseExpression.getRequiredMethods()) {
            assertTrue("Unexpected helper method " + helperMethod.getName(),
                Iterables.contains(ImmutableSet.of(requiredMethods), helperMethod));
        }
        return parseExpression;
    }

    private TypedTemplateStatement parseExpression(String expression, Class<?> returnType) throws ParsingException {
        final Map<String, ELType> contextMap = new HashMap<String, ELType>();

        Variables variables = new Variables() {

            @Override
            public ELType getVariable(String name) throws ParsingException {
                return contextMap.get(name);
            }

            @Override
            public boolean isDefined(String name) throws ParsingException {
                return contextMap.containsKey(name);
            }

            @Override
            public ELType setVariable(String name, ELType type) throws ParsingException {
                return contextMap.put(name, type);
            }

        };
        contextMap.put("action", this.getType(org.richfaces.cdk.templatecompiler.parser.el.test.Bean.class));
        contextMap.put("clientId", this.getType(String.class));
        contextMap.put("test", this.getType(boolean.class));
        contextMap.put("otherTest", this.getType(boolean.class));
        contextMap.put("this", this.getType(Object.class));
        contextMap.put("super", this.getType(Object.class));
        contextMap.put("objectVar", this.getType(Object.class));

        return parser.parse(expression, variables, this.getType(returnType));
    }

    private ELType getType(Class<?> returnType) {
        return typesFactory.getType(returnType);
    }

    @Test
    public void testAnd() throws Exception {
        parseExpression("#{test and otherTest}", "(test && otherTest)");
    }

    @Test
    public void testAnd1() throws Exception {
        parseExpression("#{otherTest && test}", "(otherTest && test)");
        // assertEquals(Boolean.TYPE, visitor.getExpressionType().getRawType());
    }

    @Test
    public void testAnd2() throws Exception {
        parseExpression("#{action and otherTest}", "(convertToBoolean(action) && otherTest)", TO_BOOLEAN_CONVERSION);
    }

    @Test
    public void testAnd3() throws Exception {
        parseExpression("#{test && action}", "(test && convertToBoolean(action))", TO_BOOLEAN_CONVERSION);
        // assertEquals(Boolean.TYPE, visitor.getExpressionType().getRawType());
    }

    @Test
    public void testBooleanReturnType() throws Exception {
        parseExpression("#{clientId}", Boolean.TYPE, "convertToBoolean(clientId)", Boolean.TYPE, TO_BOOLEAN_CONVERSION);
    }

    @Test
    public void testBooleanReturnType1() throws Exception {
        parseExpression("#{test}", Boolean.TYPE, "test", Boolean.TYPE);
    }

    @Test
    public void testChoice() throws Exception {
        parseExpression("#{test ? 2 : 3}", Object.class, "(test ? 2 : 3)", Integer.TYPE);
    }

    @Test
    public void testChoice1() throws Exception {
        parseExpression("#{test ? null : 'string'}", Object.class, "(test ? null : \"string\")", String.class);
    }

    @Test
    public void testChoice2() throws Exception {
        parseExpression("#{action ? null : 'string'}", Object.class, "(convertToBoolean(action) ? null : \"string\")",
            TO_BOOLEAN_CONVERSION);
    }

    @Test
    public void testDiv() throws Exception {
        parseExpression("#{1/2}", "(1 / 2)", Integer.TYPE);
    }

    @Test
    public void testEmpty() throws Exception {
        parseExpression("#{empty action.array}", "isEmpty(action.getArray())", Boolean.TYPE, EMPTINESS_CHECK);
    }

    @Test
    public void testEmptyString() throws Exception {
        parseExpression("", "\"\"", String.class);
    }

    @Test
    public void testEquals() throws Exception {
        parseExpression("#{1 eq 2}", "(1 == 2)", Boolean.TYPE);
    }

    @Test
    public void testEquals1() throws Exception {
        parseExpression("#{3 == 2}", "(3 == 2)", Boolean.TYPE);
    }

    @Test
    public void testEquals2() throws Exception {
        parseExpression("#{action == 2}", "isEqual(action,2)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testEquals3() throws Exception {
        parseExpression("#{2 eq action}", "isEqual(2,action)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testEquals4() throws Exception {
        parseExpression("#{action == clientId}", "isEqual(action,clientId)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testEquals5() throws Exception {
        parseExpression("#{action eq clientId}", "isEqual(action,clientId)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testEquals6() throws Exception {
        parseExpression("#{action eq null}", "(action == null)", Boolean.TYPE);
    }

    @Test
    public void testEquals7() throws Exception {
        parseExpression("#{2 == null}", "isEqual(2,null)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testFalse() throws Exception {
        parseExpression("#{false}", "false", Boolean.TYPE);
    }

    @Test
    public void testFloat() throws Exception {
        parseExpression("#{5.0}", "Double.valueOf(5.0)", Double.TYPE);
    }

    @Test
    public void testFloat1() throws Exception {
        parseExpression("#{5.012e+34}", "Double.valueOf(5.012e+34)", Double.TYPE);
    }

    @Test
    public void testFunction() throws Exception {
        parseExpression("#{super:getType()}", "super.getType()");
    }

    @Test
    public void testGreatThen() throws Exception {
        parseExpression("#{1 gt 2}", "(1 > 2)", Boolean.TYPE);
    }

    @Test
    public void testGreatThen1() throws Exception {
        parseExpression("#{3 > 2}", "(3 > 2)", Boolean.TYPE);
    }

    @Test
    public void testGreatThenEquals() throws Exception {
        parseExpression("#{1 ge 2}", "(1 >= 2)", Boolean.TYPE);
    }

    @Test
    public void testGreatThenEquals1() throws Exception {
        parseExpression("#{3 >= 2}", "(3 >= 2)", Boolean.TYPE);
    }

    @Test
    public void testIdentifier() throws Exception {
        parseExpression("#{clientId}", "clientId", String.class);
    }

    @Test
    public void testInteger() throws Exception {
        parseExpression("#{152}", "152", Integer.TYPE);
    }

    @Test
    public void testLessThen() throws Exception {
        parseExpression("#{1 lt 2}", "(1 < 2)", Boolean.TYPE);
    }

    @Test
    public void testLessThen1() throws Exception {

        parseExpression("#{3 < 2}", "(3 < 2)", Boolean.TYPE);
    }

    @Test
    public void testLessThenEquals() throws Exception {
        parseExpression("#{1 le 2}", "(1 <= 2)", Boolean.TYPE);
    }

    @Test
    public void testLessThenEquals1() throws Exception {

        parseExpression("#{3 <= 2}", "(3 <= 2)", Boolean.TYPE);
    }

    @Test
    public void testLiteral() throws Exception {
        parseExpression("clientId", "\"clientId\"", String.class);
    }

    @Test
    public void testLiteralWithDeferred() throws Exception {
        parseExpression("#{1}#{2}", "convertToString(1) + convertToString(2)", String.class,
            HelperMethod.TO_STRING_CONVERSION);
    }

    @Test
    public void testLiteralWithDeferred1() throws Exception {
        parseExpression("abs #{getType()}", "\"abs \" + convertToString(this.getType())", String.class,
            TO_STRING_CONVERSION);
    }

    @Test
    public void testLiteralWithDeferred2() throws Exception {
        parseExpression("#{getType()} abs ", "convertToString(this.getType()) + \" abs \"", String.class,
            TO_STRING_CONVERSION);
    }

    @Test
    public void testMethod() throws Exception {
        parseExpression("#{action.readOnly}", "action.isReadOnly()", Boolean.TYPE);
    }

    @Test
    public void testMethodReturnArray() throws Exception {
        parseExpression("#{action.array}", "action.getArray()"/*, UIComponent[].class*/);
    }

    @Test
    public void testMethodReturnArrayElement() throws Exception {
        parseExpression("#{action.array[0]}", "action.getArray()[0]", UIComponent.class);
    }

    @Test
    public void testMethodReturnList() throws Exception {
        TypedTemplateStatement statement =
            parseExpression("#{action.components}", "action.getComponents()", List.class);
        ELType variableType = statement.getType();
        assertEquals(List.class.getName(), variableType.getRawName());
        assertEquals(UIComponent.class.getName(), variableType.getContainerType().getRawName());
    }

    @Test
    public void testMethodReturnListElement() throws Exception {
        parseExpression("#{action.components[0]}", "action.getComponents().get(0)", UIComponent.class);
    }

    @Test
    public void testMethodReturnListElement2() throws Exception {
        parseExpression("#{action.components[0].rendered}", "action.getComponents().get(0).isRendered()", Boolean.TYPE);
    }

    // @Test
    // public void testMethodReturnMapElement1() throws Exception {
    // assertEquals("action.getFacets().get(\"header\")", resolveExpression("#{action.facets.header}"));
    // }

    @Test
    public void testMethodReturnMap() throws Exception {
        TypedTemplateStatement statement = parseExpression("#{action.facets}", "action.getFacets()", Map.class);
        ELType variableType = statement.getType();
        assertEquals(Map.class.getName(), variableType.getRawName());
        assertEquals(UIComponent.class.getName(), variableType.getContainerType().getRawName());
    }

    @Test
    public void testMethodReturnMap1() throws Exception {
        parseExpression("#{action.rawMap}", "action.getRawMap()", Map.class);
    }

    @Test
    public void testMethodReturnMapElement() throws Exception {
        parseExpression("#{action.getFacet('header')}", "action.getFacet(\"header\")", UIComponent.class);
    }

    @Test
    public void testMethodReturnMapElement1() throws Exception {

        parseExpression("#{action.facets['header']}", "action.getFacets().get(\"header\")", UIComponent.class);
    }

    @Test
    public void testMethodReturnMapElement2() throws Exception {

        parseExpression("#{action.rawMap['something']}", "action.getRawMap().get(\"something\")", Object.class);
    }

    @Test
    public void testMethodReturnMapElement3() throws Exception {
        parseExpression("#{action.facets.toString()}", "action.getFacets().toString()", String.class);
    }

    @Test
    public void testMethodReturnMapElement4() throws Exception {
        // assertEquals("action.getFacet(\"header\").isRendered()",
        // resolveExpression("#{action.getFacet('header').rendered}"));
        parseExpression("#{action.facets['header'].rendered}", "action.getFacets().get(\"header\").isRendered()",
            Boolean.TYPE);
    }

    @Test
    public void testMethodWithParam() throws Exception {
        parseExpression("#{getType(action.array[0].rendered, action.readOnly, true)}",
            "this.getType(action.getArray()[0].isRendered(),action.isReadOnly(),true)");
    }

    @Test
    public void testMethodWithParam1() throws Exception {
        parseExpression("#{action.count(123)}", "action.count(123)", Integer.class);
    }

    @Test
    public void testMethodWithParam2() throws Exception {

        parseExpression("#{action.count(clientId)}", "action.count(clientId)", Object.class);
    }

    @Test
    public void testMinus() throws Exception {
        parseExpression("#{1-2}", "(1 - 2)", Integer.TYPE);
    }

    @Test
    public void testMod() throws Exception {
        parseExpression("#{1%2}", "(1 % 2)", Integer.TYPE);
    }

    @Test
    public void testMult() throws Exception {
        parseExpression("#{1*2}", "(1 * 2)", Integer.TYPE);
    }

    @Test
    public void testNegative() throws Exception {
        parseExpression("#{-5}", "-5", Integer.TYPE);
    }

    @Test
    public void testNegativeFloat() throws Exception {
        parseExpression("#{-5.0}", "-Double.valueOf(5.0)", Double.TYPE);
    }

    @Test
    public void testNestedMethod() throws Exception {
        parseExpression("#{action.testBean2.string}", "action.getTestBean2().getString()", String.class);
    }

    @Test
    public void testNonExistingMethod() throws Exception {
        parseExpression("#{action.doSomething(clientId, 123)}", "action.doSomething(clientId,123)", Object.class);
    }

    @Test
    public void testNot() throws Exception {
        parseExpression("#{not test}", "(!test)", Boolean.TYPE);
    }

    @Test
    public void testNot1() throws Exception {
        parseExpression("#{!otherTest}", "(!otherTest)", Boolean.TYPE);
    }

    @Test
    public void testNot2() throws Exception {

        parseExpression("#{!action}", "(!convertToBoolean(action))", Boolean.TYPE, TO_BOOLEAN_CONVERSION);
    }

    @Test
    public void testNotEqual() throws Exception {
        parseExpression("#{1 ne 3}", "(1 != 3)", Boolean.TYPE);
    }

    @Test
    public void testNotEqual1() throws Exception {

        parseExpression("#{2 != 3}", "(2 != 3)", Boolean.TYPE);
    }

    @Test
    public void testNotEqual2() throws Exception {

        parseExpression("#{action != 2}", "!isEqual(action,2)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testNotEqual3() throws Exception {

        parseExpression("#{2 ne action}", "!isEqual(2,action)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testNotEqual4() throws Exception {

        parseExpression("#{action != clientId}", "!isEqual(action,clientId)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testNotEqual5() throws Exception {

        parseExpression("#{action ne clientId}", "!isEqual(action,clientId)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testNotEqual6() throws Exception {

        parseExpression("#{action ne null}", "(action != null)", Boolean.TYPE);
    }

    @Test
    public void testNotEqual7() throws Exception {

        parseExpression("#{2 != null}", "!isEqual(2,null)", Boolean.TYPE, EQUALS_CHECK);
    }

    @Test
    public void testNull() throws Exception {
        TypedTemplateStatement statement = parseExpression("#{null}", "null");
        assertTrue(statement.getType().isNullType());
    }

    @Test
    public void testOr() throws Exception {
        parseExpression("#{test or otherTest}", "(test || otherTest)", Boolean.TYPE);
    }

    @Test
    public void testOr1() throws Exception {
        parseExpression("#{otherTest || test}", "(otherTest || test)", Boolean.TYPE);
    }

    @Test
    public void testOr2() throws Exception {

        parseExpression("#{action or otherTest}", "(convertToBoolean(action) || otherTest)", Boolean.TYPE,
            TO_BOOLEAN_CONVERSION);
    }

    @Test
    public void testOr3() throws Exception {

        parseExpression("#{test || action}", "(test || convertToBoolean(action))", Boolean.TYPE, TO_BOOLEAN_CONVERSION);
    }

    @Test
    public void testPlus() throws Exception {
        // TODO: tests involving double values
        parseExpression("#{1+2}", "(1 + 2)", Integer.TYPE);
    }

    @Test
    public void testString() throws Exception {
        parseExpression("#{\"nabc\"}", "\"nabc\"", String.class);
    }

    @Test
    public void testString1() throws Exception {

        parseExpression("#{'nabc'}", "\"nabc\"", String.class);
    }

    @Test
    public void testString2() throws Exception {

        parseExpression("#{'\tabc'}", "\"\\tabc\"", String.class);
    }

    @Test
    public void testString3() throws Exception {

        parseExpression("#{'/nabc'}", "\"/nabc\"", String.class);
    }

    @Test
    public void testString4() throws Exception {

        parseExpression("#{'na\"bc'}", "\"na\\\"bc\"", String.class);
    }

    @Test
    public void testString5() throws Exception {

        parseExpression("#{'na\\\\bc'}", "\"na\\\\bc\"", String.class);
    }

    @Test
    public void testStringReturnType() throws Exception {
        parseExpression("#{clientId}", String.class, "clientId", String.class);
    }

    @Test
    public void testStringReturnType1() throws Exception {

        parseExpression("#{test}", String.class, "convertToString(test)", String.class, TO_STRING_CONVERSION);
    }

    @Test
    public void testThisFunction() throws Exception {
        parseExpression("#{getType()}", "this.getType()");
    }

    @Test
    public void testThisFunction1() throws Exception {

        parseExpression("#{this.getType()}", "this.getType()");
    }

    @Test
    public void testTrue() throws Exception {
        parseExpression("#{true}", "true", Boolean.TYPE);
    }

    @Test
    public void testVariableFunction() throws Exception {
        parseExpression("#{objectVar.getType()}", "objectVar.getType()");
    }

    @Test(/*expected = ParsingException.class*/)
    public void testWrongExpression() throws Exception {
        log.warn((CharSequence) anyObject());
        expectLastCall();
        parseExpression("#{bean.property}","bean.getProperty()");
    }

    @Test
    public void testWrongExpression2() throws Exception {
        parseExpression("#{action.property}", "action.getProperty()", Object.class);
    }
    
    @Test
    public void testLiteralExpression() throws Exception {
        controller.replay();
        TypedTemplateStatement parseExpression = parseExpression("Literal", Object.class);
        controller.verify();
        assertTrue(parseExpression.isLiteral());
        assertEquals("\"Literal\"",parseExpression.getCode());
        assertEquals(String.class.getName(), parseExpression.getType().getRawName());
    }
}
