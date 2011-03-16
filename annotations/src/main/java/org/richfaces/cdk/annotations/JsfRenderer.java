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
 * This annotation defines concrete class as JSF {@link Renderer}, or abstract class as the base for generated
 * Renderer implementation.
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
     * type will be inferred by the CDK.
     * </p>
     * 
     * @return JSF <em>renderer-type</em>.
     */
    public String type() default "";

    /**
     * <p class="changed_added_4_0">Component Family for which renderer from annotated class will be used.</p>
     * @return
     */
    public String family() default "";
    
    
    /**
     * <p class="changed_added_4_0">Name of the generated renderer class. Default value means nothing to genrate from concrete class,
     * or infer name by convention for abstract class.</p>
     * @return
     */
    public String generate() default "";

    /**
     * <p class="changed_added_4_0">RenderKit ID for which this renderer is belong to</p>
     * @return
     */
    public String renderKitId() default RenderKitFactory.HTML_BASIC_RENDER_KIT;

    /**
     * <p class="changed_added_4_0">Path to renderer template, relative to templates folder defined by build ( Maven plugin or ant task ).</p>
     * @return
     */
    public String template() default "";

    /**
     * <p class="changed_added_4_0">
     * Description to include into generated faces-config and taglib.
     * </p>
     * @return
     */
    public Description description() default @Description();

}
