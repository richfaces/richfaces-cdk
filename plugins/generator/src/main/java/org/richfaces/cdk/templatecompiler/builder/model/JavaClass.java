/**
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

package org.richfaces.cdk.templatecompiler.builder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.render.Renderer;

import org.richfaces.cdk.model.ClassName;

/**
 * Java Class model. Intended for building java classes.
 * 
 * @author Maksim Kaszynski
 */
public class JavaClass extends JavaLanguageElement {
    private static final ClassName DEFAULT_SUPERCLASS = ClassName.get(Renderer.class);
    private List<JavaField> fields = new ArrayList<JavaField>();
    private List<JavaMethod> methods = new ArrayList<JavaMethod>();
    private Set<JavaImport> imports = new TreeSet<JavaImport>(JavaImport.COMPARATOR);
    private ClassName superClass = DEFAULT_SUPERCLASS;

    private final JavaPackage pakg;

    private final String simpleName;

    public JavaClass(String simpleName, JavaPackage pakg) {
        super(getFullName(pakg, simpleName));
        this.pakg = pakg;
        this.simpleName = simpleName;
    }

    public JavaClass(ClassName javaClass) {
        this(javaClass.getSimpleName(), new JavaPackage(javaClass.getPackage()));
    }

    private static String getFullName(JavaPackage javaPackage, String className) {
        StringBuilder fullName = new StringBuilder();

        fullName.append(javaPackage.getName());

        if (fullName.length() != 0) {
            fullName.append('.');
        }
        fullName.append(className);

        return fullName.toString();
    }


    public void addImport(String name) {
        addImport(new RuntimeImport(name));
    }

    public void addImport(JavaImport javaImport) {
        imports.add(javaImport);
    }

    public void addImport(Class<?> claz) {
        if (shouldAddToImports(claz.getName())) {
            imports.add(new ClassImport(claz));
        }
    }

    public void addImports(Iterable<JavaImport> imports) {
        for (JavaImport elType : imports) {
            addImport(elType);
        }
    }

    @Override
    public void addAnnotation(JavaAnnotation annotation) {
        super.addAnnotation(annotation);
        addImports(annotation.getRequiredImports());
    }

    public void addField(JavaField field) {
        fields.add(field);
        addImports(field.getRequiredImports());
    }

    public void addMethod(JavaMethod method) {
        methods.add(method);
        addImports(method.getRequiredImports());

    }

    public JavaPackage getPakg() {
        return pakg;
    }

    public ClassName getSuperClass() {
        return superClass;
    }

    public void setSuperClass(ClassName superClass) {
        this.superClass = superClass;
        addImport(superClass.getName());
    }


    public JavaPackage getPackage() {
        return pakg;
    }

    public List<JavaField> getFields() {
        return fields;
    }

    public List<JavaMethod> getMethods() {
        return methods;
    }

    public Set<JavaImport> getImports() {
        return imports;
    }

    public String getSimpleName() {
        return simpleName;
    }

    private boolean shouldAddToImports(String className) {
        if (className == null || className.length() == 0) {
            return false;
        }

        // default package & primitive types
        if (className.indexOf('.') == -1) {
            return false;
        }

        if (className.matches("^java\\.lang\\.[^\\.]+$")) {
            return false;
        }

        return true;
    }
}
