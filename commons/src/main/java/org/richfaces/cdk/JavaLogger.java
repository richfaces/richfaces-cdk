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

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <p class="changed_added_4_0">
 * That logger delegates all calls to the JDK {@link java.util.logging.Logger}
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class JavaLogger implements Logger {
    public static final String CDK_LOG = "org.richfaces.cdk";
    private static final String CLASS_NAME = JavaLogger.class.getName();
    private int errorCount = 0;
    private Throwable firstError;
    private java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger(CDK_LOG);

    private void fillCallerData(String fqn, LogRecord record) {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();

        int i = 0;

        for (; i < stackTrace.length; i++) {
            if (fqn.equals(stackTrace[i].getClassName())) {
                break;
            }
        }

        int idx = i + 1;

        for (; idx < stackTrace.length; idx++) {
            if (!fqn.equals(stackTrace[idx].getClassName())) {
                break;
            }
        }

        if (idx < stackTrace.length) {
            record.setSourceMethodName(stackTrace[idx].getMethodName());
            record.setSourceClassName(stackTrace[idx].getClassName());
        }
    }

    private LogRecord createRecord(Level level, CharSequence message, Throwable thrown) {
        // millis and thread are filled by the constructor
        LogRecord record = new LogRecord(level, message != null ? message.toString() : null);

        // TODO resource bundle?
        record.setLoggerName(jdkLogger.getName());
        record.setThrown(thrown);
        fillCallerData(CLASS_NAME, record);

        return record;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#debug(java.lang.CharSequence)
     */
    @Override
    public void debug(CharSequence content) {
        if (jdkLogger.isLoggable(Level.FINE)) {
            jdkLogger.log(createRecord(Level.FINE, content, null));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#debug(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void debug(CharSequence content, Throwable error) {
        if (jdkLogger.isLoggable(Level.FINE)) {
            jdkLogger.log(createRecord(Level.FINE, content, error));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#debug(java.lang.Throwable)
     */
    @Override
    public void debug(Throwable error) {
        if (jdkLogger.isLoggable(Level.FINE)) {
            jdkLogger.log(createRecord(Level.FINE, "", error));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#error(java.lang.CharSequence)
     */
    @Override
    public void error(CharSequence content) {
        errorCount++;
        firstError = new CdkException(content.toString());

        if (jdkLogger.isLoggable(Level.SEVERE)) {
            jdkLogger.log(createRecord(Level.SEVERE, content, null));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#error(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void error(CharSequence content, Throwable error) {
        errorCount++;
        firstError = error;

        if (jdkLogger.isLoggable(Level.SEVERE)) {
            jdkLogger.log(createRecord(Level.SEVERE, content, error));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#error(java.lang.Throwable)
     */
    @Override
    public void error(Throwable error) {
        errorCount++;
        firstError = error;

        if (jdkLogger.isLoggable(Level.SEVERE)) {
            jdkLogger.log(createRecord(Level.SEVERE, "", error));
        }
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#info(java.lang.CharSequence)
     */
    @Override
    public void info(CharSequence content) {
        if (jdkLogger.isLoggable(Level.INFO)) {
            jdkLogger.log(createRecord(Level.INFO, content, null));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#info(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void info(CharSequence content, Throwable error) {
        if (jdkLogger.isLoggable(Level.INFO)) {
            jdkLogger.log(createRecord(Level.INFO, content, error));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#info(java.lang.Throwable)
     */
    @Override
    public void info(Throwable error) {
        if (jdkLogger.isLoggable(Level.INFO)) {
            jdkLogger.log(createRecord(Level.INFO, "", error));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return jdkLogger.isLoggable(Level.FINE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return jdkLogger.isLoggable(Level.SEVERE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return jdkLogger.isLoggable(Level.INFO);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return jdkLogger.isLoggable(Level.WARNING);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#warn(java.lang.CharSequence)
     */
    @Override
    public void warn(CharSequence content) {
        if (jdkLogger.isLoggable(Level.WARNING)) {
            jdkLogger.log(createRecord(Level.WARNING, content, null));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#warn(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void warn(CharSequence content, Throwable error) {
        if (jdkLogger.isLoggable(Level.WARNING)) {
            jdkLogger.log(createRecord(Level.WARNING, content, error));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.Logger#warn(java.lang.Throwable)
     */
    @Override
    public void warn(Throwable error) {
        if (jdkLogger.isLoggable(Level.WARNING)) {
            jdkLogger.log(createRecord(Level.WARNING, "", error));
        }
    }

    @Override
    public Throwable getFirstError() {
        return firstError;
    }
}
