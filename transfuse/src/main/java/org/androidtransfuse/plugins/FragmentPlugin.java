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
public class FragmentPlugin implements TransfusePlugin{
    @Override
    public void run(ConfigurationRepository repository) {
        repository.component(Fragment.class).method("onActivityCreated", "android.os.Bundle").event(OnActivityCreated.class);
        repository.component(Fragment.class).method("onStart").event(OnStart.class);
        repository.component(Fragment.class).method("onResume").event(OnResume.class);
        repository.component(Fragment.class).method("onPause").event(OnPause.class);
        repository.component(Fragment.class).method("onStop").event(OnStop.class);
        repository.component(Fragment.class).method("onDestroyView").event(OnDestroyView.class);
        repository.component(Fragment.class).method("onDestroy").event(OnDestroy.class);
        repository.component(Fragment.class).method("onDetach").event(OnDetach.class);
        repository.component(Fragment.class).method("onLowMemory").event(OnLowMemory.class);
        repository.component(Fragment.class).method("onActivityResult", "int", "int", "android.content.Intent").event(OnActivityResult.class);
        repository.component(Fragment.class).method("onConfigurationChanged", "android.content.res.Configuration").event(OnConfigurationChanged.class);

        repository.component(Fragment.class)
                .extending("android.support.v4.app.ListFragment")
                .method("onListItemClick", "android.widget.ListView", "android.view.View", "int", "long").event(OnListItemClick.class);
    }
}
