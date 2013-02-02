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

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.scopeBuilder.ScopeAspectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepository {

    private final Map<ASTType, ScopeAspectFactory> scopeVariableBuilderMap = new HashMap<ASTType, ScopeAspectFactory>();
    private final Map<ASTType, ASTType> scopeAnnotations = new HashMap<ASTType, ASTType>();

    public void putAspectFactory(ASTType scopeAnnotation, ASTType scopeType, ScopeAspectFactory scopeAspectFactory) {
        scopeVariableBuilderMap.put(scopeAnnotation, scopeAspectFactory);
        scopeAnnotations.put(scopeAnnotation, scopeType);
    }

    public Set<ASTType> getScopes() {
        return scopeAnnotations.keySet();
    }

    public Map<ASTType, ASTType> getScopeAnnotations(){
        return scopeAnnotations;
    }

    public ScopeAspectFactory getScopeAspectFactory(ASTType scopeType) {
        return scopeVariableBuilderMap.get(scopeType);
    }
}
