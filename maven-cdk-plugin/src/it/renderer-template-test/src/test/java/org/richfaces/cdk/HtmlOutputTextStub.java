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

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

class HtmlOutputTextStub extends UIComponentBase {

    private String prologText = "";
    private String bodyText = "";
    private String epilogText = "";
    
    public HtmlOutputTextStub() {
        super();
    }
    
    public HtmlOutputTextStub(String epilogText) {
        super();
        this.epilogText = epilogText;
    }

    @Override
    public String getFamily() {
        return javax.faces.component.html.HtmlOutputText.COMPONENT_FAMILY;
    }

    private void renderText(FacesContext facesContext, String text) throws IOException {
        if (text != null && text.length() > 0) {
            facesContext.getResponseWriter().writeText(text, null);
        }
    }
    
    @Override
    public boolean getRendersChildren() {
        return true;
    }
    
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        renderText(context, prologText);
    }
    
    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        renderText(context, bodyText);
    }
    
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        renderText(context, epilogText);
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
    
    public void setPrologText(String prologText) {
        this.prologText = prologText;
    }
    
    public void setEpilogText(String epilogText) {
        this.epilogText = epilogText;
    }
    
    public String getBodyText() {
        return bodyText;
    }
    
    public String getPrologText() {
        return prologText;
    }
    
    public String getEpilogText() {
        return epilogText;
    }
}