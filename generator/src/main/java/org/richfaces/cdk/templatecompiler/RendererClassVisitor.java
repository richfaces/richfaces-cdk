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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.xml.namespace.QName;

import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.templatecompiler.builder.model.Argument;
import org.richfaces.cdk.templatecompiler.builder.model.JavaAnnotation;
import org.richfaces.cdk.templatecompiler.builder.model.JavaClass;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.builder.model.JavaMethod;
import org.richfaces.cdk.templatecompiler.builder.model.JavaModifier;
import org.richfaces.cdk.templatecompiler.builder.model.JavaStatement;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.AnyElement;
import org.richfaces.cdk.templatecompiler.model.CdkBodyElement;
import org.richfaces.cdk.templatecompiler.model.CdkCallElement;
import org.richfaces.cdk.templatecompiler.model.CdkCaseElement;
import org.richfaces.cdk.templatecompiler.model.CdkChooseElement;
import org.richfaces.cdk.templatecompiler.model.CdkDefaultElement;
import org.richfaces.cdk.templatecompiler.model.CdkForEachElement;
import org.richfaces.cdk.templatecompiler.model.CdkFragmentElement;
import org.richfaces.cdk.templatecompiler.model.CdkIfElement;
import org.richfaces.cdk.templatecompiler.model.CdkObjectElement;
import org.richfaces.cdk.templatecompiler.model.CdkOtherwiseElement;
import org.richfaces.cdk.templatecompiler.model.CdkRenderFragmentElement;
import org.richfaces.cdk.templatecompiler.model.CdkScriptObjectElement;
import org.richfaces.cdk.templatecompiler.model.CdkScriptOptionElement;
import org.richfaces.cdk.templatecompiler.model.CdkSwitchElement;
import org.richfaces.cdk.templatecompiler.model.CdkWhenElement;
import org.richfaces.cdk.templatecompiler.model.ClassImport;
import org.richfaces.cdk.templatecompiler.model.CompositeImplementation;
import org.richfaces.cdk.templatecompiler.model.CompositeInterface;
import org.richfaces.cdk.templatecompiler.model.CompositeRenderFacet;
import org.richfaces.cdk.templatecompiler.model.ResourceDependency;
import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.templatecompiler.model.TemplateVisitor;
import org.richfaces.cdk.templatecompiler.statements.AddAttributesToScriptHashStatement;
import org.richfaces.cdk.templatecompiler.statements.AttributesStatement;
import org.richfaces.cdk.templatecompiler.statements.CaseStatement;
import org.richfaces.cdk.templatecompiler.statements.CastComponentStatement;
import org.richfaces.cdk.templatecompiler.statements.ConstantReturnMethodBodyStatement;
import org.richfaces.cdk.templatecompiler.statements.DefineObjectStatement;
import org.richfaces.cdk.templatecompiler.statements.EncodeMethodPrefaceStatement;
import org.richfaces.cdk.templatecompiler.statements.EndElementStatement;
import org.richfaces.cdk.templatecompiler.statements.ForEachStatement;
import org.richfaces.cdk.templatecompiler.statements.HelperMethod;
import org.richfaces.cdk.templatecompiler.statements.HelperMethodFactory;
import org.richfaces.cdk.templatecompiler.statements.IfElseStatement;
import org.richfaces.cdk.templatecompiler.statements.IfStatement;
import org.richfaces.cdk.templatecompiler.statements.RenderFacetStatement;
import org.richfaces.cdk.templatecompiler.statements.RenderFragmentStatement;
import org.richfaces.cdk.templatecompiler.statements.ScriptObjectStatement;
import org.richfaces.cdk.templatecompiler.statements.ScriptOptionStatement;
import org.richfaces.cdk.templatecompiler.statements.StartElementStatement;
import org.richfaces.cdk.templatecompiler.statements.StatementsContainer;
import org.richfaces.cdk.templatecompiler.statements.SwitchStatement;
import org.richfaces.cdk.templatecompiler.statements.TemplateStatement;
import org.richfaces.cdk.templatecompiler.statements.TemplateStatementImpl;
import org.richfaces.cdk.templatecompiler.statements.WriteTextStatement;
import org.richfaces.cdk.util.Strings;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Injector;

/**
 * <p class="changed_added_4_3">
 * </p>
 *
 * @author asmirnov@exadel.com
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 * @author Lukas Fryc
 */
public class RendererClassVisitor implements TemplateVisitor {
    /**
     *
     */
    // TODO externalize
    public static final String RENDER_KIT_UTILS_CLASS_NAME = "org.richfaces.renderkit.RenderKitUtils";

    public static final String RENDERER_BASE_CLASS_NAME = "org.richfaces.renderkit.RendererBase";
    /**
     *
     */
    public static final String RESPONSE_WRITER_VARIABLE = "responseWriter";
    /**
     *
     */
    public static final String COMPONENT_VARIABLE = "component";
    /**
     *
     */
    public static final String COMPONENT_PARAMETER = "uiComponent";
    /**
     *
     */
    public static final String THIS_VARIABLE = "this";
    /**
     *
     */
    public static final String SUPER_VARIABLE = "super";
    /**
     *
     */
    public static final String FACES_CONTEXT_VARIABLE = "facesContext";
    public static final ImmutableMap<String, Object> ENCODE_METHOD_VARIABLES = ImmutableMap.<String, Object>builder()
            .put("facesContextVariable", RendererClassVisitor.FACES_CONTEXT_VARIABLE)
            .put("componentVariable", RendererClassVisitor.COMPONENT_VARIABLE)
            .put("responseWriterVariable", RendererClassVisitor.RESPONSE_WRITER_VARIABLE)
            .put("clientIdVariable", RendererClassVisitor.CLIENT_ID_VARIABLE).build();
    /**
     *
     */
    static final String CLIENT_ID_VARIABLE = "clientId";
    private final Logger log;
    private final Injector injector;
    private final TypesFactory typesFactory;
    private final HelperMethodFactory helperMethodFactory;
    private final CompositeInterface compositeInterface;
    private final Collection<PropertyBase> attributes;
    private StatementsContainer currentStatement;
    private JavaClass generatedClass;
    private ClassName componentBaseClass;
    private ClassName rendererSuperClass;
    private boolean isExtendingRendererBase = false;
    private Set<HelperMethod> addedHelperMethods = EnumSet.noneOf(HelperMethod.class);
    private CdkClassLoader loader;
    private FragmentStore fragmentStore;

    public RendererClassVisitor(CompositeInterface compositeInterface, Collection<PropertyBase> attributes, Logger log,
            Injector injector, TypesFactory typesFactory, HelperMethodFactory helperFactory, CdkClassLoader loader) {
        this.compositeInterface = compositeInterface;
        this.attributes = attributes;
        this.injector = injector;
        this.typesFactory = typesFactory;
        this.log = log;
        this.helperMethodFactory = helperFactory;
        this.loader = loader;

        this.fragmentStore = new FragmentStore();
        this.injector.injectMembers(this.fragmentStore);
    }

    private void initializeJavaClass() {
        this.generatedClass = new JavaClass(compositeInterface.getJavaClass());
        this.generatedClass.addModifier(JavaModifier.PUBLIC);
        this.rendererSuperClass = compositeInterface.getBaseClass();
        if (null != this.rendererSuperClass) {
            this.generatedClass.setSuperClass(this.rendererSuperClass);
            this.isExtendingRendererBase = isExtendingRendererBase();
        }
        this.componentBaseClass = compositeInterface.getComponentBaseClass();
        if (null == this.componentBaseClass) {
            this.componentBaseClass = new ClassName(UIComponent.class.getName());
        }
        this.generatedClass.addImport(FacesContext.class);
        this.generatedClass.addImport(ResponseWriter.class);
        this.generatedClass.addImport(UIComponent.class);

        for (ClassImport classImport : compositeInterface.getClassImports()) {
            List<String> importedNames = classImport.getNames();
            if (importedNames == null || importedNames.isEmpty()) {
                importedNames = Lists.newArrayList("*");
            }

            for (String importedName : importedNames) {
                this.generatedClass.addImport(Strings.DOT_JOINER.join(classImport.getPackage(), importedName),
                        classImport.isStatic());
            }
        }

        // TODO - make this JavaDoc - Generated annotation is present since JDK6
        // this.generatedClass.addAnnotation(Generated.class, "\"RichFaces CDK\"");
        // TODO remove this after improving Java model
        // this.generatedClass.addImport(Generated.class);

        List<ResourceDependency> resourceDependencies = compositeInterface.getResourceDependencies();
        ELType dependencyType = typesFactory.getType(javax.faces.application.ResourceDependency.class);
        if (1 == resourceDependencies.size()) {
            ResourceDependency resource = resourceDependencies.get(0);
            this.generatedClass.addAnnotation(createResourceAnnotation(dependencyType, resource));
        } else if (resourceDependencies.size() > 1) {
            StringBuilder resources = new StringBuilder("{");
            for (ResourceDependency resource : resourceDependencies) {
                if (resources.length() > 1) {
                    resources.append(',');
                }
                resources.append("@ResourceDependency(");
                resources.append("name=\"").append(resource.getName()).append("\",library=\"").append(resource.getLibrary())
                        .append("\",target=\"").append(resource.getTarget()).append("\"").append(")");
            }
            resources.append("}");
            this.generatedClass.addAnnotation(new JavaAnnotation(typesFactory.getType(ResourceDependencies.class), resources
                    .toString()));
            this.generatedClass.addImport(javax.faces.application.ResourceDependency.class);
        }
        this.createMethodContext();
    }

    private boolean isExtendingRendererBase() {
        try {
            Class<?> rendererSuperType = loader.loadClass(rendererSuperClass.getFullName());
            Class<?> rendererBaseType = loader.loadClass(RENDERER_BASE_CLASS_NAME);
            return rendererBaseType.isAssignableFrom(rendererSuperType);
        } catch (ClassNotFoundException e) {
            log.warn("Could not determine if the renderer-base-class extends " + RENDERER_BASE_CLASS_NAME + ": "
                    + e.getMessage());
            return false;
        }
    }

    private JavaAnnotation createResourceAnnotation(ELType dependencyType, ResourceDependency resource) {
        return new JavaAnnotation(dependencyType, "name=\"" + resource.getName() + "\"", "library=\"" + resource.getLibrary()
                + "\"", "target=\"" + resource.getTarget() + "\"");
    }

    private void addHelperMethod(HelperMethod helperMethod) {
        if (addedHelperMethods.add(helperMethod)) {
            JavaMethod helperJavaMethod = helperMethodFactory.getHelperMethod(helperMethod);
            if (helperJavaMethod.isHidden()) {
                generatedClass.addImports(helperJavaMethod.getRequiredImports());
            } else {
                generatedClass.addMethod(helperJavaMethod);
            }
            addHelperMethods(helperJavaMethod.getMethodBody());
        }
    }

    private void addHelperMethods(JavaStatement statement) {
        if (statement instanceof TemplateStatement) {
            TemplateStatement templateStatement = (TemplateStatement) statement;
            for (HelperMethod helper : templateStatement.getRequiredMethods()) {
                addHelperMethod(helper);
            }
        }
    }

    private ELType getType(Class<?> type) {
        // get type by name in order to get the class instance from CDK class loader
        return typesFactory.getType(type.getName());
    }

    private void createMethodContext() {
        this.currentStatement = new StatementsContainer();
        currentStatement.setVariable(FACES_CONTEXT_VARIABLE, getType(FacesContext.class));
        currentStatement.setVariable(RESPONSE_WRITER_VARIABLE, getType(ResponseWriter.class));
        currentStatement.setVariable(CLIENT_ID_VARIABLE, getType(String.class));

        ELType componentBaseClassType = typesFactory.getType(componentBaseClass.getName());
        currentStatement.setVariable(COMPONENT_VARIABLE, componentBaseClassType);

        ELType generatedClassSuperType = typesFactory.getType(generatedClass.getSuperClass().getName());
        currentStatement.setVariable(SUPER_VARIABLE, generatedClassSuperType);

        ELType generatedClassType = typesFactory.getGeneratedType(generatedClass.getName(), generatedClassSuperType);
        currentStatement.setVariable(THIS_VARIABLE, generatedClassType);
    }

    private void flushToMethod(String methodName, boolean enforce, boolean override, Collection<Argument> additionaArguments) {
        if (enforce || !this.currentStatement.isEmpty()) {
            Argument facesContextArgument = new Argument(FACES_CONTEXT_VARIABLE, getType(FacesContext.class));
            Argument responseWriterArgument = new Argument(RESPONSE_WRITER_VARIABLE, getType(ResponseWriter.class));
            Argument componentArgument;
            int statementCount = 0;
            if (null != compositeInterface.getComponentBaseClass()) {
                ELType type = typesFactory.getType(compositeInterface.getComponentBaseClass().getName());
                componentArgument = new Argument(COMPONENT_PARAMETER, getType(UIComponent.class));
                CastComponentStatement statement = createStatement(CastComponentStatement.class);
                statement.setType(type);
                statement.setComponentParameter(COMPONENT_PARAMETER);
                currentStatement.addStatement(statementCount, statement);
                statementCount++;
            } else {
                componentArgument = new Argument(COMPONENT_VARIABLE, getType(UIComponent.class));
            }

            List<Argument> arguments = Lists.newLinkedList();
            if (this.isExtendingRendererBase) {
                arguments.add(responseWriterArgument);
            }
            arguments.add(facesContextArgument);
            arguments.add(componentArgument);
            arguments.addAll(additionaArguments);

            JavaMethod javaMethod = new JavaMethod(methodName, arguments);
            javaMethod.addModifier(JavaModifier.PUBLIC);
            javaMethod.getExceptions().add(getType(IOException.class));
            if (override) {
                javaMethod.addAnnotation(new JavaAnnotation(getType(Override.class)));
            }

            EncodeMethodPrefaceStatement encodeMethodPreface = createStatement(EncodeMethodPrefaceStatement.class);
            encodeMethodPreface.setRenderResponseWriter(!this.isExtendingRendererBase);
            currentStatement.addStatement(statementCount, encodeMethodPreface);

            javaMethod.setMethodBody(currentStatement);

            addHelperMethods(currentStatement);
            for (JavaField field : currentStatement.getRequiredFields()) {
                generatedClass.addField(field);
            }
            generatedClass.addMethod(javaMethod);
        }
    }

    private void flushToEncodeMethod(String encodeMethodName, boolean enforce) {
        flushToMethod(encodeMethodName, enforce, true, Collections.EMPTY_LIST);
        createMethodContext();
    }

    private void createRendersChildrenMethod() {
        Boolean rendersChildren = compositeInterface.getRendersChildren();
        if (rendersChildren != null) {
            JavaMethod rendersChildrenMethod = new JavaMethod("getRendersChildren", TypesFactory.BOOLEAN_TYPE);
            rendersChildrenMethod.addModifier(JavaModifier.PUBLIC);
            rendersChildrenMethod.addAnnotation(new JavaAnnotation(getType(Override.class)));

            ConstantReturnMethodBodyStatement statement = createStatement(ConstantReturnMethodBodyStatement.class);
            statement.setReturnValue(Boolean.toString(compositeInterface.getRendersChildren()));
            rendersChildrenMethod.setMethodBody(statement);
            generatedClass.addMethod(rendersChildrenMethod);
        }
    }

    private <T extends TemplateStatement> T createStatement(Class<T> statementClass) {
        return this.injector.getInstance(statementClass);
    }

    protected void pushStatement(StatementsContainer container) {
        addStatement(container);
        currentStatement = container;
    }

    protected <T extends StatementsContainer> T pushStatement(Class<T> container) {
        T statement = createStatement(container);
        pushStatement(statement);
        return statement;
    }

    protected void popStatement() {
        currentStatement = currentStatement.getParent();
    }

    protected <T extends TemplateStatement> T addStatement(Class<T> statementClass) {
        T statement = createStatement(statementClass);
        addStatement(statement);
        return statement;
    }

    protected void addStatement(TemplateStatement statement) {
        currentStatement.addStatement(statement);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the rendererClass
     */
    public JavaClass getGeneratedClass() {
        return this.generatedClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.CdkBodyElement)
     */

    @Override
    public void startElement(CdkBodyElement cdkBodyElement) throws CdkException {
        if (this.isExtendingRendererBase) {
            flushToEncodeMethod("doEncodeBegin", false);
        } else {
            flushToEncodeMethod("encodeBegin", false);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkBodyElement)
     */

    @Override
    public void endElement(CdkBodyElement cdkBodyElement) throws CdkException {
        if (this.isExtendingRendererBase) {
            flushToEncodeMethod("doEncodeChildren", cdkBodyElement.isEnforce());
        } else {
            flushToEncodeMethod("encodeChildren", cdkBodyElement.isEnforce());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.AnyElement)
     */

    @Override
    public void startElement(AnyElement anyElement) throws CdkException {
        QName elementName = anyElement.getName();
        if (Template.isDirectiveNamespace(elementName)) {
            log.error("Unknown directive element " + elementName);
        } else {
            StartElementStatement startElementStatement = addStatement(StartElementStatement.class);
            startElementStatement.setElementName(elementName);
            AttributesStatement attributesStatement = addStatement(AttributesStatement.class);
            attributesStatement.processAttributes(anyElement, attributes);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.AnyElement)
     */

    @Override
    public void endElement(AnyElement anyElement) throws CdkException {
        QName elementName = anyElement.getName();
        EndElementStatement endElementStatement = addStatement(EndElementStatement.class);
        endElementStatement.setElementName(elementName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor#visitElement(java.lang.String)
     */

    @Override
    public void visitElement(String text) throws CdkException {
        if (text != null) {
            String trimmedText = text.trim();
            if (trimmedText.length() > 0) {
                WriteTextStatement statement = addStatement(WriteTextStatement.class);
                statement.setExpression(trimmedText);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #visitElement(org.richfaces.cdk.templatecompiler.model.CdkCallElement)
     */

    @Override
    public void visitElement(CdkCallElement cdkCallElement) throws CdkException {
        String expression = cdkCallElement.getExpression();
        if (Strings.isEmpty(expression)) {
            expression = cdkCallElement.getBodyValue();
        }
        addStatement(new TemplateStatementImpl(expression + ";"));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.CdkIfElement)
     */

    @Override
    public void startElement(CdkIfElement cdkIfElement) {
        pushStatement(IfElseStatement.class);
        IfStatement ifStatement = pushStatement(IfStatement.class);
        ifStatement.setTest(cdkIfElement.getTest());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkIfElement)
     */

    @Override
    public void endElement(CdkIfElement cdkIfElement) {
        popStatement();
        popStatement();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.CdkChooseElement)
     */

    @Override
    public void startElement(CdkChooseElement cdkChooseElement) {
        pushStatement(IfElseStatement.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkChooseElement)
     */

    @Override
    public void endElement(CdkChooseElement cdkChooseElement) {
        popStatement();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.CdkWhenElement)
     */

    @Override
    public void startElement(CdkWhenElement cdkWhenElement) {
        IfStatement ifStatement = pushStatement(IfStatement.class);
        ifStatement.setTest(cdkWhenElement.getTest());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkWhenElement)
     */

    @Override
    public void endElement(CdkWhenElement cdkWhenElement) {
        popStatement();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.CdkOtherwiseElement)
     */

    @Override
    public void startElement(CdkOtherwiseElement cdkOtherwiseElement) {
        pushStatement(StatementsContainer.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkOtherwiseElement)
     */

    @Override
    public void endElement(CdkOtherwiseElement cdkOtherwiseElement) {
        popStatement();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #visitElement(org.richfaces.cdk.templatecompiler.model.CdkObjectElement)
     */

    @Override
    public void visitElement(CdkObjectElement cdkObjectElement) {
        String name = cdkObjectElement.getName();
        String value = cdkObjectElement.getValue();
        if (Strings.isEmpty(value)) {
            value = cdkObjectElement.getBodyValue();
        }
        String type = cdkObjectElement.getType();
        DefineObjectStatement statement = addStatement(DefineObjectStatement.class);
        statement.setObject(name, type, value, cdkObjectElement.isCast());
        currentStatement.setVariable(name, statement.getType());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #startElement(org.richfaces.cdk.templatecompiler.model.CdkForEachElement)
     */

    @Override
    public void startElement(CdkForEachElement cdkForEachElement) {
        String items = cdkForEachElement.getItems();
        // String itemsExpression = compileEl(items, Iterable.class);

        // TODO - review
        // Class<?> collectionElementClass = lastCompiledExpressionType.getContainerType().getRawType();
        // if (collectionElementClass == null) {
        // collectionElementClass = Object.class;
        // }

        String var = cdkForEachElement.getVar();
        String varStatus = cdkForEachElement.getVarStatus();
        Integer begin = cdkForEachElement.getBegin();
        Integer end = cdkForEachElement.getEnd();
        Integer step = cdkForEachElement.getStep();

        ForEachStatement forEachStatement = pushStatement(ForEachStatement.class);
        forEachStatement.setItemsExpression(items, var, varStatus, begin, end, step);
        // currentStatement.setVariable(cdkForEachElement.getVar(), lastCompiledExpressionType.getContainerType());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkForEachElement)
     */

    @Override
    public void endElement(CdkForEachElement cdkForEachElement) {
        popStatement();
    }

    @Override
    public void startElement(CdkSwitchElement cdkSwitchElement) {
        String key = cdkSwitchElement.getKey();
        // String keyExpression = compileEl(key, Object.class);
        SwitchStatement switchStatement = pushStatement(SwitchStatement.class);
        switchStatement.setKeyExpression(key);
    }

    @Override
    public void endElement(CdkSwitchElement cdkSwitchElement) {
        popStatement();
    }

    @Override
    public void startElement(CdkCaseElement cdkCaseElement) {
        CaseStatement caseStatement = pushStatement(CaseStatement.class);
        caseStatement.setValues(cdkCaseElement.getValues());
    }

    @Override
    public void endElement(CdkCaseElement cdkCaseElement) {
        popStatement();
    }

    @Override
    public void startElement(CdkDefaultElement cdkDefaultElement) {
        pushStatement(CaseStatement.class);
    }

    @Override
    public void endElement(CdkDefaultElement cdkDefaultElement) {
        popStatement();
    }

    /**
     *
     */
    public void preProcess(CompositeImplementation impl) {
        initializeJavaClass();
    }

    @Override
    public void startElement(CompositeRenderFacet compositeRenderFacetElement) throws CdkException {
        RenderFacetStatement renderFacetStatement = pushStatement(RenderFacetStatement.class);
        renderFacetStatement.setName(compositeRenderFacetElement.getName());
    }

    @Override
    public void endElement(CompositeRenderFacet compositeRenderFacetElement) throws CdkException {
        popStatement();
    }

    /**
     *
     */
    public void postProcess(CompositeImplementation impl) {
        if (this.isExtendingRendererBase) {
            flushToEncodeMethod("doEncodeEnd", false);
        } else {
            flushToEncodeMethod("encodeEnd", false);
        }
        createRendersChildrenMethod();
    }

    @Override
    public void startElement(CdkScriptObjectElement cdkScriptObjectElement) {
        ScriptObjectStatement scriptObjectStatement = pushStatement(ScriptObjectStatement.class);
        scriptObjectStatement.setObject(cdkScriptObjectElement.getName(), cdkScriptObjectElement.getBase());
    }

    @Override
    public void endElement(CdkScriptObjectElement cdkScriptObjectElement) {
        popStatement();
    }

    private void addScriptHashAttributesPassthroughStatement(List<String> attributeNames, String wrapper) {
        if (attributeNames == null || attributeNames.isEmpty()) {
            return;
        }

        AddAttributesToScriptHashStatement statement = addStatement(AddAttributesToScriptHashStatement.class);
        statement.setWrapper(wrapper);
        statement.setAttributes(attributeNames, attributes);
    }

    private void addScriptOptionStatement(String name, String value, String defaultValue, String wrapper) {
        ScriptOptionStatement scriptOptionStatement = addStatement(ScriptOptionStatement.class);
        scriptOptionStatement.setName(name);
        scriptOptionStatement.setValueExpression(value);
        scriptOptionStatement.setDefaultValue(defaultValue);
        scriptOptionStatement.setWrapper(wrapper);
    }

    @Override
    public void visitElement(CdkScriptOptionElement cdkScriptOptionElement) {
        addScriptHashAttributesPassthroughStatement(cdkScriptOptionElement.getAttributes(), cdkScriptOptionElement.getWrapper());

        for (String variableName : cdkScriptOptionElement.getVariables()) {
            addScriptOptionStatement(variableName, MessageFormat.format("#'{'{0}'}'", variableName),
                    cdkScriptOptionElement.getDefaultValue(), cdkScriptOptionElement.getWrapper());
        }

        if (!Strings.isEmpty(cdkScriptOptionElement.getName())) {
            addScriptOptionStatement(cdkScriptOptionElement.getName(), cdkScriptOptionElement.getValue(),
                    cdkScriptOptionElement.getDefaultValue(), cdkScriptOptionElement.getWrapper());
        }
    }

    @Override
    public void preProcess(CdkFragmentElement fragmentElement) {
        Fragment fragment = fragmentStore.addFragment(fragmentElement);

        this.createMethodContext();
        for (Argument argument : fragment.getAllArguments()) {
            currentStatement.setVariable(argument.getName(), argument.getType());
        }
    }

    @Override
    public void postProcess(CdkFragmentElement fragmentElement) {
        Fragment fragment = fragmentStore.getFragment(fragmentElement.getName());
        flushToMethod(fragment.getMethodName(), true, false, fragment.getAllArguments());

        this.createMethodContext();
    }

    @Override
    public void visitElement(CdkRenderFragmentElement renderFragmentElement) throws CdkException {
        RenderFragmentStatement statement = addStatement(RenderFragmentStatement.class);
        statement.setMethodName(renderFragmentElement.getName());
        statement.setAttributes(renderFragmentElement.getAttributes());
        statement.setFragmentStore(this.fragmentStore);
        statement.setExtendingRendererBase(this.isExtendingRendererBase);
    }
}
