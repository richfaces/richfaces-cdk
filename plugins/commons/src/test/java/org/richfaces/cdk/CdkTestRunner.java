/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.cdk;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.ScopeAnnotation;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class CdkTestRunner extends BlockJUnit4ClassRunner {

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param klass
     * @throws InitializationError
     * @throws InitializationError
     */
    public CdkTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /**
     * Gets all declared fields and all inherited fields.
     */
    protected Set<Field> getFields(Class<?> c) {
        Set<Field> fields = new HashSet<Field>(Arrays.asList(c.getDeclaredFields()));
        while ((c = c.getSuperclass()) != null) {
            for (Field f : c.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers())) {
                    fields.add(f);
                }
            }
        }
        return fields;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }

    @Override
    protected Object createTest() throws Exception {
        Class<?> c = getTestClass().getJavaClass();
        Set<Field> testFields = getFields(c);

        // make sure we have one (and only one) @Unit field
        // Field unitField = getUnitField(testFields);
        // if ( unitField.getAnnotation(Mock.class) != null ) {
        // throw new IncompatibleAnnotationException(Unit.class, Mock.class);
        // }
        //        
        final Map<Field, Binding> fieldValues = getMockValues(testFields);
        // if ( fieldValues.containsKey(unitField)) {
        // throw new IncompatibleAnnotationException(Unit.class, unitField.getType());
        // }

        Object test = createTest(c, fieldValues);

        // any field values created by AtUnit but not injected by the container are injected here.
        for (Field field : fieldValues.keySet()) {
            Binding binding = fieldValues.get(field);
            field.setAccessible(true);
            if (null != binding.getValue() && field.get(test) == null) {
                field.set(test, binding.getValue());
            }
        }

        return test;
    }

    private Object createTest(Class<?> testClass, Map<Field, Binding> fieldValues) throws Exception {
        FieldModule fields = new FieldModule(fieldValues);

        Injector injector;
        Object test = super.createTest();
        if (Module.class.isAssignableFrom(testClass)) {
            injector = Guice.createInjector(fields, (Module) testClass.newInstance());
        } else {
            injector = Guice.createInjector(fields);
        }
        injector.injectMembers(test);
        return test;
    }

    protected static final class FieldModule extends AbstractModule implements MockController {
        final Map<Field, Binding> fields;

        public FieldModule(Map<Field, Binding> fields) {
            this.fields = fields;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void configure() {
            // Bind mock controllet to this instance, to automatically replay/verify all mocks created by runner.
            bind(MockController.class).toInstance(this);
            // map field values by type
            for (Field field : fields.keySet()) {
                TypeLiteral literal = TypeLiteral.get(field.getGenericType());
                AnnotatedBindingBuilder builder = bind(literal);
                // Check field annotations.
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation annotation : fieldAnnotations) {
                    Class<? extends Annotation> annotationType = annotation.annotationType();
                    if (/* annotationType.isAnnotationPresent(Qualifier.class)|| */annotationType
                        .isAnnotationPresent(BindingAnnotation.class)) {
                            builder.annotatedWith(annotation);
                    }
                    if (annotationType.isAnnotationPresent(ScopeAnnotation.class)){
                        builder.in(annotationType);
                    }
                }
                Binding binding = fields.get(field);
                if (null != binding.getValue()) {
                    builder.toInstance(binding.getValue());
                } else if (null != binding.getImplementation()) {
                    builder.to(binding.getImplementation());
                } else if (null != binding.getProvider()) {
                    builder.toProvider(binding.getProvider());
                }
            }
        }

        @Override
        public void replay() {
            for (Binding field : fields.values()) {
                if(null != field.getValue()){
                    EasyMock.replay(field.getValue());
                }
            }
        }

        @Override
        public void verify() {
            for (Binding field : fields.values()) {
                if(null != field.getValue()){
                    EasyMock.verify(field.getValue());
                }
            }
        }
    }

    /**
     * <p class="changed_added_4_0">Binding definition storage</p>
     * @author asmirnov@exadel.com
     *
     */
    protected static final class Binding {
        private Object value;
        private Class<?> implementation;
        private Class<? extends Provider<?>> provider;
        protected Binding() {
        }
        /**
         * <p class="changed_added_4_0"></p>
         * @param value the value to set
         */
        void setValue(Object value) {
            this.value = value;
        }
        /**
         * <p class="changed_added_4_0"></p>
         * @return the value
         */
        Object getValue() {
            return value;
        }
        /**
         * <p class="changed_added_4_0"></p>
         * @param implementation the implementation to set
         */
        void setImplementation(Class<?> implementation) {
            this.implementation = implementation;
        }
        /**
         * <p class="changed_added_4_0"></p>
         * @return the implementation
         */
        Class<?> getImplementation() {
            return implementation;
        }
        /**
         * <p class="changed_added_4_0"></p>
         * @param provider the provider to set
         */
        void setProvider(Class<? extends Provider<?>> provider) {
            this.provider = provider;
        }
        /**
         * <p class="changed_added_4_0"></p>
         * @return the provider
         */
        Class<? extends Provider<?>> getProvider() {
            return provider;
        }
    }

    private Map<Field, Binding> getMockValues(Set<Field> testFields) {
        Map<Field, Binding> mocksAndStubs = new HashMap<Field, Binding>();
        // TODO - create annotation attribute that tells runner to use the scme Mock Controller to create related mocks.
        for (Field field : testFields) {
            if (field.getAnnotation(Mock.class) != null) {
                Binding bind = new Binding();
                bind.setValue(EasyMock.createStrictMock(field.getType()));
                mocksAndStubs.put(field, bind);
            } else if (field.getAnnotation(Stub.class) != null) {
                Binding bind = new Binding();
                bind.setValue(EasyMock.createNiceMock(field.getType()));
                mocksAndStubs.put(field, bind);
            } else if (null != field.getAnnotation(As.class)) {
                Binding bind = new Binding();
                bind.setImplementation(field.getAnnotation(As.class).value());
                mocksAndStubs.put(field, bind);
            } else if (null != field.getAnnotation(AsProvider.class)) {
                Binding bind = new Binding();
                bind.setProvider(field.getAnnotation(AsProvider.class).value());
                mocksAndStubs.put(field, bind);
            }
        }

        return mocksAndStubs;
    }

}
