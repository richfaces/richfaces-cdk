/**
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

import org.richfaces.cdk.model.ValidatorModel;

/**
 * @author akolonitsky
 * @since Jan 13, 2010
 */
public class ValidatorAdapter extends ElementAdapterBase<ValidatorBean, ValidatorModel> {
    @Override
    protected Class<? extends ValidatorBean> getBeanClass(ValidatorModel model) {
        return ValidatorBean.class;
    }

    @Override
    protected Class<? extends ValidatorModel> getModelClass(ValidatorBean bean) {
        return ValidatorModel.class;
    }
}
