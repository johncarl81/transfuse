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
package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.ScopeAspectFactoryRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.matcher.Matcher;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public interface ModuleRepository {

    void addModuleConfiguration(InjectionNodeBuilderRepository repository);

    void putModuleConfig(Matcher<ASTType> build, InjectionNodeBuilder injectionNodeBuilder);

    void putInjectionSignatureConfig(Matcher<InjectionSignature> matcher, InjectionNodeBuilder injectionNodeBuilder);

    void putScopeConfig(ScopeAspectFactoryRepository scopedVariableBuilderRepository);

    void addScopeConfig(ASTType annotation, ASTType scope);

    Collection<ASTType> getInstalledAnnotatedWith(Class<? extends Annotation> annotation);

    void addInstalledComponents(ASTType[] astType);

    ASTType getScope(ASTType astType);

    void putScoped(ASTType scope, ASTType toBeScoped);
}
