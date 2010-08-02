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
import org.richfaces.cdk.renderkit.html.CommandLinkRenderer;

/**
 * @author Nick Belaevski
 * 
 */
public class LinkRendererTest extends BaseRendererTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
        List<UIComponent> children = component.getChildren();
        children.add(new HtmlOutputTextStub("Child text"));
    }

    private void initializeBasePathAttributes() {
        componentAttributes.put("href", "http://some-site");
        componentAttributes.put("value", "The link");
        componentAttributes.put("anotherValue", Integer.valueOf(12));
        componentAttributes.put("styleClass", "menu-link");
        componentAttributes.put("onclick", "this.disabled='true'");
        componentAttributes.put("target", "_blank");
        componentAttributes.put("rel", "Next");
        componentAttributes.put("rev", "Prev");
        componentAttributes.put("imageSource", "http://some-site/img.png");
        componentAttributes.put("imageStyleClass", "my-link-image");
    }
    
    @Test
    public void testEncodeBeginBasePath() throws Exception {
        initializeBasePathAttributes();

        environment.replay();
        
        CommandLinkRenderer renderer = new CommandLinkRenderer();
        renderer.encodeBegin(facesContext, component);
    }

    @Test
    public void testEncodeChildrenBasePath() throws Exception {
        initializeBasePathAttributes();

        responseWriter.writeText(eq("Child text"), EasyMock.<String>isNull());
        
        environment.replay();
        
        CommandLinkRenderer renderer = new CommandLinkRenderer();
        renderer.encodeChildren(facesContext, component);
    }
    
    @Test
    public void testEncodeEndBasePath() throws Exception {
        initializeBasePathAttributes();

        responseWriter.startElement(eq("a"), same(component));
        
        //attributes
        responseWriter.writeAttribute(eq("id"), eq(CLIENT_ID), EasyMock.<String>isNull());
        responseWriter.writeURIAttribute(eq("href"), eq("http://some-site"), EasyMock.<String>isNull());

        //pass-through attributes
        responseWriter.writeAttribute(eq("class"), eq("menu-link"), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("onclick"), eq("this.disabled='true'"), EasyMock.<String>isNull());
        
        responseWriter.startElement(eq("img"), same(component));
        responseWriter.writeAttribute(eq("alt"), eq(""), EasyMock.<String>isNull());
        responseWriter.writeAttribute(eq("class"), eq("rich-link  my-link-image"), EasyMock.<String>isNull());
        responseWriter.writeURIAttribute(eq("src"), eq("http://some-site/img.png"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("img"));
        
        responseWriter.writeText(eq("value: The link; differentValue: ; anotherValue: 12;"), EasyMock.<String>isNull());
        
        responseWriter.endElement(eq("a"));
        
        environment.replay();
        
        CommandLinkRenderer renderer = new CommandLinkRenderer();
        renderer.encodeEnd(facesContext, component);
    }

    @Test
    public void testEncodeBeginShortPath() throws Exception {
        environment.replay();
        
        CommandLinkRenderer renderer = new CommandLinkRenderer();
        renderer.encodeBegin(facesContext, component);
    }

    @Test
    public void testEncodeChildrenShortPath() throws Exception {
        responseWriter.writeText(eq("Child text"), EasyMock.<String>isNull());

        environment.replay();
        
        CommandLinkRenderer renderer = new CommandLinkRenderer();
        renderer.encodeChildren(facesContext, component);
    }
    
    @Test
    public void testEncodeEndShortPath() throws Exception {
        responseWriter.startElement(eq("a"), same(component));
        
        //attributes
        responseWriter.writeAttribute(eq("id"), eq(CLIENT_ID), EasyMock.<String>isNull());

        responseWriter.writeText(eq("value: ; differentValue: ; anotherValue: ;"), EasyMock.<String>isNull());
        responseWriter.endElement(eq("a"));

        environment.replay();

        CommandLinkRenderer renderer = new CommandLinkRenderer();
        renderer.encodeEnd(facesContext, component);
    }
}