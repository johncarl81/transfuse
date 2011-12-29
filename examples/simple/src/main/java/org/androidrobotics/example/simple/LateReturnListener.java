package org.androidrobotics.example.simple;

import android.content.Context;
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

    @Inject
    private Context context;


    @Override
    @Asynchronous
    public void onClick(View view) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("interceptor", "Asynchonous onClick");
    }

    @UIThread
    public void notifyUIThread() {
        Toast toast = Toast.makeText(context, "hello delayed world", 1000);
        toast.show();
    }
}
