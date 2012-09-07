/*
 * $Id$
 *
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.cocoon.pipeline.component.sax.XIncludeTransformer;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class JAXBBinding implements JAXB {
    private static final FacesConfigNamespacePreffixMapper PREFFIX_MAPPER = new FacesConfigNamespacePreffixMapper();
    @Inject
    private EntityResolver2 resolver;
    @Inject
    private Logger log;

    public JAXBBinding() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.xmlconfig.JAXB#unmarshal(java.io.File, java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T unmarshal(File file, String schemaLocation, Class<T> bindClass) throws CdkException, FileNotFoundException {
        InputSource input = new InputSource(new FileInputStream(file));

        input.setSystemId(file.toURI().toString());

        T unmarshal = unmarshal(schemaLocation, bindClass, input);

        return unmarshal;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.xmlconfig.JAXB#unmarshal(java.lang.String, java.lang.String, java.lang.Class)
     */
    @Override
    public <T> T unmarshal(String url, String schemaLocation, Class<T> bindClass) throws CdkException, FileNotFoundException {
        try {
            InputSource inputSource;
            try {
                inputSource = resolver.resolveEntity(null, url);
            } catch (SAXException e) {
                inputSource = null;
            }

            if (null == inputSource) {
                inputSource = new InputSource(url);
            }

            T unmarshal = unmarshal(schemaLocation, bindClass, inputSource);

            return unmarshal;
        } catch (IOException e) {
            throw new FileNotFoundException("XML file not found at " + url);
        }
    }

    @SuppressWarnings("unchecked")
    // TODO nick - schemaLocation is unused
    <T> T unmarshal(String schemaLocation, Class<T> bindClass, InputSource inputSource) throws CdkException {
        T unmarshal = null;

        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            xmlReader.setEntityResolver(resolver);
            xmlReader.setFeature("http://xml.org/sax/features/validation", true);
            xmlReader.setFeature("http://apache.org/xml/features/validation/schema", true);
            xmlReader.setFeature("http://apache.org/xml/features/validation/dynamic", true);

            // Setup JAXB to unmarshal
            // TODO - create xinclude content handler that process xinclude directives
            // and send SAX event to the unmarshaller handler.
            Unmarshaller u = JAXBContext.newInstance(bindClass).createUnmarshaller();
            u.setEventHandler(new ValidationEventCollector());

            XIncludeTransformer xIncludeTransformer = new XIncludeTransformer(log);

            if (null != inputSource.getSystemId()) {
                xIncludeTransformer.setBaseUri(new URI(inputSource.getSystemId()));
            }

            UnmarshallerHandler unmarshallerHandler = u.getUnmarshallerHandler();

            xIncludeTransformer.setContentHandler(unmarshallerHandler);
            xIncludeTransformer.setResolver(resolver);
            xmlReader.setContentHandler(xIncludeTransformer);
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", xIncludeTransformer);
            xmlReader.parse(inputSource);

            // turn off the JAXB provider's default validation mechanism to
            // avoid duplicate validation
            // u.setValidating(false);
            unmarshal = (T) unmarshallerHandler.getResult();
        } catch (JAXBException e) {
            throw new CdkException("JAXB Unmarshaller error: " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new CdkException("Invalid XML source URI: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CdkException("JAXB Unmarshaller input error: " + e.getMessage(), e);
        } catch (SAXException e) {
            throw new CdkException("XML error: " + e.getMessage(), e);
        } finally {

            // TODO Refactoring
        }

        return unmarshal;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.xmlconfig.JAXB#marshal(java.io.File, java.lang.String, T)
     */
    @Override
    public <T> void marshal(Writer output, String schemaLocation, T model) throws CdkException {
        try {
            StreamResult result = new StreamResult(output);

            marshal(result, schemaLocation, model);
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            throw new CdkException("File not found", e);
        } catch (IOException e) {
            throw new CdkException("XML file writting error", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.xmlconfig.JAXB#marshal(javax.xml.transform.Result, java.lang.String, T)
     */
    @Override
    public <T> void marshal(Result output, String schemaLocation, T model) throws CdkException {
        try {
            JAXBContext jc = JAXBContext.newInstance(model.getClass());
            Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);

            // TODO - let writer to define additional schemes.
            // marshaller.setProperty("jaxb.schemaLocation", Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (null != schemaLocation) {
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);

                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", PREFFIX_MAPPER);
            }

            marshaller.marshal(model, output);
        } catch (JAXBException e) {
            throw new CdkException("JAXB Marshaller error", e);
        } finally {

            // TODO Refactoring
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Close input source after parsing.
     * </p>
     *
     * @param source
     */
    private void closeSource(Source source) {
        if (source instanceof SAXSource) {
            SAXSource saxSource = (SAXSource) source;
            InputSource inputSource = saxSource.getInputSource();

            try {
                Reader stream = inputSource.getCharacterStream();

                if (null != stream) {
                    stream.close();
                } else {
                    InputStream byteStream = inputSource.getByteStream();

                    if (null != byteStream) {
                        byteStream.close();
                    }
                }
            } catch (IOException e) {

                // Can be ignored because source has already been read.
            }
        }
    }

    public static boolean isCollections(Class<?> targetType, Object propertyValue) {
        return Collection.class.isAssignableFrom(targetType) && propertyValue instanceof Collection;
    }
}
