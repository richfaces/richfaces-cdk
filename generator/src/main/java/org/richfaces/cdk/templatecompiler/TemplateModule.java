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
package org.richfaces.cdk.templatecompiler;

import java.util.Map;

import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.ModelBuilder;
import org.richfaces.cdk.attributes.Schema;
import org.richfaces.cdk.generate.freemarker.CdkConfiguration;
import org.richfaces.cdk.generate.freemarker.ContextVariables;
import org.richfaces.cdk.generate.freemarker.DefaultImports;
import org.richfaces.cdk.generate.freemarker.FreeMakerUtils;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.TemplatesFolder;
import org.richfaces.cdk.templatecompiler.el.ELParserImpl;
import org.richfaces.cdk.templatecompiler.el.types.TypeParserFactory;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactoryImpl;
import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.templatecompiler.statements.HelperMethod;
import org.richfaces.cdk.templatecompiler.statements.HelperMethodFactory;
import org.richfaces.cdk.templatecompiler.statements.HelperMethodFactoryImpl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.PrivateBinder;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import freemarker.template.ObjectWrapper;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class TemplateModule extends AbstractModule {
    /*
     * (non-Javadoc)
     *
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        Multibinder<ModelBuilder> modelBinder = Multibinder.newSetBinder(binder(), ModelBuilder.class);
        modelBinder.addBinding().to(RendererTemplateParser.class);
        Multibinder.newSetBinder(binder(), CdkWriter.class).addBinding().to(RendererClassGenerator.class);
        bind(new TypeLiteral<TemplateVisitorFactory<RendererClassVisitor>>() {
        }).to(VisitorFactoryImpl.class).in(Singleton.class);
        bind(TypesFactory.class).to(TypesFactoryImpl.class);
        bind(TypeParserFactory.class);
        bind(ELParser.class).to(ELParserImpl.class);
        bind(HelperMethodFactory.class).to(HelperMethodFactoryImpl.class).in(Singleton.class);
        bind(Schema.class).annotatedWith(Names.named(Template.XHTML_EL_NAMESPACE)).toProvider(XhtmlElSchemaProvider.class)
                .in(Singleton.class);
        // Private bindings.
        PrivateBinder privateBinder = binder().newPrivateBinder();
        privateBinder.bind(FreeMarkerRenderer.class).annotatedWith(TemplateModel.class).to(CdkConfiguration.class);
        privateBinder.expose(FreeMarkerRenderer.class).annotatedWith(TemplateModel.class);
        privateBinder.bind(FreeMakerUtils.class);
        privateBinder.bind(ObjectWrapper.class).to(JavaClassModelWrapper.class);
        privateBinder.bind(new TypeLiteral<Map<String, Object>>() {
        }).annotatedWith(ContextVariables.class).toInstance(buildVariables());
        privateBinder.bind(String.class).annotatedWith(TemplatesFolder.class).toInstance("/META-INF/templates/java");
        privateBinder.bind(new TypeLiteral<Map<String, String>>() {
        }).annotatedWith(DefaultImports.class).toInstance(ImmutableMap.of("util", "util.ftl"));
    }

    public static Map<String, Object> buildVariables() {
        Map<String, Object> variables = Maps.newHashMap(RendererClassVisitor.ENCODE_METHOD_VARIABLES);
        for (Map.Entry<HelperMethod, String> entry : HelperMethod.METHOD_NAMES.entrySet()) {
            variables.put(entry.getKey().toString(), entry.getValue());
        }
        return variables;
    }
}
