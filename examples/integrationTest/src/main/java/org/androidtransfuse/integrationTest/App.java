package org.androidtransfuse.integrationTest;

import android.widget.Button;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.View;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Application(name = "IntegrationApp")
public class App {

    @Inject
    @View(R.id.notifyButton)
    private Button button;


}
