package org.androidtransfuse.example.simple;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SubComponent {

    private AnotherValue anotherValue;

    @Inject
    public SubComponent(AnotherValue anotherValue) {
        this.anotherValue = anotherValue;
    }
}
