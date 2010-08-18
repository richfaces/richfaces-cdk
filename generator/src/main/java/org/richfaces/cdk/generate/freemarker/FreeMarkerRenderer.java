package org.richfaces.cdk.generate.freemarker;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.TemplateException;

public interface FreeMarkerRenderer {

    public void writeTemplate(String templateName, Object object, Writer writer)
        throws IOException, TemplateException;

    public String renderTemplate(String templateName, Object object);

}