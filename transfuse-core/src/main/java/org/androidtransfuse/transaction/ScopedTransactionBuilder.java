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
package org.androidtransfuse.transaction;

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.CodeGenerationScope;
import org.androidtransfuse.annotations.ScopeReference;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public final class ScopedTransactionBuilder {

    private final Provider<JCodeModel> codeModelProvider;
    private final Provider<FilerSourceCodeWriter> codeWriterProvider;
    private final Provider<FilerResourceWriter> resourceWriterProvider;
    private final EnterableScope codeGenerationScope;

    @Inject
    public ScopedTransactionBuilder(Provider<JCodeModel> codeModelProvider,
                                    Provider<FilerSourceCodeWriter> codeWriterProvider,
                                    Provider<FilerResourceWriter> resourceWriterProvider,
                                    @ScopeReference(CodeGenerationScope.class) EnterableScope codeGenerationScope) {
        this.codeModelProvider = codeModelProvider;
        this.codeWriterProvider = codeWriterProvider;
        this.resourceWriterProvider = resourceWriterProvider;
        this.codeGenerationScope = codeGenerationScope;
    }


    public <T, S> Transaction<T, S> build(Provider<? extends TransactionWorker<T, S>> workerProvider){
        return new Transaction<T, S>(
                new ScopedTransactionWorker<T, S>(codeGenerationScope, new TransactionWorkerProvider<T, S>(workerProvider)
        ));
    }

    public <T, S> Transaction<T, S> build(T value, Provider<? extends TransactionWorker<T, S>> workerProvider) {
        return new Transaction<T, S>(
                value,
                new ScopedTransactionWorker<T, S>(codeGenerationScope, new TransactionWorkerProvider<T, S>(workerProvider))
        );
    }

    public <T, S> TransactionFactory<T, S> buildFactory(Provider<? extends TransactionWorker<T, S>> workerProvider) {
        return new TransactionBuilderFactory<T, S>(workerProvider);
    }

    private class TransactionBuilderFactory<T, S> implements TransactionFactory<T, S>{

        private final Provider<? extends TransactionWorker<T, S>> workerProvider;

        public TransactionBuilderFactory(Provider<? extends TransactionWorker<T, S>> workerProvider) {
            this.workerProvider = workerProvider;
        }

        @Override
        public Transaction<T, S> buildTransaction(T value) {
            return build(value, workerProvider);
        }
    }

    private class TransactionWorkerProvider<T, S> implements Provider<TransactionWorker<T, S>> {

        private final Provider<? extends TransactionWorker<T, S>> transactionWorkerProvider;

        private TransactionWorkerProvider(Provider<? extends TransactionWorker<T, S>> transactionWorker) {
            this.transactionWorkerProvider = transactionWorker;
        }

        @Override
        public TransactionWorker<T, S> get() {
            return new CodeGenerationScopedTransactionWorker<T, S>(
                    codeModelProvider.get(),
                    codeWriterProvider.get(),
                    resourceWriterProvider.get(),
                    transactionWorkerProvider.get());
        }
    }
}
