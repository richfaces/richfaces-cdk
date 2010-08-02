package org.richfaces.cdk.templatecompiler.statements;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;

import com.google.common.collect.Iterables;


@RunWith(CdkTestRunner.class)
public class FreeMarkerTemplateStatementTest1 extends FreeMarkerTestBase {

    private static final String FOO_CODE = "private static final String foo;";
    
    public FreeMarkerTemplateStatementBase setUpStatement(String template) {
        FreeMarkerTemplateStatementBase freeMarkerStatement = new FreeMarkerTemplateStatementBase(renderer, template);
        return freeMarkerStatement;
    }

 
    @Test
    public void testGetCode() {
        FreeMarkerTemplateStatementBase statement = setUpStatement("foo");
        controller.replay();
        String code = statement.getCode();
        controller.verify();
        assertEquals(FOO_CODE, code);
    }

    @Test
    public void testAddConstant() {
        FreeMarkerTemplateStatementBase statement = setUpStatement("addConstant");
        JavaField javaField = Iterables.getOnlyElement(statement.getRequiredFields());
        assertEquals("foo", javaField.getName());
        assertEquals("List<String>", javaField.getType().getCode());
        assertNotNull(javaField.getValue());
        assertEquals(FOO_CODE, javaField.getValue().getCode());
        assertEquals("",statement.getCode());
    }

    @Test
    public void testAddImport() {
        FreeMarkerTemplateStatementBase statement = setUpStatement("addImport");
        JavaImport javaImport = Iterables.getOnlyElement(statement.getRequiredImports());
        assertEquals("java.util.List",javaImport.getName());
        assertEquals("",statement.getCode());
    }

    @Test
    public void testAddRequiredMethod() {
        FreeMarkerTemplateStatementBase statement = setUpStatement("addMethod");
        HelperMethod helperMethod = Iterables.getOnlyElement(statement.getRequiredMethods());
        assertEquals(HelperMethod.EMPTINESS_CHECK, helperMethod);
        assertEquals("",statement.getCode());
    }

}
