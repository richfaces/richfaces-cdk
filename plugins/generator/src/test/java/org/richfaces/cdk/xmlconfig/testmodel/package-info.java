/**
 * <h2>CDK library model classes used by all generation tasks.</h2>
 * <p>CDK architecture seems like MVC-pattern implementation. The controller {@link org.richfaces.cdk.LibraryBuilder} class
 * generates model from different sources ( Java Annotations, XML files and so on ). That model will be used to generate all necessary
 * classes by the appropriate "renderers" that act as 'View' part of pattern.</p>
 * <p>That model:</p>
 * <ul>
 * <li>Contains all information about JSF library components and their properties</li>
 * <li>Encapsulates restrictions and references for model components, therefore it should be modified by model metods only.</li>
 * <li>Encapsulates <a href="http://www.jboss.org/community/docs/DOC-13693">CDK naming conventions</a></li>
 * <li>Provides 'Visitor' pattern methods. see {@link LibraryVisitor} for reference.</li>
 * </ul> 
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@javax.xml.bind.annotation.XmlSchema(namespace = Root.HTTP_FOO_BAR_SCHEMA, xmlns = {
        @javax.xml.bind.annotation.XmlNs(prefix = "", namespaceURI = Root.HTTP_FOO_BAR_SCHEMA),
        @javax.xml.bind.annotation.XmlNs(prefix = "ext", namespaceURI = Root.EXTENSIONS_NAMESPACE) })
package org.richfaces.cdk.xmlconfig.testmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

