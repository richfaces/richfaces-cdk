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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * @author shura
 *
 */
public abstract class AbstractGenerateMojo extends AbstractMojo {
    /**
     * Project classpath.
     *
     * @parameter expression="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    protected List<String> classpathElements;
    /**
     * The source directories containing the sources to be compiled.
     *
     * @parameter expression="${project.compileSourceRoots}"
     * @required
     * @readonly
     */
    protected List<String> compileSourceRoots;
    /**
     * Place for component configuration XML files. All '*.xml' files wil be parsed as components config. All '*.ent' files will
     * be processed as include configurations.
     *
     * @parameter expression="src/main/config/component"
     */
    protected File componentConfigDirectory;
    /**
     * Place for converter configuration XML files. All '*.xml' files wil be parsed as components config. All '*.ent' files will
     * be processed as include configurations.
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
     * @parameter expression="${project.build.directory}/generated-component/test-resources"
     */
    protected File outputTestsResourcesDirectory;
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
     * Place for validator configuration XML files. All '*.xml' files wil be parsed as component config. All '*.ent' files will
     * be processed as include configurations.
     *
     * @parameter expression="src/main/config/validator"
     */
    protected File validatorConfigDirectory;

    protected ClassLoader createProjectClassLoader(MavenProject project, boolean useCCL) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            URL[] urls = new URL[classpathElements.size() + 1];
            int i = 0;

            urls[i++] = outputDirectory.toURI().toURL();

            for (Iterator<?> iter = classpathElements.iterator(); iter.hasNext();) {
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
        }

        return classLoader;
    }

    protected ClassLoader createProjectClassLoader(MavenProject project) {
        return createProjectClassLoader(project, true);
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
