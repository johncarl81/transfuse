package org.androidtransfuse.integrationTest.register;

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
    @RegisterListener(value = R.id.button1)
    public RegisterOnClickListener listener;

    @Inject
    @RegisterListener(value = R.id.button2)
    public RegisterOnClickListener listener2;
}
