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

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.androidtransfuse.util.MessagerLogger;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author John Ericksen
 */
public final class TransfuseInjector {

    private TransfuseInjector() {
        //private singleton constructor
    }

    public static Injector buildInjector(ProcessingEnvironment environment) {
        return Guice.createInjector(new TransfuseSetupGuiceModule(
                new MessagerLogger(environment.getMessager()),
                new SynchronizedFiler(environment.getFiler()),
                new SynchronizedElements(environment.getElementUtils()),
                new ThreadLocalScope()),
                new TransfuseGenerateGuiceModule(new MapScope()));
    }
}
