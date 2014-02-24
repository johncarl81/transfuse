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
package org.androidtransfuse.plugins;

import org.androidtransfuse.ConfigurationRepository;
import org.androidtransfuse.TransfusePlugin;
import org.androidtransfuse.annotations.*;

/**
 * @author John Ericksen
 */
public class ActivityPlugin implements TransfusePlugin{
    @Override
    public void run(ConfigurationRepository repository) {
        repository.component(Activity.class).method("onDestroy").event(OnDestroy.class);
        repository.component(Activity.class).method("onPause").event(OnPause.class);
        repository.component(Activity.class).method("onRestart").event(OnRestart.class);
        repository.component(Activity.class).method("onResume").event(OnResume.class);
        repository.component(Activity.class).method("onStart").event(OnStart.class);
        repository.component(Activity.class).method("onStop").event(OnStop.class);
        repository.component(Activity.class).method("onBackPressed").event(OnBackPressed.class);
        repository.component(Activity.class).method("onPostCreate", "android.os.Bundle").event(OnPostCreate.class);
        repository.component(Activity.class).method("onActivityResult", "int", "int", "android.content.Intent").event(OnActivityResult.class);
        repository.component(Activity.class).method("onSaveInstanceState", "android.os.Bundle").event(OnSaveInstanceState.class);
        repository.component(Activity.class).method("onRestoreInstanceState", "android.os.Bundle").event(OnRestoreInstanceState.class);
        repository.component(Activity.class).extending("android.app.ListActivity")
                .method("onListItemClick", "android.widget.ListView", "android.view.View", "int", "long").event(OnListItemClick.class);
    }
}
