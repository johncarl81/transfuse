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
    private AnotherValue anotherValue;
    @Inject
    private Context activity;
    @Inject
    private VibrateOnClickListener vibrateOnClickListener;
    @Inject
    private NotifyOnClickListener notifyOnClickListener;
    private Vibrator vibrator;
    private Button vibrateButton;
    private Button notifyButton;
    private int value = 0;

    @Inject
    @View(R.id.vibrateButton)
    public void setVibrateButton(Button vibrateButton) {
        this.vibrateButton = vibrateButton;
    }

    @Inject
    @View(R.id.notifyButton)
    public void setNotifyButton(Button notifyButton) {
        this.notifyButton = notifyButton;
    }

    @Inject
    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    @OnCreate
    public void setupButton() {
        vibrateButton.setOnClickListener(vibrateOnClickListener);
        notifyButton.setOnClickListener(notifyOnClickListener);
    }

    @OnTouch
    public void vibrate() {
        Toast toast = Toast.makeText(activity, "You clicked the activity" + value++, 1000);
        toast.show();
    }

    public boolean validate() {
        return anotherValue != null && activity != null && vibrator != null && vibrateButton != null;
    }
}
