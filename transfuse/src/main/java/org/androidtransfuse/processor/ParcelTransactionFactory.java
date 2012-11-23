package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ParcelTransactionFactory {

    private final EnterableScope scope;
    private final Provider<TransactionWorker<ASTType, JDefinedClass>> workerProvider;

    @Inject
    public ParcelTransactionFactory(@Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope scope,
                                    @Named(TransfuseSetupGuiceModule.PARCEL_TRANSACTION_WORKER)
                                    Provider<TransactionWorker<ASTType, JDefinedClass>> workerProvider) {
        this.scope = scope;
        this.workerProvider = workerProvider;
    }

    public Transaction<ASTType, JDefinedClass> buildTransaction(ASTType parcel) {
        return new ScopedTransaction<TransactionWorker<ASTType, JDefinedClass>, ASTType, JDefinedClass>(parcel,
                new ScopedTransactionWorker<TransactionWorker<ASTType, JDefinedClass>, ASTType, JDefinedClass>(scope, workerProvider));
    }
}
