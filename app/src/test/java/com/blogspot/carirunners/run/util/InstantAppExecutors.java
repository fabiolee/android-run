package com.blogspot.carirunners.run.util;

import com.blogspot.carirunners.run.AppExecutors;

import java.util.concurrent.Executor;

public class InstantAppExecutors extends AppExecutors {
    private static Executor instant = command -> command.run();

    public InstantAppExecutors() {
        super(instant, instant, instant);
    }
}
