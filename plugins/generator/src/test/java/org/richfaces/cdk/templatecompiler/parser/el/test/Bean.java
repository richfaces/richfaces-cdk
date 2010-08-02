/**
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

package org.richfaces.cdk.templatecompiler.parser.el.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

public class Bean {
    private UIComponent[] array = new UIComponent[] { new UIComponent() };
    private List<UIComponent> components;
    private Map<String, UIComponent> facets;
    private Map rawMap;
    private boolean readOnly;
    private Bean2 testBean2;

    public Bean() {
        facets = new HashMap<String, UIComponent>();
        rawMap = new HashMap();

        UIComponent value = new UIComponent();

        facets.put("header", value);
        components = new ArrayList<UIComponent>();
        components.add(value);
    }

    public Integer count(Integer i) {
        return null;
    }

    public UIComponent[] getArray() {
        return array;
    }

    public List<UIComponent> getComponents() {
        return components;
    }

    public UIComponent getFacet(String name) {
        return facets.get(name);
    }

    public Map<String, UIComponent> getFacets() {
        return facets;
    }

    public Map getRawMap() {
        return rawMap;
    }

    public Bean2 getTestBean2() {
        return testBean2;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public FacesContext getContext(){
        return null;
    }
    
    public void setArray(UIComponent[] array) {
        this.array = array;
    }

    public void setComponents(List<UIComponent> components) {
        this.components = components;
    }

    public void setFacets(Map<String, UIComponent> facets) {
        this.facets = facets;
    }

    public void setRawMap(Map rawMap) {
        this.rawMap = rawMap;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setTestBean2(Bean2 testBean2) {
        this.testBean2 = testBean2;
    }

    public void test(UIComponent comp, boolean test) {
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
