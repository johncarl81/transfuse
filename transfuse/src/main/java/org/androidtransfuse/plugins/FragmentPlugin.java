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
import org.androidtransfuse.adapter.ASTPrimitiveType;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.listeners.FragmentMenuComponent;
import org.androidtransfuse.util.AndroidLiterals;

/**
 * @author John Ericksen
 */
@Bootstrap
public class FragmentPlugin implements TransfusePlugin{
    @Override
    public void run(ConfigurationRepository repository) {
        repository.component(Fragment.class).method("onActivityCreated", AndroidLiterals.BUNDLE).event(OnActivityCreated.class);
        repository.component(Fragment.class).method("onStart").event(OnStart.class);
        repository.component(Fragment.class).method("onResume").event(OnResume.class);
        repository.component(Fragment.class).method("onPause").event(OnPause.class);
        repository.component(Fragment.class).method("onStop").event(OnStop.class);
        repository.component(Fragment.class).method("onDestroyView").event(OnDestroyView.class);
        repository.component(Fragment.class).method("onDestroy").event(OnDestroy.class);
        repository.component(Fragment.class).method("onDetach").event(OnDetach.class);
        repository.component(Fragment.class).method("onLowMemory").event(OnLowMemory.class);
        repository.component(Fragment.class).method("onActivityResult", ASTPrimitiveType.INT, ASTPrimitiveType.INT, AndroidLiterals.INTENT).event(OnActivityResult.class);
        repository.component(Fragment.class).method("onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION).event(OnConfigurationChanged.class);

        repository.component(Fragment.class)
                .extending(AndroidLiterals.LIST_FRAGMENT)
                .method("onListItemClick", AndroidLiterals.LIST_VIEW, AndroidLiterals.VIEW, ASTPrimitiveType.INT, ASTPrimitiveType.LONG).event(OnListItemClick.class);

        repository.component(Fragment.class).callThroughEvent(FragmentMenuComponent.class);
    }
}
