package org.androidtransfuse.processor;

import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.ResourceCodeWriter;
import org.androidtransfuse.util.TransfuseRuntimeException;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class CodeGenerationScopedTransactionWorker<V, R> implements TransactionWorker<V, R> {

    private final JCodeModel codeModel;
    private final FilerSourceCodeWriter codeWriter;
    private final ResourceCodeWriter resourceWriter;
    private final TransactionWorker<V, R> worker;
    private boolean complete = false;

    public CodeGenerationScopedTransactionWorker(JCodeModel codeModel,
                                                 FilerSourceCodeWriter codeWriter,
                                                 ResourceCodeWriter resourceWriter,
                                                 TransactionWorker<V, R> worker) {
        this.codeModel = codeModel;
        this.codeWriter = codeWriter;
        this.resourceWriter = resourceWriter;
        this.worker = worker;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public R run(V value) {
        try {
            R result = worker.run(value);

            codeModel.build(codeWriter, resourceWriter);

            complete = true;

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
