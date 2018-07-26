package com.github.observable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.concurrent.Executor;

/**
 *
 * @author tushar_joshi
 */
public abstract class ObservableModel implements Serializable, PropertyChangeListener, Copyable {

    private static final long serialVersionUID = 1L;

    private static final String PROP_BULK = "BULK_OPERATION";

    private transient ListenerSupport listenerSupport;

    private transient ObservableModel cloneObject = null;

    private volatile boolean bulkOperation = false;
    
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(listener);
    }

    public final void addPropertyChangeListener(Executor executor, PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(executor, listener);
    }

    public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(propertyName, listener);
    }

    public final void addPropertyChangeListener(Executor executor, String propertyName, PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(executor, propertyName, listener);
    }

    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        listenerSupport.removePropertyChangeListener(listener);
    }

    public final void addListenerToChildModel(ObservableModel childModel) {
        childModel.addPropertyChangeListener(this);
    }

    public final void removeListenerFromChildModel(ObservableModel childModel) {
        childModel.removePropertyChangeListener(this);
    }

    public final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        firePropertyChangeWithBulkCheck(new PropertyChangeEvent(
                this,
                prependClassName(propertyName),
                oldValue,
                newValue
        ));
    }

    public final void firePropertyChange(PropertyChangeEvent event) {
        firePropertyChangeWithBulkCheck(event);
    }

    private void firePropertyChangeWithBulkCheck(PropertyChangeEvent event) {
        if (!bulkOperation) {
            listenerSupport.firePropertyChange(event);
        }
    }

    protected ObservableModel() {
        initializeTransientFields();
    }

    private void initializeTransientFields() {
        this.listenerSupport = new ListenerSupport(this);
    }

    protected Object readResolve() {
        this.initializeTransientFields();
        return this;
    }

    @Override
    public final void propertyChange(PropertyChangeEvent event) {
        firePropertyChangeWithBulkCheck(event);
    }

    public final void startBulkOperation() {
        cloneObject = (ObservableModel) getCopy();
        bulkOperation = true;
    }

    public final void stopBulkOperation() {
        if( null == cloneObject) {
            return;
        }
        
        bulkOperation = false;
        firePropertyChangeWithBulkCheck(new PropertyChangeEvent(
                this,
                prependClassName(PROP_BULK),
                cloneObject,
                this
        ));
        cloneObject  = null;
    }

    private String prependClassName(String propertyName) {
        return this.getClass().getName() + "." + propertyName;
    }
}
