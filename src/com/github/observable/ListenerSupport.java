package com.github.observable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 *
 * @author tushar_joshi
 */
public class ListenerSupport {
    
    private final Object source;
    
    private final Executor currentThreadExecutor
            = new DefaultThreadExecutor();
    
    private final String DEFAULT_NAME = "$$DEFAULT$$";
    
    private final Map<String, List<ListenerExecutorPair>> listenerMap
            = new HashMap<>();
    
    private final Map<PropertyChangeListener, List<ListenerExecutorPair>> listenerListMap
            = new HashMap<>();
    
    public ListenerSupport(Object source) {
        this.source = source;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(currentThreadExecutor, listener);
    }
    
    public void addPropertyChangeListener(
            Executor executor, 
            PropertyChangeListener listener) {
        addPropertyChangeListener(executor, DEFAULT_NAME, listener);
    }
    
    public void addPropertyChangeListener(String propertyName, 
            PropertyChangeListener listener) {
        addPropertyChangeListener(currentThreadExecutor, 
                propertyName, listener);
    }
    
    public void addPropertyChangeListener(Executor executor, 
            String propertyName, PropertyChangeListener listener) {
        List<ListenerExecutorPair> list = ensureListExistsAndGet(propertyName);  
        list.add(new ListenerExecutorPair(executor, listener));
        listenerListMap.put(listener, list);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        List<ListenerExecutorPair> listenerList = listenerListMap.get(listener);
        if( null != listenerList) {
            removeListenerExecutorPair(listenerList, listener);
        }
    }

    private void removeListenerExecutorPair(
            List<ListenerExecutorPair> listenerList, 
            PropertyChangeListener listener) {
        ListenerExecutorPair pair = findPair(listenerList, listener);
        if( null != pair) {
            listenerList.remove(pair);
        }
    }

    private List<ListenerExecutorPair> ensureListExistsAndGet(
            String propertyName) {
        List<ListenerExecutorPair> list = listenerMap.get(propertyName);
        if( null == list ) {
            list = new ArrayList<>();
            listenerMap.put(propertyName, list);
        }
        return list;
    }
    
    public void firePropertyChange(String propertyName,
                                        Object oldValue, Object newValue) {
        firePropertyChange(
                new PropertyChangeEvent(source, propertyName,
                                               oldValue, newValue)
        );        
        
    }
    
    public void firePropertyChange(PropertyChangeEvent event){
        if (isPropertyNotificationNeeded(event)) {
            fireCommonListeners(event);
            fireNamedListeners(event);
        }
    }

    private void fireCommonListeners(PropertyChangeEvent event) {
        ListenerExecutorPair[] commonList = getCommonListeners();
        fire(commonList, event);
    }
    
    private ListenerExecutorPair[] getCommonListeners() {
        List<ListenerExecutorPair> commonList = this.listenerMap.get(DEFAULT_NAME);
        if( null != commonList) {
            return commonList.toArray(new ListenerExecutorPair[commonList.size()]);
        } 
        
        return null;
    }
    
    private ListenerExecutorPair[] getNamedListeners(String propertyName) {
        if(propertyName != null){
            List<ListenerExecutorPair> namedList = this.listenerMap.get(propertyName);
            if( null != namedList ) {   
                return namedList.toArray(new ListenerExecutorPair[namedList.size()]);
            } 
        }
        return null;
    }

    private void fireNamedListeners(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();
        ListenerExecutorPair[] namedArray = getNamedListeners(propertyName);
        fire(namedArray, event);
    }
    
    private void fire(final ListenerExecutorPair[] pairArray, 
            final PropertyChangeEvent event) {
        if (null != pairArray) {
            for( ListenerExecutorPair pair : pairArray){
                pair.getExecutor().execute(() -> {
                    pair.getListener().propertyChange(event);
                });
            }
        }
    }
    

    private boolean isPropertyNotificationNeeded(PropertyChangeEvent event) {
        return isPropertyNotificationNeeded(
                event.getOldValue(), 
                event.getNewValue()
        );
    }

    private boolean isPropertyNotificationNeeded(Object oldValue,
            Object newValue) {
        return (oldValue == null 
                || newValue == null 
                || !(oldValue.equals(newValue))
                );
    }

    private ListenerExecutorPair findPair(
            List<ListenerExecutorPair> listenerList, 
            PropertyChangeListener listener) {
        
        for( ListenerExecutorPair pair : listenerList) {
            if( pair.getListener() == listener) {
                return pair;
            }
        }
        
        return null;
    }
    
}
