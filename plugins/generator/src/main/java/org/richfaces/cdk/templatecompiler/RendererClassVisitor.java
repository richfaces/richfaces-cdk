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
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.xml.namespace.QName;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;
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
import org.richfaces.cdk.templatecompiler.model.CdkIfElement;
import org.richfaces.cdk.templatecompiler.model.CdkObjectElement;
import org.richfaces.cdk.templatecompiler.model.CdkOtherwiseElement;
import org.richfaces.cdk.templatecompiler.model.CdkSwitchElement;
import org.richfaces.cdk.templatecompiler.model.CdkWhenElement;
import org.richfaces.cdk.templatecompiler.model.CompositeImplementation;
import org.richfaces.cdk.templatecompiler.model.CompositeInterface;
import org.richfaces.cdk.templatecompiler.model.ResourceDependency;
import org.richfaces.cdk.templatecompiler.model.Template;
import org.richfaces.cdk.templatecompiler.model.TemplateVisitor;
import org.richfaces.cdk.templatecompiler.statements.AttributesStatement;
import org.richfaces.cdk.templatecompiler.statements.CaseStatement;
import org.richfaces.cdk.templatecompiler.statements.ConstantReturnMethodBodyStatement;
import org.richfaces.cdk.templatecompiler.statements.DefineObjectStatement;
import org.richfaces.cdk.templatecompiler.statements.EncodeMethodPrefaceStatement;
import org.richfaces.cdk.templatecompiler.statements.EndElementStatement;
import org.richfaces.cdk.templatecompiler.statements.ForEachStatement;
import org.richfaces.cdk.templatecompiler.statements.HelperMethod;
import org.richfaces.cdk.templatecompiler.statements.HelperMethodFactory;
import org.richfaces.cdk.templatecompiler.statements.IfElseStatement;
import org.richfaces.cdk.templatecompiler.statements.IfStatement;
import org.richfaces.cdk.templatecompiler.statements.StartElementStatement;
import org.richfaces.cdk.templatecompiler.statements.StatementsContainer;
import org.richfaces.cdk.templatecompiler.statements.SwitchStatement;
import org.richfaces.cdk.templatecompiler.statements.TemplateStatement;
import org.richfaces.cdk.templatecompiler.statements.TemplateStatementImpl;
import org.richfaces.cdk.templatecompiler.statements.WriteTextStatement;
import org.richfaces.cdk.util.Strings;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Injector;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 */
public class RendererClassVisitor implements TemplateVisitor {

    /**
     *
     */
    // TODO externalize
    public static final String RENDER_KIT_UTILS_CLASS_NAME = "org.richfaces.renderkit.RenderKitUtils";
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
    public static final String THIS_VARIABLE = "this";
    /**
     *
     */
    public static final String SUPER_VARIABLE = "super";
    /**
     *
     */
    public static final String FACES_CONTEXT_VARIABLE = "facesContext";
    public static final ImmutableMap<String, Object> ENCODE_METHOD_VARIABLES =
        ImmutableMap.<String, Object> builder()
            .put("facesContextVariable", RendererClassVisitor.FACES_CONTEXT_VARIABLE).put("componentVariable",
                RendererClassVisitor.COMPONENT_VARIABLE).put("responseWriterVariable",
                RendererClassVisitor.RESPONSE_WRITER_VARIABLE).put("clientIdVariable",
                RendererClassVisitor.CLIENT_ID_VARIABLE).build();
    /**
     *
     */
    static final String CLIENT_ID_VARIABLE = "clientId";

    /**
     *
     */
    private static final String PASS_THROUGH_ATTRIBUTES_FIELD_NAME = "PASS_THROUGH_ATTRIBUTES";

    private final Logger log;

    private final Injector injector;
    private final TypesFactory typesFactory;
    private final HelperMethodFactory helperMethodFactory;
    private final CompositeInterface compositeInterface;
    private final Collection<PropertyBase> attributes;
    private StatementsContainer currentStatement;

    private JavaClass generatedClass;
    private Set<HelperMethod> addedHelperMethods = EnumSet.noneOf(HelperMethod.class);

    private int passThroughCounter;

    public RendererClassVisitor(CompositeInterface compositeInterface, Collection<PropertyBase> attributes, Logger log,
        Injector injector, TypesFactory typesFactory, HelperMethodFactory helperFactory) {
        this.compositeInterface = compositeInterface;
        this.attributes = attributes;
        this.injector = injector;
        this.typesFactory = typesFactory;
        this.log = log;
        this.helperMethodFactory = helperFactory;
    }

    private void initializeJavaClass() {
        this.generatedClass = new JavaClass(compositeInterface.getJavaClass());
        this.generatedClass.addModifier(JavaModifier.PUBLIC);
        if (null != compositeInterface.getBaseClass()) {
            this.generatedClass.setSuperClass(compositeInterface.getBaseClass());
        }
        this.generatedClass.addImport(FacesContext.class);
        this.generatedClass.addImport(ResponseWriter.class);
        this.generatedClass.addImport(UIComponent.class);

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
                resources.append("name=\"").append(resource.getName()).append("\",library=\"").append(
                    resource.getLibrary()).append("\",target=\"").append(resource.getTarget()).append("\"").append(")");
            }
            resources.append("}");
            this.generatedClass.addAnnotation(new JavaAnnotation(typesFactory.getType(ResourceDependencies.class),
                resources.toString()));
            this.generatedClass.addImport(javax.faces.application.ResourceDependency.class);
        }
        this.createMethodContext();
    }

    private JavaAnnotation createResourceAnnotation(ELType dependencyType, ResourceDependency resource) {
        return new JavaAnnotation(dependencyType, "name=\"" + resource.getName() + "\"", "library=\""
            + resource.getLibrary() + "\"", "target=\"" + resource.getTarget() + "\"");
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

    private ELType getType(Type type) {
        return typesFactory.getType(type);
    }

    private void createMethodContext() {
        this.currentStatement = new StatementsContainer();
        currentStatement.setVariable(FACES_CONTEXT_VARIABLE, getType(FacesContext.class));
        currentStatement.setVariable(RESPONSE_WRITER_VARIABLE, getType(ResponseWriter.class));
        currentStatement.setVariable(CLIENT_ID_VARIABLE, getType(String.class));

        // TODO: try load component class
        currentStatement.setVariable(COMPONENT_VARIABLE, getType(UIComponent.class));

        ELType generatedClassType = typesFactory.getType(generatedClass.getName());
        currentStatement.setVariable(THIS_VARIABLE, generatedClassType);

        ELType generatedClassSuperType = typesFactory.getType(generatedClass.getSuperClass().getName());
        currentStatement.setVariable(SUPER_VARIABLE, generatedClassSuperType);
    }

    private void flushToEncodeMethod(String encodeMethodName) {
        if (!this.currentStatement.isEmpty()) {
            Argument facesContextArgument = new Argument(FACES_CONTEXT_VARIABLE, getType(FacesContext.class));
            Argument componentArgument = new Argument(COMPONENT_VARIABLE, getType(UIComponent.class));

            JavaMethod javaMethod = new JavaMethod(encodeMethodName, facesContextArgument, componentArgument);
            javaMethod.addModifier(JavaModifier.PUBLIC);
            javaMethod.addAnnotation(new JavaAnnotation(getType(Override.class)));
            javaMethod.getExceptions().add(getType(IOException.class));
            currentStatement.addStatement(0, createStatement(EncodeMethodPrefaceStatement.class));

            javaMethod.setMethodBody(currentStatement);

            addHelperMethods(currentStatement);
            for (JavaField field : currentStatement.getRequiredFields()) {
                generatedClass.addField(field);
            }
            generatedClass.addMethod(javaMethod);
        }

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
        flushToEncodeMethod("encodeBegin");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.templatecompiler.model.TemplateVisitor
     * #endElement(org.richfaces.cdk.templatecompiler.model.CdkBodyElement)
     */

    @Override
    public void endElement(CdkBodyElement cdkBodyElement) throws CdkException {
        flushToEncodeMethod("encodeChildren");
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
        statement.setObject(name, type, value);
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
        ForEachStatement forEachStatement = pushStatement(ForEachStatement.class);
        forEachStatement.setItemsExpression(items, cdkForEachElement.getVar());
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
        passThroughCounter = -1;
    }

    /**
     *
     */
    public void postProcess(CompositeImplementation impl) {
        flushToEncodeMethod("encodeEnd");
        createRendersChildrenMethod();
    }

}
