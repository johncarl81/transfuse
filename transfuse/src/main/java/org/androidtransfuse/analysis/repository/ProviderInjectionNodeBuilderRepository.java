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

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class ProviderInjectionNodeBuilderRepository {

    private final Map<ASTType, InjectionNodeBuilder> providerInjectionNodeBuilders = new HashMap<ASTType, InjectionNodeBuilder>();

    public void addProvider(ASTType astType, InjectionNodeBuilder providerInjectionNodeBuilder) {
        providerInjectionNodeBuilders.put(astType, providerInjectionNodeBuilder);
    }

    public InjectionNodeBuilder getProvider(ASTType astType) {
        return providerInjectionNodeBuilders.get(astType);
    }

    public boolean isProviderDefined(ASTType astType) {
        return providerInjectionNodeBuilders.containsKey(astType);
    }
}
