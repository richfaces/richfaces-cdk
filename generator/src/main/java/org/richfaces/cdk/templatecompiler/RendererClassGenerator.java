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

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.ModelSet;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.templatecompiler.builder.model.JavaClass;
import org.richfaces.cdk.templatecompiler.model.CdkFragmentElement;
import org.richfaces.cdk.templatecompiler.model.CompositeFragmentImplementation;
import org.richfaces.cdk.templatecompiler.model.Template;

import com.google.inject.Inject;

import freemarker.template.TemplateException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 * @author Lukas Fryc
 */
public class RendererClassGenerator implements CdkWriter {
    private FileManager output;
    private Logger log;
    private TemplateVisitorFactory<RendererClassVisitor> visitorFactory;
    private FreeMarkerRenderer renderer;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param library
     * @param output
     * @param log
     * @param visitorFactory
     */
    @Inject
    public RendererClassGenerator(@Output(Outputs.JAVA_CLASSES) FileManager output, Logger log,
            TemplateVisitorFactory<RendererClassVisitor> visitorFactory, @TemplateModel FreeMarkerRenderer renderer) {
        this.output = output;
        this.log = log;
        this.visitorFactory = visitorFactory;
        this.renderer = renderer;
    }

    private ComponentModel findComponentByRenderer(RendererModel renderer, ComponentLibrary library) {
        return library.getComponentByRenderer(renderer.getFamily(), renderer.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.CdkWriter#render(org.richfaces.cdk.model.ComponentLibrary )
     */
    @Override
    public void render(ComponentLibrary library) throws CdkException {
        for (RenderKitModel renderKit : library.getRenderKits()) {
            for (RendererModel renderer : renderKit.getRenderers()) {
                if (renderer.hasChanged()) {
                    Template template = renderer.getTemplate();
                    if (null != template) {
                        Collection<PropertyBase> attributes = ModelSet.<PropertyBase>create();

                        ComponentModel component = findComponentByRenderer(renderer, library);
                        if (component != null) {
                            attributes.addAll(component.getAttributes());
                        }

                        attributes.addAll(renderer.getAttributes());
                        RendererClassVisitor visitor = visitorFactory.createVisitor(template.getInterface(), attributes);

                        template.getImplementation().beforeVisit(visitor);

                        if (template.getFragments() != null) {
                            for (CdkFragmentElement fragment : template.getFragments()) {
                                CompositeFragmentImplementation implementation = fragment.getFragmentImplementation();

                                fragment.beforeVisit(visitor);
                                if (implementation != null) {
                                    implementation.visit(visitor);
                                }
                                fragment.afterVisit(visitor);
                            }
                        }

                        template.getImplementation().visitChildren(visitor);
                        template.getImplementation().afterVisit(visitor);

                        JavaClass javaClass = visitor.getGeneratedClass();
                        String fullName = javaClass.getName();
                        Writer outFile = null;
                        try {
                            outFile = output.createOutput(fullName.replace('.', '/') + ".java", library.lastModified());

                            if (null != outFile) {
                                this.renderer.writeTemplate("class.ftl", javaClass, outFile);
                            }
                        } catch (IOException e) {
                            throw new CdkException(e);
                        } catch (TemplateException e) {
                            throw new CdkException(e);
                        } finally {
                            if (null != outFile) {
                                try {
                                    outFile.close();
                                } catch (IOException e) {
                                    log.warn("IOException occured when closing writer for renderer-class", e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
