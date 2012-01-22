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

    private static final int ONE_SECOND = 1000;

    @Inject
    private AnotherValue anotherValue;
    @Inject
    private Context activity;
    @Inject
    private VibrateOnClickListener vibrateOnClickListener;
    private NotifyOnClickListener notifyOnClickListener;
    private Vibrator vibrator;
    private Button vibrateButton;
    private Button notifyButton;
    private int value = 0;

    @Inject
    private SimpleController(NotifyOnClickListener notifyOnClickListener) {
        this.notifyOnClickListener = notifyOnClickListener;
    }

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
        Toast toast = Toast.makeText(activity, "You clicked the activity" + value++, ONE_SECOND);
        toast.show();
    }

    public boolean validate() {
        return anotherValue != null && activity != null && vibrator != null && vibrateButton != null;
    }

    public Button getNotifyButton() {
        return notifyButton;
    }

    public Button getVibrateButton() {
        return vibrateButton;
    }
}
