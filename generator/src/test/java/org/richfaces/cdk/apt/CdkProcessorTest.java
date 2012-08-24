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
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.LibraryBuilder;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.ModelBuilder;
import org.richfaces.cdk.ModelValidator;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.Stub;
import org.richfaces.cdk.apt.processors.CdkAnnotationProcessor;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public class CdkProcessorTest extends AnnotationProcessorTestBase {
    private static final String ANNOTATION2 = TestAnnotation2.class.getName();
    private static final String CLASS_JAVA = "org/richfaces/cdk/apt/TestClass.java";
    private static final String COMPONENT_CLASS_JAVA = "org/richfaces/cdk/test/component/AbstractTestComponent.java";
    private static final String INTERFACE_JAVA = "org/richfaces/cdk/apt/TestInterface.java";
    private static final ImmutableSet<String> PROCESS_ANNOTATIONS = ImmutableSet.of(TestAnnotation.class.getName());
    private static final String SUB_CLASS_JAVA = "org/richfaces/cdk/apt/TestSubClass.java";
    @Inject
    ComponentLibrary library;
    @Mock
    private LibraryBuilder builder;
    @Inject
    private CdkAnnotationProcessor cdkProcessor;
    @Mock
    private TypeElement element;
    @Stub
    private Logger log;
    @Stub
    @Output(Outputs.JAVA_CLASSES)
    private FileManager output;
    @Inject
    private CdkProcessor processor;
    @Mock
    private RoundEnvironment roundEnv;
    @Mock
    private ModelValidator validator;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(CdkProcessor.class).to(CdkProcessorImpl.class).in(Singleton.class);
        CdkAnnotationProcessor cdkProcessor = createMock(CdkAnnotationProcessor.class);
        binder.bind(CdkAnnotationProcessor.class).toInstance(cdkProcessor);
        binder.bind(new TypeLiteral<Set<CdkAnnotationProcessor>>() {
        }).toInstance(ImmutableSet.of(cdkProcessor));
        binder.bind(new TypeLiteral<Set<ModelBuilder>>() {
        }).toInstance(Collections.<ModelBuilder>emptySet());
    }

    @Test
    public void testProcess() throws Exception {
        expect(roundEnv.processingOver()).andReturn(false);
        expect((Class<TestAnnotation2>) cdkProcessor.getProcessedAnnotation()).andStubReturn(TestAnnotation2.class);
        expect((Set<TypeElement>) roundEnv.getRootElements()).andReturn(Collections.singleton(element));
        expect(element.getKind()).andReturn(ElementKind.CLASS);
        TestAnnotation2 testAnnotation2 = createNiceMock(TestAnnotation2.class);
        expect(element.getAnnotation(TestAnnotation2.class)).andReturn(testAnnotation2);
        expect(element.getSimpleName()).andStubReturn(new TestName("foo"));
        cdkProcessor.process(element, library);
        expectLastCall();
        // validator.verify(library);
        // expectLastCall();
        replay(element, roundEnv, builder, validator, cdkProcessor);
        processor.process(Collections.singleton(element), roundEnv);
        verify(element, roundEnv, builder, validator, cdkProcessor);
    }

    @Test
    public void testProcess3() throws Exception {
        expect(roundEnv.processingOver()).andReturn(false);
        expect((Class<TestAnnotation>) cdkProcessor.getProcessedAnnotation()).andStubReturn(TestAnnotation.class);
        expect((Set<TypeElement>) roundEnv.getRootElements()).andReturn(Collections.singleton(element));
        expect(element.getKind()).andReturn(ElementKind.CLASS);
        expect(element.getAnnotation(TestAnnotation.class)).andReturn(null);
        // validator.verify(library);
        // expectLastCall();
        replay(element, roundEnv, builder, validator, cdkProcessor);
        processor.process(Collections.singleton(element), roundEnv);
        verify(element, roundEnv, builder, validator, cdkProcessor);
    }

    @Test
    public void testProcessOver() throws Exception {
        expect(roundEnv.processingOver()).andReturn(true);
        validator.verify(library);
        expectLastCall();
        expect(log.getErrorCount()).andReturn(0);
        log.debug((CharSequence) anyObject());
        expectLastCall().asStub();
//        builder.generate(library);
        expectLastCall();
        replay(log, element, roundEnv, builder, validator, cdkProcessor);

        processor.process(Collections.singleton(element), roundEnv);

        verify(log, element, roundEnv, builder, validator, cdkProcessor);
    }

    @Test
    public void testProcessOver2() throws Exception {
        expect(roundEnv.processingOver()).andReturn(true);
        validator.verify(library);
        expectLastCall();
        expect(log.getErrorCount()).andReturn(1);
        replay(log, element, roundEnv, builder, validator, cdkProcessor);
        processor.process(Collections.singleton(element), roundEnv);
        verify(log, element, roundEnv, builder, validator, cdkProcessor);
    }

    @Override
    protected Iterable<String> sources() {
        return ImmutableList.of(CLASS_JAVA, INTERFACE_JAVA, SUB_CLASS_JAVA);
    }
}
