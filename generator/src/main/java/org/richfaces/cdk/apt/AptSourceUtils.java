package org.richfaces.cdk.apt;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.util.PropertyUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * Implementation to use in annotation processor.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class AptSourceUtils implements SourceUtils {
    private static final String IS = "is";
    private static final int IS_LENGTH = IS.length();
    private static final String GET = "get";
    private static final String SET = "set";
    private static final int SET_LENGTH = SET.length();
    private static final int GET_LENGTH = GET.length();
    private static final ImmutableSet<String> HIDDEN_PROPERTIES = ImmutableSet.of("eventNames", "defaultEventName",
            "clientBehaviors", "family", "class");
    private final ProcessingEnvironment processingEnv;

    private Map<Name, Map<String, AptBeanProperty>> beanPropertyCache = Maps.newHashMap();

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
        Map<String, AptBeanProperty> beanProperties = getBeanProperties(type);
        for (BeanProperty beanProperty : beanProperties.values()) {
            if (beanProperty.isAnnotationPresent(annotation)) {
                properties.add(beanProperty);
            }
        }
        return properties;
    }

    public Set<BeanProperty> getAbstractBeanProperties(TypeElement type) {
        Set<BeanProperty> properties = new HashSet<BeanProperty>();
        Map<String, AptBeanProperty> beanProperties = getBeanProperties(type);
        for (BeanProperty beanProperty : beanProperties.values()) {
            if (!beanProperty.isExists()) {
                properties.add(beanProperty);
            }
        }
        return properties;
    }

    @Override
    public BeanProperty getBeanProperty(ClassName type, final String name) {
        return getBeanProperty(asTypeElement(type), name);
    }

    @Override
    public BeanProperty getBeanProperty(TypeElement type, final String name) {
        Map<String, AptBeanProperty> beanProperties = getBeanProperties(type);
        if (beanProperties.containsKey(name)) {
            return beanProperties.get(name);
        } else {
            return new DummyPropertyImpl(name);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Utility method to get all bean properties, similar to Introspector
     * </p>
     *
     * @param type
     * @return
     */
    Map<String, AptBeanProperty> getBeanProperties(TypeElement type) {
        if (null == type) {
            return Collections.emptyMap();
        }

        Name qName = type.getQualifiedName();
        Map<String, AptBeanProperty> result = beanPropertyCache.get(qName);
        if (result != null) {
            return result;
        }

        result = Maps.newHashMap();
        List<? extends Element> members = this.processingEnv.getElementUtils().getAllMembers(type);
        // extract all getters/setters.
        for (Element element : members) {
            if (ElementKind.METHOD.equals(element.getKind())) {
                ExecutableElement method = (ExecutableElement) element;
                processMethod(type, result, method);
            }
        }
        beanPropertyCache.put(qName, result);
        return result;
    }

    private void processMethod(TypeElement type, Map<String, AptBeanProperty> result, ExecutableElement method) {
        if (isPublicNonStatic(method)) {
            if (isGetter(method)) {
                processBeanPropertyAccessor(type, result, method, false);
            } else if (isSetter(method)) {
                processBeanPropertyAccessor(type, result, method, true);
            }
        }
    }

    private void processBeanPropertyAccessor(TypeElement type, Map<String, AptBeanProperty> result, ExecutableElement method,
            boolean setter) {
        String propertyName = getPropertyName(method);
        if (!HIDDEN_PROPERTIES.contains(propertyName)) {
            ClassName propertyType = asClassDescription(setter ? method.getParameters().get(0).asType() : method
                    .getReturnType());
            if (result.containsKey(propertyName)) {
                // Merge property with existed one.
                AptBeanProperty beanProperty = result.get(propertyName);
                checkPropertyType(type, propertyName, propertyType, beanProperty);
                if (null != (setter ? beanProperty.setter : beanProperty.getter)) {
                    log.debug("Two " + (setter ? "setter" : "getter") + " methods for the same bean property " + propertyName
                            + " in the class " + type.getQualifiedName());
                    if (!method.getModifiers().contains(Modifier.ABSTRACT)) {
                        beanProperty.setAccessMethod(method, setter);
                    }
                } else {
                    beanProperty.setAccessMethod(method, setter);
                }
            } else {
                AptBeanProperty beanProperty = new AptBeanProperty(propertyName);
                beanProperty.setAccessMethod(method, setter);
                beanProperty.type = propertyType;
                result.put(propertyName, beanProperty);
            }
        }
    }

    private String getPropertyName(ExecutableElement method) {
        return PropertyUtils.methodToName(method.getSimpleName().toString());
    }

    private void checkPropertyType(TypeElement type, String propertyName, ClassName propertyType, AptBeanProperty beanProperty) {
        if (!propertyType.equals(beanProperty.type)) {
            log.warn("Unambiguious type for bean property " + propertyName + " in the class " + type.getQualifiedName());
        }
    }

    private boolean isAbstract(ExecutableElement method) {
        return method.getModifiers().contains(Modifier.ABSTRACT);
    }

    private boolean isPublicNonStatic(ExecutableElement method) {
        Set<Modifier> modifiers = method.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.STATIC);
    }

    private boolean isGetter(ExecutableElement e) {
        String methodName = e.getSimpleName().toString();
        return (isGetterName(methodName) || isBooleanGetterName(methodName)) && 0 == e.getParameters().size();
    }

    private boolean isGetterName(String methodName) {
        return methodName.startsWith(GET) && methodName.length() > GET_LENGTH
                && Character.isUpperCase(methodName.charAt(GET_LENGTH));
    }

    private boolean isBooleanGetterName(String methodName) {
        return methodName.startsWith(IS) && methodName.length() > IS_LENGTH
                && Character.isUpperCase(methodName.charAt(IS_LENGTH));
    }

    private boolean isSetter(ExecutableElement e) {
        String methodName = e.getSimpleName().toString();
        return isSetterName(methodName) && 1 == e.getParameters().size() && !e.isVarArgs()
                && TypeKind.VOID.equals(e.getReturnType().getKind());
    }

    private boolean isSetterName(String methodName) {
        return methodName.startsWith(SET) && methodName.length() > SET_LENGTH
                && Character.isUpperCase(methodName.charAt(SET_LENGTH));
    }

    private ClassName asClassDescription(TypeMirror type) {
        return new ClassName(type.toString());
    }

    public String getDocComment(Element componentElement) {
        return this.processingEnv.getElementUtils().getDocComment(componentElement);
    }

    @Override
    public AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationType) {
        List<? extends AnnotationMirror> annotationMirrors = processingEnv.getElementUtils().getAllAnnotationMirrors(element);
        TypeMirror annotationTypeMirror = processingEnv.getElementUtils().getTypeElement(annotationType.getName()).asType();
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
    public boolean isAnnotationPropertyPresent(AnnotationMirror annotation, final String propertyName) {
        return Iterables.any(getAnnotationValuesMap(annotation).entrySet(), new AnnotationAttributePredicate(propertyName));
    }

    @Override
    public boolean isDefaultValue(AnnotationMirror annotation, String propertyName) {
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> attributeEntry = findAnnotationProperty(annotation,
                propertyName);
        return !annotation.getElementValues().containsKey(attributeEntry.getKey());
    }

    @Override
    public <T> T getAnnotationValue(AnnotationMirror annotation, String propertyName, Class<T> expectedType) {
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> attributeEntry = findAnnotationProperty(annotation,
                propertyName);
        AnnotationValue annotationValue = attributeEntry.getValue();
        return convertAnnotationValue(expectedType, annotationValue);
    }

    @SuppressWarnings("unchecked")
    private <T> T convertAnnotationValue(Class<T> expectedType, AnnotationValue annotationValue) {
        if (Enum.class.isAssignableFrom(expectedType)) {
            VariableElement variable = (VariableElement) annotationValue.getValue();
            return (T) Enum.valueOf((Class<? extends Enum>) expectedType, variable.getSimpleName().toString());
        } else if (ClassName.class.equals(expectedType)) {
            Object value = annotationValue.getValue();
            return (T) ClassName.get(value.toString());
        } else if (FacesId.class.equals(expectedType)) {
            String value = (String) annotationValue.getValue();
            return (T) FacesId.parseId(value);
        } else if (AnnotationMirror.class.isAssignableFrom(expectedType)) {
            AnnotationMirror value = (AnnotationMirror) annotationValue.getValue();
            return (T) value;
        } else {
            return (T) annotationValue.getValue();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Iterable<T> getAnnotationValues(AnnotationMirror annotation, String propertyName, Class<T> expectedType) {
        Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> attributeEntry = findAnnotationProperty(annotation,
                propertyName);
        List<? extends AnnotationValue> annotationValues = (List<? extends AnnotationValue>) attributeEntry.getValue()
                .getValue();
        List<T> values = Lists.newArrayList();
        for (AnnotationValue annotationValue : annotationValues) {
            values.add(convertAnnotationValue(expectedType, annotationValue));
        }
        return values;
    }

    private Entry<? extends ExecutableElement, ? extends AnnotationValue> findAnnotationProperty(AnnotationMirror annotation,
            final String propertyName) {
        try {
            return Iterables
                    .find(getAnnotationValuesMap(annotation).entrySet(), new AnnotationAttributePredicate(propertyName));
        } catch (NoSuchElementException e) {
            throw new CdkException("Attribute " + propertyName + " not found for annotation "
                    + annotation.getAnnotationType().toString());
        }
    }

    private Map<? extends ExecutableElement, ? extends AnnotationValue> getAnnotationValuesMap(AnnotationMirror annotation) {
        return processingEnv.getElementUtils().getElementValuesWithDefaults(annotation);
    }

    /**
     * <p class="changed_added_4_0">
     * Set model property to the corresponding annotation attribute, if annotation attribute set to non-default value.
     * </p>
     *
     * @param model Model object.
     * @param annotation annotation to copy property from.
     * @param modelProperty bean attribute name in the model and annotation.
     */
    @Override
    public void setModelProperty(Object model, AnnotationMirror annotation, String modelProperty) {
        setModelProperty(model, annotation, modelProperty, modelProperty);
    }

    /**
     * <p class="changed_added_4_0">
     * Set model property to the corresponding annotation attribute, if annotation attribute set to non-default value.
     * </p>
     *
     * @param model Model object.
     * @param annotation annotation to copy property from.
     * @param modelProperty bean attribute name in model.
     * @param annotationAttribute annotation attribute name.
     */
    @Override
    public void setModelProperty(Object model, AnnotationMirror annotation, String modelProperty, String annotationAttribute) {
        if (!isDefaultValue(annotation, annotationAttribute)) {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(model, modelProperty);
            PropertyUtils.setPropertyValue(model, modelProperty,
                    getAnnotationValue(annotation, annotationAttribute, propertyDescriptor.getPropertyType()));
        }
    }

    public Object getConstant(TypeElement componentElement, String name) {
        List<VariableElement> fieldsIn = ElementFilter.fieldsIn(this.processingEnv.getElementUtils().getAllMembers(
                componentElement));
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

    private TypeElement asTypeElement(ClassName type) {
        return processingEnv.getElementUtils().getTypeElement(type.toString());
    }

    public boolean isClassExists(ClassName type) {
        return null != asTypeElement(type);
    }

    @Override
    public TypeElement asTypeElement(TypeMirror mirror) {
        if (TypeKind.DECLARED.equals(mirror.getKind())) {
            return (TypeElement) processingEnv.getTypeUtils().asElement(mirror);
        } else {
            return null;
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @author asmirnov@exadel.com
     *
     */
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
        private ExecutableElement getter;
        private ExecutableElement setter;
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

        void setAccessMethod(ExecutableElement method, boolean setter) {
            if (setter) {
                this.setter = method;
            } else {
                this.getter = method;
            }
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
            String comment = getMethodComment(getter);
            if (null == comment) {
                comment = getMethodComment(setter);
            }
            return comment;
        }

        private String getMethodComment(ExecutableElement method) {
            if (null != method) {
                return processingEnv.getElementUtils().getDocComment(method);
            } else {
                return null;
            }
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
            return !(isAbstract(getter) || isAbstract(setter));
        }

        private boolean isAbstract(ExecutableElement method) {
            return null != method && method.getModifiers().contains(Modifier.ABSTRACT);
        }

        public AnnotationMirror getAnnotationMirror(Class<? extends Annotation> annotationType) {
            if (isAnnotationPresent(getter, annotationType)) {
                return AptSourceUtils.this.getAnnotationMirror(getter, annotationType);
            } else if (isAnnotationPresent(setter, annotationType)) {
                return AptSourceUtils.this.getAnnotationMirror(setter, annotationType);
            }
            return null;
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            return isAnnotationPresent(getter, annotationType) || isAnnotationPresent(setter, annotationType);
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
            if (isAnnotationPresent(getter, annotationType)) {
                return getter.getAnnotation(annotationType);
            } else if (isAnnotationPresent(setter, annotationType)) {
                return setter.getAnnotation(annotationType);
            }
            return null;
        }

        private <T extends Annotation> boolean isAnnotationPresent(ExecutableElement method, Class<T> annotationType) {
            return null != method && null != method.getAnnotation(annotationType);
        }

        @Override
        public ACCESS_TYPE getAccessType() {
            if (null != getter && null != setter) {
                return ACCESS_TYPE.readWrite;
            } else if (null == setter) {
                return ACCESS_TYPE.readOnly;
            }
            return ACCESS_TYPE.writeOnly;
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

        @Override
        public String toString() {
            return name + "[" + getType() + "]";
        }
    }
}
