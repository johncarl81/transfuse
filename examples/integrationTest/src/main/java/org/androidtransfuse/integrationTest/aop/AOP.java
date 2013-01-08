/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.integrationTest.aop;

import android.util.Log;
import android.view.View;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * Tests the Aspect Oriented Programming Method Interceptor.
 *
 * @author John Ericksen
 */
@Activity(name = "AOPActivity", label = "AOP")
@Layout(R.layout.aop)
@DeclareField
@AOPInterceptor
public class AOP {

    public static final String INTERCEPT_VALUE = "interception";

    @RegisterListener(R.id.aopbutton1)
    private View.OnClickListener aopClick1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                interceptorWithDependency();
            } catch (InterruptedException e) {
                Log.e("error", "InterruptedException while invoking interceptorWithDependency()", e);
            }
        }
    };

    @RegisterListener(R.id.aopbutton2)
    private View.OnClickListener aopClick2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            interceptMeWithReturn();
        }
    };

    public void interceptMe() {
    }

    public String interceptMeWithReturn() {
        return INTERCEPT_VALUE;
    }

    @DependencyInterceptor
    public String interceptorWithDependency() throws InterruptedException {
        Thread.sleep(ONE_SECOND);

        return "@DependencyInterceptor";
    }
}
