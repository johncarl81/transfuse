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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.gen.componentBuilder.RegistrationGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect to associate an element (method, field or type) with a listener registration.  This triggers the code generator to
 * register the given element with the identified resource.  Typically this means generating an on*() like so:
 *
 * `resource.onClickListener(field);`
 *
 * @author John Ericksen
 */
public class RegistrationAspect {

    private final List<RegistrationGenerator> registrationBuilders = new ArrayList<RegistrationGenerator>();

    public void addRegistrationBuilders(List<RegistrationGenerator> builders){
        registrationBuilders.addAll(builders);
    }

    public List<RegistrationGenerator> getRegistrationBuilders() {
        return registrationBuilders;
    }
}
