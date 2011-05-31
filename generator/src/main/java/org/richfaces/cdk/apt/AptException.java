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
package org.richfaces.cdk.apt;

import org.richfaces.cdk.CdkException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class AptException extends CdkException {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private static final long serialVersionUID = 8023042422371321042L;

    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public AptException() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     */
    public AptException(String message) {
        super(message);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cause
     */
    public AptException(Throwable cause) {
        super(cause);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     * @param cause
     */
    public AptException(String message, Throwable cause) {
        super(message, cause);
    }
}
