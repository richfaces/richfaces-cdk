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

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.FacesId;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
@XmlType(name = "faces-config-client-behavior-rendererType", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
public class BehaviorRendererBean {
    private ClassName targetClass;
    private FacesId id;

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the id
     */
    @XmlElement(name = "client-behavior-renderer-type", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    public FacesId getId() {
        return id;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param id the id to set
     */
    public void setId(FacesId type) {
        this.id = type;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the targetClass
     */
    @XmlElement(name = "client-behavior-renderer-class", namespace = ComponentLibrary.FACES_CONFIG_NAMESPACE)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    public ClassName getTargetClass() {
        return targetClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param targetClass the targetClass to set
     */
    public void setTargetClass(ClassName rendererClass) {
        this.targetClass = rendererClass;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @author asmirnov@exadel.com
     */
    public static class BehaviorRendererExtension extends ConfigExtension {
        private ClassName baseClass;

        /**
         * <p class="changed_added_4_0"></p>
         * @return the baseClass
         */
        public ClassName getBaseClass() {
            return this.baseClass;
        }

        /**
         * <p class="changed_added_4_0"></p>
         * @param baseClass the baseClass to set
         */
        public void setBaseClass(ClassName baseClass) {
            this.baseClass = baseClass;
        }

    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the extension
     */

//  @XmlElement(name="client-behavior-renderer-extension",namespace=ComponentLibrary.FACES_CONFIG_NAMESPACE)
//  public BehaviorRendererExtension getExtension() {
//      return super.getExtension();
//  }
}
