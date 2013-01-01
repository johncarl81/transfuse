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
package org.androidtransfuse.config;

import org.androidtransfuse.annotations.BindProvider;
import org.androidtransfuse.annotations.BindProviders;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventManagerProvider;

@TransfuseModule
@BindProviders(
        @BindProvider(type = EventManager.class, provider = EventManagerProvider.class)
)
public class TransfuseAndroidModule {
}