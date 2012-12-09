package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Provider;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class ParcelProcessor {

    private final TransactionProcessor processor;
    private final TransactionProcessorPool<Provider<ASTType>, JDefinedClass> parcelProcessor;
    private final ParcelTransactionFactory parcelTransactionFactory;

    public ParcelProcessor(TransactionProcessor processor,
                           TransactionProcessorPool<Provider<ASTType>, JDefinedClass> parcelProcessor,
                           ParcelTransactionFactory parcelTransactionFactory) {
        this.processor = processor;
        this.parcelProcessor = parcelProcessor;
        this.parcelTransactionFactory = parcelTransactionFactory;
    }

    public void submit(Collection<Provider<ASTType>> parcels) {
        for (Provider<ASTType> parcel : parcels) {
            parcelProcessor.submit(parcelTransactionFactory.buildTransaction(parcel));
        }
    }

    public void execute() {
        processor.execute();
    }

    public void checkForErrors() {
        if (!processor.isComplete()) {
            throw new TransfuseAnalysisException("@Parcel code generation did not complete successfully.", processor.getErrors());
        }
    }
}
