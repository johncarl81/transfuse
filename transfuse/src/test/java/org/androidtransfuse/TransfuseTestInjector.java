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
package org.androidtransfuse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import org.androidtransfuse.config.TransfuseGenerateGuiceModule;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;
import org.androidtransfuse.model.manifest.Application;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.util.EmptyRResource;
import org.androidtransfuse.util.JavaUtilLogger;
import org.androidtransfuse.util.TestingScope;

/**
 * @author John Ericksen
 */
public class TransfuseTestInjector {

    public static void inject(Object input) {
        Injector injector = getInjector(input);
        injector.injectMembers(input);
    }

    public static Injector getInjector(Object input) {
        Manifest manifest = new Manifest();

        manifest.getApplications().add(new Application());

        TestingScope configurationScope = new TestingScope();

        configurationScope.seed(RResource.class, new EmptyRResource());
        configurationScope.seed(Key.get(Manifest.class, Names.named(TransfuseGenerateGuiceModule.ORIGINAL_MANIFEST)), manifest);

        return Guice.createInjector(Stage.DEVELOPMENT,
                new TransfuseSetupGuiceModule(new JavaUtilLogger(input), new NoOpFiler(), new NoOpElements(), new TestingScope()),
                new TransfuseGenerateGuiceModule(configurationScope));
    }
}
