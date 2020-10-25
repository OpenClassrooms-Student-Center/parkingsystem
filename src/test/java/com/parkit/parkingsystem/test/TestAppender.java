package com.parkit.parkingsystem.test;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestAppender implements Appender {

    private List<LogEvent> logEventList = new ArrayList<>();

    @Override
    public void append(LogEvent logEvent) {
        logEventList.add(logEvent);
    }

    @Override
    public String getName() {
        return "TestAppender";
    }

    @Override
    public Layout<? extends Serializable> getLayout() {
        return null;
    }

    @Override
    public boolean ignoreExceptions() {
        return false;
    }

    @Override
    public ErrorHandler getHandler() {
        return null;
    }

    @Override
    public void setHandler(ErrorHandler errorHandler) {

    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

    public void reset() {
        logEventList.clear();
    }

    public int getLogCount() {
        return logEventList.size();
    }
}
