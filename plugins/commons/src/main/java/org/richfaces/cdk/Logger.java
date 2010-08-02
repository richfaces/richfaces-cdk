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
 * That interface hides current logging system from generator classes.
 * Concrete tools ( Maven, Ant, JUnit ) should provide appropriate logger instance that delegates
 * messages to the current log system.
 *
 * @author shura
 */
@ImplementedBy(JavaLogger.class)
public interface Logger {
    public boolean isDebugEnabled();

    public void debug(CharSequence content);

    public void debug(CharSequence content, Throwable error);

    public void debug(Throwable error);

    public boolean isInfoEnabled();

    public void info(CharSequence content);

    public void info(CharSequence content, Throwable error);

    public void info(Throwable error);

    public boolean isWarnEnabled();

    public void warn(CharSequence content);

    public void warn(CharSequence content, Throwable error);

    public void warn(Throwable error);

    public boolean isErrorEnabled();

    public void error(CharSequence content);

    public void error(CharSequence content, Throwable error);

    public void error(Throwable error);
    
    public int getErrorCount();
}
