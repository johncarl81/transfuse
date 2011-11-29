package org.androidrobotics.example.simple;

import android.content.Context;
import android.os.Vibrator;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class TestController {

    @Inject
    private SubComponent subComponent;
    @Inject
    private Context activity;
    private Vibrator vibrator;

    @Inject
    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;

        vibrator.vibrate(1000);
    }

    public boolean validate() {
        return subComponent != null && activity != null && vibrator != null;
    }

}
