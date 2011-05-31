/**
 *
 */
package org.richfaces.cdk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author asmirnov
 *
 */
public class LibraryBuilderTest extends CdkTestBase {
    private static final String[] JAVA_INCLUDES = new String[] { "**/*.java" };
    private static final String MAIN_CONFIG = "src/main/config";
    private static final String MAIN_TEMPLATES = "src/main/templates";
    private static final String PROJECT_BASE = "";
    private static final String[] STRINGS_ARRAY = new String[0];
    private static final String XML_INCLUDES = "**/*.xml";
    protected File outputJavaDirectory = new File(PROJECT_BASE + "target/generated-sources/main/java");
    protected File outputResourcesDirectory = new File(PROJECT_BASE + "target/generated-sources/main/resources");
    protected File outputTestDirectory = new File(PROJECT_BASE + "target/generated-sources/test/java");
    protected File outputTestResourcesDirectory = new File(PROJECT_BASE + "target/generated-sources/test/resources");

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

        generator.addOutputFolder(type, directory);
    }

    @Test
    public void createInstance() throws Exception {
        List<String> compileSourceRoots = Arrays.asList("");

        Generator generator = new Generator();
        generator.setLoader(createClassLoader());
        // Set source folders.
        ArrayList<File> folders = new ArrayList<File>(compileSourceRoots.size());

        for (String sourceFolder : compileSourceRoots) {
            File folder = new File(sourceFolder);

            if (folder.exists() && folder.isDirectory()) {
                folders.add(folder);
            }
        }

        generator.addSources(Sources.JAVA_SOURCES, findJavaFiles(), folders);
        // TODO - detect templates and configs directories.
        generator.addSources(Sources.RENDERER_TEMPLATES, findTemplateFiles(), null);
        generator.addSources(Sources.FACES_CONFIGS, findFacesConfigFiles(), null);

        // Setup output folders.
        setOutput(generator, outputJavaDirectory, Outputs.JAVA_CLASSES);
        setOutput(generator, outputResourcesDirectory, Outputs.RESOURCES);
        setOutput(generator, outputTestDirectory, Outputs.TEST_JAVA_CLASSES);
        setOutput(generator, outputTestResourcesDirectory, Outputs.TEST_RESOURCES);

        // configure CDK workers.
        // setupPlugins(generator);

        // Build JSF library.
        // LibraryBuilder builder = LibraryBuilder.createInstance(context);
        generator.init();
    }

    /**
     * <p class="changed_added_4_0">
     * This method checks library configuration and sets default values if necessary.
     * </p>
     */
    protected void checkLibraryConfig() {

        // TODO Auto-generated method stub
    }

    @Override
    protected CdkClassLoader createClassLoader() {
        return new CdkClassLoader(this.getClass().getClassLoader());
    }

    private Iterable<File> findFacesConfigFiles() {

        return Collections.emptySet();
    }

    private Iterable<File> findJavaFiles() {
        return Collections.emptySet();
    }

    private Iterable<File> findTemplateFiles() {
        return Collections.emptySet();
    }
}
