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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

/**
 * <p class="changed_added_4_0">Defines annotated class as JSF {@link ClientBehaviorRenderer}, or, if used in the {@link JsfBehavior} annotation,
 * defines renderer type for {@link ClientBehavior}.</p>
 * @author asmirnov@exadel.com
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface JsfBehaviorRenderer {

    /**
     * <p class="changed_added_4_0">ClientBehavior renderer type.</p>
     * @return
     */
    public String type() default "";

    /**
     * <p class="changed_added_4_0">Defines {@link RenderKit} for which generated renderer is belong to.</p>
     * @return
     */
    public String renderKitId() default RenderKitFactory.HTML_BASIC_RENDER_KIT;

    /**
     * <p class="changed_added_4_0">Fully qualified class name of the generated renderer class.</p>
     * @return
     */
    public String generate() default "";
    
    /**
     * <p class="changed_added_4_0">Renderer description to include into generated faces-config.xml</p>
     * @return
     */
    public Description description() default @Description();

}
