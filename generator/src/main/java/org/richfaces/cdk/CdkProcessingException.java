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

/**
 * <p class="changed_added_4_0">
 * That exception indicates recoverable CDK error, that it is, means errors in source code or configuration files that makes
 * project build failed, so no result shulde be generated, but does not stop further processing of other classes or files. CDK
 * should collect such errors but do not stop processing that let developer to know about all errors in the project.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class CdkProcessingException extends Exception {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private static final long serialVersionUID = -3696046213271071968L;

    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public CdkProcessingException() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     */
    public CdkProcessingException(String message) {
        super(message);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cause
     */
    public CdkProcessingException(Throwable cause) {
        super(cause);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     * @param cause
     */
    public CdkProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
