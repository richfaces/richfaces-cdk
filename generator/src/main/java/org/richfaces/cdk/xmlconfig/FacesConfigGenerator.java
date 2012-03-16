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
package org.richfaces.cdk.xmlconfig;

import java.io.IOException;
import java.io.Writer;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.CdkWriter;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.xmlconfig.model.FacesConfigAdapter;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class FacesConfigGenerator implements CdkWriter {
    public static final String FACES_SCHEMA_LOCATION = ComponentLibrary.FACES_CONFIG_NAMESPACE + " "
            + ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION;
    private static final String FACES_CONFIG_XML = "META-INF/faces-config.xml";
    @Inject
    private JAXB jaxbBinding;
    @Inject
    @Output(Outputs.RESOURCES)
    private FileManager outputFileManager;
    private FacesConfigAdapter libraryAdapter;
    @Inject
    private Logger log;

    public FacesConfigGenerator() {
        libraryAdapter = new FacesConfigAdapter();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.CdkWriter#render(org.richfaces.cdk.model.ComponentLibrary )
     */
    @Override
    public void render(ComponentLibrary library) throws CdkException {

        // do not render empty config.
        if (!library.isEmpty()) {
            Writer facesConfigXml = null;
            try {
                facesConfigXml = outputFileManager.createOutput(FACES_CONFIG_XML, library.lastModified());

                if (null != facesConfigXml) {
                    jaxbBinding.marshal(facesConfigXml, FACES_SCHEMA_LOCATION, libraryAdapter.marshal(library));
                }
            } catch (Exception e) {
                if (e instanceof CdkException) {
                    throw (CdkException) e;
                } else {
                    throw new CdkException(e);
                }
            } finally {
                if (null != facesConfigXml) {
                    try {
                        facesConfigXml.close();
                    } catch (IOException e) {
                        log.warn("IOException occured when closing facesConfigXml writer", e);
                    }
                }
            }
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Check that library is empty
     * </p>
     *
     * @param library
     * @return
     */
    private boolean empty(ComponentLibrary library) {

        return library.getComponents().isEmpty() && library.getBehaviors().isEmpty() && library.getConverters().isEmpty()
                && library.getEvents().isEmpty() && library.getFunctions().isEmpty() && library.getListeners().isEmpty()
                && library.getRenderKits().isEmpty() && library.getValidators().isEmpty()
                && library.getExtension().getExtensions().isEmpty();
    }
}
