package com.github.observable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.junit.Test;

/**
 *
 * @author tushar_joshi
 */
public class PersonListTest {
    
    @Test
    public void test() {
        PersonList personList = new PersonList();
        personList.addPropertyChangeListener(new PersonListener());
        Person person = new Person("Santa");
        personList.add(person);
        person.setName("Phantom");
        personList.startBulkOperation();
        person = new Person("Banta");
        personList.add(person);
        personList.stopBulkOperation();
        
        
        PersonList cloneList = (PersonList)personList.getCopy();
        cloneList.addPropertyChangeListener(new PersonListener());
        for(Person item : cloneList) {
            System.out.println("Name:"+ item.getName());
            item.setName("John");
        }
        
    }
    
    public static class PersonListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println("Property Changed:"+ evt.getPropertyName());
            System.out.println("Source:"+ evt.getSource());
            System.out.println("Old Object:"+ evt.getOldValue());
            System.out.println("New Object:"+ evt.getNewValue());
        }
        
    }
}
