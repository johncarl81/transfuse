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
package org.androidtransfuse.integrationTest.inject;

import android.widget.TextView;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "View Injection")
@Layout(R.layout.viewinjection)
public class ViewInjection {

    @Inject
    @View(R.id.viewText)
    private TextView textViewById;

    @Inject
    @View(tag = "viewTag")
    private TextView textViewByTag;

    @OnCreate
    public void updateText() {
        textViewById.setText("View by ID");
        textViewByTag.setText("View by Tag");
    }
}
