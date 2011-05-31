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
package org.richfaces.cdk.templatecompiler;

import javax.xml.namespace.QName;

import org.richfaces.cdk.templatecompiler.model.Template;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateModel;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class QNameTemplateModel extends StringModel implements TemplateModel {
    private final QName qName;

    public QNameTemplateModel(QName obj, BeansWrapper wrapper) {
        super(obj, wrapper);
        this.qName = obj;
    }

    @Override
    public String getAsString() {
        StringBuilder nameBuilder = new StringBuilder();
        if (!Template.isDefaultNamespace(qName)) {
            nameBuilder.append(qName.getPrefix()).append(':');
        }
        return nameBuilder.append(qName.getLocalPart()).toString();
    }
}
