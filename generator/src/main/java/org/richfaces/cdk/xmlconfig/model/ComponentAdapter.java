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
package org.richfaces.cdk.xmlconfig.model;

import org.richfaces.cdk.model.ComponentModel;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class ComponentAdapter extends ElementAdapterBase<ComponentBean, ComponentModel> {
    private static final AttributeAdapter ATTRIBUTE_ADAPTER = new AttributeAdapter();
    private static final FacetAdapter FACET_ADAPTER = new FacetAdapter();

    @Override
    protected Class<? extends ComponentBean> getBeanClass(ComponentModel c) {
        return ComponentBean.class;
    }

    @Override
    protected Class<? extends ComponentModel> getModelClass(ComponentBean b) {
        return ComponentModel.class;
    }
}
