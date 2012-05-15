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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class CdkClassLoader extends URLClassLoader {
    private static final URL[] EMPTY_URLS = {};
    private Iterable<File> files;

    public CdkClassLoader(Iterable<File> files) throws MalformedURLException {
        super(EMPTY_URLS);
        addFiles(files);
    }

    public CdkClassLoader(Iterable<File> files, ClassLoader parent) throws MalformedURLException {
        super(EMPTY_URLS, parent);
        addFiles(files);
    }

    public CdkClassLoader(ClassLoader classLoader) {
        super(EMPTY_URLS, classLoader);
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the files
     */
    public Iterable<File> getFiles() {
        return this.files;
    }

    private void addFileNames(Iterable<String> files) throws MalformedURLException {
        Set<File> filesSet = Sets.newHashSet();
        for (String name : files) {
            File file = new File(name);
            filesSet.add(file);
        }
        addFiles(filesSet);
    }

    private void addFiles(Iterable<File> filesSet) throws MalformedURLException {
        this.files = ImmutableSet.copyOf(filesSet);
        for (File file : filesSet) {
            URL url = file.toURI().toURL();
            addURL(url);
        }
    }
}
