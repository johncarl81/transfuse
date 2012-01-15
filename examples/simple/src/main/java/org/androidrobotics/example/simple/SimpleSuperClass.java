package org.androidrobotics.example.simple;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import org.androidrobotics.annotations.OnTouch;
import org.androidrobotics.annotations.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SimpleSuperClass {

    @Inject
    @View(R.id.vibrateButton)
    private Button vibrateButton;
    private Context context;

    @Inject
    @LogInterception
    protected void setContext(Context context) {
        Log.i("intercept", "set Context");
        this.context = context;
    }

    @OnTouch
    @LogInterception
    protected void touch() {
    }

    public Button getVibrateButton() {
        return vibrateButton;
    }

    public Context getContext() {
        return context;
    }
}
