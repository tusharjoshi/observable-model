package com.github.observable;

import java.beans.PropertyChangeListener;
import java.util.concurrent.Executor;

/**
 *
 * @author tushar_joshi
 */
public class ListenerExecutorPair {
    
    private final PropertyChangeListener listener;
    private final Executor executor;

    public ListenerExecutorPair(Executor executor, PropertyChangeListener listener) {
        this.listener = listener;
        this.executor = executor;
    }

    public PropertyChangeListener getListener() {
        return listener;
    }

    public Executor getExecutor() {
        return executor;
    }
    
}
