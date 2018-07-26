package com.github.observable;

import java.util.concurrent.Executor;

/**
 *
 * @author tushar_joshi
 */
public class DefaultThreadExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }
    
}
