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

import java.io.File;
import java.io.FileNotFoundException;

import org.richfaces.cdk.Cache;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.ModelBuilder;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.apt.CacheType;
import org.richfaces.cdk.apt.LibraryCache;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.util.JavaUtils;
import org.richfaces.cdk.xmlconfig.model.FacesConfigAdapter;
import org.richfaces.cdk.xmlconfig.model.FacesConfigBean;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class FacesConfigParser implements ModelBuilder {
    private static final FacesConfigAdapter ADAPTER = new FacesConfigAdapter();
    @Inject
    private Logger log;
    @Inject
    private JAXB jaxbBinding;
    @Inject
    private ComponentLibrary library;
    @Inject
    @Source(Sources.FACES_CONFIGS)
    private FileManager configFiles;

    @Inject
    @Cache(CacheType.NON_JAVA_SOURCES)
    private LibraryCache nonJavaCache;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.ModelBuilder#build()
     */
    @Override
    public void build() throws CdkException {
        for (File file : configFiles.getFiles()) {
            if (nonJavaCache.storedBefore(file.lastModified())) {
                try {
                    FacesConfigBean unmarshal = unmarshalFacesConfig(file);
                    if (null != unmarshal) {
                        ComponentLibrary facesConfig = ADAPTER.unmarshal(unmarshal);
                        library.getComponents().addAll(facesConfig.getComponents());
                        library.getRenderKits().addAll(facesConfig.getRenderKits());
                        library.getConverters().addAll(facesConfig.getConverters());
                        library.getValidators().addAll(facesConfig.getValidators());
                        library.getBehaviors().addAll(facesConfig.getBehaviors());
                        library.getFunctions().addAll(facesConfig.getFunctions());
                        library.getEvents().addAll(facesConfig.getEvents());
                        if (null != unmarshal.getMetadataComplete()) {
                            library.setMetadataComplete(unmarshal.getMetadataComplete());
                        }
                        library.getExtension().getExtensions().addAll(facesConfig.getExtension().getExtensions());
                        if (null != facesConfig.getTaglib()) {
                            if (null == library.getTaglib()) {
                                library.setTaglib(facesConfig.getTaglib());
                            } else {
                                JavaUtils.copyProperties(facesConfig.getTaglib(), library.getTaglib());
                            }
                        }
                        if (null != facesConfig.getPrefix()) {
                            library.setPrefix(facesConfig.getPrefix());
                        }
                    }
                } catch (FileNotFoundException e) {
                    log.error("faces-config not found", e);
                }
            }
        }
    }

    protected FacesConfigBean unmarshalFacesConfig(File file) throws CdkException, FileNotFoundException {
        return jaxbBinding.unmarshal(file, ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION, FacesConfigBean.class);
    }
}
