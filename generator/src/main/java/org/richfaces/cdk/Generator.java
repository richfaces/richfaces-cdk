/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;

import org.richfaces.cdk.apt.AptModule;
import org.richfaces.cdk.apt.CacheType;
import org.richfaces.cdk.apt.LibraryCache;
import org.richfaces.cdk.apt.LibraryCacheImpl;
import org.richfaces.cdk.generate.java.ClassGeneratorModule;
import org.richfaces.cdk.generate.taglib.TaglibModule;
import org.richfaces.cdk.model.ModelModule;
import org.richfaces.cdk.model.validator.ValidatorImpl;
import org.richfaces.cdk.templatecompiler.TemplateModule;
import org.richfaces.cdk.xmlconfig.XmlModule;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;

/**
 * @author asmirnov
 * @version $Id$
 *
 */
public class Generator {
    public static final String RENDERER_UTILS_CLASS = "rendererUtils";
    public static final String OPTIONS = "OPTIONS";
    private CdkClassLoader loader;
    private Logger log = new JavaLogger();
    private Injector injector;
    private String namespace;
    private Locale locale = Locale.getDefault();
    private Charset charset = Charset.defaultCharset();
    private Map<Outputs, FileManager> outputFolders = Maps.newEnumMap(Outputs.class);
    private Map<Sources, FileManager> sources = Maps.newEnumMap(Sources.class);
    private LibraryBuilder libraryBuilder;
    private Map<String, String> options = Maps.newHashMap();
    private java.util.logging.Logger logger;

    public Generator() {
        EmptyFileManager emptyFileManager = new EmptyFileManager();
        for (Sources source : Sources.values()) {
            sources.put(source, emptyFileManager);
        }

        for (Outputs output : Outputs.values()) {
            outputFolders.put(output, emptyFileManager);
        }
    }

    public void setLoader(CdkClassLoader loader) {
        this.loader = loader;
    }

    public void setLog(Logger log) {
        this.log = log;
        // setup freemaker logger.
        try {
            freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_JAVA);
            freemarker.log.Logger.setCategoryPrefix(JavaLogger.CDK_LOG + ".");
            logger = java.util.logging.Logger.getLogger(JavaLogger.CDK_LOG);
            logger.addHandler(new CDKHandler(log));
            if (log.isDebugEnabled()) {
                logger.setLevel(Level.ALL);
            } else if (log.isInfoEnabled()) {
                logger.setLevel(Level.INFO);
            } else if (log.isWarnEnabled()) {
                logger.setLevel(Level.WARNING);
            } else {
                logger.setLevel(Level.SEVERE);
            }
        } catch (ClassNotFoundException e) {
            log.error(e);
            // DO Nothing, JDK 6 has built-in Logger facility;
        }
    }

    public void addOutputFolder(Outputs type, File outputFolder) {
        this.outputFolders.put(type, new OutputFileManagerImpl(outputFolder));
    }

    public void addSources(Sources type, Iterable<File> files, Iterable<File> folders) {
        this.sources.put(type, new SourceFileManagerImpl(files, folders));
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public void init() {
        TimeMeasure time = new TimeMeasure("module instantiation", log).info(true).start();

        Module[] defaultModules = new Module[] { new CdkConfigurationModule(), new AptModule(), new ModelModule(),
                new ClassGeneratorModule(), new TemplateModule(), new XmlModule(), new TaglibModule() };

        ServiceLoader<CdkModule> overlayModules = ServiceLoader.load(CdkModule.class);

        Module modules = Modules.override(defaultModules).with(overlayModules);

        injector = Guice.createInjector(Stage.DEVELOPMENT, modules);

        time.stop();

        if (!log.isDebugEnabled()) {
            try {
                freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
            } catch (ClassNotFoundException e) {
                // Do nothing.
            }
        }

        // Create builder instance.
        this.libraryBuilder = injector.getInstance(LibraryBuilder.class);
    }

    public void execute() {
        checkNotNull(libraryBuilder, "initialized");
        libraryBuilder.build();
    }

    public static final class EmptyFileManager implements FileManager {
        @Override
        public Iterable<File> getFolders() {
            return Collections.emptyList();
        }

        @Override
        public Iterable<File> getFiles() {
            return Collections.emptyList();
        }

        @Override
        public File getFile(String path) throws FileNotFoundException {
            throw new FileNotFoundException();
        }

        @Override
        public Writer createOutput(String path, long lastModified) throws IOException {
            throw new IOException("read-only");
        }
    }

    public class CdkConfigurationModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(CdkClassLoader.class).toInstance(loader);
            bind(Logger.class).toInstance(log);
            bind(Locale.class).toInstance(locale);
            bind(Charset.class).toInstance(charset);
            bind(Generator.class).toInstance(Generator.this);
            for (Map.Entry<Outputs, FileManager> entry : outputFolders.entrySet()) {
                bind(FileManager.class).annotatedWith(new OutputImpl(entry.getKey())).toInstance(entry.getValue());
            }
            for (Map.Entry<Sources, FileManager> entry : sources.entrySet()) {
                bind(FileManager.class).annotatedWith(new SourceImpl(entry.getKey())).toInstance(entry.getValue());
            }
            for (CacheType cacheType : CacheType.values()) {
                LibraryCache cache = new LibraryCacheImpl(cacheType);
                requestInjection(cache);
                bind(LibraryCache.class).annotatedWith(new CacheImpl(cacheType)).toInstance(cache);
            }
            bind(ModelValidator.class).to(ValidatorImpl.class);
            bind(NamingConventions.class).to(RichFacesConventions.class);
            Names.bindProperties(binder(), options);
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
