package org.androidtransfuse.example.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class GotoSecondActivity implements View.OnClickListener {

    @Inject
    private Context context;
    @Inject
    private Activity activity;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, SecondActivity.class);

        intent.putExtra("testExtra", "hello from First Activity");
        context.startActivity(intent);
    }
}
