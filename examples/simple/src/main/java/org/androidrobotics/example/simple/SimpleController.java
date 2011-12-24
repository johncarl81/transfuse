package org.androidrobotics.example.simple;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.Toast;
import org.androidrobotics.annotations.OnCreate;
import org.androidrobotics.annotations.OnTouch;
import org.androidrobotics.annotations.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SimpleController {

    @Inject
    private AnotherValueImpl anotherValue;
    @Inject
    private Context activity;
    @Inject
    private VibrateOnClickListener vibrateOnClickListener;
    private Vibrator vibrator;
    private Button button;
    private int value = 0;

    @Inject
    @View(R.id.button)
    public void setButton(Button button) {
        this.button = button;
    }

    @Inject
    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    @OnCreate
    public void setupButton() {
        button.setOnClickListener(vibrateOnClickListener);
    }

    @OnTouch
    public void vibrate() {
        Toast toast = Toast.makeText(activity, "You clicked the activity" + value++, 1000);
        toast.show();
    }

    public boolean validate() {
        return anotherValue != null && activity != null && vibrator != null && button != null;
    }
}
