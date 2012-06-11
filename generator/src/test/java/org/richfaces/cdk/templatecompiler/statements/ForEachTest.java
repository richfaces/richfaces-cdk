package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class ForEachTest extends FreeMarkerTestBase {
    @Mock
    private TypesFactory typesFactory;
    @Mock
    private ELType arbitraryType;
    @Inject
    private ForEachStatement statement;

    @Test
    public void testSetItemsExpression() throws Exception {
        expect(parser.parse(HTTP_EXAMPLE_COM, statement, Object.class.getName())).andReturn(parsedExpression);
        expect(parsedExpression.getCode()).andStubReturn("get(" + HTTP_EXAMPLE_COM + ")");
        expect(parsedExpression.isLiteral()).andStubReturn(false);
        expect(parsedExpression.getType()).andStubReturn(TypesFactory.OBJECT_TYPE);
        expect(parsedExpression.getRequiredMethods()).andStubReturn(Collections.<HelperMethod>emptySet());
        parsedExpression.setParent(statement);
        expect(typesFactory.getType(Iterable.class)).andStubReturn(arbitraryType);
        expect(typesFactory.getType(Iterator.class)).andStubReturn(arbitraryType);
        expect(arbitraryType.isAssignableFrom(TypesFactory.OBJECT_TYPE)).andStubReturn(true);
        expectLastCall();
        controller.replay();
        statement.setItemsExpression(HTTP_EXAMPLE_COM, "foo", null, null, null, null);
        String code = statement.getCode();
        verifyCode(code, HTTP_EXAMPLE_COM, "for");
        controller.verify();
    }
}
