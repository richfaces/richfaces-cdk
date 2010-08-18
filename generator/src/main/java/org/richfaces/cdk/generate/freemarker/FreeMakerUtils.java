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

package org.richfaces.cdk.generate.freemarker;

import java.util.List;

import org.richfaces.cdk.util.Strings;

import com.google.common.collect.ImmutableMap;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <p class="changed_added_4_0">This class implements different utility functions used by generator.</p>
 * @author asmirnov@exadel.com
 *
 */
public class FreeMakerUtils implements TemplateHashModel {

    private static final ImmutableMap<String, ? extends TemplateMethodModel> FUNCTIONS = ImmutableMap.<String, TemplateMethodModel>builder().
        put("version", new TemplateMethodModelEx() {
            @SuppressWarnings("unchecked")
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                return new SimpleScalar("4.0.0");
            }
        }). 
        put("capitaliyze", new TemplateMethodModel() {            
            @SuppressWarnings("unchecked")
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                if (arguments.size() == 1) {
                    return new SimpleScalar(Strings.firstToUpperCase(arguments.get(0).toString()));
                } else {
                    return null;
                }
            }
        }). 
        build();
    /* (non-Javadoc)
     * @see freemarker.template.TemplateHashModel#get(java.lang.String)
     */
    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        return FUNCTIONS.get(key);
    }

    /* (non-Javadoc)
     * @see freemarker.template.TemplateHashModel#isEmpty()
     */
    @Override
    public boolean isEmpty() throws TemplateModelException {
        return false;
    }

}
