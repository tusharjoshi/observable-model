package com.github.observable;

/**
 *
 * @author tushar_joshi
 */
public class PersonList extends ObservableList<Person>{

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("\n");
        for( Person person : this) {
            builder.append(person);
            builder.append("\n");
        }
        return builder.toString(); 
    }
    
}
