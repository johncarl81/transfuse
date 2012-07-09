package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
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
