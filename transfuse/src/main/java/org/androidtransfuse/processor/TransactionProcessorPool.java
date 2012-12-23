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
import org.androidtransfuse.analysis.TransfuseAnalysisException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Runs the set of submitted Transactions.  If any of the transactions fails (isComplete() == false) the associated
 * aggregateWorker will not be run and the processor will need to be retried.  This offers some resilience to errors
 * that may be caused from 3rd party inputs that this library has no control over.  By retrying in a later round this
 * gives external factors a chance to fill in any missing code that is required by a given Transaction to complete.
 * Additionally, if any external processors depend on code generated in a Transaction, this approach will generate
 * as much as possible despite encountering any errors.
 *
 * @author John Ericksen
 */
public class TransactionProcessorPool<V, R> implements TransactionProcessor<V, R> {

    private final List<Transaction<V, R>> transactions = new ArrayList<Transaction<V, R>>();

    /**
     * Submit a new transaction to the collection of transactions to execute.
     *
     * @param transaction
     */
    public void submit(Transaction<V, R> transaction) {
        transactions.add(transaction);
    }

    /**
     * Executes the submitted work and if all transactions complete, executes the aggregate on the aggregateWorker.
     */
    public void execute() {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Transaction<V, R> transaction : transactions) {
            if (!transaction.isComplete()) {
                executorService.execute(transaction);
            }
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new TransfuseAnalysisException("Pool executor interrupted", e);
        }
    }

    public Map<V, R> getResults() {
        Map<V, R> aggregate = new HashMap<V, R>();

        for (Transaction<V, R> transaction : transactions) {
            if (transaction.isComplete()) {
                aggregate.put(transaction.getValue(), transaction.getResult());
            } else {
                return Collections.EMPTY_MAP;
            }
        }

        return aggregate;
    }

    /**
     * Returns the completion status of the set of transactions.  Will only return complete = true if all the transactions
     * have completed successfully.
     *
     * @return transaction completion status
     */
    public boolean isComplete() {
        for (Transaction<V, R> transaction : transactions) {
            if (!transaction.isComplete()) {
                return false;
            }
        }
        return true;
    }

    public ImmutableSet<Exception> getErrors() {
        ImmutableSet.Builder<Exception> exceptions = ImmutableSet.builder();
        for (Transaction<V, R> transaction : transactions) {
            if (!transaction.isComplete() && transaction.getError() != null) {
                exceptions.add(transaction.getError());
            }
        }
        return exceptions.build();
    }
}
