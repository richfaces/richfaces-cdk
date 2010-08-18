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

import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.model.DescriptionGroup;
import org.richfaces.cdk.util.Strings;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
public class DescriptionProcessorImpl implements DescriptionProcessor {

    @Override
    public void processDescription(DescriptionGroup model, Description description, String docComment) {
        if (!Strings.isEmpty(docComment)) {
            model.setDescription(docComment);
        }
        if (description != null) {
            setIcon(model, description);
            if (!Strings.isEmpty(description.displayName())) {
                model.setDisplayname(description.displayName());
            }
            if (!Strings.isEmpty(description.value())) {
                model.setDescription(description.value());
            }
        }
    }

    @Override
    public void processDescription(DescriptionGroup model, Description description) {
        processDescription(model, description, null);

    }

    protected void setIcon(DescriptionGroup component, Description icon) {
        if (null != icon && (!Strings.isEmpty(icon.smallIcon()) || !Strings.isEmpty(icon.largeIcon()))) {
            DescriptionGroup.Icon iconValue = new DescriptionGroup.Icon();

            if (!Strings.isEmpty(icon.smallIcon())) {
                iconValue.setSmallIcon(icon.smallIcon());
            }

            if (!Strings.isEmpty(icon.largeIcon())) {
                iconValue.setLargeIcon(icon.largeIcon());
            }

            component.setIcon(iconValue);
        }
    }

}
