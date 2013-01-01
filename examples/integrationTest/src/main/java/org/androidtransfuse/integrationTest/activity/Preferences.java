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
package org.androidtransfuse.integrationTest.activity;

import android.preference.PreferenceActivity;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * Tests registration of a Special Activity type, PreferenceActivity.
 *
 * @author John Ericksen
 */
@Activity(type = PreferenceActivity.class, label = "Preferences")
@DeclareField
public class Preferences {

    @Inject
    private PreferenceActivity activity;

    @OnCreate
    public void setupPreferences() {
        activity.addPreferencesFromResource(R.xml.preferences);
    }

    public PreferenceActivity getActivity() {
        return activity;
    }
}
