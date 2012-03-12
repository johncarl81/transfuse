package org.androidtransfuse.integrationTest.scope;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ScopeOneActivity")
@Layout(R.layout.main)
public class ScopeOne {

    @Inject
    private SingletonObject singleton;

    public SingletonObject getSingleton() {
        return singleton;
    }

    @OnPause
    public void keepInActivity() {
    }
}
