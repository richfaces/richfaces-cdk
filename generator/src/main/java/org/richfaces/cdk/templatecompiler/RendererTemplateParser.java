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
package org.richfaces.cdk.templatecompiler;

import static org.richfaces.cdk.apt.CacheType.NON_JAVA_SOURCES;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.richfaces.cdk.Cache;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.ModelBuilder;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.apt.LibraryCache;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.EventName;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.MethodSignature;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.PropertyModel;
import org.richfaces.cdk.model.RenderKitModel;
import org.richfaces.cdk.model.RendererModel;
import org.richfaces.cdk.model.SimpleVisitor;
import org.richfaces.cdk.templatecompiler.model.Attribute;
import org.richfaces.cdk.templatecompiler.model.ClientBehavior;
import org.richfaces.cdk.templatecompiler.model.CompositeInterface;
import org.richfaces.cdk.templatecompiler.model.ImportAttributes;
import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.util.Strings;
import org.richfaces.cdk.xmlconfig.FragmentParser;
import org.richfaces.cdk.xmlconfig.JAXB;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class RendererTemplateParser implements ModelBuilder {
    private static final Pattern PARAMETERS_STRING_PATTERN = Pattern.compile("^(\\S+)\\s+(\\S+)\\s*\\(([^\\)]*)\\)$",
            Pattern.COMMENTS);
    private static final Pattern COMMA_SEPARATED_PATTERN = Pattern.compile("\\s*,\\s*", Pattern.COMMENTS);
    private ComponentLibrary library;
    private JAXB jaxbBinding;
    private Logger log;
    private FileManager sources;
    private FragmentParser fragmentParser;

    @Inject
    @Cache(NON_JAVA_SOURCES)
    public LibraryCache nonJavaCache;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param library
     * @param jaxbBinding
     * @param log
     * @param sources
     */
    @Inject
    public RendererTemplateParser(ComponentLibrary library, JAXB jaxbBinding, Logger log,
            @Source(Sources.RENDERER_TEMPLATES) FileManager sources, FragmentParser fragmentParser) {
        this.library = library;
        this.jaxbBinding = jaxbBinding;
        this.log = log;
        this.sources = sources;
        this.fragmentParser = fragmentParser;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.ModelBuilder#build()
     */
    @Override
    public void build() throws CdkException {
        Iterable<File> sourceFiles = this.sources.getFiles();
        for (File file : sourceFiles) {
            if (nonJavaCache.storedBefore(file.lastModified())) {
                build(file);
            }
        }
    }

    public void build(final File file) throws CdkException {
        log.debug("RendererTemplateParser.build");
        final String absolutePath = file.getAbsolutePath();
        log.debug("  - file = " + absolutePath);
        try {
            RendererModel existedModel = library.accept(new SimpleVisitor<RendererModel, String>() {
                @Override
                public RendererModel visitRender(RendererModel model, String absolutePath) {
                    Template template = model.getTemplate();
                    if (null != template) {
                        if (absolutePath.equals(template.getTemplatePath())) {
                            throw new AlreadyProcessedException();
                        }
                    } else if (null != model.getTemplatePath()) {
                        try {
                            if (file.equals(sources.getFile(model.getTemplatePath()))) {
                                return model;
                            }
                        } catch (FileNotFoundException e) {
                            throw new CdkException("Template file defined in Renderer not found: " + model.getTemplatePath(), e);
                        }
                    }
                    return null;
                }
            }, absolutePath);
            Template template = parseTemplate(file);
            mergeTemplateIntoModel(template, existedModel);
        } catch (AlreadyProcessedException e) {
            log.warn("Template " + absolutePath + "was already processed");
        }
    }

    protected RendererModel mergeTemplateIntoModel(Template template, RendererModel renderer) throws CdkException {
        CompositeInterface compositeInterface = template.getInterface();

        if (renderer == null) {
            renderer = new RendererModel();
            RenderKitModel renderKit = library.addRenderKit(compositeInterface.getRenderKitId());
            renderKit.getRenderers().add(renderer);
        }
        renderer.setTemplate(template);
        setRendererType(compositeInterface, renderer);
        setFamily(compositeInterface, renderer);
        setRendererClass(compositeInterface, renderer);
        setRendererBaseClass(compositeInterface, renderer);

        Boolean rendersChildren = compositeInterface.getRendersChildren();
        if (rendersChildren != null) {
            renderer.setRendersChildren(rendersChildren);
        }

        List<ImportAttributes> attributesImports = compositeInterface.getAttributesImports();
        for (ImportAttributes attributesImport : attributesImports) {
            String importURI = attributesImport.getSource();
            Collection<PropertyBase> properties;
            try {
                properties = fragmentParser.parseProperties(importURI);
                if (properties != null) {
                    renderer.getAttributes().addAll(properties);
                }
            } catch (FileNotFoundException e) {
                throw new CdkException("File for import not found", e);
            }
        }

        List<Attribute> templateAttributes = compositeInterface.getAttributes();
        for (Attribute templateAttribute : templateAttributes) {
            renderer.getAttributes().add(buildProperty(templateAttribute));
        }
        return renderer;
    }

    private PropertyModel buildProperty(Attribute templateAttribute) {
        PropertyModel rendererProperty = new PropertyModel();
        rendererProperty.setName(templateAttribute.getName());
        rendererProperty.setDefaultValue(templateAttribute.getDefaultValue());

        rendererProperty.setDescription(templateAttribute.getShortDescription());
        rendererProperty.setDisplayName(templateAttribute.getDisplayName());

        Set<EventName> eventNamesSet = convertBehaviorsToEvents(templateAttribute.getClientBehaviors());
        rendererProperty.getEventNames().addAll(eventNamesSet);

        Boolean required = templateAttribute.getRequired();
        if (null != required) {
            rendererProperty.setRequired(required);
        }
        MethodSignature parsedSignature = parseSignature(templateAttribute.getMethodSignature());
        rendererProperty.setSignature(parsedSignature);

        rendererProperty.setType(templateAttribute.getType());
        return rendererProperty;
    }

    private Set<EventName> convertBehaviorsToEvents(Collection<ClientBehavior> clientBehaviors) {
        Set<EventName> result = Sets.newLinkedHashSet();
        for (ClientBehavior clientBehavior : clientBehaviors) {
            EventName eventName = new EventName();
            eventName.setName(clientBehavior.getEvent());
            eventName.setDefaultEvent(clientBehavior.isDefaultEvent());
            result.add(eventName);
        }

        return result;
    }

    private MethodSignature parseSignature(String signatureString) {

        List<ClassName> parameters = Lists.newArrayList();
        MethodSignature signature = null;
        if (!Strings.isEmpty(signatureString)) {
            Matcher parametersStringMatcher = PARAMETERS_STRING_PATTERN.matcher(signatureString);
            if (!parametersStringMatcher.find()) {
                throw new IllegalArgumentException(MessageFormat.format("Signature string {0} cannot be parsed!",
                        signatureString));
            }
            signature = new MethodSignature();
            signature.setReturnType(ClassName.parseName(parametersStringMatcher.group(1)));
            String parametersString = parametersStringMatcher.group(3);
            if (parametersString.trim().length() != 0) {
                String[] parameterStrings = COMMA_SEPARATED_PATTERN.split(parametersString);
                for (String parameter : parameterStrings) {
                    String trimmedParameter = parameter.trim();
                    parameters.add(ClassName.parseName(trimmedParameter));
                }
                signature.setParameters(parameters);
            }
        }
        return signature;
    }

    private void setRendererClass(CompositeInterface compositeInterface, RendererModel renderer) {
        ClassName javaClass = compositeInterface.getJavaClass();
        if (null != javaClass) {
            renderer.setRendererClass(javaClass);
        }
    }

    private void setRendererBaseClass(CompositeInterface compositeInterface, RendererModel renderer) {
        ClassName baseClass = compositeInterface.getBaseClass();
        if (null != baseClass) {
            renderer.setBaseClass(baseClass);
        }
    }

    private void setFamily(CompositeInterface compositeInterface, RendererModel renderer) {
        FacesId componentFamily = compositeInterface.getComponentFamily();
        if (null != componentFamily) {
            renderer.setFamily(componentFamily);
        }
    }

    private void setRendererType(CompositeInterface compositeInterface, RendererModel renderer) {
        FacesId rendererType = compositeInterface.getRendererType();
        if (null != rendererType) {
            renderer.setId(rendererType);
        }
    }

    protected Template parseTemplate(File file) throws CdkException {
        try {
            Template template = jaxbBinding.unmarshal(file, "http://jboss.org/schema/richfaces/cdk/cdk-template.xsd",
                    Template.class);
            template.setTemplatePath(file.getAbsolutePath());
            return template;
        } catch (FileNotFoundException e) {
            throw new CdkException("Template file not found " + file.getAbsolutePath(), e);
        }
    }
}
