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

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.cdk.renderkit.html.CoercedRenderer;

/**
 * @author Nick Belaevski
 * 
 */
public class CoercionsTest extends BaseRendererTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testEncodeEnd() throws Exception {
        responseWriter.writeText(eq("0"), EasyMock.<String>isNull());        
        
        environment.replay();
        
        CoercedRenderer renderer = new CoercedRenderer();
        renderer.encodeEnd(facesContext, component);
    }
    
    @Test
    public void testEncodeEndObjectCoercedToString() throws Exception {
        componentAttributes.put("someObject", "test value");
        
        responseWriter.writeText(eq(String.valueOf("test value".length())), EasyMock.<String>isNull());        
        
        environment.replay();
        
        CoercedRenderer renderer = new CoercedRenderer();
        renderer.encodeEnd(facesContext, component);
    }

    @Test
    public void testEncodeEndWithBooleanFlags() throws Exception {
        componentAttributes.put("flag", Boolean.FALSE);
        componentAttributes.put("anotherFlag", Boolean.TRUE);
        
        responseWriter.writeText(eq("AnotherFlag"), EasyMock.<String>isNull());        
        responseWriter.writeText(eq("0"), EasyMock.<String>isNull());        
        
        environment.replay();
        
        CoercedRenderer renderer = new CoercedRenderer();
        renderer.encodeEnd(facesContext, component);
    }

    @Test
    public void testEncodeEndWithStringFlags() throws Exception {
        componentAttributes.put("flag", "true");
        componentAttributes.put("anotherFlag", "false");
        
        responseWriter.writeText(eq("Flag"), EasyMock.<String>isNull());        
        responseWriter.writeText(eq("0"), EasyMock.<String>isNull());        
        
        environment.replay();
        
        CoercedRenderer renderer = new CoercedRenderer();
        renderer.encodeEnd(facesContext, component);
    }
}