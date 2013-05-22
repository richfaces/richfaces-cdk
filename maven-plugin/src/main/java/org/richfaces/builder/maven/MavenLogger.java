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
package org.richfaces.builder.maven;

import org.apache.maven.plugin.logging.Log;
import org.richfaces.cdk.CdkException;
import org.richfaces.cdk.Logger;

/**
 * @author shura
 *
 */
public class MavenLogger implements Logger {
    private Log log;
    private int errorCount;
    private Throwable firstError;

    /**
     * @param log
     */
    public MavenLogger(Log log) {
        super();
        this.log = log;
    }

    /**
     * @param content
     * @param error
     * @see org.apache.maven.plugin.logging.Log#debug(java.lang.CharSequence, java.lang.Throwable)
     */
    public void debug(CharSequence content, Throwable error) {
        log.debug(content, error);
    }

    /**
     * @param content
     * @see org.apache.maven.plugin.logging.Log#debug(java.lang.CharSequence)
     */
    public void debug(CharSequence content) {
        log.debug(content);
    }

    /**
     * @param error
     * @see org.apache.maven.plugin.logging.Log#debug(java.lang.Throwable)
     */
    public void debug(Throwable error) {
        log.debug(error);
    }

    /**
     * @param content
     * @param error
     * @see org.apache.maven.plugin.logging.Log#error(java.lang.CharSequence, java.lang.Throwable)
     */
    public void error(CharSequence content, Throwable error) {
        log.error(content, error);
        errorCount++;
        firstError = error;
    }

    /**
     * @param content
     * @see org.apache.maven.plugin.logging.Log#error(java.lang.CharSequence)
     */
    public void error(CharSequence content) {
        log.error(content);
        errorCount++;
        firstError = new CdkException(content.toString());
    }

    /**
     * @param error
     * @see org.apache.maven.plugin.logging.Log#error(java.lang.Throwable)
     */
    public void error(Throwable error) {
        log.error(error);
        errorCount++;
        firstError = error;
    }

    /**
     * @param content
     * @param error
     * @see org.apache.maven.plugin.logging.Log#info(java.lang.CharSequence, java.lang.Throwable)
     */
    public void info(CharSequence content, Throwable error) {
        log.info(content, error);
    }

    /**
     * @param content
     * @see org.apache.maven.plugin.logging.Log#info(java.lang.CharSequence)
     */
    public void info(CharSequence content) {
        log.info(content);
    }

    /**
     * @param error
     * @see org.apache.maven.plugin.logging.Log#info(java.lang.Throwable)
     */
    public void info(Throwable error) {
        log.info(error);
    }

    /**
     * @return
     * @see org.apache.maven.plugin.logging.Log#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * @return
     * @see org.apache.maven.plugin.logging.Log#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * @return
     * @see org.apache.maven.plugin.logging.Log#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * @return
     * @see org.apache.maven.plugin.logging.Log#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    /**
     * @param content
     * @param error
     * @see org.apache.maven.plugin.logging.Log#warn(java.lang.CharSequence, java.lang.Throwable)
     */
    public void warn(CharSequence content, Throwable error) {
        log.warn(content, error);
    }

    /**
     * @param content
     * @see org.apache.maven.plugin.logging.Log#warn(java.lang.CharSequence)
     */
    public void warn(CharSequence content) {
        log.warn(content);
    }

    /**
     * @param error
     * @see org.apache.maven.plugin.logging.Log#warn(java.lang.Throwable)
     */
    public void warn(Throwable error) {
        log.warn(error);
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public Throwable getFirstError() {
        return firstError;
    }
}
