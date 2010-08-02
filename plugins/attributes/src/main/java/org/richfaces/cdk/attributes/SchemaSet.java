/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.attributes;

import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Nick Belaevski
 * 
 */
@XmlRootElement(name = "schema-set")
@XmlSeeAlso({
    Schema.class,
    Element.class,
    Attribute.class
})
public class SchemaSet {

    private Map<String, Schema> schemas = new TreeMap<String, Schema>();
    
    public SchemaSet() {
        super();
    }
    
    public void addSchema(Schema schema) {
        schemas.put(schema.getNamespace(), schema);
    }
    
    /**
     * @return the schemas
     */
    @XmlJavaTypeAdapter(Adapters.SchemaAdapter.class)
    public Map<String, Schema> getSchemas() {
        return schemas;
    }
    
    /**
     * @param schemas the schemas to set
     */
    public void setSchemas(Map<String, Schema> schemas) {
        this.schemas = schemas;
    }
    
}
