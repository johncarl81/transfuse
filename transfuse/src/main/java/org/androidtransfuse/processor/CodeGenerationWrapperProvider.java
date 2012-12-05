package org.androidtransfuse.processor;

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class CodeGenerationWrapperProvider<V, R> implements Provider<TransactionWorker<V, R>> {

    private final Provider<JCodeModel> codeModelProvider;
    private final Provider<FilerSourceCodeWriter> sourceCodeWriterProvider;
    private final Provider<FilerResourceWriter> resourceCodeWriterProvider;
    private final Provider<? extends TransactionWorker<V, R>> workerProvider;

    public CodeGenerationWrapperProvider(Provider<? extends TransactionWorker<V, R>> workerProvider,
                                         Provider<JCodeModel> codeModelProvider,
                                         Provider<FilerSourceCodeWriter> sourceCodeWriterProvider,
                                         Provider<FilerResourceWriter> resourceCodeWriterProvider) {
        this.codeModelProvider = codeModelProvider;
        this.sourceCodeWriterProvider = sourceCodeWriterProvider;
        this.resourceCodeWriterProvider = resourceCodeWriterProvider;
        this.workerProvider = workerProvider;
    }

    @Override
    public TransactionWorker<V, R> get() {
        return new CodeGenerationScopedTransactionWorker<V, R>(
                codeModelProvider.get(), sourceCodeWriterProvider.get(), resourceCodeWriterProvider.get(), workerProvider.get());
    }
}
