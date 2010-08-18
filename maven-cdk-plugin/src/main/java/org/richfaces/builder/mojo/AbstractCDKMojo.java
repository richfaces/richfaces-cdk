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



package org.richfaces.builder.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;

import java.lang.reflect.Method;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shura
 *
 */
public abstract class AbstractCDKMojo extends AbstractMojo {

    /**
     * Project classpath.
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    protected List classpathElements;

    /**
     * The source directories containing the sources to be compiled.
     *
     * @parameter expression="${project.compileSourceRoots}"
     * @required
     * @readonly
     */
    protected List<String> compileSourceRoots;

    /**
     * Place for component configuration XML files. All '*.xml' files wil be
     * parsed as components config. All '*.ent' files will be processed as
     * include configurations.
     *
     * @parameter expression="src/main/config/component"
     */
    protected File componentConfigDirectory;

    /**
     * Place for converter configuration XML files. All '*.xml' files wil be
     * parsed as components config. All '*.ent' files will be processed as
     * include configurations.
     *
     * @parameter expression="src/main/config/converter"
     */
    protected File converterConfigDirectory;

    /**
     * Place for faces configuration XML files
     *
     * @parameter expression="src/main/config/faces"
     */
    protected File facesConfigInclude;

    /**
     *
     * @parameter
     */
    protected String key;

    /**
     * @parameter
     */
    protected Library library;

    /**
     * The directory for compiled classes.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    protected File outputDirectory;

    /**
     * Directory where the output Java Files will be located.
     *
     * @parameter expression="${project.build.directory}/generated-component/java"
     */
    protected File outputJavaDirectory;

    /**
     * Directory where the output Java Files will be located.
     *
     * @parameter expression="${project.build.directory}/generated-component/resources"
     */
    protected File outputResourcesDirectory;

    /**
     * @parameter expression="${project.build.directory}/generated-component/test"
     */
    protected File outputTestsDirectory;

    /**
     * Top maven project.
     *
     * @parameter expression="${project}"
     * @readonly
     */
    protected MavenProject project;

    /**
     * Place for component configuration XML files
     *
     * @parameter expression="src/main/config/resources"
     */
    protected File resourcesInclude;

    /**
     * Place for component configuration XML files
     *
     * @parameter expression="src/main/config/taglib"
     */
    protected File taglibInclude;

    /**
     *
     * @parameter expression="src/main/templates"
     */
    protected File templatesDirectory;

    /**
     * Place for validator configuration XML files. All '*.xml' files wil be
     * parsed as component config. All '*.ent' files will be processed as
     * include configurations.
     *
     * @parameter expression="src/main/config/validator"
     */
    protected File validatorConfigDirectory;

    /**
     * Check library configuration, and fill all empty values to default.
     *
     * @return
     */
    protected Taglib checkLibraryConfig() {
        if (null != library) {
            getLog().debug("Library prefix is " + library.getPrefix());
        } else {
            library = new Library();
        }

        if (null == library.getPrefix()) {
            library.setPrefix(project.getGroupId());
        }

        getLog().debug("Default prefix for a generated packages: " + library.getPrefix());

        if (null == library.getDescription()) {
            library.setDescription(project.getDescription());
        }

        getLog().debug("Library description: " + library.getDescription());

        if (null == library.getJsfVersion()) {
            String version = Library.JSF11;

            // Check version-specific methods in UIComponent class
            try {
                Class<?> componentClass = createProjectClassLoader(project,
                                              false).loadClass("javax.faces.component.UIComponent");
                Method[] methods = componentClass.getDeclaredMethods();

                for (int i = 0; i < methods.length; i++) {
                    if ("encodeAll".equals(methods[i].getName())) {
                        version = Library.JSF12;

                        break;
                    }
                }
            } catch (ClassNotFoundException e) {

                // Ignore - by defaule, generate codes for JSF 1.1
            }

            library.setJsfVersion(version);
        }

        // velocity = new DefaultVelocityComponent();
        getLog().debug("Generate files for a JSF " + library.getJsfVersion());

        Renderkit[] renderkits = library.getRenderkits();

        if (null != renderkits) {
            for (int i = 0; i < renderkits.length; i++) {
                Renderkit renderkit = renderkits[i];

                getLog().debug("Renderkit name is " + renderkit.getName());

                if (null == renderkit.getPackage()) {
                    renderkit.setPackage(library.getPrefix() + ".renderkit." + renderkit.getName().toLowerCase());
                }
            }
        } else {
            renderkits = new Renderkit[1];

            Renderkit renderkit = new Renderkit();

            renderkit.setMarkup("html");
            renderkit.setName("HTML_BASIC");
            renderkit.setPackage(library.getPrefix() + ".renderkit." + renderkit.getName().toLowerCase());
            renderkits[0] = renderkit;
            library.setRenderkits(renderkits);
        }

        Taglib taglib = library.getTaglib();

        if (null != taglib) {
            getLog().debug("Taglib uri is " + taglib.getUri());
            getLog().debug("Taglib shortname is " + taglib.getShortName());
        } else {
            taglib = new Taglib();
            library.setTaglib(taglib);
        }

        if (null == taglib.getDisplayName()) {
            taglib.setDisplayName(project.getDescription());
        }

        if (null == taglib.getJspVersion()) {

            // Jsf 1.2 can use JSP 2.1 only, other - 2.0
            taglib.setJspVersion(library.getJsfVersion().equals(Library.JSF12) ? "2.1" : "1.2");
        }

        if (null == taglib.getUri()) {
            String url = project.getUrl();

            if (null == url) {
                url = "http://";

                String[] parts = project.getGroupId().split(".");

                for (int i = parts.length - 1; i >= 0; i--) {
                    url = url + parts[i];

                    if (i > 0) {
                        url = url + ".";
                    }
                }

                url = url + "/" + project.getArtifactId();
            }

            taglib.setUri(url);
        }

        if (null == taglib.getShortName()) {
            taglib.setShortName(project.getArtifactId());
        }

        if (null == taglib.getTaglib()) {
            taglib.setTaglib(taglib.getShortName());
        }

        if (null == taglib.getTlibVersion()) {
            taglib.setTlibVersion(createTaglibVersionFromProjectVersion());
        }

        getLog().debug("Taglib uri is " + taglib.getUri());
        getLog().debug("Taglib shortname is " + taglib.getShortName());

        if (null != library.getTaglibs() && library.getTaglibs().length > 0) {
            for (int i = 0; i < library.getTaglibs().length; i++) {
                Taglib t = library.getTaglibs()[i];

                checkTaglib(t);
            }
        }

        return taglib;
    }

    private String createTaglibVersionFromProjectVersion() {
        Artifact artifact = project.getArtifact();
        String version = artifact.getVersion();
        Matcher matcher = Pattern.compile("^(\\d+(?:\\.\\d+)*)").matcher(version);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "1.2";
    }

    protected ClassLoader createProjectClassLoader(MavenProject project, boolean useCCL) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            List<?> compileClasspathElements = project.getCompileClasspathElements();
            String outputDirectory = project.getBuild().getOutputDirectory();
            URL[] urls = new URL[compileClasspathElements.size() + 1];
            int i = 0;

            urls[i++] = new File(outputDirectory).toURI().toURL();

            for (Iterator<?> iter = compileClasspathElements.iterator(); iter.hasNext(); ) {
                String element = (String) iter.next();

                urls[i++] = new File(element).toURI().toURL();
            }

            if (useCCL) {
                classLoader = new URLClassLoader(urls, classLoader);
            } else {
                classLoader = new URLClassLoader(urls);
            }
        } catch (MalformedURLException e) {
            getLog().error("Bad URL in classpath", e);
        } catch (DependencyResolutionRequiredException e) {
            getLog().error("Dependencies not resolved ", e);
        }

        return classLoader;
    }

    protected ClassLoader createProjectClassLoader(MavenProject project) {
        return createProjectClassLoader(project, true);
    }

    protected void checkTaglib(Taglib taglib) {
        if (null == taglib.getDisplayName()) {
            taglib.setDisplayName(library.getTaglib().getDisplayName());
        }

        if (null == taglib.getShortName()) {
            taglib.setShortName(library.getTaglib().getShortName());
        }

        if (null == taglib.getJspVersion()) {
            taglib.setJspVersion(library.getTaglib().getJspVersion());
        }

        if (null == taglib.getUri()) {
            taglib.setUri(library.getTaglib().getUri() + "/" + taglib.getShortName());
        }

        if (null == taglib.getTaglib()) {
            taglib.setTaglib(taglib.getShortName());
        }

        if (null == taglib.getTlibVersion()) {
            taglib.setTlibVersion(createTaglibVersionFromProjectVersion());
        }

        getLog().debug("Taglib uri is " + taglib.getUri());
        getLog().debug("Taglib shortname is " + taglib.getShortName());
        getLog().debug("Taglib version is " + taglib.getTlibVersion());
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
}
