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
public class InjectorsTransactionWorker extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private final InjectorsGenerator injectorsGenerator;

    @Inject
    public InjectorsTransactionWorker(InjectorsGenerator injectorsGenerator) {
        this.injectorsGenerator = injectorsGenerator;
    }

    @Override
    public Void innerRun(Map<Provider<ASTType>, JDefinedClass> aggregate) {
        injectorsGenerator.generateInjectors(aggregate);
        return null;
    }
}
