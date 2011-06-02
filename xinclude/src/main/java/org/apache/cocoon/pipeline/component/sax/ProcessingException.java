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
package org.apache.cocoon.pipeline.component.sax;

import org.xml.sax.SAXException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@SuppressWarnings("serial")
public class ProcessingException extends SAXException {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    public ProcessingException() {

        // TODO Auto-generated constructor stub
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param cause
     */
    public ProcessingException(Exception cause) {
        super(cause);

        // TODO Auto-generated constructor stub
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     */
    public ProcessingException(String message) {
        super(message);

        // TODO Auto-generated constructor stub
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param message
     * @param cause
     */
    public ProcessingException(String message, Exception cause) {
        super(message, cause);

        // TODO Auto-generated constructor stub
    }
}
