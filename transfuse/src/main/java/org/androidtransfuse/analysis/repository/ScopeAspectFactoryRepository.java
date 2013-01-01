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
package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepository {

    private final Map<Class<? extends Annotation>, ScopeAspectFactory> scopeVariableBuilderMap = new HashMap<Class<? extends Annotation>, ScopeAspectFactory>();

    public void putAspectFactory(Class<? extends Annotation> scopeType, ScopeAspectFactory scopeAspectFactory) {
        scopeVariableBuilderMap.put(scopeType, scopeAspectFactory);
    }

    public Set<Class<? extends Annotation>> getScopes() {
        return scopeVariableBuilderMap.keySet();
    }

    public ScopeAspectFactory getScopeAspectFactory(Class<? extends Annotation> scopeType) {
        return scopeVariableBuilderMap.get(scopeType);
    }
}
