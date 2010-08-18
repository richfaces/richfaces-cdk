package org.richfaces.cdk.apt;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.InvalidNameException;
import org.richfaces.cdk.util.PropertyUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class AptSourceUtils implements SourceUtils {
    private static final Set<String> PROPERTIES =
        new HashSet<String>(Arrays.asList("getEventNames", "getDefaultEventName", "getClientBehaviors", "getFamily"));

    private final ProcessingEnvironment processingEnv;

    @Inject
    private Logger log;

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param processingEnv
     */
    public AptSourceUtils(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    /**
     * <p class="changed_added_4_0">
     * Get all fields and bean properties that are annotated with given annotation.
     * </p>
     * 
     * @param annotation
     * @param type
     * @return
     */
    public Set<BeanProperty> getBeanPropertiesAnnotatedWith(Class<? extends Annotation> annotation, TypeElement type) {
        Set<BeanProperty> properties = new HashSet<BeanProperty>();
        List<? extends Element> members = this.processingEnv.getElementUtils().getAllMembers(type);

        // Get all methods and fields annotated by annotation.
        for (Element childElement : members) {
            boolean annotated = null != childElement.getAnnotation(annotation);
            if (!annotated) {
                continue;
            }

            // Have an annotation, infer property name.
            if (ElementKind.METHOD.equals(childElement.getKind())) {
                processMethod(properties, childElement, annotated);
            } else if (ElementKind.FIELD.equals(childElement.getKind())) {
                processFiled(properties, childElement);
            }

            // TODO - merge properties with same name ?
        }

        return properties;
    }

    public Set<BeanProperty> getAbstractBeanProperties(TypeElement type) {
        log.debug("AptSourceUtils.getAbstractBeanProperties");
        log.debug("  - type = " + type);

        Set<BeanProperty> properties = new HashSet<BeanProperty>();
        List<? extends Element> members = this.processingEnv.getElementUtils().getAllMembers(type);

        Map<String, List<ExecutableElement>> props = groupMethodsBySignature(members);
        removeNotAbstractGroups(props);

        for (List<ExecutableElement> methods : props.values()) {
            ExecutableElement method = methods.get(0);

            if (ElementKind.METHOD.equals(method.getKind()) && !PROPERTIES.contains(method.getSimpleName().toString())) {
                processMethod(properties, method, false);
            }

            // TODO - merge properties with same name ?
        }

        return properties;
    }

    private void removeNotAbstractGroups(Map<String, List<ExecutableElement>> props) {
        List<String> removeKeys = new ArrayList<String>();
        for (Map.Entry<String, List<ExecutableElement>> entry : props.entrySet()) {
            List<ExecutableElement> value = entry.getValue();
            for (ExecutableElement element : value) {
                if (!isAbstract(element)) {
                    removeKeys.add(entry.getKey());
                }
            }
        }

        for (String removeKey : removeKeys) {
            props.remove(removeKey);
        }
    }

    private Map<String, List<ExecutableElement>> groupMethodsBySignature(List<? extends Element> members) {
        Map<String, List<ExecutableElement>> props = new HashMap<String, List<ExecutableElement>>();
        for (Element element : members) {
            if (ElementKind.METHOD.equals(element.getKind())
                && !PROPERTIES.contains(element.getSimpleName().toString())) {

                ExecutableElement method = (ExecutableElement) element;

                String signature = getSignature(method);

                List<ExecutableElement> methods = props.get(signature);
                if (methods == null) {
                    methods = new ArrayList<ExecutableElement>(5);
                    props.put(signature, methods);
                }

                methods.add(method);
            }
        }
        return props;
    }

    private String getSignature(ExecutableElement method) {
        String name = method.getSimpleName().toString();
        List<? extends VariableElement> methodParams = method.getParameters();
        StringBuilder builder = new StringBuilder(name);
        for (VariableElement methodParam : methodParams) {
            builder.append(":").append(methodParam.getKind().name());
        }
        return builder.toString();
    }

    private void processFiled(Set<BeanProperty> properties, Element childElement) {
        AptBeanProperty property = new AptBeanProperty(childElement.getSimpleName().toString());

        property.type = asClassDescription(childElement.asType());
        property.element = childElement;

        // TODO - find getter/setter, check them for abstract.
        property.exists = true;

        properties.add(property);
    }

    private void processMethod(Set<BeanProperty> properties, Element childElement, boolean annotated) {
        ExecutableElement method = (ExecutableElement) childElement;
        boolean exists = !isAbstract(method);
        if (!annotated && exists) {
            log.debug("      - " + childElement.getSimpleName() + " : didn't annotated and didn't abstract.");
            return;
        }

        TypeMirror propertyType = method.getReturnType();
        List<? extends VariableElement> parameters = method.getParameters();
        if (TypeKind.VOID.equals(propertyType.getKind()) && 1 == parameters.size()) {

            // That is setter method, get type from parameter.
            propertyType = parameters.get(0).asType();
        } else if (!parameters.isEmpty()) {
            // TODO Invalid method signature for a bean property,
            // throw exception ?
            log.debug("      - " + childElement.getSimpleName() + " : Invalid method signature for a bean property.");
            return;
        }

        try {
            String name = PropertyUtils.methodToName(childElement.getSimpleName().toString());
            AptBeanProperty property = new AptBeanProperty(name);

            property.type = asClassDescription(propertyType);
            property.element = childElement;
            property.exists = exists;

            properties.add(property);
            log.debug("      - " + childElement.getSimpleName() + " : was added.");

        } catch (InvalidNameException e) {
            log.debug("      - " + childElement.getSimpleName() + " : Invalid method name for a bean property, throw.");

            // TODO Invalid method name for a bean property, throw
            // exception ?
        }
    }

    private boolean isAbstract(ExecutableElement method) {
        return method.getModifiers().contains(Modifier.ABSTRACT);
    }

    private ClassName asClassDescription(TypeMirror type) {
        return new ClassName(type.toString());
    }

    public String getDocComment(Element componentElement) {
        return this.processingEnv.getElementUtils().getDocComment(componentElement);
    }

    @Override
    public AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationType) {
        List<? extends AnnotationMirror> annotationMirrors =
            processingEnv.getElementUtils().getAllAnnotationMirrors(element);
        TypeMirror annotationTypeMirror =
            processingEnv.getElementUtils().getTypeElement(annotationType.getName()).asType();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (processingEnv.getTypeUtils().isSameType(annotationTypeMirror, annotationMirror.getAnnotationType())) {
                return annotationMirror;
            }
        }
        return null;
    }

    @Override
    public boolean isAnnotationPresent(Element element, Class<? extends Annotation> annotationType) {
        return null != element.getAnnotation(annotationType);
    }

    
    @Override
    public boolean isAnnotationPropertyPresent(AnnotationMirror annotation, final String propertyName){
        return Iterables.any(getAnnotationValuesMap(annotation).entrySet(), new AnnotationAttributePredicate(propertyName));
    }
    
    @Override
    public boolean isDefaultValue(AnnotationMirror annotation, String propertyName) {
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> attributeEntry =
            findAnnotationProperty(annotation, propertyName);
        return !annotation.getElementValues().containsKey(attributeEntry.getKey());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAnnotationValue(AnnotationMirror annotation, String propertyName, Class<T> expectedType) {
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> attributeEntry =
            findAnnotationProperty(annotation, propertyName);
        AnnotationValue annotationValue = attributeEntry.getValue();
        return convertAnnotationValue(expectedType, annotationValue);
    }

    private <T> T convertAnnotationValue(Class<T> expectedType, AnnotationValue annotationValue) {
        if (Enum.class.isAssignableFrom(expectedType)) {
            VariableElement variable = (VariableElement) annotationValue.getValue();
            return (T) Enum.valueOf((Class<? extends Enum>) expectedType, variable.getSimpleName().toString());
        } else if (ClassName.class.equals(expectedType)) {
            TypeMirror value = (TypeMirror) annotationValue.getValue();
            return (T) ClassName.get(value.toString());
        } else if (AnnotationMirror.class.isAssignableFrom(expectedType)) {
            AnnotationMirror value = (AnnotationMirror) annotationValue.getValue();
            return (T) value;
        } else {
            // TODO - check value for expected type.
            return (T) annotationValue.getValue();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getAnnotationValues(AnnotationMirror annotation, String propertyName, Class<T> expectedType) {
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> attributeEntry =
            findAnnotationProperty(annotation, propertyName);
        List<? extends AnnotationValue>annotationValues = (List<? extends AnnotationValue>) attributeEntry.getValue().getValue();
        List<T> values = Lists.newArrayList();
        for (AnnotationValue annotationValue : annotationValues) {
            values.add(convertAnnotationValue(expectedType, annotationValue));
        }
        return values;
    }

    private Entry<? extends ExecutableElement, ? extends AnnotationValue> findAnnotationProperty(
        AnnotationMirror annotation, final String propertyName) {
        try {
            return Iterables.find(getAnnotationValuesMap(annotation).entrySet(),
                new AnnotationAttributePredicate(propertyName));
        } catch (NoSuchElementException e) {
            throw new CdkException("Attribute " + propertyName + " not found for annotation "
                + annotation.getAnnotationType().toString());
        }
    }

    private Map<? extends ExecutableElement, ? extends AnnotationValue> getAnnotationValuesMap(AnnotationMirror annotation) {
        return processingEnv.getElementUtils().getElementValuesWithDefaults(annotation);
    }

    /**
     * <p class="changed_added_4_0">Set model property to the corresponding annotation attribute, if annotation attribute set to non-default value.</p>
     * @param model Model object.
     * @param annotation annotation to copy property from. 
     * @param modelProperty bean attribute name in the model and annotation.
     */
    @Override
    public void setModelProperty(Object model, AnnotationMirror annotation, String modelProperty) {
        setModelProperty(model, annotation,  modelProperty, modelProperty);
    }

    /**
     * <p class="changed_added_4_0">Set model property to the corresponding annotation attribute, if annotation attribute set to non-default value.</p>
     * @param model Model object.
     * @param annotation annotation to copy property from.
     * @param modelProperty bean attribute name in model.
     * @param annotationAttribute annotation attribute name.
     */
    @Override
    public void setModelProperty(Object model, AnnotationMirror annotation, 
        String modelProperty, String annotationAttribute) {
        if (!isDefaultValue(annotation, annotationAttribute)) {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(model, modelProperty);
            PropertyUtils.setPropertyValue(model, modelProperty, getAnnotationValue(annotation,
                annotationAttribute, propertyDescriptor.getPropertyType()));
        }
    }

    public Object getConstant(TypeElement componentElement, String name) {
        List<VariableElement> fieldsIn =
            ElementFilter.fieldsIn(this.processingEnv.getElementUtils().getAllMembers(componentElement));
        Object value = null;

        for (VariableElement field : fieldsIn) {
            Set<Modifier> modifiers = field.getModifiers();

            if (modifiers.contains(Modifier.FINAL) && modifiers.contains(Modifier.STATIC)
                && field.getSimpleName().toString().equals(name)) {
                value = field.getConstantValue();
            }
        }
        return value;
    }

    public void visitSupertypes(TypeElement type, SuperTypeVisitor visitor) {
        visitSupertypes(type.asType(), visitor);
    }

    private void visitSupertypes(TypeMirror type, SuperTypeVisitor visitor) {
        List<? extends TypeMirror> supertypes = this.processingEnv.getTypeUtils().directSupertypes(type);
        for (TypeMirror typeMirror : supertypes) {
            visitSupertypes(typeMirror, visitor);
        }
        visitor.visit(type);
    }

    @Override
    public TypeElement asTypeElement(ClassName type) {
        return processingEnv.getElementUtils().getTypeElement(type.toString());
    }

    @Override
    public TypeElement asTypeElement(TypeMirror mirror) {
        if (TypeKind.DECLARED.equals(mirror.getKind())) {
            return (TypeElement) processingEnv.getTypeUtils().asElement(mirror);
        } else {
            return null;
        }
    }

    private static final class AnnotationAttributePredicate implements
        Predicate<Map.Entry<? extends ExecutableElement, ? extends AnnotationValue>> {
        private final String propertyName;

        private AnnotationAttributePredicate(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public boolean apply(Entry<? extends ExecutableElement, ? extends AnnotationValue> input) {

            return this.propertyName.equals(input.getKey().getSimpleName().toString());
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @author asmirnov@exadel.com
     * 
     */
    protected final class AptBeanProperty implements BeanProperty {
        private Element element;
        private boolean exists;
        private final String name;
        private ClassName type;

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @param name
         */
        public AptBeanProperty(String name) {
            this.name = name;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + ((name == null) ? 0 : name.hashCode());

            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            AptBeanProperty other = (AptBeanProperty) obj;

            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }

            return true;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * <p class="changed_added_4_0">
         * Get JavaDoc comment of appropriate bean property element.
         * </p>
         * 
         * @return
         */
        public String getDocComment() {
            return processingEnv.getElementUtils().getDocComment(element);
        }

        public ClassName getType() {
            return type;
        }

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @return the exists
         */
        public boolean isExists() {
            return exists;
        }

        public AnnotationMirror getAnnotationMirror(Class<? extends Annotation> annotationType) {
            return AptSourceUtils.this.getAnnotationMirror(element, annotationType);
        }

        public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
            return element.getAnnotation(annotationType);
        }
    }

}
