package org.androidtransfuse.integrationTest.listeners;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.listeners.ActivityOnTouchEventListener;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
@Activity(label = "Activity Listeners")
@Layout(R.layout.main)
@DeclareField
public class Listeners {

    private Context context;
    private ActivityOnTouchEventListener touchEventListener = new ActivityOnTouchEventListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Toast.makeText(context, "Activity Touch Event", ONE_SECOND).show();
            onTouchEventOccurred = true;
            return true;
        }
    };
    private boolean onTouchEventOccurred = false;

    @Inject
    public Listeners(Context context) {
        this.context = context;
    }

    public boolean isOnTouchEventOccurred() {
        return onTouchEventOccurred;
    }

    @RegisterListener
    public ActivityOnTouchEventListener getOnTouchListener() {
        return touchEventListener;
    }
}
