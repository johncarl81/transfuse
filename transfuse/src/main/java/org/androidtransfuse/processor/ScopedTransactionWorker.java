package org.androidtransfuse.processor;

import com.google.inject.Injector;
import com.sun.codemodel.JCodeModel;
import org.androidtransfuse.config.TransfuseInjector;
import org.androidtransfuse.gen.FilerSourceCodeWriter;
import org.androidtransfuse.gen.ResourceCodeWriter;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class ScopedTransactionWorker<T extends TransactionWorker<V, R>, V, R> implements TransactionWorker<V, R> {

    private final Class<T> scopedClass;
    private T scoped = null;
    private boolean complete = false;

    public ScopedTransactionWorker(Class<T> scopedClass) {
        this.scopedClass = scopedClass;
    }

    @Override
    public boolean isComplete() {
        if (complete && scoped != null) {
            return scoped.isComplete();
        }
        return complete;
    }

    @Override
    public R runScoped(V value) {

        try {
            JCodeModel codeModel = new JCodeModel();

            Injector injector = TransfuseInjector.getInstance().buildInjector(codeModel);

            scoped = injector.getInstance(scopedClass);

            R result = scoped.runScoped(value);

            FilerSourceCodeWriter codeWriter = injector.getInstance(FilerSourceCodeWriter.class);
            ResourceCodeWriter resourceWriter = injector.getInstance(ResourceCodeWriter.class);

            codeModel.build(codeWriter, resourceWriter);

            complete = true;
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransactionRuntimeException re) {
            //retry later
            complete = false;
        }
        return null;
    }
}
