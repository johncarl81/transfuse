package org.androidrobotics.example.simple;

import android.app.Application;
import android.view.View;
import android.widget.Toast;
import org.androidrobotics.annotations.Asynchronous;
import org.androidrobotics.annotations.UIThread;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LateReturnListener implements View.OnClickListener {

    @Inject
    private Application context;

    @Override
    @Asynchronous
    public void onClick(View view) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyUIThread();
    }

    @UIThread
    public void notifyUIThread() {
        Toast toast = Toast.makeText(context, "delayed hello world", 1000);
        toast.show();
    }
}
