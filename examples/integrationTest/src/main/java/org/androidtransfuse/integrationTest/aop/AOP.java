package org.androidtransfuse.integrationTest.aop;

import android.view.View;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;

/**
 * Tests the Aspect Oriented Programming Method Interceptor.
 *
 * @author John Ericksen
 */
@Activity(name = "AOPActivity")
@Layout(R.layout.aop)
public class AOP {

    private static final int ONE_SECOND = 1000;
    public static final String INTERCEPT_VALUE = "interception";

    @RegisterListener(R.id.aopbutton1)
    public View.OnClickListener aopclick1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                interceptorWithDependency();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @RegisterListener(R.id.aopbutton2)
    public View.OnClickListener aopclick2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            interceptMeWithReturn();
        }
    };

    @AOPInterceptor
    public void interceptMe() {
    }

    @AOPInterceptor
    public String interceptMeWithReturn() {
        return INTERCEPT_VALUE;
    }

    @DependencyInterceptor
    public String interceptorWithDependency() throws InterruptedException {
        Thread.sleep(ONE_SECOND);

        return "@DependencyInterceptor";
    }

    @OnPause
    public void keepInActivity() {
    }
}
