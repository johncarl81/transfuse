/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.integrationTest.scope;

import android.view.View;
import android.widget.EditText;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.util.DeclareField;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
@Activity(name = "CustomScopeActivity", label = "Custom Scope")
@Layout(R.layout.customscope)
@DeclareField
public class CustomScopeExample {

    private static final String SCOPE_TEXT = "scopeText";
    private static final ScopeKey<String> SCOPE_TEXT_KEY = ScopeKey.of(String.class).annotatedBy(SCOPE_TEXT);

    @Inject
    @ScopeReference(CustomScope.class)
    private MapScope scope;

    @Inject
    @org.androidtransfuse.annotations.View(R.id.custonscopetext)
    private EditText scopeText;

    @OnCreate
    public void update(){
        if(scope.isStarted()){
            scopeText.setText(scope.getScopedObject(SCOPE_TEXT_KEY, new Provider<String>() {
                @Override
                public String get() {
                    return "";
                }
            }));
        }
    }

    @RegisterListener(R.id.refreshbutton)
    private View.OnClickListener refreshScope = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            update();
        }
    };

    @RegisterListener(R.id.updatebutton)
    private View.OnClickListener updateScope = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(scope.isStarted()){
                scope.seed(ScopeKey.of(String.class).annotatedBy(SCOPE_TEXT), scopeText.getText().toString());
            }
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

    public MapScope getScope() {
        return scope;
    }
}
