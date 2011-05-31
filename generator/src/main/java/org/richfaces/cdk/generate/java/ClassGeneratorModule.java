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
package org.richfaces.cdk.generate.java;

import java.util.Map;

import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.generate.freemarker.CdkConfiguration;
import org.richfaces.cdk.generate.freemarker.DefaultImports;
import org.richfaces.cdk.generate.freemarker.FreeMakerUtils;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.LibraryModelWrapper;
import org.richfaces.cdk.generate.java.taghandler.ListenerTagHandlerGenerator;
import org.richfaces.cdk.generate.java.taghandler.TagHandlerWriter;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.PrivateBinder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import freemarker.template.ObjectWrapper;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class ClassGeneratorModule extends AbstractModule {
    /*
     * (non-Javadoc)
     *
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        Multibinder<CdkWriter> setBinder = Multibinder.newSetBinder(binder(), CdkWriter.class);
        setBinder.addBinding().to(ComponentClassGenerator.class);
        setBinder.addBinding().to(ConverterClassGenerator.class);
        setBinder.addBinding().to(ValidatorClassGenerator.class);
        setBinder.addBinding().to(BehaviorClassGenerator.class);
        setBinder.addBinding().to(EventSourceInterfaceGenerator.class);
        setBinder.addBinding().to(TagHandlerWriter.class);
        setBinder.addBinding().to(ListenerTagHandlerGenerator.class);
        PrivateBinder privateBinder = binder().newPrivateBinder();
        privateBinder.bind(FreeMarkerRenderer.class).annotatedWith(LibraryModel.class).to(CdkConfiguration.class);
        privateBinder.expose(FreeMarkerRenderer.class).annotatedWith(LibraryModel.class);
        privateBinder.bind(ObjectWrapper.class).to(LibraryModelWrapper.class);
        privateBinder.bind(FreeMakerUtils.class);
        privateBinder.bind(new TypeLiteral<Map<String, String>>() {
        }).annotatedWith(DefaultImports.class).toInstance(ImmutableMap.of("util", "util.ftl"));
    }
}
