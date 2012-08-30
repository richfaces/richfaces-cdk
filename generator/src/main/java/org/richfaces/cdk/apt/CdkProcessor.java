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

import javax.annotation.processing.Processor;

/**
 * <p class="changed_added_4_0">
 * Base class for all CDK Annotation processors. That class provides access to current CDK context and utility methods for Java
 * source models.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public interface CdkProcessor extends Processor {

    /**
     * This method will be called once all the Java sources are processed
     */
    void continueAfterJavaSourceProcessing();
}
