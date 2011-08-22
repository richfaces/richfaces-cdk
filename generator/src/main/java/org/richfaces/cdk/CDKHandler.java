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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class CDKHandler extends Handler {
    private final Logger log;

    public CDKHandler(Logger log) {
        this.log = log;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.logging.Handler#close()
     */
    @Override
    public void close() throws SecurityException {
        // do nothing

    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.logging.Handler#flush()
     */
    @Override
    public void flush() {
        // do nothing

    }

    @Override
    public boolean isLoggable(LogRecord record) {
        int level = record.getLevel().intValue();
        if (level >= Level.SEVERE.intValue()) {
            return log.isErrorEnabled();
        } else if (level >= Level.WARNING.intValue()) {
            return log.isWarnEnabled();
        } else if (level >= Level.INFO.intValue()) {
            return log.isInfoEnabled();
        } else {
            return log.isDebugEnabled();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
     */
    @Override
    public void publish(LogRecord record) {
        int level = record.getLevel().intValue();
        if (level >= Level.SEVERE.intValue()) {
            log.error(record.getMessage(), record.getThrown());
        } else if (level >= Level.WARNING.intValue()) {
            log.warn(record.getMessage(), record.getThrown());
        } else if (level >= Level.INFO.intValue()) {
            log.info(record.getMessage(), record.getThrown());
        } else {
            log.debug(record.getMessage(), record.getThrown());
        }
    }
}
