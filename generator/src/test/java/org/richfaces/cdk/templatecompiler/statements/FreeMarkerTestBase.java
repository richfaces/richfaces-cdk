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

package org.richfaces.cdk.templatecompiler.statements;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.Map;
import java.util.NoSuchElementException;

import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.MockController;
import org.richfaces.cdk.Stub;
import org.richfaces.cdk.generate.freemarker.CdkConfiguration;
import org.richfaces.cdk.generate.freemarker.ContextVariables;
import org.richfaces.cdk.generate.freemarker.DefaultImports;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.TemplatesFolder;
import org.richfaces.cdk.templatecompiler.ELParser;
import org.richfaces.cdk.templatecompiler.JavaClassModelWrapper;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.TemplateModule;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;

import freemarker.template.ObjectWrapper;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class FreeMarkerTestBase extends CdkTestBase {

    protected static final String HTTP_EXAMPLE_COM = "http://example.com/";
    @Inject
    @TemplateModel
    protected FreeMarkerRenderer renderer;
    @Stub
    protected Logger log;
    @Mock
    protected ELParser parser;
    @Mock
    protected TypedTemplateStatement parsedExpression;
    @Inject
    protected MockController controller;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(ObjectWrapper.class).to(JavaClassModelWrapper.class);
        binder.bind(FreeMarkerRenderer.class).annotatedWith(TemplateModel.class).to(CdkConfiguration.class);
        binder.bind(String.class).annotatedWith(TemplatesFolder.class).toInstance("/META-INF/templates/java");
        binder.bind(new TypeLiteral<Map<String, String>>() {
        }).annotatedWith(DefaultImports.class).toInstance(ImmutableMap.of("util", "util.ftl"));
        binder.bind(new TypeLiteral<Map<String, Object>>() {
        }).annotatedWith(ContextVariables.class).toInstance(TemplateModule.buildVariables());
    }

    protected void verifyImports(TemplateStatement statement, String... expected) {
        Iterable<JavaImport> requiredImports = statement.getRequiredImports();
        for (final String expectedImport : expected) {
            try {
                Iterables.find(requiredImports, new Predicate<JavaImport>() {

                    @Override
                    public boolean apply(JavaImport input) {
                        return input.getName().equals(expectedImport);
                    }
                });
            } catch (NoSuchElementException e) {
                assertTrue("Import for " + expectedImport + " not found in statement", false);
            }
        }
    }

    protected void verifyHelpers(TemplateStatement statement, HelperMethod... expected) {
        Iterable<HelperMethod> requiredHelpers = statement.getRequiredMethods();
        for (final HelperMethod expectedHelper : expected) {
            try {
                Iterables.find(requiredHelpers, new Predicate<HelperMethod>() {

                    @Override
                    public boolean apply(HelperMethod input) {
                        return input.equals(expectedHelper);
                    }
                });
            } catch (NoSuchElementException e) {
                assertTrue("Helper method " + expectedHelper + " not found in statement", false);
            }
        }
    }

    protected void verifyCode(String code, String... expected) {
        for (String string : expected) {
            if(string.startsWith("!")){
                assertThat(code, not(containsString(string.substring(1))));                
            } else {
                assertThat(code, containsString(string));
            }
        }
    }
}
