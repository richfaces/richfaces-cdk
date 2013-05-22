/**
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

import com.google.inject.ImplementedBy;

/**
 * That interface hides current logging system from generator classes. Concrete tools ( Maven, Ant, JUnit ) should provide
 * appropriate logger instance that delegates messages to the current log system.
 *
 * @author shura
 */
@ImplementedBy(JavaLogger.class)
public interface Logger {
    boolean isDebugEnabled();

    void debug(CharSequence content);

    void debug(CharSequence content, Throwable error);

    void debug(Throwable error);

    boolean isInfoEnabled();

    void info(CharSequence content);

    void info(CharSequence content, Throwable error);

    void info(Throwable error);

    boolean isWarnEnabled();

    void warn(CharSequence content);

    void warn(CharSequence content, Throwable error);

    void warn(Throwable error);

    boolean isErrorEnabled();

    void error(CharSequence content);

    void error(CharSequence content, Throwable error);

    void error(Throwable error);

    int getErrorCount();

    Throwable getFirstError();
}
