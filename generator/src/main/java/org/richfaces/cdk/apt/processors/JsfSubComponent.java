/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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

package org.richfaces.cdk.apt.processors;

import java.lang.annotation.Annotation;

import javax.faces.event.FacesEvent;

import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.RendererSpecificComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.Test;

/**
 * This calss wraps {@link RendererSpecificComponent} annotation so it would be used by {@link ComponentProcessor} methods, so they
 * can be reused for different types of the component annotations.
 * 
 * @author akolonitsky
 * @since Mar 31, 2010
 */
public class JsfSubComponent implements JsfComponent {

    private final RendererSpecificComponent subcomponent;

    private final JsfComponent parent;

    public JsfSubComponent(RendererSpecificComponent subcomponent, JsfComponent parent) {
        this.subcomponent = subcomponent;
        this.parent = parent;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return JsfComponent.class;
    }

    @Override
    public String type() {
        return this.subcomponent.type();
    }

    @Override
    public Test test() {
        return this.subcomponent.test();
    }

    @Override
    public Tag[] tag() {
        return this.subcomponent.tag();
    }

    @Override
    public JsfRenderer renderer() {
        return this.subcomponent.renderer();
    }

    @Override
    public Class<?>[] interfaces() {
        return this.subcomponent.interfaces();
    }

    @Override
    public String generate() {
        return this.subcomponent.generate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends FacesEvent>[] fires() {
        return new Class[0];
    }

    @Override
    public String family() {
        return this.parent.family();
    }

    @Override
    public Facet[] facets() {
        return this.subcomponent.facets();
    }

    @Override
    public Description description() {
        return this.subcomponent.description();
    }

    @Override
    public RendererSpecificComponent[] components() {
        return new RendererSpecificComponent[0];
    }

    @Override
    public String[] attributes() {
        return this.subcomponent.attributes();
    }
}
