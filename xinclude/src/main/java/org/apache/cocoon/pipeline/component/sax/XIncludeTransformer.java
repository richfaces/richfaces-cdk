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

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cocoon.pipeline.component.xpointer.XPointer;
import org.apache.cocoon.pipeline.component.xpointer.XPointerContext;
import org.apache.cocoon.pipeline.component.xpointer.parser.ParseException;
import org.apache.cocoon.pipeline.component.xpointer.parser.XPointerFrameworkParser;
import org.richfaces.cdk.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public final class XIncludeTransformer implements SAXConsumer {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String HTTP_ACCEPT = "Accept";
    private static final String HTTP_ACCEPT_LANGUAGE = "Accept-Language";
    private static final String UNKNOWN_LOCATION = "unknow location";
    private static final String XINCLUDE_ACCEPT = "accept";
    private static final String XINCLUDE_ACCEPT_LANGUAGE = "accept-language";
    private static final String XINCLUDE_ENCODING = "encoding";
    private static final String XINCLUDE_FALLBACK = "fallback";
    private static final String XINCLUDE_HREF = "href";
    private static final String XINCLUDE_INCLUDE = "include";
    private static final String XINCLUDE_NAMESPACE_URI = "http://www.w3.org/2001/XInclude";
    private static final String XINCLUDE_PARSE = "parse";
    private static final String XINCLUDE_PARSE_TEXT = "text";
    private static final String XINCLUDE_PARSE_XML = "xml";
    private static final String XINCLUDE_XPOINTER = "xpointer";
    private final Logger log;
    /**
     * The nesting level of fallback that should be used
     */
    private int useFallbackLevel = 0;
    /**
     * The nesting level of xi:include elements that have been encountered.
     */
    private int xIncludeElementLevel = 0;
    /**
     * Keep a map of namespaces prefix in the source document to pass it to the XPointerContext for correct namespace
     * identification.
     */
    private final Map<String, String> namespaces = new HashMap<String, String>();
    private URI baseUri;
    private ContentHandler contentHandler;
    /**
     * The nesting level of xi:fallback elements that have been encountered.
     */
    private int fallbackElementLevel;
    private LexicalHandler lexicalHandler;
    /**
     * Locator of the current stream, stored here so that it can be restored after another document send its content to the
     * consumer.
     */
    private Locator locator;
    private EntityResolver2 resolver;

    public XIncludeTransformer(Logger log) {
        this.log = log;
    }

    public XIncludeTransformer(URI baseUri, Logger log) {
        this(log);
        this.setBaseUri(baseUri);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the resolver
     */
    public EntityResolver2 getResolver() {
        return resolver;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param resolver the resolver to set
     */
    public void setResolver(EntityResolver2 resolver) {
        this.resolver = resolver;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the contentHandler
     */
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the lexicalHandler
     */
    public LexicalHandler getLexicalHandler() {
        if (lexicalHandler == null) {
            lexicalHandler = new DummyLexicalHandler();
        }

        return lexicalHandler;
    }

    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Eventually previous errors don't reset local variables status, so every time a new consumer is set, local variables
     * should be re-initialized
     */
    public void setContentHandler(ContentHandler delegateHandler) {
        this.contentHandler = delegateHandler;

        if (delegateHandler instanceof LexicalHandler) {
            lexicalHandler = (LexicalHandler) delegateHandler;
        }

        this.xIncludeElementLevel = 0;
        this.fallbackElementLevel = 0;
        this.useFallbackLevel = 0;
    }

    /**
     * Determine whether the pipe is currently in a state where contents should be evaluated, i.e. xi:include elements should be
     * resolved and elements in other namespaces should be copied through. Will return false for fallback contents within a
     * successful xi:include, and true for contents outside any xi:include or within an xi:fallback for an unsuccessful
     * xi:include.
     */
    private boolean isEvaluatingContent() {
        return xIncludeElementLevel == 0 || (fallbackElementLevel > 0 && fallbackElementLevel == useFallbackLevel);
    }

    private String getLocation() {
        if (locator == null) {
            return UNKNOWN_LOCATION;
        } else {
            return locator.getSystemId() + ":" + locator.getColumnNumber() + ":" + locator.getLineNumber();
        }
    }

    public void startDocument() throws SAXException {
        if (xIncludeElementLevel == 0) {
            getContentHandler().startDocument();
        }
    }

    public void endDocument() throws SAXException {
        if (xIncludeElementLevel == 0) {
            getContentHandler().endDocument();
        }
    }

    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        if (XINCLUDE_NAMESPACE_URI.equals(uri)) {

            // Handle xi:include:
            if (XINCLUDE_INCLUDE.equals(localName)) {

                // Process the include, unless in an ignored fallback:
                if (isEvaluatingContent()) {
                    String href = atts.getValue("", XINCLUDE_HREF);
                    String parse = atts.getValue("", XINCLUDE_PARSE);

                    // Default for @parse is "xml"
                    if (parse == null) {
                        parse = XINCLUDE_PARSE_XML;
                    }

                    String xpointer = atts.getValue("", XINCLUDE_XPOINTER);
                    String encoding = atts.getValue("", XINCLUDE_ENCODING);
                    String accept = atts.getValue("", XINCLUDE_ACCEPT);
                    String acceptLanguage = atts.getValue("", XINCLUDE_ACCEPT_LANGUAGE);

                    processXIncludeElement(href, parse, xpointer, encoding, accept, acceptLanguage);
                }

                xIncludeElementLevel++;
            } else if (XINCLUDE_FALLBACK.equals(localName)) {

                // Handle xi:fallback
                fallbackElementLevel++;
            } else {

                // Unknown element:
                throw new SAXException("Unknown XInclude element " + localName + " at " + getLocation());
            }
        } else if (isEvaluatingContent()) {

            // Copy other elements through when appropriate:
            getContentHandler().startElement(uri, localName, name, atts);
        }
    }

    private void processXIncludeElement(String href, String parse, String xpointer, String encoding, String accept,
            String acceptLanguage) throws SAXException {

        if (log.isDebugEnabled()) {
            log.debug("Processing XInclude element: href=" + href + ", parse=" + parse + ", xpointer=" + xpointer
                    + ", encoding=" + encoding + ", accept=" + accept + ", acceptLanguage=" + acceptLanguage);
        }

        int fragmentIdentifierPos = href.indexOf('#');

        if (fragmentIdentifierPos != -1) {
            log.warn("Fragment identifer found in 'href' attribute: " + href
                    + "\nFragment identifiers are forbidden by the XInclude specification. "
                    + "They are still handled by XIncludeTransformer for backward "
                    + "compatibility, but their use is deprecated and will be prohibited "
                    + "in a future release.  Use the 'xpointer' attribute instead.");

            if (xpointer == null) {
                xpointer = href.substring(fragmentIdentifierPos + 1);
            }

            href = href.substring(0, fragmentIdentifierPos);
        }

        // An empty or absent href is a reference to the current document -- this can be different than the current base
        if (href == null || href.length() == 0) {
            throw new SAXException("XIncludeTransformer: encountered empty href (= href pointing to the current document).");
        }

        InputSource source = createSource(href);

        if (log.isDebugEnabled()) {
            log.debug("Parse type=" + parse);
        }

        if (XINCLUDE_PARSE_XML.equals(parse)) {
            try {
                if (xpointer != null && xpointer.length() > 0) {
                    XPointer xPointer = XPointerFrameworkParser.parse(xpointer);
                    XPointerContext xPointerContext = new XPointerContext(xpointer, source, this, resolver, log);

                    for (Entry<String, String> namespace : namespaces.entrySet()) {
                        xPointerContext.addPrefix(namespace.getKey(), namespace.getValue());
                    }

                    xPointer.process(xPointerContext);
                } else {

                    // just parses the document and streams it
                    XMLReader xmlReader = XMLReaderFactory.createXMLReader();

                    xmlReader.setContentHandler(this);
                    xmlReader.setEntityResolver(resolver);
                    xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
                    xmlReader.parse(source);
                }
            } catch (ParseException e) {

                // this exception is thrown in case of an invalid xpointer expression
                useFallbackLevel++;
                log.info("Error parsing XPointer expression: " + e.getMessage() + " , will try to use fallback.");
            } catch (IOException e) {
                useFallbackLevel++;
                log.info("Error processing an xInclude: " + e.getMessage() + "  will try to use fallback.");
            } catch (SAXException e) {
                useFallbackLevel++;
                log.info("Error processing an xInclude: " + e.getMessage() + "  will try to use fallback.");
            }
        } else if (XINCLUDE_PARSE_TEXT.equals(parse)) {
            if (xpointer != null) {
                throw new SAXException("xpointer attribute must not be present when parse='text': " + getLocation());
            }

            // TODO - read source as text.
            if (null != source.getCharacterStream()) {

                // use reader
            } else if (null != source.getByteStream()) {

                // use stream, detect encoding
            } else if (null != source.getSystemId()) {

                // get from url.
            } else {
                useFallbackLevel++;
                log.error("Can't read XInclude href " + href + " at " + getLocation());
            }
        } else {
            throw new SAXException("Found 'parse' attribute with unknown value " + parse + " at " + getLocation());
        }
    }

    private InputSource createSource(String sourceAtt) throws SAXException {
        try {
            InputSource source = null;
            URI sourceURI = URI.create(sourceAtt);

            if (!sourceURI.isAbsolute() && null != this.baseUri) {
                sourceAtt = this.baseUri.resolve(sourceURI).toString();
            }

            if (null != resolver) {
                source = resolver.resolveEntity(null, sourceAtt);
            }

            if (null == source) {
                source = new InputSource(sourceAtt);
            }

            if (this.log.isDebugEnabled()) {
                this.log.debug("Including source: " + sourceAtt);
            }

            return source;
        } catch (IllegalArgumentException e) {
            String message = "Invalid xinclude URI " + sourceAtt;

            this.log.error(message, e);

            throw new ProcessingException(message, e);
        } catch (IOException e) {
            String message = "Can't resolve URL " + sourceAtt;

            this.log.error(message, e);

            throw new ProcessingException(message, e);
        }
    }

    public void endElement(String uri, String localName, String name) throws SAXException {

        // Handle elements in xinclude namespace:
        if (XINCLUDE_NAMESPACE_URI.equals(uri)) {

            // Handle xi:include:
            if (XINCLUDE_INCLUDE.equals(localName)) {
                xIncludeElementLevel--;

                if (useFallbackLevel > xIncludeElementLevel) {
                    useFallbackLevel = xIncludeElementLevel;
                }
            } else if (XINCLUDE_FALLBACK.equals(localName)) {

                // Handle xi:fallback:
                fallbackElementLevel--;
            }
        } else if (isEvaluatingContent()) {

            // Copy other elements through when appropriate:
            getContentHandler().endElement(uri, localName, name);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (isEvaluatingContent()) {

            // removed xinclude namespace from result document
            if (!uri.equals(XINCLUDE_NAMESPACE_URI)) {
                getContentHandler().startPrefixMapping(prefix, uri);
            }

            namespaces.put(prefix, uri);
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        if (isEvaluatingContent()) {
            String uri = namespaces.remove(prefix);

            if (!XINCLUDE_NAMESPACE_URI.equals(uri)) {
                getContentHandler().endPrefixMapping(prefix);
            }
        }
    }

    public void startCDATA() throws SAXException {
        if (isEvaluatingContent()) {
            getLexicalHandler().startCDATA();
        }
    }

    public void endCDATA() throws SAXException {
        if (isEvaluatingContent()) {
            getLexicalHandler().startCDATA();
        }
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {

        // ignoring DTD
    }

    public void endDTD() throws SAXException {

        // ignoring DTD
    }

    public void startEntity(String name) throws SAXException {
        if (isEvaluatingContent()) {
            getLexicalHandler().startEntity(name);
        }
    }

    public void endEntity(String name) throws SAXException {
        if (isEvaluatingContent()) {
            getLexicalHandler().endEntity(name);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isEvaluatingContent()) {
            getContentHandler().characters(ch, start, length);
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (isEvaluatingContent()) {
            getContentHandler().ignorableWhitespace(ch, start, length);
        }
    }

    public void comment(char[] ch, int start, int length) throws SAXException {

        // skip comments
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (isEvaluatingContent()) {
            getContentHandler().processingInstruction(target, data);
        }
    }

    public void setDocumentLocator(Locator locator) {
        if (log.isDebugEnabled()) {
            log.debug("setDocumentLocator called " + locator.getSystemId());
        }

        this.locator = locator;
        getContentHandler().setDocumentLocator(locator);
    }

    public void skippedEntity(String name) throws SAXException {
        if (isEvaluatingContent()) {
            getContentHandler().skippedEntity(name);
        }
    }

    private static final class DummyLexicalHandler implements LexicalHandler {
        public void startEntity(String name) throws SAXException {
        }

        public void startDTD(String name, String publicId, String systemId) throws SAXException {
        }

        public void startCDATA() throws SAXException {
        }

        public void endEntity(String name) throws SAXException {
        }

        public void endDTD() throws SAXException {
        }

        public void endCDATA() throws SAXException {
        }

        public void comment(char[] ch, int start, int length) throws SAXException {
        }
    }
}
