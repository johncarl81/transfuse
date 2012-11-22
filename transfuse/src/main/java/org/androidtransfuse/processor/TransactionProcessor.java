package org.androidtransfuse.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class TransactionProcessor<V, R> {

    private List<Transaction<V, R>> transactions = new ArrayList<Transaction<V, R>>();
    private TransactionWorker<Map<V, R>, Void> aggregateWorker;

    public TransactionProcessor(TransactionWorker<Map<V, R>, Void> aggregateWorker) {
        this.aggregateWorker = aggregateWorker;
    }

    public void submit(Transaction<V, R> transaction) {
        transactions.add(transaction);
    }

    public void execute() {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

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

    public boolean isComplete() {
        boolean complete = true;

        for (Transaction<V, R> transaction : transactions) {
            complete = complete && transaction.isComplete();
        }

        return complete;
    }
}
