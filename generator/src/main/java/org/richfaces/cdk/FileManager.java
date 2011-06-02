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
 *
 */
public interface FileManager {
    /**
     * <p class="changed_added_4_0">
     * Get existing file or directory.
     * </p>
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    File getFile(String path) throws FileNotFoundException;

    /**
     * <p class="changed_added_4_0">
     * Get collection of all source files for this manager.
     * </p>
     *
     * @return collection of files explicitly included for processing.
     */
    Iterable<File> getFiles();

    /**
     * <p class="changed_added_4_0">
     * Create new file in output folder.
     * </p>
     *
     * @param path relative path to output file.
     * @param lastModified time of last modification of model.
     * @return file descriptor or null if file exists and its modification time is later then <code>lastModified</code>
     *         parameter.
     * @throws IOException
     */
    Writer createOutput(String path, long lastModified) throws IOException;

    /**
     * <p class="changed_added_4_0">
     * Return collection of all folders included in this manager instance.
     * </p>
     *
     * @return
     */
    Iterable<File> getFolders();
}
