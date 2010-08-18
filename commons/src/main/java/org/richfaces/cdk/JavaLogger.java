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

/**
 * <p class="changed_added_4_0">That logger delegates all calls to the JDK {@link java.util.logging.Logger}</p>
 *
 * @author asmirnov@exadel.com
 */
public class JavaLogger implements Logger {
    
    public static final String CDK_LOG = "org.richfaces.cdk";

    private int errorCount = 0;
    
    private java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger(CDK_LOG);

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#debug(java.lang.CharSequence)
     */
    @Override
    public void debug(CharSequence content) {
        jdkLogger.fine(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#debug(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void debug(CharSequence content, Throwable error) {
        jdkLogger.log(Level.FINE, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#debug(java.lang.Throwable)
     */
    @Override
    public void debug(Throwable error) {
        jdkLogger.log(Level.FINE, "", error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#error(java.lang.CharSequence)
     */
    @Override
    public void error(CharSequence content) {
        errorCount++;
        jdkLogger.severe(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#error(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void error(CharSequence content, Throwable error) {
        errorCount++;
        jdkLogger.log(Level.SEVERE, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#error(java.lang.Throwable)
     */
    @Override
    public void error(Throwable error) {
        errorCount++;
        jdkLogger.log(Level.SEVERE, "", error);
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#info(java.lang.CharSequence)
     */
    @Override
    public void info(CharSequence content) {
        jdkLogger.info(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#info(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void info(CharSequence content, Throwable error) {
        jdkLogger.log(Level.INFO, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#info(java.lang.Throwable)
     */
    @Override
    public void info(Throwable error) {
        jdkLogger.log(Level.INFO, "", error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return jdkLogger.isLoggable(Level.FINE);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return jdkLogger.isLoggable(Level.SEVERE);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return jdkLogger.isLoggable(Level.INFO);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return jdkLogger.isLoggable(Level.WARNING);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#warn(java.lang.CharSequence)
     */
    @Override
    public void warn(CharSequence content) {
        jdkLogger.warning(String.valueOf(content));
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#warn(java.lang.CharSequence, java.lang.Throwable)
     */
    @Override
    public void warn(CharSequence content, Throwable error) {
        jdkLogger.log(Level.WARNING, String.valueOf(content), error);
    }

    /*
     *  (non-Javadoc)
     * @see org.richfaces.cdk.Logger#warn(java.lang.Throwable)
     */
    @Override
    public void warn(Throwable error) {
        jdkLogger.log(Level.WARNING, "", error);
    }
}
