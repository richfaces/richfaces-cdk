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
 * <p class="changed_added_4_0">Instance of an Object implemented this interface can be injected into test class to simplify mock objects manipulations.</p>
 * @author asmirnov@exadel.com
 *
 */
public interface MockController {
    
    /**
     * <p class="changed_added_4_0">Repaly all mock objects created by the {@link CdkTestRunner}</p>
     */
    public void replay();
    
    /**
     * <p class="changed_added_4_0">Verify all mock objects created by the {@link CdkTestRunner}</p>
     */
    public void verify();

}
