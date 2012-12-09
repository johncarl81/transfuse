package org.androidtransfuse.processor;

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.gen.FilerResourceWriter;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.util.TransfuseRuntimeException;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class CodeGenerationScopedTransactionWorker<V, R> extends AbstractCompletionTransactionWorker<V, R> {

    private final JCodeModel codeModel;
    private final FilerSourceCodeWriter codeWriter;
    private final FilerResourceWriter resourceWriter;
    private final TransactionWorker<V, R> worker;

    public CodeGenerationScopedTransactionWorker(JCodeModel codeModel,
                                                 FilerSourceCodeWriter codeWriter,
                                                 FilerResourceWriter resourceWriter,
                                                 TransactionWorker<V, R> worker) {
        this.codeModel = codeModel;
        this.codeWriter = codeWriter;
        this.resourceWriter = resourceWriter;
        this.worker = worker;
    }

    @Override
    public R innerRun(V value) {
        try {
            R result = worker.run(value);

            codeModel.build(codeWriter, resourceWriter);

            return result;
        } catch (IOException e) {
            throw new TransfuseRuntimeException("Unable to perform code generation", e);
        }
    }

    @Override
    public Exception getError() {
        return null;
    }
}
