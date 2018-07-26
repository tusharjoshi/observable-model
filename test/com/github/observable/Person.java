package com.github.observable;

/**
 *
 * @author tushar_joshi
 */
public class Person extends ObservableModel {
    
    private static final String PROP_NAME = ".NAME";
    
    private String name;
    
    public Person(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        firePropertyChange(getClass().getName()+PROP_NAME, this.name, this.name = name);
    }
    
    @Override
    public Object getCopy() {
        return new Person(name);
    }

    @Override
    public String toString() {
        return "Person: "+name;
    }
    
}
