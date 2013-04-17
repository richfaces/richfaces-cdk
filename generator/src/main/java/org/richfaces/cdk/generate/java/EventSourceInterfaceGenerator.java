/*
 * JBoss, Home of Professional Open Source
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

import java.io.File;
import java.util.Collection;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.FreeMarkerWriter;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventModel;

import com.google.inject.Inject;

/**
 * @author asmirnov
 * @since Jan 20, 2010
 */
public class EventSourceInterfaceGenerator extends FreeMarkerWriter<EventModel> implements CdkWriter {
    @Inject
    public EventSourceInterfaceGenerator(@LibraryModel FreeMarkerRenderer configuration,
            @Output(Outputs.JAVA_CLASSES) FileManager output) {
        super(configuration, output);
    }

    @Override
    public void render(ComponentLibrary library) throws CdkException {
        Collection<EventModel> models = library.getEvents();
        for (EventModel model : models) {
            if (model.isGenerateSource()) {
                this.generate(library, model);
            }
        }
    }

    @Override
    protected String getOutputFileName(EventModel c) throws CdkException {
        return c.getSourceInterface().getName().replace('.', File.separatorChar) + ".java";
    }

    @Override
    protected String getTemplateName() {
        return "sourceInterface.ftl";
    }
}
