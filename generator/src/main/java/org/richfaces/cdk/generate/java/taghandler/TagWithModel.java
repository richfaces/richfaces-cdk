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
package org.richfaces.cdk.generate.java.taghandler;

import org.richfaces.cdk.model.TagModel;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class TagWithModel<M> {
    private final TagModel tag;
    private final M model;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param tag
     * @param model
     */
    public TagWithModel(TagModel tag, M model) {
        this.tag = tag;
        this.model = model;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the tag
     */
    public TagModel getTag() {
        return this.tag;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the model
     */
    public M getModel() {
        return this.model;
    }
}
