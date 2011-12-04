package org.androidrobotics.example.simple;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnotherValueImpl implements AnotherValue {

    private SubComponent subComponent;

    @Inject
    public AnotherValueImpl(SubComponent subComponent) {
        this.subComponent = subComponent;
    }
}
