package org.androidrobotics.example.simple;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class TestController {

    @Inject
    private SubComponent subComponent;

    public boolean validate() {
        return subComponent != null;
    }

}
