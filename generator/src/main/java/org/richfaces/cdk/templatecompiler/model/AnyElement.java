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

package org.richfaces.cdk.templatecompiler.model;
import static org.richfaces.cdk.templatecompiler.QNameComparator.QNAME_COMPARATOR;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.namespace.QName;

import org.richfaces.cdk.CdkException;

import com.google.common.collect.Maps;

/**
 * <p class="changed_added_4_0"></p>
 *
 * @author asmirnov@exadel.com
 */
public class AnyElement extends ModelFragment {

    private QName name;
    private String passThrough;
    private String passThroughWithExclusions;

    private Map<QName, Object> attributes = Maps.newTreeMap(QNAME_COMPARATOR);

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the name
     */
    public QName getName() {
        return this.name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param name the name to set
     */
    public void setName(QName name) {
        this.name = name;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the passThrough
     */
    @XmlAttribute(namespace=Template.CDK_NAMESPACE)
    public String getPassThrough() {
        return this.passThrough;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param passThrough the passThrough to set
     */
    public void setPassThrough(String passThrough) {
        this.passThrough = passThrough;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the passThroughWithExclusions
     */
    @XmlAttribute(namespace=Template.CDK_NAMESPACE)
    public String getPassThroughWithExclusions() {
        return this.passThroughWithExclusions;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @param passThroughWithExclusions the passThroughWithExclusions to set
     */
    public void setPassThroughWithExclusions(String passThroughWithExclusions) {
        this.passThroughWithExclusions = passThroughWithExclusions;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @return the attributes
     */
    @XmlAnyAttribute
    public Map<QName, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * <p class="changed_added_4_0"></p>
     *
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<QName, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void beforeVisit(TemplateVisitor visitor) throws CdkException {
        visitor.startElement(this);
    }

    @Override
    public void afterVisit(TemplateVisitor visitor) throws CdkException {
        visitor.endElement(this);
    }
}
