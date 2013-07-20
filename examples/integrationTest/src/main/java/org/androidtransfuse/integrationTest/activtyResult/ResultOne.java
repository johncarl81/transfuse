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
package org.androidtransfuse.integrationTest.activtyResult;

import android.widget.Toast;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnActivityResult;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.integrationTest.SharedVariables;
import org.androidtransfuse.intentFactory.IntentFactory;

import javax.inject.Inject;

@Activity(label = "@string/app_name")
@Layout(R.layout.result_one)
public class ResultOne {

    public static final String RESULT_KEY = "result";
    private static final int REQUEST = 1;

    private android.app.Activity context;
    private IntentFactory intentFactory;

    @Inject
    public ResultOne(IntentFactory intentFactory, android.app.Activity context) {
        this.intentFactory = intentFactory;
        this.context = context;
    }

    @RegisterListener(R.id.resultonebutton)
    public android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {

        @Override
        public void onClick(android.view.View view) {
            android.content.Intent intent = intentFactory.buildIntent(new ResultTwoActivityStrategy());
            context.startActivityForResult(intent, REQUEST);
        }
    };

    @OnActivityResult
    public void result(int requestCode, int resultCode, android.content.Intent data) {
        if (requestCode == REQUEST) {

            if(resultCode == android.app.Activity.RESULT_OK){
                String result=data.getStringExtra(RESULT_KEY);
                Toast.makeText(context, result, SharedVariables.ONE_SECOND).show();
            }
        }
    }
}
