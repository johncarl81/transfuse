package org.androidtransfuse.integrationTest.externalGenerator;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.main)
@DeclareField
public class Test {

    private Target target;
    private Proxied proxied;

    @Inject
    public Test(Target target, Proxied proxied) {
        this.target = target;
        this.proxied = proxied;
    }

    public Target getTarget() {
        return target;
    }

    public Proxied getProxied() {
        return proxied;
    }
}
