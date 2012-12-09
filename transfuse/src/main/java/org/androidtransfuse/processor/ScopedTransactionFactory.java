package org.androidtransfuse.processor;

import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ScopedTransactionFactory {

    private final EnterableScope codeGenerationScope;

    @Inject
    public ScopedTransactionFactory(@Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope codeGenerationScope) {
        this.codeGenerationScope = codeGenerationScope;
    }

    public <V, R> Transaction<V, R> buildTransaction(V value, Provider<TransactionWorker<V, R>> workerProvider) {
        return new Transaction<V, R>(value, new ScopedTransactionWorker<V, R>(codeGenerationScope, workerProvider));
    }
}
