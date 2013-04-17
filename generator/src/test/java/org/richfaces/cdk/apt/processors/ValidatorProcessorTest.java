/**
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
package org.richfaces.cdk.apt.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.annotations.JsfValidator;
import org.richfaces.cdk.apt.AnnotationProcessorTestBase;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.ValidatorModel;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Jan 14, 2010
 */
@RunWith(CdkTestRunner.class)
public class ValidatorProcessorTest extends AnnotationProcessorTestBase {
    private static class MyName implements Name {
        private final String toString;

        MyName(String toString) {
            this.toString = toString;
        }

        @Override
        public char charAt(int index) {
            return 0;
        }

        @Override
        public boolean contentEquals(CharSequence cs) {
            return false;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    private static final String VALIDATOR_CLASS_JAVA = "org/richfaces/cdk/test/component/MyValidator.java";
    @Inject
    private ComponentLibrary library;

    @Test
    @Ignore
    public void testProcess() throws Exception {
        Collection<ValidatorModel> validators = library.getValidators();

        assertEquals(2, validators.size());

        for (ValidatorModel model : validators) {
            FacesId id = model.getId();
            if (id != null) {
                assertEquals("my_validator", id.toString());
            }
        }
    }

    // TODO create Test for ProcessorBase
    @Test
    public void testSetNames() {
        ValidatorModel model;

        // @JsfValidator public class BaseClass { ...
        model = getValidatorModelForSetNames(true, null, "BaseClass", false);
        check("BaseClass", null, false, model);

        // @JsfValidator(validatorClass = "") public class BaseClass { ...
        model = getValidatorModelForSetNames(true, "", "BaseClass", false);
        check("BaseClass", null, false, model);

        // @JsfValidator(validatorClass = BaseClass) public class BaseClass { ...
        model = getValidatorModelForSetNames(true, "BaseClass", "BaseClass", false);
        check("BaseClass", null, false, model);

        // @JsfValidator(validatorClass = GeneratedClass) public class BaseClass { ...
        model = getValidatorModelForSetNames(true, "GeneratedClass", "BaseClass", false);
        check("GeneratedClass", "BaseClass", true, model);

        // /////////////////////////////// ABSTRACT ///////////////////////////////////////
        // @JsfValidator public abstract class BaseClass { ...
        // checkAbstractWithException(null);

        // @JsfValidator(validatorClass = "") public abstract class BaseClass { ...
        // checkAbstractWithException("");

        // @JsfValidator(validatorClass = BaseClass) public abstract class BaseClass { ...
        // checkAbstractWithException("BaseClass");

        // @JsfValidator(validatorClass = GeneratedClass) public abstract class BaseClass { ...
        model = getValidatorModelForSetNames(true, "GeneratedClass", "BaseClass", true);
        check("GeneratedClass", "BaseClass", true, model);
    }

    @Override
    protected Iterable<String> sources() {
        return Collections.singleton(VALIDATOR_CLASS_JAVA);
    }

    private void check(String validatorClass, String baseClass, boolean generate, ValidatorModel model) {
        if (baseClass != null) {
            // assertEquals(model.getBaseClass().toString(), baseClass);
        } else {
            assertNull(model.getBaseClass());
        }

        // assertEquals(generate, model.isGenerate());
    }

    private void checkAbstractWithException(String validatorClass) {
        try {
            getValidatorModelForSetNames(true, validatorClass, "BaseClass", true);
            Assert.fail("Abstract class can't be a validator.");
        } catch (IllegalStateException e) {
            // Do nothing.
        }
    }

    private ValidatorModel getValidatorModelForSetNames(boolean isAnnotationDefined, String validatorClass,
            final String baseClass, boolean isAbstractPresent) {
        Name name = new MyName(baseClass);

        TypeElement element = EasyMock.createMock(TypeElement.class);
        EasyMock.expect(element.getQualifiedName()).andReturn(name);

        Set<Modifier> set = new HashSet<Modifier>(1);
        if (isAbstractPresent) {
            set.add(Modifier.ABSTRACT);
        }
        EasyMock.expect(element.getModifiers()).andReturn(set);
        EasyMock.replay(element);

        ValidatorModel validatorModel = new ValidatorModel();
        JsfValidator validator = null;
        if (isAnnotationDefined) {
            validator = EasyMock.createMock(JsfValidator.class);
            EasyMock.expect(validator.generate()).andReturn(validatorClass);
            EasyMock.replay(validator);
        }

        EasyMock.verify();
        // ValidatorProcessor.setClassNames(element, validatorModel, validator);
        return validatorModel;
    }
}
