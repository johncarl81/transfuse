package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectorsGenerator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectorsTransactionWorker implements TransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private final InjectorsGenerator injectorsGenerator;
    private boolean complete = false;

    @Inject
    public InjectorsTransactionWorker(InjectorsGenerator injectorsGenerator) {
        this.injectorsGenerator = injectorsGenerator;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Void run(Map<Provider<ASTType>, JDefinedClass> aggregate) {

        injectorsGenerator.generateInjectors(aggregate);

        complete = true;

        return null;
    }

    @Override
    public Exception getError() {
        return null;
    }
}
