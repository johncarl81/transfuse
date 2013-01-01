/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.integrationTest.broadcastReceiver;

import android.content.Context;
import android.widget.Toast;
import org.androidtransfuse.annotations.BroadcastReceiver;
import org.androidtransfuse.annotations.Intent;
import org.androidtransfuse.annotations.IntentType;
import org.androidtransfuse.annotations.OnReceive;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
@BroadcastReceiver
@Intent(type = IntentType.ACTION, name = Toaster.INTENT)
public class Toaster {

    public static final String INTENT = "Toaster";

    private static boolean onReceive = false;

    @Inject
    private Context context;

    @OnReceive
    public void onReceive() {
        Toast.makeText(context, "Broadcast Received", ONE_SECOND).show();
        onReceiveCalled();
    }

    private static void onReceiveCalled() {
        onReceive = true;
    }

    public static boolean isOnReceive() {
        return onReceive;
    }
}
