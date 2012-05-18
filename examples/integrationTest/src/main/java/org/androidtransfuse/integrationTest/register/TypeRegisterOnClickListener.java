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

    @Override
    public void onClick(View v) {
        Toast toast = Toast.makeText(context, "Type Registration", 1000);
        toast.show();
    }
}
