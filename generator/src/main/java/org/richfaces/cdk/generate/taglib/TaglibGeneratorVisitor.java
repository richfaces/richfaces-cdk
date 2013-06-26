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
package org.richfaces.cdk.generate.taglib;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.model.BeanModelBase;
import org.richfaces.cdk.model.BehaviorModel;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentModel;
import org.richfaces.cdk.model.ConverterModel;
import org.richfaces.cdk.model.DescriptionGroup;
import org.richfaces.cdk.model.EventModel;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.FunctionModel;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.SimpleVisitor;
import org.richfaces.cdk.model.TagModel;
import org.richfaces.cdk.model.ValidatorModel;
import org.richfaces.cdk.util.Strings;

/**
 * @author akolonitsky
 * @since Feb 3, 2010
 */
public class TaglibGeneratorVisitor extends SimpleVisitor<Boolean, ComponentLibrary> {
    private static final String COMPONENT = "component";
    private static final String COMPONENT_TYPE = "component-type";
    private static final String RENDERER_TYPE = "renderer-type";
    private static final String HANDLER_CLASS = "handler-class";
    /**
     * <p class="changed_added_4_0">
     * Generated document. IDEA - set document as visitor patameter, to reuse this object instance.
     * </p>
     */
    private Document document = DocumentHelper.createDocument();
    /**
     * <p class="changed_added_4_0">
     * faces-config element in document.
     * </p>
     */
    private Element faceletTaglib;
    private boolean empty = true;

    public TaglibGeneratorVisitor() {
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the empty
     */
    public boolean isEmpty() {
        return this.empty;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public Boolean visitComponentLibrary(ComponentLibrary model, ComponentLibrary componentLibrary) {

        faceletTaglib = document.addElement("facelet-taglib", "http://java.sun.com/xml/ns/javaee");
        faceletTaglib.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        faceletTaglib.addAttribute("xsi:schemaLocation",
                "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd");

        faceletTaglib.addAttribute("version", "2.0");
        faceletTaglib.addAttribute("id", model.getTaglib().getShortName());

        faceletTaglib.addElement("namespace").addText(model.getTaglib().getUri());
        return null;
    }

    @Override
    public Boolean visitComponent(ComponentModel model, ComponentLibrary componentLibrary) {
        for (TagModel tagModel : model.getTags()) {
            if (isFaceletsTag(tagModel)) {
                Element tag = createTag(tagModel.getName());
                // TODO - investigate proper usage of the <handler-class> element.
                // Most libraries use <handler-class> INSTEAD of <component>
                Element component = tag.addElement(COMPONENT);
                addDescription(tag, model);
                addDescription(component, model);
                component.addElement(COMPONENT_TYPE).addText(model.getId().getType());
                FacesId rendererType = model.getRendererType();
                // RendererModel renderer = componentLibrary.getRenderer(model.getFamily(), model.getId().getType());
                if (null != rendererType) {
                    component.addElement(RENDERER_TYPE).addText(rendererType.toString());
                }
                addTagHandler(component, tagModel);
                appendAttributes(tag, model);
                empty = false;
            }
        }
        return null;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param parent DOM element for which &lt;handler-class&gt; element should be appended.
     * @param tag model.
     * @return true is explicit handler class definition was generated.
     */
    private boolean addTagHandler(Element parent, TagModel tag) {
        if (tag != null && tag.getTargetClass() != null) {
            parent.addElement(HANDLER_CLASS).addText(tag.getTargetClass().getName());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean visitConverter(ConverterModel model, ComponentLibrary componentLibrary) {
        for (TagModel tagModel : model.getTags()) {
            if (isFaceletsTag(tagModel)) {
                Element tag = createTag(tagModel.getName());
                Element converter = tag.addElement("converter");
                addDescription(tag, model);
                addDescription(converter, model);
                converter.addElement("converter-id").addText(model.getId().toString());
                addTagHandler(converter, tagModel);
                appendAttributes(tag, model);
                empty = false;
            }
        }
        return null;
    }

    /**
     * This method generates validator tag.
     *
     * <pre>
     *     &lt;tag&gt;
     *        &lt;tag-name>formatValidator&lt;/tag-name&gt;
     *        &lt;validator&gt;
     *            &lt;validator-id&gt;&lt;/validator-id&gt;
     *            &lt;handler-class&gt;&lt;/handler-class&gt;
     *        &lt;/validator&gt;
     *
     *        &lt;attribute&gt;
     *            ...
     *        &lt;/attribute&gt;
     * &lt;/tag&gt;
     *
     * <pre>
     *
     * */
    public Boolean visitValidator(ValidatorModel model, ComponentLibrary componentLibrary) {
        for (TagModel tagModel : model.getTags()) {
            if (isFaceletsTag(tagModel)) {
                Element tag = createTag(tagModel.getName());
                Element validator = tag.addElement("validator");
                addDescription(tag, model);
                addDescription(validator, model);
                validator.addElement("validator-id").addText(model.getId().toString());
                addTagHandler(validator, tagModel);
                appendAttributes(tag, model);
                empty = false;
            }
        }
        return null;
    }

    private void appendAttributes(Element tag, BeanModelBase model) {
        for (PropertyBase entry : model.getAttributes()) {
            if (!entry.isHidden() && !entry.isReadOnly()) {
                createAttributeElement(tag, entry.getName(), entry);
            }
        }
    }

    /**
     * <attribute> <description></description> <name>formatPatterns</name> <required>true</required> <type>String</type>
     * </attribute>
     *
     * @param tag
     * @param name
     * @param attribute
     *
     * @return
     * */
    private Element createAttributeElement(Element tag, String name, PropertyBase attribute) {
        Element attr = tag.addElement("attribute");
        addDescription(attr, attribute);
        attr.addElement("name").addText(name);
        if (attribute.isRequired()) {
            attr.addElement("required").addText("true");
        }
        attr.addElement("type").addText(attribute.getType().getName());
        return attr;
    }

    private Element createTag(String tagName) {
        Element tag = faceletTaglib.addElement("tag");
        tag.addElement("tag-name").addText(tagName);
        empty = false;
        return tag;
    }

    @Override
    public Boolean visitBehavior(BehaviorModel model, ComponentLibrary componentLibrary) {
        for (TagModel tagModel : model.getTags()) {
            if (isFaceletsTag(tagModel)) {
                Element tag = createTag(tagModel.getName());
                Element behavior = tag.addElement("behavior");
                addDescription(tag, model);
                addDescription(behavior, model);
                behavior.addElement("behavior-id").addText(model.getId().toString());
                addTagHandler(behavior, tagModel);
                appendAttributes(tag, model);
                empty = false;
            }
        }
        return null;
    }

    @Override
    public Boolean visitFunction(FunctionModel model, ComponentLibrary componentLibrary) {
        if (isFaceletsTag(model.getType())) {
            Element functionElement = faceletTaglib.addElement("function");
            addDescription(functionElement, model);
            functionElement.addElement("function-name").addText(model.getName());
            functionElement.addElement("function-class").addText(model.getFunctionClass().getName());
            functionElement.addElement("function-signature").addText(model.getSignature());
            empty = false;
        }
        return null;
    }

    @Override
    public Boolean visitEvent(EventModel model, ComponentLibrary param) {
        for (TagModel tagModel : model.getTags()) {
            if (isFaceletsTag(tagModel)) {
                Element tag = createTag(tagModel.getName());
                addDescriptionForListener(tag, model);
                addTagHandler(tag, tagModel);
                appendAttributesForListener(tag, model);
            }
        }
        return null;
    }

    private void addDescriptionForListener(Element tag, EventModel model) {
        Element desc = tag.addElement("description");
        String description = model.getDescription() == null ? "" : model.getDescription();
        desc.setText(description);
    }

    private void appendAttributesForListener(Element tag, EventModel model) {
        for (ListenerAttribute attribute : ListenerAttribute.values()) {
            PropertyBase property = attribute.derivateProperty(model);
            createAttributeElement(tag, property.getName(), property);
        }
    }

    private boolean isFaceletsTag(TagType type) {

        return TagType.Facelets.equals(type) || TagType.All.equals(type);
    }

    private boolean isFaceletsTag(TagModel tagModel) {
        return isFaceletsTag(tagModel.getType());
    }

    /**
     * <p class="changed_added_4_0">
     * Add common description elements.
     * </p>
     *
     * @param parent
     * @param model
     */
    private void addDescription(Element parent, DescriptionGroup model) {
        if (!Strings.isEmpty(model.getDescription())) {
            parent.addElement("description").addText(model.getDescription());
        }
        if (!Strings.isEmpty(model.getDisplayName())) {
            parent.addElement("display-name").addText(model.getDisplayName());
        }
        if (null != model.getIcon()) {
            Element iconElement = parent.addElement("icon");
            if (!Strings.isEmpty(model.getIcon().getSmallIcon())) {
                iconElement.addElement("small-icon").addText(model.getIcon().getSmallIcon());
            }
            if (!Strings.isEmpty(model.getIcon().getLargeIcon())) {
                iconElement.addElement("large-icon").addText(model.getIcon().getLargeIcon());
            }
        }
    }
}
