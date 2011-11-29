package org.androidrobotics.provider;

import android.content.Context;
import android.os.Vibrator;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class VibratorProvider implements Provider<Vibrator> {

    private Context context;

    @Inject
    public VibratorProvider(Context context) {
        this.context = context;
    }

    @Override
    public Vibrator get() {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
