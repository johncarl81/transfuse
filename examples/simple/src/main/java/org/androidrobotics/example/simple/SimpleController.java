package org.androidrobotics.example.simple;

import android.content.Context;
import android.os.Vibrator;
import org.androidrobotics.annotations.OnTouch;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SimpleController {

    @Inject
    private AnotherValue anotherValue;
    @Inject
    private Context activity;
    private Vibrator vibrator;

    @Inject
    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    @OnTouch
    public void vibrate() {
        vibrator.vibrate(1000);
    }

    public boolean validate() {
        return anotherValue != null && activity != null && vibrator != null;
    }

}
