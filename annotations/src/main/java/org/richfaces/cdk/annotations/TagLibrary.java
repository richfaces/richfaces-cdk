/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation defines taglib for which all tags for JSF components from that packages belong to. Current limitation - there
 * sould be only one taglib package in the project, otherwise the last processed package will override others.
 *
 * @author asmirnov
 * @version $Id$
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface TagLibrary {
    /**
     * <p class="changed_added_4_0">
     * Library URI, the same used for JSP and Facelets.
     * </p>
     *
     * @return
     */
    String uri();

    /**
     * <p class="changed_added_4_0">
     * Library short name ( default prefix ). The same same also used for faces-config &lt;name&gt; element.
     * </p>
     *
     * @return
     */
    String shortName();

    /**
     * <p class="changed_added_4_0">
     * Default preffix for package names and JSF ids in the library
     * </p>
     *
     * @return
     */
    String prefix() default "";

    /**
     * <p class="changed_added_4_0">
     * Implementation version of the generated taglib.
     * </p>
     *
     * @return
     */
    String tlibVersion() default "";

    /**
     * <p class="changed_added_4_0">
     * JSP taglib validator. TODO - ? extends Validator ?
     * </p>
     *
     * @return
     */
    Class<?> validatorClass() default NONE.class;

    /**
     * <p class="changed_added_4_0">
     * Servlet ... listener used by JSP library. TODO - ? extends {@link EventListener} ?
     * </p>
     *
     * @return
     */
    Class<?> listenerClass() default NONE.class;

    /**
     * <p class="changed_added_4_0">
     * Library description, included into generated taglib and faces-config.E
     * </p>
     *
     * @return
     */
    String displayName() default "";

    /**
     * <p class="changed_added_4_0">
     * Java Server Pages version for generated tld, for JSP taglib only.
     * </p>
     *
     * @return
     */
    String jspVersion() default "2.0";

    public static final class NONE {
    }
}
