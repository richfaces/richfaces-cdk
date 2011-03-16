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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.faces.view.facelets.TagHandler;
import javax.faces.webapp.UIComponentTagBase;

/**
 * <p class="changed_added_4_0">
 * Defines Faces VDL ( Facelets,JSP )tag.
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Tag {

    public static final String NAME = "org.richfaces.cdk.annotations.Tag";

    /**
     * <p class="changed_added_4_0">
     * Name of the JSF tag that creates target component.
     * </p>
     * 
     * @return tag name.
     */
    public String name() default "";

    /**
     * <p class="changed_added_4_0">Defines target View Description Language: JSP, Facelets, or both.</p>
     * @return
     */
    public TagType type() default TagType.Facelets;

    /**
     * <p class="changed_added_4_0">Tag handler class. Fully qualified class name of the generated or
     * existing tag handler. For {@link TagType#Jsp} it's {@link JspTag} or, more likely, {@link UIComponentTagBase} instance.
     * For facelets, it's {@link TagHandler} instance.</p>
     * @return
     */
    public String handler() default "";

    /**
     * <p class="changed_added_4_0">Base class for generated tag handler. Default value depends from {@link #type()} attribute value.</p>
     * @return
     */
    public String baseClass() default "";

    /**
     * <p class="changed_added_4_0">Flag indicates that special tag handler should be generated.</p>
     * @return
     */
    public boolean generate() default false;

}
