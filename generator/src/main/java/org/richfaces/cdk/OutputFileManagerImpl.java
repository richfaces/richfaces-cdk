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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class OutputFileManagerImpl implements OutputFileManager {
    private final Iterable<File> folders;
    private final File folder;

    public OutputFileManagerImpl(File folder) {
        this.folder = folder;
        this.folders = Collections.singleton(folder);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.FileManager#createFile(java.lang.String)
     */
    @Override
    public Writer createOutput(String path, long lastModified) throws IOException {
        if (null == path) {
            throw new NullPointerException("Output file path is null");
        }

        if (null == folder) {
            throw new FileNotFoundException("No output folder set for file " + path);
        }

        if (folder.exists() && !folder.isDirectory()) {
            throw new IOException("Output folder " + folder + " not is directory.");
        }

        // Strip leading '/'
        if (path.startsWith(File.separator)) {
            path = path.substring(1);
        }

        File outputFile = new File(folder, path);

        if (outputFile.exists()) {
            if (lastModified > 0 && outputFile.lastModified() > lastModified) {
                return null;
            } else {
                outputFile.delete();
            }
        }

        // Create file folder.
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        return new FileWriter(outputFile);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.FileManager#getFile(java.lang.String)
     */
    @Override
    public File getFile(String path) throws FileNotFoundException {
        File file = new File(folder, path);
        if (file.exists()) {
            return file;
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
        // TODO - list all files in folder.
        return Collections.emptySet();
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
