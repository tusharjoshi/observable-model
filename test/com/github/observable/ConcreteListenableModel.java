package com.github.observable;

/**
 *
 * @author tushar_joshi
 */
public class ConcreteListenableModel extends ObservableModel {
    
    private String stateData;
    
    public ConcreteListenableModel(String stateData) {
        this.stateData = stateData;
    }

    public String getStateData() {
        return stateData;
    }

    public void setStateData(String stateData) {
        this.stateData = stateData;
    }

    @Override
    public Object getCopy() {
        return new ConcreteListenableModel(stateData);
    }
    
}
