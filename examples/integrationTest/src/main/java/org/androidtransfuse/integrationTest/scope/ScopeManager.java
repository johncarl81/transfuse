package org.androidtransfuse.integrationTest.scope;

import android.util.Log;
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
    public void readSingleton(){
        scopedText.setText(singleton.getValue());
    }

    @OnPause
    public void updateSingleton() {
        singleton.setValue(scopedText.getText().toString());
    }
}
