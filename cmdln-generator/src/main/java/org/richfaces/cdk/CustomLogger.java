package org.richfaces.cdk;

import java.lang.reflect.Method;

public class CustomLogger implements Logger {

    private static final boolean INFO = true;
    private static final boolean WARN = true;
    private static final boolean ERROR = true;

    private boolean debug = false;
    private int errorCount = 0;
    private Throwable firstError;

    @Override
    public boolean isDebugEnabled() {
        return debug;
    }

    public void setDebugEnabled(boolean enabled) {
        this.debug = enabled;
    }

    @Override
    public void debug(CharSequence content) {
        debug(content, null);
    }

    @Override
    public void debug(Throwable error) {
        debug(null, error);
    }

    @Override
    public void debug(CharSequence content, Throwable error) {
        log("Debug", content, error);
    }

    @Override
    public boolean isInfoEnabled() {
        return INFO;
    }

    @Override
    public void info(CharSequence content) {
        info(content, null);
    }

    @Override
    public void info(Throwable error) {
        info(null, error);
    }

    @Override
    public void info(CharSequence content, Throwable error) {
        log("Info", content, error);
    }

    @Override
    public boolean isWarnEnabled() {
        return WARN;
    }

    @Override
    public void warn(CharSequence content) {
        warn(content, null);
    }

    @Override
    public void warn(Throwable error) {
        warn(null, error);
    }

    @Override
    public void warn(CharSequence content, Throwable error) {
        log("Warn", content, error);
    }

    @Override
    public boolean isErrorEnabled() {
        return ERROR;
    }

    @Override
    public void error(CharSequence content) {
        error(content, null);
    }

    @Override
    public void error(Throwable error) {
        error(null, error);
    }

    @Override
    public void error(CharSequence content, Throwable error) {
        errorCount += 1;
        firstError = (error != null) ? error : new CdkException(content.toString());
        log("Error", content, error);
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    private void log(String severity, CharSequence content, Throwable error) {
        if (!isEnabled(severity)) {
            return;
        }
        if (content != null) {
            System.out.println("[" + severity.toUpperCase() + "] " + content);
        } else {
            System.out.println("[" + severity.toUpperCase() + "] ");
        }
        if (error != null) {
            error.printStackTrace();
        }
    }

    private boolean isEnabled(String severity) {
        try {
            Method method = this.getClass().getMethod("is" + severity + "Enabled");
            return (Boolean) method.invoke(this);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Throwable getFirstError() {
        return firstError;
    }

}
