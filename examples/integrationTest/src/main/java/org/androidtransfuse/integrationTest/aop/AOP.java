package org.androidtransfuse.integrationTest.aop;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;

/**
 * @author John Ericksen
 */
@Activity(name = "AOPActivity")
@Layout(R.layout.main)
public class AOP {

    public static final String INTERCEPT_VALUE = "interception";

    @AOPInterceptor
    public void interceptMe() {
    }

    @AOPInterceptor
    public String interceptMeWithReturn() {
        return INTERCEPT_VALUE;
    }

    @DependencyInterceptor
    public void interceptorWithDependency() {
    }

    @OnPause
    public void keepInActivity() {
    }
}
