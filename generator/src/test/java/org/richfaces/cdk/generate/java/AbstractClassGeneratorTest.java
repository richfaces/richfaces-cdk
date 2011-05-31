/*
Fr * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.generate.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.junit.Assert;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.generate.freemarker.CdkConfiguration;
import org.richfaces.cdk.generate.freemarker.DefaultImports;
import org.richfaces.cdk.generate.freemarker.FreeMakerUtils;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.LibraryModelWrapper;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.PropertyBase;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;

import freemarker.template.ObjectWrapper;

/**
 * @author akolonitsky
 * @since Jan 20, 2010
 */
public abstract class AbstractClassGeneratorTest extends CdkTestBase {
    @Inject
    @LibraryModel
    protected FreeMarkerRenderer configuration;
    @Inject
    protected ComponentLibrary library;
    @Inject
    protected Logger log;
    @Mock
    @Output(Outputs.JAVA_CLASSES)
    protected FileManager output;
    @Inject
    protected ObjectWrapper wrapper;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(FreeMarkerRenderer.class).annotatedWith(LibraryModel.class).to(CdkConfiguration.class);
        binder.bind(ObjectWrapper.class).to(LibraryModelWrapper.class);
        binder.bind(FreeMakerUtils.class);
        binder.bind(new TypeLiteral<Map<String, String>>() {
        }).annotatedWith(DefaultImports.class).toInstance(ImmutableMap.of("util", "util.ftl"));
    }

    protected static PropertyBase addAttribute(ModelElementBase model, String attributeName, Class<?> type, boolean generate) {
        PropertyBase attribute = model.getOrCreateAttribute(attributeName);
        attribute.setType(new ClassName(type));
        attribute.setGenerate(generate);
        return attribute;
    }

    protected static void compareTextFiles(Reader reference, Reader output) throws IOException {
        LineNumberReader ref = new LineNumberReader(reference);
        LineNumberReader out = new LineNumberReader(output);
        String refLine = "";
        String outLine = "";
        while (refLine != null || outLine != null) {
            if (refLine == null) {
                Assert.fail("Output text is longer than reference text");
            }
            if (outLine == null) {
                Assert.fail("Output text is shorter than reference text");
            }

            refLine = ref.readLine();
            outLine = out.readLine();

            if (refLine != null && outLine != null && !refLine.trim().equals(outLine.trim())) {
                Assert.fail("Difference found on line " + ref.getLineNumber() + ".\nReference text is: " + refLine
                        + "\nOutput text is: " + outLine);
            }
        }
    }

    protected static EventName getEvent(String name, boolean defaultEvent) {
        EventName event = new EventName();
        event.setName(name);
        event.setDefaultEvent(defaultEvent);
        return event;
    }

    protected void compare(StringWriter writer, String fileName) throws IOException {
        InputStream expectedFacesConfigFile = this.getClass().getResourceAsStream(fileName);
        Assert.assertNotNull("File (" + fileName + ") with expected result wasn't found.", expectedFacesConfigFile);

        compareTextFiles(new StringReader(writer.toString()), new InputStreamReader(expectedFacesConfigFile));
    }
}
