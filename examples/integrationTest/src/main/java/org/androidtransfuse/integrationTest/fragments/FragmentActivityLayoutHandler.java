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
package org.androidtransfuse.integrationTest.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.layout.LayoutHandlerDelegate;

import javax.inject.Inject;

public class FragmentActivityLayoutHandler implements LayoutHandlerDelegate {

    @Inject
    private Activity activity;

    @Override
    public void invokeLayout() {
        // Need to check if Activity has been switched to landscape mode
        // If yes, finished and go back to the start Activity

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.finish();
        }
        else{
            activity.setContentView(R.layout.details_activity_layout);
        }
    }
}