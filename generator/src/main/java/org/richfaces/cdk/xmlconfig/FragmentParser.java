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
package org.richfaces.cdk.xmlconfig;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.xmlconfig.model.ComponentAdapter;
import org.richfaces.cdk.xmlconfig.model.Fragment;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * That class parses xml document with fragment of faces-config ( eg, standard component attributes )
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class FragmentParser {
    private ComponentAdapter adapter;
    private final JAXB binding;

    @Inject
    public FragmentParser(JAXB binding) {
        this.binding = binding;
        this.adapter = new ComponentAdapter();
    }

    /**
     * <p class="changed_added_4_0">
     * Parses faces-config.xml fragment with component/renderer properties.
     * </p>
     *
     * @param url
     * @return
     */
    public Collection<PropertyBase> parseProperties(String url) throws CdkException, FileNotFoundException {
        String schemaLocation = ComponentLibrary.FACES_CONFIG_SCHEMA_LOCATION;
        Class<Fragment> bindClass = Fragment.class;
        Fragment unmarshal = binding.unmarshal(url, schemaLocation, bindClass);
        if (null != unmarshal) {
            ComponentModel component = adapter.unmarshal(unmarshal);
            return component.getAttributes();
        } else {
            return Collections.<PropertyBase>emptySet();
        }
    }
}
