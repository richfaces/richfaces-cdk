package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.*;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class WtiteTextTest extends FreeMarkerTestBase {

    @Inject
    private WriteTextStatement statement;
    
    @Test
    public void testWriteLiteral() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, TypesFactory.OBJECT_TYPE)).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("\""+HTTP_EXAMPLE_COM+"\"");
        expect(parsedExpression.isLiteral()).andStubReturn(true);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.STRING_TYPE);
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setExpression(HTTP_EXAMPLE_COM);
        String code = statement.getCode();
        controller.verify();
        verifyCode(code, "\""+HTTP_EXAMPLE_COM+"\"","writeText","!if");
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
        statement.setExpression(HTTP_EXAMPLE_COM);
        String code = statement.getCode();
        verifyCode(code, "!\""+HTTP_EXAMPLE_COM+"\"","writeText","if");
        controller.verify();
    }

}
