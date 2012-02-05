package org.androidtransfuse.example.simple;

import android.widget.Button;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnotherValueImpl implements AnotherValue {

    private SubComponent subComponent;
    @Inject
    @View(R.id.secondActivity)
    private Button secondActivityButton;
    @Inject
    private GotoSecondActivity gotoSecondActivity;
    @Inject
    private ProviderTestValue providerTestValue;

    @Inject
    public AnotherValueImpl(SubComponent subComponent) {
        this.subComponent = subComponent;
    }

    @OnCreate
    public void registerSecondActivity() {
        secondActivityButton.setOnClickListener(gotoSecondActivity);
    }

    public Boolean doWork() {
        return true;
    }
}
