package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ParcelTransactionFactory implements TransactionFactory<Provider<ASTType>, JDefinedClass> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider;

    @Inject
    public ParcelTransactionFactory(ScopedTransactionFactory scopedTransactionFactory,
                                    @Named(TransfuseSetupGuiceModule.PARCEL_TRANSACTION_WORKER)
                                    Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        this.workerProvider = workerProvider;
    }

    public Transaction<Provider<ASTType>, JDefinedClass> buildTransaction(Provider<ASTType> parcel) {
        return scopedTransactionFactory.buildTransaction(parcel, workerProvider);
    }
}
