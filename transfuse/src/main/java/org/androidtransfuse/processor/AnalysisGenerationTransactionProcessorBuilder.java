package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.config.TransfuseSetupGuiceModule;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGenerationTransactionProcessorBuilder implements TransactionProcessorBuilder<Provider<ASTType>, Void> {

    private final TransactionProcessorPool<Provider<ASTType>, Void> transactionProcessor;
    private final EnterableScope codeGenerationScope;
    private final CodeGenerationWrapperProvider<Provider<ASTType>, Void> workerProvider;

    @Inject
    public AnalysisGenerationTransactionProcessorBuilder(
            @Assisted WorkerProvider workerProvider,
            @Named(TransfuseSetupGuiceModule.CODE_GENERATION_SCOPE) EnterableScope codeGenerationScope,
            Provider<JCodeModel> codeModelProvider,
            Provider<FilerSourceCodeWriter> sourceCodeWriterProvider,
            Provider<FilerResourceWriter> resourceCodeWriterProvider) {
        transactionProcessor = new TransactionProcessorPool<Provider<ASTType>, Void>();
        this.workerProvider = new CodeGenerationWrapperProvider<Provider<ASTType>, Void>(workerProvider, codeModelProvider, sourceCodeWriterProvider, resourceCodeWriterProvider);
        this.codeGenerationScope = codeGenerationScope;
    }

    @Override
    public void submit(Provider<ASTType> astTypeProvider) {
        transactionProcessor.submit(new Transaction<Provider<ASTType>, Void>(astTypeProvider,
                new ScopedTransactionWorker<Provider<ASTType>, Void>(codeGenerationScope, workerProvider)));
    }

    @Override
    public TransactionProcessor getTransactionProcessor() {
        return transactionProcessor;
    }
}
