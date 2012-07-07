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

    @Inject
    @View(R.id.state1)
    private TextView state1;

    @Inject
    @View(R.id.state2)
    private TextView state2;

    @OnSaveInstanceState
    public void saveText(Bundle bundle){
        bundle.putCharSequence(TEXT_KEY, state1.getText());
    }

    @OnRestoreInstanceState
    public void restoreText(Bundle bundle){
        state1.setText(bundle.getCharSequence(TEXT_KEY));
    }

    public TextView getState1() {
        return state1;
    }
}
