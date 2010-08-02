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

package org.richfaces.cdk.xmlconfig.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConfigExtension;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(name = "faces-config-facetType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE,
    propOrder = {"name", "extension"})
@XmlJavaTypeAdapter(FacetAdapter.class)
public class FacetBean extends ExtensibleBean<FacetBean.FacetExtension> {
    
    
    private String name;

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the name
     */
    @XmlElement(name = "facet-name", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public String getName() {
        return name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the extension
     */
    @XmlElement(name = "facet-extension", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public FacetExtension getExtension() {
        return super.getExtension();
    }

    @Override
    public void setExtension(FacetExtension extension) {
        super.setExtension(extension);
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @author asmirnov@exadel.com
     */
    public static final class FacetExtension extends ConfigExtension {
        private Boolean generate;

        /**
         * <p class="changed_added_4_0"></p>
         *
         * @return the generate
         */
        @XmlElement(name = "generate", namespace = ComponentLibrary.CDK_EXTENSIONS_NAMESPACE)
        public Boolean getGenerate() {
            return generate;
        }

        /**
         * <p class="changed_added_4_0"></p>
         *
         * @param generate the generate to set
         */
        public void setGenerate(Boolean generate) {
            this.generate = generate;
        }
    }
}
