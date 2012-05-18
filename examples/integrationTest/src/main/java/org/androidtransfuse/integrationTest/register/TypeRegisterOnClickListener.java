package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@RegisterListener(R.id.button4)
public class TypeRegisterOnClickListener implements View.OnClickListener {

    @Inject
    private Context context;

    private boolean clicked = false;

    @Override
    public void onClick(View v) {

        clicked = true;

        Toast toast = Toast.makeText(context, "Type Registration", 1000);
        toast.show();
    }

    public boolean isClicked() {
        return clicked;
    }
}
