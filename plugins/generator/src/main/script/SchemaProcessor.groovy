

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.richfaces.cdk.attributes.Attribute;
import org.richfaces.cdk.attributes.Attribute.Kind;
import org.richfaces.cdk.attributes.Element;
import org.richfaces.cdk.attributes.Schema;
import org.richfaces.cdk.attributes.SchemaSet;
import org.w3c.dom.Node;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSUnionSimpleType;

final class SchemaProcessor {

    private NamespaceContext CDK_NAMESPACES_CONTEXT = new SchemaProcessorNamespaceContext();

    private XSSchema schema;

    private String namespace;

    private XSSimpleType anyURIType;

    private XPathFactory xPathFactory = XPathFactory.newInstance();

    private SchemaSet modelSchemaSet;

    public SchemaProcessor(XSSchemaSet schemaSet, String namespace) {
        super();

        this.namespace = namespace;
        this.schema = schemaSet.getSchema(namespace);
        this.anyURIType = schemaSet.getSimpleType("http://www.w3.org/2001/XMLSchema", "anyURI");
    }

    private XPath createXPath() {
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(CDK_NAMESPACES_CONTEXT);

        return xPath;
    }

    private String getAdditionalInfo(XSAttributeUse attributeUse, String elementName) throws XPathExpressionException {
        String defaultValue = null;

        XSAttributeDecl attributeDecl = attributeUse.getDecl();
        XSAnnotation annotation = attributeDecl.getAnnotation();
        if (annotation != null) {
            Object annotationElement = annotation.getAnnotation();
            if (annotationElement != null) {
                XPath xPath = createXPath();
                Node node = (Node) xPath.evaluate("xs:appinfo/cdk-schema-info:" + elementName, annotationElement,
                    XPathConstants.NODE);
                if (node != null) {
                    defaultValue = xPath.evaluate("text()", node);
                }
            }
        }

        return defaultValue;
    }
	
	private String getComponentAttributeName(XSAttributeUse attributeUse) throws XPathExpressionException {
		return getAdditionalInfo(attributeUse, "component-attribute-name");
	}   
	
	private String getDefaultValue(XSAttributeUse attributeUse) throws XPathExpressionException {
	    return getAdditionalInfo(attributeUse, "default-value");
	}	
	
	private boolean isGenericKind(Attribute.Kind attributeKind) {
	    return Kind.GENERIC.equals(attributeKind);
	}
	
    private Attribute.Kind getAttributeKind(XSSimpleType simpleType) {
        return getAttributeKind(simpleType, new HashSet<XSType>());
    }

    private Attribute.Kind getAttributeKind(XSSimpleType simpleType, Set<XSType> processedTypes) {
		Attribute.Kind attributeKind = Kind.GENERIC;
		
		if (simpleType == null) {
            return attributeKind;
        }

        if (!processedTypes.add(simpleType)) {
            return attributeKind;
        }

        if (simpleType.isDerivedFrom(anyURIType)) {
            return Attribute.Kind.URI;
        }
		
		if (simpleType.isRestriction()) {
		    XSRestrictionSimpleType restrictionType = simpleType.asRestriction();
		    List<XSFacet> facets = restrictionType.getDeclaredFacets("enumeration");
			if (facets != null && facets.size() == 1) {
				return Attribute.Kind.BOOLEAN;
			}
		}
		
		attributeKind = getAttributeKind(simpleType.getSimpleBaseType(), processedTypes);
		if (!isGenericKind(attributeKind)) {
			return attributeKind;
		}

		attributeKind = getAttributeKind(simpleType.getPrimitiveType(), processedTypes);
		if (!isGenericKind(attributeKind)) {
			return attributeKind;
		}
			
        if (simpleType.isUnion()) {
            XSUnionSimpleType unionSimpleType = simpleType.asUnion();
            int memberSize = unionSimpleType.getMemberSize();
            for (int i = 0; i < memberSize; i++) {
                XSSimpleType unionMemberType = unionSimpleType.getMember(i);
				attributeKind = getAttributeKind(unionMemberType, processedTypes);
				if (!isGenericKind(attributeKind)) {
				    return attributeKind;
				}
            }
        } else if (simpleType.isRestriction()) {
            // do nothing
        } else if (simpleType.isList()) {
            XSListSimpleType listSimpleType = simpleType.asList();
            XSSimpleType listItemType = listSimpleType.getItemType();
			attributeKind = getAttributeKind(listItemType, processedTypes);
    		if (!isGenericKind(attributeKind)) {
    			return attributeKind;
    		}
		}

        return attributeKind;
    }

    public void buildModel() throws Exception {
        modelSchemaSet = new SchemaSet();
        Schema modelSchema = new Schema(namespace);
        modelSchemaSet.addSchema(modelSchema);

        Iterator<XSElementDecl> elements = schema.iterateElementDecls();
        while (elements.hasNext()) {
            XSElementDecl element = elements.next();

            Element modelElement = new Element(element.getName());
            modelSchema.addElement(modelElement);

            XSComplexType complexType = element.getType().asComplexType();
            if (complexType != null) {
                Collection<? extends XSAttributeUse> uses = complexType.getAttributeUses();
                for (XSAttributeUse xsAttributeUse : uses) {
                    XSAttributeDecl attributeDecl = xsAttributeUse.getDecl();
                    String attributeNamespace = attributeDecl.getTargetNamespace();
                    if (namespace.equals(attributeNamespace) || attributeNamespace.length() == 0) {
                        Attribute modelAttribute = new Attribute(attributeDecl.getName());
                        modelElement.addAttribute(modelAttribute);
                        modelAttribute.setKind(getAttributeKind(attributeDecl.getType()));
                        modelAttribute.setRequired(xsAttributeUse.isRequired());
                        modelAttribute.setDefaultValue(getDefaultValue(xsAttributeUse));
						modelAttribute.setComponentAttributeName(getComponentAttributeName(xsAttributeUse));
                    }
                }
            }
        }
    }

    public void serializeModel(OutputStream outputStream) throws IOException {
		try {
			JAXBContext jc = JAXBContext.newInstance(modelSchemaSet.getClass());
			Marshaller marshaller = jc.createMarshaller();
    		
    		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		
    		marshaller.marshal(modelSchemaSet, outputStream);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
