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

import java.util.Collection;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.generate.freemarker.FreeMarkerWriter;
import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Jan 21, 2010
 */
public class BehaviorClassGenerator extends FreeMarkerWriter<BehaviorModel> implements CdkWriter {
    @Inject
    public BehaviorClassGenerator(@LibraryModel FreeMarkerRenderer configuration,
            @Output(Outputs.JAVA_CLASSES) FileManager output) {
        super(configuration, output);
    }

    @Override
    public void render(ComponentLibrary library) throws CdkException {
        Collection<BehaviorModel> models = library.getBehaviors();
        for (BehaviorModel model : models) {
            if (model.getGenerate()) {
                this.generate(library, model);
            }
        }
    }

    @Override
    protected String getTemplateName() {
        return "behavior.ftl";
    }
}
