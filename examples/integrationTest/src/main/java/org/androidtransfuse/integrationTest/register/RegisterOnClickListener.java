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
package org.androidtransfuse.integrationTest.register;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
public class RegisterOnClickListener implements View.OnClickListener, View.OnLongClickListener {

    @Inject
    private Context context;

    private boolean clicked = false;
    private boolean longClicked = false;

    @Override
    public boolean onLongClick(View v) {

        longClicked = true;

        Toast toast = Toast.makeText(context, "Long Click", ONE_SECOND);
        toast.show();

        return true;
    }

    @Override
    public void onClick(View v) {

        clicked = true;

        Toast toast = Toast.makeText(context, "Click", ONE_SECOND);
        toast.show();
    }

    public boolean isLongClicked() {
        return longClicked;
    }

    public boolean isClicked() {
        return clicked;
    }
}
