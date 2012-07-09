package org.androidtransfuse.integrationTest.scope;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ScopeTwoActivity", label = "Scope One")
@Layout(R.layout.scoped)
@DeclareField
public class ScopeTwo {

    @Inject
    private SingletonObject singleton;

    @Inject
    private ScopeManager scopeManager;

    public SingletonObject getSingleton() {
        return singleton;
    }
}
