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
package org.richfaces.cdk.generate.java.taghandler;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.java.LibraryModel;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.TagModel;
import org.richfaces.cdk.model.Trackable;

import com.google.inject.Inject;

import freemarker.template.TemplateException;

/**
 * @author akolonitsky
 * @since Feb 22, 2010
 */
public class TagHandlerClassGenerator {
    private static final String TAGHANDLER_TEMPLATE = "taghandler.ftl";
    private final FileManager output;
    private final FreeMarkerRenderer configuration;

    @Inject
    public TagHandlerClassGenerator(@LibraryModel FreeMarkerRenderer configuration,
            @Output(Outputs.JAVA_CLASSES) FileManager output) {
        this.configuration = configuration;
        this.output = output;
    }

    public boolean process(ModelElementBase model, TagModel tag) throws CdkException {
        try {
            Writer writer = getOutput(tag);
            configuration.writeTemplate(TAGHANDLER_TEMPLATE, new TagWithModel<ModelElementBase>(tag, model), writer);
            writer.close();
        } catch (IOException e) {
            throw new CdkException(e);
        } catch (TemplateException e) {
            throw new CdkException(e);
        }

        return false;
    }

    private Writer getOutput(TagModel tag) throws CdkException {
        long lastModified = Long.MIN_VALUE;

        if (tag instanceof Trackable) {
            lastModified = ((Trackable) tag).lastModified();
        }

        try {
            return output.createOutput(getOutputFileName(tag), lastModified);
        } catch (IOException e) {
            throw new CdkException(e);
        }
    }

    private String getOutputFileName(TagModel tag) {
        return tag.getTargetClass().getName().replace('.', File.separatorChar) + ".java";
    }
}
