package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ParcelProcessor {

    private final TransactionProcessor<Provider<ASTType>, JDefinedClass> transactionProcessor;
    private final ParcelTransactionFactory parcelTransactionFactory;

    @Inject
    public ParcelProcessor(@Named(TransfuseSetupGuiceModule.PARCELS_TRANSACTION_PROCESSOR)
                           TransactionProcessor<Provider<ASTType>, JDefinedClass> transactionProcessor,
                           ParcelTransactionFactory parcelTransactionFactory) {
        this.transactionProcessor = transactionProcessor;
        this.parcelTransactionFactory = parcelTransactionFactory;
    }

    public void submit(Collection<Provider<ASTType>> parcels) {
        for (Provider<ASTType> parcel : parcels) {
            transactionProcessor.submit(parcelTransactionFactory.buildTransaction(parcel));
        }
    }

    public void execute() {
        transactionProcessor.execute();
    }

    public void checkForErrors() {
        if (!transactionProcessor.isComplete()) {
            Exception error = transactionProcessor.getError();
            throw new TransfuseAnalysisException("@Parcel code generation did not complete successfully.", error);
        }
    }
}
