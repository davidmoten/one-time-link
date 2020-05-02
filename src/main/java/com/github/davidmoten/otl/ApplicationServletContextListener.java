package com.github.davidmoten.otl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class ApplicationServletContextListener implements ServletContextListener {

    private ScheduledExecutorService executor;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay( //
                () -> Stores.instance().cleanup(), //
                1, //
                1, //
                TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ScheduledExecutorService ex = executor;
        if (ex != null) {
            ex.shutdownNow();
        }
    }

}
