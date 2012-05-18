package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class RegisterOnClickListener implements View.OnClickListener, View.OnLongClickListener {

    @Inject
    private Context context;

    private boolean clicked = false;
    private boolean longClicked = false;

    @Override
    public boolean onLongClick(View v) {

        longClicked = true;

        Toast toast = Toast.makeText(context, "Long Click", 1000);
        toast.show();

        return true;
    }

    @Override
    public void onClick(View v) {

        clicked = true;

        Toast toast = Toast.makeText(context, "Click", 1000);
        toast.show();
    }

    public boolean isLongClicked() {
        return longClicked;
    }

    public boolean isClicked() {
        return clicked;
    }
}
