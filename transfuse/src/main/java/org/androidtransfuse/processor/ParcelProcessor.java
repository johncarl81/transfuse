package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ParcelProcessor {

    private final TransactionProcessor<ASTType, JDefinedClass> transactionProcessor;
    private final ParcelTransactionFactory parcelTransactionFactory;

    @Inject
    public ParcelProcessor(@Named(TransfuseSetupGuiceModule.PARCELS_TRANSACTION_PROCESSOR)
                           TransactionProcessor<ASTType, JDefinedClass> transactionProcessor,
                           ParcelTransactionFactory parcelTransactionFactory) {
        this.transactionProcessor = transactionProcessor;
        this.parcelTransactionFactory = parcelTransactionFactory;
    }

    public void submit(Collection<? extends ASTType> parcels) {
        for (ASTType parcel : parcels) {
            transactionProcessor.submit(parcelTransactionFactory.buildTransaction(parcel));
        }
    }

    public void execute() {
        transactionProcessor.execute();
    }

    public void checkForErrors() {
        if (!transactionProcessor.isComplete()) {
            throw new TransfuseAnalysisException("@Parcel code generation did not complete successfully.");
        }
    }
}
