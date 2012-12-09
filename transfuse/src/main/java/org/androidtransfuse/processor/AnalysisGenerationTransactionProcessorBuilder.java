package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGenerationTransactionProcessorBuilder implements TransactionProcessorBuilder<Provider<ASTType>, Void> {

    private final ScopedTransactionFactory scopedTransactionFactory;
    private final TransactionProcessorPool<Provider<ASTType>, Void> transactionProcessor;
    private final CodeGenerationWrapperProvider<Provider<ASTType>, Void> workerProvider;

    @Inject
    public AnalysisGenerationTransactionProcessorBuilder(
            @Assisted WorkerProvider workerProvider,
            Provider<JCodeModel> codeModelProvider,
            Provider<FilerSourceCodeWriter> sourceCodeWriterProvider,
            Provider<FilerResourceWriter> resourceCodeWriterProvider,
            ScopedTransactionFactory scopedTransactionFactory) {
        this.scopedTransactionFactory = scopedTransactionFactory;
        transactionProcessor = new TransactionProcessorPool<Provider<ASTType>, Void>();
        this.workerProvider = new CodeGenerationWrapperProvider<Provider<ASTType>, Void>(workerProvider, codeModelProvider, sourceCodeWriterProvider, resourceCodeWriterProvider);
    }

    @Override
    public void submit(Provider<ASTType> astTypeProvider) {
        transactionProcessor.submit(scopedTransactionFactory.buildTransaction(astTypeProvider, workerProvider));
    }

    @Override
    public TransactionProcessor getTransactionProcessor() {
        return transactionProcessor;
    }
}
