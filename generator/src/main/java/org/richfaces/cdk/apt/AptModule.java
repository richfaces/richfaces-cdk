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

import org.richfaces.cdk.LibraryBuilder;
import org.richfaces.cdk.apt.processors.AttributesProcessor;
import org.richfaces.cdk.apt.processors.AttributesProcessorImpl;
import org.richfaces.cdk.apt.processors.BehaviorProcessor;
import org.richfaces.cdk.apt.processors.BehaviorRendererProcessor;
import org.richfaces.cdk.apt.processors.CdkAnnotationProcessor;
import org.richfaces.cdk.apt.processors.ComponentProcessor;
import org.richfaces.cdk.apt.processors.ConverterProcessor;
import org.richfaces.cdk.apt.processors.DescriptionProcessor;
import org.richfaces.cdk.apt.processors.DescriptionProcessorImpl;
import org.richfaces.cdk.apt.processors.EventProcessor;
import org.richfaces.cdk.apt.processors.FunctionProcessor;
import org.richfaces.cdk.apt.processors.RendererProcessor;
import org.richfaces.cdk.apt.processors.TagLibProcessor;
import org.richfaces.cdk.apt.processors.ValidatorProcessor;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class AptModule extends AbstractModule {

    /*
     * (non-Javadoc)
     *
     * @see com.google.inject.AbstractModule#configure()
     */

    @Override
    protected void configure() {
        Multibinder<CdkAnnotationProcessor> setBinder = Multibinder.newSetBinder(binder(), CdkAnnotationProcessor.class);
        setBinder.addBinding().to(ComponentProcessor.class);
        setBinder.addBinding().to(BehaviorProcessor.class);
        setBinder.addBinding().to(BehaviorRendererProcessor.class);
        setBinder.addBinding().to(RendererProcessor.class);
        setBinder.addBinding().to(ConverterProcessor.class);
        setBinder.addBinding().to(ValidatorProcessor.class);
        setBinder.addBinding().to(TagLibProcessor.class);
        setBinder.addBinding().to(FunctionProcessor.class);
        setBinder.addBinding().to(EventProcessor.class);

        bind(CdkProcessor.class).to(CdkProcessorImpl.class);
        bind(AttributesProcessor.class).to(AttributesProcessorImpl.class);
        bind(DescriptionProcessor.class).to(DescriptionProcessorImpl.class);
        bind(CompilationTaskFactory.class).to(TaskFactoryImpl.class);
        bind(LibraryBuilder.class).to(AptBuilder.class);
        bind(SourceUtilsProvider.class).in(Singleton.class);
        bind(SourceUtils.class).toProvider(SourceUtilsProvider.class);
        bind(LibraryCompiler.class).to(IncrementalLibraryCompiler.class);
        bind(JavaSourceProcessor.class).in(Singleton.class);
        bind(LibraryGenerator.class).to(DefaultLibraryGenerator.class);
        bind(JavaSourceTracker.class).to(JavaSourceTrackerImpl.class);
    }
}
