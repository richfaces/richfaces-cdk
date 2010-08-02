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
package org.richfaces.cdk;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.same;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jboss.test.faces.mock.MockFacesEnvironment;
import org.junit.After;
import org.junit.Before;

/**
 * @author Nick Belaevski
 * 
 */
public class BaseRendererTest {

    protected final String CLIENT_ID = "mainForm:personEntry";
    
    protected MockFacesEnvironment environment;

    protected ResponseWriter responseWriter;

    protected FacesContext facesContext;
    
    protected UIComponent component;

    protected Map<String, Object> componentAttributes;

    @Before
    public void setUp() throws Exception {
        environment = MockFacesEnvironment.createEnvironment();
        facesContext = environment.getFacesContext();
        component = environment.createMock(UIComponentBase.class);

        componentAttributes = new HashMap<String, Object>();
        expect(component.getAttributes()).andStubReturn(componentAttributes);

        expect(component.getClientId(same(environment.getFacesContext()))).andStubReturn(CLIENT_ID);
        
        responseWriter = environment.createMock(ResponseWriter.class);
        expect(environment.getFacesContext().getResponseWriter()).andStubReturn(responseWriter);
        expect(responseWriter.getContentType()).andStubReturn("text/html");
    }

    @After
    public void tearDown() throws Exception {
        environment.verify();
        environment.release();
        environment = null;

        responseWriter = null;
        facesContext = null;
        component = null;
        componentAttributes = null;
    }

}
