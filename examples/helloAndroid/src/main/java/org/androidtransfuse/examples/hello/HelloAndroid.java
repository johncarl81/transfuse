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
package org.androidtransfuse.examples.hello;

import android.widget.TextView;
import org.androidtransfuse.annotations.*;

import javax.inject.Inject;

@Activity(label = "@string/app_name")
@IntentFilter({
        @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
        @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
})
@Layout(R.layout.main)
public class HelloAndroid {

    @Inject
    @View(R.id.textview)
    private TextView textView;

    @Inject
    @Resource(R.string.hello)
    private String helloText;

    @OnCreate
    public void hello() {
        textView.setText(helloText);
    }
}
