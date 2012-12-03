package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectorProcessor implements TransactionProcessorBuilder<Provider<ASTType>, JDefinedClass> {

    private final TransactionProcessor processor;
    private final TransactionProcessorPool<Provider<ASTType>, JDefinedClass> parcelProcessor;
    private final TransactionFactory<Provider<ASTType>, JDefinedClass> injectorTransactionFactory;

    public InjectorProcessor(TransactionProcessor processor,
                             TransactionProcessorPool<Provider<ASTType>, JDefinedClass> parcelProcessor,
                             TransactionFactory<Provider<ASTType>, JDefinedClass> injectorTransactionFactory) {
        this.processor = processor;
        this.parcelProcessor = parcelProcessor;
        this.injectorTransactionFactory = injectorTransactionFactory;
    }

    public void submit(Provider<ASTType> parcel) {
        parcelProcessor.submit(injectorTransactionFactory.buildTransaction(parcel));
    }

    public void execute() {
        processor.execute();
    }

    public TransactionProcessor getTransactionProcessor() {
        return processor;
    }

    public void checkForErrors() {
        if (!processor.isComplete()) {
            Exception error = processor.getError();
            throw new TransfuseAnalysisException("@Parcel code generation did not complete successfully.", error);
        }
    }
}
