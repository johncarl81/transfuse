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
package org.androidtransfuse.integrationTest.activtyResult;

import android.content.Intent;
import android.widget.EditText;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.annotations.View;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.result_two)
public class ResultTwo {

    private EditText inputText;
    private android.app.Activity activity;

    @Inject
    public ResultTwo(@View(R.id.resulttwotext) EditText inputText, android.app.Activity activity) {
        this.inputText = inputText;
        this.activity = activity;
    }

    @RegisterListener(R.id.resulttwobutton)
    public android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {

        @Override
        public void onClick(android.view.View view) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(ResultOne.RESULT_KEY, inputText.getText().toString());
            activity.setResult(android.app.Activity.RESULT_OK, returnIntent);
            activity.finish();
        }
    };
}
