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
package org.richfaces.cdk.apt;

import javax.tools.JavaCompiler.CompilationTask;

import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.LibraryBuilder;

import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * That class compiles files from sources and process annotations
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class AptBuilder implements LibraryBuilder {

    @Inject
    private CompilationTaskFactory taskFactory;

    public void build() throws CdkException {
        CompilationTask task = taskFactory.get();
        if (!task.call()) {
            throw new AptException("Compilation error");
        }
    }

}
