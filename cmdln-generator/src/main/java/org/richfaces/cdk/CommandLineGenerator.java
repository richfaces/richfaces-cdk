/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.plexus.util.DirectoryScanner;
import org.richfaces.cdk.apt.CdkProcessorImpl;
import org.richfaces.cdk.apt.LibraryCache;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Configurable command-line interface of CDK generator.
 *
 * This class is similar functionality as {@link org.richfaces.builder.mojo.GenerateMojo} from richfaces-cdk-maven-plugin.
 *
 * @author Lukas Fryc
 */
@Parameters(resourceBundle = "cmdln")
public class CommandLineGenerator {

    private static final String[] JAVA_INCLUDES = new String[] { "**/*.java" };
    private static final String MAIN_CONFIG = "src/main/config/";
    private static final String DEFAULT_TEMPLATES_ROOT = "src/main/templates/";
    private static final String[] EMPTY = new String[0];
    private static final String[] XML_INCLUDES = new String[] { "**/*.xml" };

    @Parameter(names = { "-p", "--project" }, descriptionKey = "projectRoot")
    String projectRoot = ".";

    @Parameter(names = { "-d", "--debug" }, descriptionKey = "debug")
    boolean debug = false;

    @Parameter(names = { "-n", "--namespace" }, descriptionKey = "taglibNamespace")
    private String taglibNamespace;

    @Parameter(names = { "-t", "--templates" }, descriptionKey = "templateIncludes")
    List<String> templateIncludes;

    @Parameter(names = { "-h", "--help" }, descriptionKey = "help")
    boolean help = false;

    @Parameter(names = { "-r", "--force-recompile" }, descriptionKey = "forceRecompile")
    boolean forceRecompile = false;

    @Parameter(names = { "-e", "--cache-eagerly" }, descriptionKey = "cacheEagerly")
    boolean cacheEagerly = false;

    private List<String> compileSourceRoots;
    protected String[] sourceIncludes;
    protected String[] sourceExcludes;
    protected String configRoot;

    protected File outputDirectory;
    protected File outputJavaDirectory;
    protected File outputResourcesDirectory;
    protected File outputTestDirectory;
    protected File outputTestResourcesDirectory;
    protected File outputLibraryCache;

    protected Map<String, String> options = new HashMap<String, String>();

    private Logger logger;

    private void setup() {
        compileSourceRoots = Arrays.asList(projectRoot + "/src/main/java");

        if (templateIncludes == null || templateIncludes.isEmpty()) {
            templateIncludes = getDefaultTemplateIncludes();
        }
        configRoot = projectRoot + "/" + MAIN_CONFIG;

        outputDirectory = new File(projectRoot, "target/classes");
        outputJavaDirectory = new File(projectRoot, "target/generated-sources/main/java");
        outputResourcesDirectory = new File(projectRoot, "target/generated-sources/main/resources");
        outputTestDirectory = new File(projectRoot, "target/generated-sources/test/java");
        outputTestResourcesDirectory = new File(projectRoot, "target/generated-sources/test/resources");
        outputLibraryCache = new File(projectRoot, "target/library-cache");

        CustomLogger logger = new CustomLogger();
        logger.setDebugEnabled(debug);
        this.logger = logger;
    }

    public boolean isHelp() {
        return help;
    }

    public void execute() {
        setup();

        TimeMeasure totalTime = new TimeMeasure("cdk", logger).info(true).start(new File(projectRoot).getAbsolutePath());
        executeGenerator();
        totalTime.stop();
    }

    private void executeGenerator() {
        Generator generator = new Generator();
        generator.setLog(logger);
        generator.setLoader(createProjectClassLoader());

        // Set source folders.
        ArrayList<File> folders = new ArrayList<File>(compileSourceRoots.size());

        for (String sourceFolder : compileSourceRoots) {
            File folder = new File(sourceFolder);

            if (folder.exists() && folder.isDirectory()) {
                folders.add(folder);
            }
        }

        generator.addSources(Sources.JAVA_SOURCES, findJavaFiles(), folders);
        // detect templates and configs directories.
        generator.addSources(Sources.RENDERER_TEMPLATES, findTemplateFiles(), null);
        generator.addSources(Sources.FACES_CONFIGS, findFacesConfigFiles(), null);

        // Setup output folders.
        setOutput(generator, outputJavaDirectory, Outputs.JAVA_CLASSES);
        setOutput(generator, outputResourcesDirectory, Outputs.RESOURCES);
        setOutput(generator, outputTestDirectory, Outputs.TEST_JAVA_CLASSES);
        setOutput(generator, outputTestResourcesDirectory, Outputs.TEST_RESOURCES);
        setOutput(generator, outputLibraryCache, Outputs.LIBRARY_CACHE);

        options.put(LibraryCache.CACHE_ENABLED_OPTION, Boolean.toString(!forceRecompile));
        options.put(CdkProcessorImpl.CACHE_EAGERLY_OPTION, Boolean.toString(cacheEagerly));

        // configure CDK workers.
        setupPlugins(generator);

        if (null != options) {
            generator.setOptions(options);
        }

        try {
            if (taglibNamespace != null) {
                generator.setNamespace(taglibNamespace);
            }

            // Build JSF library.
            // LibraryBuilder builder = LibraryBuilder.createInstance(context);
            generator.init();
            generator.execute();

            if (logger.getErrorCount() > 0) {
                throw new IllegalStateException("Errors occurred while JSF library was built");
            }
        } catch (CdkException e) {
            throw new IllegalStateException("CDK build error", e);
        }
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param generator
     * @throws MojoFailureException
     */
    private void setupPlugins(Generator generator) {
        // TODO - get additional modules, as Maven components ?
    }

    /**
     * <p class="changed_added_4_0">
     * This utility method sets output directory for particular type. I such directory does not exist, it is created.
     * </p>
     *
     * @param generator
     * @param directory
     * @param type
     */
    private static void setOutput(Generator generator, File directory, Outputs type) {
        // if (!directory.exists()) {
        // directory.mkdirs();
        // }

        generator.addOutputFolder(type, directory);
    }

    private Iterable<File> findTemplateFiles() {
        String[] includes = templateIncludes.toArray(new String[templateIncludes.size()]);
        String[] files = doScan(includes, EMPTY, new File(projectRoot));
        return Collections2.transform(Arrays.asList(files), new StringToFile(projectRoot));
    }

    private Iterable<File> findJavaFiles() {
        Set<File> javaSources = new HashSet<File>();
        String[] includes = null == sourceIncludes ? JAVA_INCLUDES : sourceIncludes;

        for (String compileRoot : compileSourceRoots) {
            File rootFolder = new File(compileRoot);
            String[] sources = doScan(includes, sourceExcludes, rootFolder);
            for (String src : sources) {
                javaSources.add(new File(rootFolder, src));
            }
        }

        return javaSources;
    }

    private Iterable<File> findFacesConfigFiles() {
        String[] files = doScan(XML_INCLUDES, EMPTY, new File(configRoot));
        return Collections2.transform(Arrays.asList(files), new StringToFile(configRoot));
    }

    CdkClassLoader createProjectClassLoader() {
        CdkClassLoader classLoader = null;

        try {
            // This Mojo executed befor process-resources phase, therefore we have to use original resource folders.
            List<File> urls = getClassPathElements();
            classLoader = new CdkClassLoader(urls, this.getClass().getClassLoader());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }

        return classLoader;
    }

    List<File> getClassPathElements() {
        List<File> files = new ArrayList<File>();

        files.add(new File(projectRoot, "target/classes"));
        files.add(new File(projectRoot, "target/dependency"));

        return files;
    }

    protected String[] doScan(String[] includes, String[] excludes, File rootFolder) {
        if (!rootFolder.exists()) {
            return new String[] {};
        }
        try {
            DirectoryScanner directoryScanner = new DirectoryScanner();

            directoryScanner.setFollowSymlinks(true);
            directoryScanner.setBasedir(rootFolder);
            directoryScanner.setExcludes(excludes);
            directoryScanner.setIncludes(includes);
            directoryScanner.addDefaultExcludes();
            directoryScanner.scan();

            return directoryScanner.getIncludedFiles();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Error scanning source root: \'" + rootFolder + "\'", e);
        }
    }

    private List<String> getDefaultTemplateIncludes() {
        List<String> includes = Lists.newLinkedList();
        for (String xmlInclude : XML_INCLUDES) {
            includes.add(DEFAULT_TEMPLATES_ROOT + xmlInclude);
        }
        return includes;
    }

    private class StringToFile implements Function<String, File> {

        private String root;

        public StringToFile(String root) {
            this.root = root;
        }

        @Override
        public File apply(String input) {
            return new File(root, input);
        }
    };
}
