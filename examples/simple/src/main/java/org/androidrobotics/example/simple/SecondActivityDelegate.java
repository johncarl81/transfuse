package org.androidrobotics.example.simple;

import android.widget.TextView;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnTouch;
import org.androidrobotics.annotations.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity("SecondActivity")
@Layout(R.layout.second)
public class SecondActivityDelegate {

    private TextView textView;

    @Inject
    public SecondActivityDelegate(@View(R.id.text2) TextView textView) {
        this.textView = textView;
    }

    @OnTouch
    public void update() {
        textView.setText("touched");
    }

    public TextView getTextView() {
        return textView;
    }
}
