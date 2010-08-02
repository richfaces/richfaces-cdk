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
package org.richfaces.cdk.generate.java;

import javax.annotation.Generated;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import org.richfaces.PartialStateHolderHelper;
import javax.faces.component.UIOutput;
import java.util.ArrayList;
import javax.el.MethodExpression;
import javax.faces.el.MethodBinding;

/**
 * 
 **/
@Generated({"RichFaces CDK", "4.0.0-SNAPSHOT"})
public class GeneratedComponent extends UIOutput
    implements ClientBehaviorHolder
    {

    public static final String COMPONENT_TYPE="foo.bar";



    public GeneratedComponent() {
        setRendererType("foo.barRenderer");
    }

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(
        "id",
        "action"

        ));
    
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }


    public String getDefaultEventName() {
        return "action";
    }

    protected static enum Properties {
        testValue,
        testFlag,
        testBinding,
        testExpr,
        listStrings,
        listInteger,
        list

    }


    public Object getTestValue() {
        return getStateHelper().get(Properties.testValue);
    }
    
    public void setTestValue(Object testValue) {
        getStateHelper().put(Properties.testValue, testValue);
    }

    public Boolean isTestFlag() {
        return Boolean.valueOf(getStateHelper().eval(Properties.testFlag).toString());
    }
    
    public void setTestFlag(Boolean testFlag) {
        getStateHelper().put(Properties.testFlag, testFlag);
    }

    public MethodBinding getTestBinding() {
        return (MethodBinding) getStateHelper().get(Properties.testBinding);
    }
    
    public void setTestBinding(MethodBinding testBinding) {
        getStateHelper().put(Properties.testBinding, testBinding);
    }

    public MethodExpression getTestExpr() {
        return (MethodExpression) getStateHelper().get(Properties.testExpr);
    }
    
    public void setTestExpr(MethodExpression testExpr) {
        getStateHelper().put(Properties.testExpr, testExpr);
    }

    public ArrayList getListStrings() {
        return (ArrayList) getStateHelper().eval(Properties.listStrings);
    }
    
    public void setListStrings(ArrayList listStrings) {
        getStateHelper().put(Properties.listStrings, listStrings);
    }

    public ArrayList getListInteger() {
        return (ArrayList) getStateHelper().eval(Properties.listInteger);
    }
    
    public void setListInteger(ArrayList listInteger) {
        getStateHelper().put(Properties.listInteger, listInteger);
    }

    public ArrayList getList() {
        return (ArrayList) getStateHelper().eval(Properties.list);
    }
    
    public void setList(ArrayList list) {
        getStateHelper().put(Properties.list, list);
    }
}