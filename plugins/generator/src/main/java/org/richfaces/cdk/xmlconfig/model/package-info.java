/**
 * <h2>JAXB classes that wraps real model classes.</h2>
 * <p>Some model requirements do not match JAXB plain bean model. The most important case is requirements for unique 
 * Id's like component and renderer type. the other important difference is model properties which do not map to
 * faces-config schema but moved into &lt;<....-extension&gt; elements.</p>
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@javax.xml.bind.annotation.XmlSchema(namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
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
package org.richfaces.cdk.xmlconfig.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.richfaces.cdk.model.AttributeModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.PropertyModel;

