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
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.el.ValueBinding;

/**
 * 
 **/
@Generated({"RichFaces CDK", "4.0.0-SNAPSHOT"})
public class GeneratedBehavior extends Object implements Behavior, PartialStateHolder {

    @Override
    public void broadcast(BehaviorEvent event) {
    }

    
    private Object testValue;
    

    private Boolean testFlag;
    

    
    public void setTestValue(Object testValue) {
        this.testValue = testValue;
    }

    public Object getTestValue() {
        if (testValue != null) {
            return testValue;
        }

        ValueBinding vb = getValueBinding("testValue");
        if (null != vb) {
            return (Object) vb.getValue(getFacesContext());
        }

        return testValue;
    }    
    public void setTestFlag(Boolean testFlag) {
        this.testFlag = testFlag;
    }

    public Boolean isTestFlag() {
        if (testFlag != null) {
            return testFlag;
        }

        ValueBinding vb = getValueBinding("testFlag");
        if (null != vb) {
            return (Boolean) vb.getValue(getFacesContext());
        }

        return testFlag;
    }
    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private static ValueBinding getValueBinding(String name) {
        return getFacesContext().getApplication().createValueBinding(name);
    }

    // ----------------------------------------------------- StateHolder Methods
    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[1];
                        values[0] = this.getTestValue();
            values[1] = this.isTestFlag();

            return values;
        }
        return null;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }

        Object values[] = (Object[]) state;
        setTestValue((Object) values[0]);
        setTestFlag((Boolean) values[1]);
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