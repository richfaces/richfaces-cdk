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
import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.richfaces.PartialStateHolderHelper;
import org.richfaces.cdk.generate.freemarker.PropertyModel;

/**
 * 
 **/
@Generated({"RichFaces CDK", "4.0.0-SNAPSHOT"})
public class GeneratedConverter extends Object implements Converter, PartialStateHolder {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return null;
    }

    protected enum Properties {
        myClass,
        testObject,
        testBoolean,
        testInteger,
        visible
    }


    public PropertyModel getMyClass() {
        return (PropertyModel) getStateHelper().eval(Properties.myClass);
    }
    
    public void setMyClass(PropertyModel myClass) {
        getStateHelper().put(Properties.myClass, myClass);
    }

    public Object getTestObject() {
        return getStateHelper().get(Properties.testObject);
    }
    
    public void setTestObject(Object testObject) {
        getStateHelper().put(Properties.testObject, testObject);
    }

    public Boolean isTestBoolean() {
        return Boolean.valueOf(getStateHelper().eval(Properties.testBoolean).toString());
    }
    
    public void setTestBoolean(Boolean testBoolean) {
        getStateHelper().put(Properties.testBoolean, testBoolean);
    }

    public Integer getTestInteger() {
        return (Integer) getStateHelper().eval(Properties.testInteger);
    }
    
    public void setTestInteger(Integer testInteger) {
        getStateHelper().put(Properties.testInteger, testInteger);
    }

    public boolean isVisible() {
        return Boolean.valueOf(getStateHelper().eval(Properties.visible).toString());
    }
    
    public void setVisible(boolean visible) {
        getStateHelper().put(Properties.visible, visible);
    }

    private StateHelper stateHelper = null;
        
    protected StateHelper getStateHelper() {
        if (stateHelper == null) {
            stateHelper = new PartialStateHolderHelper(this);
        }
        return stateHelper;
    }
    // ----------------------------------------------------- StateHolder Methods
    @Override
    public Object saveState(FacesContext context) {
        return stateHelper.saveState(context);
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        stateHelper.restoreState(context, state);
    }

    private boolean transientValue = false;

    @Override
    public boolean isTransient() {
        return this.transientValue;
    }

    @Override
    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }

    private boolean initialState;

    @Override
    public void markInitialState() {
        initialState = true;
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public void clearInitialState() {
        initialState = false;
    }

}