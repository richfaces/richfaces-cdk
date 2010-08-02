/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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

import java.io.Serializable;
import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Nick Belaevski
 * @since 4.0
 */
@XmlRootElement(name = "resourceDependency", namespace = Template.CDK_NAMESPACE)
public class ResourceDependency implements Serializable {

    private static final long serialVersionUID = -7513798674871079584L;
    
    private String name;

    private String library ="";

    private String target = "";

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the name
     */
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the libraryName
     */
    @XmlAttribute(name = "library")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    public String getLibrary() {
        return library;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param library the libraryName to set
     */
    public void setLibrary(String library) {
        this.library = library;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the target
     */
    @XmlAttribute(name = "target")
    public String getTarget() {
        return target;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return MessageFormat.format("ResourceDependency[name={0}, library={1}, target={2}]",
            getName(), getLibrary(), getTarget());
    }
}
