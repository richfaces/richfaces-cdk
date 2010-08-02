/**
 * License Agreement.
 *
 * YUI Compressor Maven Mojo
 *
 * Copyright (C) 2007 Alchim31 Team
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



package net.sf.alchim.mojo.yuicompressor;

import org.apache.maven.plugin.logging.Log;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class ErrorReporter4Mojo implements ErrorReporter {
    private boolean acceptWarn_;
    private String defaultFilename_;
    private int errorCnt_;
    private Log log_;
    private int warningCnt_;

    public ErrorReporter4Mojo(Log log, boolean jswarn) {
        log_ = log;
        acceptWarn_ = jswarn;
    }

    public void setDefaultFileName(String v) {
        if (v.length() == 0) {
            v = null;
        }

        defaultFilename_ = v;
    }

    public int getErrorCnt() {
        return errorCnt_;
    }

    public int getWarningCnt() {
        return warningCnt_;
    }

    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        String fullMessage = newMessage(message, sourceName, line, lineSource, lineOffset);

        log_.error(fullMessage);
        errorCnt_++;
    }

    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
            int lineOffset) {
        error(message, sourceName, line, lineSource, lineOffset);

        throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (acceptWarn_) {
            String fullMessage = newMessage(message, sourceName, line, lineSource, lineOffset);

            log_.warn(fullMessage);
            warningCnt_++;
        }
    }

    private String newMessage(String message, String sourceName, int line, String lineSource, int lineOffset) {
        StringBuilder back = new StringBuilder();

        if ((sourceName == null) || (sourceName.length() == 0)) {
            sourceName = defaultFilename_;
        }

        if (sourceName != null) {
            back.append(sourceName).append(":line ").append(line).append(":column ").append(lineOffset).append(':')
            ;
        }

        if ((message != null) && (message.length() != 0)) {
            back.append(message);
        } else {
            back.append("unknown error");
        }

        if ((lineSource != null) && (lineSource.length() != 0)) {
            back.append("\n\t").append(lineSource)
            ;
        }

        return back.toString();
    }
}
