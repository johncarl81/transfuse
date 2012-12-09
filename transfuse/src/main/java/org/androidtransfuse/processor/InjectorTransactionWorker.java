package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectorGenerator;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class InjectorTransactionWorker extends AbstractCompletionTransactionWorker<Provider<ASTType>, JDefinedClass> {

    private final InjectorGenerator injectorGenerator;

    @Inject
    public InjectorTransactionWorker(InjectorGenerator injectorGenerator) {
        this.injectorGenerator = injectorGenerator;
    }

    @Override
    public JDefinedClass innerRun(Provider<ASTType> value) {
        return injectorGenerator.generate(value.get());
    }
}
