package org.androidtransfuse.integrationTest.broadcastReceiver;

import android.content.Context;
import android.widget.Toast;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentType;
import org.androidtransfuse.annotations.OnReceive;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@BroadcastReceiver
@Intent(type = IntentType.ACTION, name = Toaster.INTENT)
public class Toaster {

    public static final String INTENT = "Toaster";
    private static final int ONE_SECOND = 1000;

    private static boolean onReceive = false;

    @Inject
    private Context context;

    @OnReceive
    public void onReceive() {
        Toast.makeText(context, "Toast", ONE_SECOND).show();
        onReceiveCalled();
    }

    private static void onReceiveCalled() {
        onReceive = true;
    }

    public static boolean isOnReceive() {
        return onReceive;
    }
}
