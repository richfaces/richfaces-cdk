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
package org.richfaces.cdk.templatecompiler.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Nick Belaevski
 * 
 */
public class ClassImport {

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    private String _package;

    @XmlAttribute
    private boolean _static;
    
    @XmlAttribute
    private List<String> names;
    
    /**
     * @return the package_
     */
    public String getPackage() {
        return _package;
    }
    
    /**
     * @param _package the package_ to set
     */
    public void setPackage(String _package) {
        this._package = _package;
    }
    
    /**
     * @return the static_
     */
    public boolean isStatic() {
        return _static;
    }
    
    /**
     * @param static_ the static_ to set
     */
    public void setStatic(boolean _static) {
        this._static = _static;
    }
    
    /**
     * @return the classes
     */
    public List<String> getNames() {
        return names;
    }
    
    /**
     * @param classes the classes to set
     */
    public void setNames(List<String> classes) {
        this.names = classes;
    }
 
}
