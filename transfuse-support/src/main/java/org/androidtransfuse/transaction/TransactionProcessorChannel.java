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
package org.androidtransfuse.transaction;

import com.google.common.collect.ImmutableSet;

import java.util.Map;

/**
 * @author John Ericksen
 */
public class TransactionProcessorChannel<V, R, R2> implements TransactionProcessor<Map<V, R>, R2> {

    private TransactionProcessor<V, R> completionProcessor;
    private TransactionProcessorPool<Map<V, R>, R2> afterCompletionProcessor;
    private TransactionFactory<Map<V, R>, R2> completionTransactionFactory;

    public TransactionProcessorChannel(TransactionProcessor<V, R> completionProcessor,
                                       TransactionProcessorPool<Map<V, R>, R2> afterCompletionProcessor,
                                       TransactionFactory<Map<V, R>, R2> completionTransactionFactory) {
        this.completionProcessor = completionProcessor;
        this.afterCompletionProcessor = afterCompletionProcessor;
        this.completionTransactionFactory = completionTransactionFactory;
    }

    @Override
    public void execute() {

        boolean workToComplete = !completionProcessor.isComplete();
        completionProcessor.execute();

        if (workToComplete && completionProcessor.isComplete()) {
            Transaction<Map<V, R>, R2> completionTransaction = completionTransactionFactory.buildTransaction(completionProcessor.getResults());
            afterCompletionProcessor.submit(completionTransaction);
            afterCompletionProcessor.execute();
        }
    }

    @Override
    public boolean isComplete() {
        return completionProcessor.isComplete() && afterCompletionProcessor.isComplete();
    }

    @Override
    public ImmutableSet<Exception> getErrors() {
        ImmutableSet.Builder<Exception> exceptions = ImmutableSet.builder();
        exceptions.addAll(completionProcessor.getErrors());
        exceptions.addAll(afterCompletionProcessor.getErrors());

        return exceptions.build();
    }

    @Override
    public Map<Map<V, R>, R2> getResults() {
        return afterCompletionProcessor.getResults();
    }
}
