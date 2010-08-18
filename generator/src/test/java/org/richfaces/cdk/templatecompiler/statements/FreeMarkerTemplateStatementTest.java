package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.MockController;
import org.richfaces.cdk.Stub;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.builder.model.RuntimeImport;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(CdkTestRunner.class)
public class FreeMarkerTemplateStatementTest {

    private static final String FOO_CODE = "private static final String foo;";

    @Mock
    private FreeMarkerRenderer renderer;

    @Stub
    private TemplateStatement statement;

    @Stub
    @Named("second")
    private TemplateStatement statement1;

    @Inject
    private MockController controller;

    private FreeMarkerTemplateStatementBase freeMarkerStatement;

    @Before
    public void setUp() {
        freeMarkerStatement = new FreeMarkerTemplateStatementBase(renderer, "foo");
    }

    public void tearDown() {
        freeMarkerStatement = null;
    }

    @Test
    public void testGetCode() {
        expect(renderer.renderTemplate("foo.ftl", freeMarkerStatement)).andReturn(FOO_CODE);
        controller.replay();
        String code = freeMarkerStatement.getCode();
        controller.verify();
        assertEquals(FOO_CODE, code);
    }

    @Test
    public void testGetRequiredImports() {
        freeMarkerStatement.addImport("foo.Bar");
        expect(statement.getRequiredImports()).andStubReturn(
            Collections.<JavaImport> singleton(new RuntimeImport("foo.baz")));
        expect(renderer.renderTemplate("foo.ftl", freeMarkerStatement)).andReturn(FOO_CODE);
        controller.replay();
        freeMarkerStatement.addStatement(statement);
        Iterable<JavaImport> requiredImports = freeMarkerStatement.getRequiredImports();
        assertEquals(2, Iterables.size(requiredImports));
        controller.verify();
    }

    @Test
    public void testGetRequiredFields() {
        freeMarkerStatement.addConstant("boolean", "foo", FOO_CODE);
        expect(statement.getRequiredFields()).andStubReturn(
            Collections.<JavaField> singleton(new JavaField(TypesFactory.INT_TYPE, "bar")));
        expect(renderer.renderTemplate("foo.ftl", freeMarkerStatement)).andReturn(FOO_CODE);
        controller.replay();
        freeMarkerStatement.addStatement(statement);
        Iterable<JavaField> requiredFields = freeMarkerStatement.getRequiredFields();
        assertEquals(2, Iterables.size(requiredFields));
        controller.verify();
    }

    @Test
    public void testGetRequiredMethods() {
        freeMarkerStatement.addRequiredMethod(HelperMethod.EMPTINESS_CHECK.toString());
        expect(renderer.renderTemplate("foo.ftl", freeMarkerStatement)).andReturn(FOO_CODE);
        expect(statement.getRequiredMethods()).andStubReturn(
            Collections.<HelperMethod> singleton(HelperMethod.EQUALS_CHECK));
        controller.replay();
        freeMarkerStatement.addStatement(statement);
        Iterable<HelperMethod> requiredMethods = freeMarkerStatement.getRequiredMethods();
        assertEquals(2, Iterables.size(requiredMethods));
        controller.verify();
    }

    @Test
    public void testAddConstant() {
        freeMarkerStatement.addConstant("java.util.List<java.lang.String>", "foo", FOO_CODE);
        JavaField javaField = Iterables.getOnlyElement(freeMarkerStatement.getRequiredFields());
        assertEquals("foo", javaField.getName());
        assertEquals("java.util.List<java.lang.String>", javaField.getType().getCode());
        assertEquals(FOO_CODE, javaField.getValue().getCode());
    }

    @Test
    public void testAddImport() {
        freeMarkerStatement.addImport("java.util.List");
        JavaImport javaImport = Iterables.getOnlyElement(freeMarkerStatement.getRequiredImports());
        assertEquals("java.util.List",javaImport.getName());
    }

    @Test
    public void testAddRequiredMethod() {
        freeMarkerStatement.addRequiredMethod(HelperMethod.EMPTINESS_CHECK.toString());
        HelperMethod helperMethod = Iterables.getOnlyElement(freeMarkerStatement.getRequiredMethods());
        assertEquals(HelperMethod.EMPTINESS_CHECK, helperMethod);
    }

}
