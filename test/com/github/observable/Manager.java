package com.github.observable;

/**
 *
 * @author tushar_joshi
 */
public class Manager extends Person {
    
    public Manager(String name) {
        super(name);
    }

    @Override
    public Object getCopy() {
        return new Manager(getName());
    }
    
}
