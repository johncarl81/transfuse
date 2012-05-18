package org.androidtransfuse.integrationTest.register;

import android.view.View;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.button)
public class Register {

    @Inject
    @RegisterListener(value = R.id.button1, interfaces = View.OnClickListener.class)
    private RegisterOnClickListener listener;

    @Inject
    @RegisterListener(value = R.id.button2)
    private RegisterOnClickListener listener2;

    @Inject
    private MethodOnClickListener listener3;

    @Inject
    private TypeRegisterOnClickListener listener4;

    @RegisterListener(value = R.id.button3)
    public MethodOnClickListener getListener() {
        return listener3;
    }
}
