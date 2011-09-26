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

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.richfaces.application.Module;
import org.richfaces.application.ServiceException;
import org.richfaces.application.ServiceLoader;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.ServicesFactoryImpl;
import org.richfaces.cdk.concurrent.CountingExecutorCompletionService;
import org.richfaces.cdk.faces.FacesImpl;
import org.richfaces.cdk.naming.FileNameMapperImpl;
import org.richfaces.cdk.resource.handler.impl.DynamicResourceHandler;
import org.richfaces.cdk.resource.handler.impl.StaticResourceHandler;
import org.richfaces.cdk.resource.scan.ResourcesScanner;
import org.richfaces.cdk.resource.scan.impl.DynamicResourcesScanner;
import org.richfaces.cdk.resource.scan.impl.ResourceOrderingScanner;
import org.richfaces.cdk.resource.scan.impl.StaticResourcesScanner;
import org.richfaces.cdk.resource.util.ResourceUtil;
import org.richfaces.cdk.resource.writer.ResourceProcessor;
import org.richfaces.cdk.resource.writer.impl.CSSResourceProcessor;
import org.richfaces.cdk.resource.writer.impl.JavaScriptResourceProcessor;
import org.richfaces.cdk.resource.writer.impl.ResourceWriterImpl;
import org.richfaces.cdk.task.ResourceTaskFactoryImpl;
import org.richfaces.cdk.util.MoreConstraints;
import org.richfaces.cdk.util.MorePredicates;
import org.richfaces.cdk.vfs.VFS;
import org.richfaces.cdk.vfs.VFSRoot;
import org.richfaces.cdk.vfs.VirtualFile;
import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceFactoryImpl;
import org.richfaces.resource.ResourceKey;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Constraints;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * @goal process
 * @requiresDependencyResolution compile
 * @phase process-classes
 */
public class ProcessMojo extends AbstractMojo {
    private static final URL[] EMPTY_URL_ARRAY = new URL[0];
    private static final Function<String, Predicate<CharSequence>> REGEX_CONTAINS_BUILDER_FUNCTION = new Function<String, Predicate<CharSequence>>() {
        public Predicate<CharSequence> apply(String from) {
            Predicate<CharSequence> containsPredicate = Predicates.containsPattern(from);
            return Predicates.and(Predicates.notNull(), containsPredicate);
        }

        ;
    };
    private static final Function<Resource, String> CONTENT_TYPE_FUNCTION = new Function<Resource, String>() {
        public String apply(Resource from) {
            return from.getContentType();
        }

        ;
    };
    private static final Function<Resource, String> RESOURCE_QUALIFIER_FUNCTION = new Function<Resource, String>() {
        public String apply(Resource from) {
            return ResourceUtil.getResourceQualifier(from);
        }

        ;
    };
    private final Function<String, URL> filePathToURL = new Function<String, URL>() {
        public URL apply(String from) {
            try {
                File file = new File(from);
                if (file.exists()) {
                    return file.toURI().toURL();
                }
            } catch (MalformedURLException e) {
                getLog().error("Bad URL in classpath", e);
            }

            return null;
        }

        ;
    };
    /**
     * @parameter
     * @required
     */
    private String outputDir;
    /**
     * @parameter
     * @required
     */
    // TODO handle base skins
    private String[] skins;
    /**
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;
    /**
     * @parameter
     */
    private List<String> includedContentTypes;
    /**
     * @parameter
     */
    private List<String> excludedContentTypes;
    /**
     * @parameter
     */
    private List<String> includedFiles;
    /**
     * @parameter
     */
    private List<String> excludedFiles;
    /**
     * @parameter
     */
    private boolean compress = true;
    /**
     * @parameter
     */
    private boolean pack = false;
    /**
     * @parameter
     */
    // TODO review usage of properties?
    private FileNameMapping[] fileNameMappings = new FileNameMapping[0];
    /**
     * @parameter
     */
    private ProcessMode processMode = ProcessMode.embedded;
    /**
     * @parameter expression="${basedir}/src/main/webapp"
     */
    private String webRoot;
    /**
     * @parameter expression="${encoding}" default-value="${project.build.sourceEncoding}"
     */
    private String encoding;
    // TODO handle resource locales
    private Locale resourceLocales;
    private Collection<ResourceKey> foundResources = Sets.newHashSet();
    private Ordering<ResourceKey> resourceOrdering;
    private Set<ResourceKey> resourcesWithKnownOrder;
    

    public static final ResourceKey JSF_UNCOMPRESSED = new ResourceKey("jsf-uncompressed.js", "javax.faces");

    // TODO executor parameters
    private static ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(1);
    }

    private Collection<ResourceProcessor> getDefaultResourceProcessors() {
        if (!compress) {
            return Arrays.asList();
        }
        Charset charset = Charset.defaultCharset();
        if (!Strings.isNullOrEmpty(encoding)) {
            charset = Charset.forName(encoding);
        } else {
            getLog().warn(
                    "Encoding is not set explicitly, CDK resources plugin will use default platform encoding for processing char-based resources");
        }
        return Arrays.<ResourceProcessor>asList(new JavaScriptResourceProcessor(charset, getLog()), new CSSResourceProcessor(
                charset));
    }

    private Predicate<Resource> createResourcesFilter() {
        Predicate<CharSequence> qualifierPredicate = MorePredicates.compose(includedFiles, excludedFiles,
                REGEX_CONTAINS_BUILDER_FUNCTION);

        Predicate<Resource> qualifierResourcePredicate = Predicates.compose(qualifierPredicate, RESOURCE_QUALIFIER_FUNCTION);

        Predicate<CharSequence> contentTypePredicate = MorePredicates.compose(includedContentTypes, excludedContentTypes,
                REGEX_CONTAINS_BUILDER_FUNCTION);
        Predicate<Resource> contentTypeResourcePredicate = Predicates.compose(contentTypePredicate, CONTENT_TYPE_FUNCTION);

        return Predicates.and(qualifierResourcePredicate, contentTypeResourcePredicate);
    }

    private URL resolveWebRoot() throws MalformedURLException {
        File result = new File(webRoot);
        if (!result.exists()) {
            result = new File(project.getBasedir(), webRoot);
        }
        if (!result.exists()) {
            return null;
        }

        return result.toURI().toURL();
    }

    private void scanDynamicResources(Collection<VFSRoot> cpFiles, ResourceFactory resourceFactory) throws Exception {
        ResourcesScanner scanner = new DynamicResourcesScanner(cpFiles, resourceFactory);
        scanner.scan();
        foundResources.addAll(scanner.getResources());
    }

    private void scanStaticResources(Collection<VirtualFile> resourceRoots) throws Exception {
        ResourcesScanner scanner = new StaticResourcesScanner(resourceRoots);
        scanner.scan();
        foundResources.addAll(scanner.getResources());
    }
    
    private void scanResourceOrdering(Collection<VFSRoot> cpFiles) throws Exception {
        ResourceOrderingScanner scanner = new ResourceOrderingScanner(cpFiles);
        scanner.scan();
        resourceOrdering = scanner.getCompleteOrdering();
        resourcesWithKnownOrder = Sets.newLinkedHashSet(scanner.getResources());
    }

    private Collection<VFSRoot> fromUrls(Iterable<URL> urls) throws URISyntaxException, IOException {
        Collection<VFSRoot> result = Lists.newArrayList();

        for (URL url : urls) {
            if (url == null) {
                continue;
            }

            VFSRoot vfsRoot = VFS.getRoot(url);
            vfsRoot.initialize();
            result.add(vfsRoot);
        }

        return result;
    }

    private Collection<VFSRoot> getClasspathVfs(URL[] urls) throws URISyntaxException, IOException {
        return fromUrls(Arrays.asList(urls));
    }

    private Collection<VFSRoot> getWebrootVfs() throws URISyntaxException, IOException {
        return fromUrls(Collections.singletonList(resolveWebRoot()));
    }

    protected URL[] getProjectClassPath() {
        try {
            List<String> classpath = Constraints.constrainedList(Lists.<String>newArrayList(),
                    MoreConstraints.cast(String.class));
            classpath.addAll((List<String>) project.getCompileClasspathElements());
            classpath.add(project.getBuild().getOutputDirectory());

            URL[] urlClasspath = filter(transform(classpath, filePathToURL), notNull()).toArray(EMPTY_URL_ARRAY);
            return urlClasspath;
        } catch (DependencyResolutionRequiredException e) {
            getLog().error("Dependencies not resolved ", e);
        }

        return new URL[0];
    }

    protected ClassLoader createProjectClassLoader(URL[] cp) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        classLoader = new URLClassLoader(cp, classLoader);
        return classLoader;
    }
    
    private void initializerServiceTracker() {
        ServicesFactoryImpl servicesFactory = new ServicesFactoryImpl();
        ServiceTracker.setFactory(servicesFactory);
        
        ArrayList<Module> modules = new ArrayList<Module>();
        modules.add(new FakeModule());
        try {
            modules.addAll(ServiceLoader.loadServices(Module.class));
            servicesFactory.init(modules);
        } catch (ServiceException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private void reorderFoundResources(Collection<VFSRoot> cpResources, DynamicResourceHandler dynamicResourceHandler, ResourceFactory resourceFactory) throws Exception {
        Faces faces = new FacesImpl(null, new FileNameMapperImpl(fileNameMappings), dynamicResourceHandler);
        faces.start();
        
        initializerServiceTracker();
        
        foundResources = new ResourceLibraryExpander().expandResourceLibraries(foundResources);
        
        faces.startRequest();
        scanResourceOrdering(cpResources);
        faces.stopRequest();
        faces.stop();
        
        foundResources = resourceOrdering.sortedCopy(foundResources);
        
        foundResources.remove(JSF_UNCOMPRESSED);
        getLog().debug("foundResources: " + foundResources);
        
        
        resourcesWithKnownOrder.add(JSF_UNCOMPRESSED);
        getLog().debug("resourcesWithKnownOrder: " + resourcesWithKnownOrder);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        Faces faces = null;
        ExecutorService executorService = null;

        Collection<VFSRoot> webResources = null;
        Collection<VFSRoot> cpResources = null;

        try {
            URL[] projectCP = getProjectClassPath();
            ClassLoader projectCL = createProjectClassLoader(projectCP);
            Thread.currentThread().setContextClassLoader(projectCL);

            webResources = getWebrootVfs();
            cpResources = getClasspathVfs(projectCP);

            Collection<VirtualFile> resourceRoots = ResourceUtil.getResourceRoots(cpResources, webResources);
            scanStaticResources(resourceRoots);
            StaticResourceHandler staticResourceHandler = new StaticResourceHandler(resourceRoots);
            ResourceFactory resourceFactory = new ResourceFactoryImpl(staticResourceHandler);

            scanDynamicResources(cpResources, resourceFactory);

            DynamicResourceHandler dynamicResourceHandler = new DynamicResourceHandler(staticResourceHandler, resourceFactory);
            
            if (pack) {
                reorderFoundResources(cpResources, dynamicResourceHandler, resourceFactory);
            }
            
            File resourceOutputDir = new File(outputDir);
            if (!resourceOutputDir.exists()) {
                resourceOutputDir = new File(project.getBuild().getDirectory(), outputDir);
            }
            
            File resourceMappingDir = new File(project.getBuild().getOutputDirectory());

            faces = new FacesImpl(null, new FileNameMapperImpl(fileNameMappings), dynamicResourceHandler);
            faces.start();
            
            ResourceWriterImpl resourceWriter = new ResourceWriterImpl(resourceOutputDir, resourceMappingDir,
                    getDefaultResourceProcessors(), getLog(), resourcesWithKnownOrder);
            ResourceTaskFactoryImpl taskFactory = new ResourceTaskFactoryImpl(faces, pack);
            taskFactory.setResourceWriter(resourceWriter);

            executorService = createExecutorService();
            CompletionService<Object> completionService = new CountingExecutorCompletionService<Object>(executorService);
            taskFactory.setCompletionService(completionService);
            taskFactory.setSkins(skins);
            taskFactory.setLog(getLog());
            taskFactory.setFilter(createResourcesFilter());
            taskFactory.submit(foundResources);

            Future<Object> future = null;
            while (true) {
                future = completionService.take();
                if (future != null) {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        // TODO: handle exception
                        e.getCause().printStackTrace();
                    }
                } else {
                    break;
                }
            }

            resourceWriter.writeProcessedResourceMappings();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {

            if (cpResources != null) {
                for (VFSRoot vfsRoot : cpResources) {
                    try {
                        vfsRoot.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            if (webResources != null) {
                for (VFSRoot vfsRoot : webResources) {
                    try {
                        vfsRoot.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            // TODO review finally block
            if (executorService != null) {
                executorService.shutdown();
            }
            if (faces != null) {
                faces.stop();
            }
            Thread.currentThread().setContextClassLoader(contextCL);
        }
    }
}
