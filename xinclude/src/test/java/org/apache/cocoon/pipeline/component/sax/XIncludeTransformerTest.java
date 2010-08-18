
/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
 */
package org.apache.cocoon.pipeline.component.sax;

import static junit.framework.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import java.net.URL;

import java.util.Properties;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.custommonkey.xmlunit.Diff;

import org.junit.Ignore;
import org.junit.Test;

import org.richfaces.cdk.JavaLogger;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public final class XIncludeTransformerTest {
    private static final SAXTransformerFactory SAX_TRANSFORMER_FACTORY =
        (SAXTransformerFactory) TransformerFactory.newInstance();

    /**
     * A pipeline that reads from a file and perform a simple XInclude operation.
     */
    @Test
    public void testPipelineWithXInclude() throws Exception {
        internalXIncludeTest("xinclude-xml.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><x>\n  <test/>\n</x>");
    }

    /**
     * A pipeline that performs an XInclude operation, including just text.
     */
    @Test
    @Ignore
    public void testPipelineWithXIncludeText() throws Exception {
        internalXIncludeTest(
            "xinclude-text-only.xml",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><x>\n  in girum imus nocte et cosumimur igni\n</x>");
    }

    /**
     * A pipeline that performs an XInclude operation, forced to use the fallback.
     */
    @Test
    public void testPipelineWithXIncludeFallback() throws Exception {
        internalXIncludeTest(
            "xinclude-fallback.xml",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><x>\n  \n        <error>the linked document has not found</error>\n    \n</x>");
    }

    /**
     * A pipeline that performs an XInclude operation and use XPointer to extract
     * a fragment from the included document.
     */
    @Test
    public void testPipelineWithXIncludeAndXPointer() throws Exception {
        internalXIncludeTest(
            "xinclude-xpointer.xml",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><x xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n  <p>\n      <xsl:value-of select=\"$myParam\"/>\n    </p>\n</x>");
    }

    /**
     * A pipeline that performs an XInclude operation and use the deprecated
     * XPointer to extract a fragment from the included document.
     */
    @Test
    public void testPipelineWithXIncludeAndDeprecatedXPointer() throws Exception {
        internalXIncludeTest(
            "xinclude-deprecated_xpointer.xml",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><x xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n  <p>\n      <xsl:value-of select=\"$myParam\"/>\n    </p>\n</x>");
    }

    /**
     *
     */
    private void internalXIncludeTest(String testResource, String expectedDocument) throws Exception {
        URL base = getClass().getResource("/");
        URL source = new URL(base, testResource);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TransformerHandler transformerHandler = SAX_TRANSFORMER_FACTORY.newTransformerHandler();
        Properties properties = new Properties();

        properties.put("method", "xml");
        transformerHandler.getTransformer().setOutputProperties(properties);
        transformerHandler.setResult(new StreamResult(baos));

        XIncludeTransformer xinclude = new XIncludeTransformer(base.toURI(),new JavaLogger());

        xinclude.setContentHandler(transformerHandler);

        XMLReader xmlReader = XMLReaderFactory.createXMLReader();

        xmlReader.setContentHandler(xinclude);
        xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", xinclude);
        xmlReader.parse(source.toExternalForm());

        String actualDocument = new String(baos.toByteArray());
        Diff diff = new Diff(expectedDocument, actualDocument);

        assertTrue("XInclude transformation didn't work as expected " + diff, diff.identical());
    }
}
