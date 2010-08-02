/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.cdk.xmlconfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.xmlconfig.model.FacesConfigAdapter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Jan 13, 2010
 */
@RunWith(CdkTestRunner.class)
public abstract class XmlTest extends JaxbTestBase {

    @Inject
    protected Logger log;

    static {
        XMLUnit.setControlEntityResolver(new CdkEntityResolver());
    }

    protected void checkXmlStructure(StringWriter writer) throws SAXException, IOException {
        Class<?> testClass = this.getClass();
        InputStream expectedFacesConfigFile = testClass.getResourceAsStream(testClass.getSimpleName() + ".xml");
        if (expectedFacesConfigFile == null) {
            return;
        }
        XMLUnit.setNormalizeWhitespace(true);
        Diff xmlDiff = new Diff(new StringReader(writer.toString()), new InputStreamReader(expectedFacesConfigFile));

        Assert.assertTrue("XML was not similar:" + xmlDiff.toString(), xmlDiff.similar());
    }

    protected StringWriter generateFacesConfig(ComponentLibrary library) throws Exception {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        JAXBBinding jaxbBinding = new JAXBBinding();
        jaxbBinding.marshal(result, FacesConfigGenerator.FACES_SCHEMA_LOCATION, new FacesConfigAdapter()
            .marshal(library));
        return writer;
    }

    protected void validateTaglib(StringWriter facesConfig) throws SAXException, IOException {
        validateXml(facesConfig, ComponentLibrary.TAGLIB_SCHEMA_LOCATION);
    }

    protected void validateXml(StringWriter facesConfig) throws SAXException, IOException {
        validateXml(facesConfig, ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION);
    }

    protected void validateXml(StringWriter facesConfig, String schemaLocation) throws SAXException, IOException {
        InputSource is = new InputSource(new StringReader(facesConfig.toString()));
        Validator validator = new Validator(is);
        validator.useXMLSchema(true);
        InputSource source = new CdkEntityResolver().resolveEntity(null, schemaLocation);
        validator.setJAXP12SchemaSource(source);

        validator.assertIsValid();
    }
}
