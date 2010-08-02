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

package org.richfaces.cdk.test.component;

import org.richfaces.cdk.annotations.JsfConverter;
import org.richfaces.cdk.annotations.JsfBehavior;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.convert.Converter;
import javax.faces.event.BehaviorEvent;

/**
 * @author akolonitsky
 * @since Jan 14, 2010
 */
@JsfBehavior(id = "my_behavior")
public class MyBehavior implements Behavior {

    @Override
    public void broadcast(BehaviorEvent event) { }
}

@JsfBehavior
class MyBehavior04 implements Behavior {

    @Override
    public void broadcast(BehaviorEvent event) { }
}
