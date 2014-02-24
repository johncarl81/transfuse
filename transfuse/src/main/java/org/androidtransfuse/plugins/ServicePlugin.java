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
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.listeners.ServiceOnStartCommand;
import org.androidtransfuse.listeners.ServiceOnUnbind;
import org.androidtransfuse.util.AndroidLiterals;

/**
 * @author John Ericksen
 */
@Bootstrap
public class ServicePlugin implements TransfusePlugin{
    @Override
    public void run(ConfigurationRepository repository) {
        repository.component(Service.class).method("onCreate").event(OnCreate.class).registration();
        repository.component(Service.class).method("onDestroy").event(OnDestroy.class);
        repository.component(Service.class).method("onLowMemory").event(OnLowMemory.class);
        repository.component(Service.class).method("onRebind", AndroidLiterals.INTENT).event(OnRebind.class);
        repository.component(Service.class).method("onConfigurationChanged", AndroidLiterals.CONTENT_CONFIGURATION).event(OnConfigurationChanged.class);
        repository.component(Service.class).extending(AndroidLiterals.INTENT_SERVICE)
                .method("onHandleIntent", AndroidLiterals.INTENT).event(OnHandleIntent.class);

        repository.component(Service.class).callThroughEvent(ServiceOnStartCommand.class);
        repository.component(Service.class).callThroughEvent(ServiceOnUnbind.class);
    }
}
