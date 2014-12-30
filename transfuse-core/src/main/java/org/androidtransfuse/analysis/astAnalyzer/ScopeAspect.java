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
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.gen.variableBuilder.VariableBuilder;

/**
 * Aspect to associate with the InjectionNode a builder used to build the current type in the given scope.
 *
 * @author John Ericksen
 */
public class ScopeAspect {

    private final VariableBuilder scopeBuilder;

    public ScopeAspect(VariableBuilder scopeBuilder) {
        this.scopeBuilder = scopeBuilder;
    }

    public VariableBuilder getScopeBuilder() {
        return scopeBuilder;
    }
}
