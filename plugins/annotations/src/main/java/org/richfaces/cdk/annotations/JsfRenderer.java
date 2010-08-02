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

import javax.faces.render.RenderKitFactory;

/**
 * <p class="changed_added_4_0">
 * The presence of this annotation in the JSF component class associated particular renderer with component.
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface JsfRenderer {

    public static final String NAME = "org.richfaces.cdk.annotations.JsfRenderer";

    /**
     * <p class="changed_added_4_0">
     * The value of this annotation attribute is taken to be JSF <em>renderer-type</em>. If this value was empty,
     * component type will be inferred from by the CDK.
     * </p>
     * 
     * @return JSF <em>renderer-type</em>.
     */
    public String type() default "";

    /**
     * <p class="changed_added_4_0">Renderer Family</p>
     * @return
     */
    public String family() default "";

    /**
     * <p class="changed_added_4_0">RenderKit for which this renderer is belong to</p>
     * @return
     */
    public String renderKitId() default RenderKitFactory.HTML_BASIC_RENDER_KIT;

    public String template() default "";

    public Description description() default @Description();

}
