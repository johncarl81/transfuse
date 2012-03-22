package org.androidtransfuse.integrationTest.scope;

import android.widget.EditText;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ScopeOneActivity")
@Layout(R.layout.scoped)
public class ScopeOne {

    @Inject
    private SingletonObject singleton;

    @Inject
    private ScopeManager scopeManager;

    public SingletonObject getSingleton() {
        return singleton;
    }

    @OnPause
    public void keepInActivity() {
    }
}
