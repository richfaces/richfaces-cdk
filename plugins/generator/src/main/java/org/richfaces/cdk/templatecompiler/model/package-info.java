/**
 * <h2>JAXB classes that wraps real model classes.</h2>
 * <p>Some model requirements do not match JAXB plain bean model. The most important case is requirements for unique 
 * Id's like component and renderer type. the other important difference is model properties which do not map to
 * faces-config schema but moved into &lt;<....-extension&gt; elements.</p>
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@javax.xml.bind.annotation.XmlSchema(namespace = Template.CDK_NAMESPACE,
    xmlns = {@javax.xml.bind.annotation.XmlNs(prefix = "cdk",
        namespaceURI = Template.CDK_NAMESPACE),
        @javax.xml.bind.annotation.XmlNs(prefix = "cc",
            namespaceURI = Template.COMPOSITE_NAMESPACE)}) 
package org.richfaces.cdk.templatecompiler.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


