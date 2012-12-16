/**
 * Copyright 2012 John Ericksen
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

/**
 * @author John Ericksen
 */
public class TransactionProcessorPredefined implements TransactionProcessor {

    private TransactionProcessorPool<Void, Void> transactionProcessor = new TransactionProcessorPool<Void, Void>();

    public TransactionProcessorPredefined(ImmutableSet<Transaction<Void, Void>> transactions) {
        for (Transaction<Void, Void> transaction : transactions) {
            transactionProcessor.submit(transaction);
        }
    }

    @Override
    public void execute() {
        transactionProcessor.execute();
    }

    @Override
    public boolean isComplete() {
        return transactionProcessor.isComplete();
    }

    @Override
    public ImmutableSet<Exception> getErrors() {
        return transactionProcessor.getErrors();
    }
}
