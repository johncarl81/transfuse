package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.TransactionWorker;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGeneration implements TransactionWorker<Provider<ASTType>, Void> {

    private final Provider<? extends Analysis<ComponentDescriptor>> analysis;
    private final Provider<ComponentGenerator> generatorProvider;
    private boolean complete = false;

    public AnalysisGeneration(Provider<? extends Analysis<ComponentDescriptor>> analysis,
                              Provider<ComponentGenerator> generatorProvider) {
        this.analysis = analysis;
        this.generatorProvider = generatorProvider;
    }

    @Override
    public Void run(Provider<ASTType> astTypeProvider) {

        ASTType astType = astTypeProvider.get();

        ComponentDescriptor descriptor = analysis.get().analyze(astType);

        generatorProvider.get().generate(descriptor);

        complete = true;
        return null;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public Exception getError() {
        return null;
    }
}
