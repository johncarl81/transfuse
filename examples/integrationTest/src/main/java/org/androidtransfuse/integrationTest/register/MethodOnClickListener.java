package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MethodOnClickListener implements View.OnClickListener {

    private static final int ONE_SECOND = 1000;

    @Inject
    private Context context;

    private boolean clicked = false;

    @Override
    public void onClick(View v) {

        clicked = true;

        Toast toast = Toast.makeText(context, "Method Registration", ONE_SECOND);
        toast.show();
    }

    public boolean isClicked() {
        return clicked;
    }
}
