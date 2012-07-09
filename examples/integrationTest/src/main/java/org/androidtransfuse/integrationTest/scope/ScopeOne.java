package org.androidtransfuse.integrationTest.scope;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ScopeOneActivity", label = "Scope One")
@Layout(R.layout.scoped)
@DeclareField
public class ScopeOne {

    @Inject
    private SingletonObject singleton;

    @Inject
    private ScopeManager scopeManager;

    public SingletonObject getSingleton() {
        return singleton;
    }
}
