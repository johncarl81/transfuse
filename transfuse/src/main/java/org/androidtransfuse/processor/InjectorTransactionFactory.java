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
public class InjectorTransactionFactory {

    private final EnterableScope scope;
    private final Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider;

    @Inject
    public InjectorTransactionFactory(@Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope scope,
                                      @Named(TransfuseSetupGuiceModule.INJECTOR_TRANSACTION_WORKER)
                                      Provider<TransactionWorker<Provider<ASTType>, JDefinedClass>> workerProvider) {
        this.scope = scope;
        this.workerProvider = workerProvider;
    }

    public Transaction<Provider<ASTType>, JDefinedClass> buildTransaction(Provider<ASTType> astTypeProvider) {
        return new Transaction<Provider<ASTType>, JDefinedClass>(astTypeProvider,
                new ScopedTransactionWorker<Provider<ASTType>, JDefinedClass>(scope, workerProvider));
    }
}
