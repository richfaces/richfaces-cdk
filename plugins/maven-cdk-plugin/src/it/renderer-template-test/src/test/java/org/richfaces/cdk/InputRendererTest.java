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
import org.richfaces.cdk.renderkit.html.InputRenderer;

/**
 * @author Nick Belaevski
 * 
 */
public class InputRendererTest extends BaseRendererTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
        List<UIComponent> children = component.getChildren();
        children.add(new HtmlOutputTextStub("Single child"));
    }
    
    @Test
    public void testEncodeBeginDefaultComponent() throws Exception {
        responseWriter.startElement(eq("span"), same(component));
        responseWriter.writeAttribute(eq("id"), eq(CLIENT_ID), EasyMock.<String>isNull());
        
        environment.replay();
        
        InputRenderer renderer = new InputRenderer();
        renderer.encodeBegin(facesContext, component);
    }

    @Test
    public void testEncodeChildrenDefaultComponent() throws Exception {
        responseWriter.writeText(eq("Single child"), EasyMock.<String>isNull());
        
        environment.replay();
        
        InputRenderer renderer = new InputRenderer();
        renderer.encodeChildren(facesContext, component);
    }

    
    @Test
    public void testEncodeEndDefaultComponent() throws Exception {
        responseWriter.startElement(eq("input"), same(component));
        responseWriter.writeAttribute(eq("type"), eq("text"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("input"));
        
        responseWriter.writeText(eq("No image defined"), EasyMock.<String>isNull());
        
        responseWriter.endElement(eq("span"));
        
        environment.replay();
        
        InputRenderer renderer = new InputRenderer();
        renderer.encodeEnd(facesContext, component);
    }

    @Test
    public void testEncodeEndDisabledComponent() throws Exception {
        componentAttributes.put("disabled", Boolean.TRUE);
        
        responseWriter.startElement(eq("input"), same(component));
        responseWriter.writeAttribute(eq("disabled"), eq(Boolean.TRUE), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("type"), eq("text"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("input"));
        
        responseWriter.endElement(eq("span"));
        
        environment.replay();
        
        InputRenderer renderer = new InputRenderer();
        renderer.encodeEnd(facesContext, component);
    }
    
    @Test
    public void testEncodeEndDisabledImage() throws Exception {
        componentAttributes.put("image", "http://site/img.png");
        componentAttributes.put("disabledImage", "http://site/disabled.png");
        
        responseWriter.startElement(eq("input"), same(component));
        responseWriter.writeAttribute(eq("type"), eq("text"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("input"));

        responseWriter.startElement(eq("img"), same(component));
        responseWriter.writeAttribute(eq("alt"), eq(""), EasyMock.<String>isNull());
        responseWriter.writeURIAttribute(eq("src"), eq("http://site/disabled.png"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("img"));
        
        responseWriter.endElement(eq("span"));

        environment.replay();
        
        InputRenderer renderer = new InputRenderer();
        renderer.encodeEnd(facesContext, component);
    }

    @Test
    public void testEncodeEndImage() throws Exception {
        componentAttributes.put("image", "http://site/img.png");
        
        responseWriter.startElement(eq("input"), same(component));
        responseWriter.writeAttribute(eq("type"), eq("text"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("input"));

        responseWriter.startElement(eq("img"), same(component));
        responseWriter.writeAttribute(eq("alt"), eq(""), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("onclick"), eq("alert('clicked')"), EasyMock.<String>isNull());
        responseWriter.writeURIAttribute(eq("src"), eq("http://site/img.png"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("img"));
        
        responseWriter.endElement(eq("span"));

        environment.replay();
        
        InputRenderer renderer = new InputRenderer();
        renderer.encodeEnd(facesContext, component);
    }
}