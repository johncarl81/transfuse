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

import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
@Bootstrap
public class Exercise {

    @Inject
    private Dependency dependency;
    @Inject
    private Provider<Dependency> dependencyProvider;
    @Inject
    private DependencyBase dependencyBase;
    @Inject
    private FactoryExample factory;
    @Inject
    private SingletonDependency singleton;

    public Exercise(){
        Bootstraps.inject(this);
    }

    public Dependency getDependency() {
        return dependency;
    }

    public Provider<Dependency> getDependencyProvider() {
        return dependencyProvider;
    }

    public DependencyBase getDependencyBase() {
        return dependencyBase;
    }

    public FactoryExample getFactory() {
        return factory;
    }

    public SingletonDependency getSingleton() {
        return singleton;
    }
}

