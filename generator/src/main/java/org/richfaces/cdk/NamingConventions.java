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
package org.richfaces.cdk;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.InvalidNameException;

/**
 * <p class="changed_added_4_0">
 * That interface defines methods that calculates names according to <a href="http://www.jboss.org/community/docs/DOC-13693">CDK
 * naming conventions</>
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public interface NamingConventions {
    /**
     * <p class="changed_added_4_0">
     * Calculates component type from class name.
     * </p>
     * <ul>
     * <li><code>&lt;prefix&gt;.component.Abstract&lt;Name&gt;</code> =&gt;<code>&lt;prefix&gt;.&lt;Name&gt;</code></li>
     * <li><code>&lt;prefix&gt;.component.&lt;Name&gt;Base</code> =&gt; <code>&lt;prefix&gt;.&lt;Name&gt;</code></li>
     * <li><code>&lt;prefix&gt;.component.UI&lt;Name&gt;</code> =&gt; <code>&lt;prefix&gt;.&lt;Name&gt;</code></li>
     * </ul>
     *
     * @param componentClass
     * @return JSF component type.
     * @throws InvalidNameException if className does not match naming conventions.
     */
    FacesId inferComponentType(ClassName componentClass) throws InvalidNameException;

    /**
     * <p class="changed_added_4_0">
     * Calculates concrete component class from explicit value or type.
     * </p>
     * <ul>
     * <li>Calculate name from type as <code>&lt;prefix&gt;.&lt;Name&gt;</code> =&gt;
     * <code>&lt;prefix&gt;.component.UI&lt;Name&gt;</code> .</li>
     * </ul>
     *
     * @param componentType JSF component type.
     * @return Descriptor of generated UIComponent class.
     * @throws InvalidNameException if component type does not follow naming conventions.
     */
    ClassName inferUIComponentClass(FacesId componentType) throws InvalidNameException;

    /**
     * <p class="changed_added_4_0">
     * Infer component family by component type
     * </p>
     *
     * @param componentType
     * @return
     * @throws InvalidNameException
     */
    FacesId inferUIComponentFamily(FacesId componentType) throws InvalidNameException;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param componentType
     * @param markup
     * @return
     * @throws InvalidNameException
     */
    ClassName inferTagHandlerClass(FacesId componentType, String markup) throws InvalidNameException;

    /**
     * <p class="changed_added_4_0">
     * Infer VDL tag name by component type
     * </p>
     *
     * @param componentType
     * @return
     * @throws InvalidNameException
     */
    String inferTagName(FacesId componentType) throws InvalidNameException;

    /**
     * <p class="changed_added_4_0">
     * Determine renderer type by Java class
     * </p>
     *
     * @param targetClass
     * @return
     */
    FacesId inferRendererType(ClassName targetClass);

    /**
     * <p class="changed_added_4_0">
     * Infer renderer family by renderer type
     * </p>
     *
     * @param type
     * @return
     */
    FacesId inferRendererFamily(FacesId type);

    /**
     * <p class="changed_added_4_0">
     * Infer renderer type from template file path
     * </p>
     *
     * @param templateName
     * @return
     */
    FacesId inferRendererTypeByTemplatePath(String templateName);

    /**
     * <p class="changed_added_4_0">
     * Infer Java class name by renderer type.
     * </p>
     *
     * @param id
     * @return
     */
    ClassName inferRendererClass(FacesId id);

    /**
     * <p class="changed_added_4_0">
     * Infer behavior type by class name
     * </p>
     *
     * @param targetClass
     * @return
     */
    FacesId inferBehaviorType(ClassName targetClass);

    /**
     * <p class="changed_added_4_0">
     * Infer name for generated behavior class by type
     * </p>
     *
     * @param id
     * @return
     */
    ClassName inferBehaviorClass(FacesId id);

    /**
     * <p class="changed_added_4_0">
     * Determine default taglib uri from library model.
     * </p>
     *
     * @param library
     * @return
     */
    String inferTaglibUri(ComponentLibrary library);

    /**
     * <p class="changed_added_4_0">
     * Infer short taglib name from URI
     * </p>
     *
     * @param uri
     * @return
     */
    String inferTaglibName(String uri);

    /**
     * <p class="changed_added_4_0">
     * Infer renderer type by component type or family.
     * </p>
     *
     * @param id
     * @return
     */
    FacesId inferRendererType(FacesId id);
}
