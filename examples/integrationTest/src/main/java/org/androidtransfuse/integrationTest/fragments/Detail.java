/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.integrationTest.fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

@Activity(type = FragmentActivity.class)
@LayoutHandler(FragmentActivityLayoutHandler.class)
public class Detail {

    @Inject
    @Extra(value = "value", optional = true)
    private String value;

    @Inject
    @View(R.id.detailsText)
    private TextView view;

    @Inject
    private Resources resources;

    @OnCreate
    protected void onCreate() {
        if (resources.getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE &&
                value != null) {
            view.setText(value);
        }
    }
}