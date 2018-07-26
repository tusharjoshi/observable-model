package com.github.observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tushar_joshi
 * @param <T>
 */
public abstract class ObservableList<T extends ObservableModel> extends ObservableModel implements Iterable<T> {

    private static final String PROP_ADDED = "ADDED";
    private static final String PROP_REMOVED = "REMOVED";
    
    private final List<T> list;

    protected ObservableList() {
        list = new ArrayList<>();
        attachChildListeners();
    }

    private void attachChildListeners() {
        for (T item : list) {
            removeListenerFromChildModel(item);
            addListenerToChildModel(item);
        }
    }

    @Override
    protected Object readResolve() {
        super.readResolve();
        this.attachChildListeners();
        return this;
    }

    @Override
    public final Iterator<T> iterator() {
        List<T> localList = new ArrayList<>();
        synchronized (list) {
            localList.addAll(list);
        }

        return localList.iterator();
    }
    
    public final T get(int index) {
        return list.get(index);
    }

    public final boolean add(T child) {
        ObservableList cloneList = (ObservableList)getCopy();
        if (list.add(child)) {
            addListenerToChildModel(child);
            firePropertyChange(PROP_ADDED, cloneList, this);
            return true;
        }

        return false;
    }

    public final boolean remove(T child) {
        ObservableList cloneList = (ObservableList)getCopy();
        if (list.remove(child)) {
            removeListenerFromChildModel(child);
            firePropertyChange(PROP_REMOVED, cloneList, this);
            return true;
        }

        return false;
    }

    @Override
    public Object getCopy() {
        ObservableList<T> modelList;
        try {
            modelList = this.getClass().newInstance();
            for (T model : this) {
                modelList.add((T) model.getCopy());
            }

            return modelList;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ObservableList.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

}
