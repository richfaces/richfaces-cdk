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

import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import org.richfaces.cdk.Logger;

final class DiagnosticListenerImplementation implements DiagnosticListener<JavaFileObject> {
    /**
     * <p class="changed_added_4_0">
     * </p>
     */
    private final Logger log;
    private Locale locale;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param locale TODO
     * @param taskFactoryImpl
     */
    DiagnosticListenerImplementation(Logger log, Locale locale) {
        this.log = log;
    }

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        StringBuilder message = new StringBuilder(diagnostic.getMessage(this.locale));
        JavaFileObject source = diagnostic.getSource();
        if (null != source) {
            message.append(", in the file:").append(source.getName()).append(" at line ").append(diagnostic.getLineNumber())
                    .append(" in column ").append(diagnostic.getColumnNumber());
        }
        Kind kind = diagnostic.getKind();
        if (Kind.ERROR.equals(kind)) {
            this.log.error(message);
        } else if (Kind.MANDATORY_WARNING.equals(kind) || Kind.WARNING.equals(kind)) {
            this.log.warn(message);
        } else if (Kind.NOTE.equals(kind)) {
            this.log.info(message);
        } else {
            this.log.debug(message);
        }
    }
}