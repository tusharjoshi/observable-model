package com.github.observable;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tushar_joshi
 */
public class ObservableModelTest {
    
    private String stateData;
    private ObservableModel instance;
    
    @Before
    public void setUp() {
        stateData = "StateData";
        instance = new ConcreteListenableModel(stateData);
    }
    
    @Test
    public void test_addPropertyChangeListener() {
        
    }
}
