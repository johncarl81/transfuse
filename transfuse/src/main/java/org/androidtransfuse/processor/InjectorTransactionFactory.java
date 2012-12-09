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
public class InjectorTransactionFactory implements TransactionFactory<Provider<ASTType>, JDefinedClass> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider;

    @Inject
    public InjectorTransactionFactory(ScopedTransactionFactory scopedTransactionFactory,
                                      @Named(TransfuseSetupGuiceModule.INJECTOR_TRANSACTION_WORKER)
                                      Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        this.workerProvider = workerProvider;
    }

    public Transaction<Provider<ASTType>, JDefinedClass> buildTransaction(Provider<ASTType> astTypeProvider) {
        return scopedTransactionFactory.buildTransaction(astTypeProvider, workerProvider);
    }
}
