/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.richfaces.cdk.templatecompiler;

import java.util.Map;

import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.CdkFragmentElement;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * Stores fragments for the context of given {@link RendererClassGenerator}.
 *
 * @author Lukas Fryc
 */
public class FragmentStore {

    Map<String, Fragment> store = Maps.newHashMap();

    @Inject
    TypesFactory typesFactory;

    public Fragment getFragment(String fragmentName) {
        return store.get(fragmentName);
    }

    public Fragment addFragment(CdkFragmentElement fragmentElement) {
        String fragmentName = fragmentElement.getName();

        if (store.containsKey(fragmentName)) {
            throw new IllegalStateException("Fragment " + fragmentName + " is already defined.");
        }

        Fragment fragment = new Fragment(fragmentElement, typesFactory);
        store.put(fragmentName, fragment);

        return fragment;
    }
}
