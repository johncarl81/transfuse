package org.androidtransfuse.integrationTest.register;

import android.view.View;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.button)
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

    @RegisterListener(value = R.id.button5)
    private View.OnClickListener listener5 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            listener5Clicked = true;
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

    @OnPause
    public void keepInActivity() {
    }
}
