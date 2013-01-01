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
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ResourceInjectionActivity", label = "Resources")
@Layout(R.layout.display)
@DeclareField
public class ResourceInjection {

    @Inject
    @Resource(R.string.app_name)
    private String appName;

    @Inject
    @View(R.id.displayText)
    private TextView textView;

    public String getAppName() {
        return appName;
    }

    @OnCreate
    public void updateDisplayText(){
        textView.setText("Text Resource: " + appName);
    }
}
