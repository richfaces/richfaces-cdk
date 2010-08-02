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

package org.richfaces.cdk.test.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.Tag;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@Event(listener = TestListener.class, 
       source = "org.richfaces.cdk.test.event.TestSource",
       listenerMethod="process",
       tag = @Tag(name = "testListener", generate = true, handler = "org.richfaces.cdk.test.view.facelets.TestListenerHandler"))
public class TestEvent extends FacesEvent {

    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private static final long serialVersionUID = -2659567077711540715L;

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param component
     */
    public TestEvent(UIComponent component) {
        super(component);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
     */
    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof TestListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
     */
    @Override
    public void processListener(FacesListener listener) {
        if (listener instanceof TestListener) {
            TestListener testListener = (TestListener) listener;
            testListener.process(this);
        }
    }

}
