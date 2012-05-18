package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

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

        Toast toast = Toast.makeText(context, "Method Registration", 1000);
        toast.show();
    }

    public boolean isClicked() {
        return clicked;
    }
}
