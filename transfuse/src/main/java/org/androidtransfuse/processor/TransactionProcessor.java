package org.androidtransfuse.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class TransactionProcessor<V, R> {

    private List<Transaction<V, R>> transactions = new ArrayList<Transaction<V, R>>();
    private TransactionWorker<Map<V, R>, Void> aggregateWorker;

    public TransactionProcessor(TransactionWorker<Map<V, R>, Void> aggregateWorker) {
        this.aggregateWorker = aggregateWorker;
    }

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

        executorService.shutdown();

        try {
            while (!executorService.awaitTermination(100, TimeUnit.MILLISECONDS)) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<V, R> aggregate = new HashMap<V, R>();
        boolean complete = true;

        for (Transaction<V, R> transaction : transactions) {
            if (transaction.isComplete()) {
                aggregate.put(transaction.getValue(), transaction.getResult());
            } else {
                complete = false;
                break;
            }
        }

        if (complete && !aggregateWorker.isComplete()) {
            aggregateWorker.runScoped(aggregate);
        }
    }

    /**
     * Returns the completion status of the set of transactions.  Will only return complete = true if all the transactions
     * have completed successfully.
     *
     * @return transaction completion status
     */
    public boolean isComplete() {
        boolean complete = true;

        for (Transaction<V, R> transaction : transactions) {
            complete = complete && transaction.isComplete();
        }

        return complete;
    }
}
