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

package org.richfaces.cdk.model.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.ConverterHandler;
import javax.xml.validation.ValidatorHandler;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.ModelValidator;
import org.richfaces.cdk.NamingConventions;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.DescriptionGroup;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.FacesComponent;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FacetModel;
import org.richfaces.cdk.model.InvalidNameException;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.model.TagModel;
import org.richfaces.cdk.model.Taglib;
import org.richfaces.cdk.util.Strings;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class ValidatorImpl implements ModelValidator {

    public static final ClassName DEFAULT_COMPONENT_HANDLER = new ClassName(ComponentHandler.class);
    public static final ClassName DEFAULT_VALIDATOR_HANDLER = new ClassName(ValidatorHandler.class);
    public static final ClassName DEFAULT_CONVERTER_HANDLER = new ClassName(ConverterHandler.class);
    public static final ClassName DEFAULT_BEHAVIOR_HANDLER = new ClassName(BehaviorHandler.class);

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @author asmirnov@exadel.com
     * 
     */
    interface NamingConventionsCallback {

        FacesId inferType(ClassName targetClass);

        ClassName inferClass(FacesId id);

    }

    @Inject
    private Logger log;

    private final NamingConventions namingConventions;

    private final SourceUtils sourceUtils;

    @Inject
    public ValidatorImpl(NamingConventions namingConventions, SourceUtils sourceUtils) {
        this.namingConventions = namingConventions;
        this.sourceUtils = sourceUtils;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.ValidatorModel#verify(org.richfaces.cdk.model.ComponentLibrary)
     */
    @Override
    public void verify(ComponentLibrary library) throws CdkException {
        verifyComponents(library);
        verifyEvents(library);
        verifyBehaviors(library);
        verifyRenderers(library);
        verifyTaglib(library);
    }

    protected void verifyEvents(ComponentLibrary library) {
        for (EventModel event : library.getEvents()) {
            ClassName listenerInterface = event.getListenerInterface();
            if (null == listenerInterface) {
                // TODO - infer listener interface name.
            }
            event.setGenerateListener(null == sourceUtils.asTypeElement(listenerInterface));
            String methodName = event.getListenerMethod();
            if (null == methodName) {
                // TODO infer listener method name.
                methodName="process";
                event.setListenerMethod(methodName);
            }
            ClassName sourceInterface = event.getSourceInterface();
            if (null == sourceInterface) {
                // TODO - infer source interface.
            }
            event.setGenerateSource(null == sourceUtils.asTypeElement(sourceInterface));
            // Propagate event to corresponding components.
            for (ComponentModel component : library.getComponents()) {
                for (EventModel componentEvent : component.getEvents()) {
                    if (event.getType().equals(componentEvent.getType())) {
                        componentEvent.merge(event);
                    }
                }
            }
        }
    }

    protected void verifyTaglib(ComponentLibrary library) {
        Taglib taglib = library.getTaglib();
        if (null == taglib) {
            // Oops, create taglib model
            taglib = new Taglib();
            library.setTaglib(taglib);
        }
        // Verify URI
        String uri = taglib.getUri();
        if (null == uri) {
            // infer default value.
            uri = namingConventions.inferTaglibUri(library);
            taglib.setUri(uri);
            // log.error("No uri defined for taglib");
        }
        String shortName = taglib.getShortName();
        if (null == shortName) {
            shortName = namingConventions.inferTaglibName(uri);
            taglib.setShortName(shortName);
            // log.error("No short defined for taglib");
        }
    }

    /**
     * <p class="changed_added_4_0">
     * Verify all behaviors in the library.
     * </p>
     * 
     * @param library
     */
    protected void verifyBehaviors(ComponentLibrary library) {
        for (BehaviorModel behavior : library.getBehaviors()) {
            verifyTypes(behavior, new NamingConventionsCallback() {

                @Override
                public FacesId inferType(ClassName targetClass) {
                    // TODO Auto-generated method stub
                    return namingConventions.inferBehaviorType(targetClass);
                }

                @Override
                public ClassName inferClass(FacesId id) {
                    // TODO Auto-generated method stub
                    return namingConventions.inferBehaviorClass(id);
                }
            });
            for (TagModel tag : behavior.getTags()) {
                verifyTag(tag, behavior.getId(), DEFAULT_BEHAVIOR_HANDLER);
            }

        }
    }

    protected void verifyRenderers(ComponentLibrary library) {
        for (RenderKitModel renderKit : library.getRenderKits()) {
            // Check render kit name and class.
            for (RendererModel renderer : renderKit.getRenderers()) {

                vefifyRenderer(library, renderer);
            }
        }
    }

    protected void vefifyRenderer(ComponentLibrary library, RendererModel renderer) {

        // Check type.
        // Check family.
        // Check generated class.
        // Check superclass.
        // Check component type.
    }

    protected void verifyComponents(ComponentLibrary library) throws CdkException {
        // Verify types and classes. Do it first to be sure what all all values are set before second stage.
        for (ComponentModel component : library.getComponents()) {
            verifyComponentType(component);
        }
        // Verify component attributes
        HashSet<ComponentModel> verified = Sets.newHashSet();
        for (ComponentModel component : library.getComponents()) {
            verifyComponentAttributes(library, component, verified);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param library
     * @param component
     * @param verified
     */
    protected void verifyComponentAttributes(ComponentLibrary library, final ComponentModel component,
        Collection<ComponentModel> verified) {
        // There is potential StackOverflow, so we process only components which have not been
        // verified before.
        if (!verified.contains(component)) {
            // Propagate attributes from parent component, if any.
            verified.add(component);
            if (null != component.getBaseClass()) {
                try {
                    // Step one, lookup for parent.
                    ComponentModel parentComponent =
                        Iterables.find(library.getComponents(), new Predicate<ComponentModel>() {

                            @Override
                            public boolean apply(ComponentModel input) {
                                return component.getBaseClass().equals(input.getTargetClass());
                            }
                        });
                    // To be sure what all properties for parent component were propagated.
                    verifyComponentAttributes(library, parentComponent, verified);
                    for (PropertyBase parentAttribute : parentComponent.getAttributes()) {
                        PropertyBase attribute = component.getOrCreateAttribute(parentAttribute.getName());
                        attribute.merge(parentAttribute);
                        // TODO Check generate status. Attribute should not be generated if the parent component
                        // represents
                        // concrete class.
                        attribute.setGenerate(false);
                    }
                } catch (NoSuchElementException e) {
                    // No parent component in the library
                }

            } // Check attributes.
            for (PropertyBase attribute : component.getAttributes()) {
                verifyAttribute(attribute, component.isGenerate());
            }
            // compact(component.getAttributes());
            // Check renderers.
            // Check Tag
            for (TagModel tag : component.getTags()) {
                verifyTag(tag, component.getId(), DEFAULT_COMPONENT_HANDLER);
            }
            verifyDescription(component);
            for (FacetModel facet : component.getFacets()) {
                verifyDescription(facet);
            }
        }
    }

    protected void verifyTag(TagModel tag, FacesId id, ClassName handler) {
        if (Strings.isEmpty(tag.getName())) {
            String defaultTagName = namingConventions.inferTagName(id);
            tag.setName(defaultTagName);
        }
        if (tag.isGenerate()) {
            if (null == tag.getBaseClass()) {
                // TODO - choose default handler class by tag type.
                tag.setBaseClass(handler);
            }
            if (null == tag.getTargetClass()) {
                namingConventions.inferTagHandlerClass(id, tag.getType().toString());// TODO - get markup somethere.
            }
        }
        // TODO - copy component description to tag, if it has no description.
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @param component
     * @throws InvalidNameException
     */
    protected void verifyComponentType(ComponentModel component) throws InvalidNameException {
        // Check JsfComponent type.
        if (verifyTypes(component, new NamingConventionsCallback() {

            @Override
            public FacesId inferType(ClassName targetClass) {
                return namingConventions.inferComponentType(targetClass);
            }

            @Override
            public ClassName inferClass(FacesId id) {
                return namingConventions.inferUIComponentClass(id);
            }
        }) && null == component.getFamily()) {
            // Check family.
            component.setFamily(namingConventions.inferUIComponentFamily(component.getId()));
        }
    }

    protected boolean verifyTypes(FacesComponent component, NamingConventionsCallback callback) {
        // Check JsfComponent type.
        if (null == component.getId()) {
            if (null != component.getTargetClass()) {
                component.setId(callback.inferType(component.getTargetClass()));
            } else if (null != component.getBaseClass()) {
                component.setId(callback.inferType(component.getBaseClass()));
            } else {
                // No clue for component type, log error and return.
                log.error("No type information available for component: " + component);
                return false;
            }
        }
        // Check classes.
        if (component.isGenerate()) {
            if (null == component.getBaseClass()) {
                log.error("Base class for generated component is not set :" + component.getId());
                // return;
            } else if (null == component.getTargetClass()) {
                component.setTargetClass(callback.inferClass(component.getId()));
            }
        } else if (null == component.getTargetClass()) {
            if (null != component.getBaseClass()) {
                component.setTargetClass(component.getBaseClass());
            } else {
                log.error("No class information available for component: " + component);
                return false;
            }
        }
        return true;
    }

    protected void verifyAttribute(PropertyBase attribute, boolean generatedComponent) {
        // Check name.
        if (Strings.isEmpty(attribute.getName())) {
            log.error("No name for attribute " + attribute);
            return;
        }
        if (attribute.getName().contains(".") || Character.isDigit(attribute.getName().charAt(0))
            || attribute.getName().contains(" ")) {
            log.error("Invalid attribute name [" + attribute.getName() + "]");
            return;
        }
        // Check type
        if (null == attribute.getType()) {
            log.error("Unknown type of attribute [" + attribute.getName() + "]");
            return;
        }
        // Check binding properties.
        if ("javax.faces.el.MethodBinding".equals(attribute.getType().getName())) {
            attribute.setBinding(true);
            attribute.setBindingAttribute(true);
        } else if ("javax.el.MethodExpression".equals(attribute.getType().getName())) {
            attribute.setBindingAttribute(true);
        }
        //if(attribute.isBindingAttribute() && attribute.getSignature().isEmpty() && !attribute.isHidden()) {
        //    log.error("Signature for method expression attribute "+attribute.getName()+" has not been set");
        //}
        // Check "generate" flag.
        if (generatedComponent) {
            // TODO Attribute should be only generated if it does not exist or abstract in the base class.
            // Step one - check base class
        } else {
            attribute.setGenerate(false);
        }
        verifyDescription(attribute);
    }

    protected void verifyDescription(DescriptionGroup element) {

    }
}
