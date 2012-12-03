package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ParcelsTransactionFactory implements TransactionFactory<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private final Provider<TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>> workerProvider;
    private final EnterableScope scope;

    @Inject
    public ParcelsTransactionFactory(
            @Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope scope,
            @Named(TransfuseSetupGuiceModule.PARCELS_TRANSACTION_WORKER)
            Provider<TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>> workerProvider) {
        this.scope = scope;
        this.workerProvider = workerProvider;
    }

    @Override
    public Transaction<Map<Provider<ASTType>, JDefinedClass>, Void> buildTransaction(Map<Provider<ASTType>, JDefinedClass> value) {
        return new Transaction<Map<Provider<ASTType>, JDefinedClass>, Void>(value,
                new ScopedTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void>(scope, workerProvider));
    }
}
