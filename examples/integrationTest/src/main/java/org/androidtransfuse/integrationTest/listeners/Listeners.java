/**
 * Copyright 2012 John Ericksen
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
