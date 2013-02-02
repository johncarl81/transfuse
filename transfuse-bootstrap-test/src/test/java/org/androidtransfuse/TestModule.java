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

import org.androidtransfuse.annotations.Bind;
import org.androidtransfuse.annotations.Bindings;
import org.androidtransfuse.annotations.Install;
import org.androidtransfuse.annotations.Provides;
import org.androidtransfuse.bootstrap.BootstrapModule;

import javax.inject.Named;

/**
 * @author John Ericksen
 */
@BootstrapModule
@Bindings({
        @Bind(type = DependencyBase.class, to = Dependency.class)
})
@Install({
        InjectorExample.class
})
public class TestModule {

    public static final String MODULE_VALUE = "moduleValue";

    private String moduleValue;

    public TestModule(String moduleValue){
        this.moduleValue = moduleValue;
    }

    @Provides
    @Named(MODULE_VALUE)
    public String getModuleValue(){
        return moduleValue;
    }
}
