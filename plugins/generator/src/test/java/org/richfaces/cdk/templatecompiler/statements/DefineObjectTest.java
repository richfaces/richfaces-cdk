package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.*;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class DefineObjectTest extends FreeMarkerTestBase {

    @Mock
    private TypesFactory typesFactory;
    
    @Inject
    private DefineObjectStatement statement;
    
    @Test
    public void testDefineVariable() throws Exception {
        expect(typesFactory.getType("java.lang.String")).andReturn(TypesFactory.STRING_TYPE);
        controller.replay();
        statement.setObject("foo","java.lang.String",null);
        String code = statement.getCode();
        controller.verify();
        verifyCode(code, "String"," foo","!=");
    }
    
    @Test
    public void testDefineAndInitVariable() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get("+HTTP_EXAMPLE_COM+")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.OBJECT_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        expect(typesFactory.getType("java.lang.String")).andReturn(TypesFactory.STRING_TYPE);
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setObject("foo","java.lang.String",HTTP_EXAMPLE_COM);
        String code = statement.getCode();
        verifyCode(code, HTTP_EXAMPLE_COM,"String","=");
        controller.verify();
    }
    @Test
    public void testDefineAndInitUnknownVariable() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get("+HTTP_EXAMPLE_COM+")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.INTEGER_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setObject("foo",null,HTTP_EXAMPLE_COM);
        String code = statement.getCode();
        verifyCode(code, "Integer","foo","=");
        controller.verify();
    }

}
