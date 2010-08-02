package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Collections;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class WriteAttributeTest extends FreeMarkerTestBase {
    @Inject
    private WriteAttributeStatement statement;
    
    @Test
    public void testWriteLiteral() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("\""+HTTP_EXAMPLE_COM+"\"");
        expect(parsedExpression.isLiteral()).andStubReturn(true);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.STRING_TYPE);
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setAttribute(QName.valueOf("href"), HTTP_EXAMPLE_COM,null);
        String code = statement.getCode();
        controller.verify();
        verifyCode(code, "\""+HTTP_EXAMPLE_COM+"\"","writeAttribute","!if(");
    }
    
    @Test
    public void testWriteExpression() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get("+HTTP_EXAMPLE_COM+")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.OBJECT_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setAttribute(QName.valueOf("href"), HTTP_EXAMPLE_COM,null);
        String code = statement.getCode();
        verifyCode(code, "!\""+HTTP_EXAMPLE_COM+"\"","writeAttribute","if(",HelperMethod.SHOULD_RENDER_ATTRIBUTE.getName());
        verifyHelpers(statement, HelperMethod.SHOULD_RENDER_ATTRIBUTE);
        controller.verify();
    }

    @Test
    public void testIntegerExpression() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get("+HTTP_EXAMPLE_COM+")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.INTEGER_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setAttribute(QName.valueOf("href"), HTTP_EXAMPLE_COM,null);
        String code = statement.getCode();
        verifyCode(code, "!\""+HTTP_EXAMPLE_COM+"\"","writeAttribute","if(","Integer.MIN_VALUE");
        controller.verify();
    }
    @Test
    public void testURIExpression() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get("+HTTP_EXAMPLE_COM+")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.STRING_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setUriAttribute(QName.valueOf("href"), HTTP_EXAMPLE_COM,null);
        String code = statement.getCode();
        verifyCode(code, "!\""+HTTP_EXAMPLE_COM+"\"","writeURIAttribute","if(","!toString()",".length()");
        controller.verify();
    }
}
