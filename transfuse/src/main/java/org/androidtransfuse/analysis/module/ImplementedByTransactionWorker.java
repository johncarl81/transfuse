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
package org.androidtransfuse.analysis.module;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.ImplementedBy;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.transaction.AbstractCompletionTransactionWorker;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.lang.model.type.TypeMirror;

import static org.androidtransfuse.util.TypeMirrorUtil.getTypeMirror;

/**
 * Sets up the annotated @ImplementedBy interface to be bound to the contained class.
 *
 * @author John Ericksen
 */
public class ImplementedByTransactionWorker extends AbstractCompletionTransactionWorker<Provider<ASTType>, Void> {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private final Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider;
    private final Validator validator;
    private final Logger log;

    @Inject
    public ImplementedByTransactionWorker(InjectionNodeBuilderRepositoryFactory injectionNodeBuilders,
                                          VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                          ASTTypeBuilderVisitor astTypeBuilderVisitor,
                                          Provider<InjectionNodeBuilderRepository> injectionNodeBuilderRepositoryProvider,
                                          Validator validator,
                                          Logger log) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionNodeBuilderRepositoryProvider = injectionNodeBuilderRepositoryProvider;
        this.validator = validator;
        this.log = log;
    }

    @Override
    public Void innerRun(Provider<ASTType> astTypeProvider) {

        ASTType astType = astTypeProvider.get();

        ImplementedBy annotation = astType.getAnnotation(ImplementedBy.class);

        if (annotation != null) {
            TypeMirror implementedClass = getTypeMirror(annotation, "value");

            ASTType implAstType = implementedClass.accept(astTypeBuilderVisitor, null);

            if (!implAstType.inheritsFrom(astType)) {
                validator.error("@ImplementedBy must reference a subclass")
                        .element(astType)
                        .annotation(astType.getASTAnnotation(ImplementedBy.class))
                        .parameter("value")
                        .build();
            }
            else{
                log.debug("Mapping @ImplementedBy " + astType + " -> " + implementedClass);

                InjectionNodeBuilderRepository repository = injectionNodeBuilderRepositoryProvider.get();
                repository.putType(astType, variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(implAstType));
                injectionNodeBuilders.addModuleRepository(repository);
            }
        }

        return null;
    }
}
