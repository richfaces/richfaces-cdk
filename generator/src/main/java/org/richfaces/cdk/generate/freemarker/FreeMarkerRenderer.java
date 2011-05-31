package org.richfaces.cdk.generate.freemarker;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.TemplateException;

public interface FreeMarkerRenderer {
    void writeTemplate(String templateName, Object object, Writer writer) throws IOException, TemplateException;

    String renderTemplate(String templateName, Object object);
}