package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGeneration extends AbstractCompletionTransactionWorker<Provider<ASTType>, Void> {

    private final Provider<? extends Analysis<ComponentDescriptor>> analysis;
    private final Provider<ComponentGenerator> generatorProvider;

    public AnalysisGeneration(Provider<? extends Analysis<ComponentDescriptor>> analysis,
                              Provider<ComponentGenerator> generatorProvider) {
        this.analysis = analysis;
        this.generatorProvider = generatorProvider;
    }

    @Override
    public Void innerRun(Provider<ASTType> astTypeProvider) {

        ASTType astType = astTypeProvider.get();

        ComponentDescriptor descriptor = analysis.get().analyze(astType);

        generatorProvider.get().generate(descriptor);
        return null;
    }
}
