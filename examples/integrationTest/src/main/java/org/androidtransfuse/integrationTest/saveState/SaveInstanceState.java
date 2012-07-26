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
    public android.view.View.OnClickListener stateSaveListener = new android.view.View.OnClickListener() {
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
