package org.androidtransfuse.gen;

import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.TransactionWorker;
import org.androidtransfuse.processor.WorkerProvider;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGenerationFactory {

    @Inject
    private Provider<ComponentGenerator> componentGeneratorProvider;

    public WorkerProvider buildAnalysisGenerationProvider(Provider<? extends Analysis<ComponentDescriptor>> analysis) {
        return new AnalysisGenerationProvider(analysis, componentGeneratorProvider);
    }

    private static final class AnalysisGenerationProvider implements WorkerProvider {

        private Provider<? extends Analysis<ComponentDescriptor>> analysis;
        private Provider<ComponentGenerator> generator;

        private AnalysisGenerationProvider(Provider<? extends Analysis<ComponentDescriptor>> analysis, Provider<ComponentGenerator> generator) {
            this.analysis = analysis;
            this.generator = generator;
        }

        @Override
        public TransactionWorker<Provider<ASTType>, Void> get() {
            return new AnalysisGeneration(analysis, generator);
        }
    }
}
