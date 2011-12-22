package org.androidrobotics.example.simple;

import android.os.Vibrator;
import android.view.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VibrateOnClickListener implements View.OnClickListener {

    @Inject
    private Vibrator vibrator;

    @Override
    public void onClick(View view) {
        vibrator.vibrate(1000);
    }
}
