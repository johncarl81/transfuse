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

import org.androidtransfuse.annotations.ContextScope;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.gen.scopeBuilder.ContextScopeAspectFactory;
import org.androidtransfuse.gen.scopeBuilder.SingletonScopeAspectFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepositoryProvider implements Provider<ScopeAspectFactoryRepository> {

    private final SingletonScopeAspectFactory singletonScopeAspectFactory;
    private final ContextScopeAspectFactory contextScopeAspectFactory;

    @Inject
    public ScopeAspectFactoryRepositoryProvider(SingletonScopeAspectFactory singletonScopeAspectFactory,
                                                ContextScopeAspectFactory contextScopeAspectFactory) {
        this.singletonScopeAspectFactory = singletonScopeAspectFactory;
        this.contextScopeAspectFactory = contextScopeAspectFactory;
    }


    @Override
    public ScopeAspectFactoryRepository get() {
        ScopeAspectFactoryRepository scopedVariableBuilderRepository = new ScopeAspectFactoryRepository();

        scopedVariableBuilderRepository.putAspectFactory(TransfuseModule.class, singletonScopeAspectFactory);
        scopedVariableBuilderRepository.putAspectFactory(Singleton.class, singletonScopeAspectFactory);
        scopedVariableBuilderRepository.putAspectFactory(ContextScope.class, contextScopeAspectFactory);

        return scopedVariableBuilderRepository;
    }
}
