package org.richfaces.cdk.xmlconfig;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.richfaces.cdk.CdkTestBase;

public abstract class JaxbTestBase extends CdkTestBase {
    protected static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    protected <T> String marshal(T root) {
        ByteArrayOutputStream xml = new ByteArrayOutputStream();

        JAXB.marshal(root, xml);

        return new String(xml.toByteArray());
    }

    protected <T> T unmarshal(Class<T> type, String src) {
        StringBuilder xml = new StringBuilder(XML_PROLOG);

        xml.append(src);

        StringReader reader = new StringReader(xml.toString());
        T result = JAXB.unmarshal(reader, type);

        return result;
    }
}
