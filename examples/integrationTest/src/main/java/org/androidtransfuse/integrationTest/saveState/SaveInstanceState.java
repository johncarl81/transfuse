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
package org.androidtransfuse.integrationTest.saveState;

import android.os.Bundle;
import android.widget.TextView;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "Save Instance State")
@Layout(R.layout.save_instance_state)
public class SaveInstanceState {

    public static final String TEXT_KEY = "key";

    private CharSequence value = "";

    @Inject
    @View(R.id.stateInput)
    private TextView stateInput;

    @Inject
    @View(R.id.stateOutput)
    private TextView stateOutput;

    @RegisterListener(R.id.stateSave)
    private android.view.View.OnClickListener stateSaveListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            value = stateInput.getText();
            stateInput.setText("");
            updateValueText();
        }
    };

    public void updateValueText(){
        stateOutput.setText(value);
    }

    @OnSaveInstanceState
    public void saveText(Bundle bundle){
        bundle.putCharSequence(TEXT_KEY, value);
    }

    @OnRestoreInstanceState
    public void restoreText(Bundle bundle){
        value = bundle.getCharSequence(TEXT_KEY);
        updateValueText();
    }

    public TextView getStateInput() {
        return stateInput;
    }
}
