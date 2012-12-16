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
package org.androidtransfuse.integrationTest.inject;

import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "Preference Injection")
@Layout(R.layout.preferenceinjection)
public class PreferenceInjection {

    @Inject
    @View(R.id.preferenceText)
    private TextView preferenceText;

    @Inject
    @Preference(value = "pref_dialog", defaultValue = "test")
    private String preference;

    @OnCreate
    public void updatePreferenceText(){
        preferenceText.setText("Preference Value: " + preference);
    }
}
