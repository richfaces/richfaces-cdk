package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.attributes.Attribute;
import org.richfaces.cdk.attributes.Attribute.Kind;
import org.richfaces.cdk.attributes.Element;
import org.richfaces.cdk.attributes.Schema;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.Named.NamePredicate;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.templatecompiler.model.AnyElement;
import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.util.Strings;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class AttributesStatement extends StatementsContainer {
    private static final Splitter PASS_THGOUGH_SPLITTER = Splitter.on(Pattern.compile("\\s+,?\\s*"));
    private static final String WILDCARD_SUFFIX = "*";
    private final Schema attributesSchema;
    private final Provider<WriteAttributeStatement> statementProvider;
    private final Provider<WriteAttributesSetStatement> passThroughStatementProvider;
    private final Logger logger;
    private QName elementName;
    private Collection<PropertyBase> componentAttributes;

    @Inject
    public AttributesStatement(@Named(Template.XHTML_EL_NAMESPACE) Schema attributesSchema,
            Provider<WriteAttributeStatement> attributeStatementProvider,
            Provider<WriteAttributesSetStatement> passThroughStatementProvider, Logger logger) {
        this.attributesSchema = attributesSchema;
        this.statementProvider = attributeStatementProvider;
        this.passThroughStatementProvider = passThroughStatementProvider;
        this.logger = logger;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param attributes
     * @param componentAttributes
     */
    public void processAttributes(AnyElement element, Collection<PropertyBase> componentAttributes) {
        this.componentAttributes = componentAttributes;
        Set<String> processedAttributes = Sets.newHashSet();
        TreeSet<PassThrough> passThroughAttributes = Sets.newTreeSet();
        this.elementName = element.getName();
        processRegularAttributes(element, processedAttributes, passThroughAttributes);
        String passThrough = element.getPassThrough();
        processPassThrough(processedAttributes, passThroughAttributes, passThrough);
        String passThroughWithExclusions = element.getPassThroughWithExclusions();
        processPassThroughWithExclusions(processedAttributes, passThroughAttributes, passThroughWithExclusions);
        if (!passThroughAttributes.isEmpty()) {
            WriteAttributesSetStatement writeAttributesSetStatement = passThroughStatementProvider.get();
            addStatement(writeAttributesSetStatement);
            writeAttributesSetStatement.setAttributes(passThroughAttributes);
        }
    }

    private void processPassThroughWithExclusions(Set<String> processedAttributes, TreeSet<PassThrough> passThroughAttributes,
            String passThroughWithExclusions) {
        if (null != passThroughWithExclusions) {
            // cdk:passThroughWithExclusions="id,class,style"
            Map<String, Element> elements = attributesSchema.getElements();
            String elementLocalName = elementName.getLocalPart();
            if (Template.isDefaultNamespace(elementName) && elements.containsKey(elementLocalName)) {
                Element schemaElement = elements.get(elementLocalName);
                Iterable<String> exclusions = PASS_THGOUGH_SPLITTER.split(passThroughWithExclusions);

                // mark all passThrough exclusions as processed - they won't be passed through
                for (String exclusion : exclusions) {
                    if (exclusion.endsWith(WILDCARD_SUFFIX)) {
                        String attributePrefix = exclusion.substring(0, exclusion.length() - WILDCARD_SUFFIX.length());
                        for (Attribute schemaAttribute : schemaElement.getAttributes().values()) {
                            if (schemaAttribute.getName().startsWith(attributePrefix)) {
                                processedAttributes.add(schemaAttribute.getName());
                            }
                        }
                    } else {
                        processedAttributes.add(exclusion);
                    }
                }

                Iterables.addAll(processedAttributes, exclusions);

                for (Attribute schemaAttribute : schemaElement.getAttributes().values()) {
                    if (!processedAttributes.contains(schemaAttribute.getName())) {
                        passThroughAttributes.add(createPassThrough(schemaAttribute.getName(),
                                schemaAttribute.getComponentAttributeName()));
                    }
                }
            }
        }
    }

    private void processPassThrough(Set<String> processedAttributes, TreeSet<PassThrough> passThroughAttributes,
            String passThrough) {
        if (null != passThrough) {
            // cdk:passThrough="class:styleClass,style , id:clientId"
            Iterable<String> split = PASS_THGOUGH_SPLITTER.split(passThrough);
            for (String attribute : split) {
                String[] splitAttr = attribute.split(":");
                String attributeName = splitAttr[0];
                String componentAttributeName = splitAttr.length > 1 ? splitAttr[1] : attributeName;

                if (attributeName.endsWith(WILDCARD_SUFFIX)) {
                    processPassThroughWithWildcard(processedAttributes, passThroughAttributes, attributeName,
                            componentAttributeName);
                } else {
                    addPassThroughAttribute(processedAttributes, passThroughAttributes, attributeName, componentAttributeName);
                }
            }
        }
    }

    private void addPassThroughAttribute(Set<String> processedAttributes, TreeSet<PassThrough> passThroughAttributes,
            String attributeName, String componentAttributeName) {

        if (processedAttributes.add(attributeName)) {
            passThroughAttributes.add(createPassThrough(attributeName, componentAttributeName));
        }
    }

    private void processPassThroughWithWildcard(Set<String> processedAttributes, TreeSet<PassThrough> passThroughAttributes,
            String wildcardAttributeName, String wildcardComponentAttributeName) {

        // cdk:passThrough="on*:onlist*"

        String attributePrefix = wildcardAttributeName.substring(0, wildcardAttributeName.length() - WILDCARD_SUFFIX.length());
        String componentAttributePrefix = wildcardComponentAttributeName.substring(0, wildcardComponentAttributeName.length()
                - WILDCARD_SUFFIX.length());

        Map<String, Element> elements = attributesSchema.getElements();
        String elementLocalName = elementName.getLocalPart();
        if (Template.isDefaultNamespace(elementName) && elements.containsKey(elementLocalName)) {
            Element schemaElement = elements.get(elementLocalName);
            for (Attribute schemaAttribute : schemaElement.getAttributes().values()) {
                String attributeName = schemaAttribute.getName();
                if (attributeName.startsWith(attributePrefix)) {
                    if (!processedAttributes.contains(schemaAttribute.getName())) {
                        String componentAttributeName = componentAttributePrefix
                                + attributeName.substring(attributePrefix.length());

                        addPassThroughAttribute(processedAttributes, passThroughAttributes, attributeName,
                                componentAttributeName);
                    }
                }

            }
        }
    }

    private void processRegularAttributes(AnyElement element, Set<String> processedAttributes,
            TreeSet<PassThrough> passThroughAttributes) {
        for (Map.Entry<QName, Object> entry : element.getAttributes().entrySet()) {
            QName qName = entry.getKey();
            if (Template.CDK_NAMESPACE.equals(qName.getNamespaceURI())) {
                // CDK attributes should be assigned to AnyElement attribute, log error.
                logger.error("Unknown attribute " + qName);
            } else {
                Object value = entry.getValue();
                String localAttributeName = qName.getLocalPart();
                if (Template.CDK_PASS_THROUGH_NAMESPACE.equals(qName.getNamespaceURI())) {
                    // TODO - check empty attribute value.
                    // pass through attribute in format <div pf:class="styleClass" />
                    passThroughAttributes.add(createPassThrough(localAttributeName, value.toString()));
                    processedAttributes.add(localAttributeName);
                } else if (Template.isDefaultNamespace(qName.getNamespaceURI())) {
                    Attribute schemaAttribute = getSchemaAttribute(localAttributeName);
                    setupAttributeStatement(qName, value, schemaAttribute);
                    processedAttributes.add(localAttributeName);
                } else {
                    setupAttributeStatement(qName, value, getGenericAttribute(localAttributeName));
                }
            }
        }
    }

    private PassThrough createPassThrough(String localAttributeName, String componentAttribute) {
        final PassThrough passThrough = new PassThrough();
        passThrough.name = QName.valueOf(localAttributeName);
        Attribute schemaAttribute = getSchemaAttribute(localAttributeName);
        if (Strings.isEmpty(componentAttribute)) {
            passThrough.componentAttribute = schemaAttribute.getComponentAttributeName();
        } else {
            passThrough.componentAttribute = componentAttribute;
        }
        passThrough.kind = schemaAttribute.getKind();
        passThrough.defaultValue = schemaAttribute.getDefaultValue();
        try {
            PropertyBase componetProperty = findComponentAttribute(passThrough.componentAttribute);
            passThrough.type = componetProperty.getType().toString();
            for (EventName event : componetProperty.getEventNames()) {
                passThrough.behaviors.add(event.getName());
            }
        } catch (NoSuchElementException e) {
            passThrough.type = Object.class.getName();
        }
        return passThrough;
    }

    private WriteAttributeStatement createAttributeStatement() {
        WriteAttributeStatement writeAttributeStatement = statementProvider.get();
        addStatement(writeAttributeStatement);
        return writeAttributeStatement;
    }

    private PropertyBase findComponentAttribute(final String name) throws NoSuchElementException {
        return Iterables.find(componentAttributes, new NamePredicate(name));
    }

    private WriteAttributeStatement setupAttributeStatement(QName qName, Object value, Attribute schemaAttribute) {
        WriteAttributeStatement writeAttributeStatement = createAttributeStatement();
        String defaultValue = schemaAttribute.getDefaultValue();
        switch (schemaAttribute.getKind()) {
            case GENERIC:
                writeAttributeStatement.setAttribute(qName, value, defaultValue);
                // Process behaviors
                break;
            case URI:
                writeAttributeStatement.setUriAttribute(qName, value, defaultValue);
                break;
            case BOOLEAN:
                writeAttributeStatement.setBooleanAttribute(qName, value, defaultValue);
                break;
            default:
                // Only happens if new kind has bin added without modifiing this method:
                logger.error("Unknown kind of statement, fix case operator code: " + schemaAttribute.getKind());
        }
        return writeAttributeStatement;
    }

    protected Kind getAttributeKind(String attributeName) {
        return getSchemaAttribute(attributeName).getKind();
    }

    protected Attribute getSchemaAttribute(String attributeName) {
        Attribute result;
        Map<String, Element> elements = attributesSchema.getElements();
        String elementLocalName = elementName.getLocalPart();
        if (Template.isDefaultNamespace(elementName) && elements.containsKey(elementLocalName)) {
            Element schemaElement = elements.get(elementLocalName);
            if (schemaElement.getAttributes().containsKey(attributeName)) {
                result = schemaElement.getAttributes().get(attributeName);
            } else {
                result = getGenericAttribute(attributeName);
            }
        } else {
            result = getGenericAttribute(attributeName);
        }
        return result;
    }

    protected Attribute getGenericAttribute(String name) {
        Attribute attribute = new Attribute(name);
        attribute.setKind(Kind.GENERIC);
        // check default exceptions like class -> styleClass;
        if ("class".equals(name)) {
            attribute.setComponentAttributeName("styleClass");
        }
        return attribute;
    }
}
