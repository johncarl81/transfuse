package org.androidtransfuse.processor;

import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class PackageHelperTransactionFactory implements TransactionFactory<Void, Void> {

    private final EnterableScope codeGenerationScope;
    private final Provider<TransactionWorker<Void, Void>> packageHelperGeneratorProvider;

    @Inject
    public PackageHelperTransactionFactory(
            @Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope codeGenerationScope,
            @Named(TransfuseSetupGuiceModule.PACKAGE_HELPER_TRANSACTION_WORKER)
            Provider<TransactionWorker<Void, Void>> packageHelperGeneratorProvider) {
        this.packageHelperGeneratorProvider = packageHelperGeneratorProvider;
        this.codeGenerationScope = codeGenerationScope;
    }

    @Override
    public Transaction<Void, Void> buildTransaction(Void value) {
        return new Transaction<Void, Void>(
                new ScopedTransactionWorker<Void, Void>(codeGenerationScope, packageHelperGeneratorProvider)
        );
    }
}
