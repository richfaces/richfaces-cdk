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
package org.richfaces.cdk.model;

import org.richfaces.cdk.CdkException;

/**
 * <p class="changed_added_4_0">
 * That exception indicates what some name does not meet CDK naming conventions.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
@SuppressWarnings("serial")
public class InvalidNameException extends CdkException {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public InvalidNameException() {
        super();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     */
    public InvalidNameException(String message) {
        super(message);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cause
     */
    public InvalidNameException(Throwable cause) {
        super(cause);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     * @param cause
     */
    public InvalidNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
