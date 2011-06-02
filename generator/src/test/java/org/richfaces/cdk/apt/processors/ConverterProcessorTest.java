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

import java.util.Collection;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.apt.AnnotationProcessorTestBase;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConverterModel;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Jan 14, 2010
 */
@RunWith(CdkTestRunner.class)
public class ConverterProcessorTest extends AnnotationProcessorTestBase {
    private static final String COMPONENT_CLASS_JAVA = "org/richfaces/cdk/test/component/MyConverter.java";
    @Inject
    private ComponentLibrary library;

    @Test
    @Ignore
    public void testProcess() throws Exception {
        Collection<ConverterModel> converters = library.getConverters();

        assertEquals(3, converters.size());

        ConverterModel converterModel = Iterables.get(converters, 0);
        ClassName forClass = converterModel.getConverterForClass();
        if (forClass != null && !Object.class.getName().equals(forClass.getName())) {
            assertEquals(Integer.class.getName(), forClass.getName());
        }
    }

    @Override
    protected Iterable<String> sources() {
        return Collections.singleton(COMPONENT_CLASS_JAVA);
    }
}
