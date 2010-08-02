/**
 * License Agreement.
 *
 * YUI Compressor Maven Mojo
 *
 * Copyright (C) 2007 Alchim31 Team
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



package net.sf.alchim.mojo.yuicompressor;

import java.io.File;

import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Common class for mojos.
 *
 * @author David Bernard
 * @created 2007-08-29
 */

//@SuppressWarnings("unchecked")
public abstract class MojoSupport extends AbstractMojo {
    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * list of additionnal excludes
     *
     * @parameter
     */
    private List<String> excludes;

    /**
     * define if plugin must stop/fail on warnings.
     *
     * @parameter expression="${maven.yuicompressor.failOnWarning}"
     *            default-value="false"
     */
    protected boolean failOnWarning;

    /**
     * list of additionnal includes
     *
     * @parameter
     */
    private List<String> includes;
    protected ErrorReporter4Mojo jsErrorReporter_;

    /**
     * [js only] Display possible errors in the code
     *
     * @parameter expression="${maven.yuicompressor.jswarm}"
     *            default-value="true"
     */
    protected boolean jswarn;

    /**
     * The output directory into which to copy the resources.
     *
     * @parameter default-value="${project.build.outputDirectory}"
     */
    protected File outputDirectory;

    /**
     * @parameter expression="${project}"
     * @readonly
     * @required
     */
    protected MavenProject project;

    /**
     * The list of resources we want to transfer.
     *
     * @parameter default-value="${project.resources}"
     */
    private List<Resource> resources;

    /**
     * Javascript source directory. (result will be put to outputDirectory).
     * This allow project with "src/main/js" structure.
     *
     * @parameter default-value="${project.build.sourceDirectory}/../js"
     */
    private File sourceDirectory;

    /**
     * Single directory for extra files to include in the WAR.
     *
     * @parameter default-value="${basedir}/src/main/webapp"
     */
    private File warSourceDirectory;

    /**
     * The directory where the webapp is built.
     *
     * @parameter default-value="${project.build.directory}/${project.build.finalName}"
     */
    protected File webappDirectory;

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            if (failOnWarning) {
                jswarn = true;
            }

            jsErrorReporter_ = new ErrorReporter4Mojo(getLog(), jswarn);
            beforeProcess();
            processDir(sourceDirectory, outputDirectory, null, null, true);

            for (Resource resource : resources) {
                File destRoot = outputDirectory;

                if (resource.getTargetPath() != null) {
                    destRoot = new File(outputDirectory, resource.getTargetPath());
                }

                processDir(new File(resource.getDirectory()), destRoot, resource.getIncludes(), resource.getExcludes(),
                           true);
            }

            processDir(warSourceDirectory, webappDirectory, null, null, false);
            afterProcess();
            getLog().info(String.format("nb warnings: %d, nb errors: %d", jsErrorReporter_.getWarningCnt(),
                                        jsErrorReporter_.getErrorCnt()));

            if (failOnWarning && (jsErrorReporter_.getWarningCnt() > 0)) {
                throw new MojoFailureException("warnings on " + this.getClass().getSimpleName()
                                               + "=> failure ! (see log)");
            }
        } catch (RuntimeException exc) {
            throw exc;
        } catch (MojoFailureException exc) {
            throw exc;
        } catch (MojoExecutionException exc) {
            throw exc;
        } catch (Exception exc) {
            throw new MojoExecutionException("wrap: " + exc.getMessage(), exc);
        }
    }

    protected abstract String[] getDefaultIncludes() throws Exception;

    protected abstract void beforeProcess() throws Exception;

    protected abstract void afterProcess() throws Exception;

    protected void processDir(File srcRoot, File destRoot, List<String> srcIncludes, List<String> srcExcludes,
                              boolean destAsSource)
            throws Exception {
        if ((srcRoot == null) || (!srcRoot.exists())) {
            return;
        }

        if (destRoot == null) {
            throw new MojoFailureException("destination directory for " + srcRoot + " is null");
        }

        DirectoryScanner scanner = new DirectoryScanner();

        scanner.setBasedir(srcRoot);

        if ((srcIncludes != null) && !srcIncludes.isEmpty()) {
            scanner.setIncludes(srcIncludes.toArray(EMPTY_STRING_ARRAY));
        }

        if ((includes != null) && !includes.isEmpty()) {
            scanner.setIncludes(includes.toArray(EMPTY_STRING_ARRAY));
        } else {
            scanner.setIncludes(getDefaultIncludes());
        }

        if ((srcExcludes != null) && !srcExcludes.isEmpty()) {
            scanner.setExcludes(srcExcludes.toArray(EMPTY_STRING_ARRAY));
        }

        if ((excludes != null) && !excludes.isEmpty()) {
            scanner.setExcludes(excludes.toArray(EMPTY_STRING_ARRAY));
        }

        scanner.addDefaultExcludes();
        scanner.scan();

        for (String name : scanner.getIncludedFiles()) {
            SourceFile src = new SourceFile(srcRoot, destRoot, name, destAsSource);

            jsErrorReporter_.setDefaultFileName("..."
                    + src.toFile().getAbsolutePath().substring(project.getBasedir().getAbsolutePath().length()));
            processFile(src);
        }
    }

    protected abstract void processFile(SourceFile src) throws Exception;
}
