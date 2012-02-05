package org.androidtransfuse.example.simple;

import android.os.Vibrator;
import android.view.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VibrateOnClickListener implements View.OnClickListener {

    private static final int ONE_SECOND = 1000;

    @Inject
    private Vibrator vibrator;

    @Override
    @LogInterception
    public void onClick(View view) {
        vibrator.vibrate(ONE_SECOND);
    }
}
