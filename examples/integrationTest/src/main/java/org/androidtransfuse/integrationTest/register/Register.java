/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * Tests listener registration of the View.On*Listener implementation to the appropriate view objects.
 *
 * @author John Ericksen
 */
@Activity(label = "Listener Registration")
@Layout(R.layout.button)
@DeclareField
public class Register {

    private boolean listener5Clicked = false;

    @Inject
    @RegisterListener(value = R.id.button1, interfaces = View.OnClickListener.class)
    private RegisterOnClickListener listener1;

    @Inject
    @RegisterListener(value = R.id.button2)
    private RegisterOnClickListener listener2;

    @Inject
    private MethodOnClickListener listener3;

    @Inject
    private TypeRegisterOnClickListener listener4;

    @Inject
    private Context context;

    @RegisterListener(value = R.id.button5)
    private View.OnClickListener listener5 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            listener5Clicked = true;

            Toast toast = Toast.makeText(context, "Method Registration", ONE_SECOND);
            toast.show();
        }
    };

    @RegisterListener(R.id.editText)
    private TextView.OnEditorActionListener onEditListener = new TextView.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Toast toast = Toast.makeText(context, "Text View OnEdit Registration", ONE_SECOND);
            toast.show();

            return true;
        }
    };

    public RegisterOnClickListener getListener1() {
        return listener1;
    }

    public RegisterOnClickListener getListener2() {
        return listener2;
    }

    @RegisterListener(value = R.id.button3)
    public MethodOnClickListener getListener3() {
        return listener3;
    }

    public TypeRegisterOnClickListener getListener4() {
        return listener4;
    }

    public boolean isListener5Clicked() {
        return listener5Clicked;
    }

    public View.OnClickListener getListener5() {
        return listener5;
    }
}
