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

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.annotations.Provides;
import org.androidtransfuse.gen.variableBuilder.ProvidesInjectionNodeBuilderFactory;
import org.androidtransfuse.gen.variableDecorator.GeneratedProviderInjectionNodeBuilder;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.QualifierPredicate;
import org.androidtransfuse.util.ScopePredicate;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collection;

/**
 * Sets up the @Provides configuration.
 *
 * @author John Ericksen
 */
public class ProvidesProcessor implements MethodProcessor {

    private final ProvidesInjectionNodeBuilderFactory variableInjectionBuilderFactory;
    private final QualifierPredicate qualifierPredicate;
    private final ScopePredicate scopePredicate;
    private final ASTClassFactory astClassFactory;
    private final GeneratedProviderInjectionNodeBuilder generatedProviderInjectionNodeBuilder;
    private final Validator validator;

    @Inject
    public ProvidesProcessor(ProvidesInjectionNodeBuilderFactory variableInjectionBuilderFactory,
                             QualifierPredicate qualifierPredicate,
                             ScopePredicate scopePredicate,
                             ASTClassFactory astClassFactory,
                             GeneratedProviderInjectionNodeBuilder generatedProviderInjectionNodeBuilder,
                             Validator validator) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.qualifierPredicate = qualifierPredicate;
        this.scopePredicate = scopePredicate;
        this.astClassFactory = astClassFactory;
        this.generatedProviderInjectionNodeBuilder = generatedProviderInjectionNodeBuilder;
        this.validator = validator;
    }

    public ModuleConfiguration process(ASTType moduleType, ASTMethod astMethod, ASTAnnotation astAnnotation) {

        ImmutableSet<ASTAnnotation> qualifierAnnotations =
                FluentIterable.from(astMethod.getAnnotations())
                        .filter(qualifierPredicate).toImmutableSet();

        ImmutableSet<ASTAnnotation> scopeAnnotations =
                FluentIterable.from(astMethod.getAnnotations())
                        .filter(scopePredicate).toImmutableSet();

        ASTAnnotation scope = null;
        if(scopeAnnotations.size() > 0){
            scope = scopeAnnotations.iterator().next();
        }

        validate(astMethod, astMethod.getAnnotations());

        return new ProvidesModuleConfiguration(moduleType, qualifierAnnotations, astMethod, scope);
    }

    private void validate(ASTMethod astMethod, Collection<ASTAnnotation> annotations) {
        ImmutableSet<ASTAnnotation> nonQualifierAnnotations =
                FluentIterable.from(annotations)
                        .filter(Predicates.and(
                                Predicates.not(qualifierPredicate),
                                Predicates.not(scopePredicate))).toImmutableSet();

        ImmutableSet<ASTAnnotation> scopeAnnotations =
                FluentIterable.from(annotations)
                        .filter(scopePredicate).toImmutableSet();


        ASTType providesType = astClassFactory.getType(Provides.class);

        for (ASTAnnotation annotation : nonQualifierAnnotations) {
            if(!annotation.getASTType().equals(providesType)){
                //error
                validator.error("@Provides methods may only be anntated with scope or qualifier annotations")
                        .element(astMethod).annotation(annotation).build();
            }
        }

        if(scopeAnnotations.size() > 1){
            for (ASTAnnotation scopeAnnotation : scopeAnnotations) {
                validator.error("Only one scope annotation is allowed per @Provides method")
                                                .element(astMethod).annotation(scopeAnnotation).build();
            }
        }
    }

    private final class ProvidesModuleConfiguration implements ModuleConfiguration {

        private final ASTType moduleType;
        private final ASTMethod astMethod;
        private final ImmutableSet<ASTAnnotation> qualifiers;
        private final ASTAnnotation scope;

        private ProvidesModuleConfiguration(ASTType moduleType, ImmutableSet<ASTAnnotation> qualifiers, ASTMethod astMethod, ASTAnnotation scope) {
            this.moduleType = moduleType;
            this.astMethod = astMethod;
            this.qualifiers = qualifiers;
            this.scope = scope;
        }

        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            InjectionSignature signature = new InjectionSignature(astMethod.getReturnType(), qualifiers);

            configurationRepository.putType(signature, variableInjectionBuilderFactory.buildProvidesBuilder(moduleType, astMethod, scope));

            ASTType providerType = new ASTGenericTypeWrapper(astClassFactory.getType(Provider.class), new LazyTypeParameterBuilder() {
                @Override
                public ImmutableSet<ASTType> buildGenericParameters() {
                    return ImmutableSet.of(astMethod.getReturnType());
                }
            });

            InjectionSignature providerSignature = new InjectionSignature(providerType, qualifiers);

            configurationRepository.putType(providerSignature, generatedProviderInjectionNodeBuilder);
        }
    }
}
