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
package org.richfaces.builder.mojo;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.FileSet;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.richfaces.builder.maven.MavenLogger;
import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Generator;
import org.richfaces.cdk.Logger;
import org.richfaces.cdk.Outputs;
import org.richfaces.cdk.Sources;
import org.richfaces.cdk.apt.CdkProcessorImpl;
import org.richfaces.cdk.apt.LibraryCache;
import org.richfaces.cdk.model.Cacheable;

import org.apache.maven.plugins.annotations.Parameter;

import com.google.common.collect.Maps;

/**
 * @author asmirnov@exadel.com
 */
@Mojo(name="generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution= ResolutionScope.COMPILE)
public class GenerateMojo extends AbstractMojo {
    private static final String[] JAVA_INCLUDES = new String[] { "**/*.java" };
    private static final String MAIN_CONFIG = "src/main/config";
    private static final String MAIN_TEMPLATES = "src/main/templates";
    private static final String[] STRINGS_ARRAY = new String[0];
    private static final String XML_INCLUDES = "**/*.xml";
    /**
     * Project classpath.
     */
    @Parameter(property="project.compileClasspathElements", readonly = true, required = true)
    protected List<String> classpathElements;
    /**
     * The source directories containing the sources to be compiled.
     */
    @Parameter(property="project.compileSourceRoots", readonly = true, required = true)
    protected List<String> compileSourceRoots;
    /**
     * The list of JSF configuration files that will be processed by CDK. By default, CDK looks for all files in the
     * <code>src/main/config</code> folder with "xml" extension.
     */
    @Parameter
    protected FileSet[] facesConfigs;
    @Parameter
    protected Map<String, String> options = Maps.newHashMap();
    @Parameter
    protected Library library;
    /**
     * The directory for compiled classes.
     */
    @Parameter(property="project.build.outputDirectory", readonly = true, required = true)
    protected File outputDirectory;
    /**
     * Directory where the output Java Files will be located.
     */
    @Parameter(defaultValue="${project.build.directory}/generated-sources/main/java")
    protected File outputJavaDirectory;
    /**
     * Directory where the output Java Files will be located.
     */
    @Parameter(defaultValue="${project.build.directory}/generated-sources/main/resources")
    protected File outputResourcesDirectory;
    @Parameter(defaultValue="${project.build.directory}/generated-sources/test/java")
    protected File outputTestDirectory;
    /**
     * Directory where the output Java Files will be located.
     */
    @Parameter( defaultValue="${project.build.directory}/generated-sources/test/resources")
    protected File outputTestResourcesDirectory;
    /**
     * Directory where serialized library will be cached
     */
    @Parameter(defaultValue="${project.build.directory}/library-cache")
    protected File outputLibraryCache;
    /**
     * Forces compiler to do not use cache and re-compile all sources from scratch
     */
    @Parameter(property="cdk.recompile", defaultValue="false")
    protected boolean forceRecompile;
    /**
     * Turns off library generation and verification in case when no change was detected in sources which supports
     * {@link Cacheable} (it does not have to mean no change was done). Warning: when getting undesired results, try to turn off
     * this option.
     */
    @Parameter(property="cdk.cache.eagerly", defaultValue="false")
    protected boolean cacheEagerly;
    /**
     * Top maven project.
     */
    @Parameter(defaultValue="${project}", readonly = true)
    protected MavenProject project;
    /**
     * List of filename patterns that will be excluded from process by annotations processor. By default, all *.java files will
     * be processed.
     */
    @Parameter
    protected String[] sourceExcludes;
    /**
     * List of filename patterns that will be included to process by annotations processor. By default, all *.java files will be
     * processed.
     */
    @Parameter
    protected String[] sourceIncludes;
    /**
     * The list of JsfRenderer template files that will be processed by CDK. By default, CDK looks for all files in the
     * <code>src/main/templates</code> folder with "xml" extension.
     */
    @Parameter
    protected FileSet[] templates;
    @Parameter
    protected Map<String, String> workers;
    @Parameter
    protected String locale = Locale.getDefault().toString();
    @Parameter
    protected String charset = Charset.defaultCharset().name();

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if ("pom".equals(project.getPackaging())) {
            getLog().info("Skipping 'pom' packaging project: " + project.getModel().getId());
            return;
        }

        // Setup logger.
        Logger logger = new MavenLogger(getLog());

        Generator generator = new Generator();
        generator.setLog(logger);
        generator.setLoader(createProjectClassLoader(project));
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

        // configure CDK workers.
        setupPlugins(generator);

        options.put(LibraryCache.CACHE_ENABLED_OPTION, Boolean.toString(!forceRecompile));
        options.put(CdkProcessorImpl.CACHE_EAGERLY_OPTION, Boolean.toString(cacheEagerly));
        generator.setOptions(options);

        try {
            if (this.library != null && this.library.getTaglib() != null && this.library.getTaglib().getShortName() != null) {

                generator.setNamespace(this.library.getTaglib().getShortName());
            }

            generator.setLocale(localeFromString(locale));
            generator.setCharset(Charset.forName(charset));

            // Build JSF library.
            // LibraryBuilder builder = LibraryBuilder.createInstance(context);
            generator.init();
            generator.execute();
            if (logger.getErrorCount() > 0) {
                throw new MojoFailureException("Error occurred while JSF library was built", logger.getFirstError());
            }
            // Tell project about generated files.
            if (outputJavaDirectory.exists()) {
                project.addCompileSourceRoot(outputJavaDirectory.getAbsolutePath());
            }

            if (outputResourcesDirectory.exists()) {
                Resource resource = new Resource();

                resource.setDirectory(outputResourcesDirectory.getAbsolutePath());
                project.addResource(resource);
            }
            if (outputTestDirectory.exists()) {
                project.addTestCompileSourceRoot(outputTestDirectory.getAbsolutePath());
            }
            if (outputTestResourcesDirectory.exists()) {
                Resource testResource = new Resource();

                testResource.setDirectory(outputTestResourcesDirectory.getAbsolutePath());
                project.addTestResource(testResource);
            }
        } catch (CdkException e) {
            throw new MojoExecutionException("CDK build error", e);
        }
    }

    private Locale localeFromString(String locale) {
        String parts[] = locale.split("_", -1);
        if (parts.length == 1) return new Locale(parts[0]);
        else if (parts.length == 2
                || (parts.length == 3 && parts[2].startsWith("#")))
            return new Locale(parts[0], parts[1]);
        else return new Locale(parts[0], parts[1], parts[2]);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param generator
     * @throws MojoFailureException
     */
    private void setupPlugins(Generator generator) throws MojoFailureException {
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

    private File resolveRelativePath(File file) {
        File result = file;
        if (!result.isAbsolute()) {
            result = new File(project.getBasedir(), result.getPath());
        }

        return result;
    }

    private Iterable<File> findTemplateFiles() throws MojoExecutionException {
        if (null == templates) {
            File defaultDirectory = resolveRelativePath(new File(MAIN_TEMPLATES));

            if (defaultDirectory.exists() && defaultDirectory.isDirectory()) {
                FileSet fileSet = new FileSet();

                fileSet.setDirectory(MAIN_TEMPLATES);
                fileSet.addInclude(XML_INCLUDES);
                templates = new FileSet[] { fileSet };
            }
        }

        return doScan(templates);
    }

    private Iterable<File> findJavaFiles() throws MojoExecutionException {
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

    private Iterable<File> findFacesConfigFiles() throws MojoExecutionException {
        if (null == facesConfigs) {
            File defaultDirectory = resolveRelativePath(new File(MAIN_CONFIG));

            if (defaultDirectory.exists() && defaultDirectory.isDirectory()) {
                FileSet fileSet = new FileSet();

                fileSet.setDirectory(MAIN_CONFIG);
                fileSet.addInclude(XML_INCLUDES);
                facesConfigs = new FileSet[] { fileSet };
            }
        }

        return doScan(facesConfigs);
    }

    protected CdkClassLoader createProjectClassLoader(MavenProject project) {
        CdkClassLoader classLoader = null;

        try {
            // This Mojo executed befor process-resources phase, therefore we have to use original resource folders.
            List<Resource> resources = project.getResources();
            List<File> urls = new ArrayList<File>(classpathElements.size() + resources.size());
            for (Resource resource : resources) {
                String directory = resource.getDirectory();
                // TODO - use includes/excludes and target path.
                urls.add(resolveRelativePath(new File(directory)));
            }
            for (Iterator<String> iter = classpathElements.iterator(); iter.hasNext();) {
                String element = iter.next();

                urls.add(new File(element));
            }

            classLoader = new CdkClassLoader(urls);
        } catch (MalformedURLException e) {
            getLog().error("Bad URL in classpath", e);
        }

        return classLoader;
    }

    protected String[] doScan(String[] includes, String[] excludes, File rootFolder) throws MojoExecutionException {
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
            throw new MojoExecutionException("Error scanning source root: \'" + rootFolder + "\'", e);
        }
    }

    /**
     * Skan Array of filesets for selected resources.
     *
     * @param filesets
     * @return
     * @throws MojoExecutionException
     */
    @SuppressWarnings("unchecked")
    protected Collection<File> doScan(FileSet[] filesets) throws MojoExecutionException {
        List<File> files = new ArrayList<File>();

        if (null != filesets) {
            for (FileSet fileSet : filesets) {
                String[] includes = (String[]) fileSet.getIncludes().toArray(STRINGS_ARRAY);
                String[] excludes = (String[]) fileSet.getExcludes().toArray(STRINGS_ARRAY);
                File fileSetDirectory = resolveRelativePath(new File(fileSet.getDirectory()));
                String[] scan = doScan(includes, excludes, fileSetDirectory);

                for (String filename : scan) {
                    files.add(resolveRelativePath(new File(fileSetDirectory, filename)));
                }
            }
        }

        return files;
    }
}
