package org.richfaces.cdk;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.codehaus.plexus.util.DirectoryScanner;

import com.beust.jcommander.Parameter;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class Parameters {

    private static final String[] JAVA_INCLUDES = new String[] { "**/*.java" };
    private static final String MAIN_CONFIG = "src/main/config";
    private static final String MAIN_TEMPLATES = "src/main/templates";
    private static final String[] STRINGS_ARRAY = new String[0];
    private static final String[] EMPTY = new String[0];
    private static final String[] XML_INCLUDES = new String[] { "**/*.xml" };

    @Parameter(names = "-p")
    String projectRoot;

    @Parameter(names = "-n")
    private String taglibNamespace;

    private List<String> compileSourceRoots;
    protected String[] sourceIncludes;
    protected String[] sourceExcludes;
    protected String templatesRoot;
    protected String configRoot;

    protected File outputDirectory;
    protected File outputJavaDirectory;
    protected File outputResourcesDirectory;
    protected File outputTestDirectory;
    protected File outputTestResourcesDirectory;

    protected Map<String, String> options = new HashMap<String, String>();

    private Logger logger = new CustomLogger();

    private void setup() {
        compileSourceRoots = Arrays.asList(projectRoot + "/src/main/java");

        templatesRoot = projectRoot + "/" + MAIN_TEMPLATES;
        configRoot = projectRoot + "/" + MAIN_CONFIG;

        outputDirectory = new File(projectRoot, "target/classes");
        outputJavaDirectory = new File(projectRoot, "target/generated-sources/main/java");
        outputResourcesDirectory = new File(projectRoot, "target/generated-sources/main/resources");
        outputTestDirectory = new File(projectRoot, "target/generated-sources/test/java");
        outputTestResourcesDirectory = new File(projectRoot, "target/generated-sources/test/resources");
    }

    public void execute() {
        setup();

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

        // configure CDK workers.
        setupPlugins(generator);

        if (null != options) {

            // TODO make it type safe.
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
        String[] files = doScan(XML_INCLUDES, EMPTY, new File(templatesRoot));
        return Collections2.transform(Arrays.asList(files), new StringToFile(templatesRoot));
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
        
//        files.addAll(listClassFiles(new File(projectRoot, "target/classes")));
//        files.addAll(listClassFiles(new File(projectRoot, "target/dependency")));
        files.add(new File(projectRoot, "target/classes"));
        files.add(new File(projectRoot, "target/dependency"));
        
        return files;
    }
    
    private Collection<File> listClassFiles(File directory) {
        return FileUtils.listFiles(directory, FileFilterUtils.suffixFileFilter(".class"),
                FileFilterUtils.directoryFileFilter());
    }

    protected String[] doScan(String[] includes, String[] excludes, File rootFolder) {
        if (!rootFolder.exists()) {
            return new String[]{};
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
