package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
public class MethodOnClickListener implements View.OnClickListener {

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
