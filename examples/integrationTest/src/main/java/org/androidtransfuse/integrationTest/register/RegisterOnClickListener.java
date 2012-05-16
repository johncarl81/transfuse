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

    @Override
    public boolean onLongClick(View v) {

        Toast toast = Toast.makeText(context, "Long Click", 1000);
        toast.show();

        return true;
    }

    @Override
    public void onClick(View v) {
        Toast toast = Toast.makeText(context, "Click", 1000);
        toast.show();
    }
}
