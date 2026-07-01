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
package org.androidtransfuse.example.agp8;

import android.widget.TextView;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.View;

import jakarta.inject.Inject;

/**
 * Transfuse @Activity POJO. Transfuse generates the concrete HelloTransfuseActivity referenced by
 * the manifest. The Greeter dependency is wired with jakarta.inject to exercise jakarta support in
 * a real, dexed Android build under AGP 8.
 *
 * @author John Ericksen
 */
@Activity(label = "@string/app_name")
@Layout(R.layout.main)
public class HelloTransfuse {

    @Inject
    @View(R.id.greeting)
    TextView greeting;

    @Inject
    Greeter greeter;

    @OnCreate
    public void displayGreeting() {
        greeting.setText(greeter.greeting());
    }
}
