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

package org.richfaces.cdk.test.component;

import javax.el.MethodExpression;
import javax.faces.component.ActionSource2;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionListener;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@FacesComponent("cdk.TestCommand")
public class UITestCommand extends UIComponentBase implements ActionSource2 {
    private static final String COMPONENT_FAMILY = "cdk.TestFamily";

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.UIComponent#getFamily()
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#addActionListener(javax.faces.event.ActionListener)
     */
    public void addActionListener(ActionListener listener) {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#getAction()
     */
    public MethodBinding getAction() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#getActionListener()
     */
    public MethodBinding getActionListener() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#getActionListeners()
     */
    public ActionListener[] getActionListeners() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#isImmediate()
     */
    public boolean isImmediate() {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#removeActionListener(javax.faces.event.ActionListener)
     */
    public void removeActionListener(ActionListener listener) {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#setAction(javax.faces.el.MethodBinding)
     */
    public void setAction(MethodBinding action) {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#setActionListener(javax.faces.el.MethodBinding)
     */
    public void setActionListener(MethodBinding actionListener) {

        // TODO Auto-generated method stub
    }

    public void setActionExpression(MethodExpression action) {

        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.component.ActionSource#setImmediate(boolean)
     */
    public void setImmediate(boolean immediate) {

        // TODO Auto-generated method stub
    }

    public MethodExpression getActionExpression() {

        // TODO Auto-generated method stub
        return null;
    }
}
