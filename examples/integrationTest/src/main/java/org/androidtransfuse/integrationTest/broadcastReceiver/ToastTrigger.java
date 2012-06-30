package org.androidtransfuse.integrationTest.broadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "Broadcast Receiver")
@Layout(R.layout.toasttrigger)
public class ToastTrigger {

    @Inject
    private Context context;

    @RegisterListener(R.id.toastbutton)
    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            context.sendBroadcast(new Intent(Toaster.INTENT));
        }
    };
}
