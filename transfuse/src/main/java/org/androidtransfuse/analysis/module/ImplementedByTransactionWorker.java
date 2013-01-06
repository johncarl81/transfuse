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

import com.google.inject.ImplementedBy;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.androidtransfuse.util.matcher.Matchers;

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

    @Inject
    public ImplementedByTransactionWorker(InjectionNodeBuilderRepositoryFactory injectionNodeBuilders, VariableInjectionBuilderFactory variableInjectionBuilderFactory, ASTTypeBuilderVisitor astTypeBuilderVisitor) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
    }

    @Override
    public Void innerRun(Provider<ASTType> astTypeProvider) {

        ASTType astType = astTypeProvider.get();

        ImplementedBy annotation = astType.getAnnotation(ImplementedBy.class);

        if (annotation != null) {
            TypeMirror implementedClass = getTypeMirror(new ImplementedByClassTypeMirrorRunnable(annotation));

            ASTType implAstType = implementedClass.accept(astTypeBuilderVisitor, null);

            if (!implAstType.inheritsFrom(astType)) {
                throw new TransfuseAnalysisException("ImplementedBy configuration points to a class that doesn't inherit from the given base class");
            }

            injectionNodeBuilders.putModuleConfig(Matchers.type(astType).build(),
                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(implAstType));
        }
        return null;
    }

    private static class ImplementedByClassTypeMirrorRunnable extends TypeMirrorRunnable<ImplementedBy> {

        protected ImplementedByClassTypeMirrorRunnable(ImplementedBy annotation) {
            super(annotation);
        }

        @Override
        public void run(ImplementedBy annotation) {
            annotation.value();
        }
    }
}
