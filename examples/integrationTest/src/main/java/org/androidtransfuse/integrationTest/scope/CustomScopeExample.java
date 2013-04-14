package org.androidtransfuse.integrationTest.scope;

import android.view.View;
import android.widget.EditText;
import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.RegisterListener;
import org.androidtransfuse.annotations.ScopeReference;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.util.Providers;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "CustomScopeActivity", label = "Custom Scope")
@Layout(R.layout.customscope)
public class CustomScopeExample {

    private final static String SCOPE_TEXT = "scopeText";
    private final static ScopeKey<String> SCOPE_TEXT_KEY = ScopeKey.of(String.class).annotatedBy(SCOPE_TEXT);

    @Inject
    @ScopeReference(CustomScope.class)
    private MapScope scope;

    @Inject
    @org.androidtransfuse.annotations.View(R.id.custonscopetext)
    private EditText scopeText;

    @RegisterListener(R.id.refreshbutton)
    private View.OnClickListener refreshScope = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            scope.seed(ScopeKey.of(String.class).annotatedBy(SCOPE_TEXT), scopeText.toString());
        }
    };

    @RegisterListener(R.id.updatebutton)
    private View.OnClickListener updateScope = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            scopeText.setText(scope.getScopedObject(SCOPE_TEXT_KEY, Providers.<String>of(null)));
        }
    };


    @RegisterListener(R.id.startscopebutton)
    private View.OnClickListener startScope = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            scope.enter();
        }
    };

    @RegisterListener(R.id.endscopebutton)
    private View.OnClickListener stopScope = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            scope.exit();
        }
    };


}
