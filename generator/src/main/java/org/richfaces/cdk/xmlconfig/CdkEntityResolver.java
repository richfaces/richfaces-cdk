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
import java.net.URI;
import java.net.URL;

import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * That class resolves entities used by CDK ( standard JSF schemas, extensions, configuration fragments.)
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class CdkEntityResolver implements EntityResolver2 {
    // configure a validating SAX2.0 parser (Xerces2)
    public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public static final String JAXP_SCHEMA_LOCATION = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    public static final String URN_ATTRIBUTES = "urn:attributes:";
    public static final String URN_CONFIG = "urn:config:";
    public static final String URN_RESOURCE = "urn:resource:";
    public static final String URN_TEMPLATES = "urn:templates:";
    public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String RESOURCE_PREFIX = "";
    private static final String ATTRIBUTES_PREFIX = "META-INF/cdk/attributes/";
    private static final String SYSTEM_PREFIX = "/META-INF/schema";
    private static final String URN_SYSTEM = "urn:system:";
    private static final ImmutableMap<String, String> SYSTEM_ENTITIES = ImmutableMap.<String, String>builder()
            .put("http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd", "/web-facesconfig_2_0.xsd")
            .put("http://java.sun.com/xml/ns/javaee/web-facesuicomponent_2_0.xsd", "/web-facesuicomponent_2_0.xsd")
            .put("http://java.sun.com/xml/ns/javaee/web-partialresponse_2_0.xsd", "/web-partialresponse_2_0.xsd")
            .put("http://java.sun.com/xml/ns/javaee/web-facesconfig_1_2.xsd", "/web-facesconfig_1_2.xsd")
            .put("http://java.sun.com/xml/ns/javaee/javaee_5.xsd", "/javaee_5.xsd")
            .put("http://java.sun.com/xml/ns/javaee/javaee_web_services_1_2.xsd", "/javaee_web_services_1_2.xsd")
            .put("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_2.xsd", "/javaee_web_services_client_1_2.xsd")
            .put("http://www.w3.org/2001/03/XMLSchema.dtd", "/XMLSchema.dtd")
            .put("http://www.w3.org/2001/03/xml.xsd", "/xml.xsd").put("http://www.w3.org/2001/xml.xsd", "/xml.xsd")
            .put("http://jboss.org/schema/richfaces/cdk/cdk-template.xsd", "/cdk-template.xsd")
            .put("http://jboss.org/schema/richfaces/cdk/cdk-jstl-core.xsd", "/cdk-jstl-core.xsd")
            .put("http://jboss.org/schema/richfaces/cdk/cdk-schemas-aggregation.xsd", "/cdk-schemas-aggregation.xsd")
            .put("http://jboss.org/schema/richfaces/cdk/cdk-composite.xsd", "/cdk-composite.xsd")
            .put("http://jboss.org/schema/richfaces/cdk/xhtml-el.xsd", "/xhtml-el.xsd").build();
    @Inject
    private CdkClassLoader loader;
    @Inject
    @Source(Sources.FACES_CONFIGS)
    private FileManager facesConfigFolders;
    @Inject
    @Source(Sources.RENDERER_TEMPLATES)
    private FileManager rendererTemplatesFolders;

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.EntityResolver2#getExternalSubset(java.lang.String, java.lang.String)
     */
    @Override
    public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {

        // do nothing because we use XML schema only.
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.ext.EntityResolver2#resolveEntity(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException,
            IOException {

        String temSystemId = systemId;

        // perform conversions with baseURI
        if (null != temSystemId) {
            try {
                URI sourceURI = URI.create(temSystemId);

                if (!sourceURI.isAbsolute() && null != baseURI) {
                    temSystemId = URI.create(baseURI).resolve(sourceURI).toString();
                }
            } catch (IllegalArgumentException e) {

                // Ignore ?
            }
        }

        return resolveSystemId(temSystemId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        InputSource entity = null;

        if (null != systemId) {
            entity = resolveSystemId(systemId);
        }

        return entity;
    }

    protected InputSource resolveSystemId(String systemId) throws IOException {
        InputSource entity = null;

        // first step - convert known system url's:
        String systemIdInternal;

        if (SYSTEM_ENTITIES.containsKey(systemId)) {
            systemIdInternal = URN_SYSTEM + SYSTEM_ENTITIES.get(systemId);
        } else {
            systemIdInternal = systemId;
        }

        // Check registered urn's:
        if (systemIdInternal.startsWith(URN_SYSTEM)) {

            // Cdk resources
            String path = systemIdInternal.substring(URN_SYSTEM.length());
            URL url;
            url = CdkEntityResolver.class.getResource(SYSTEM_PREFIX + path);

            InputStream inputStream = null;
            if (null != url) {
                inputStream = url.openStream();
                entity = new InputSource(inputStream);
                entity.setSystemId(url.toString());
            } else {
                throw new FileNotFoundException("Resource not found in generator class path: " + SYSTEM_PREFIX + path);
            }
        } else if (systemIdInternal.startsWith(URN_RESOURCE)) {

            // Project resources
            String path = systemIdInternal.substring(URN_RESOURCE.length());
            URL url;
            url = loader.getResource(RESOURCE_PREFIX + path);

            InputStream inputStream = null;
            if (null != url) {
                inputStream = url.openStream();
                entity = new InputSource(inputStream);
                entity.setSystemId(url.toString());
            } else {
                throw new FileNotFoundException("Resource not found in project class path: " + path);
            }
        } else if (systemIdInternal.startsWith(URN_ATTRIBUTES)) {

            // Standard attributes. Look for them in the satndard place via
            // project classloader.
            String path = systemIdInternal.substring(URN_ATTRIBUTES.length());
            // Project classpath has precedence
            InputStream inputStream = loader.getResourceAsStream(ATTRIBUTES_PREFIX + path);
            if (null == inputStream) {
                inputStream = getClass().getClassLoader().getResourceAsStream(ATTRIBUTES_PREFIX + path);
            }
            if (null != inputStream) {
                entity = new InputSource(inputStream);
            } else {
                throw new FileNotFoundException("Resource not found in class path: " + ATTRIBUTES_PREFIX + path);
            }
        } else if (systemIdInternal.startsWith(URN_CONFIG)) {

            // Config folder.
            String path = systemIdInternal.substring(URN_CONFIG.length());
            entity = getProjectInputSource(facesConfigFolders, path);
        } else if (systemIdInternal.startsWith(URN_TEMPLATES)) {

            // Templates folder.
            String path = systemIdInternal.substring(URN_TEMPLATES.length());
            entity = getProjectInputSource(rendererTemplatesFolders, path);
        }

        if (null != entity && entity.getSystemId() == null) {
            entity.setSystemId(systemId);
        }

        return entity;
    }

    protected InputSource getProjectInputSource(FileManager folders, String path) throws FileNotFoundException {

        InputSource entity = null;

        File configFile = folders.getFile(path);

        if (null != configFile && configFile.exists()) {
            InputStream inputStream = new FileInputStream(configFile);
            entity = new InputSource(inputStream);
        } else {
            throw new FileNotFoundException("File not found in project : " + path);
        }

        return entity;
    }
}
