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
package org.richfaces.cdk.apt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.lang.model.element.Name;

import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Generator;
import org.richfaces.cdk.LibraryBuilder;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.MockController;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.SourceFileManagerImpl;
import org.richfaces.cdk.SourceImpl;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.apt.processors.ComponentProcessor;
import org.richfaces.cdk.model.ComponentModel;

import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author akolonitsky
 * @since Jan 14, 2010
 */
public abstract class AnnotationProcessorTestBase extends CdkTestBase {
    protected static final class TestName implements Name {
        private final String name;

        public TestName(String name) {
            this.name = name;
        }

        @Override
        public char charAt(int index) {
            return name.charAt(index);
        }

        @Override
        public boolean contentEquals(CharSequence cs) {
            // TODO Auto-generated method stub
            return name.equals(cs);
        }

        @Override
        public int length() {
            return name.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return name.substring(start, end);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    protected static final String FOO_BAR = "foo.Bar";
    @Inject
    protected Logger log;
    @Inject
    @Source(Sources.JAVA_SOURCES)
    protected FileManager sources;
    @Inject
    protected MockController mockController;
    @Inject
    private Injector injector;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        try {
            CdkClassLoader cdkClassLoader = createClassLoader();
            binder.bind(CdkClassLoader.class).toInstance(cdkClassLoader);
            List<File> sourceFiles = new ArrayList<File>();
            for (String src : sources()) {
                sourceFiles.add(getJavaFile(src));
            }
            binder.bind(FileManager.class).annotatedWith(new SourceImpl(Sources.JAVA_SOURCES))
                    .toInstance(new SourceFileManagerImpl(sourceFiles, this.testSourceDirectory));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected CdkClassLoader createClassLoader() {
        try {
            return new CdkClassLoader(ImmutableList.of(getLibraryFile("test.source.properties"),
                    getLibraryFile(ComponentModel.class), getLibraryFile(ELContext.class), getLibraryFile(JsfComponent.class),
                    getLibraryFile(UIComponent.class)), this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void process(String javaFilePath) throws Exception {
        Generator generator = new Generator();
        generator.setLoader(createClassLoader());
        generator.addSources(Sources.JAVA_SOURCES, ImmutableList.of(getJavaFile(javaFilePath)), null);

        generator.init();
        injector.injectMembers(new ComponentProcessor());

        LibraryBuilder builder = injector.getInstance(LibraryBuilder.class);
        builder.build();
    }

    protected abstract Iterable<String> sources();
}
