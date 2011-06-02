/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.cdk.apt.processors;

import javax.lang.model.element.AnnotationMirror;

import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.DescriptionGroup;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class DescriptionProcessorImpl implements DescriptionProcessor {
    private final Provider<SourceUtils> utilsProvider;

    @Inject
    public DescriptionProcessorImpl(Provider<SourceUtils> utilsProvider) {
        this.utilsProvider = utilsProvider;
    }

    @Override
    public void processDescription(DescriptionGroup model, AnnotationMirror description, String docComment) {
        if (!Strings.isEmpty(docComment)) {
            model.setDescription(docComment);
        }
        if (description != null) {
            SourceUtils utils = utilsProvider.get();
            setIcon(model, description);
            utils.setModelProperty(model, description, "displayName");
            utils.setModelProperty(model, description, "description", "value");
        }
    }

    @Override
    public void processDescription(DescriptionGroup model, AnnotationMirror description) {
        processDescription(model, description, null);
    }

    protected void setIcon(DescriptionGroup component, AnnotationMirror description) {
        SourceUtils utils = utilsProvider.get();
        if (null != description
                && (!utils.isDefaultValue(description, "smallIcon") || !utils.isDefaultValue(description, "largeIcon"))) {
            DescriptionGroup.Icon iconValue = new DescriptionGroup.Icon();
            utils.setModelProperty(iconValue, description, "smallIcon");
            utils.setModelProperty(iconValue, description, "largeIcon");
            component.setIcon(iconValue);
        }
    }
}
