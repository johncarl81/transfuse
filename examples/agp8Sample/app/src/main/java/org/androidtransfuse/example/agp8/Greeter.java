/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.example.agp8;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * A jakarta.inject @Singleton service injected into the Transfuse activity.
 *
 * @author John Ericksen
 */
@Singleton
public class Greeter {

    @Inject
    public Greeter() {
    }

    public String greeting() {
        return "Hello from Transfuse with jakarta.inject on AGP 8";
    }
}
