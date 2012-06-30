package org.androidtransfuse.integrationTest.scope;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ScopeOneActivity", label = "Scope One")
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
