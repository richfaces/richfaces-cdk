package org.richfaces.cdk.apt.processors;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;
import org.richfaces.cdk.apt.SourceUtils.SuperTypeVisitor;
import org.richfaces.cdk.model.BeanModelBase;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.MethodSignature;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.util.JavaUtils;
import org.richfaces.cdk.xmlconfig.CdkEntityResolver;
import org.richfaces.cdk.xmlconfig.FragmentParser;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class AttributesProcessorImpl implements AttributesProcessor {
    private static final ClassName SIGNATURE_NONE_CLASS_NAME = ClassName.get(Signature.NONE.class);
    private static final ClassName STRING_TYPE = ClassName.get(String.class);
    @Inject
    private Logger log;
    private final DescriptionProcessor descriptionProcessor;
    private final Provider<SourceUtils> utilsProvider;
    private final FragmentParser parser;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param descriptionProcessor
     * @param utilsProvider
     * @param parser
     */
    @Inject
    public AttributesProcessorImpl(DescriptionProcessor descriptionProcessor, Provider<SourceUtils> utilsProvider,
            FragmentParser parser) {
        this.descriptionProcessor = descriptionProcessor;
        this.utilsProvider = utilsProvider;
        this.parser = parser;
    }

    protected void processAttribute(SourceUtils.BeanProperty beanProperty, PropertyBase attribute) {

        attribute.setType(beanProperty.getType());

        AnnotationMirror attributeAnnotarion = beanProperty.getAnnotationMirror(Attribute.class);
        if (attributeAnnotarion == null) {
            attribute.setGenerate(!beanProperty.isExists());
            attribute.setDescription(beanProperty.getDocComment());
            attribute.setHidden(true);

            if (attribute.getType().isPrimitive()) {
                String value = getPimitiveDefaultValue(attribute.getType().getName());
                if (value != null) {
                    attribute.setDefaultValue(value);
                }
            }
        } else {
            SourceUtils utils = utilsProvider.get();
            utils.setModelProperty(attribute, attributeAnnotarion, "hidden");
            utils.setModelProperty(attribute, attributeAnnotarion, "literal");
            utils.setModelProperty(attribute, attributeAnnotarion, "passThrough");
            utils.setModelProperty(attribute, attributeAnnotarion, "required");
            utils.setModelProperty(attribute, attributeAnnotarion, "readOnly");
            if (!utils.isDefaultValue(attributeAnnotarion, "generate")) {
                attribute.setGenerate(utils.getAnnotationValue(attributeAnnotarion, "generate", boolean.class));
            } else {
                attribute.setGenerate(!beanProperty.isExists());
            }

            descriptionProcessor.processDescription(attribute,
                    utils.getAnnotationValue(attributeAnnotarion, "description", AnnotationMirror.class),
                    beanProperty.getDocComment());

            setDefaultValue(attribute, attributeAnnotarion);

            utils.setModelProperty(attribute, attributeAnnotarion, "suggestedValue");

            // MethodExpression call signature.
            attribute.setSignature(getSignature(attributeAnnotarion));

            for (AnnotationMirror event : utils.getAnnotationValues(attributeAnnotarion, "events", AnnotationMirror.class)) {
                setBehaviorEvent(attribute, event);
            }
        }
    }

    private void setDefaultValue(PropertyBase attribute, AnnotationMirror attributeAnnotarion) {
        SourceUtils utils = utilsProvider.get();
        String defaultValue;
        // TODO - move to model validator.
        if (utils.isDefaultValue(attributeAnnotarion, "defaultValue")) {
            if (attribute.getType().isPrimitive()) {
                String pimitiveDefaultValue = getPimitiveDefaultValue(attribute.getType().getName());
                attribute.setDefaultValue(pimitiveDefaultValue);
            }
        } else {
            defaultValue = utils.getAnnotationValue(attributeAnnotarion, "defaultValue", String.class);
            if (STRING_TYPE.equals(attribute.getType())) {
                defaultValue = JavaUtils.getEscapedString(defaultValue);
            }
            attribute.setDefaultValue(defaultValue);
        }
    }

    private String getPimitiveDefaultValue(String typeName) {
        if (isInstace(boolean.class, typeName)) {
            return "false";
        } else if (isInstace(int.class, typeName)) {
            return "Integer.MIN_VALUE";
        } else if (isInstace(long.class, typeName)) {
            return "Long.MIN_VALUE";
        } else if (isInstace(byte.class, typeName)) {
            return "Byte.MIN_VALUE";
        } else if (isInstace(short.class, typeName)) {
            return "Short.MIN_VALUE";
        } else if (isInstace(float.class, typeName)) {
            return "Float.MIN_VALUE";
        } else if (isInstace(double.class, typeName)) {
            return "Double.MIN_VALUE";
        } else if (isInstace(char.class, typeName)) {
            return "Character.MIN_VALUE";
        }
        return null;
    }

    private boolean isInstace(Class<?> byteClass, String typeName) {
        return byteClass.getSimpleName().equals(typeName);
    }

    private MethodSignature getSignature(AnnotationMirror attributeAnnotarion) {

        SourceUtils utils = utilsProvider.get();

        if (!utils.isDefaultValue(attributeAnnotarion, "signature")) {
            AnnotationMirror signatureAnnotation = utils.getAnnotationValue(attributeAnnotarion, "signature",
                    AnnotationMirror.class);
            ClassName returnType = utils.getAnnotationValue(signatureAnnotation, "returnType", ClassName.class);

            if (!SIGNATURE_NONE_CLASS_NAME.equals(returnType)) {
                MethodSignature methodSignature = new MethodSignature();
                methodSignature.setParameters(Lists.newArrayList(utils.getAnnotationValues(signatureAnnotation, "parameters",
                        ClassName.class)));
                methodSignature.setReturnType(returnType);
                return methodSignature;
            }
        }
        return null;
    }

    private void setBehaviorEvent(PropertyBase attribute, AnnotationMirror eventMirror) {
        if (null != eventMirror) {
            SourceUtils utils = utilsProvider.get();
            org.richfaces.cdk.model.EventName event = new org.richfaces.cdk.model.EventName();
            utils.setModelProperty(event, eventMirror, "name", "value");
            utils.setModelProperty(event, eventMirror, "defaultEvent");
            attribute.getEventNames().add(event);
        }
    }

    @Override
    public void processType(final BeanModelBase component, TypeElement element) throws CdkException {
        log.debug("AttributesProcessorImpl.processType");
        log.debug("  -> component = " + component);
        log.debug("  -> typeElement = " + element);

        log.debug("  -- Process XML files with standard attributes definitions.");
        log.debug("     -> sourceUtils.visitSupertypes...");
        SourceUtils sourceUtils = getSourceUtils();
        sourceUtils.visitSupertypes(element, new SuperTypeVisitor() {
            @Override
            public void visit(TypeMirror type) {
                String uri = type.toString() + ".xml";
                try {
                    log.debug("        -> visit - " + type.toString());
                    component.getAttributes().addAll(parseProperties(uri));
                } catch (CdkException e) {
                    log.error(e);
                } catch (FileNotFoundException e) {
                    log.debug("No properties description found at " + uri);
                }
            }
        });

        log.debug("  -- Process Java files.");
        Set<BeanProperty> properties = Sets.newHashSet();
        properties.addAll(sourceUtils.getBeanPropertiesAnnotatedWith(Attribute.class, element));
        // properties.addAll(sourceUtils.getAbstractBeanProperties(element));
        for (BeanProperty beanProperty : properties) {
            processAttribute(beanProperty, component.getOrCreateAttribute(beanProperty.getName()));
        }
    }

    private Collection<? extends PropertyBase> parseProperties(String uri) throws FileNotFoundException {
        return parser.parseProperties(CdkEntityResolver.URN_ATTRIBUTES + uri);
    }

    private SourceUtils getSourceUtils() {
        return utilsProvider.get();
    }

    @Override
    public void processXmlFragment(BeanModelBase component, String... attributesConfig) {
        // Process all files from @Jsf.. attributes property.
        for (String attributes : attributesConfig) {
            try {
                component.getAttributes().addAll(parseProperties(attributes));
            } catch (CdkException e) {
                log.error(e);
            } catch (FileNotFoundException e) {
                log.error("No properties description found at " + attributes);
            }
        }
    }
}
