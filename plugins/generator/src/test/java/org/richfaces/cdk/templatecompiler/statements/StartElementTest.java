package org.richfaces.cdk.templatecompiler.statements;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.inject.Inject;

@RunWith(CdkTestRunner.class)
public class StartElementTest extends FreeMarkerTestBase {

    @Mock
    private TypesFactory typesFactory;
    
    @Inject
    private StartElementStatement statement;
    
    
    @Test
    public void testStartElement() throws Exception {
        controller.replay();
        statement.setElementName(QName.valueOf("div"));
        String code = statement.getCode();
        verifyCode(code, "startElement","div");
        controller.verify();
    }

}
