/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.integrationTest.inject;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author John Ericksen
 */
public class AssistedDoubleTarget {

    public static final String ONE = "one";
    public static final String TWO = "two";

    private final AssistedDependency dependencyOne;
    private final AssistedDependency dependencyTwo;

    @Inject
    public AssistedDoubleTarget(@Named(TWO) AssistedDependency dependencyTwo, @Named(ONE) AssistedDependency dependencyOne) {
        this.dependencyOne = dependencyOne;
        this.dependencyTwo = dependencyTwo;
    }

    public AssistedDependency getDependencyOne() {
        return dependencyOne;
    }

    public AssistedDependency getDependencyTwo() {
        return dependencyTwo;
    }
}
