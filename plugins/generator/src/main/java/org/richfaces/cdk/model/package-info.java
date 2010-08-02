/**
 * <h2>CDK library model classes used by all generation tasks.</h2>
 * <p>CDK architecture seems like MVC-pattern implementation. The controller {@link org.richfaces.cdk.LibraryBuilder}
 * class generates model from different sources ( Java Annotations, XML files and so on ). That model will be used to
 * generate all necessary classes by the appropriate "renderers" that act as 'View' part of pattern.</p>
 * <p>That model:</p>
 * <ul>
 * <li>Contains all information about JSF library components and their properties</li>
 * <li>Encapsulates restrictions and references for model components, therefore it should be modified by model metods
 * only.</li>
 * <li>Encapsulates <a href="http://www.jboss.org/community/docs/DOC-13693">CDK naming conventions</a></li>
 * <li>Provides 'Visitor' pattern methods. see {@link Visitor} for reference.</li>
 * </ul> 
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@javax.xml.bind.annotation.XmlSchema(
    namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
    location = ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION,
    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,    
    xmlns = {@javax.xml.bind.annotation.XmlNs(prefix = "cdk",
        namespaceURI = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE),
        @javax.xml.bind.annotation.XmlNs(prefix = "",
            namespaceURI = ComponentLibrary.FACES_CONFIG_NAMESPACE)})
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type = ClassName.class, value = ClassAdapter.class),
    @XmlJavaTypeAdapter(type = AttributeModel.class, value = AttributeAdapter.class),
    @XmlJavaTypeAdapter(type = PropertyModel.class, value = PropertyAdapter.class),
    @XmlJavaTypeAdapter(type = FacesId.class, value = FacesIdAdapter.class),
    @XmlJavaTypeAdapter(type = ComponentLibrary.class, value = FacesConfigAdapter.class)
})
package org.richfaces.cdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.richfaces.cdk.xmlconfig.model.AttributeAdapter;
import org.richfaces.cdk.xmlconfig.model.ClassAdapter;
import org.richfaces.cdk.xmlconfig.model.FacesConfigAdapter;
import org.richfaces.cdk.xmlconfig.model.FacesIdAdapter;
import org.richfaces.cdk.xmlconfig.model.PropertyAdapter;

