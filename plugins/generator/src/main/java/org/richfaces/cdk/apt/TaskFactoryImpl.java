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

package org.richfaces.cdk.apt;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaCompiler.CompilationTask;

import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.Source;
import org.richfaces.cdk.Sources;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public class TaskFactoryImpl implements CompilationTaskFactory {

    private final class DiagnosticListenerImplementation implements DiagnosticListener<JavaFileObject> {

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            StringBuilder message = new StringBuilder(diagnostic.getMessage(locale));
            JavaFileObject source = diagnostic.getSource();
            if (null != source) {
                message.append(", in the file:").append(source.getName()).append(" at line ").append(
                    diagnostic.getLineNumber()).append(" in column ").append(diagnostic.getColumnNumber());
            }
            Kind kind = diagnostic.getKind();
            if (Kind.ERROR.equals(kind)) {
                log.error(message);
            } else if (Kind.MANDATORY_WARNING.equals(kind) || Kind.WARNING.equals(kind)) {
                log.warn(message);
            } else if (Kind.NOTE.equals(kind)) {
                log.info(message);
            } else {
                log.debug(message);
            }
        }
    }

    private static List<String> compilerOptions = new ArrayList<String>(Arrays.asList("-proc:only", "-implicit:class"));

    @Inject
    private Logger log;

    @Inject
    private Locale locale;

    @Inject
    private Charset charset;

    @Inject
    private CdkClassLoader classPathLoader;

    @Inject
    @Output(Outputs.JAVA_CLASSES)
    private FileManager outputFolder;

    @Inject
    @Source(Sources.JAVA_SOURCES)
    private FileManager sourceFolders;

    @Inject
    private CdkProcessor cdkProcessor;

    private JavaCompiler javaCompiler;

    private StandardJavaFileManager fileManager;

    private final DiagnosticListener<JavaFileObject> diagnosticListener = new DiagnosticListenerImplementation();

    /*
     * (non-Javadoc)
     * 
     * @see org.richfaces.cdk.apt.CompilationTaskFactory#getTask(java.lang.Iterable)
     */
    @Override
    public CompilationTask get() throws AptException {
        if (sourceFolders.getFiles().iterator().hasNext()) {
            Iterable<? extends JavaFileObject> sourceObjects =
                getFileManager().getJavaFileObjectsFromFiles(sourceFolders.getFiles());

            if (log.isDebugEnabled()) {
                compilerOptions.add("-verbose");
            }

            CompilationTask task =
                getJavaCompiler().getTask(null, getFileManager(), diagnosticListener, compilerOptions, null,
                    sourceObjects);
            task.setLocale(locale);
            task.setProcessors(Collections.singleton(cdkProcessor));
            return task;
        } else {
            // no Java sources, try to build from xml files
            return new CompilationTask() {
                
                @Override
                public void setProcessors(Iterable<? extends Processor> processors) {
                    // do nothing 
                }
                
                @Override
                public void setLocale(Locale locale) {
                    
                }
                
                @Override
                public Boolean call() {
                    cdkProcessor.processNonJavaSources();
                    return 0 == log.getErrorCount();
                }
            };
        }
    }

    private StandardJavaFileManager getFileManager() {
        if (fileManager == null) {
            fileManager = getJavaCompiler().getStandardFileManager(diagnosticListener, locale, charset);
            try {
                fileManager.setLocation(StandardLocation.CLASS_PATH, classPathLoader.getFiles());
                Iterable<File> outputFolders = outputFolder.getFolders();
                if (null != outputFolders) {
                    // Append only existed folders to output.
                    Iterable<File> existedFolders = Iterables.filter(outputFolders, new Predicate<File>() {

                        @Override
                        public boolean apply(File input) {
                            return input.exists();
                        }
                    });
                    if (existedFolders.iterator().hasNext()) {
                        fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, outputFolders);
                    }
                }
                fileManager.setLocation(StandardLocation.SOURCE_PATH, sourceFolders.getFolders());
            } catch (IOException e) {
                throw new CdkException("Cannot configure JavaFileManager for compilator", e);
            }

        }
        return fileManager;
    }

    private JavaCompiler getJavaCompiler() {
        if (javaCompiler == null) {
            javaCompiler = ToolProvider.getSystemJavaCompiler();
        }
        return javaCompiler;
    }

}
