package org.androidtransfuse.examples.hello;

import android.widget.TextView;
import org.androidtransfuse.annotations.*;

import javax.inject.Inject;

@Activity(label = "@string/app_name")
@IntentFilters({
        @Intent(type = IntentType.ACTION, name = "android.intent.action.MAIN"),
        @Intent(type = IntentType.CATEGORY, name = "android.intent.category.LAUNCHER")
})
@Layout(R.layout.main)
public class HelloAndroid {

    @Inject
    @View(R.id.textview)
    private TextView textView;

    @Inject
    @Resource(R.string.hello)
    private String helloText;

    @OnCreate
    public void hello() {
        textView.setText(helloText);
    }
}
