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

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.same;

import java.util.List;

import javax.faces.component.UIComponent;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.cdk.renderkit.html.GridRenderer;

/**
 * @author Nick Belaevski
 * 
 */
public class GridRendererTest extends BaseRendererTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        
        List<UIComponent> children = component.getChildren();
        children.add(new HtmlOutputTextStub("First child"));
        children.add(new HtmlOutputTextStub("Second child"));
    }

    @Test
    public void testEncodeBegin() throws Exception {
        responseWriter.startElement(eq("table"), same(component));
        responseWriter.writeAttribute(eq("id"), eq(CLIENT_ID), EasyMock.<String>isNull());        
        responseWriter.startElement(eq("tbody"), same(component));
        
        environment.replay();
        
        GridRenderer renderer = new GridRenderer();
        renderer.encodeBegin(facesContext, component);
    }
    
    @Test
    public void testEncodeChildren() throws Exception {
        responseWriter.startElement(eq("tr"), EasyMock.<UIComponent>notNull());
        responseWriter.startElement(eq("td"), EasyMock.<UIComponent>notNull());

        responseWriter.startElement(eq("div"), EasyMock.<UIComponent>notNull());
        responseWriter.writeText(eq("First child"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("div"));
        
        responseWriter.endElement(eq("td"));
        responseWriter.endElement(eq("tr"));
        

        responseWriter.startElement(eq("tr"), EasyMock.<UIComponent>notNull());
        responseWriter.startElement(eq("td"), EasyMock.<UIComponent>notNull());

        responseWriter.startElement(eq("div"), EasyMock.<UIComponent>notNull());
        responseWriter.writeText(eq("Second child"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("div"));
        
        responseWriter.endElement(eq("td"));
        responseWriter.endElement(eq("tr"));
        
        environment.replay();
        
        GridRenderer renderer = new GridRenderer();
        renderer.encodeChildren(facesContext, component);
    }
    
    @Test
    public void testEncodeEnd() throws Exception {
        responseWriter.endElement(eq("tbody"));
        responseWriter.endElement(eq("table"));

        environment.replay();
        
        GridRenderer renderer = new GridRenderer();
        renderer.encodeEnd(facesContext, component);
    }

}