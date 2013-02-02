/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.integrationTest.observes;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
@Activity(label = "Observes")
@Layout(R.layout.observer)
@DeclareField
public class EventObserver {

    private boolean eventOneTriggered;
    private boolean eventTwoTriggered;
    @Inject
    private EventManager eventManager;
    @Inject
    private Provider<EventManager> eventManagerProvider;
    @Inject
    @View(R.id.observertext)
    private EditText editText;
    @Inject
    private Context context;
    @Inject
    private SingletonObserver observer;

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

    @RegisterListener(R.id.eventthreeobutton)
    private android.view.View.OnClickListener eventThreeListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            eventManagerProvider.get().trigger(new EventThree());
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
}
