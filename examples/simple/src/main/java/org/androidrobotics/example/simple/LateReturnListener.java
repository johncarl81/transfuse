package org.androidrobotics.example.simple;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.androidrobotics.annotations.Asynchronous;
import org.androidrobotics.annotations.UIThread;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LateReturnListener implements View.OnClickListener {

    private static final int TWO_SECONDS = 2000;
    private static final int ONE_SECOND = 1000;

    @Inject
    private Application context;

    @Override
    @Asynchronous
    public void onClick(View view) {
        try {
            Thread.sleep(TWO_SECONDS);
        } catch (InterruptedException e) {
            Log.e("Sleep", "InterruptedException", e);
        }
        notifyUIThread();
    }

    @UIThread
    public void notifyUIThread() {
        Toast toast = Toast.makeText(context, "delayed hello world", ONE_SECOND);
        toast.show();
    }
}