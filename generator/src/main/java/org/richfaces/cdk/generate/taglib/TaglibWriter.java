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
package org.richfaces.cdk.generate.taglib;

import java.io.IOException;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.model.ComponentLibrary;

import com.google.inject.Inject;

/**
 * @author akolonitsky
 * @since Feb 9, 2010
 */
public class TaglibWriter implements CdkWriter {
    @Inject
    @Output(Outputs.RESOURCES)
    private FileManager output;

    @Override
    public void render(ComponentLibrary library) throws CdkException {
        TaglibGeneratorVisitor visitor = new TaglibGeneratorVisitor();
        library.accept(visitor, library);
        if (!visitor.isEmpty()) {
            Document document = visitor.getDocument();
            try {
                OutputFormat format1 = OutputFormat.createPrettyPrint();
                format1.setIndentSize(4);

                XMLWriter writer = new XMLWriter(getOutput(library), format1);
                writer.write(document);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace(); // TODO
            }
        }
    }

    protected Writer getOutput(ComponentLibrary library) throws CdkException {
        try {
            return output
                    .createOutput("META-INF/" + library.getTaglib().getShortName() + ".taglib.xml", library.lastModified());
        } catch (IOException e) {
            throw new CdkException(e);
        }
    }

    public void setOutput(FileManager output) {
        this.output = output;
    }
}
