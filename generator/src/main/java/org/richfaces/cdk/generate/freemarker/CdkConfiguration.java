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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.richfaces.cdk.Logger;

import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class CdkConfiguration extends Configuration implements FreeMarkerRenderer {
    private static final String TEMPLATES = "/META-INF/templates";
    private String templatesFolder = TEMPLATES;
    private Logger log;

    @Inject
    public CdkConfiguration(ObjectWrapper wrapper, Logger log, FreeMakerUtils utils) {
        super();
        this.log = log;

        // load templates from plugin classloader.
        setClassForTemplateLoading(this.getClass(), getTemplatesFolder());
        setTemplateUpdateDelay(10000);// Forever...
        setSharedVariable("utils", utils);
        setObjectWrapper(wrapper);
        // Add context variables
        // this.setSharedVariable("context", new BeanModel(context, new BeansWrapper()));
    }

    @Inject(optional = true)
    public void setSharedVariables(@ContextVariables Map<String, Object> variables) {
        // template method for subclasses.
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            try {
                setSharedVariable(entry.getKey(), entry.getValue());
            } catch (TemplateModelException e) {
                log.error("Error to set shared variable " + entry.getKey(), e);
            }
        }
    }

    @Inject(optional = true)
    public synchronized void setImports(@DefaultImports Map<String, String> map) {
        super.setAutoImports(map);
    }

    @Override
    public void writeTemplate(String templateName, Object object, Writer writer) throws IOException, TemplateException {
        getTemplate(templateName).process(object, writer);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.FreeMarkerRenderer#renderSnippet(java.lang.String, java.lang.Object)
     */
    @Override
    public String renderTemplate(String templateName, Object object) {
        StringWriter writer = new StringWriter();
        try {
            writeTemplate(templateName, object, writer);
            return writer.toString();
        } catch (IOException e) {
            log.error("Error rendering template", e);
            return e.getMessage();
        } catch (TemplateException e) {
            log.error("Error rendering template", e);
            return e.getMessage();
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param templatesFolder the templatesFolder to set
     */
    @Inject(optional = true)
    public void setTemplatesFolder(@TemplatesFolder String templatesFolder) {
        setClassForTemplateLoading(this.getClass(), templatesFolder);
        this.templatesFolder = templatesFolder;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the templates
     */
    public String getTemplatesFolder() {
        return templatesFolder;
    }
}
