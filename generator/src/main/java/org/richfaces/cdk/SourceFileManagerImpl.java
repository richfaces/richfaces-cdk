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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class SourceFileManagerImpl implements SourceFileManager, FileManager {
    private final Iterable<File> sources;
    private final Iterable<File> folders;

    public SourceFileManagerImpl(Iterable<File> sources, Iterable<File> folders) {
        this.sources = sources;
        this.folders = folders;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.FileManager#createFile(java.lang.String)
     */

    @Override
    public Writer createOutput(String path, long lastModified) throws IOException {
        throw new UnsupportedOperationException("Cannot create file in source folder");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.FileManager#getFile(java.lang.String)
     */

    @Override
    public File getFile(String path) throws FileNotFoundException {
        String platformPath;

        if (File.separatorChar == '/') {
            platformPath = path;
        } else {
            platformPath = path.replace('/', File.separatorChar);
        }

        if (null != folders) {
            for (File folder : folders) {
                if (folder.exists() && folder.isDirectory()) {
                    File configFile = new File(folder, platformPath);
                    if (configFile.exists()) {
                        return configFile;
                    }
                }
            }
        }
        if (null != sources) {
            for (File file : sources) {
                if (file.getAbsolutePath().endsWith(platformPath)) {
                    return file;
                }
            }
        }
        throw new FileNotFoundException(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.FileManager#getFiles()
     */

    @Override
    public Iterable<File> getFiles() {
        return sources;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the folders
     */
    @Override
    public Iterable<File> getFolders() {
        return this.folders;
    }
}
