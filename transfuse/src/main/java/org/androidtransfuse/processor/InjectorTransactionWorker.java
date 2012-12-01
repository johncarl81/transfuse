package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectorGenerator;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectorTransactionWorker implements TransactionWorker<Provider<ASTType>, JDefinedClass> {

    private boolean complete = false;
    private final InjectorGenerator injectorGenerator;

    @Inject
    public InjectorTransactionWorker(InjectorGenerator injectorGenerator) {
        this.injectorGenerator = injectorGenerator;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public JDefinedClass run(Provider<ASTType> value) {

        JDefinedClass definedClass = injectorGenerator.generate(value.get());

        complete = true;

        return definedClass;
    }

    @Override
    public Exception getError() {
        return null;
    }
}
