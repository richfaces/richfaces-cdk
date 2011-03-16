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

package org.richfaces.cdk.annotations;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="changed_added_4_0">
 * This annotation tells CDK to generate event-related classes:
 * <ul>
 * <li>Event listener interface</li>
 * <li>Event source interface</li>
 * <li>Listener tag handler and wrapper to bind listener to the EL-expression.</li>
 * </ul>
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Inherited
public @interface Event {

    public static final String NAME = "org.richfaces.cdk.annotations.Event";

    /**
     * <p class="changed_added_4_0">Used in the {@link JsfComponent} only, to define event type.</p>
     * @return
     */
    public Class<? extends FacesEvent> value() default DEFAULT.class;
    /**
     * <p class="changed_added_4_0">
     * The listener interface class that process annotated event.
     * </p>
     * 
     * @return name of listener interface
     */
    public Class<? extends FacesListener> listener() default DEFAULT_LISTENER.class;

    /**
     * <p class="changed_added_4_0">Name for the listener interface method that process annotated event type.</p>
     * @return
     */
    public String listenerMethod() default "";

    /**
     * <p class="changed_added_4_0">
     * Name of the interface class that fires annotated events and accepts its listeners.
     * </p>
     * 
     * @return name of generated source interface..
     */
    public String source() default "";

    /**
     * <p class="changed_added_4_0">
     * View Description Language, JSP or Facelets, tags for event listeners, eg &lt;foo:barListener&gt;.
     * </p>
     * 
     * @return
     */
    public Tag[] tag() default {};

    @SuppressWarnings("serial")
    static final class DEFAULT extends FacesEvent {

        public DEFAULT(UIComponent component) {
            super(component);
        }

        @Override
        public boolean isAppropriateListener(FacesListener listener) {
            return false;
        }

        @Override
        public void processListener(FacesListener listener) {
        }
        }
    
    static final class DEFAULT_LISTENER implements FacesListener {
        
    }
}
