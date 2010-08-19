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
package org.richfaces.cdk.resource.handler.impl;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceFactoryImpl;
import org.richfaces.util.Util;

/**
 * @author Nick Belaevski
 * 
 */
public class DynamicResourceHandler extends AbstractResourceHandler  {

    private ResourceFactory resourceFactory;
    
    private ResourceHandler staticResourceHandler;
    
    public DynamicResourceHandler(ResourceHandler staticResourceHandler) {
        this.staticResourceHandler = staticResourceHandler;
        this.resourceFactory = new ResourceFactoryImpl(staticResourceHandler);
    }
    
    private void setupResourceState(Resource source, Resource target) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object state = Util.saveResourceState(facesContext, source);
        if (state != null) {
            Util.restoreResourceState(facesContext, target, state);
        }
    }
    
    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Resource result = resourceFactory.createResource(resourceName, libraryName, contentType);
        
        if (result != null) {
            Resource newResource = resourceFactory.createResource(resourceName, libraryName, contentType);
            setupResourceState(newResource, result);
            result = new DynamicResourceWrapper(result);
        } else {
            result = staticResourceHandler.createResource(resourceName, libraryName, contentType);
        }
        
        return result;
    }

}
