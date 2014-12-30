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
package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.annotations.TransfuseModule;
import org.androidtransfuse.gen.scopeBuilder.CustomScopeAspectFactoryFactory;
import org.androidtransfuse.gen.scopeBuilder.SingletonScopeAspectFactory;
import org.androidtransfuse.scope.ApplicationScope;
import org.androidtransfuse.scope.ConcurrentDoubleLockingScope;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepositoryProvider implements Provider<InjectionNodeBuilderRepository> {

    private final SingletonScopeAspectFactory singletonScopeAspectFactory;
    private final CustomScopeAspectFactoryFactory customScopeAspectFactoryFactory;
    private final ASTClassFactory astClassFactory;

    @Inject
    public ScopeAspectFactoryRepositoryProvider(SingletonScopeAspectFactory singletonScopeAspectFactory,
                                                CustomScopeAspectFactoryFactory customScopeAspectFactoryFactory,
                                                ASTClassFactory astClassFactory) {
        this.singletonScopeAspectFactory = singletonScopeAspectFactory;
        this.customScopeAspectFactoryFactory = customScopeAspectFactoryFactory;
        this.astClassFactory = astClassFactory;
    }


    @Override
    public InjectionNodeBuilderRepository get() {
        InjectionNodeBuilderRepository scopedVariableBuilderRepository = new InjectionNodeBuilderRepository(astClassFactory);

        ASTType concurrentScopeType = astClassFactory.getType(ConcurrentDoubleLockingScope.class);

        scopedVariableBuilderRepository.putScopeAspectFactory(astClassFactory.getType(TransfuseModule.class), concurrentScopeType, singletonScopeAspectFactory);
        scopedVariableBuilderRepository.putScopeAspectFactory(astClassFactory.getType(Singleton.class), concurrentScopeType, singletonScopeAspectFactory);
        scopedVariableBuilderRepository.putScopeAspectFactory(
                astClassFactory.getType(ApplicationScope.ApplicationScopeQualifier.class),
                astClassFactory.getType(ApplicationScope.class), customScopeAspectFactoryFactory.buildScopeBuilder(astClassFactory.getType(ApplicationScope.ApplicationScopeQualifier.class)));

        return scopedVariableBuilderRepository;
    }
}
