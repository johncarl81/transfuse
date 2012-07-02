package org.androidtransfuse.integrationTest.observes;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
@Activity(label = "Observes")
@Layout(R.layout.observer)
public class EventObserver {

    private boolean eventOneTriggered;
    private boolean eventTwoTriggered;
    @Inject
    private EventManager eventManager;
    @Inject
    @View(R.id.observertext)
    private EditText editText;
    @Inject
    private Context context;

    @RegisterListener(R.id.eventonebutton)
    private android.view.View.OnClickListener eventOneListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            eventManager.trigger(new EventOne(editText.getText().toString()));
        }
    };

    @RegisterListener(R.id.eventtwobutton)
    private android.view.View.OnClickListener eventTwoListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            eventManager.trigger(new EventTwo());
        }
    };

    @Observes
    public void observe(EventOne one){
        Toast.makeText(context, "EventOne Observed with value: " + one.getValue(), ONE_SECOND).show();
        eventOneTriggered = true;
    }

    public void observe(@Observes EventTwo two){
        Toast.makeText(context, "EventTwo Observed", ONE_SECOND).show();
        eventTwoTriggered = true;
    }

    public boolean isEventOneTriggered() {
        return eventOneTriggered;
    }

    public boolean isEventTwoTriggered() {
        return eventTwoTriggered;
    }

    @OnPause
    public void keepInActivity(){

    }
}
