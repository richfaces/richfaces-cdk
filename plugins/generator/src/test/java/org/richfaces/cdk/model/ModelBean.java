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

package org.richfaces.cdk.model;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class ModelBean implements ModelElement<ModelBean> {

    public static final class Type extends FacesId {
        private static final long serialVersionUID = 6227413669285563473L;

        public Type(String type) {
            super(type);
        }
    }
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private static final long serialVersionUID = -4853397197172488116L;
    String readOnly;

    String writeOnly;
    private String doNotReplace;
    private Object result;
    private Type type;

    private boolean vizited = false;

    public ModelBean() {
    }

    public ModelBean(String name) {
        this.type = new Type(name);
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        vizited = true;

        return visitor.visit(this, data);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the doNotReplace
     */
    @Merge(false)
    public String getDoNotReplace() {
        return doNotReplace;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the readOnly
     */
    @Merge
    public String getReadOnly() {
        return readOnly;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the result
     */
    @Merge
    public Object getResult() {
        return result;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the vizited
     */
    @Merge
    public boolean isVizited() {
        return vizited;
    }

    @Override
    public void merge(ModelBean other) {
        ComponentLibrary.merge(this, other);
    }

    @Override
    public boolean same(ModelBean other) {
        return null != getType() && getType().equals(other.getType());
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param doNotReplace
     *            the doNotReplace to set
     */
    public void setDoNotReplace(String doNotReplace) {
        this.doNotReplace = doNotReplace;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param result
     *            the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param type
     *            the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param vizited
     *            the vizited to set
     */
    public void setVizited(boolean vizited) {
        this.vizited = vizited;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param writeOnly
     *            the writeOnly to set
     */
    public void setWriteOnly(String writeOnly) {
        this.writeOnly = writeOnly;
    }
}
