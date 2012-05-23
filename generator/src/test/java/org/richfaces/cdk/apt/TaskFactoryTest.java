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
package org.richfaces.cdk.apt;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler.CompilationTask;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.Stub;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public class TaskFactoryTest extends AnnotationProcessorTestBase {
    private static final String CLASS_JAVA = "org/richfaces/cdk/apt/TestClass.java";
    private static final String INTERFACE_JAVA = "org/richfaces/cdk/apt/TestInterface.java";
    private static final String SUB_CLASS_JAVA = "org/richfaces/cdk/apt/TestSubClass.java";
    @Mock
    CdkProcessor processor;
    @Inject
    private TaskFactoryImpl factory;
    @Stub
    @Output(Outputs.JAVA_CLASSES)
    private FileManager output;

    /**
     * Test method for {@link org.richfaces.cdk.apt.TaskFactoryImpl#get()}.
     *
     * @throws Exception
     * @throws AptException
     */
    @Test
    public void testGetTask() throws Exception {
        expect(output.getFolders()).andReturn(null);
        replay(processor, output);

        CompilationTask task = factory.get();

        assertNotNull(task);
        verify(processor, output);
    }

    @Test
    public void testTask() throws Exception {
        expect(output.getFolders()).andReturn(null);
        processor.init((ProcessingEnvironment) anyObject());
        expectLastCall();
        expect(processor.getSupportedSourceVersion()).andReturn(SourceVersion.RELEASE_6);
        expect(processor.getSupportedAnnotationTypes()).andReturn(Collections.singleton("*"));
        expect(processor.getSupportedOptions()).andReturn(Collections.<String>emptySet());
        // processor.process(null,null);
        Capture<Set<? extends TypeElement>> capturedTypes = new Capture<Set<? extends TypeElement>>(CaptureType.FIRST);

        expect(processor.process(capture(capturedTypes), EasyMock.<RoundEnvironment>anyObject())).andReturn(true).times(2);
        replay(processor, output);
        CompilationTask task = factory.get();
        assertTrue(task.call());
        Set<? extends TypeElement> elements = capturedTypes.getValue();

        assertFalse(elements.isEmpty());
        assertEquals("TestAnnotation2", elements.iterator().next().getSimpleName().toString());
        verify(processor, output);
    }

    @Override
    protected Iterable<String> sources() {
        return ImmutableList.of(CLASS_JAVA, SUB_CLASS_JAVA);
    }
}
