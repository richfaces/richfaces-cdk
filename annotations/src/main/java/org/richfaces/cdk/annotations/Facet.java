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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.component.UIComponent;

/**
 * <p class="changed_added_4_0">Describes facet of {@link UIComponent}.
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@Inherited
public @interface Facet {

    public static final String NAME = "org.richfaces.cdk.annotations.Facet";

    /**
     * <p class="changed_added_4_0">
     * The name of that facet.
     * </p>
     * 
     * @return
     */
    /**
     * <p class="changed_added_4_0">Facet name</p>
     * @return
     */
    String name() default "";
    
    /**
     * <p class="changed_added_4_0">If true, getter and setter methods for this facet will be generated.</p>
     * @return
     */
    boolean generate() default true;
    
    /**
     * <p class="changed_added_4_0">Facet description.</p>
     * @return
     */
    Description description() default @Description;
    

}
