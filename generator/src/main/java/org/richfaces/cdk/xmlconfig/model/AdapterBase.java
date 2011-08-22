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
package org.richfaces.cdk.xmlconfig.model;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.model.ConfigExtension;
import org.richfaces.cdk.model.Extensible;
import org.richfaces.cdk.util.JavaUtils;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public abstract class AdapterBase<Bean, Model> extends XmlAdapter<Bean, Model> {
    @Override
    public Bean marshal(Model model) throws CdkException {
        Bean bean = null;
        try {
            bean = createBean(getBeanClass(model), model);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        postMarshal(model, bean);
        return bean;
    }

    /**
     * <p class="changed_added_4_0">
     * This method creates adapter object and copies properties from model object to adapter.
     * </p>
     *
     * @param beanClass adapter class.
     * @param model model object class.
     * @return initialized instance of adapter object.
     *
     * @throws org.richfaces.cdk.CdkException
     */
    @SuppressWarnings("unchecked")
    public Bean createBean(Class<? extends Bean> beanClass, Model model) throws CdkException {
        try {
            Bean bean = beanClass.newInstance();

            // Copy properties from model to bean.
            JavaUtils.copyProperties(model, bean);
            if (model instanceof Extensible && bean instanceof Extensible) {
                copyExtensions((Extensible) model, (Extensible) bean, true);
            }

            return bean;
        } catch (InstantiationException e) {
            throw new CdkException("JAXB adapter class instantiation error", e);
        } catch (IllegalAccessException e) {
            throw new CdkException("JAXB adapter class instantiation error", e);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Template method to copy non bean attributes.
     * </p>
     *
     * @param model
     * @param bean
     */
    protected void postMarshal(Model model, Bean bean) {
        // template method to perform additional conversations
    }

    /**
     * <p class="changed_added_4_0">
     * Returns concrete bean class.
     * </p>
     *
     * @param model
     * @return
     */
    protected abstract Class<? extends Bean> getBeanClass(Model model);

    @Override
    public Model unmarshal(Bean bean) throws CdkException {
        Model model = createModelElement(getModelClass(bean), bean);
        postUnmarshal(bean, model);
        return model;
    }

    @SuppressWarnings("unchecked")
    protected <D, E extends ConfigExtension> E createExtension(D destination) throws NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        Method method = destination.getClass().getMethod("getExtension");
        return ((Class<E>) method.getReturnType()).newInstance();
    }

    private void copyExtensions(Extensible<ConfigExtension> source, Extensible<ConfigExtension> destination, Boolean fromModel) {
        try {
            ConfigExtension sourceExtension = source.getExtension();

            if (null != sourceExtension) {
                ConfigExtension destinationExtension = createExtension(destination);

                destinationExtension.setExtensions(sourceExtension.getExtensions());

                if (fromModel) {
                    JavaUtils.copyProperties(source, destinationExtension);
                } else {
                    JavaUtils.copyProperties(sourceExtension, destination);
                }

                destination.setExtension(destinationExtension);
            } else if (fromModel) {
                ConfigExtension destinationExtension = createExtension(destination);

                JavaUtils.copyProperties(source, destinationExtension);
                destination.setExtension(destinationExtension);
            }
        } catch (Exception e) {
            throw new CdkException("Properties copiing error", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected Model createModelElement(Class<? extends Model> modelClass, Bean adapter) {
        try {

            Model modelBean = modelClass.newInstance();

            JavaUtils.copyProperties(adapter, modelBean);

            if (adapter instanceof Extensible && modelBean instanceof Extensible) {
                copyExtensions((Extensible) adapter, (Extensible) modelBean, false);
            }

            return modelBean;
        } catch (Exception e) {
            throw new CdkException("CDK model class instantiation error", e);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Template method to copy non bean attributes
     * </p>
     *
     * @param bean
     * @param model
     */
    protected void postUnmarshal(Bean bean, Model model) {
        // template method to perform additional conversations
    }

    /**
     * <p class="changed_added_4_0">
     * Returns concrete model class
     * </p>
     *
     * @param bean
     * @return
     */
    protected abstract Class<? extends Model> getModelClass(Bean bean);
}
