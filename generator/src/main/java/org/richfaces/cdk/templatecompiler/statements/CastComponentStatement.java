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
package org.richfaces.cdk.templatecompiler.statements;

import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.TemplateModel;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

import com.google.inject.Inject;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class CastComponentStatement extends FreeMarkerTemplateStatementBase {
    private ELType type;
    private String componentParameter;

    @Inject
    public CastComponentStatement(@TemplateModel FreeMarkerRenderer renderer) {
        super(renderer, "cast-component");
    }

    /**
     * @return the type
     */
    public ELType getType() {
        return type;
    }

    public void setType(ELType type) {
        this.addImports(type.getRequiredImports());
        this.type = type;
    }

    public String getComponentParameter() {
        return componentParameter;
    }

    public void setComponentParameter(String componentParameter) {
        this.componentParameter = componentParameter;
    }
}