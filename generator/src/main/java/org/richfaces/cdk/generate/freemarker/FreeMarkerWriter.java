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
package org.richfaces.cdk.generate.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.Trackable;
import org.richfaces.cdk.model.ViewElement;

import freemarker.template.TemplateException;

/**
 * <p class="changed_added_4_0">
 * Base class for all output file buildes that use FreeMarker as template engine.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public abstract class FreeMarkerWriter<C> implements CdkWriter {
    private final FreeMarkerRenderer configuration;
    private final FileManager output;

    public FreeMarkerWriter(FreeMarkerRenderer configuration, FileManager output) {
        this.configuration = configuration;
        this.output = output;
    }

    public void generate(ComponentLibrary library, C c) throws CdkException {
        try {
            Writer out = getOutput(library, c);
            if (null != out) {
                configuration.writeTemplate(getTemplateName(), c, out);
                out.close();
            }
        } catch (IOException e) {
            throw new CdkException(e);
        } catch (TemplateException e) {
            throw new CdkException(e);
        }
    }

    protected Writer getOutput(ComponentLibrary library, C c) throws CdkException {
        long lastModified = Long.MIN_VALUE;

        if (c instanceof Trackable) {
            Trackable trackuble = (Trackable) c;
            lastModified = trackuble.lastModified();
        }

        try {
            return output.createOutput(getOutputFileName(c), lastModified);
        } catch (IOException e) {
            throw new CdkException(e);
        }
    }

    protected String getOutputFileName(C c) throws CdkException {
        if (c instanceof ViewElement) {
            ModelElementBase modelElement = (ModelElementBase) c;
            return modelElement.getTargetClass().getName().replace('.', File.separatorChar) + ".java";
        } else {
            throw new CdkException("Unknown model object " + c);
        }
    }

    protected String getJavaFileName(ClassName targetClass) {
        return targetClass.getName().replace('.', File.separatorChar) + ".java";
    }

    protected abstract String getTemplateName();
}
