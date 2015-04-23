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
package org.androidtransfuse.processor;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.config.TransfuseAndroidModule;
import org.androidtransfuse.transaction.TransactionProcessorBuilder;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author John Ericksen
 */
@Singleton
public class TransfuseProcessor {

    private final boolean stacktrace;
    private final Logger log;
    private final GeneratorRepository generatorRepository;

    @Inject
    public TransfuseProcessor(GeneratorRepository generatorRepository, Logger log, @Named(TransfuseAndroidModule.STACKTRACE) boolean stacktrace) {
        this.generatorRepository = generatorRepository;
        this.log = log;
        this.stacktrace = stacktrace;
    }

    public void submit(Class<? extends Annotation> componentAnnotation, Collection<Provider<ASTType>> astProviders) {
        for (Provider<ASTType> astProvider : astProviders) {
           submit(componentAnnotation, astProvider);
        }
    }

    public void submit(Class<? extends Annotation> componentAnnotation, Provider<ASTType> astProvider) {
        log.debug("Submitted " + astProvider + " for processing under @" + componentAnnotation.getSimpleName() + ".");
        TransactionProcessorBuilder<Provider<ASTType>, ?> builder = generatorRepository.getComponentBuilder(componentAnnotation);
        if (builder == null) {
            throw new TransfuseAnalysisException("Builder for type " + componentAnnotation.getName() + " not found");
        }

        builder.submit(astProvider);
    }

    public void execute() {
        generatorRepository.getProcessor().execute();
    }

    public void logErrors() {
        boolean errored = false;
        ImmutableSet.Builder<Exception> exceptions = ImmutableSet.builder();

        for (TransactionProcessorBuilder transactionProcessor : generatorRepository.getComponentBuilders().values()) {
            if (!transactionProcessor.getTransactionProcessor().isComplete()) {
                errored = true;
                exceptions.addAll(transactionProcessor.getTransactionProcessor().getErrors());
            }
        }

        if (errored) {
            if (stacktrace) {
                for (Exception exception : exceptions.build()) {
                    log.error("Code generation did not complete successfully.", exception);
                }
            }
            else{
                log.error("Code generation did not complete successfully.  For more details add the compiler argument -AtransfuseStacktrace");
            }
        }
    }
}
