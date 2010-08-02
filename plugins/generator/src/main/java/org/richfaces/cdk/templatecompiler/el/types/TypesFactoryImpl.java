/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.templatecompiler.el.types;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.FacesEvent;
import javax.faces.model.DataModel;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;

import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.util.ArrayUtils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 * 
 */
public final class TypesFactoryImpl implements TypesFactory {

    static final PropertyDescriptor[] EMPTY_PROPERTY_DESCRIPTORS = new PropertyDescriptor[0];

    static final ImmutableMap<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_CLASSES_MAP = ImmutableMap
        .<Class<?>, Class<?>> builder().put(Boolean.TYPE, Boolean.class).put(Float.TYPE, Float.class)
        .put(Long.TYPE, Long.class).put(Integer.TYPE, Integer.class).put(Short.TYPE, Short.class)
        .put(Byte.TYPE, Byte.class).put(Double.TYPE, Double.class).put(Character.TYPE, Character.class).build();

    static final ImmutableMap<String, Class<?>> PRIMITIVE_CLASSES_MAP;

    static {

        Builder<String, Class<?>> builder = ImmutableMap.<String, Class<?>> builder();
        for (Class<?> primitiveClass : PRIMITIVE_TO_WRAPPER_CLASSES_MAP.keySet()) {
            builder.put(primitiveClass.getName(), primitiveClass);
        }
        PRIMITIVE_CLASSES_MAP = builder.build();
    }

    private static final Pattern CLASS_SIGNATURE_PATTERN = Pattern.compile("^" + "\\s*([^\\[<]+)\\s*" + // class name
        "(?:<\\s*(.*)\\s*>)?\\s*" + // generic signature
        "((?:\\[\\s*\\]\\s*)+)?\\s*" + // array signature
        "$");

    private static final int CLASS_NAME_GROUP_IDX = 1;

    private static final int TYPE_ARGUMENTS_GROUP_IDX = 2;

    private static final int ARRAY_SIGNATURE_GROUP_IDX = 3;

    private static final int ARRAY_SIGNATURE_LENGTH = "[]".length();

    private static final Function<Class<?>, String> PACKAGE_NAME_FUNCTION = new Function<Class<?>, String>() {

        @Override
        public String apply(Class<?> from) {
            return from.getPackage().getName();
        }
    };

    private static final ImmutableCollection<String> GUESS_PACKAGES = ImmutableSet.<String> copyOf(Iterables.transform(
        ImmutableSet.<Class<?>> of(UIComponent.class, Behavior.class, Converter.class, Validator.class,
            FacesContext.class, Application.class, FacesEvent.class, DataModel.class, Renderer.class, Collection.class,
            Object.class), PACKAGE_NAME_FUNCTION));

    private final ClassLoader classLoader;

    private final Logger log;

    private final Map<java.lang.reflect.Type, ELType> reflectionTypesCache =
        new ConcurrentHashMap<java.lang.reflect.Type, ELType>();

    private final Map<String, ELType> refferencedTypesCache = new ConcurrentHashMap<String, ELType>();

    private final Map<Class<?>, ClassDataHolder> classDataCache = Maps.newHashMap();

    @Inject
    public TypesFactoryImpl(Logger log, CdkClassLoader classLoader) {
        this.log = log;
        this.classLoader = classLoader;
    }

    private ELType getPlainClassType(Class<?> plainClass) {
        ELType plainClassType = reflectionTypesCache.get(plainClass);
        if (plainClassType == null) {
            plainClassType = new PlainClassType(plainClass);
            reflectionTypesCache.put(plainClass, plainClassType);
        }

        return plainClassType;
    }

    private ELType getReferencedType(String classCodeString) {
        ELType type = refferencedTypesCache.get(classCodeString);
        if (type == null) {
            type = new ReferencedType(classCodeString);
            refferencedTypesCache.put(classCodeString, type);
        }

        return type;
    }

    private Class<?> tryLoadClas(String className) throws ClassNotFoundException {
        int dotIndex = className.indexOf('.');
        if (dotIndex < 0) {
            // guess type
            for (String guessPackage : GUESS_PACKAGES) {
                String guessTypeName = guessPackage + "." + className;
                try {
                    // while by default initialize = true for Class.forName(String) method
                    // initialize = false used here prevents loading of dependencies that
                    // are accessible only in runtime, e.g. log initializer from API
                    // depends on the concrete logger implementation provided in runtime only
                    return Class.forName(guessTypeName, false, classLoader);
                } catch (ClassNotFoundException e) {
                    // ignore
                } catch (LinkageError e) {
                    if (log.isInfoEnabled()) {
                        log.info(MessageFormat.format("Class {0} couldn''t be loaded because of: {1}", guessTypeName,
                            e.getMessage()));
                    }
                }
            }
        }

        Class<?> result = PRIMITIVE_CLASSES_MAP.get(className);
        if (result == null) {
            try {
                // initialize = false here for the same reason as already mentioned for the previous load block
                result = Class.forName(className, false, classLoader);
            } catch (LinkageError e) {
                String errorMessage =
                    MessageFormat.format("Class {0} couldn''t be loaded because of: {1}", className, e.getMessage());
                if (log.isInfoEnabled()) {
                    log.info(errorMessage);
                }
                throw new ClassNotFoundException(errorMessage, e);
            }
        }

        return result;
    }

    ELType[] parseTypeArgumentsString(String typeArguments) {
        if (typeArguments == null) {
            return PlainClassType.NO_TYPES;
        }

        String[] typeArgumentsSplit = typeArguments.trim().split(",");

        ELType[] types = new ELType[typeArgumentsSplit.length];
        for (int i = 0; i < typeArgumentsSplit.length; i++) {
            types[i] = getType(typeArgumentsSplit[i]);
        }

        return types;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.templatecompiler.el.types.TypesFactory#getType(java.lang.String)
     */
    @Override
    public ELType getType(String typeString) {
        Matcher matcher = CLASS_SIGNATURE_PATTERN.matcher(typeString);
        boolean matchResult = matcher.matches();
        if (matchResult) {
            String className = matcher.group(CLASS_NAME_GROUP_IDX).trim();

            String typeArgumentsString = matcher.group(TYPE_ARGUMENTS_GROUP_IDX);
            ELType[] typeArguments = parseTypeArgumentsString(typeArgumentsString);

            String arraySignature = matcher.group(ARRAY_SIGNATURE_GROUP_IDX);
            int arrayDepth = 0;
            if (arraySignature != null) {
                arrayDepth = arraySignature.replaceAll("\\s+", "").length() / ARRAY_SIGNATURE_LENGTH;
            }

            ELType baseType;
            try {
                // NB: loadedClass can have name that differs from className!
                Class<?> loadedClas = tryLoadClas(className);

                baseType = getType(loadedClas);
            } catch (ClassNotFoundException e) {
                baseType = getReferencedType(className);
            }

            if (arrayDepth != 0 || !ArrayUtils.isEmpty(typeArguments)) {
                return new ComplexType(baseType, typeArguments, arrayDepth);
            } else {
                return baseType;
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn(MessageFormat.format("Cannot parse type signature: ''{0}''", typeString));
            }
            return getReferencedType(typeString);
        }
    }

    ELType createType(java.lang.reflect.Type reflectionType) {
        java.lang.reflect.Type[] actualTypeArguments = null;
        Class<?> rawType = null;
        int arrayDepth = 0;

        java.lang.reflect.Type localReflectionType = reflectionType;

        while (localReflectionType instanceof GenericArrayType) {
            localReflectionType = ((GenericArrayType) localReflectionType).getGenericComponentType();
            arrayDepth++;
        }

        if (localReflectionType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) localReflectionType;

            actualTypeArguments = parameterizedType.getActualTypeArguments();
            rawType = (Class<?>) parameterizedType.getRawType();
        } else if (localReflectionType instanceof Class<?>) {
            rawType = (Class<?>) localReflectionType;
        }

        if (rawType != null) {
            while (rawType.isArray()) {
                arrayDepth++;
                rawType = rawType.getComponentType();
            }

            ELType[] typeArguments = PlainClassType.NO_TYPES;
            if (!ArrayUtils.isEmpty(actualTypeArguments)) {
                typeArguments = getTypesArray(actualTypeArguments);
            }

            ELType clearComponentType = getPlainClassType(rawType);
            if (!ArrayUtils.isEmpty(typeArguments) || arrayDepth != 0) {
                return new ComplexType(clearComponentType, typeArguments, arrayDepth);
            } else {
                return clearComponentType;
            }
        } else {
            // TODO better way of handling unknown types
            return getReferencedType(reflectionType.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.templatecompiler.el.types.TypesFactory#getType(java.lang.reflect.Type)
     */
    @Override
    public ELType getType(java.lang.reflect.Type reflectionType) {
        ELType result = reflectionTypesCache.get(reflectionType);
        if (result == null) {
            result = createType(reflectionType);
            reflectionTypesCache.put(reflectionType, result);
        }

        return result;
    }

    private ELType[] getTypesArray(java.lang.reflect.Type[] reflectionTypes) {
        ELType[] types = new ELType[reflectionTypes.length];
        for (int i = 0; i < reflectionTypes.length; i++) {
            types[i] = getType(reflectionTypes[i]);
        }

        return types;
    }

    /**
     * Returns wrapper classes for passed-in class. If type is primitive, then corresponding wrapper class is returned
     * (e.g. boolean -> Boolean), otherwise does nothing and returns passed-in class.
     * 
     * @return wrapper for primitive types, or passed-in class
     */
    static Class<?> getWrapperClass(Class<?> inClazz) {
        if (inClazz.isPrimitive()) {
            return PRIMITIVE_TO_WRAPPER_CLASSES_MAP.get(inClazz);
        } else {
            return inClazz;
        }
    }

    private static final class JavaELPropertyDescriptor implements ELPropertyDescriptor {

        private final PropertyDescriptor descriptor;
        private final String descriptorName;
        private final ELType propertyType;

        /**
         * <p class="changed_added_4_0">
         * </p>
         * 
         * @param descriptor
         * @param propertyType
         * @param descriptorName
         */
        public JavaELPropertyDescriptor(PropertyDescriptor descriptor, ELType propertyType) {
            this.descriptor = descriptor;
            this.propertyType = propertyType;
            this.descriptorName = descriptor.getName();
        }

        @Override
        public boolean isWritable() {
            return null != descriptor.getWriteMethod();
        }

        @Override
        public boolean isReadable() {
            return null != descriptor.getReadMethod();
        }

        @Override
        public String getWriteMethosName() {
            return descriptor.getWriteMethod().getName();
        }

        @Override
        public ELType getType() {
            return propertyType;
        }

        @Override
        public String getReadMethodName() {
            return descriptor.getReadMethod().getName();
        }

        @Override
        public String getName() {
            return descriptorName;
        }
    }

    private static final class ClassDataHolder implements ClassVisitor {

        private Map<String, ELPropertyDescriptor> resolvedProperties;

        private List<Method> resolvedMethods;

        private final TypesFactory typesFactory;

        public ClassDataHolder(TypesFactory typesFactory) {
            super();
            this.typesFactory = typesFactory;

            this.resolvedProperties = Maps.newHashMap();
            this.resolvedMethods = Lists.newArrayList();
        }

        public Map<String, ELPropertyDescriptor> getResolvedProperties() {
            return resolvedProperties;
        }

        public List<Method> getResolvedMethods() {
            return resolvedMethods;
        }

        @Override
        public void visit(Class<?> clazz) throws ParsingException {
            PropertyDescriptor[] pds;
            Method[] declaredMethods;

            try {
                pds = getPropertyDescriptors(clazz);
                declaredMethods = clazz.getDeclaredMethods();
            } catch (LinkageError e) {
                throw new ParsingException(e.getMessage(), e);
            }

            for (PropertyDescriptor descriptor : pds) {
                String descriptorName = descriptor.getName();
                if (resolvedProperties.get(descriptorName) == null) {
                    
                    Type reflectionType;
                    if(null != descriptor.getReadMethod()){
                        reflectionType = descriptor.getReadMethod().getGenericReturnType();
                    } else if (null != descriptor.getWriteMethod()) {
                        reflectionType = descriptor.getWriteMethod().getGenericParameterTypes()[0];
                    } else {
                        reflectionType = descriptor.getPropertyType();
                    }
                    ELType propertyType = typesFactory.getType(reflectionType);
                    ELPropertyDescriptor elDescriptor = new JavaELPropertyDescriptor(descriptor, propertyType);
                    resolvedProperties.put(descriptorName, elDescriptor);
                }
            }

            resolvedMethods.addAll(Arrays.asList(declaredMethods));
        }
    }

    interface ClassVisitor {
        void visit(Class<?> clazz) throws ParsingException;
    }

    static class ClassWalkingLogic {

        private Queue<Class<?>> classesList = new LinkedList<Class<?>>();

        private Set<Class<?>> visitedClasses = new HashSet<Class<?>>();

        public ClassWalkingLogic(Class<?> clazz) {
            super();
            this.classesList.add(clazz);
        }

        public void walk(ClassVisitor visitor) throws ParsingException {
            // BFS algorithm
            while (!classesList.isEmpty()) {
                Class<?> clazz = classesList.remove();

                if (visitedClasses.add(clazz)) {
                    visitor.visit(clazz);

                    Class<?> superclass = clazz.getSuperclass();
                    if (superclass != null) {
                        if (!visitedClasses.contains(superclass)) {
                            classesList.add(superclass);
                        }
                    }

                    Class<?>[] interfaces = clazz.getInterfaces();
                    if (interfaces != null) {
                        for (Class<?> iface : interfaces) {
                            if (!visitedClasses.contains(iface)) {
                                classesList.add(iface);
                            }
                        }
                    }
                }
            }

            // While interfaces do not have Object.class in their hierarchy directly,
            // implementations of interface are always inherited from Object.
            // As methods in this class are primarily designed to work with implementations (beans),
            // we are adding Object.class explicitly if it hasn't been visited yet.
            if (visitedClasses.add(Object.class)) {
                visitor.visit(Object.class);
            }

            visitedClasses.clear();
        }
    }

    private ClassDataHolder resolveClassPropertiesAndMethods(Class<?> clazz) throws ParsingException {
        ClassDataHolder classDataHolder = classDataCache.get(clazz);
        if (classDataHolder == null) {
            classDataHolder = new ClassDataHolder(this);
            new ClassWalkingLogic(clazz).walk(classDataHolder);
            classDataCache.put(clazz, classDataHolder);
        }

        return classDataHolder;
    }

    /**
     * This method return PropertyDescriptor by specified propertyName and clazz.
     * 
     * @param elType
     *            - class to search
     * @param propertyName
     *            - propertyName to search
     * @return property descriptor if found.
     * @throws ParsingException
     *             if error occured.
     */
    public ELPropertyDescriptor getPropertyDescriptor(ELType elType, String propertyName) throws ParsingException {
        ELPropertyDescriptor propertyDescriptor;

        if (elType == null) {
            propertyDescriptor = new DummyPropertyDescriptor(propertyName);
        } else {
            ClassDataHolder classDataHolder = resolveClassPropertiesAndMethods(getClassFromType(elType));
            Map<String, ELPropertyDescriptor> resolvedProperties = classDataHolder.getResolvedProperties();
            if (resolvedProperties.containsKey(propertyName)) {
                propertyDescriptor = resolvedProperties.get(propertyName);
            } else {
                propertyDescriptor = new DummyPropertyDescriptor(propertyName);
            }
        }
        return propertyDescriptor;
    }

    /**
     * <p>
     * Retrieve the property descriptors for the specified class, introspecting and caching them the first time a
     * particular bean class is encountered.
     * </p>
     * 
     * <p>
     * <strong>FIXME</strong> - Does not work with DynaBeans.
     * </p>
     * 
     * @param beanClass
     *            Bean class for which property descriptors are requested
     * @return the property descriptors
     * @throws ParsingException
     *             if error occured.
     * 
     * @exception IllegalArgumentException
     *                if <code>beanClass</code> is null
     */
    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) throws ParsingException {
        if (beanClass == null) {
            throw new IllegalArgumentException("No bean class specified");
        }

        // Look up any cached descriptors for this bean class
        PropertyDescriptor[] descriptors = null;

        // Introspect the bean and cache the generated descriptors
        BeanInfo beanInfo = null;

        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
            descriptors = beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            return EMPTY_PROPERTY_DESCRIPTORS;
        }

        if (descriptors == null) {
            descriptors = EMPTY_PROPERTY_DESCRIPTORS;
        }

        return descriptors;
    }

    private static boolean isMethodVisible(Method method) {
        return !Modifier.isPrivate(method.getModifiers());
    }

    private static Class<?> getClassFromType(ELType elType) {
        if (elType == null) {
            return Object.class;
        } else if (elType instanceof PlainClassType) {
            Class<?> clazz = ((PlainClassType) elType).getPlainJavaClass();
            return clazz;
        } else if (elType instanceof ComplexType) {
            return getClassFromType(elType.getRawType());
        }
        return Object.class;
    }

    /**
     * <p>
     * Find an accessible method that matches the given name and has compatible parameters. Compatible parameters mean
     * that every method parameter is assignable from the given parameters. In other words, it finds a method with the
     * given name that will take the parameters given.
     * <p>
     * 
     * <p>
     * This method is slightly undeterminstic since it loops through methods names and return the first matching method.
     * </p>
     * 
     * <p>
     * This method is used by {@link #invokeMethod(Object object,String methodName,Object [] args,Class[] parameterTypes)}.
     * 
     * <p>
     * This method can match primitive parameter by passing in wrapper classes. For example, a <code>Boolean</code> will
     * match a primitive <code>boolean</code> parameter.
     * 
     * @param elType
     *            find method in this class
     * @param methodName
     *            find method with this name
     * @param parameterTypes
     *            find method with compatible parameters
     * @return The accessible method
     * @throws ParsingException
     *             if error occured.
     */
    public ELType getMatchingVisibleMethodReturnType(ELType elType, final String methodName, ELType... parameterTypes)
        throws ParsingException {

        ClassDataHolder classDataHolder = resolveClassPropertiesAndMethods(getClassFromType(elType));
        List<Method> resolvedMethods = classDataHolder.getResolvedMethods();

        // search through all methods
        int paramSize = parameterTypes.length;
        Method bestMatch = null;

        for (Method resolvedMethod : resolvedMethods) {
            if (isMethodVisible(resolvedMethod) && resolvedMethod.getName().equals(methodName)) {
                // compare parameters
                ELType[] methodsParams = getTypesArray(resolvedMethod.getParameterTypes());
                int methodParamSize = methodsParams.length;
                if (methodParamSize == paramSize) {
                    boolean match = true;
                    for (int n = 0; n < methodParamSize; n++) {
                        if (!methodsParams[n].isAssignableFrom(parameterTypes[n])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        bestMatch = resolvedMethod;
                    }
                }
            }
        }

        if (bestMatch != null) {
            return getType(bestMatch.getGenericReturnType());
        } else {
            return TypesFactory.OBJECT_TYPE;
        }
    }

}
