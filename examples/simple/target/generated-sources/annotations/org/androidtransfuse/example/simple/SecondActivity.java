
package org.androidtransfuse.example.simple;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import org.androidtransfuse.example.simple.R.array;
import org.androidtransfuse.util.InjectionUtil;

public class SecondActivity
    extends Activity
{

    private SecondActivityDelegate secondActivityDelegate_0;

    public void onCreate(Bundle savedInstanceState) {
        long time;
        time = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(2130903041);
        secondActivityDelegate_0 = new SecondActivityDelegate(((TextView) this.findViewById(org.androidtransfuse.example.simple.R.id.text2)));
        InjectionUtil.setField(secondActivityDelegate_0, 0, "testExtra", ((String) this.getIntent().getExtras().get("testExtra")));
        InjectionUtil.setField(secondActivityDelegate_0, 0, "simpleStringArray", this.getApplication().getResources().getStringArray(array.simpleStringArray));
        InjectionUtil.setField(secondActivityDelegate_0, 0, "textView3", ((TextView) this.findViewById(org.androidtransfuse.example.simple.R.id.text3)));
        secondActivityDelegate_0 .onCreate();
        Log.i("timer", Long.toString((System.currentTimeMillis()-time)));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        secondActivityDelegate_0 .update();
        return true;
    }

}
