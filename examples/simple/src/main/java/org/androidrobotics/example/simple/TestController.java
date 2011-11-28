package org.androidrobotics.example.simple;

import android.content.Context;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class TestController {

    @Inject
    private SubComponent subComponent;
    @Inject
    private Context activity;

    public boolean validate() {
        return subComponent != null && activity != null;
    }

}
