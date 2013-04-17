/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.model;

import java.io.Serializable;

/**
 * @author akolonitsky
 * @since Jan 22, 2010
 */
public class DescriptionGroupBase implements DescriptionGroup, Extensible<ConfigExtension>, Serializable {
    /**
     * <p class="changed_added_4_0">
     * Long description for documentation
     * </p>
     */
    private String description;
    /**
     * <p class="changed_added_4_0">
     * Short name for IDE tools
     * </p>
     */
    private String displayname;
    /**
     * <p class="changed_added_4_0">
     * Description name for IDE tools
     * </p>
     */
    private Icon icon;
    private ConfigExtension extension;

    @Merge
    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    @Merge
    public final String getDisplayName() {
        return displayname;
    }

    public final void setDisplayName(String displayname) {
        this.displayname = displayname;
    }

    @Merge
    public final Icon getIcon() {
        return icon;
    }

    public final void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the extension
     */
    @Merge
    public final ConfigExtension getExtension() {
        return extension;
    }

    public final void setExtension(ConfigExtension extension) {
        this.extension = extension;
    }
}
