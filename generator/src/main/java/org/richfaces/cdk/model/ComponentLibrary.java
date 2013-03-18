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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.richfaces.cdk.util.JavaUtils;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Singleton;

/**
 * <p class="changed_added_4_0">
 * That class contains model of all JSF components asscoiated with that project
 * </p>
 * <p>
 * To keep consistence of library references, only library methods are allowed to components manipulations.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@Singleton
public class ComponentLibrary implements Serializable, Extensible<ConfigExtension>, Trackable, Visitable, Cacheable {
    public static final String CDK_EXTENSIONS_NAMESPACE = "http://jboss.org/schema/richfaces/cdk/extensions";
    public static final String FACES_CONFIG_NAMESPACE = "http://java.sun.com/xml/ns/javaee";
    public static final String FACES_CONFIG_SCHEMA_LOCATION = "http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd";
    public static final String TAGLIB_SCHEMA_LOCATION = "http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd";
    private static final long serialVersionUID = -6055670836731899832L;
    private final ModelCollection<ComponentModel> components = ModelSet.<ComponentModel>create();
    /**
     * <p class="changed_added_4_0">
     * JSF renderer associated with that library
     * </p>
     */
    private final ModelCollection<RenderKitModel> renderKits = ModelSet.<RenderKitModel>create();
    private final ModelCollection<ConverterModel> converters = ModelSet.<ConverterModel>create();
    private final ModelCollection<ValidatorModel> validators = ModelSet.<ValidatorModel>create();
    private final ModelCollection<ListenerModel> listeners = ModelSet.<ListenerModel>create();
    private final ModelCollection<FunctionModel> functions = ModelSet.<FunctionModel>create();
    private long lastModified = Long.MIN_VALUE;
    /**
     * <p class="changed_added_4_0">
     * Application level events fired by the component
     * </p>
     */
    private final ModelCollection<EventModel> events = ModelSet.<EventModel>create();
    private final ModelCollection<BehaviorModel> behaviors = ModelSet.<BehaviorModel>create();
    private ConfigExtension extension = new ConfigExtension();
    private String prefix;
    private Taglib taglib;
    private boolean metadataComplete;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     */
    public ComponentLibrary() {
    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        R result = visitor.visitComponentLibrary(this, data);

        result = renderKits.accept(result, visitor, data);
        result = components.accept(result, visitor, data);
        result = converters.accept(result, visitor, data);
        result = validators.accept(result, visitor, data);
        result = listeners.accept(result, visitor, data);
        result = events.accept(result, visitor, data);
        result = behaviors.accept(result, visitor, data);
        result = functions.accept(result, visitor, data);
        return result;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param renderKitId
     * @return
     */
    public RenderKitModel addRenderKit(String renderKitId) {
        FacesId renderKitType = new FacesId(renderKitId);

        RenderKitModel renderKit = addRenderKit(renderKitType);
        return renderKit;
    }

    public RenderKitModel addRenderKit(FacesId renderKitType) {
        RenderKitModel renderKit = getRenderKit(renderKitType);

        if (null == renderKit) {
            renderKit = new RenderKitModel();
            renderKit.setId(renderKitType);
            renderKits.add(renderKit);
        }
        return renderKit;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the components
     */
    public ModelCollection<ComponentModel> getComponents() {
        return components;
    }

    public ComponentModel getComponentByRenderer(FacesId family, FacesId rendererType) {
        if (family == null) {
            return null;
        }

        for (ComponentModel component : components) {
            if (family.equals(component.getFamily()) && rendererType.equals(component.getRendererType())) {
                return component;
            }
        }
        return null;
    }

    public ComponentModel getComponentByType(FacesId componentType) {

        for (ComponentModel component : components) {
            if (componentType.equals(component.getId())) {
                return component;
            }
        }
        return null;
    }

    public ComponentModel getComponentByFamily(FacesId family) {
        if (family == null) {
            return null;
        }

        for (ComponentModel component : components) {
            if (family.equals(component.getFamily())) {
                return component;
            }
        }
        return null;
    }

    public ComponentModel getComponentByFamily(String componentFamily) {
        return getComponentByFamily(FacesId.parseId(componentFamily));
    }

    public RendererModel getRendererByType(FacesId rendererType) {
        for (RenderKitModel renderKit : getRenderKits()) {
            for (RendererModel renderer : renderKit.getRenderers()) {
                if (rendererType.equals(renderer.getId())) {
                    return renderer;
                }
            }
        }
        return null;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the renderKits
     */
    public ModelCollection<RenderKitModel> getRenderKits() {
        return renderKits;
    }

    public RenderKitModel getRenderKit(FacesId id) {
        if (null != id) {
            for (RenderKitModel renderKit : renderKits) {
                if (id.equals(renderKit.getId())) {
                    return renderKit;
                }
            }
        }
        return null;
    }

    public void addRenderer(FacesId renderKitId, RendererModel rendererModel) {
        RenderKitModel renderKit = addRenderKit(renderKitId);

        renderKit.getRenderers().add(rendererModel);
    }

    public void addRenderer(String renderKitId, RendererModel rendererModel) {
        this.addRenderer(new FacesId(renderKitId), rendererModel);
    }

    public RendererModel getRenderer(String componentFamily, String componentType) {
        if (Strings.isEmpty(componentFamily)) {
            return null;
        }

        List<RendererModel> res = new ArrayList<RendererModel>();
        for (RenderKitModel renderKitModel : renderKits) {
            for (RendererModel rendererModel : renderKitModel.getRenderers()) {
                if (componentFamily.equals(rendererModel.getFamily())) {
                    res.add(rendererModel);
                }
            }
        }

        if (res.size() == 0) {
            return null;
        }

        if (res.size() > 1) {
            for (RendererModel renderer : res) {
                String rendererComponentType = renderer.getComponentType();
                if (componentType.equals(rendererComponentType)) {
                    return renderer;
                }
            }
        }

        return res.get(0);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the converters
     */
    public ModelCollection<ConverterModel> getConverters() {
        return converters;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the validators
     */
    public ModelCollection<ValidatorModel> getValidators() {
        return validators;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the behaviors
     */
    public ModelCollection<BehaviorModel> getBehaviors() {
        return behaviors;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the listeners
     */
    public ModelCollection<ListenerModel> getListeners() {
        return listeners;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the events
     */
    public ModelCollection<EventModel> getEvents() {
        return events;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the functions
     */
    public ModelCollection<FunctionModel> getFunctions() {
        return this.functions;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the extension
     */
    public ConfigExtension getExtension() {
        return extension;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param extension the extension to set
     */
    public void setExtension(ConfigExtension extension) {
        this.extension = extension;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the metadataComplete
     */
    public boolean isMetadataComplete() {
        return this.metadataComplete;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param metadataComplete the metadataComplete to set
     */
    public void setMetadataComplete(boolean metadataComplete) {
        this.metadataComplete = metadataComplete;
    }

    public Boolean getMetadataComplete() {
        return metadataComplete;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Taglib getTaglib() {
        return taglib;
    }

    public void setTaglib(Taglib taglib) {
        this.taglib = taglib;
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    /**
     * <p class="changed_added_4_0">
     * Check that library is empty
     * </p>
     *
     * @param this
     * @return
     */
    public boolean isEmpty() {

        return this.getComponents().isEmpty() && this.getBehaviors().isEmpty() && this.getConverters().isEmpty()
                && this.getEvents().isEmpty() && this.getFunctions().isEmpty() && this.getListeners().isEmpty()
                && this.getRenderKits().isEmpty() && this.getValidators().isEmpty()
                && (null == this.getExtension() || this.getExtension().getExtensions().isEmpty());
    }

    static <T extends ModelElement<T>> void merge(Collection<T> target, Collection<T> source) {
        for (T element : source) {
            T targetElement = null;

            try {
                // TODO - lookup for equivalent element in target collection.
                // find(target, element.getKey());
            } catch (NoSuchElementException e) {
                // TODO: handle exception
            }

            if (null == targetElement) {
                target.add(element);
            } else {
                targetElement.merge(element);
            }
        }
    }

    static <T extends Mergeable<T>> void merge(T target, T source) {
        try {
            PropertyDescriptor[] properties = Introspector.getBeanInfo(target.getClass()).getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : properties) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Method writeMethod = propertyDescriptor.getWriteMethod();

                if (null != readMethod && null != writeMethod && readMethod.isAnnotationPresent(Merge.class)) {
                    boolean preferTrue = readMethod.getAnnotation(Merge.class).preferTrue();
                    boolean overwrite = readMethod.getAnnotation(Merge.class).overwrite();
                    Object oldValue = readMethod.invoke(target);
                    Object newValue = readMethod.invoke(source);

                    if (preferTrue) {
                        if (boolean.class == readMethod.getReturnType()) {
                            if ((Boolean) oldValue || (Boolean) newValue) {
                                writeMethod.invoke(target, Boolean.TRUE);
                                continue;
                            }
                        } else {
                            throw new IllegalStateException("@Merge methods can be enforceTrue=true only for boolean properties: " + readMethod);
                        }
                    }

                    if (null != newValue && (overwrite || null == oldValue)) {
                        writeMethod.invoke(target, newValue);
                    }
                }
            }
        } catch (IntrospectionException e) {

            // TODO Auto-generated catch block
        } catch (IllegalArgumentException e) {

            // TODO Auto-generated catch block
        } catch (IllegalAccessException e) {

            // TODO Auto-generated catch block
        } catch (InvocationTargetException e) {

            // TODO Auto-generated catch block
        }
    }

    public void merge(ComponentLibrary library) {
        this.getComponents().addAll(library.getComponents());
        this.getRenderKits().addAll(library.getRenderKits());
        this.getConverters().addAll(library.getConverters());
        this.getValidators().addAll(library.getValidators());
        this.getBehaviors().addAll(library.getBehaviors());
        this.getFunctions().addAll(library.getFunctions());
        this.getEvents().addAll(library.getEvents());
        if (null != library.getMetadataComplete()) {
            this.setMetadataComplete(library.getMetadataComplete());
        }
        this.getExtension().getExtensions().addAll(library.getExtension().getExtensions());
        if (null != library.getTaglib()) {
            if (null == this.getTaglib()) {
                this.setTaglib(library.getTaglib());
            } else {
                JavaUtils.copyProperties(library.getTaglib(), this.getTaglib());
            }
        }
        if (null != library.getPrefix()) {
            this.setPrefix(library.getPrefix());
        }
    }

    @Override
    public void markUnchanged() {
        for (RenderKitModel renderKit : getRenderKits()) {
            for (RendererModel renderer : renderKit.getRenderers()) {
                renderer.markUnchanged();
            }
        }
        for (ComponentModel component : getComponents()) {
            component.markUnchanged();
        }
    }

    /**
     * This method does not track all changes in library,
     * but only models marked as {@link Cacheable}.
     */
    @Override
    public boolean hasChanged() {
        for (RenderKitModel renderKit : getRenderKits()) {
            for (RendererModel renderer : renderKit.getRenderers()) {
                if (renderer.hasChanged()) {
                    return true;
                }
            }
        }
        for (ComponentModel component : getComponents()) {
            if (component.hasChanged()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void stopTrackingChanges() {
        for (RenderKitModel renderKit : getRenderKits()) {
            for (RendererModel renderer : renderKit.getRenderers()) {
                renderer.stopTrackingChanges();
            }
        }
        for (ComponentModel component : getComponents()) {
            component.stopTrackingChanges();
        }
    }
}
