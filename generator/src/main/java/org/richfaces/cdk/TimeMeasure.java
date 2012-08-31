package org.richfaces.cdk;

import com.google.inject.Inject;

public class TimeMeasure {

    private static final String START = "[%s started]";
    private static final String START_PARAM = "[%s started: %s]";

    private static final String STOP = "[%s completed: %s ms]";

    private String name;
    private boolean info = false;
    private long start = System.currentTimeMillis();

    public TimeMeasure(String name, Logger log) {
        this.name = name;
        this.log = log;
    }

    @Inject
    private Logger log;

    public TimeMeasure start() {
        log(START, name);
        start = System.currentTimeMillis();
        return this;
    }

    public TimeMeasure start(String param) {
        log(START_PARAM, name, param);
        start = System.currentTimeMillis();
        return this;
    }

    public TimeMeasure stop() {
        long end = System.currentTimeMillis();
        log(STOP, name, (end - start));
        return this;
    }

    public TimeMeasure info(boolean info) {
        this.info = info;
        return this;
    }

    private void log(String message, Object... arguments) {
        if (info) {
            log.info(String.format(message, arguments));
        } else if (log.isDebugEnabled()) {
            log.debug(String.format(message, arguments));
        }
    }
}
