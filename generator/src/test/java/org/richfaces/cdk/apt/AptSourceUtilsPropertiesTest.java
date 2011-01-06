/*
 * $Id$
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

package org.richfaces.cdk.apt;

import static org.junit.Assert.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.AbstractElementVisitor6;
import javax.lang.model.util.ElementKindVisitor6;
import javax.lang.model.util.ElementScanner6;

import org.junit.Test;
import org.richfaces.cdk.apt.SourceUtils.BeanProperty;

import com.google.common.collect.ImmutableList;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class AptSourceUtilsPropertiesTest extends SourceUtilsTestBase {

    private static final String TEST_SUB_CLASS = "PropertyTestClass";
    private static final String TEST_CLASS = "TestClass";
    private static final String TEST_INTERFACE = "TestInterface";
    private static final String PROPERTY_TEST_INTERFACE = "TestInterface";
    private static final String PACKAGE_PATH = "org/richfaces/cdk/apt/";
    private static final String CLASS_JAVA = PACKAGE_PATH + TEST_CLASS + ".java";
    private static final String SUB_CLASS_JAVA = PACKAGE_PATH + TEST_SUB_CLASS + ".java";
    private static final String INTERFACE_JAVA = PACKAGE_PATH + TEST_INTERFACE + ".java";
    private static final String PROPERTY_INTERFACE_JAVA = PACKAGE_PATH + PROPERTY_TEST_INTERFACE + ".java";

    @Test
    public void testGetConcreteProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "concreteValue");
                assertTrue(beanProperty.isExists());
            }
        });
    }

    @Test
    public void testGetAbstractProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "value");
                assertFalse(beanProperty.isExists());
            }
        });
    }

    @Test
    public void testGetNotExistedProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "notExistedValue");
                assertFalse(beanProperty.isExists());
            }
        });
    }

    @Test
    public void testGetInheritedProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "inheritedValue");
                assertTrue(beanProperty.isExists());
            }
        });
    }

    @Test
    public void testGetReadOnlyProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "readOnly");
                assertTrue(beanProperty.isExists());
            }
        });
    }

    @Test
    public void testGetWriteOnlyProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "writeOnly");
                assertTrue(beanProperty.isExists());
            }
        });
    }

    @Test
    public void testGetWrongProperty() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                BeanProperty beanProperty = utils.getBeanProperty(subClassType, "wrongValue");
                assertTrue(beanProperty.isExists());
            }
        });
    }
    
    @Test
    public void testGetBeanProperties() throws Exception {
        execute(new SourceUtilsCallback() {

            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                AptSourceUtils aptUtils = (AptSourceUtils) utils;
                TypeElement subClassType = (TypeElement) findElement(roundEnv, TEST_SUB_CLASS);
                assertEquals(6, aptUtils.getBeanProperties(subClassType).size());
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.apt.AnnotationProcessorTestBase#sources()
     */
    @Override
    protected Iterable<String> sources() {
        return ImmutableList.of(CLASS_JAVA, SUB_CLASS_JAVA, INTERFACE_JAVA,PROPERTY_INTERFACE_JAVA);
    }

}
