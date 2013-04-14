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
package org.androidtransfuse.processor;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.module.ImplementedByTransactionWorker;
import org.androidtransfuse.transaction.TransactionProcessor;
import org.androidtransfuse.transaction.TransactionProcessorBuilder;
import org.androidtransfuse.transaction.TransactionProcessorPool;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ImplementedByProcessorBuilder implements TransactionProcessorBuilder<Provider<ASTType>, Void> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final TransactionProcessorPool<Provider<ASTType>, Void> transactionProcessor;
    private final Provider<ImplementedByTransactionWorker> workerProvider;

    @Inject
    public ImplementedByProcessorBuilder(
            Provider<ImplementedByTransactionWorker> workerProvider,
            ScopedTransactionFactory scopedTransactionFactory) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        this.transactionProcessor = new TransactionProcessorPool<Provider<ASTType>, Void>();
        this.workerProvider = workerProvider;
    }

    @Override
    public void submit(Provider<ASTType> astTypeProvider) {
        transactionProcessor.submit(scopedTransactionFactory.buildTransaction(astTypeProvider, workerProvider));
    }

    @Override
    public TransactionProcessor<Provider<ASTType>, Void> getTransactionProcessor() {
        return transactionProcessor;
    }
}
