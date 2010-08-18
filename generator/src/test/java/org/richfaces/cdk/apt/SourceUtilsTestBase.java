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

import static junit.framework.Assert.*;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.FileManager;
import org.richfaces.cdk.Output;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.Stub;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * <p class="changed_added_4_0"></p>
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public abstract class SourceUtilsTestBase extends AnnotationProcessorTestBase {

    @Inject
    protected CdkProcessor processor;

    @Inject
    protected TaskFactoryImpl factory;

    @Stub
    @Output(Outputs.JAVA_CLASSES)
    protected FileManager output;
    

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(CdkProcessor.class).to(TestProcessor.class).in(Singleton.class);
    }
    
    
    protected void execute(SourceUtilsCallback callback){
        ((TestProcessor)processor).callback = callback;
        assertTrue("Compilation error",factory.get().call());
    }
    
    /**
     * <p class="changed_added_4_0">Interface to call back test method from APT</p>
     * @author asmirnov@exadel.com
     *
     */
    public interface SourceUtilsCallback {
        void process(SourceUtils utils, RoundEnvironment roundEnv);
    }


    /**
     * <p class="changed_added_4_0"></p>
     * @author asmirnov@exadel.com
     *
     */
    @SupportedSourceVersion(SourceVersion.RELEASE_6)
    @SupportedAnnotationTypes("*")
    public static class TestProcessor extends AbstractProcessor implements CdkProcessor {

        @Inject
        protected SourceUtilsProvider sourceUtilsProvider;
        
        SourceUtilsCallback callback;

        @Override
        public synchronized void init(ProcessingEnvironment processingEnv) {
            super.init(processingEnv);
            sourceUtilsProvider.setProcessingEnv(processingEnv);
        }

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            if(!roundEnv.processingOver()){
                callback.process(sourceUtilsProvider.get(), roundEnv);
            }
            return false;
        }

        @Override
        public void processNonJavaSources() {
            // do nothing            
        }
        
    }
}
