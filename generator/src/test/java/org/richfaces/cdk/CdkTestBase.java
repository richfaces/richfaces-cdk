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

package org.richfaces.cdk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.LogManager;

import org.junit.After;
import org.junit.Before;
import org.richfaces.cdk.annotations.JsfComponent;

import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public abstract class CdkTestBase implements Module {

    protected File tempDir;

    protected Iterable<File> testSourceDirectory;

    @Override
    public void configure(Binder binder) {
        binder.bind(Locale.class).toInstance(Locale.getDefault());
        binder.bind(Charset.class).toInstance(Charset.defaultCharset());
    }

    @Before
    public void setUpSourceDirectory() throws Exception {
        testSourceDirectory = Collections.singleton(getLibraryFile("test.source.properties"));

        this.tempDir = File.createTempFile("cdk", "test");

        InputStream stream = this.getClass().getResourceAsStream("logging.properties");

        if (null != stream) {
            try {
                LogManager.getLogManager().readConfiguration(stream);
            } catch (Exception e) {

                // Ignore it.
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {

                    // Ignore it.
                }
            }
        }
    }

    @After
    public void tearDownSourceDirectory() {
        testSourceDirectory = null;
    }

    protected CdkClassLoader createClassLoader() {
        try {
            return new CdkClassLoader(ImmutableList.of(getLibraryFile("test.source.properties"),
                getLibraryFile(JsfComponent.class)), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected File getJavaFile(String name) throws URISyntaxException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL testResource = classLoader.getResource(name);
        URI testUri = testResource.toURI();
        final File classFile = new File(testUri);

        return classFile;
    }

    protected File getLibraryFile(Class<?> clazz) throws Exception {
        return getLibraryFile(clazz.getName().replaceAll("\\.", "/") + ".class");
    }

    protected File getLibraryFile(String resource) throws Exception {
        URL url = this.getClass().getClassLoader().getResource(resource);
        if (null != url) {
            if ("file".equals(url.getProtocol())) {
                String path = url.getPath();
                // trim resource path
                path = path.substring(0, path.length() - resource.length());
                File dir = new File(path);
                return dir;
            } else if ("jar".equals(url.getProtocol())) {
                String jarPath = url.getPath();
                jarPath = jarPath.substring(0, jarPath.indexOf('!'));
                File file = new File(new URI(jarPath));
                return file;
            } else {
                throw new Exception("Unknown protocol " + url.getProtocol());
            }
        } else {
            throw new Exception("Resource does not exists " + resource);
        }
    }
}
