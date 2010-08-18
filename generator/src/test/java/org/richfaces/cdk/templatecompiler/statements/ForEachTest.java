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
public class ForEachTest extends FreeMarkerTestBase {

    @Mock
    private TypesFactory typesFactory;
    
    @Inject
    private ForEachStatement statement;
    
    
    @Test
    public void testSetItemsExpression() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, Iterable.class.getName())).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get("+HTTP_EXAMPLE_COM+")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.OBJECT_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        parsedExpression.setParent(statement);expectLastCall();
        controller.replay();
        statement.setItemsExpression(HTTP_EXAMPLE_COM,"foo");
        String code = statement.getCode();
        verifyCode(code, HTTP_EXAMPLE_COM,"for");
        controller.verify();
    }

}
