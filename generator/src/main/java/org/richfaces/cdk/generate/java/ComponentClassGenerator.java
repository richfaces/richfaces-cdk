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

import java.util.Collection;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.FreeMarkerWriter;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class ComponentClassGenerator extends FreeMarkerWriter<ComponentModel> implements CdkWriter {
    @Inject
    public ComponentClassGenerator(@LibraryModel FreeMarkerRenderer configuration,
            @Output(Outputs.JAVA_CLASSES) FileManager output) {
        super(configuration, output);
    }

    @Override
    public void render(ComponentLibrary library) throws CdkException {
        Collection<ComponentModel> models = library.getComponents();
        for (ComponentModel model : models) {
            if (model.hasChanged() && model.getGenerate()) {
                this.generate(library, model);
            }
        }
    }

    @Override
    protected String getTemplateName() {
        return "component.ftl";
    }
}
