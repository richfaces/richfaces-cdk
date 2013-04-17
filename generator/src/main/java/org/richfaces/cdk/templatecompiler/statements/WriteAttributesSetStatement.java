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
package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.richfaces.cdk.generate.freemarker.FreeMarkerRenderer;
import org.richfaces.cdk.templatecompiler.TemplateModel;

import com.google.inject.Inject;

/**
 * @author Nick Belaevski
 */
public class WriteAttributesSetStatement extends FreeMarkerTemplateStatementBase {
    /**
     *
     */
    private static final String PASS_THROUGH_ATTRIBUTES_FIELD_NAME = "PASS_THROUGH_ATTRIBUTES";
    private static AtomicInteger fieldCounter = new AtomicInteger(0);
    private String fieldName;
    private Collection<PassThrough> attributes;

    @Inject
    public WriteAttributesSetStatement(@TemplateModel FreeMarkerRenderer renderer) {
        super(renderer, "write-attributes-set");
        fieldName = PASS_THROUGH_ATTRIBUTES_FIELD_NAME + fieldCounter.getAndIncrement();
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setAttributes(Collection<PassThrough> passThroughAttributes) {
        this.attributes = passThroughAttributes;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the attributes
     */
    public Collection<PassThrough> getAttributes() {
        return this.attributes;
    }
}
