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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.io.File;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Parent;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;

import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

/**
 * Compile all xml templates, matched given pattern to Java classes. Sources
 * will be created in {@link AbstractCDKMojo#outputJavaDirectory}
 *
 * @goal compile
 * @requiresDependencyResolution compile
 * @phase generate-sources
 * @author shura
 *
 */
public class CompileMojo extends AbstractCDKMojo implements Contextualizable {

    /**
     * To look up Archiver/UnArchiver implementations
     *
     * @component
     */
    private ArchiverManager archiverManager;
    private PlexusContainer container;

    /**
     * @parameter default-value=${project.groupId}
     */
    private String defaultPackage;

    /**
     * A list of exclusion filters for the compiler. None by default.
     *
     * @parameter
     */
    private String[] excludes;

    /**
     * Project executed by first compile lifecycle.
     *
     * @parameter expression="${executedProject}"
     * @readonly
     */
    private MavenProject executedProject;

    /**
     * A list of inclusion filters for the compiler. By default, include all
     * files in templates directory.
     *
     * @parameter
     */
    private String[] includes;

    /**
     * The local repository.
     *
     * @parameter expression="${localRepository}"
     */
    private ArtifactRepository localRepository;

    /**
     * Project builder
     *
     * @component
     */
    private MavenProjectBuilder mavenProjectBuilder;

    /**
     * The reactor projects.
     *
     * @parameter expression="${project.parent}"
     * @readonly
     */
    private MavenProject parentProject;

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        // VelocityTemplates.init();
        try {
            List components = container.lookupList("org.richfaces.templatecompiler.elements.ElementsFactory");

            for (Iterator iter = components.iterator(); iter.hasNext(); ) {
                Object element = iter.next();

                System.out.println(element.getClass().getName());
            }

            System.out.println("Components Map");

            Map componentsMap = container.lookupMap("org.richfaces.templatecompiler.elements.ElementsFactory");

            for (Iterator iter = componentsMap.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry element = (Map.Entry) iter.next();

                System.out.println(element.getKey() + ":" + element.getValue().getClass().getName());
            }
        } catch (ComponentLookupException e) {
            throw new MojoExecutionException("Error lookup ElementFactory components");
        }

        Parent parentModel = project.getModel().getParent();

        if (null != parentModel) {
            String relativePath = parentModel.getRelativePath();
            File parentPom = new File(project.getFile().getParentFile(), relativePath);

            if (parentPom.isDirectory()) {
                parentPom = new File(parentPom, "pom.xml");
            }

            if (parentPom.exists()) {
                try {
                    parentProject = mavenProjectBuilder.build(parentPom, localRepository, null);
                } catch (ProjectBuildingException e) {
                    throw new MojoExecutionException("Error get parent project for a components library", e);
                }
            } else {
                throw new MojoFailureException("Parent project pom file " + parentPom.getAbsolutePath()
                                               + " is not found for a components library");
            }
        } else {
            throw new MojoFailureException("Components library project must have parent pom with components modules");
        }

        getLog().info("Parent Project object :\n" + toLog(parentProject) + "\n");
        getLog().info("Project object :\n" + toLog(project) + "\n");
        getLog().info("Project object Model :\n" + toLog(project.getModel()) + "\n");
        getLog().info("Project object Parent Model :\n" + toLog(project.getModel().getParent()) + "\n");
        getLog().info("Executed Project object :\n" + toLog(executedProject) + "\n");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable#contextualize(org.codehaus.plexus.context.Context)
     */
    public void contextualize(Context context) throws ContextException {
        this.container = (PlexusContainer) context.get(PlexusConstants.PLEXUS_KEY);
    }

    private void logBean(Object bean) {
        StringBuffer content = new StringBuffer();
    }

    /**
     * Convert any Java Object to JavaScript representation ( as possible ).
     *
     * @param obj
     * @return
     * @throws MojoExecutionException
     */
    public String toLog(Object obj) throws MojoExecutionException {
        if (null == obj) {
            return "null";
        } else if (obj.getClass().isArray()) {
            StringBuffer ret = new StringBuffer("[");
            boolean first = true;

            for (int i = 0; i < Array.getLength(obj); i++) {
                Object element = Array.get(obj, i);

                if (!first) {
                    ret.append(',');
                }

                ret.append(toLog(element));
                first = false;
            }

            return ret.append("]\n").toString();
        } else if (obj instanceof Collection) {

            // Collections put as JavaScript array.
            Collection collection = (Collection) obj;
            StringBuffer ret = new StringBuffer("[");
            boolean first = true;

            for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                Object element = iter.next();

                if (!first) {
                    ret.append(',');
                }

                ret.append(toLog(element));
                first = false;
            }

            return ret.append("]\n").toString();
        } else if (obj instanceof Map) {

            // Maps put as JavaScript hash.
            Map map = (Map) obj;
            StringBuffer ret = new StringBuffer("{");
            boolean first = true;

            for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
                Object key = (Object) iter.next();

                if (!first) {
                    ret.append(',');
                }

                ret.append(key);
                ret.append(":");
                ret.append(toLog(map.get(key)));
                first = false;
            }

            return ret.append("}\n").toString();
        } else if (obj instanceof Number || obj instanceof Boolean) {

            // numbers and boolean put as-is, without conversion
            return obj.toString();
        } else if (obj instanceof String) {

            // all other put as encoded strings.
            StringBuffer ret = new StringBuffer();

            addEncodedString(ret, obj);

            return ret.append("\n").toString();
        }

        // All other objects threaded as Java Beans.
        try {
            StringBuffer ret = new StringBuffer("{");
            PropertyDescriptor[] propertyDescriptors =
                Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();
            boolean first = true;

            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                String key = propertyDescriptor.getName();

                if (!"class".equals(key) && propertyDescriptor.getReadMethod() != null) {
                    if (!first) {
                        ret.append(",\n\t");
                    }

                    addEncodedString(ret, key);
                    ret.append(":");

                    try {
                        Object value = propertyDescriptor.getReadMethod().invoke(obj, new Object[] {});

                        propertyDescriptor.getReadMethod();
                        ret.append(String.valueOf(value));
                    } catch (InvocationTargetException e) {
                        ret.append("null");
                    } // ret.append(toLog(PropertyUtils.getProperty(obj, key)));

                    first = false;
                }
            }

            return ret.append("}\n").toString();
        } catch (Exception e) {
            throw new MojoExecutionException("Error in conversion Java Object to String", e);
        }
    }

    public void addEncodedString(StringBuffer buff, Object obj) {
        buff.append("'");
        buff.append(obj);
        buff.append("'");
    }
}
