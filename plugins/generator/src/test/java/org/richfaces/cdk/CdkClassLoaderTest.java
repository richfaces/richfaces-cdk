package org.richfaces.cdk;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.google.common.collect.Lists;

public class CdkClassLoaderTest extends CdkTestBase {

    @Test
    public void resourcePath() throws Exception {
        File libraryFile = getLibraryFile("test.source.properties");
        assertNotNull(libraryFile);
        assertTrue(libraryFile.isDirectory());
    }

    @Test
    public void resourcePath2() throws Exception {
        File libraryFile = getLibraryFile("test.source.properties");
        File libraryFile2 = getLibraryFile("org/richfaces/cdk/apt/test.html");
        assertNotNull(libraryFile);
        assertEquals(libraryFile, libraryFile2);
    }

    @Test
    public void resourcePath3() throws Exception {
        File libraryFile = getLibraryFile("javax/faces/component/UIComponent.class");
        assertNotNull(libraryFile);
        assertFalse(libraryFile.isDirectory());
        assertTrue(libraryFile.getName().contains("jsf-api"));
    }

    @Test
    public void testClassLoader() throws Exception {
        Iterable<File> paths =
            Lists.newArrayList(getLibraryFile("test.source.properties"),
                getLibraryFile("javax/faces/component/UIComponent.class"));
        CdkClassLoader loader = new CdkClassLoader(paths, null);
        Class<?> class1 = loader.loadClass("javax.faces.application.Application");
        assertNotNull(loader.getResource("javax/faces/FacesException.class"));
        assertNotNull(loader.getResource("org/richfaces/cdk/apt/test.html"));
        assertNull(loader.getResource("javax/el/ELContext.class"));
    }

}
