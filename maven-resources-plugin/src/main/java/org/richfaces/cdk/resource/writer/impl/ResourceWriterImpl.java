/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.resource.writer.impl;

import static org.richfaces.cdk.strings.Constants.COLON_JOINER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.Resource;

import org.apache.maven.plugin.logging.Log;
import org.richfaces.cdk.ResourceWriter;
import org.richfaces.cdk.resource.writer.ResourceProcessor;
import org.richfaces.cdk.strings.Constants;
import org.richfaces.resource.ResourceFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceWriterImpl implements ResourceWriter {

    private static final class ResourceInputStreamSupplier implements InputSupplier<InputStream> {

        private Resource resource;

        public ResourceInputStreamSupplier(Resource resource) {
            super();
            this.resource = resource;
        }

        @Override
        public InputStream getInput() throws IOException {
            return resource.getInputStream();
        }

    }

    private File resourceContentsDir;
    
    private File resourceMappingDir;
    
    private Map<String, String> processedResources = Maps.newConcurrentMap();
    
    private Iterable<ResourceProcessor> resourceProcessors;

    private Log log;
    
    private long currentTime;
    
    public ResourceWriterImpl(File resourceContentsDir, File resourceMappingDir, Iterable<ResourceProcessor> resourceProcessors, Log log) {
        this.resourceContentsDir = resourceContentsDir;
        this.resourceMappingDir = resourceMappingDir;
        this.resourceProcessors = Iterables.concat(resourceProcessors, Collections.singleton(ThroughputResourceProcessor.INSTANCE));
        this.log = log;
        
        resourceContentsDir.mkdirs();
        
        currentTime = System.currentTimeMillis();
    }

    private String getResourceQualifier(Resource resource) {
        return COLON_JOINER.join(resource.getLibraryName(), resource.getResourceName());
    }
    
    private synchronized File createOutputFile(String path) throws IOException {
        File outFile = new File(resourceContentsDir, path);
        outFile.getParentFile().mkdirs();
        
        if (outFile.exists()) {
            if (outFile.lastModified() > currentTime) {
                log.warn(MessageFormat.format("File {0} already exists and will be overwritten", outFile.getPath()));
            }
            outFile.delete();
        }

        if (!outFile.createNewFile()) {
            log.warn(MessageFormat.format("Could not create {0} file", outFile.getPath()));
        }
        

        return outFile;
    }
    
    public void writeResource(String skinName, Resource resource) throws IOException {
        final String requestPath = resource.getRequestPath();
        String requestPathWithSkin = requestPath;
        
        if (requestPath.startsWith(ResourceFactory.SKINNED_RESOURCE_PREFIX)) {
            requestPathWithSkin = Constants.SLASH_JOINER.join(skinName, 
                requestPath.substring(ResourceFactory.SKINNED_RESOURCE_PREFIX.length()));
        }

        ResourceProcessor matchingProcessor = Iterables.get(Iterables.filter(resourceProcessors, new Predicate<ResourceProcessor>() {
            @Override
            public boolean apply(ResourceProcessor input) {
                return input.isSupportedFile(requestPath);
            }
        }), 0);
        
        File outFile = createOutputFile(requestPathWithSkin); 
        
        matchingProcessor.process(requestPathWithSkin, new ResourceInputStreamSupplier(resource), Files.newOutputStreamSupplier(outFile));
        processedResources.put(getResourceQualifier(resource), requestPath);
    }

    @Override
    public void writeProcessedResourceMappings() throws IOException {
        //TODO separate mappings file location
        FileOutputStream fos = null;
        try {
            File mappingsFile = new File(resourceMappingDir, ResourceFactory.STATIC_RESOURCE_MAPPINGS);
            //TODO merge properties
            mappingsFile.delete();
            mappingsFile.getParentFile().mkdirs();
            mappingsFile.createNewFile();
            
            fos = new FileOutputStream(mappingsFile);
            Properties properties = new Properties();
            properties.putAll(processedResources);
            properties.store(fos, null);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // TODO: handle exception
            }
        }
    }

}
