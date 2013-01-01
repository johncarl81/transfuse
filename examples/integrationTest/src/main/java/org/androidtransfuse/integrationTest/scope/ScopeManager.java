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
package org.androidtransfuse.integrationTest.scope;

import android.widget.EditText;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ScopeManager {

    @Inject
    private SingletonObject singleton;

    @Inject
    @View(R.id.scopedText)
    private EditText scopedText;

    public SingletonObject getSingleton() {
        return singleton;
    }

    @OnCreate
    public void readSingleton() {
        scopedText.setText(singleton.getValue());
    }

    @OnPause
    public void updateSingleton() {
        singleton.setValue(scopedText.getText().toString());
    }
}
